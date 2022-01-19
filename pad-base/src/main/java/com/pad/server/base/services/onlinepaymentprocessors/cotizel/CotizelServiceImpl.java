package com.pad.server.base.services.onlinepaymentprocessors.cotizel;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.entities.OnlinePayment;
import com.pad.server.base.entities.OnlinePaymentParameter;
import com.pad.server.base.entities.Payment;
import com.pad.server.base.exceptions.PADOnlinePaymentException;
import com.pad.server.base.jsonentities.api.onlinepayment.cotizel.PaymentRequestJson;
import com.pad.server.base.jsonentities.api.onlinepayment.cotizel.PaymentResponseJson;
import com.pad.server.base.services.onlinepayment.OnlinePaymentService;
import com.pad.server.base.services.payment.PaymentService;

@Service
@Transactional
public class CotizelServiceImpl implements CotizelService {

    private static final Logger  logger = Logger.getLogger(CotizelServiceImpl.class);

    @Autowired
    private OnlinePaymentService onlinePaymentService;

    @Autowired
    private PaymentService       paymentService;

    @Value("${system.url}")
    private String               systemUrl;

    @Value("${cotizel.sn.url}")
    private String               cotizelSnUrl;

    @Value("${cotizel.sn.access.login}")
    private String               cotizelSnAccessLogin;

    @Value("${cotizel.sn.access.password}")
    private String               cotizelSnAccessPassword;

    @Value("${cotizel.sn.access.token}")
    private String               cotizelSnAccessToken;

    @Value("${cotizel.sn.callback.url.endpoint}")
    private String               cotizelSnCallbackUrlEndpoint;

    @Value("${cotizel.sn.groupe}")
    private String               cotizelSnGroupe;

    private ObjectMapper         mapper;

