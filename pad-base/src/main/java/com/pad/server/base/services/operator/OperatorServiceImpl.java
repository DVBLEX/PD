package com.pad.server.base.services.operator;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pad.server.base.entities.IndependentPortOperator;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Email;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.entities.Sms;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.OperatorJson;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.sms.SmsService;
import com.pad.server.base.services.system.SystemService;

@Service
@Transactional
public class OperatorServiceImpl implements OperatorService {

    @Autowired
    private HibernateTemplate  hibernateTemplate;

    @Autowired
    private JdbcTemplate       jdbcTemplate;

    @Autowired
    private SessionFactory     sessionFactory;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private EmailService       emailService;

    @Autowired
    private SmsService         smsService;

    @Autowired
    private SystemService      systemService;

    @Value("${system.url}")
    private String             systemURL;

    @SuppressWarnings("unchecked")
    @Override
    public Operator getOperator(String username) {

        Operator operator = null;

        List<Operator> operatorList = (List<Operator>) hibernateTemplate.findByNamedParam("FROM Operator WHERE username = :username", "username", username);

        if (operatorList != null && !operatorList.isEmpty()) {
            operator = operatorList.get(0);
        }
        return operator;
    }

    @Override
    public Operator getOperatorById(long id) {

        Operator operator = null;

        operator = hibernateTemplate.get(Operator.class, id);

        return operator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Operator getOperatorByCode(String code) {

        Operator operator = null;

        List<Operator> operatorList = (List<Operator>) hibernateTemplate.findByNamedParam("FROM Operator WHERE code = :code", "code", code);

        if (operatorList != null && !operatorList.isEmpty()) {
            operator = operatorList.get(0);
        }
        return operator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Operator getOperatorByAccountId(long accountId) {

        Operator operator = null;

        List<Operator> operatorList = (List<Operator>) hibernateTemplate.findByNamedParam("FROM Operator WHERE accountId = :accountId", "accountId", accountId);

        if (operatorList != null && !operatorList.isEmpty()) {
            operator = operatorList.get(0);
        }
        return operator;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Operator> getOperatorsByAccountId(long accountId) {

        return (List<Operator>) hibernateTemplate.findByNamedParam("FROM Operator WHERE accountId = :accountId", "accountId", accountId);
    }

    @Override
    public String getValidOperatorUsername(String username) {

        String query = "SELECT COUNT(id) FROM operators WHERE username REGEXP ?";

        List<Object> params = new ArrayList<>();
        params.add("^" + username + "[0-9]*$");

        long count = jdbcTemplate.queryForObject(query, params.toArray(), Long.class);

        if (count == 0)
            return username;
        else {
            count = count + 1;
            return username + count;
        }
    }

    @Override
    public List<OperatorJson> getSystemOperatorList(String firstName, String lastName, Integer role, long accountId, boolean isLimit, String sortColumn, boolean sortAsc,
        int startLimit, int endLimit) throws SQLException {

        final List<OperatorJson> operatorList = new ArrayList<>();

        StringBuffer query = new StringBuffer();
        query.append("SELECT * FROM operators WHERE account_id = ? ");

        List<Object> params = new ArrayList<>();
        params.add(accountId);

        if (StringUtils.isNotBlank(firstName)) {
            query.append(" AND first_name LIKE '%" + firstName + "%' ");
        }

        if (StringUtils.isNotBlank(lastName)) {
            query.append(" AND last_name LIKE '%" + lastName + "%' ");
        }

        if (role != null && role > ServerConstants.ZERO_INT) {
            query.append(" AND role_id = ? ");
            params.add(role);
        }

        if (StringUtils.isBlank(sortColumn)) {
            query.append(" ORDER BY id DESC ");
        } else {
            query.append(" ORDER BY ").append(sortColumn).append(sortAsc ? " ASC" : " DESC");
        }

        if (isLimit) {
            query.append(" LIMIT ?, ? ");
            params.add(startLimit);
            params.add(endLimit);
        }

        jdbcTemplate.query(query.toString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {

                OperatorJson operatorJson = new OperatorJson();
                operatorJson.setCode(rs.getString("code"));
                operatorJson.setPortOperatorId(rs.getInt("port_operator_id"));
                operatorJson.setIndependentPortOperatorCode(systemService.getIndependentPortOperatorCodeById(rs.getLong("independent_port_operator_id")));
                operatorJson.setFirstName(rs.getString("first_name"));
                operatorJson.setLastName(rs.getString("last_name"));
                operatorJson.setEmail(rs.getString("email"));
                operatorJson.setMsisdn(rs.getString("msisdn"));
                operatorJson.setUsername(rs.getString("username"));
                operatorJson.setPassword(rs.getString("password"));
                operatorJson.setRoleId(rs.getInt("role_id"));
                operatorJson.setIsActive(rs.getBoolean("is_active"));
                operatorJson.setIsDeleted(rs.getBoolean("is_deleted"));
                operatorJson.setIsLocked(rs.getBoolean("is_locked"));
                operatorJson.setLoginFailureCount(rs.getInt("login_failure_count"));

                if (rs.getTimestamp("date_last_login") != null) {

                    try {
                        operatorJson.setDateLastLoginString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("date_last_login")));
                    } catch (ParseException e) {
                        operatorJson.setDateLastLoginString("");
                    }

                } else {
                    operatorJson.setDateLastLoginString("");
                }

                try {
                    operatorJson.setDateCreatedString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("date_created")));
                } catch (ParseException e) {
                    operatorJson.setDateCreatedString("");
                }

                operatorList.add(operatorJson);
            }
        }, params.toArray());

        return operatorList;
    }

    @Override
    public Long getSystemOperatorCount(String firstName, String lastName, Integer role, long accountId) {

        StringBuffer query = new StringBuffer();
        query.append("SELECT COUNT(id) FROM operators WHERE account_id = ? ");

        List<Object> params = new ArrayList<>();
        params.add(accountId);

        if (StringUtils.isNotBlank(firstName)) {
            query.append(" AND first_name LIKE '%" + firstName + "%' ");
        }

        if (StringUtils.isNotBlank(lastName)) {
            query.append(" AND last_name LIKE '%" + lastName + "%' ");
        }

        if (role != null && role > ServerConstants.ZERO_INT) {
            query.append(" AND role_id = ? ");
            params.add(role);
        }

        return jdbcTemplate.queryForObject(query.toString(), params.toArray(), Long.class);
    }

    @Override
    public void updateOperator(Operator operator) {

        hibernateTemplate.update(operator);
    }

    @Override
    public void saveOperator(Operator operator) {

        hibernateTemplate.save(operator);
    }

    @Override
    public void deleteOperator(Operator operator) {

        hibernateTemplate.delete(operator);
    }

    @Override
    public void sendPasswdForgotEmail(String email, long languageId) throws PADException, UnsupportedEncodingException {

        Operator operator = this.getOperator(email);

        operator.setCountPasswdForgotRequests(operator.getCountPasswdForgotRequests() + 1);

        Date dateForgotPasswdRequest = new Date();

        String token = SecurityUtil.generateDateBasedToken1(email, dateForgotPasswdRequest);

        Email scheduledEmail = new Email();
        scheduledEmail.setEmailTo(email);
        scheduledEmail.setLanguageId(languageId);
        scheduledEmail.setAccountId(ServerConstants.DEFAULT_LONG);
        scheduledEmail.setMissionId(ServerConstants.DEFAULT_LONG);
        scheduledEmail.setTripId(ServerConstants.DEFAULT_LONG);
        HashMap<String, Object> params = new HashMap<>();
        params.put("firstName", operator.getFirstName());
        params.put("resetPasswordLink", systemURL + "passwordForgotChange.htm?u=" + URLEncoder.encode(email, StandardCharsets.UTF_8.name()) + "&a="
            + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_COMPANY_URL_STRING + "&t=" + token);
        params.put("validMinutes", ServerConstants.FORGOT_PASSWORD_LINK_VALID_MINUTES);

        emailService.scheduleEmailByType(scheduledEmail, ServerConstants.EMAIL_PASSWORD_FORGOT_TEMPLATE_TYPE, params);

        operator.setDateLastPasswdForgotRequest(dateForgotPasswdRequest);

        this.updateOperator(operator);
    }

    @Override
    public void sendPasswdForgotSms(String msisdn, long languageId) throws PADException {

        Operator operator = this.getOperator(msisdn);

        operator.setCountPasswdForgotRequests(operator.getCountPasswdForgotRequests() + 1);

        Date dateForgotPasswdRequest = new Date();

        String token = SecurityUtil.generateDateBasedToken1(msisdn, dateForgotPasswdRequest).substring(0, 25);

        Sms scheduleSms = new Sms();
        scheduleSms.setLanguageId(languageId);
        scheduleSms.setAccountId(ServerConstants.DEFAULT_LONG);
        scheduleSms.setMissionId(ServerConstants.DEFAULT_LONG);
        scheduleSms.setTripId(ServerConstants.DEFAULT_LONG);
        scheduleSms.setMsisdn(msisdn);

        HashMap<String, Object> params = new HashMap<>();
        params.put("resetPasswordLink",
            systemURL + "passwordForgotChange.htm?u=" + msisdn + "&a=" + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_INDIVIDUAL_URL_STRING + "&t=" + token);

        smsService.scheduleSmsByType(scheduleSms, ServerConstants.SMS_PASSWORD_FORGOT_TEMPLATE_TYPE, params);

        operator.setDateLastPasswdForgotRequest(dateForgotPasswdRequest);

        this.updateOperator(operator);
    }

    @Override
    public void sendPasswdSetUpEmail(String email, long languageId, Operator operator) throws PADException, UnsupportedEncodingException {

        Date datePasswdSetUp = new Date();

        String token = SecurityUtil.generateDateBasedToken1(operator.getUsername(), datePasswdSetUp);

        Email scheduledEmail = new Email();
        scheduledEmail.setEmailTo(email);
        scheduledEmail.setLanguageId(languageId);
        scheduledEmail.setAccountId(ServerConstants.DEFAULT_LONG);
        scheduledEmail.setMissionId(ServerConstants.DEFAULT_LONG);
        scheduledEmail.setTripId(ServerConstants.DEFAULT_LONG);

        HashMap<String, Object> params = new HashMap<>();
        params.put("firstName", operator.getFirstName());
        params.put("setPasswordLink", systemURL + "passwordSetUp.htm?u=" + URLEncoder.encode(operator.getUsername(), StandardCharsets.UTF_8.name()) + "&t=" + token);
        params.put("validHours", ServerConstants.SET_UP_PASSWORD_LINK_VALID_HOURS);

        emailService.scheduleEmailByType(scheduledEmail, ServerConstants.EMAIL_PASSWORD_SETUP_TEMPLATE_TYPE, params);

        operator.setDateLastPasswdSetUp(datePasswdSetUp);

        this.updateOperator(operator);
    }

    @Override
    public void sendPasswdSetUpSms(String msisdn, long languageId, Operator operator) throws PADException, UnsupportedEncodingException {

        Date datePasswdSetUp = new Date();

        String token = SecurityUtil.generateDateBasedToken1(operator.getUsername(), datePasswdSetUp).substring(0, 25);

        Sms scheduleSms = new Sms();
        scheduleSms.setLanguageId(languageId);
        scheduleSms.setAccountId(ServerConstants.DEFAULT_LONG);
        scheduleSms.setMissionId(ServerConstants.DEFAULT_LONG);
        scheduleSms.setTripId(ServerConstants.DEFAULT_LONG);
        scheduleSms.setMsisdn(msisdn);

        HashMap<String, Object> params = new HashMap<>();
        params.put("setPasswordLink", systemURL + "passwordSetUp.htm?u=" + URLEncoder.encode(operator.getUsername(), StandardCharsets.UTF_8.name()) + "&t=" + token);

        smsService.scheduleSmsByType(scheduleSms, ServerConstants.SMS_PASSWORD_SETUP_TEMPLATE_TYPE, params);

        operator.setDateLastPasswdSetUp(datePasswdSetUp);

        this.updateOperator(operator);
    }

    @Override
    public Operator addOperator(long accountId, OperatorJson operatorJson, String msisdn, long languageId, long operatorId) throws PADException, PADValidationException {

        String username = ServerConstants.DEFAULT_STRING;
        String firstName = operatorJson.getFirstName();
        String lastName = operatorJson.getLastName();
        String email = operatorJson.getEmail();
        Integer portOperatorId = operatorJson.getPortOperatorId();

        long independentPortOperatorId;
        if (operatorJson.getIndependentPortOperatorCode() == null) {
            independentPortOperatorId = ServerConstants.DEFAULT_LONG;
        } else {
            IndependentPortOperator independentPortOperator = systemService.getIndependentPortOperatorByCode(operatorJson.getIndependentPortOperatorCode());
            independentPortOperatorId = independentPortOperator == null ? ServerConstants.DEFAULT_LONG : independentPortOperator.getId();
        }

        if (accountId == ServerConstants.DEFAULT_LONG) {
            username = firstName.toLowerCase() + "." + lastName.toLowerCase();
            username = username.replaceAll("[\\s'-]", "");
            username = getValidOperatorUsername(username);
        } else {
            username = email;
        }

        if (systemService.isUsernameRegisteredAlready(username))
            throw new PADValidationException(ServerResponseConstants.OPERATOR_WITH_USERNAME_ALREADY_EXISTS_CODE, ServerResponseConstants.OPERATOR_WITH_USERNAME_ALREADY_EXISTS_TEXT,
                "");

        Operator operator = new Operator();
        operator.setCode(SecurityUtil.generateUniqueCode());
        operator.setAccountId(accountId);
        operator.setPortOperatorId(portOperatorId == null ? ServerConstants.DEFAULT_LONG : portOperatorId);
        operator.setIndependentPortOperatorId(independentPortOperatorId);
        operator.setFirstName(firstName);
        operator.setLastName(lastName);
        operator.setEmail(StringUtils.isBlank(email) ? "" : email);
        operator.setMsisdn(StringUtils.isBlank(msisdn) ? "" : msisdn);
        operator.setUsername(username);
        operator.setPassword(ServerConstants.DEFAULT_STRING);
        operator.setRoleId(operatorJson.getRoleId());
        operator.setIsActive(false);
        operator.setIsDeleted(false);
        operator.setIsLocked(false);
        operator.setLoginFailureCount(0);
        operator.setDateLocked(null);
        operator.setDateLastLogin(null);
        operator.setOperatorId(operatorId);
        operator.setLanguageId(languageId);
        operator.setCountPasswdForgotRequests(0);
        operator.setDateLastPasswdForgotRequest(null);
        operator.setDatePasswordForgotReported(null);
        operator.setDateLastPassword(new Date());
        operator.setIsCredentialsExpired(false);
        operator.setDateCreated(new Date());
        operator.setDateEdited(operator.getDateCreated());

        saveOperator(operator);

        activityLogService.saveActivityLogOperator(ServerConstants.ACTIVITY_LOG_OPERATOR_CREATE, operatorId, operator.getId());

        try {
            if (StringUtils.isNotBlank(email)) {
                sendPasswdSetUpEmail(email, languageId, operator);

            } else {
                sendPasswdSetUpSms(msisdn, languageId, operator);
            }

        } catch (UnsupportedEncodingException e) {
            throw new PADException(ServerResponseConstants.FAILURE_CODE, ServerResponseConstants.FAILURE_TEXT, "UnsupportedEncodingException");
        }

        return operator;
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    public Map<String, String> getKioskOperatorNamesMap() {

        Map<String, String> kioskOperatorNamesMap = new ConcurrentHashMap<>();

        Session currentSession = sessionFactory.getCurrentSession();

        List<Operator> kioskOperatorNamesList = new ArrayList<>();

        kioskOperatorNamesList = currentSession.createQuery("FROM Operator o where o.isActive=:is_active and o.roleId=:role_id").setParameter("is_active", true)
            .setParameter("role_id", ServerConstants.OPERATOR_ROLE_PARKING_KIOSK_OPERATOR).list();

        if (kioskOperatorNamesList != null && !kioskOperatorNamesList.isEmpty()) {

            for (Operator operator : kioskOperatorNamesList) {
                kioskOperatorNamesMap.put(operator.getCode(), operator.getFirstName() + " " + operator.getLastName());
            }
        }

        return kioskOperatorNamesMap;
    }

    @Override
    public void unlockOperator(Operator operator) {
        if (operator != null && operator.getIsLocked()) {
            operator.setLoginFailureCount(0);
            operator.setIsLocked(false);
            operator.setDateEdited(new Date());

            this.updateOperator(operator);
        }
    }

}
