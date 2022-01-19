package com.pad.server.base.services.registration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.Email;
import com.pad.server.base.entities.EmailCodeRequest;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.entities.SMSCodeRequest;
import com.pad.server.base.entities.Sms;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.sms.SmsService;
import com.pad.server.base.services.system.SystemService;

@Service
@Transactional
public class RegistrationServiceImpl implements RegistrationService {

    private static final Logger   logger = Logger.getLogger(RegistrationServiceImpl.class);

    @Autowired
    private HibernateTemplate     hibernateTemplate;

    @Autowired
    private JdbcTemplate          jdbcTemplate;

    @Autowired
    private AccountService        accountService;

    @Autowired
    private ActivityLogService    activityLogService;

    @Autowired
    private EmailService          emailService;

    @Autowired
    private SmsService            smsService;

    @Autowired
    private SystemService         systemService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${system.url}")
    private String                systemUrl;

    @Value("${system.url.login.tp}")
    private String                loginTransporterUrl;

    @Override
    @Transactional(rollbackFor = PADException.class)
    public void sendRegistrationCodeEmail(String emailTo, String firstName, String lastName, long languageId) throws PADException {

        String code = RandomStringUtils.randomNumeric(ServerConstants.SIZE_VERIFICATION_CODE);
        long emailCount = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM email_code_requests WHERE email = '" + emailTo + "'", Long.class);

        if (emailCount > 0l) {
            jdbcTemplate.update("UPDATE email_code_requests SET code = ?, date_verified = NULL, date_code_sent = now(), count_code_sent = count_code_sent + 1 WHERE email = ?",
                code, emailTo);
        } else {
            jdbcTemplate.update("INSERT INTO email_code_requests (email, code, count_verified, count_code_sent, date_code_sent, date_created) VALUES (?, ?, 0, 1, now(), now())",
                emailTo, code);
        }

        Email email = new Email();
        email.setEmailTo(emailTo);
        email.setLanguageId(languageId);
        email.setAccountId(ServerConstants.DEFAULT_LONG);
        email.setMissionId(ServerConstants.DEFAULT_LONG);
        email.setTripId(ServerConstants.DEFAULT_LONG);

        HashMap<String, Object> params = new HashMap<>();
        params.put("emailCodeValidityPeriod", systemService.getSystemParameter().getRegEmailCodeValidMinutes());
        params.put("verificationCode", code);
        params.put("firstName", firstName);

        emailService.scheduleEmailByType(email, ServerConstants.EMAIL_VERIFICATION_CODE_TEMPLATE_TYPE, params);
    }

    @Override
    @Transactional(rollbackFor = PADException.class)
    public void sendRegistrationCodeSMS(String msisdnTo, long languageId) throws PADException {

        String code = RandomStringUtils.randomNumeric(ServerConstants.SIZE_VERIFICATION_CODE);
        long smsCount = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM sms_code_requests WHERE msisdn = '" + msisdnTo + "'", Long.class);

        if (smsCount > 0l) {
            jdbcTemplate.update("UPDATE sms_code_requests SET code = ?, date_verified = NULL, date_code_sent = now(), count_code_sent = count_code_sent + 1 WHERE msisdn = ?", code,
                msisdnTo);
        } else {
            jdbcTemplate.update("INSERT INTO sms_code_requests (msisdn, code, count_verified, count_code_sent, date_code_sent, date_created) VALUES (?, ?, 0, 1, now(), now())",
                msisdnTo, code);
        }

        Sms scheduleSms = new Sms();
        scheduleSms.setLanguageId(languageId);
        scheduleSms.setAccountId(ServerConstants.DEFAULT_LONG);
        scheduleSms.setMissionId(ServerConstants.DEFAULT_LONG);
        scheduleSms.setTripId(ServerConstants.DEFAULT_LONG);
        scheduleSms.setMsisdn(msisdnTo);

        HashMap<String, Object> params = new HashMap<>();
        params.put("smsCodeValidityPeriod", systemService.getSystemParameter().getRegSMSCodeValidMinutes());
        params.put("verificationCode", code);

        smsService.scheduleSmsByType(scheduleSms, ServerConstants.SMS_VERIFICATION_CODE_TEMPLATE_TYPE, params);
    }

