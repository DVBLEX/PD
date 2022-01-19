package com.pad.server.base.services.email;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.MessageFormatterUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Email;
import com.pad.server.base.entities.EmailConfig;
import com.pad.server.base.entities.EmailTemplate;
import com.pad.server.base.entities.NameValuePair;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.TripApiJson;
import com.pad.server.base.services.system.SystemService;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger                        logger                       = Logger.getLogger(EmailServiceImpl.class);

    private final ConcurrentMap<Long, Properties>      emailConfigPropertiesMap     = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, EmailConfig>     emailConfigMap               = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, EmailTemplate>   emailTemplateMap             = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, EmailTemplate> emailTypeLanguageTemplateMap = new ConcurrentHashMap<>();

    private boolean                                    isLive                       = false;

    private List<EmailConfig>                          emailConfigList              = new CopyOnWriteArrayList<>();

    private List<EmailTemplate>                        emailTemplateList            = new CopyOnWriteArrayList<>();

    @Autowired
    private JdbcTemplate                               jdbcTemplate;

    @Autowired
    private HibernateTemplate                          hibernateTemplate;

    @Autowired
    private SystemService                              systemService;

    @Value("${system.environment}")
    private String                                     systemEnvironment;

    @Value("${system.shortname}")
    private String                                     systemShortName;

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {

        isLive = ServerConstants.SYSTEM_ENVIRONMENT_PROD.equals(systemEnvironment);

        emailConfigList = (List<EmailConfig>) hibernateTemplate.find("FROM EmailConfig");
        if (emailConfigList != null && !emailConfigList.isEmpty()) {
            for (EmailConfig emailConfig : emailConfigList) {
                emailConfigPropertiesMap.put(emailConfig.getId(), createProperties(emailConfig));
                emailConfigMap.put(emailConfig.getId(), emailConfig);
            }
        }

        emailTemplateList = (List<EmailTemplate>) hibernateTemplate.find("FROM EmailTemplate");
        if (emailTemplateList != null && !emailTemplateList.isEmpty()) {
            for (EmailTemplate emailTemplate : emailTemplateList) {
                emailTemplateMap.put(emailTemplate.getId(), emailTemplate);

                String key = emailTemplate.getType() + "#" + emailTemplate.getLanguageId();
                emailTypeLanguageTemplateMap.put(key, emailTemplate);
            }
        }
    }

    private Properties createProperties(EmailConfig emailConfig) {
        Properties emailProps = new Properties();
        emailProps.put("mail.smtp.host", emailConfig.getSmtpHost());
        emailProps.put("mail.smtp.auth", emailConfig.getSmtpAuth());
        emailProps.put("mail.smtp.port", emailConfig.getSmtpPort());

        if (emailConfig.getSmtpStarttlsEnable() != null) {
            emailProps.put("mail.smtp.starttls.enable", emailConfig.getSmtpStarttlsEnable());
            emailProps.put("mail.smtp.ssl.protocols", emailConfig.getSmtpSslProtocols());
        }

        return emailProps;
    }

    @Override
    public void sendSystemEmail(String subject, String emailType, String headerContent, List<NameValuePair> nameValuePairs, String footerContent) {

        try {
            subject = systemEnvironment + " " + systemShortName + " " + subject;

            String content = getContent(emailType, headerContent, nameValuePairs, footerContent);
            InternetAddress[] addressTo = new InternetAddress[1];
            addressTo[0] = new InternetAddress(systemService.getSystemParameter().getErrorsToEmail());

            sendEmail(emailConfigPropertiesMap.get(ServerConstants.DEFAULT_LONG), null, systemService.getSystemParameter().getErrorsFromEmail(),
                systemService.getSystemParameter().getErrorsFromEmailPassword(), addressTo, null, null, subject, content, null);

        } catch (Exception e) {
            logger.error("sendSystemEmail###Exception: ", e);
        }
    }

    @Override
    public String getContent(String emailType, String headerContent, List<NameValuePair> nameValuePairs, String footerContent) {

        StringBuilder content = new StringBuilder("<b>").append(emailType).append("</b>");

        if (headerContent != null) {
            content.append("<p>").append(headerContent).append("</p>");
        }

        content.append("<table>");
        content.append("<tr><td>Date</td><td>").append(new Date()).append("</td></tr>");
        if (nameValuePairs != null) {
            for (NameValuePair nameValuePair : nameValuePairs) {
                content.append("<tr><td>").append(nameValuePair.getName()).append("</td><td>").append(nameValuePair.getValue()).append("</td></tr>");
            }
        }
        content.append("</table>");

        if (footerContent != null) {
            content.append("<p>").append(footerContent).append("</p>");
        }

        return content.toString();
    }

    @Override
    public void sendEmail(Email email) throws PADException {

        InternetAddress[] addressTo = null;
        InternetAddress[] addressBcc = null;
        try {
            addressTo = InternetAddress.parse(email.getEmailTo());
            for (InternetAddress address : addressTo) {
                address.validate();
            }

            if (StringUtils.isNotBlank(email.getEmailBcc())) {
                addressBcc = InternetAddress.parse(email.getEmailBcc());
                for (InternetAddress address : addressBcc) {
                    address.validate();
                }
            }

        } catch (Exception e) {
            throw new PADException(ServerResponseConstants.INVALID_EMAIL_CODE, ServerResponseConstants.INVALID_EMAIL_TEXT + " To. " + e.getClass() + "###" + e.getMessage(),
                "sendEmail#addressTo###Exception");
        }

        EmailTemplate emailTemplate = emailTemplateMap.get(email.getTemplateId());

        File attachment = null;
        if (StringUtils.isNotBlank(email.getAttachmentPath())) {
            attachment = new File(email.getAttachmentPath());
        }

        sendEmail(emailConfigPropertiesMap.get(email.getConfigId()), emailTemplate.getUser(), emailTemplate.getEmailFrom(), emailTemplate.getEmailFromPassword(), addressTo, null,
            addressBcc, email.getSubject(), email.getMessage(), attachment);
    }

    private void sendEmail(Properties props, final String user, final String fromEmail, final String fromEmailPassword, InternetAddress[] addressTo, InternetAddress[] addressCc,
        InternetAddress[] addressBcc, String subject, String content, File attachment) throws PADException {

        try {
            String userAuth = "";

            if (props.get("mail.smtp.starttls.enable") == null) {
            	userAuth = fromEmail;
            } else {
            	if (StringUtils.isBlank(user)) {
                    userAuth = fromEmail;
                } else {
                    userAuth = user;
                }
            }

            final String userAuthentication = userAuth;

            Session session = Session.getInstance(props, new Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {

                    return new PasswordAuthentication(userAuthentication, fromEmailPassword);
                }
            });

            Message msg = new MimeMessage(session);

            InternetAddress addressFrom = new InternetAddress(fromEmail);
            addressFrom.setPersonal(systemShortName);

            msg.setFrom(addressFrom);

            msg.setRecipients(Message.RecipientType.TO, addressTo);
            if (addressCc != null) {
                msg.setRecipients(Message.RecipientType.CC, addressCc);
            }
            if (addressBcc != null) {
                msg.setRecipients(Message.RecipientType.BCC, addressBcc);
            }

            msg.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));

            if (attachment == null) {
                msg.setContent(content, "text/html; charset=utf-8");
            } else {
                BodyPart msgContent = new MimeBodyPart();
                msgContent.setContent(content, "text/html; charset=utf-8");

                BodyPart msgAttachment = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                msgAttachment.setDataHandler(new DataHandler(source));
                msgAttachment.setFileName(attachment.getName());
                Multipart multipart = new MimeMultipart();

                multipart.addBodyPart(msgContent);
                multipart.addBodyPart(msgAttachment);

                msg.setContent(multipart);
            }

            Transport.send(msg);

        } catch (Exception e) {
            logger.error("sendEmail###Exception: ", e);
            throw new PADException(ServerResponseConstants.EXTERNAL_API_CONNECTION_FAILURE_CODE, e.getClass() + "###" + e.getMessage(), "sendEmail###Exception");
        }
    }

    private void scheduleEmail(Email email) {

        email.setIsProcessed(ServerConstants.PROCESS_NOTPROCESSED);
        email.setDateCreated(new Date());
        email.setRetryCount(0);
        email.setResponseCode(ServerConstants.DEFAULT_INT);
        email.setResponseText(ServerConstants.DEFAULT_STRING);

        email.setId(createEmailLog(email));

        jdbcTemplate.update(
            "INSERT INTO email_scheduler(id, is_processed, type, config_id, language_id, account_id, mission_id, trip_id, template_id, priority, email_to, email_bcc, subject, message, channel, attachment_path, date_created, "
                + "date_scheduled, retry_count, date_processed, response_code, response_text) "
                + "SELECT id, is_processed, type, config_id, language_id, account_id, mission_id, trip_id, template_id, priority, email_to, email_bcc, subject, ?, channel, attachment_path, date_created, "
                + "date_scheduled, retry_count, date_processed, response_code, response_text FROM email_log WHERE id = ?",
            email.getMessage(), email.getId());
    }

    private Long createEmailLog(Email email) {

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource()).withTableName("email_log").usingGeneratedKeyColumns("id");

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("is_processed", email.getIsProcessed());
        parameters.addValue("type", email.getType());
        parameters.addValue("config_id", email.getConfigId());
        parameters.addValue("language_id", email.getLanguageId());
        parameters.addValue("account_id", email.getAccountId());
        parameters.addValue("mission_id", email.getMissionId());
        parameters.addValue("trip_id", email.getTripId());
        parameters.addValue("template_id", email.getTemplateId());
        parameters.addValue("priority", email.getPriority());
        parameters.addValue("email_to", email.getEmailTo());
        parameters.addValue("email_bcc", email.getEmailBcc());
        parameters.addValue("subject", email.getSubject());
        parameters.addValue("channel", email.getChannel());
        parameters.addValue("attachment_path", email.getAttachmentPath());
        parameters.addValue("date_created", email.getDateCreated());
        parameters.addValue("date_scheduled", email.getDateScheduled());
        parameters.addValue("retry_count", email.getRetryCount());
        parameters.addValue("response_code", email.getResponseCode());
        parameters.addValue("response_text", email.getResponseText());

        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public void updateScheduledEmail(Email email) {

        email.setDateProcessed(new Date());
        jdbcTemplate.update(
            "UPDATE email_scheduler SET is_processed = ?, date_scheduled = ?, retry_count = ?, date_processed = ?, response_code = ?, response_text = ? WHERE id = ?",
            email.getIsProcessed(), email.getDateScheduled(), email.getRetryCount(), email.getDateProcessed(), email.getResponseCode(), email.getResponseText(), email.getId());
    }

    @Override
    public void deleteScheduledEmail(long emailId) {
        jdbcTemplate.update("DELETE FROM email_scheduler WHERE id = ?", emailId);
    }

    @Override
    public void updateEmail(Email email) {

        email.setDateProcessed(new Date());
        jdbcTemplate.update("UPDATE email_log SET is_processed = ?, date_scheduled = ?, retry_count = ?, date_processed = ?, response_code = ?, response_text = ? WHERE id = ?",
            email.getIsProcessed(), email.getDateScheduled(), email.getRetryCount(), email.getDateProcessed(), email.getResponseCode(), email.getResponseText(), email.getId());
    }

    @Override
    public void scheduleEmailByType(Email email, long templateType, HashMap<String, Object> params) throws PADException {

        updateEmailWithTemplateData(email, templateType, params);
        email.setChannel(ServerConstants.CHANNEL_SYSTEM);
        email.setAttachmentPath(email.getAttachmentPath() == null ? ServerConstants.DEFAULT_STRING : email.getAttachmentPath());
        email.setDateScheduled(email.getDateScheduled() == null ? new Date() : email.getDateScheduled());

        scheduleEmail(email);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void sendTransporterShortNameErrorEmailNotification(TripApiJson tripApiJson, int transactionType, long languageId, long portOpeartorId, String emailTo,
        long templateType) throws PADException {

        EmailTemplate template = emailTypeLanguageTemplateMap.get(templateType + "#" + languageId);

        HashMap<String, Object> params = new HashMap<>();

        if (languageId == ServerConstants.LANGUAGE_FR_ID) {
            params.put("referenceLabel", "BAD / Réservation Export");
        } else {
            params.put("referenceLabel", "BAD / Booking Export");
        }

        params.put("portOperator", systemService.getPortOperatorNameById(portOpeartorId));
        params.put("referenceNumber", tripApiJson.getReferenceNumber());
        params.put("transactionType", ServerUtil.getTransactionTypeName(transactionType, languageId));
        params.put("transporterShortName", tripApiJson.getTransporterShortName());
        params.put("startDate", tripApiJson.getDateSlotFrom());
        params.put("endDate", tripApiJson.getDateSlotTo());

        Email email = new Email();
        email.setEmailTo(emailTo);
        email.setLanguageId(languageId);
        email.setAccountId(ServerConstants.DEFAULT_LONG);
        email.setType(template.getType());
        email.setConfigId(template.getConfigId());
        email.setMissionId(ServerConstants.DEFAULT_LONG);
        email.setTripId(ServerConstants.DEFAULT_LONG);
        email.setTemplateId(template.getId());
        email.setPriority(template.getPriority());
        email.setEmailBcc(template.getEmailBcc());
        email.setAttachmentPath(ServerConstants.DEFAULT_STRING);

        if (isLive) {
            email.setSubject(MessageFormatterUtil.formatText(template.getSubject(), params));
        } else {
            email.setSubject(systemEnvironment + " " + MessageFormatterUtil.formatText(template.getSubject(), params));
        }

        HashMap<String, Object> templateBody = new HashMap<>();
        templateBody.put("templateBody", MessageFormatterUtil.formatText(template.getMessage(), params));

        email.setMessage(MessageFormatterUtil.formatText(template.getTemplate(), templateBody));
        email.setChannel(ServerConstants.CHANNEL_SYSTEM);
        email.setDateScheduled(new Date());

        scheduleEmail(email);
    }

    @Override
    public boolean getIsLive() {
        return this.isLive;
    }

    private void updateEmailWithTemplateData(Email email, long templateType, HashMap<String, Object> params) throws PADException {
        EmailTemplate template = emailTypeLanguageTemplateMap.get(templateType + "#" + email.getLanguageId());// email.getAccountId()

        email.setType(template.getType());
        email.setConfigId(template.getConfigId());
        email.setLanguageId(template.getLanguageId());
        email.setTemplateId(template.getId());
        email.setPriority(template.getPriority());
        email.setEmailBcc(template.getEmailBcc());

        if (isLive) {
            email.setSubject(MessageFormatterUtil.formatText(template.getSubject(), params));
        } else {
            email.setSubject(systemEnvironment + " " + MessageFormatterUtil.formatText(template.getSubject(), params));
        }

        HashMap<String, Object> templateBody = new HashMap<>();
        templateBody.put("templateBody", MessageFormatterUtil.formatText(template.getMessage(), params));

        email.setMessage(MessageFormatterUtil.formatText(template.getTemplate(), templateBody));
    }
}
