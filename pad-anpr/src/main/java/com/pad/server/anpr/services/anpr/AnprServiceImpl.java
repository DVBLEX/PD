package com.pad.server.anpr.services.anpr;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pad.server.anpr.common.ServerConstants;
import com.pad.server.anpr.entities.AddVehicleRequest;
import com.pad.server.anpr.entities.AddVehicleResponse;
import com.pad.server.anpr.entities.CreateWhiteListRequest;
import com.pad.server.anpr.entities.CreateWhiteListResponse;
import com.pad.server.anpr.entities.DeleteWhiteListRequest;
import com.pad.server.anpr.entities.DeleteWhiteListResponse;
import com.pad.server.anpr.entities.EntryLog;
import com.pad.server.anpr.entities.GetEntryLogRequest;
import com.pad.server.anpr.entities.GetEntryLogResponse;
import com.pad.server.anpr.entities.UpdateWhiteListStatusRequest;
import com.pad.server.anpr.entities.UpdateWhiteListStatusResponse;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Anpr;
import com.pad.server.base.entities.AnprEntryLog;
import com.pad.server.base.entities.AnprEntryScheduler;
import com.pad.server.base.entities.AnprParameter;
import com.pad.server.base.entities.AnprScheduler;
import com.pad.server.base.entities.Parking;
import com.pad.server.base.entities.PortAccess;
import com.pad.server.base.entities.PortAccessWhitelist;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.entities.VehicleAnpr;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ParkingJson;
import com.pad.server.base.jsonentities.api.PortAccessJson;
import com.pad.server.base.jsonentities.api.PortAccessWhitelistJson;
import com.pad.server.base.services.anpr.AnprBaseService;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.parking.ParkingService;
import com.pad.server.base.services.portaccess.PortAccessService;
import com.pad.server.base.services.trip.TripService;
import com.pad.server.base.services.vehicle.VehicleService;

@Service
public class AnprServiceImpl implements AnprService {

    private static final Logger      logger                           = Logger.getLogger(AnprServiceImpl.class);

    private AnprScheduler            anprScheduler;

    private List<AnprScheduler>      anprSchedulerList                = new CopyOnWriteArrayList<>();

    private List<AnprEntryScheduler> anprEntrySchedulerInProgressList = new CopyOnWriteArrayList<>();

    @Autowired
    private HibernateTemplate        hibernateTemplate;

    @Autowired
    private JdbcTemplate             jdbcTemplate;

    @Autowired
    private SessionFactory           sessionFactory;

    @Autowired
    private AnprBaseService          anprBaseService;

    @Autowired
    private EmailService             emailService;

    @Autowired
    private ParkingService           parkingService;

    @Autowired
    private PortAccessService        portAccessService;

    @Autowired
    private TripService              tripService;

    @Autowired
    private VehicleService           vehicleService;

    @Value("${anpr.api.base.url}")
    private String                   anprApiBaseUrl;

    @Value("${anpr.api.secret}")
    private String                   anprApiSecret;

    @Value("${anpr.company.id}")
    private long                     anprCompanyId;

    @Value("${anpr.type.id}")
    private long                     anprTypeId;

    @Value("${anpr.color.id}")
    private long                     anprColorId;

    @Value("${anpr.parking.spot.count}")
    private long                     anprParkingSpotCount;

    @Value("${anpr.parkingpermission.status.active.id}")
    private long                     anprParkingPermissionStatusActiveId;

    @Value("${anpr.parkingpermission.status.disabled.id}")
    private long                     anprParkingPermissionStatusDisabledId;

    @Value("${anpr.person.id}")
    private long                     anprPersonId;

    @Value("${anpr.vip}")
    private String                   anprVip;

