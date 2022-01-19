package com.pad.server.base.services.session;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.PreparedJDBCQuery;
import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.NameValuePair;
import com.pad.server.base.entities.Session;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.SessionJson;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.email.EmailService;

@Service
@Transactional
public class SessionServiceImpl implements SessionService {

    private static final String COMPUTED_VALUE_AMOUNT_END_EXPECTED = "amount_end_expected";

    @Autowired
    private HibernateTemplate   hibernateTemplate;

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private SessionFactory      sessionFactory;

    @Autowired
    private EmailService        emailService;

    @Autowired
    private ActivityLogService  activityLogService;

    @Override
    public void saveSession(Session session) {

        hibernateTemplate.save(session);
    }

    @Override
    public void updateSession(Session session) {

        hibernateTemplate.update(session);
    }

    @Override
    public long getSessionCount(SessionJson sessionJson) throws PADException {

        PreparedJDBCQuery query = getSessionsQuery(PreparedJDBCQuery.QUERY_TYPE_COUNT, sessionJson);

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Long.class);
    }

    @Override
    public List<SessionJson> getSessionList(SessionJson sessionJson, String sortColumn, boolean sortAsc, int startLimit, int endLimit) throws PADException {

        final List<SessionJson> sessionList = new ArrayList<>();

        PreparedJDBCQuery query = getSessionsQuery(PreparedJDBCQuery.QUERY_TYPE_SELECT, sessionJson);

        if (!COMPUTED_VALUE_AMOUNT_END_EXPECTED.equals(sortColumn)) { // do not make DB sort if sortColumn is computed value
            query.setSortParameters(sortColumn, sortAsc, "sessions", ServerConstants.DEFAULT_SORTING_FIELD, "DESC");
        }
        query.append(" LIMIT ").append(startLimit).append(", ").append(endLimit);

        jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {

                SessionJson sessionJson = new SessionJson();
                sessionJson.setCode(rs.getString("sessions.code"));
                sessionJson.setType(rs.getInt("sessions.type"));
                sessionJson.setStatus(rs.getInt("sessions.status"));
                sessionJson.setKioskOperatorCode(rs.getString("kioskOpCode"));
                sessionJson.setKioskOperatorName((rs.getString("operators.first_name") + " " + rs.getString("operators.last_name")));
                sessionJson.setLaneNumber(rs.getInt("sessions.lane_number"));
                sessionJson.setCashBagNumber(rs.getString("sessions.cash_bag_number"));
                sessionJson.setCashAmountStart(rs.getBigDecimal("sessions.cash_amount_start"));
                sessionJson.setCashAmountEnd(rs.getBigDecimal("sessions.cash_amount_end"));
                sessionJson.setValidationStep(rs.getInt("sessions.validation_step"));

                if (sessionJson.getStatus() == ServerConstants.SESSION_STATUS_VALIDATED) {

                    sessionJson.setNoAccountCashTransactionTotalAmount(rs.getBigDecimal("sessions.no_account_cash_transaction_total_amount"));
                    sessionJson.setNoAccountOnlineTransactionTotalAmount(rs.getBigDecimal("sessions.no_account_online_transaction_total_amount"));
                    sessionJson.setAccountCashTransactionTotalAmount(rs.getBigDecimal("sessions.account_cash_transaction_total_amount"));
                    sessionJson.setAccountOnlineTransactionTotalAmount(rs.getBigDecimal("sessions.account_online_transaction_total_amount"));
                    sessionJson.setAccountDeductTransactionTotalAmount(rs.getBigDecimal("sessions.account_deduct_transaction_total_amount"));
                    sessionJson.setCashChangeGivenTotalAmount(rs.getBigDecimal("sessions.cash_change_given_total_amount"));
                    sessionJson.setCashAmountEndExpected(
                        sessionJson.getCashAmountStart().add(sessionJson.getNoAccountCashTransactionTotalAmount()).add(sessionJson.getAccountCashTransactionTotalAmount()));

                } else {
                    sessionJson.setNoAccountCashTransactionTotalAmount(BigDecimal.ZERO);
                    sessionJson.setNoAccountOnlineTransactionTotalAmount(BigDecimal.ZERO);
                    sessionJson.setAccountCashTransactionTotalAmount(BigDecimal.ZERO);
                    sessionJson.setAccountOnlineTransactionTotalAmount(BigDecimal.ZERO);
                    sessionJson.setAccountDeductTransactionTotalAmount(BigDecimal.ZERO);
                    sessionJson.setCashChangeGivenTotalAmount(BigDecimal.ZERO);
                    sessionJson.setCashAmountEndExpected(BigDecimal.ZERO);
                }

                try {
                    sessionJson.setDateCreatedString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("sessions.date_created")));
                } catch (ParseException e) {
                    sessionJson.setDateCreatedString("");
                }

                try {
                    sessionJson.setDateStartString(rs.getTimestamp("sessions.date_start") == null ? ""
                        : ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("sessions.date_start")));
                } catch (ParseException e) {
                    sessionJson.setDateStartString("");
                }

                try {
                    sessionJson.setDateEndString(rs.getTimestamp("sessions.date_end") == null ? ""
                        : ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("sessions.date_end")));
                } catch (ParseException e) {
                    sessionJson.setDateEndString("");
                }

                sessionList.add(sessionJson);
            }
        }, query.getQueryParameters());

        if (COMPUTED_VALUE_AMOUNT_END_EXPECTED.equals(sortColumn)) {
            if (sessionJson.getSortAsc()) {
                sessionList.sort((sj1, sj2) -> sj1.getCashAmountEndExpected().compareTo(sj2.getCashAmountEndExpected()));
            } else {
                sessionList.sort((sj1, sj2) -> sj2.getCashAmountEndExpected().compareTo(sj1.getCashAmountEndExpected()));
            }
        }

        return sessionList;
    }

    @Override
    public void addSession(long kioskOperatorId, int type, long laneId, int laneNumber, BigDecimal amountStart, String cashBagNumber, long operatorId)
        throws PADException, PADValidationException {

        int count = getActiveSessionCountByKioskOperatorId(kioskOperatorId);

        if (count > 0)
            throw new PADValidationException(ServerResponseConstants.KIOSK_OPERATOR_ACTIVE_SESSION_EXISTS_CODE, ServerResponseConstants.KIOSK_OPERATOR_ACTIVE_SESSION_EXISTS_TEXT,
                "getActiveSessionCountByKioskOperatorId > 0");

        Session session = new Session();
        session.setCode(SecurityUtil.generateUniqueCode());
        session.setType(type);
        session.setStatus(ServerConstants.SESSION_STATUS_ASSIGNED);
        session.setKioskOperatorId(kioskOperatorId);
        session.setOperatorId(operatorId);
        session.setLaneId(laneId);
        session.setLaneNumber(laneNumber);
        session.setCashBagNumber(cashBagNumber);
        session.setCashAmountStart(amountStart);
        session.setCashAmountEnd(BigDecimal.ZERO);
        session.setValidationStep(ServerConstants.SESSION_VALIDATION_STEP_NOT_VALIDATED);
        session.setReasonAmountUnexpected("");

        session.setNoAccountCashTransactionCount(0);
        session.setNoAccountOnlineTransactionCount(0);
        session.setAccountCashTransactionCount(0);
        session.setAccountOnlineTransactionCount(0);
        session.setAccountDeductTransactionCount(0);
        session.setExitOnlySessionCount(0);
        session.setAdhocTripsCreatedCount(0);
        session.setAdhocTripsCancelledCount(0);

        session.setNoAccountCashTransactionTotalAmount(BigDecimal.ZERO);
        session.setNoAccountOnlineTransactionTotalAmount(BigDecimal.ZERO);
        session.setAccountCashTransactionTotalAmount(BigDecimal.ZERO);
        session.setAccountOnlineTransactionTotalAmount(BigDecimal.ZERO);
        session.setAccountDeductTransactionTotalAmount(BigDecimal.ZERO);
        session.setCashChangeGivenTotalAmount(BigDecimal.ZERO);

        session.setDateCreated(new Date());

        saveSession(session);

        activityLogService.saveActivityLogSession(ServerConstants.ACTIVITY_LOG_SESSION_ADD, operatorId, session.getId());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Session getLastSessionByKioskOperatorId(long kioskOperatorId) {

        Session kioskSession = null;
        org.hibernate.Session currentSession = sessionFactory.getCurrentSession();

        List<Session> sessionList = currentSession.createQuery("FROM Session s where s.kioskOperatorId=:kiosk_operator_id ORDER BY s.id DESC")
            .setParameter("kiosk_operator_id", kioskOperatorId).list();

        if (sessionList != null && !sessionList.isEmpty()) {
            kioskSession = sessionList.get(0);
        }

        return kioskSession;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Session getSessionById(long sessionId) {

        Session session = null;

        List<Session> sessionList = (List<Session>) hibernateTemplate.findByNamedParam("FROM Session WHERE id = :sessionId", "sessionId", sessionId);

        if (sessionList != null && !sessionList.isEmpty()) {
            session = sessionList.get(0);
        }
        return session;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Session getSessionByCode(String code) {

        Session session = null;

        List<Session> sessionList = (List<Session>) hibernateTemplate.findByNamedParam("FROM Session WHERE code = :code", "code", code);

        if (sessionList != null && !sessionList.isEmpty()) {
            session = sessionList.get(0);
        }
        return session;
    }

    @Override
    public long getSessionIdByLaneNumberAndDateStartEnd(int laneNumber, Date dateEvent) {

        StringBuffer query = new StringBuffer();
        query.append(" SELECT s.id FROM sessions s ");
        query.append(" WHERE s.lane_number = ? AND s.status != ? ");
        query.append(" AND ( ? >= s.date_start AND s.date_end IS NULL || ? BETWEEN s.date_start AND s.date_end) ");

        List<Object> params = new ArrayList<>();
        params.add(laneNumber);
        params.add(ServerConstants.SESSION_STATUS_ASSIGNED);
        params.add(dateEvent);
        params.add(dateEvent);

        try {
            return jdbcTemplate.queryForObject(query.toString(), params.toArray(), Long.class).longValue();
        } catch (Exception e) {
            return ServerConstants.DEFAULT_LONG;
        }
    }

    @Override
    public void performKioskSessionValidationChecks(Session kioskSession) throws PADException {

        SessionJson sessionJson = new SessionJson();

        List<NameValuePair> validationIssues = new ArrayList<>();

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        query.append(" SELECT ");
        query.append(" SUM(CASE WHEN (p.payment_option = 1 AND p.type = 1 AND p.account_id = -1) THEN 1 ELSE 0 END) AS 'noAccountCashTransactionCount', ");
        query.append(" SUM(CASE WHEN (p.payment_option = 1 AND p.type = 1 AND p.account_id = -1) THEN p.amount_due ELSE 0 END) AS 'noAccountCashTransactionTotalAmount', ");
        query.append(" SUM(CASE WHEN (p.payment_option = 1 AND p.type = 2 AND p.account_id != -1) THEN 1 ELSE 0 END) AS 'accountCashTransactionCount', ");
        query.append(" SUM(CASE WHEN (p.payment_option = 1 AND p.type = 2 AND p.account_id != -1) THEN p.amount_due ELSE 0 END) AS 'accountCashTransactionTotalAmount', ");
        query.append(" SUM(CASE WHEN (p.payment_option IN (2, 4) AND p.account_id = -1) THEN 1 ELSE 0 END) AS 'noAccountOnlineTransactionCount', ");
        query.append(" SUM(CASE WHEN (p.payment_option IN (2, 4) AND p.account_id = -1) THEN p.amount_due ELSE 0 END) AS 'noAccountOnlineTransactionTotalAmount', ");
        query.append(" SUM(CASE WHEN (p.payment_option IN (2, 4) AND p.account_id != -1) THEN 1 ELSE 0 END) AS 'accountOnlineTransactionCount', ");
        query.append(" SUM(CASE WHEN (p.payment_option IN (2, 4) AND p.account_id != -1) THEN p.amount_due ELSE 0 END) AS 'accountOnlineTransactionTotalAmount' ");

        query.append(" FROM pad.payments p ");
        query.append(" WHERE p.lane_session_id = ? ");

        query.addQueryParameter(kioskSession.getId());

        jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {

                sessionJson.setNoAccountCashTransactionCount(rs.getInt("noAccountCashTransactionCount"));
                sessionJson.setAccountCashTransactionCount(rs.getInt("accountCashTransactionCount"));
                sessionJson.setNoAccountOnlineTransactionCount(rs.getInt("noAccountOnlineTransactionCount"));
                sessionJson.setAccountOnlineTransactionCount(rs.getInt("accountOnlineTransactionCount"));

                sessionJson.setNoAccountCashTransactionTotalAmount(
                    rs.getBigDecimal("noAccountCashTransactionTotalAmount") == null ? BigDecimal.ZERO : rs.getBigDecimal("noAccountCashTransactionTotalAmount"));
                sessionJson.setAccountCashTransactionTotalAmount(
                    rs.getBigDecimal("accountCashTransactionTotalAmount") == null ? BigDecimal.ZERO : rs.getBigDecimal("accountCashTransactionTotalAmount"));
                sessionJson.setNoAccountOnlineTransactionTotalAmount(
                    rs.getBigDecimal("noAccountOnlineTransactionTotalAmount") == null ? BigDecimal.ZERO : rs.getBigDecimal("noAccountOnlineTransactionTotalAmount"));
                sessionJson.setAccountOnlineTransactionTotalAmount(
                    rs.getBigDecimal("accountOnlineTransactionTotalAmount") == null ? BigDecimal.ZERO : rs.getBigDecimal("accountOnlineTransactionTotalAmount"));

            }
        }, query.getQueryParameters());

        if (sessionJson.getNoAccountCashTransactionCount() != kioskSession.getNoAccountCashTransactionCount())
            throw new PADException(ServerResponseConstants.KIOSK_SESSION_DATA_MISMATCH_CODE, ServerResponseConstants.KIOSK_SESSION_DATA_MISMATCH_TEXT,
                "noAccountCashTransactionCount [expected value: " + sessionJson.getNoAccountCashTransactionCount() + "]");

        else if (sessionJson.getAccountCashTransactionCount() != kioskSession.getAccountCashTransactionCount())
            throw new PADException(ServerResponseConstants.KIOSK_SESSION_DATA_MISMATCH_CODE, ServerResponseConstants.KIOSK_SESSION_DATA_MISMATCH_TEXT,
                "accountCashTransactionCount [expected value: " + sessionJson.getAccountCashTransactionCount() + "]");

        else if (sessionJson.getNoAccountCashTransactionTotalAmount().compareTo(kioskSession.getNoAccountCashTransactionTotalAmount()) != 0)
            throw new PADException(ServerResponseConstants.KIOSK_SESSION_DATA_MISMATCH_CODE, ServerResponseConstants.KIOSK_SESSION_DATA_MISMATCH_TEXT,
                "noAccountCashTransactionTotalAmount [expected value: " + sessionJson.getNoAccountCashTransactionTotalAmount() + "]");

        else if (sessionJson.getAccountCashTransactionTotalAmount().compareTo(kioskSession.getAccountCashTransactionTotalAmount()) != 0)
            throw new PADException(ServerResponseConstants.KIOSK_SESSION_DATA_MISMATCH_CODE, ServerResponseConstants.KIOSK_SESSION_DATA_MISMATCH_TEXT,
                "accountCashTransactionTotalAmount [expected value: " + sessionJson.getAccountCashTransactionTotalAmount() + "]");

        String issueDescription = ServerConstants.DEFAULT_STRING;
        if (sessionJson.getNoAccountOnlineTransactionCount() != kioskSession.getNoAccountOnlineTransactionCount()) {
            issueDescription = "Expected value: " + sessionJson.getNoAccountOnlineTransactionCount() + " / Current value : " + kioskSession.getNoAccountOnlineTransactionCount();
            validationIssues.add(new NameValuePair("noAccountOnlineTransactionCount", issueDescription));
        }

        if (sessionJson.getAccountOnlineTransactionCount() != kioskSession.getAccountOnlineTransactionCount()) {
            issueDescription = "Expected value: " + sessionJson.getAccountOnlineTransactionCount() + " / Current value : " + kioskSession.getAccountOnlineTransactionCount();
            validationIssues.add(new NameValuePair("accountOnlineTransactionCount", issueDescription));
        }

        if (sessionJson.getNoAccountOnlineTransactionTotalAmount().compareTo(kioskSession.getNoAccountOnlineTransactionTotalAmount()) != 0) {
            issueDescription = "Expected value: " + sessionJson.getNoAccountOnlineTransactionTotalAmount() + " / Current value : "
                + kioskSession.getNoAccountOnlineTransactionTotalAmount();
            validationIssues.add(new NameValuePair("noAccountOnlineTransactionTotalAmount", issueDescription));
        }

        if (sessionJson.getAccountOnlineTransactionTotalAmount().compareTo(kioskSession.getAccountOnlineTransactionTotalAmount()) != 0) {
            issueDescription = "Expected value: " + sessionJson.getAccountOnlineTransactionTotalAmount() + " / Current value : "
                + kioskSession.getAccountOnlineTransactionTotalAmount();
            validationIssues.add(new NameValuePair("accountOnlineTransactionTotalAmount", issueDescription));
        }

        if (!validationIssues.isEmpty()) {
            String emailSubject = "PerformKioskSessionValidationChecks - Kiosk Session Id: " + kioskSession.getId();
            String emailHeader = ServerResponseConstants.KIOSK_SESSION_DATA_MISMATCH_TEXT + " Kiosk Session Id: " + kioskSession.getId();
            String emailFooter = "SessionServiceImpl.PerformKioskSessionValidationChecks()";

            emailService.sendSystemEmail(emailSubject, EmailService.EMAIL_TYPE_EXCEPTION, emailHeader, validationIssues, emailFooter);
        }

    }

    private PreparedJDBCQuery getSessionsQuery(int queryType, SessionJson sessionJson) throws PADException {
        PreparedJDBCQuery query = new PreparedJDBCQuery();

        if (queryType == PreparedJDBCQuery.QUERY_TYPE_COUNT) {
            query.append("SELECT COUNT(sessions.id) ");

        } else if (queryType == PreparedJDBCQuery.QUERY_TYPE_SELECT) {
            query.append("SELECT sessions.*, operators.code AS kioskOpCode, operators.first_name, operators.last_name ");
        }

        query.append(" FROM sessions sessions ");
        query.append(" LEFT JOIN operators operators ON sessions.kiosk_operator_id = operators.id ");

        if (sessionJson.getStatus() == null) {
            query.append(" WHERE sessions.status IN (?,?,?)");
            query.addQueryParameter(ServerConstants.SESSION_STATUS_ASSIGNED);
            query.addQueryParameter(ServerConstants.SESSION_STATUS_START);
            query.addQueryParameter(ServerConstants.SESSION_STATUS_END);
            // query.addQueryParameter(ServerConstants.SESSION_STATUS_VALIDATED);

        } else {
            query.append(" WHERE sessions.status = ?");
            query.addQueryParameter(sessionJson.getStatus());
        }

        if (sessionJson.getType() != null) {
            query.append(" AND sessions.type = ?");
            query.addQueryParameter(sessionJson.getType());
        }

        if (StringUtils.isNotBlank(sessionJson.getKioskOperatorName())) {
            query.append(" AND (CONCAT(operators.first_name, ' ', operators.last_name) LIKE ?)");
            query.addQueryParameter("%" + sessionJson.getKioskOperatorName() + "%");
        }

        if (sessionJson.getLaneNumber() != null) {
            query.append(" AND sessions.lane_number = ?");
            query.addQueryParameter(sessionJson.getLaneNumber());
        }

        if (StringUtils.isNotBlank(sessionJson.getDateStartString())) {
            try {
                Date startDate = ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyy, sessionJson.getDateStartString());
                query.append(" AND (sessions.date_start >= ? OR sessions.date_start IS NULL)");
                query.addQueryParameter(startDate);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
            }
        }

        return query;
    }

    private int getActiveSessionCountByKioskOperatorId(long kioskOperatorId) {

        String query = "SELECT COUNT(id) FROM sessions WHERE kiosk_operator_id = ? AND status != ?";

        List<Object> params = new ArrayList<>();
        params.add(kioskOperatorId);
        params.add(ServerConstants.SESSION_STATUS_VALIDATED);

        return jdbcTemplate.queryForObject(query, params.toArray(), Integer.class);
    }
}
