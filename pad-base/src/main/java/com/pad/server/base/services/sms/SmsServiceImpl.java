package com.pad.server.base.services.sms;

import java.io.ByteArrayOutputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.MessageFormatterUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Sms;
import com.pad.server.base.entities.SmsConfig;
import com.pad.server.base.entities.SmsTemplate;
import com.pad.server.base.exceptions.PADException;

@Service
@Transactional
public class SmsServiceImpl implements SmsService {

    private static final Logger                      logger                     = Logger.getLogger(SmsServiceImpl.class);
    private final ConcurrentMap<Long, SmsConfig>     smsConfigMap               = new ConcurrentHashMap<>();
    private boolean                                  isLive                     = false;
    private List<SmsConfig>                          smsConfigList              = new CopyOnWriteArrayList<>();
    private List<SmsTemplate>                        smsTemplateList            = new CopyOnWriteArrayList<>();
    private final ConcurrentMap<Long, SmsTemplate>   smsTemplateMap             = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, SmsTemplate> smsTypeLanguageTemplateMap = new ConcurrentHashMap<>();

    @Autowired
    private JdbcTemplate                             jdbcTemplate;

    @Autowired
    private HibernateTemplate                        hibernateTemplate;

    @Value("${system.environment}")
    private String                                   systemEnvironment;

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {

        isLive = ServerConstants.SYSTEM_ENVIRONMENT_PROD.equals(systemEnvironment);

        smsConfigList = (List<SmsConfig>) hibernateTemplate.find("FROM SmsConfig");
        if (smsConfigList != null && !smsConfigList.isEmpty()) {
            for (SmsConfig smsConfig : smsConfigList) {

                logger.info("init###" + smsConfig.toString());
                smsConfigMap.put(smsConfig.getId(), smsConfig);
            }
        }

        smsTemplateList = (List<SmsTemplate>) hibernateTemplate.find("FROM SmsTemplate");
        if (smsTemplateList != null && !smsTemplateList.isEmpty()) {
            for (SmsTemplate smsTemplate : smsTemplateList) {

                logger.info("init###" + smsTemplate.toString());
                smsTemplateMap.put(smsTemplate.getId(), smsTemplate);

                String key = smsTemplate.getType() + "#" + smsTemplate.getLanguageId();
                smsTypeLanguageTemplateMap.put(key, smsTemplate);
            }
        }
    }