    private ObjectMapper             mapper;

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {

        anprSchedulerList = (List<AnprScheduler>) hibernateTemplate.find("FROM AnprScheduler WHERE requestType = " + ServerConstants.REQUEST_TYPE_ANPR_API_GET_ENTRY_LOG);
        if (anprSchedulerList != null && !anprSchedulerList.isEmpty()) {
            anprScheduler = anprSchedulerList.get(0);

            if (anprScheduler.getIsProcessed() != ServerConstants.PROCESS_NOTPROCESSED) {
                resetAnprSchedulerProcessing(anprScheduler);
            }
        }

        anprEntrySchedulerInProgressList = (List<AnprEntryScheduler>) hibernateTemplate.find("FROM AnprEntryScheduler WHERE isProcessed = " + ServerConstants.PROCESS_PROGRESS);
        if (anprEntrySchedulerInProgressList != null && !anprEntrySchedulerInProgressList.isEmpty()) {

            for (AnprEntryScheduler anprEntryScheduler : anprEntrySchedulerInProgressList) {
                resetAnprEntrySchedulerProcessing(anprEntryScheduler);
            }
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void resetAnprSchedulerProcessing(AnprScheduler anprScheduler) {

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        anprScheduler.setIsProcessed(ServerConstants.PROCESS_NOTPROCESSED);

        logger.info("resetAnprSchedulerProcessing#anprSchedulerId: " + anprScheduler.getId());

        session.saveOrUpdate(anprScheduler);
        tx.commit();

        if (session != null) {
            session.close();
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void resetAnprEntrySchedulerProcessing(AnprEntryScheduler anprEntryScheduler) {

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        anprEntryScheduler.setIsProcessed(ServerConstants.PROCESS_NOTPROCESSED);

        logger.info("resetAnprEntrySchedulerProcessing#anprEntrySchedulerId: " + anprEntryScheduler.getId());

        session.saveOrUpdate(anprEntryScheduler);
        tx.commit();

        if (session != null) {
            session.close();
        }
    }

    private String callAnprServlet(String url, List<NameValuePair> nameValuePairs, AnprParameter anprParameter) throws PADException {

        String responseSource = "callAnprServlet#";

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        RequestConfig requestConfig = null;
        String responseText = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            httpClient = HttpClients.createDefault();

            if (url.contains(ServerConstants.ANPR_API_LIST_ENTRY_LOG_ENDPOINT)) {
                requestConfig = RequestConfig.custom().setSocketTimeout(anprParameter.getEntryLogSocketTimeout()).setConnectTimeout(anprParameter.getEntryLogConnectTimeout())
                    .setConnectionRequestTimeout(anprParameter.getEntryLogConnRequestTimeout()).build();

            } else {
                requestConfig = RequestConfig.custom().setSocketTimeout(anprParameter.getDefaultSocketTimeout()).setConnectTimeout(anprParameter.getDefaultConnectTimeout())
                    .setConnectionRequestTimeout(anprParameter.getDefaultConnRequestTimeout()).build();
            }

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

                logger.info(responseSource + "#Response: [StatusCode=" + httpResponse.getStatusLine().getStatusCode() + ", ReasonPhrase="
                    + httpResponse.getStatusLine().getReasonPhrase() + "]");
                throw new PADException(ServerResponseConstants.EXTERNAL_API_CONNECTION_FAILURE_CODE, responseSource + "Response: [StatusCode="
                    + httpResponse.getStatusLine().getStatusCode() + ", ReasonPhrase=" + httpResponse.getStatusLine().getReasonPhrase() + "]", responseSource);
            }
        } catch (PADException pade) {
            throw pade;
        } catch (Exception e) {
            logger.error(responseSource + "###Exception: ", e);
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
                logger.error(responseSource + "###finally#Exception: ", e);
            }
        }

        return responseText;
    }

    @Override
    public void scheduleAnprEventLog() throws PADException {

        try {
            anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_GET_ENTRY_LOG, ServerConstants.DEFAULT_LONG, null, null, null, new Date());

        } catch (Exception e) {
            logger.error("scheduleAnprEventLog###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "scheduleAnprEventLog#");
        }
    }

    @Override
    public void saveAnprEventLog(Anpr anpr) throws PADException {

        AnprParameter anprParameter = anprBaseService.getAnprParameter();

        GetEntryLogRequest getEntryLogRequest = createGetEntryLogRequest(anprParameter);

        GetEntryLogResponse getEntryLogResponse = getAnprEntryLog(getEntryLogRequest, anprParameter);

        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            int count = 0;
            long lastEntryLogId = ServerConstants.DEFAULT_LONG;

            for (EntryLog entryLog : getEntryLogResponse.getEntrylog()) {

                AnprEntryLog anprEntryLog = new AnprEntryLog();
                anprEntryLog.setDateCreated(new Date());
                anprEntryLog.setDateScheduled(new Date());
                anprEntryLog.setRetryCount(0);
                anprEntryLog.setTripId(ServerConstants.DEFAULT_LONG);
                anprEntryLog.setMissionId(ServerConstants.DEFAULT_LONG);

                anprEntryLog.setEntryLogId(entryLog.getEntryLogId());

                if (entryLog.getLaneId() != null) {
                    anprEntryLog.setLaneId(entryLog.getLaneId());
                }

                anprEntryLog.setPlateNumber(StringUtils.isEmpty(entryLog.getPlateNumber()) ? "" : entryLog.getPlateNumber());
                anprEntryLog.setRecognizedPlateNumber(StringUtils.isEmpty(entryLog.getRecognizedPlateNumber()) ? "" : entryLog.getRecognizedPlateNumber());

                if (entryLog.getParkingPermissionId() != null) {
                    anprEntryLog.setParkingPermissionId(entryLog.getParkingPermissionId());
                }

                if (StringUtils.isEmpty(entryLog.getNotes())) {
                    anprEntryLog.setNotes("");

                } else {

                    if (entryLog.getNotes().length() > 256) {
                        anprEntryLog.setNotes(entryLog.getNotes().substring(0, entryLog.getNotes().length() - 1));

                    } else {
                        anprEntryLog.setNotes(entryLog.getNotes());
                    }
                }

                anprEntryLog.setTimestamp(StringUtils.isEmpty(entryLog.getTimestamp()) ? "" : entryLog.getTimestamp());
                anprEntryLog.setEntryEventTypeId(entryLog.getEntryEventTypeId());
                anprEntryLog.setEnteringDirectionId(entryLog.getEnteringDirectionId());

                if (entryLog.getTenantCardId() != null) {
                    anprEntryLog.setTenantCardId(entryLog.getTenantCardId());
                }

                if (entryLog.getCarId() != null) {
                    anprEntryLog.setCarId(entryLog.getCarId());
                }

                if (entryLog.getTenantPersonId() != null) {
                    anprEntryLog.setTenantPersonId(entryLog.getTenantPersonId());
                }

                if (entryLog.getTenantCompanyId() != null) {
                    anprEntryLog.setTenantCompanyId(entryLog.getTenantCompanyId());
                }

                anprEntryLog.setImagePath(StringUtils.isEmpty(entryLog.getImagePath()) ? "" : entryLog.getImagePath());

                if (entryLog.getEnteringEventId() != null) {
                    anprEntryLog.setEnteringEventId(entryLog.getEnteringEventId());
                }

                if (entryLog.getBlacklistId() != null) {
                    anprEntryLog.setBlacklistId(entryLog.getBlacklistId());
                }

                if (entryLog.getUserId() != null) {
                    anprEntryLog.setUserId(entryLog.getUserId());
                }

                anprEntryLog.setZoneIdFrom(entryLog.getZoneIdFrom());
                anprEntryLog.setZoneFrom(StringUtils.isEmpty(entryLog.getZoneFrom()) ? "" : entryLog.getZoneFrom());
                anprEntryLog.setZoneIdTo(entryLog.getZoneIdTo());
                anprEntryLog.setZoneTo(StringUtils.isEmpty(entryLog.getZoneTo()) ? "" : entryLog.getZoneTo());
                anprEntryLog.setApplicants(StringUtils.isEmpty(entryLog.getApplicants()) ? "" : entryLog.getApplicants());
                anprEntryLog.setUserName(StringUtils.isEmpty(entryLog.getUserName()) ? "" : entryLog.getUserName());
                anprEntryLog.setLaneName(StringUtils.isEmpty(entryLog.getLaneName()) ? "" : entryLog.getLaneName());
                anprEntryLog.setCompanyName(StringUtils.isEmpty(entryLog.getCompanyName()) ? "" : entryLog.getCompanyName());
                anprEntryLog.setPersonName(StringUtils.isEmpty(entryLog.getPersonName()) ? "" : entryLog.getPersonName());
                anprEntryLog.setCarPlateNumber(StringUtils.isEmpty(entryLog.getCarPlateNumber()) ? "" : entryLog.getCarPlateNumber());
                anprEntryLog.setCardNumber(StringUtils.isEmpty(entryLog.getCardNumber()) ? "" : entryLog.getCardNumber());
                anprEntryLog.setEnteringEventName(StringUtils.isEmpty(entryLog.getEnteringEventName()) ? "" : entryLog.getEnteringEventName());
                anprEntryLog.setEnteringDirectionName(StringUtils.isEmpty(entryLog.getEnteringDirectionName()) ? "" : entryLog.getEnteringDirectionName());
                anprEntryLog.setAreaCodeName(StringUtils.isEmpty(entryLog.getAreaCodeName()) ? "" : entryLog.getAreaCodeName());

                if (anprEntryLog.getEntryEventTypeId() == ServerConstants.ENTRY_EVENT_TYPE_ID_APPROVED
                    || anprEntryLog.getEntryEventTypeId() == ServerConstants.ENTRY_EVENT_TYPE_ID_NO_PERMISSION
                    || anprEntryLog.getEntryEventTypeId() == ServerConstants.ENTRY_EVENT_TYPE_ID_NO_PERMISSION_FOR_ZONE) {

                    anprEntryLog.setIsProcessed(ServerConstants.PROCESS_NOTPROCESSED);

                } else {
                    anprEntryLog.setIsProcessed(ServerConstants.PROCESS_CANCELLED);
                }

                session.save(anprEntryLog);

                if (anprEntryLog.getIsProcessed() == ServerConstants.PROCESS_NOTPROCESSED) {
                    // only include in the queue the entries scheduled for processing
                    AnprEntryScheduler anprEntryScheduler = new AnprEntryScheduler();

                    BeanUtils.copyProperties(anprEntryLog, anprEntryScheduler);

                    session.save(anprEntryScheduler);
                }

                if (count % ServerConstants.JDBC_BATCH_SIZE == 0) {

                    // flush a batch of inserts and release memory
                    session.flush();
                    session.clear();
                }

                count++;
            }

            tx.commit();

            if (getEntryLogResponse.getEntrylog() != null && getEntryLogResponse.getEntrylog().size() > 0) {

                lastEntryLogId = getEntryLogResponse.getEntrylog().get(getEntryLogResponse.getEntrylog().size() - 1).getEntryLogId();

                anprParameter.setLastEntryLogId(lastEntryLogId);
                anprParameter.setDateEdited(new Date());

                anprBaseService.updateAnprParameter(anprParameter);
            }

        } catch (HibernateException he) {
            logger.error("saveAnprEventLog###HibernateException: ", he);

            emailService.sendSystemEmail("saveAnprEventLog " + he.getClass().getSimpleName(), EmailService.EMAIL_TYPE_EXCEPTION, null, null, "AnprServiceImpl#saveAnprEventLog###"
                + he.getClass().getSimpleName() + ":<br />Cause: " + he.getCause() + "<br />Message: " + he.getMessage() + "<br />Stacktrace: " + he.getStackTrace());

            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "saveAnprEventLog#1");

        } catch (NullPointerException npe) {
            logger.error("saveAnprEventLog###NullPointerException: ", npe);

            emailService.sendSystemEmail("saveAnprEventLog " + npe.getClass().getSimpleName(), EmailService.EMAIL_TYPE_EXCEPTION, null, null, "AnprServiceImpl#saveAnprEventLog###"
                + npe.getClass().getSimpleName() + ":<br />Cause: " + npe.getCause() + "<br />Message: " + npe.getMessage() + "<br />Stacktrace: " + npe.getStackTrace());

            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "saveAnprEventLog#2");

        } catch (BeansException be) {
            logger.error("saveAnprEventLog###BeansException: ", be);

            emailService.sendSystemEmail("saveAnprEventLog " + be.getClass().getSimpleName(), EmailService.EMAIL_TYPE_EXCEPTION, null, null, "AnprServiceImpl#saveAnprEventLog###"
                + be.getClass().getSimpleName() + ":<br />Cause: " + be.getCause() + "<br />Message: " + be.getMessage() + "<br />Stacktrace: " + be.getStackTrace());

            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "saveAnprEventLog#3");

        } catch (Exception e) {
            logger.error("saveAnprEventLog###Exception: ", e);

            emailService.sendSystemEmail("saveAnprEventLog " + e.getClass().getSimpleName(), EmailService.EMAIL_TYPE_EXCEPTION, null, null, "AnprServiceImpl#saveAnprEventLog###"
                + e.getClass().getSimpleName() + ":<br />Cause: " + e.getCause() + "<br />Message: " + e.getMessage() + "<br />Stacktrace: " + e.getStackTrace());

            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "saveAnprEventLog#4");

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void createWhiteList(Anpr anpr) throws PADException, ParseException {

        long anprVehicleCarId = ServerConstants.DEFAULT_LONG;

        VehicleAnpr vehicleAnpr = vehicleService.getVehicleAnprByRegNumber(anpr.getVehicleRegistration());

        if (vehicleAnpr == null) {

            AddVehicleResponse addVehicleResponse = addVehicle(createAddVehicleRequest(anpr.getVehicleRegistration()));
            anprVehicleCarId = addVehicleResponse.getCarId();

        } else {
            anprVehicleCarId = vehicleAnpr.getCarId();
        }

        CreateWhiteListRequest createWhiteListRequest = createWhiteListRequest(anprVehicleCarId, anpr.getZoneId(), anpr.getDateValidFrom(), anpr.getDateValidTo());

        CreateWhiteListResponse createWhiteListResponse = createWhiteList(createWhiteListRequest);

        anpr.setParkingPermissionId(createWhiteListResponse.getParkingPermissionId());

        if (anpr.getParkingPermissionId() != ServerConstants.DEFAULT_LONG && anpr.getTripId() != ServerConstants.DEFAULT_LONG) {

            Trip trip = tripService.getTripById(anpr.getTripId());

            switch (anpr.getRequestType()) {

                case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_ENTRY:
                    trip.setParkingPermissionIdParkingEntryFirst(anpr.getParkingPermissionId());
                    trip.setParkingPermissionIdParkingEntry(anpr.getParkingPermissionId());

                    break;

                case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_REENTRY_AFTER_PREM_EXIT:
                    trip.setParkingPermissionIdParkingEntry(anpr.getParkingPermissionId());
                    break;

                case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_EXIT:
                    trip.setParkingPermissionIdParkingExit(anpr.getParkingPermissionId());
                    break;

                case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY:
                    trip.setParkingPermissionIdPortEntry(anpr.getParkingPermissionId());
                    break;

                case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_DIRECT:
                case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_WHITELISTED:
                case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_URGENT:
                    trip.setParkingPermissionIdPortEntry(anpr.getParkingPermissionId());

                    break;

                case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_EXIT:
                    trip.setParkingPermissionIdPortExit(anpr.getParkingPermissionId());
                    break;
            }

            if (trip.getIsDirectToPort() && trip.getIsAllowMultipleEntries()) {

                if (trip.getParkingPermissionId() < 0l) {
                    // in case of trip on direct to port with multiple port entries allowed, set parking permission id only the very first time
                    trip.setParkingPermissionId(anpr.getParkingPermissionId());

                    tripService.updateTrip(trip);
                }

            } else {
                trip.setParkingPermissionId(anpr.getParkingPermissionId());

                tripService.updateTrip(trip);
            }

        } else if (anpr.getParkingPermissionId() != ServerConstants.DEFAULT_LONG && anpr.getPortAccessWhitelistId() != ServerConstants.DEFAULT_LONG) {

            PortAccessWhitelist portAccessWhitelist = portAccessService.getPortAccessWhitelistById(anpr.getPortAccessWhitelistId());

            if (portAccessWhitelist != null) {
                portAccessWhitelist.setParkingPermissionId(anpr.getParkingPermissionId());
                portAccessWhitelist.setDateEdited(new Date());

                portAccessService.updatePortAccessWhitelist(portAccessWhitelist);
            }
        }
    }

    @Override
    public void deleteWhiteList(Anpr anpr) throws PADException {

        DeleteWhiteListRequest deleteWhiteListRequest = deleteWhiteListRequest(anpr.getParkingPermissionId());

        deleteWhiteList(deleteWhiteListRequest);
    }

    @Override
    public void updateWhiteListStatus(Anpr anpr) throws PADException {

        long statusId = -1;
        if (anpr.getRequestType() == ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PARKING_ENTRY_STATUS_DISABLED
            || anpr.getRequestType() == ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_ENTRY_STATUS_DISABLED
            || anpr.getRequestType() == ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_EXIT_STATUS_DISABLED) {
            statusId = anprParkingPermissionStatusDisabledId;

        } else if (anpr.getRequestType() == ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_ENTRY_STATUS_ENABLED
            || anpr.getRequestType() == ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PARKING_ENTRY_STATUS_ENABLED) {
            statusId = anprParkingPermissionStatusActiveId;
        }

        UpdateWhiteListStatusRequest updateWhiteListStatusRequest = updateWhiteListStatusRequest(anpr.getParkingPermissionId(), statusId);

        updateWhiteListStatus(updateWhiteListStatusRequest);
    }

    private GetEntryLogResponse getAnprEntryLog(GetEntryLogRequest getEntryLogRequest, AnprParameter anprParameter) throws PADException {

        GetEntryLogResponse getEntryLogResponse = null;
        String anprEntryLogUrl = anprApiBaseUrl + ServerConstants.ANPR_API_LIST_ENTRY_LOG_ENDPOINT;

        try {
            String responseJson = callAnprServlet(anprEntryLogUrl, getEntryLogRequest.getNameValuePairs(), anprParameter);

            logger.info("getAnprEntryLog###" + ServerConstants.ANPR_API_LIST_ENTRY_LOG_ENDPOINT + "#Response: " + responseJson);

            mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            getEntryLogResponse = mapper.readValue(responseJson, GetEntryLogResponse.class);

            if (!getEntryLogResponse.getSuccess())
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, getEntryLogResponse.getMessage(), "getAnprEntryLog#1");

        } catch (PADException pade) {
            throw pade;

        } catch (Exception e) {
            logger.error("getAnprEntryLog###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "getAnprEntryLog#2");
        }

        return getEntryLogResponse;
    }

    private AddVehicleResponse addVehicle(AddVehicleRequest addVehicleRequest) throws PADException {

        AddVehicleResponse addVehicleResponse = null;
        String anprAddVehicleUrl = anprApiBaseUrl + ServerConstants.ANPR_API_CREATE_CAR_ENDPOINT;

        try {
            VehicleAnpr vehicleAnpr = new VehicleAnpr();
            vehicleAnpr.setVehicleRegistration(addVehicleRequest.getPlateNumber());
            vehicleAnpr.setDateCreated(new Date());
            vehicleAnpr.setDateRequest(vehicleAnpr.getDateCreated());

            String responseJson = callAnprServlet(anprAddVehicleUrl, addVehicleRequest.getNameValuePairs(), anprBaseService.getAnprParameter());

            logger.info("addVehicle###" + ServerConstants.ANPR_API_CREATE_CAR_ENDPOINT + "#Response: " + responseJson);

            vehicleAnpr.setDateResponse(new Date());

            mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            addVehicleResponse = mapper.readValue(responseJson, AddVehicleResponse.class);

            if (addVehicleResponse.getSuccess()) {

                vehicleAnpr.setCarId(addVehicleResponse.getCarId());
                vehicleService.saveVehicleAnpr(vehicleAnpr);

            } else
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, addVehicleResponse.getMessage(), "addVehicle#1");

        } catch (PADException pade) {
            throw pade;

        } catch (Exception e) {
            logger.error("addVehicle###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "addVehicle#2");
        }

        return addVehicleResponse;
    }

    private CreateWhiteListResponse createWhiteList(CreateWhiteListRequest createWhiteListRequest) throws PADException {

        CreateWhiteListResponse createWhiteListResponse = null;
        String anprCreateWhiteListUrl = anprApiBaseUrl + ServerConstants.ANPR_API_CREATE_PARKINGPERMISSIONS_ENDPOINT;

        try {
            String responseJson = callAnprServlet(anprCreateWhiteListUrl, createWhiteListRequest.getNameValuePairs(), anprBaseService.getAnprParameter());

            logger.info("createWhiteList###" + ServerConstants.ANPR_API_CREATE_PARKINGPERMISSIONS_ENDPOINT + "#Response: " + responseJson);

            mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            createWhiteListResponse = mapper.readValue(responseJson, CreateWhiteListResponse.class);

            if (!createWhiteListResponse.getSuccess())
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, createWhiteListResponse.getMessage(), "createWhiteList#1");

        } catch (PADException pade) {
            throw pade;

        } catch (Exception e) {
            logger.error("createWhiteList###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "createWhiteList#2");
        }

        return createWhiteListResponse;
    }

    private DeleteWhiteListResponse deleteWhiteList(DeleteWhiteListRequest deleteWhiteListRequest) throws PADException {

        DeleteWhiteListResponse deleteWhiteListResponse = null;
        String anprDeleteWhiteListUrl = anprApiBaseUrl + ServerConstants.ANPR_API_DELETE_PARKINGPERMISSIONS_ENDPOINT;

        try {
            String responseJson = callAnprServlet(anprDeleteWhiteListUrl, deleteWhiteListRequest.getNameValuePairs(), anprBaseService.getAnprParameter());

            logger.info("deleteWhiteList###" + ServerConstants.ANPR_API_DELETE_PARKINGPERMISSIONS_ENDPOINT + "#Response: " + responseJson);

            mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            deleteWhiteListResponse = mapper.readValue(responseJson, DeleteWhiteListResponse.class);

            if (!deleteWhiteListResponse.getSuccess()) {

                if (deleteWhiteListResponse.getMessage().equalsIgnoreCase("The requested item cannot be found"))
                    throw new PADException(ServerResponseConstants.PARKING_PERMISSION_ID_NOT_FOUND_CODE, deleteWhiteListResponse.getMessage(), "deleteWhiteList#1");
                else if (deleteWhiteListResponse.getMessage()
                    .equalsIgnoreCase("Unable to delete this permission: it's still in use. There is a car parking inside using this permission."))
                    throw new PADException(ServerResponseConstants.PARKING_PERMISSION_ID_CANNOT_DELETE_STILL_IN_USE_CODE, deleteWhiteListResponse.getMessage(),
                        "deleteWhiteList#2");
                else
                    throw new PADException(ServerResponseConstants.API_FAILURE_CODE, deleteWhiteListResponse.getMessage(), "deleteWhiteList#3");
            }

        } catch (PADException pade) {
            throw pade;

        } catch (Exception e) {
            logger.error("deleteWhiteList###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "deleteWhiteList#4");
        }

        return deleteWhiteListResponse;
    }

    private UpdateWhiteListStatusResponse updateWhiteListStatus(UpdateWhiteListStatusRequest updateWhiteListStatusRequest) throws PADException {

        UpdateWhiteListStatusResponse updateWhiteListStatusResponse = null;
        String anprUpdateWhiteListStatusUrl = anprApiBaseUrl + ServerConstants.ANPR_API_UPDATE_PARKINGPERMISSIONS_STATUS_ENDPOINT;

        try {
            String responseJson = callAnprServlet(anprUpdateWhiteListStatusUrl, updateWhiteListStatusRequest.getNameValuePairs(), anprBaseService.getAnprParameter());

            logger.info("updateWhiteListStatus###" + ServerConstants.ANPR_API_UPDATE_PARKINGPERMISSIONS_STATUS_ENDPOINT + "#Response: " + responseJson);

            mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            updateWhiteListStatusResponse = mapper.readValue(responseJson, UpdateWhiteListStatusResponse.class);

            if (!updateWhiteListStatusResponse.getSuccess()) {

                if (updateWhiteListStatusResponse.getMessage().equalsIgnoreCase("The requested item cannot be found"))
                    throw new PADException(ServerResponseConstants.PARKING_PERMISSION_ID_NOT_FOUND_CODE, updateWhiteListStatusResponse.getMessage(), "updateWhiteListStatus#1");
                else
                    throw new PADException(ServerResponseConstants.API_FAILURE_CODE, updateWhiteListStatusResponse.getMessage(), "updateWhiteListStatus#2");
            }

        } catch (PADException pade) {
            throw pade;

        } catch (Exception e) {
            logger.error("updateWhiteListStatus###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "updateWhiteListStatus#3");
        }

        return updateWhiteListStatusResponse;
    }

    private GetEntryLogRequest createGetEntryLogRequest(AnprParameter anprParameter) throws PADException {

        return new GetEntryLogRequest(anprApiSecret, anprParameter.getEntryLogPageSize(), 1,
            "EntrylogId" + ServerConstants.ANPR_API_FILTER_GREATER_THAN + anprParameter.getLastEntryLogId());
    }

    private AddVehicleRequest createAddVehicleRequest(String regNumber) throws PADException {

        return new AddVehicleRequest(anprApiSecret, regNumber, anprCompanyId, anprTypeId, anprColorId);
    }

    private CreateWhiteListRequest createWhiteListRequest(long anprVehicleId, long zoneId, Date validFrom, Date validTo) throws PADException, ParseException {

        return new CreateWhiteListRequest(anprApiSecret, ServerUtil.formatDate(ServerConstants.dateFormatMMddyyyyhhmma, validFrom),
            ServerUtil.formatDate(ServerConstants.dateFormatMMddyyyyhhmma, validTo), zoneId, anprParkingSpotCount, anprCompanyId, anprParkingPermissionStatusActiveId, anprPersonId,
            anprVehicleId, anprVip);
    }

    private DeleteWhiteListRequest deleteWhiteListRequest(long parkingPermissionId) throws PADException {

        return new DeleteWhiteListRequest(anprApiSecret, parkingPermissionId);
    }

    private UpdateWhiteListStatusRequest updateWhiteListStatusRequest(long parkingPermissionId, long statusId) throws PADException {

        return new UpdateWhiteListStatusRequest(anprApiSecret, parkingPermissionId, statusId);
    }

    @Override
    public void updateScheduledAnpr(Anpr anpr) {

        anpr.setDateProcessed(new Date());
        jdbcTemplate.update(
            "UPDATE anpr_scheduler SET is_processed = ?, date_scheduled = ?, retry_count = ?, date_processed = ?, response_code = ?, response_text = ? WHERE id = ?",
            anpr.getIsProcessed(), anpr.getDateScheduled(), anpr.getRetryCount(), anpr.getDateProcessed(), anpr.getResponseCode(), anpr.getResponseText(), anpr.getId());
    }

    @Override
    public void updateScheduledAnprEntryLog(AnprEntryScheduler anprEntryScheduler) {

        anprEntryScheduler.setDateProcessed(new Date());
        jdbcTemplate.update("UPDATE anpr_entry_scheduler SET is_processed = ?, date_scheduled = ?, retry_count = ?, date_processed = ? WHERE id = ?",
            anprEntryScheduler.getIsProcessed(), anprEntryScheduler.getDateScheduled(), anprEntryScheduler.getRetryCount(), anprEntryScheduler.getDateProcessed(),
            anprEntryScheduler.getId());
    }

    @Override
    public void deleteScheduledAnpr(long anprId) {
        jdbcTemplate.update("DELETE FROM anpr_scheduler WHERE id = ?", anprId);
    }

    @Override
    public void deleteAnprEntryScheduler(long anprEntrySchedulerId) {

        jdbcTemplate.update("DELETE FROM anpr_entry_scheduler WHERE id = ?", anprEntrySchedulerId);
    }

    @Override
    public void updateAnpr(Anpr anpr) {

        anpr.setDateProcessed(new Date());
        jdbcTemplate.update(
            "UPDATE anpr_log SET parking_permission_id = ?, is_processed = ?, date_scheduled = ?, retry_count = ?, date_processed = ?, response_code = ?, response_text = ? WHERE id = ?",
            anpr.getParkingPermissionId(), anpr.getIsProcessed(), anpr.getDateScheduled(), anpr.getRetryCount(), anpr.getDateProcessed(), anpr.getResponseCode(),
            anpr.getResponseText(), anpr.getId());
    }

    @Override
    public void updateAnprEntryLog(AnprEntryScheduler anprEntryScheduler) {

        anprEntryScheduler.setDateProcessed(new Date());
        jdbcTemplate.update(
            "UPDATE anpr_entry_log SET trip_id = ?, mission_id = ?, is_processed = ?, date_scheduled = ?, date_processed = ?, response_code = ?, response_text = ? WHERE id = ?",
            anprEntryScheduler.getTripId(), anprEntryScheduler.getMissionId(), anprEntryScheduler.getIsProcessed(), anprEntryScheduler.getDateScheduled(),
            anprEntryScheduler.getDateProcessed(), anprEntryScheduler.getResponseCode(), anprEntryScheduler.getResponseText(), anprEntryScheduler.getId());
    }

    @Override
    public void processAnprEventScheduler(AnprEntryScheduler anprEntryScheduler) {

        try {
            processAnprEvent(anprEntryScheduler);

            anprEntryScheduler.setIsProcessed(ServerConstants.PROCESS_PROCESSED);
            anprEntryScheduler.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
            anprEntryScheduler.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

            updateAnprEntryLog(anprEntryScheduler);
            deleteAnprEntryScheduler(anprEntryScheduler.getId());

        } catch (PADException pade) {

            logger.error("run#AnprEntrySchedulerId=" + anprEntryScheduler.getId() + "###PADException: [responseCode=" + pade.getResponseCode() + ", responseText="
                + pade.getResponseText() + ", responseSource=" + pade.getResponseSource() + "]");

            anprEntryScheduler.setRetryCount(anprEntryScheduler.getRetryCount() + 1);
            anprEntryScheduler.setIsProcessed(ServerConstants.PROCESS_FAILED);
            anprEntryScheduler.setResponseCode(pade.getResponseCode());
            anprEntryScheduler.setResponseText(pade.getResponseText());

            if (pade.getResponseCode() != ServerResponseConstants.VEHICLE_ALREADY_PARKED_CODE
                && !pade.getResponseSource().equalsIgnoreCase(ServerConstants.PROCESS_ANPR_EVENT_NO_PARKING_PERMISSION_RESPONSE_SOURCE)) {

                emailService.sendSystemEmail("AnprEntryLogProcessTask PADException" + " - ANPREntryLogId: " + anprEntryScheduler.getId(), EmailService.EMAIL_TYPE_EXCEPTION, null,
                    null, "AnprEntryLogProcessTask#run###PADException:<br />Response Code: " + pade.getResponseCode() + "<br />Response Text: " + pade.getResponseText()
                        + "<br />Response Source: " + pade.getResponseSource() + "<br /><br />ANPR Entry Log ID: " + anprEntryScheduler.getId());
            }

            updateAnprEntryLog(anprEntryScheduler);
            deleteAnprEntryScheduler(anprEntryScheduler.getId());

        } catch (PADValidationException padve) {

            logger.error("run#AnprEntrySchedulerId=" + anprEntryScheduler.getId() + "###PADValidationException: [responseCode=" + padve.getResponseCode() + ", responseText="
                + padve.getResponseText() + ", responseSource=" + padve.getResponseSource() + "]");

            anprEntryScheduler.setRetryCount(anprEntryScheduler.getRetryCount() + 1);
            anprEntryScheduler.setIsProcessed(ServerConstants.PROCESS_FAILED);
            anprEntryScheduler.setResponseCode(padve.getResponseCode());
            anprEntryScheduler.setResponseText(padve.getResponseText());

            emailService.sendSystemEmail("AnprEntryLogProcessTask PADValidationException" + " - ANPREntryLogId: " + anprEntryScheduler.getId(), EmailService.EMAIL_TYPE_EXCEPTION,
                null, null, "AnprEntryLogProcessTask#run###PADValidationException:<br />Response Code: " + padve.getResponseCode() + "<br />Response Text: "
                    + padve.getResponseText() + "<br />Response Source: " + padve.getResponseSource() + "<br /><br />ANPR Entry Log ID: " + anprEntryScheduler.getId());

            updateAnprEntryLog(anprEntryScheduler);
            deleteAnprEntryScheduler(anprEntryScheduler.getId());

        } catch (IndexOutOfBoundsException | DateTimeException | DataAccessException | HibernateException ex) {

            logger.error("run#AnprEntrySchedulerId=" + anprEntryScheduler.getId() + "###Exception: ", ex);

            anprEntryScheduler.setIsProcessed(ServerConstants.PROCESS_FAILED);
            anprEntryScheduler.setResponseCode(ServerResponseConstants.FAILURE_CODE);
            anprEntryScheduler.setResponseText(ServerResponseConstants.FAILURE_TEXT);

            emailService.sendSystemEmail("AnprEntryLogProcessTask " + ex.getClass().getSimpleName() + " - ANPREntryLogId: " + anprEntryScheduler.getId(),
                EmailService.EMAIL_TYPE_EXCEPTION, null, null, "AnprEntryLogProcessTask#run###" + ex.getClass().getSimpleName() + ":<br />Cause: " + ex.getCause()
                    + "<br />Message: " + ex.getMessage() + "<br />Stacktrace: " + ex.getStackTrace() + "<br /><br />ANPR Entry Log ID: " + anprEntryScheduler.getId());

            updateAnprEntryLog(anprEntryScheduler);
            deleteAnprEntryScheduler(anprEntryScheduler.getId());

        } catch (Exception e) {

            logger.error("run#AnprEntrySchedulerId=" + anprEntryScheduler.getId() + "###Exception: ", e);

            anprEntryScheduler.setIsProcessed(ServerConstants.PROCESS_FAILED);
            anprEntryScheduler.setResponseCode(ServerResponseConstants.FAILURE_CODE);
            anprEntryScheduler.setResponseText(ServerResponseConstants.FAILURE_TEXT);

            emailService.sendSystemEmail("AnprEntryLogProcessTask " + e.getClass().getSimpleName() + " - ANPREntryLogId: " + anprEntryScheduler.getId(),
                EmailService.EMAIL_TYPE_EXCEPTION, null, null, "AnprEntryLogProcessTask#run###" + e.getClass().getSimpleName() + ":<br />Cause: " + e.getCause() + "<br />Message: "
                    + e.getMessage() + "<br />Stacktrace: " + e.getStackTrace() + "<br /><br />ANPR Entry Log ID: " + anprEntryScheduler.getId());

            updateAnprEntryLog(anprEntryScheduler);
            deleteAnprEntryScheduler(anprEntryScheduler.getId());
        }
    }

    private void processAnprEvent(AnprEntryScheduler anprEntryScheduler) throws PADException, PADValidationException {

        if (StringUtils.isBlank(anprEntryScheduler.getPlateNumber()) && StringUtils.isBlank(anprEntryScheduler.getRecognizedPlateNumber()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, "Missing PlateNumber", "processAnprEvent#Missing PlateNumber");

        if (StringUtils.isBlank(anprEntryScheduler.getTimestamp()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, "Missing Timestamp", "processAnprEvent#Missing Timestamp");

        String vehicleRegNumber = "";

        if (StringUtils.isBlank(anprEntryScheduler.getPlateNumber())) {
            vehicleRegNumber = ServerUtil.formatVehicleRegNumber(anprEntryScheduler.getRecognizedPlateNumber());
        } else {
            vehicleRegNumber = ServerUtil.formatVehicleRegNumber(anprEntryScheduler.getPlateNumber());
        }

        int entryEventTypeTypeId = anprEntryScheduler.getEntryEventTypeId().intValue();
        long laneId = anprEntryScheduler.getLaneId() == null ? ServerConstants.DEFAULT_LONG : anprEntryScheduler.getLaneId();
        long eventEpochTimeMillis = Long.parseLong(anprEntryScheduler.getTimestamp().substring(6, 19));

        final LocalDateTime eventLocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(eventEpochTimeMillis), ZoneId.systemDefault());
        final Date dateEvent = Date.from(eventLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        AnprParameter anprParameter = anprBaseService.getAnprParameter();

        switch (entryEventTypeTypeId) {
            case (int) ServerConstants.ENTRY_EVENT_TYPE_ID_APPROVED:

                if (anprEntryScheduler.getParkingPermissionId() == null)
                    throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, "Missing parkingPermissionId", "processAnprEvent#Missing parkingPermissionId");

                if (anprEntryScheduler.getZoneIdFrom() == anprParameter.getAnprZoneIdOutside() && anprEntryScheduler.getZoneIdTo() == anprParameter.getAnprZoneIdAgsparking()) {
                    // PARKING ENTRY
                    //
                    Trip trip = tripService.findApprovedTripByVehicleReg(vehicleRegNumber, anprEntryScheduler.getParkingPermissionId());

                    parkingService.processVehicleEntry(trip.getCode(), ServerConstants.DEFAULT_LONG, laneId, dateEvent);

                    anprEntryScheduler.setMissionId(trip.getMission().getId());
                    anprEntryScheduler.setTripId(trip.getId());

                } else if (anprEntryScheduler.getZoneIdFrom() == anprParameter.getAnprZoneIdAgsparking()
                    && anprEntryScheduler.getZoneIdTo() == anprParameter.getAnprZoneIdOutside()) {
                    // PARKING EXIT
                    //
                    ParkingJson parkingJsonSession = parkingService.getParkingSessionByVehicleReg(vehicleRegNumber);

                    if (parkingJsonSession == null) {
                        // no vehicle with that reg found in the existing parking sessions.
                        parkingService.processVehicleExit(null, "", anprEntryScheduler.getParkingPermissionId(), vehicleRegNumber, ServerConstants.DEFAULT_LONG, laneId, dateEvent);

                    } else {
                        // vehicle found
                        anprEntryScheduler = parkingService.processVehicleExit(anprEntryScheduler, parkingJsonSession.getCode(), anprEntryScheduler.getParkingPermissionId(),
                            parkingJsonSession.getVehicleRegistration(), ServerConstants.DEFAULT_LONG, laneId, dateEvent);
                    }

                } else if (anprEntryScheduler.getZoneIdFrom() == anprParameter.getAnprZoneIdOutside() && (anprEntryScheduler.getZoneIdTo() == anprParameter.getAnprZoneIdMole2()
                    || anprEntryScheduler.getZoneIdTo() == anprParameter.getAnprZoneIdMole4() || anprEntryScheduler.getZoneIdTo() == anprParameter.getAnprZoneIdMole8())) {
                    // PORT ENTRY
                    //
                    PortAccess portAccess = null;

                    Trip trip = tripService.getTripByVehicleRegNumberAndParkingPermissionId(vehicleRegNumber, anprEntryScheduler.getParkingPermissionId(),
                        ServerConstants.PARKING_PERMISSION_TYPE_PORT_ENTRY);

                    if (trip == null) {

                        // check if vehicle is on a port entry whitelist
                        PortAccessWhitelistJson portAccessWhitelistJson = portAccessService.getVehicleWhitelisted(vehicleRegNumber);

                        if (portAccessWhitelistJson == null)
                            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, "No trip associated with vehicle and parking permission id",
                                "processAnprEvent#No trip associated with vehicle and parking permission id");
                        else {
                            portAccess = portAccessService.processWhitelistedVehiclePortEntry(vehicleRegNumber, anprEntryScheduler.getParkingPermissionId(),
                                portAccessWhitelistJson.getPortOperatorId(), portAccessWhitelistJson.getGateId(), ServerConstants.DEFAULT_LONG, laneId, dateEvent, ServerConstants.DEFAULT_STRING);

                            anprEntryScheduler.setMissionId(portAccess.getMissionId());
                            anprEntryScheduler.setTripId(portAccess.getTripId());
                        }

                    } else {

                        if (trip.getIsDirectToPort()) {

                            if (trip.getIsAllowMultipleEntries()) {

                                portAccess = portAccessService.processUrgentTripPortEntry(trip.getCode(), vehicleRegNumber, anprEntryScheduler.getParkingPermissionId(),
                                    ServerConstants.DEFAULT_LONG, laneId, dateEvent, ServerConstants.DEFAULT_STRING);

                            } else {
                                portAccess = portAccessService.processVehiclePortEntry(trip.getCode(), "", ServerConstants.DEFAULT_LONG, laneId, dateEvent, ServerConstants.DEFAULT_STRING);
                            }

                        } else {
                            // check if there was a parking session for this trip
                            Parking parking = parkingService.getParkingSessionByTripId(trip.getId());
                            if (parking == null)
                                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, "No parking session associated with this trip. tripId=" + trip.getId(),
                                    "processAnprEvent#No parking session associated with this trip. tripId=" + trip.getId());

                            if (parking.getStatus() != com.pad.server.base.common.ServerConstants.PARKING_STATUS_EXIT)
                                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE,
                                    "Parking session associated with tripId=" + trip.getId() + " has an unexpected status. status=" + parking.getStatus(),
                                    "processAnprEvent#Parking session associated with tripId=" + trip.getId() + " has an unexpected status. status=" + parking.getStatus());

                            if (!parking.getIsEligiblePortEntry())
                                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE,
                                    "Parking session associated with tripId=" + trip.getId() + " has flag isEligiblePortEntry set to false",
                                    "processAnprEvent#Parking session associated with tripId=" + trip.getId() + " has flag isEligiblePortEntry set to false");

                            portAccess = portAccessService.processVehiclePortEntry(trip.getCode(), parking.getCode(), ServerConstants.DEFAULT_LONG, laneId, dateEvent, ServerConstants.DEFAULT_STRING);
                        }

                        anprEntryScheduler.setMissionId(portAccess.getMissionId());
                        anprEntryScheduler.setTripId(portAccess.getTripId());
                    }

                } else if ((anprEntryScheduler.getZoneIdFrom() == anprParameter.getAnprZoneIdMole2() || anprEntryScheduler.getZoneIdFrom() == anprParameter.getAnprZoneIdMole4()
                    || anprEntryScheduler.getZoneIdFrom() == anprParameter.getAnprZoneIdMole8()) && anprEntryScheduler.getZoneIdTo() == anprParameter.getAnprZoneIdOutside()) {
                    // PORT EXIT
                    //
                    PortAccessJson portAccessJson = portAccessService.findEnteredVehicleByRegNumber(vehicleRegNumber);

                    if (portAccessJson != null) {
                        portAccessService.processVehiclePortExit(portAccessJson.getCode(), "", ServerConstants.DEFAULT_LONG, laneId, dateEvent);

                        if (!StringUtils.isBlank(portAccessJson.getTripCode())) {

                            Trip trip = tripService.getTripByCode(portAccessJson.getTripCode());

                            anprEntryScheduler.setMissionId(trip.getMission().getId());
                            anprEntryScheduler.setTripId(trip.getId());
                        }
                    }
                }

                break;

            case (int) ServerConstants.ENTRY_EVENT_TYPE_ID_NO_PERMISSION:
            case (int) ServerConstants.ENTRY_EVENT_TYPE_ID_NO_PERMISSION_FOR_ZONE:
                // ANPR recognised the plate for a truck that didn't have valid parking permission or it had expired before vehicle exited port
                //
                if ((anprEntryScheduler.getZoneIdFrom() == anprParameter.getAnprZoneIdMole2() || anprEntryScheduler.getZoneIdFrom() == anprParameter.getAnprZoneIdMole4()
                    || anprEntryScheduler.getZoneIdFrom() == anprParameter.getAnprZoneIdMole8()) && anprEntryScheduler.getZoneIdTo() == anprParameter.getAnprZoneIdOutside()) {

                    PortAccessJson portAccessJson = portAccessService.findEnteredVehicleByRegNumber(vehicleRegNumber);

                    if (portAccessJson != null) {
                        portAccessService.processVehiclePortExit(portAccessJson.getCode(), "", ServerConstants.DEFAULT_LONG, laneId, dateEvent);

                        if (!StringUtils.isBlank(portAccessJson.getTripCode())) {

                            Trip trip = tripService.getTripByCode(portAccessJson.getTripCode());

                            anprEntryScheduler.setMissionId(trip.getMission().getId());
                            anprEntryScheduler.setTripId(trip.getId());
                        }
                    }

                } else
                    throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, "Vehicle with reg: " + vehicleRegNumber + " didn't have a valid parking permission",
                        ServerConstants.PROCESS_ANPR_EVENT_NO_PARKING_PERMISSION_RESPONSE_SOURCE);

                break;

            default:
                break;
        }
    }

}
