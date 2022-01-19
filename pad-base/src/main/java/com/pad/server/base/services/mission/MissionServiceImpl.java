package com.pad.server.base.services.mission;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

import com.pad.server.base.common.PreparedJDBCQuery;
import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.Email;
import com.pad.server.base.entities.IndependentPortOperator;
import com.pad.server.base.entities.Mission;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.entities.PortOperatorTransactionType;
import com.pad.server.base.entities.Sms;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.MissionJson;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.sms.SmsService;
import com.pad.server.base.services.system.SystemService;
import com.pad.server.base.services.trip.TripService;

@Service
@Transactional
public class MissionServiceImpl implements MissionService {

    @Autowired
    private JdbcTemplate       jdbcTemplate;

    @Autowired
    private HibernateTemplate  hibernateTemplate;

    @Autowired
    private SessionFactory     sessionFactory;

    @Autowired
    private AccountService     accountService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private EmailService       emailService;

    @Autowired
    private SmsService         smsService;

    @Autowired
    private SystemService      systemService;

    @Autowired
    private TripService        tripService;

    @Autowired
    private OperatorService    operatorService;

    @Value("${system.url}")
    private String             systemUrl;

    @Value("${system.url.login.tp}")
    private String             loginTransporterUrl;

    @Override
    public long getMissionCount(Account account, MissionJson missionJson, Date dateMissionStart, Date dateMissionEnd) {

        PreparedJDBCQuery query = getMissionQuery(PreparedJDBCQuery.QUERY_TYPE_COUNT, account, missionJson, dateMissionStart, dateMissionEnd);

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Long.class);
    }

    private PreparedJDBCQuery getMissionQuery(int queryType, Account account, MissionJson missionJson, Date dateMissionStart, Date dateMissionEnd) {

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        if (queryType == PreparedJDBCQuery.QUERY_TYPE_COUNT) {

            query.append(" SELECT COUNT(missions.id) ");
            query.append(" FROM pad.missions missions  ");

        } else if (queryType == PreparedJDBCQuery.QUERY_TYPE_SELECT) {

            query.append("SELECT missions.*, accounts.id, accounts.number, accounts.type, accounts.first_name, accounts.last_name, accounts.company_name ");
            query.append(" FROM pad.missions missions ");
            query.append(" LEFT JOIN accounts accounts ON missions.account_id = accounts.id ");
        }

        query.append(" WHERE (1=1) ");

        if (account != null) {
            query.append(" AND missions.account_id = ?");
            query.addQueryParameter(account.getId());
        }

        if (missionJson.getStatus() != null) {
            query.append(" AND missions.status = ?");
            query.addQueryParameter(missionJson.getStatus());
        }

        if (missionJson.getPortOperatorId() != null) {
            if (missionJson.getPortOperatorId() == ServerConstants.PORT_OPERATOR_TM) {
                if (missionJson.getIndependentPortOperatorId() == ServerConstants.INDEPENDENT_PORT_OPERATOR_ISTAMCO) {
                    query.append(" AND missions.port_operator_id IN (?, ?, ?)");
                    query.addQueryParameter(ServerConstants.PORT_OPERATOR_TM_NORTH);
                    query.addQueryParameter(ServerConstants.PORT_OPERATOR_TM_SOUTH);
                    query.addQueryParameter(ServerConstants.PORT_OPERATOR_ISTAMCO);
                } else {
                    query.append(" AND missions.port_operator_id IN (?, ?)");
                    query.addQueryParameter(ServerConstants.PORT_OPERATOR_TM_NORTH);
                    query.addQueryParameter(ServerConstants.PORT_OPERATOR_TM_SOUTH);
                }
            } else {
                query.append(" AND missions.port_operator_id = ?");
                query.addQueryParameter(missionJson.getPortOperatorId());
            }
        }

        if (missionJson.getIndependentPortOperatorId() != null) {
            if (missionJson.getIndependentPortOperatorId() == ServerConstants.INDEPENDENT_PORT_OPERATOR_ISTAMCO) {
                query.append(" AND missions.independent_port_operator_id IN (?, ?)");
                query.addQueryParameter(missionJson.getIndependentPortOperatorId());
                query.addQueryParameter(ServerConstants.DEFAULT_LONG);
            } else {
                query.append(" AND missions.independent_port_operator_id = ?");
                query.addQueryParameter(missionJson.getIndependentPortOperatorId());
            }
        }

        if (missionJson.getTransactionType() != null && missionJson.getTransactionType() != ServerConstants.DEFAULT_INT) {
            query.append(" AND missions.transaction_type = ?");
            query.addQueryParameter(missionJson.getTransactionType());
        }

        if (StringUtils.isNotBlank(missionJson.getReferenceNumber())) {
            query.append(" AND missions.reference_number = ?");
            query.addQueryParameter(missionJson.getReferenceNumber());
        }

        if (dateMissionStart != null) {
            query.append(" AND missions.date_mission_start >= ?");
            query.addQueryParameter(dateMissionStart);
        }

        if (dateMissionEnd != null) {
            query.append(" AND missions.date_mission_end <= ?");
            query.addQueryParameter(dateMissionEnd);
        }

        return query;
    }

    @Override
    public List<MissionJson> getMissionList(Account account, MissionJson missionJson, Date dateMissionStart, Date dateMissionEnd) {
        final List<MissionJson> missionList = new ArrayList<>();
        int startLimit = ServerUtil.getStartLimitPagination(missionJson.getCurrentPage(), missionJson.getPageCount());

        PreparedJDBCQuery query = getMissionQuery(PreparedJDBCQuery.QUERY_TYPE_SELECT, account, missionJson, dateMissionStart, dateMissionEnd);

        query.setSortParameters(missionJson.getSortColumn(), missionJson.getSortAsc(), "missions", "id", "DESC");
        query.append(" LIMIT ").append(startLimit).append(", ").append(missionJson.getPageCount());

        jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                MissionJson missionJson = new MissionJson();
                missionJson.setCode(rs.getString("code"));
                missionJson.setStatus(rs.getInt("status"));
                missionJson.setPortOperatorId(rs.getInt("port_operator_id"));
                missionJson.setIndependentPortOperatorCode(systemService.getIndependentPortOperatorCodeById(rs.getLong("independent_port_operator_id")));
                missionJson.setTransactionType(rs.getInt("transaction_type"));
                missionJson.setReferenceNumber(rs.getString("reference_number"));
                missionJson.setTransporterComments(rs.getString("transporter_comments"));
                missionJson.setTripsCompletedCount(rs.getInt("count_trips_completed"));
                missionJson.setTripsBookedCount(rs.getInt("count_trips_booked"));
                missionJson.setDateMissionStart(rs.getTimestamp("date_mission_start"));
                missionJson.setDateMissionEnd(rs.getTimestamp("date_mission_end"));
                missionJson.setDateCreated(rs.getTimestamp("date_created"));

                try {
                    missionJson.setDateCreatedString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, missionJson.getDateCreated()));
                } catch (ParseException e) {
                    missionJson.setDateCreatedString("");
                }

                try {
                    missionJson.setDateMissionStartString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, missionJson.getDateMissionStart()));
                } catch (ParseException e) {
                    missionJson.setDateMissionStartString("");
                }

                try {
                    missionJson.setDateMissionEndString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, missionJson.getDateMissionEnd()));
                } catch (ParseException e) {
                    missionJson.setDateMissionEndString("");
                }

                if (rs.getObject("accounts.id") == null || rs.getLong("accounts.id") == ServerConstants.DEFAULT_LONG) {
                    missionJson.setTransporterAccountName("");
                    missionJson.setTransporterAccountNumber(ServerConstants.DEFAULT_LONG);

                } else {

                    missionJson.setTransporterAccountNumber(rs.getLong("accounts.number"));
                    if (rs.getInt("accounts.type") == ServerConstants.ACCOUNT_TYPE_COMPANY) {
                        missionJson.setTransporterAccountName(rs.getString("accounts.company_name"));
                    } else if (rs.getInt("accounts.type") == ServerConstants.ACCOUNT_TYPE_INDIVIDUAL) {
                        missionJson.setTransporterAccountName(rs.getString("accounts.first_name") + " " + rs.getString("accounts.last_name"));
                    } else {
                        missionJson.setTransporterAccountName("");
                    }

                }

                missionList.add(missionJson);
            }
        }, query.getQueryParameters());

        return missionList;
    }

    @Override
    public void saveMission(Mission mission) {

        hibernateTemplate.save(mission);
    }

    @Override
    public void updateMission(Mission mission) {

        hibernateTemplate.update(mission);
    }

    @Override
    public Mission getMissionById(Long id) {

        Mission mission = null;

        mission = hibernateTemplate.get(Mission.class, id);

        return mission;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Mission getMissionByCode(String code) {

        Mission mission = null;

        List<Mission> missionList = (List<Mission>) hibernateTemplate.findByNamedParam("FROM Mission WHERE code = :code", "code", code);

        if (missionList != null && !missionList.isEmpty()) {
            mission = missionList.get(0);
        }
        return mission;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Mission getMissionByReferenceNumber(String referenceNumber) {

        Mission mission = null;

        String[] paramNames = { "referenceNumber", "statusPending", "statusBooked" };
        Object[] paramValues = { referenceNumber, ServerConstants.MISSION_STATUS_TRIPS_PENDING, ServerConstants.MISSION_STATUS_TRIPS_BOOKED };

        List<Mission> missionList = (List<Mission>) hibernateTemplate
            .findByNamedParam("FROM Mission WHERE referenceNumber = :referenceNumber AND status IN (:statusPending, :statusBooked)", paramNames, paramValues);

        if (missionList != null && !missionList.isEmpty()) {
            mission = missionList.get(0);
        }
        return mission;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Mission getActiveMissionByReferenceNumberAndAccountId(String referenceNumber, long accountId, int portOperatorId) {

        Mission mission = null;

        String[] paramNames = { "referenceNumber", "accountId", "portOperatorId", "statusPending", "statusBooked" };
        Object[] paramValues = { referenceNumber, accountId, portOperatorId, ServerConstants.MISSION_STATUS_TRIPS_PENDING, ServerConstants.MISSION_STATUS_TRIPS_BOOKED };

        List<Mission> missionList = (List<Mission>) hibernateTemplate.findByNamedParam(
            "FROM Mission WHERE referenceNumber = :referenceNumber AND accountId = :accountId AND portOperatorId = :portOperatorId AND status IN (:statusPending, :statusBooked)",
            paramNames, paramValues);

        if (missionList != null && !missionList.isEmpty()) {
            mission = missionList.get(0);
        }
        return mission;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getMissionReferenceNumbersByAccountIdAndPortOperator(long accountId, int portOperatorId) {

        Date dateToday = new Date();
        List<String> missionReferenceNumbers = new ArrayList<>();
        List<Mission> missions = new ArrayList<>();

        Session currentSession = sessionFactory.getCurrentSession();

        missions = currentSession.createQuery("FROM Mission m where m.accountId=:account_id and m.portOperatorId=:port_operator_id and m.status IN (:statusPending, :statusBooked)")
            .setParameter("account_id", accountId).setParameter("port_operator_id", portOperatorId).setParameter("statusPending", ServerConstants.MISSION_STATUS_TRIPS_PENDING)
            .setParameter("statusBooked", ServerConstants.MISSION_STATUS_TRIPS_BOOKED).list();

        if (missions != null && !missions.isEmpty()) {

            for (Mission mission : missions) {
                if (mission.getDateMissionStart().before(dateToday) && dateToday.before(mission.getDateMissionEnd())) {
                    missionReferenceNumbers.add(mission.getReferenceNumber());
                }
            }
        }

        return missionReferenceNumbers;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Mission getMissionByContainerId(String containerId) {

        Mission mission = null;

        String[] paramNames = { "containerId", "statusPending", "statusBooked" };
        Object[] paramValues = { containerId, ServerConstants.MISSION_STATUS_TRIPS_PENDING, ServerConstants.MISSION_STATUS_TRIPS_BOOKED };

        List<Mission> missionList = (List<Mission>) hibernateTemplate
            .findByNamedParam("FROM Mission WHERE containerId = :containerId AND status IN (:statusPending, :statusBooked)", paramNames, paramValues);

        if (missionList != null && !missionList.isEmpty()) {
            mission = missionList.get(0);
        }
        return mission;
    }

    @Override
    public void saveMissionTrip(Trip trip) {

        hibernateTemplate.save(trip);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Mission getMissionByPortOperatorIdAndTransactionTypeAndReferenceNumber(int portOperatorId, int transactionType, String referenceNumber) {

        Mission mission = null;
        Session currentSession = sessionFactory.getCurrentSession();

        List<Mission> missionList = currentSession
            .createQuery(
                "FROM Mission m where m.portOperatorId=:port_operator_id and m.transactionType=:transaction_type and m.referenceNumber=:reference_number ORDER BY m.id DESC")
            .setParameter("port_operator_id", portOperatorId).setParameter("transaction_type", transactionType).setParameter("reference_number", referenceNumber).list();

        if (missionList != null && !missionList.isEmpty()) {
            mission = missionList.get(0);
        }

        return mission;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Mission getMissionByPortOperatorIdAndReferenceNumberAndAccountId(int portOperatorId, String referenceNumber, long accountId) {

        Mission mission = null;
        Session currentSession = sessionFactory.getCurrentSession();

        String query = "FROM Mission m where m.portOperatorId=:port_operator_id and m.referenceNumber=:reference_number and m.accountId=:account_id and m.status!=:status ORDER BY m.id DESC";

        List<Mission> missionList = currentSession.createQuery(query).setParameter("port_operator_id", portOperatorId).setParameter("reference_number", referenceNumber)
            .setParameter("account_id", accountId).setParameter("status", ServerConstants.MISSION_STATUS_CANCELLED).list();

        if (missionList != null && !missionList.isEmpty()) {
            mission = missionList.get(0);
        }

        return mission;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMission(MissionJson missionJson, long loggedOperatorId, boolean isApi) throws PADValidationException, PADException {

        PortOperatorTransactionType portOperatorTransactionType = null;

        for (String accountCode : missionJson.getAccountCodes()) {

            Account account = accountService.getAccountByCode(accountCode);

            if (account == null)
                throw new PADValidationException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT + "accountCode(" + accountCode + ")",
                    "account is null");

            Mission existingMission = getMissionByPortOperatorIdAndReferenceNumberAndAccountId(missionJson.getPortOperatorId(), missionJson.getReferenceNumber(), account.getId());

            if (existingMission != null)
                throw new PADValidationException(ServerResponseConstants.MISSION_WITH_BAD_ALREADY_EXISTS_CODE,
                    ServerResponseConstants.MISSION_WITH_BAD_ALREADY_EXISTS_TEXT + (isApi ? "" : "accountCode(" + accountCode + ")"), "mission already exists");

            portOperatorTransactionType = systemService.getPortOperatorTransactionTypeEntity(missionJson.getPortOperatorId(), missionJson.getTransactionType());

            IndependentPortOperator independentPortOperator = StringUtils.isBlank(missionJson.getIndependentPortOperatorCode()) ? null
                : systemService.getIndependentPortOperatorByCode(missionJson.getIndependentPortOperatorCode());

            if ((missionJson.getPortOperatorId() == ServerConstants.PORT_OPERATOR_TM_NORTH || missionJson.getPortOperatorId() == ServerConstants.PORT_OPERATOR_TM_SOUTH)
                && independentPortOperator == null)
                throw new PADException(ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_CODE, ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_TEXT, "#createMission");

            Mission mission = new Mission();
            mission.setCode(SecurityUtil.generateUniqueCode());
            mission.setStatus(ServerConstants.MISSION_STATUS_TRIPS_PENDING);
            mission.setAccountId(account.getId());
            mission.setPortOperatorId(missionJson.getPortOperatorId());
            mission.setIndependentPortOperatorId(independentPortOperator == null ? ServerConstants.DEFAULT_LONG : independentPortOperator.getId());
            mission.setPortOperatorGateId(missionJson.getGateId());
            mission.setTransactionType(missionJson.getTransactionType());
            mission.setReferenceNumber(missionJson.getReferenceNumber());
            mission.setTransporterComments(missionJson.getTransporterComments());
            mission.setTripsCompletedCount(0);
            mission.setTripsBookedCount(0);
            mission.setOperatorId(loggedOperatorId);
            mission.setIsDirectToPort(portOperatorTransactionType.getIsDirectToPort());
            mission.setIsAllowMultipleEntries(portOperatorTransactionType.getIsAllowMultipleEntries());
            mission.setDateMissionStart(missionJson.getDateMissionStart());
            mission.setDateMissionEnd(missionJson.getDateMissionEnd());
            mission.setDateCreated(new Date());
            mission.setDateEdited(mission.getDateCreated());

            saveMission(mission);

            missionJson.setCode(mission.getCode());

            if (!isApi) {
                sendNotification(account, mission, ServerConstants.EMAIL_MISSION_ADDED_NOTIFICATION_TEMPLATE_TYPE, ServerConstants.SMS_MISSION_ADDED_NOTIFICATION_TEMPLATE_TYPE);
            }

            activityLogService.saveActivityLogMission(ServerConstants.ACTIVITY_LOG_MISSION_ADD, loggedOperatorId, mission.getId());
        }
    }

    private void sendNotification(Account account, Mission mission, long notificationEmailType, long notificationSmsType) throws PADException {
        HashMap<String, Object> params = new HashMap<>();

        if (account.getLanguageId() == ServerConstants.LANGUAGE_FR_ID) {
            params.put("referenceLabel", "BAD / Réservation Export");
        } else {
            params.put("referenceLabel", "BAD / Booking Export");
        }

        params.put("accountName", account.getFirstName());
        params.put("portOperator", systemService.getPortOperatorNameById(mission.getPortOperatorId()));
        params.put("referenceNumber", mission.getReferenceNumber());
        params.put("transactionType", ServerUtil.getTransactionTypeName(mission.getTransactionType(), account.getLanguageId()));
        params.put("transporterComments",mission.getTransporterComments());

        try {
            params.put("startDate", ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, mission.getDateMissionStart()));
        } catch (ParseException e) {
            params.put("startDate", "");
        }

        try {
            params.put("endDate", ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, mission.getDateMissionEnd()));
        } catch (ParseException e) {
            params.put("endDate", "");
        }

        params.put("loginPageUrl", systemUrl + loginTransporterUrl);

        Operator transporter = operatorService.getOperatorByAccountId(account.getId());

        if (account.getType() == ServerConstants.ACCOUNT_TYPE_COMPANY) {
            // email transporter

            Email scheduledEmail = new Email();
            scheduledEmail.setEmailTo(transporter.getUsername());
            scheduledEmail.setLanguageId(account.getLanguageId());
            scheduledEmail.setAccountId(account.getId());
            scheduledEmail.setMissionId(mission.getId());
            scheduledEmail.setTripId(ServerConstants.DEFAULT_LONG);

            emailService.scheduleEmailByType(scheduledEmail, notificationEmailType, params);

        } else {
            // send sms to individual transporter
            Sms scheduledSms = new Sms();
            scheduledSms.setLanguageId(account.getLanguageId());
            scheduledSms.setAccountId(account.getId());
            scheduledSms.setMissionId(mission.getId());
            scheduledSms.setTripId(ServerConstants.DEFAULT_LONG);
            scheduledSms.setMsisdn(transporter.getUsername());

            smsService.scheduleSmsByType(scheduledSms, notificationSmsType, params);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mission> getExpiredMissions(int portOperatorId, int transactionType, Date dateMissionExpiry) {

        String[] paramNames = { "statusExpired", "statusCancelled", "portOperatorId", "transactionType", "dateMissionExpiry" };
        Object[] paramValues = { ServerConstants.MISSION_STATUS_EXPIRED, ServerConstants.MISSION_STATUS_CANCELLED, portOperatorId, transactionType, dateMissionExpiry };

        List<Mission> missionList = (List<Mission>) hibernateTemplate.findByNamedParam(
            "FROM Mission WHERE status != :statusExpired AND status != :statusCancelled AND portOperatorId = :portOperatorId AND transactionType = :transactionType AND dateMissionEnd < :dateMissionExpiry",
            paramNames, paramValues);

        return missionList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelMission(MissionJson missionJson, long loggedOperatorId) throws PADValidationException, PADException {

        Mission mission = getMissionByCode(missionJson.getCode());

        if (mission == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "Mission code is invalid");

        for (Trip trip : mission.getTripList()) {

            if (trip.getStatus() != ServerConstants.TRIP_STATUS_PENDING_APPROVAL && trip.getStatus() != ServerConstants.TRIP_STATUS_PENDING)
                throw new PADValidationException(ServerResponseConstants.MISSION_CANNOT_BE_CANCEL_CODE, ServerResponseConstants.MISSION_CANNOT_BE_CANCEL_TEXT, "");

            trip.setStatus(ServerConstants.TRIP_STATUS_CANCELLED);
            trip.setOperatorId(loggedOperatorId);

            tripService.updateTrip(trip);
        }

        mission.setStatus(ServerConstants.MISSION_STATUS_CANCELLED);
        mission.setOperatorId(loggedOperatorId);
        mission.setDateEdited(new Date());

        updateMission(mission);

        Account account = accountService.getAccountById(mission.getAccountId());

        sendNotification(account, mission, ServerConstants.EMAIL_MISSION_CANCEL_NOTIFICATION_TEMPLATE_TYPE, ServerConstants.SMS_MISSION_CANCEL_NOTIFICATION_TEMPLATE_TYPE);

        activityLogService.saveActivityLogMission(ServerConstants.ACTIVITY_LOG_MISSION_CANCEL, loggedOperatorId, mission.getId());

    }
}