    @Override
    public void processOnlinePayment(OnlinePayment onlinePayment) throws PADOnlinePaymentException {

        try {
            PaymentRequestJson paymentRequest = createPaymentRequestRequest(onlinePayment);

            onlinePayment.setRequestHash(paymentRequest.getHashcode());
            onlinePaymentService.updateOnlinePayment(onlinePayment);

            PaymentResponseJson paymentResponse = processPayment(paymentRequest);

            if (StringUtils.isBlank(paymentResponse.getStatus())) {

                String errorMsg = StringUtils.isBlank(paymentResponse.getError()) ? "" : paymentResponse.getError() + "#";

                if (!StringUtils.isBlank(paymentResponse.getError300())) {

                    errorMsg += paymentResponse.getError300();
                    throw new PADOnlinePaymentException(ServerResponseConstants.EXTERNAL_API_300_ERROR_CODE, errorMsg, "");

                } else if (!StringUtils.isBlank(paymentResponse.getError301())) {

                    errorMsg += paymentResponse.getError301();
                    throw new PADOnlinePaymentException(ServerResponseConstants.EXTERNAL_API_301_ERROR_CODE, errorMsg, "");

                } else if (!StringUtils.isBlank(paymentResponse.getError302())) {

                    errorMsg += paymentResponse.getError302();
                    throw new PADOnlinePaymentException(ServerResponseConstants.EXTERNAL_API_302_ERROR_CODE, errorMsg, "");

                } else if (!StringUtils.isBlank(paymentResponse.getError303())) {

                    errorMsg += paymentResponse.getError303();
                    throw new PADOnlinePaymentException(ServerResponseConstants.EXTERNAL_API_303_ERROR_CODE, errorMsg, "");

                } else if (!StringUtils.isBlank(paymentResponse.getError304())) {

                    errorMsg += paymentResponse.getError304();
                    throw new PADOnlinePaymentException(ServerResponseConstants.EXTERNAL_API_304_ERROR_CODE, errorMsg, "");

                } else if (!StringUtils.isBlank(paymentResponse.getError400())) {

                    errorMsg += paymentResponse.getError400();
                    throw new PADOnlinePaymentException(ServerResponseConstants.EXTERNAL_API_400_ERROR_CODE, errorMsg, "");

                } else if (!StringUtils.isBlank(paymentResponse.getError401())) {

                    errorMsg += paymentResponse.getError401();
                    throw new PADOnlinePaymentException(ServerResponseConstants.EXTERNAL_API_401_ERROR_CODE, errorMsg, "");

                } else if (!StringUtils.isBlank(paymentResponse.getError402())) {

                    errorMsg += paymentResponse.getError402();
                    throw new PADOnlinePaymentException(ServerResponseConstants.EXTERNAL_API_402_ERROR_CODE, errorMsg, "");

                } else if (!StringUtils.isBlank(paymentResponse.getError403())) {

                    errorMsg += paymentResponse.getError403();
                    throw new PADOnlinePaymentException(ServerResponseConstants.EXTERNAL_API_403_ERROR_CODE, errorMsg, "");

                } else if (!StringUtils.isBlank(paymentResponse.getError405())) {

                    errorMsg += paymentResponse.getError405();
                    throw new PADOnlinePaymentException(ServerResponseConstants.EXTERNAL_API_405_ERROR_CODE, errorMsg, "");

                } else
                    throw new PADOnlinePaymentException(ServerResponseConstants.EXTERNAL_API_ERROR_CODE, errorMsg, "");

            } else {
                onlinePayment.setClientIdAggregator(paymentResponse.getIdFromClient());
                onlinePayment.setGuIdAggregator(paymentResponse.getIdFromGU());
                onlinePayment.setAmountAggregator(new BigDecimal(paymentResponse.getAmount()));
                onlinePayment.setFeeAggregator(new BigDecimal(paymentResponse.getFees()));
                onlinePayment.setDateResponse(new Date());

                final LocalDateTime localDateTimeResponse = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(paymentResponse.getDateTime())), ZoneId.systemDefault());
                final Date dateTimeResponse = Date.from(localDateTimeResponse.atZone(ZoneId.systemDefault()).toInstant());

                onlinePayment.setDateResponseAggregator(dateTimeResponse);
                onlinePayment.setStatusAggregator(paymentResponse.getStatus());
                onlinePayment.setReferenceAggregator(StringUtils.isBlank(paymentResponse.getReference()) ? "" : paymentResponse.getReference());
                onlinePayment.setResponseHash(StringUtils.isBlank(paymentResponse.getHashcode()) ? "" : paymentResponse.getHashcode());
                onlinePayment.setResponseCode(paymentResponse.getStatus().equalsIgnoreCase("INITIATED") ? ServerResponseConstants.MNO_RESPONSE_CODE_INITIATED
                    : ServerResponseConstants.MNO_RESPONSE_CODE_FAILURE);

                Payment payment = paymentService.getPaymentById(onlinePayment.getPaymentId());

                if (payment != null) {
                    payment.setResponseCode(onlinePayment.getResponseCode());
                    payment.setDateResponse(onlinePayment.getDateResponse());

                    paymentService.updatePayment(payment);
                }

                onlinePaymentService.updateOnlinePayment(onlinePayment);
            }

        } catch (PADOnlinePaymentException pade) {

            if (pade.getResponseCode() == ServerResponseConstants.EXTERNAL_API_SOCKET_TIMEOUT_CODE) {
                onlinePayment.setErrorAggregator(ServerConstants.DEFAULT_STRING);
                onlinePayment.setStatusAggregator("PENDING");
            } else {
                onlinePayment.setErrorAggregator(pade.getResponseText().length() > 128 ? pade.getResponseText().substring(0, 127) : pade.getResponseText());
            }

            onlinePayment.setResponseCode(pade.getResponseCode());
            onlinePayment.setDateResponse(new Date());

            Payment payment = paymentService.getPaymentById(onlinePayment.getPaymentId());

            if (payment != null) {
                payment.setResponseCode(onlinePayment.getResponseCode());
                payment.setDateResponse(onlinePayment.getDateResponse());

                paymentService.updatePayment(payment);
            }

            onlinePaymentService.updateOnlinePayment(onlinePayment);

            throw pade;
        }
    }

    private PaymentRequestJson createPaymentRequestRequest(OnlinePayment onlinePayment) {

        return new PaymentRequestJson(systemUrl + cotizelSnCallbackUrlEndpoint, cotizelSnGroupe, cotizelSnAccessLogin, onlinePayment.getAmount().toString(),
            onlinePayment.getFirstName(), onlinePayment.getMnoName(), cotizelSnAccessPassword, onlinePayment.getLastName(), String.valueOf(onlinePayment.getId()),
            onlinePayment.getMsisdn(), cotizelSnAccessToken, onlinePayment.getMnoId());

    }

    private PaymentResponseJson processPayment(PaymentRequestJson paymentRequest) throws PADOnlinePaymentException {

        PaymentResponseJson paymentResponse = null;
        String processPaymentUrl = cotizelSnUrl + ServerConstants.COTIZEL_API_PROCESS_PAYMENT_ENDPOINT;

        try {
            logger.info("processPayment###" + ServerConstants.COTIZEL_API_PROCESS_PAYMENT_ENDPOINT + "#Request: " + paymentRequest.toString());

            String responseJson = callCotizelServlet(processPaymentUrl, paymentRequest, onlinePaymentService.getOnlinePaymentParameterFromMap(paymentRequest.getMnoId()));

            logger.info("processPayment###" + ServerConstants.COTIZEL_API_PROCESS_PAYMENT_ENDPOINT + "#Response: " + responseJson);

            mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            paymentResponse = mapper.readValue(responseJson, PaymentResponseJson.class);

        } catch (PADOnlinePaymentException pade) {
            throw pade;

        } catch (Exception e) {
            logger.error("processPayment###Exception: ", e);
            throw new PADOnlinePaymentException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "processPayment#2");
        }

        return paymentResponse;
    }

    private String callCotizelServlet(String url, PaymentRequestJson paymentRequest, OnlinePaymentParameter onlinePaymentParameter) throws PADOnlinePaymentException {

        String responseSource = "callCotizelServlet#";

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        RequestConfig requestConfig = null;
        String responseText = null;

        try {

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(paymentRequest)));

            httpClient = HttpClients.createDefault();

            requestConfig = RequestConfig.custom().setSocketTimeout(onlinePaymentParameter.getDefaultSocketTimeout())
                .setConnectTimeout(onlinePaymentParameter.getDefaultConnectTimeout()).setConnectionRequestTimeout(onlinePaymentParameter.getDefaultConnRequestTimeout()).build();

            httpPost.setConfig(requestConfig);
            httpPost.setHeader("content-type", "application/json");

            httpResponse = httpClient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                HttpEntity httpResponseEntity = httpResponse.getEntity();
                if (httpResponseEntity != null) {
                    ByteArrayOutputStream httpResponseByteArrayOutputStream = new ByteArrayOutputStream();
                    httpResponseEntity.writeTo(httpResponseByteArrayOutputStream);
                    responseText = httpResponseByteArrayOutputStream.toString();
                }
            } else {

                logger.info(responseSource + "#Response: [StatusCode=" + httpResponse.getStatusLine().getStatusCode() + ", ReasonPhrase="
                    + httpResponse.getStatusLine().getReasonPhrase() + "]");
                throw new PADOnlinePaymentException(ServerResponseConstants.EXTERNAL_API_CONNECTION_FAILURE_CODE, responseSource + "Response: [StatusCode="
                    + httpResponse.getStatusLine().getStatusCode() + ", ReasonPhrase=" + httpResponse.getStatusLine().getReasonPhrase() + "]", responseSource);
            }
        } catch (PADOnlinePaymentException padope) {
            throw padope;
        } catch (SocketTimeoutException ste) {
            logger.error(responseSource + "###SocketTimeoutException: ", ste);
            throw new PADOnlinePaymentException(ServerResponseConstants.EXTERNAL_API_SOCKET_TIMEOUT_CODE, ServerResponseConstants.EXTERNAL_API_SOCKET_TIMEOUT_TEXT,
                responseSource + "##SocketTimeoutException");
        } catch (Exception e) {
            logger.error(responseSource + "###Exception: ", e);
            throw new PADOnlinePaymentException(ServerResponseConstants.EXTERNAL_API_CONNECTION_FAILURE_CODE, e.getClass() + "###" + e.getMessage(),
                responseSource + "##Exception");
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (Exception e) {
                logger.error(responseSource + "###finally#Exception: ", e);
            }
        }

        return responseText;
    }

}