    @Override
    public void sendBulkSms(Sms sms) throws PADException {

        try {
            sms.setTransactionId(ServerConstants.DEFAULT_LONG);
            sms.setResponseCode(ServerConstants.DEFAULT_INT);
            SmsConfig smsConfig = smsConfigMap.get(sms.getConfigId());

            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("username", smsConfig.getUsername()));
            nameValuePairs.add(new BasicNameValuePair("password", smsConfig.getPassword()));
            nameValuePairs.add(new BasicNameValuePair("destinationAddress", sms.getMsisdn()));
            nameValuePairs.add(new BasicNameValuePair("header", sms.getSourceAddr()));
            nameValuePairs.add(new BasicNameValuePair("message", sms.getMessage()));

            String responseText = "";
            if (isLive) {
                responseText = callAllPointsServlet(sms.getId(), smsConfig.getUrl(), nameValuePairs);
            } else {
                // accountService.getAccountById(sms.getAccountId()); //will not save the result. unnecessary call;
                if (ServerConstants.PLUG_TEST_MSISDN.equals(sms.getMsisdn())) { // testMobileNumber
                    responseText = "requestId=1000, responseCode=0, responseText=Success.";
                } else {
                    responseText = callAllPointsServlet(sms.getId(), smsConfig.getUrl(), nameValuePairs);
                }
            }
            logger.info("sendBulkSms#smsId=" + sms.getId() + "#Response: [" + responseText + "]");

            if (responseText == null)
                throw new PADException(ServerResponseConstants.EXTERNAL_API_CONNECTION_FAILURE_CODE, ServerResponseConstants.EXTERNAL_API_CONNECTION_FAILURE_TEXT, "sendBulkSms");
            else {
                String responseParameters[] = responseText.split(",");
                sms.setTransactionId(Long.parseLong(responseParameters[0].substring("requestId=".length())));
                sms.setResponseCode(Integer.parseInt(responseParameters[1].substring(" responseCode=".length())));
                sms.setResponseText(responseParameters[2].substring(" responseText=".length()));

                if (sms.getResponseCode() != ServerResponseConstants.SUCCESS_CODE)
                    throw new PADException(sms.getResponseCode(), sms.getResponseText(), "sendBulkSms");
            }
        } catch (PADException rbse) {
            throw rbse;
        } catch (Exception e) {
            logger.error("sendBulkSms###Exception: ", e);
            throw new PADException(ServerResponseConstants.EXTERNAL_API_CONNECTION_FAILURE_CODE, e.getClass() + "###" + e.getMessage(), "sendBulkSms###Exception");
        }
    }

    private String callAllPointsServlet(long smsId, String url, List<NameValuePair> nameValuePairs) throws PADException {

        String responseSource = "callAllPointsServlet#";

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String responseText = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            httpClient = HttpClients.createDefault();

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).setConnectionRequestTimeout(30000).build();
            httpPost.setConfig(requestConfig);

            httpResponse = httpClient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                HttpEntity httpResponseEntity = httpResponse.getEntity();
                if (httpResponseEntity != null) {
                    ByteArrayOutputStream httpResponseByteArrayOutputStream = new ByteArrayOutputStream();
                    httpResponseEntity.writeTo(httpResponseByteArrayOutputStream);
                    responseText = httpResponseByteArrayOutputStream.toString();
                }
            } else {

                logger.info(responseSource + "smsId=" + smsId + "#Response: [StatusCode=" + httpResponse.getStatusLine().getStatusCode() + ", ReasonPhrase="
                    + httpResponse.getStatusLine().getReasonPhrase() + "]");
                throw new PADException(ServerResponseConstants.EXTERNAL_API_CONNECTION_FAILURE_CODE, responseSource + "Response: [StatusCode="
                    + httpResponse.getStatusLine().getStatusCode() + ", ReasonPhrase=" + httpResponse.getStatusLine().getReasonPhrase() + "]", responseSource);
            }
        } catch (PADException rbse) {
            throw rbse;
        } catch (Exception e) {
            logger.error(responseSource + "smsId=" + smsId + "###Exception: ", e);
            throw new PADException(ServerResponseConstants.EXTERNAL_API_CONNECTION_FAILURE_CODE, e.getClass() + "###" + e.getMessage(), responseSource + "##Exception");
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }

                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (Exception e) {
                logger.error(responseSource + "smsId=" + smsId + "###finally#Exception: ", e);
            }
        }

        return responseText;
    }

    @Override
    public void scheduleSmsById(Sms sms, long templateId, HashMap<String, Object> params) throws PADException {

        SmsTemplate template = smsTemplateMap.get(templateId);

        sms.setType(template.getType());
        sms.setConfigId(template.getConfigId());
        sms.setTemplateId(template.getId());
        sms.setPriority(template.getPriority());

        if (isLive) {
            sms.setSourceAddr(template.getSourceAddr());
        } else {
            sms.setSourceAddr("TEST - " + template.getSourceAddr());
        }

        sms.setMessage(MessageFormatterUtil.formatText(template.getMessage(), params));
        sms.setChannel(ServerConstants.CHANNEL_SYSTEM);
        sms.setDateScheduled(sms.getDateScheduled() == null ? new Date() : sms.getDateScheduled());

        scheduleSms(sms);
    }

    @Override
    public void scheduleSmsByType(Sms sms, long templateType, HashMap<String, Object> params) throws PADException {

        SmsTemplate template = smsTypeLanguageTemplateMap.get(templateType + "#" + sms.getLanguageId());

        sms.setType(template.getType());
        sms.setConfigId(template.getConfigId());
        sms.setTemplateId(template.getId());
        sms.setPriority(template.getPriority());

        if (isLive) {
            sms.setSourceAddr(template.getSourceAddr());
        } else {
            sms.setSourceAddr("TEST - " + template.getSourceAddr());
        }

        HashMap<String, Object> templateBody = new HashMap<>();
        templateBody.put("templateBody", MessageFormatterUtil.formatText(template.getMessage(), params));

        sms.setMessage(MessageFormatterUtil.formatText(template.getMessage(), params));
        sms.setChannel(ServerConstants.CHANNEL_SYSTEM);
        sms.setDateScheduled(new Date());

        scheduleSms(sms);
    }

    private void scheduleSms(Sms sms) {

        sms.setIsProcessed(ServerConstants.PROCESS_NOTPROCESSED);
        sms.setDateCreated(new Date());
        sms.setRetryCount(0);
        sms.setTransactionId(ServerConstants.DEFAULT_LONG);
        sms.setResponseCode(ServerConstants.DEFAULT_INT);

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource()).withTableName("sms_log").usingGeneratedKeyColumns("id");

        if (sms.getLanguageId() != ServerConstants.LANGUAGE_EN_ID) {

            // replace any letter accents with its regular ASCII equivalent (e.g. é à becomes e a). This is to avoid encoding issues with sending SMS
            String normalizedMessage = Normalizer.normalize(sms.getMessage(), Normalizer.Form.NFD);

            sms.setMessage(normalizedMessage.replaceAll("[^\\p{ASCII}]", ""));
        }

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("is_processed", sms.getIsProcessed());
        parameters.addValue("type", sms.getType());
        parameters.addValue("config_id", sms.getConfigId());
        parameters.addValue("language_id", sms.getLanguageId());
        parameters.addValue("account_id", sms.getAccountId());
        parameters.addValue("mission_id", sms.getMissionId());
        parameters.addValue("trip_id", sms.getTripId());
        parameters.addValue("template_id", sms.getTemplateId());
        parameters.addValue("priority", sms.getPriority());
        parameters.addValue("msisdn", sms.getMsisdn());
        parameters.addValue("source_addr", sms.getSourceAddr());
        parameters.addValue("message", sms.getMessage());
        parameters.addValue("channel", sms.getChannel());
        parameters.addValue("date_created", sms.getDateCreated());
        parameters.addValue("date_scheduled", sms.getDateScheduled());
        parameters.addValue("retry_count", sms.getRetryCount());
        parameters.addValue("transaction_id", sms.getTransactionId());
        parameters.addValue("response_code", sms.getResponseCode());

        sms.setId(jdbcInsert.executeAndReturnKey(parameters).longValue());

        if (sms.getMsisdn().equals("221773563777")) {
            // ignore sending SMS for this number that is used for testing
            sms.setIsProcessed(ServerConstants.PROCESS_CANCELLED);
            sms.setDateProcessed(new Date());
            sms.setResponseText("");

            updateSms(sms);

        } else {
            jdbcTemplate.update(
                "INSERT INTO sms_scheduler(id, is_processed, type, config_id, language_id, account_id, mission_id, trip_id, template_id, priority, msisdn, source_addr, message, channel, "
                    + "date_created, date_scheduled, retry_count, date_processed, transaction_id, response_code, response_text) SELECT id, is_processed, type, config_id, language_id, account_id, mission_id, trip_id, template_id, priority, "
                    + "msisdn, source_addr, message, channel, date_created, date_scheduled, retry_count, date_processed, transaction_id, response_code, response_text FROM sms_log WHERE id = ?",
                sms.getId());
        }
    }

    @Override
    public void updateScheduledSms(Sms sms) {

        sms.setDateProcessed(new Date());
        jdbcTemplate.update(
            "UPDATE sms_scheduler SET is_processed = ?, date_scheduled = ?, retry_count = ?, date_processed = ?, transaction_id = ?, response_code = ?, response_text = ? WHERE id = ?",
            sms.getIsProcessed(), sms.getDateScheduled(), sms.getRetryCount(), sms.getDateProcessed(), sms.getTransactionId(), sms.getResponseCode(), ServerUtil.restrictLength(
                sms.getResponseText(), 256), sms.getId());
    }

    @Override
    public void deleteScheduledSms(long smsId) {
        jdbcTemplate.update("DELETE FROM sms_scheduler WHERE id = ?", smsId);
    }

    @Override
    public void updateSms(Sms sms) {

        sms.setDateProcessed(new Date());
        jdbcTemplate.update(
            "UPDATE sms_log SET is_processed = ?, date_scheduled = ?, retry_count = ?, date_processed = ?, transaction_id = ?, response_code = ?, response_text = ? WHERE id = ?",
            sms.getIsProcessed(), sms.getDateScheduled(), sms.getRetryCount(), sms.getDateProcessed(), sms.getTransactionId(), sms.getResponseCode(), ServerUtil.restrictLength(
                sms.getResponseText(), 256), sms.getId());
    }

    @Override
    public boolean isParkingExitSmsEligibleToSend(long tripId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM sms_log WHERE trip_id = ? AND TIME_TO_SEC(timediff(now(), date_created)) < ? ORDER BY id DESC", new Object[] {
            tripId, ServerConstants.EXIT_PARKING_SMS_TIME_LIMIT_SECONDS }, Long.class) == 0l;
    }

    @Override
    public String getParkingExitSMSMessage(long languageId) {
        return smsTypeLanguageTemplateMap.get(ServerConstants.SMS_PARKING_EXIT_TEMPLATE_TYPE + "#" + ServerConstants.DEFAULT_LONG + "#" + languageId).getMessage();
    }

}