    @Override
    public boolean verifyRegistrationCodeEmail(final String email, final String code) {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM email_code_requests WHERE email = ? AND code = ? AND date_verified IS NULL", new Object[] { email, code },
            Long.class) > 0l;
    }

    @Override
    public boolean verifyRegistrationCodeSMS(final String msisdn, final String code) {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM sms_code_requests WHERE msisdn = ? AND code = ? AND date_verified IS NULL", new Object[] { msisdn, code },
            Long.class) > 0l;
    }

    @Override
    public boolean verifyRegistrationCodeSMSForPasswordReset(final String msisdn, final String code) {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM sms_code_requests WHERE msisdn = ? AND code = ?", new Object[] { msisdn, code }, Long.class) > 0l;
    }

    @Override
    public void setEmailToVerified(String email, String code, String token) {
        jdbcTemplate.update("UPDATE email_code_requests SET date_verified = now(), token = ?, count_verified = count_verified + 1 WHERE code = ? AND email = ?", token, code,
            email);
    }

    @Override
    public void setMsisdnToVerified(String msisdn, String code, String token) {
        jdbcTemplate.update("UPDATE sms_code_requests SET date_verified = now(), token = ?, count_verified = count_verified + 1 WHERE code = ? AND msisdn = ?", token, code,
            msisdn);
    }

    @Override
    public boolean isEmailVerifiedWithinHours(String email, String token, int hours) {
        return jdbcTemplate.queryForObject(
            "SELECT COUNT(1) FROM email_code_requests WHERE email = ? AND token = ? AND date_verified IS NOT NULL AND date_verified > SUBDATE( CURRENT_TIMESTAMP, INTERVAL ? HOUR )",
            new Object[] { email, token, hours }, Long.class) > 0l;
    }

    @Override
    public boolean isMsisdnVerifiedWithinHours(String msisdn, String token, int hours) {
        return jdbcTemplate.queryForObject(
            "SELECT COUNT(1) FROM sms_code_requests WHERE msisdn = ? AND token = ? AND date_verified IS NOT NULL AND date_verified > SUBDATE( CURRENT_TIMESTAMP, INTERVAL ? HOUR )",
            new Object[] { msisdn, token, hours }, Long.class) > 0l;
    }

    @Override
    public boolean isCountEmailCodeSentUnderLimit(String email, int codeSentLimit) {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM email_code_requests WHERE email = ? AND count_code_sent >= ? ", new Object[] { email, codeSentLimit },
            Long.class) == 0l;
    }

    @Override
    public boolean isCountSmsCodeSentUnderLimit(String msisdn, int codeSentLimit) {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM sms_code_requests WHERE msisdn = ? AND count_code_sent >= ? ", new Object[] { msisdn, codeSentLimit },
            Long.class) == 0l;
    }

    @Override
    public boolean isCountEmailVerifiedUnderLimit(String email, int verifiedLimit) {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM email_code_requests WHERE email = ? AND count_verified >= ? ", new Object[] { email, verifiedLimit },
            Long.class) == 0l;
    }

    @Override
    public boolean isCountMsidnVerifiedUnderLimit(String msisdn, int verifiedLimit) {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM sms_code_requests WHERE msisdn = ? AND count_verified >= ? ", new Object[] { msisdn, verifiedLimit },
            Long.class) == 0l;
    }

    @Override
    public EmailCodeRequest getEmailCodeRequest(String email) throws SQLException {

        EmailCodeRequest regEmailCodeRequest = jdbcTemplate.query("SELECT * FROM email_code_requests WHERE email = ? ", new ResultSetExtractor<EmailCodeRequest>() {

            @Override
            public EmailCodeRequest extractData(ResultSet rs) throws SQLException, DataAccessException {
                EmailCodeRequest regEmailCodeRequest = null;
                if (rs.next()) {
                    regEmailCodeRequest = new EmailCodeRequest(rs.getLong("id"), rs.getString("email"), rs.getString("code"), rs.getString("token"), rs.getInt("count_code_sent"),
                        rs.getInt("count_verified"), rs.getTimestamp("date_code_sent"), rs.getTimestamp("date_verified"));
                }
                return regEmailCodeRequest;
            }
        }, email);

        return regEmailCodeRequest;
    }

    @Override
    public SMSCodeRequest getSmsCodeRequest(String msisdn) throws SQLException {

        SMSCodeRequest regSmsCodeRequest = jdbcTemplate.query("SELECT * FROM sms_code_requests WHERE msisdn = ? ", new ResultSetExtractor<SMSCodeRequest>() {

            @Override
            public SMSCodeRequest extractData(ResultSet rs) throws SQLException, DataAccessException {
                SMSCodeRequest regSmsCodeRequest = null;
                if (rs.next()) {
                    regSmsCodeRequest = new SMSCodeRequest(rs.getLong("id"), rs.getString("msisdn"), rs.getString("code"), rs.getString("token"), rs.getInt("count_code_sent"),
                        rs.getInt("count_verified"), rs.getTimestamp("date_code_sent"), rs.getTimestamp("date_verified"));
                }
                return regSmsCodeRequest;
            }
        }, msisdn);

        return regSmsCodeRequest;
    }

    @Override
    public void deleteEmailCodeRequest(String email) throws SQLException {

        jdbcTemplate.update("DELETE FROM email_code_requests WHERE email = ?", email);
    }

    @Override
    public void deleteSmsCodeRequest(String msisdn) throws SQLException {

        jdbcTemplate.update("DELETE FROM sms_code_requests WHERE msisdn = ?", msisdn);
    }

    @Transactional(rollbackFor = PADException.class)
    @Override
    public void processRegistration(String email, String msisdn, String firstName, String lastName, String password, Account account) throws PADException {

        try {
            account.setCode(SecurityUtil.generateUniqueCode());
            account.setDateCreated(new Date());
            account.setDateEdited(account.getDateCreated());

            hibernateTemplate.save(account);

            account.setNumber(accountService.getAccountNumberForAccount(account.getId()));

            hibernateTemplate.update(account);

            Operator operator = new Operator();
            operator.setCode(SecurityUtil.generateUniqueCode());
            operator.setAccountId(account.getId());
            operator.setPortOperatorId(ServerConstants.DEFAULT_LONG);
            operator.setIndependentPortOperatorId(ServerConstants.DEFAULT_LONG);
            operator.setFirstName(firstName);
            operator.setLastName(lastName);
            operator.setEmail(StringUtils.isBlank(email) ? "" : email);
            operator.setMsisdn(StringUtils.isBlank(msisdn) ? "" : msisdn);

            if (account.getType() == ServerConstants.ACCOUNT_TYPE_COMPANY) {
                operator.setUsername(email);
            } else if (account.getType() == ServerConstants.ACCOUNT_TYPE_INDIVIDUAL) {
                operator.setUsername(msisdn);
            }

            operator.setPassword(passwordEncoder.encode(password));
            operator.setRoleId(ServerConstants.OPERATOR_ROLE_TRANSPORTER);
            operator.setIsActive(true);
            operator.setIsDeleted(false);
            operator.setIsLocked(false);
            operator.setLoginFailureCount(0);
            operator.setDateLocked(null);
            operator.setDateLastLogin(null);
            operator.setOperatorId(ServerConstants.DEFAULT_LONG);
            operator.setLanguageId(account.getLanguageId());
            operator.setCountPasswdForgotRequests(0);
            operator.setDateLastPasswdForgotRequest(null);
            operator.setDatePasswordForgotReported(null);
            operator.setDateLastPassword(new Date());
            operator.setIsCredentialsExpired(false);
            operator.setDateCreated(new Date());
            operator.setDateEdited(operator.getDateCreated());

            hibernateTemplate.save(operator);

            HashMap<String, Object> params = new HashMap<>();
            params.put("operatorName", firstName);
            params.put("accountNumber", account.getNumber());
            params.put("userGuideUrl", systemUrl + "userGuide.htm");
            params.put("loginPageUrl", systemUrl + loginTransporterUrl);
            params.put("loginDetails", operator.getUsername());

            if (account.getType() == ServerConstants.ACCOUNT_TYPE_COMPANY) {

                Email registrationEmail = new Email();
                registrationEmail.setEmailTo(email);
                registrationEmail.setAccountId(account.getId());
                registrationEmail.setLanguageId(account.getLanguageId());
                registrationEmail.setMissionId(ServerConstants.DEFAULT_LONG);
                registrationEmail.setTripId(ServerConstants.DEFAULT_LONG);

                emailService.scheduleEmailByType(registrationEmail, ServerConstants.EMAIL_COMPANY_REGISTRATION_COMPLETED_TEMPLATE_TYPE, params);

            } else if (account.getType() == ServerConstants.ACCOUNT_TYPE_INDIVIDUAL) {

                Sms scheduleSms = new Sms();
                scheduleSms.setLanguageId(account.getLanguageId());
                scheduleSms.setAccountId(account.getId());
                scheduleSms.setMissionId(ServerConstants.DEFAULT_LONG);
                scheduleSms.setTripId(ServerConstants.DEFAULT_LONG);
                scheduleSms.setMsisdn(msisdn);

                smsService.scheduleSmsByType(scheduleSms, ServerConstants.SMS_INDIVIDUAL_REGISTRATION_COMPLETED_TEMPLATE_TYPE, params);
            }

            Email agsEmail = new Email();
            agsEmail.setEmailTo(systemService.getSystemParameter().getAgsOperationsEmail());
            agsEmail.setAccountId(account.getId());
            agsEmail.setLanguageId(ServerConstants.LANGUAGE_FR_ID);
            agsEmail.setMissionId(ServerConstants.DEFAULT_LONG);
            agsEmail.setTripId(ServerConstants.DEFAULT_LONG);

            params = new HashMap<>();
            params.put("accountType", account.getType() == ServerConstants.ACCOUNT_TYPE_COMPANY ? "Entreprise" : "Individuel");
            params.put("AccountName", account.getType() == ServerConstants.ACCOUNT_TYPE_COMPANY ? account.getCompanyName() : account.getFirstName() + " " + account.getLastName());

            emailService.scheduleEmailByType(agsEmail, ServerConstants.EMAIL_NEW_REGISTRATION_AGS_NOTIFICATION_TYPE, params);

            activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_REGISTRATION, operator.getId(), account.getId());

        } catch (PADException pade) {
            logger.error("processRegistration##Exception: ", pade);
            emailService.sendSystemEmail("Process Registration Error", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "processRegistration###Exception:<br />" + pade.getMessage() + "<br /> <br /> Source: " + pade.getResponseSource());
            throw pade;

        } catch (Exception e) {
            logger.error("processRegistration##Exception: ", e);
            emailService.sendSystemEmail("Process Registration Error", EmailService.EMAIL_TYPE_EXCEPTION, null, null, "processRegistration###Exception:<br />" + e.getMessage());

            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "");
        }
    }

}
