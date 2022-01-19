package com.pad.server.base.services.anpr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.TimestampType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.entities.Anpr;
import com.pad.server.base.entities.AnprEntryLog;
import com.pad.server.base.entities.AnprLog;
import com.pad.server.base.entities.AnprParameter;
import com.pad.server.base.entities.AnprScheduler;
import com.pad.server.base.entities.PortAccessWhitelist;
import com.pad.server.base.entities.SystemParameter;
import com.pad.server.base.entities.Trip;

@Service
@Transactional
public class AnprBaseServiceImpl implements AnprBaseService {

    private static final Logger   logger              = Logger.getLogger(AnprBaseServiceImpl.class);

    @Autowired
    private HibernateTemplate     hibernateTemplate;

    @Autowired
    private JdbcTemplate          jdbcTemplate;

    @Autowired
    private SessionFactory        sessionFactory;

    private SystemParameter       systemParameter;

    private List<SystemParameter> systemParameterList = new CopyOnWriteArrayList<>();

    private AnprParameter         anprParameter;

    private List<AnprParameter>   anprParameterList   = new CopyOnWriteArrayList<>();

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {

        systemParameterList = (List<SystemParameter>) hibernateTemplate.find("FROM SystemParameter");
        if (systemParameterList != null && !systemParameterList.isEmpty()) {
            systemParameter = systemParameterList.get(0);
            logger.info("init###" + systemParameter.toString());
        }

        anprParameterList = (List<AnprParameter>) hibernateTemplate.find("FROM AnprParameter");
        if (anprParameterList != null && !anprParameterList.isEmpty()) {
            anprParameter = anprParameterList.get(0);
            logger.info("init###" + anprParameter.toString());
        }
    }

    @Override
    public AnprParameter getAnprParameter() {
        return anprParameter;
    }

    @Override
    public void updateAnprParameter(AnprParameter anprParameter) {

        logger.info("updateAnprParameter#" + anprParameter.toString());

        hibernateTemplate.update(anprParameter);

        this.anprParameter = anprParameter;

    }

    @Override
    public SystemParameter getSystemParameter() {
        return systemParameter;
    }

    @Override
    public void updateSystemParameter(SystemParameter systemParameter) {

        logger.info("updateSystemParameter#" + systemParameter.toString());

        hibernateTemplate.update(systemParameter);

        this.systemParameter = systemParameter;
    }

    @Override
    public void scheduleAnpr(int requestType, long zoneId, Trip trip, PortAccessWhitelist portAccessWhitelist, Date parkingExitDate, Date dateScheduled) throws Exception {

        AnprParameter anprParameter = getAnprParameter();

        final Calendar calendarValidFrom = Calendar.getInstance();
        final Calendar calendarValidTo = Calendar.getInstance();

        long missionId = trip == null ? ServerConstants.DEFAULT_LONG : trip.getMission().getId();
        long tripId = trip == null ? ServerConstants.DEFAULT_LONG : trip.getId();
        String vehicleRegistration = trip == null ? "" : trip.getVehicleRegistration();

        Anpr anpr = new Anpr();
        anpr.setRequestType(requestType);
        anpr.setMissionId(missionId);
        anpr.setTripId(tripId);
        anpr.setPortAccessWhitelistId(portAccessWhitelist == null ? ServerConstants.DEFAULT_LONG : portAccessWhitelist.getId());
        anpr.setVehicleRegistration(vehicleRegistration);
        anpr.setParkingPermissionId(ServerConstants.DEFAULT_LONG);
        anpr.setPriority(ServerConstants.ANPR_PRIORITY_NORMAL);

        switch (requestType) {

            case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_ENTRY:
            case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_DIRECT:
                if (!trip.getIsDirectToPort() && trip.getIsAllowMultipleEntries()) {
                    calendarValidTo.setTime(trip.getMission().getDateMissionEnd());

                } else {
                    calendarValidTo.setTime(trip.getDateSlotApproved());
                    calendarValidTo.add(Calendar.HOUR_OF_DAY, anprParameter.getParkingPermissionHoursAfterSlotDate());
                    calendarValidTo.add(Calendar.SECOND, -1);
                }

                calendarValidFrom.setTime(trip.getDateSlotApproved());
                calendarValidFrom.add(Calendar.HOUR_OF_DAY, anprParameter.getParkingPermissionHoursPriorSlotDate() * -1);

                anpr.setZoneId(zoneId);
                anpr.setDateValidFrom(calendarValidFrom.getTime());
                anpr.setDateValidTo(calendarValidTo.getTime());

                break;

            case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_EXIT:
            case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_REENTRY_AFTER_PREM_EXIT:
                calendarValidTo.setTime(new Date());
                calendarValidTo.add(Calendar.HOUR_OF_DAY, 1);
                calendarValidTo.add(Calendar.SECOND, -1);

                anpr.setZoneId(zoneId);
                anpr.setDateValidFrom(new Date());
                anpr.setDateValidTo(calendarValidTo.getTime());
                anpr.setPriority(ServerConstants.ANPR_PRIORITY_HIGH);

                break;

            case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY:
                anpr.setZoneId(zoneId);

                calendarValidTo.setTime(parkingExitDate);
                calendarValidTo.add(Calendar.HOUR_OF_DAY, anprParameter.getParkingPermissionHoursAfterExitDate());
                calendarValidTo.add(Calendar.SECOND, -1);

                anpr.setDateValidFrom(parkingExitDate);
                anpr.setDateValidTo(calendarValidTo.getTime());

                break;

            case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_WHITELISTED:
                anpr.setZoneId(zoneId);
                anpr.setDateValidFrom(trip.getMission().getDateMissionStart());
                anpr.setDateValidTo(trip.getMission().getDateMissionEnd());

                break;

            case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_URGENT:
                anpr.setZoneId(zoneId);
                anpr.setDateValidFrom(trip.getDateSlotApproved());
                anpr.setDateValidTo(trip.getMission().getDateMissionEnd());

                break;

            case ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_EXIT:
                anpr.setZoneId(zoneId);

                calendarValidTo.setTime(new Date());
                calendarValidTo.add(Calendar.HOUR_OF_DAY, 1);
                calendarValidTo.add(Calendar.SECOND, -1);

                anpr.setDateValidFrom(new Date());
                anpr.setDateValidTo(calendarValidTo.getTime());

                break;

            case ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PARKING_ENTRY_STATUS_DISABLED:
            case ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PARKING_ENTRY_STATUS_ENABLED:
                anpr.setDateValidFrom(new Date(0l));
                anpr.setDateValidTo(new Date(0l));

                if (trip != null && trip.getParkingPermissionIdParkingEntry() != ServerConstants.DEFAULT_LONG) {
                    anpr.setParkingPermissionId(trip.getParkingPermissionIdParkingEntry());
                } else
                    // no parking permission id available so no need to schedule update permission request
                    return;

                break;

            case ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_ENTRY_STATUS_DISABLED:
            case ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_ENTRY_STATUS_ENABLED:
                anpr.setDateValidFrom(new Date(0l));
                anpr.setDateValidTo(new Date(0l));

                if (trip != null && trip.getParkingPermissionIdPortEntry() != ServerConstants.DEFAULT_LONG) {
                    anpr.setParkingPermissionId(trip.getParkingPermissionIdPortEntry());
                } else
                    // no parking permission id available so no need to schedule update permission request
                    return;

                break;

            case ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_EXIT_STATUS_DISABLED:
                anpr.setDateValidFrom(new Date(0l));
                anpr.setDateValidTo(new Date(0l));

                if (trip != null && trip.getParkingPermissionIdPortExit() != ServerConstants.DEFAULT_LONG) {
                    anpr.setParkingPermissionId(trip.getParkingPermissionIdPortExit());
                } else
                    // no parking permission id available so no need to schedule update permission request
                    return;

                break;

            case ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_ENTRY:
                anpr.setDateValidFrom(new Date(0l));
                anpr.setDateValidTo(new Date(0l));

                if (trip != null && trip.getParkingPermissionIdParkingEntry() != ServerConstants.DEFAULT_LONG) {
                    anpr.setParkingPermissionId(trip.getParkingPermissionIdParkingEntry());
                } else
                    // no parking permission id available so no need to schedule the delete permission request
                    return;

                break;

            case ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_EXIT:
                anpr.setDateValidFrom(new Date(0l));
                anpr.setDateValidTo(new Date(0l));

                if (trip != null && trip.getParkingPermissionIdParkingExit() != ServerConstants.DEFAULT_LONG) {
                    anpr.setParkingPermissionId(trip.getParkingPermissionIdParkingExit());

                } else if (trip != null && trip.getParkingPermissionIdParkingExit() == ServerConstants.DEFAULT_LONG) {
                    // check if there exists a parking exit request in queue that has not been processed yet. This can happen in case ParkIT is down.
                    // if that's the case then mark the request as processed since vehicle already exited parking manually.
                    AnprLog anprLogForParkingPermissionParkingExit = getAnprLogByRequestTypeAndParkingPermissionIdAndTripId(
                        ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_EXIT, ServerConstants.DEFAULT_LONG, tripId);

                    if (anprLogForParkingPermissionParkingExit != null && anprLogForParkingPermissionParkingExit.getIsProcessed() != ServerConstants.PROCESS_PROCESSED) {

                        anprLogForParkingPermissionParkingExit.setIsProcessed(ServerConstants.PROCESS_PROCESSED);
                        anprLogForParkingPermissionParkingExit.setDateProcessed(new Date());
                        anprLogForParkingPermissionParkingExit.setResponseCode(ServerResponseConstants.PARKING_PERMISSION_MANUAL_PARKING_EXIT_CODE);
                        anprLogForParkingPermissionParkingExit.setResponseText("Manual parking exit. Scheduled ANPR permission request cancelled");

                        updateAnprLog(anprLogForParkingPermissionParkingExit);
                        deleteScheduledAnpr(anprLogForParkingPermissionParkingExit.getId());
                    }
                    return;

                } else
                    // no parking permission id available so no need to schedule the delete permission request
                    return;

                break;

            case ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_ENTRY:
                anpr.setDateValidFrom(new Date(0l));
                anpr.setDateValidTo(new Date(0l));

                if (trip != null && trip.getParkingPermissionIdPortEntry() != ServerConstants.DEFAULT_LONG) {
                    anpr.setParkingPermissionId(trip.getParkingPermissionIdPortEntry());
                } else if (portAccessWhitelist != null && portAccessWhitelist.getParkingPermissionId() != ServerConstants.DEFAULT_LONG) {
                    anpr.setParkingPermissionId(portAccessWhitelist.getParkingPermissionId());
                } else
                    // no parking permission id available so no need to schedule the delete permission request
                    return;

                break;

            case ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_EXIT:
                anpr.setDateValidFrom(new Date(0l));
                anpr.setDateValidTo(new Date(0l));

                if (trip != null && trip.getParkingPermissionIdPortExit() != ServerConstants.DEFAULT_LONG) {
                    anpr.setParkingPermissionId(trip.getParkingPermissionIdPortExit());
                } else
                    // no parking permission id available so no need to schedule the delete permission request
                    return;

                break;

            default:
                anpr.setDateValidFrom(new Date(0l));
                anpr.setDateValidTo(new Date(0l));
                break;
        }

        anpr.setDateScheduled(dateScheduled);

        scheduleAnpr(anpr);
    }

    private void scheduleAnpr(Anpr anpr) {

        try {
            anpr.setIsProcessed(ServerConstants.PROCESS_NOTPROCESSED);

            anpr.setDateCreated(new Date());
            anpr.setRetryCount(0);
            anpr.setResponseCode(ServerConstants.DEFAULT_INT);
            anpr.setResponseText(ServerConstants.DEFAULT_STRING);

            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource()).withTableName("anpr_log").usingGeneratedKeyColumns("id");

            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("is_processed", anpr.getIsProcessed());
            parameters.addValue("request_type", anpr.getRequestType());
            parameters.addValue("zone_id", anpr.getZoneId());
            parameters.addValue("mission_id", anpr.getMissionId());
            parameters.addValue("trip_id", anpr.getTripId());
            parameters.addValue("port_access_whitelist_id", anpr.getPortAccessWhitelistId());
            parameters.addValue("vehicle_registration", anpr.getVehicleRegistration());
            parameters.addValue("parking_permission_id", anpr.getParkingPermissionId());
            parameters.addValue("date_valid_from", anpr.getDateValidFrom());
            parameters.addValue("date_valid_to", anpr.getDateValidTo());
            parameters.addValue("priority", anpr.getPriority());
            parameters.addValue("date_created", anpr.getDateCreated());
            parameters.addValue("date_scheduled", anpr.getDateScheduled());
            parameters.addValue("retry_count", anpr.getRetryCount());
            parameters.addValue("response_code", anpr.getResponseCode());
            parameters.addValue("response_text", anpr.getResponseText());

            anpr.setId(jdbcInsert.executeAndReturnKey(parameters).longValue());

            jdbcTemplate.update(
                "INSERT INTO anpr_scheduler(id, is_processed, request_type, zone_id, mission_id, trip_id, port_access_whitelist_id, vehicle_registration, parking_permission_id, date_valid_from, date_valid_to, priority, date_created, date_scheduled, retry_count, date_processed, response_code, response_text) "
                    + "SELECT id, is_processed, request_type, zone_id, mission_id, trip_id, port_access_whitelist_id, vehicle_registration, parking_permission_id, date_valid_from, date_valid_to, priority, date_created, date_scheduled, retry_count, date_processed, response_code, response_text FROM anpr_log WHERE id = ?",
                anpr.getId());

        } catch (Exception e) {
            logger.error("scheduleAnpr###Exception: ", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public AnprLog getAnprLogByRequestTypeAndParkingPermissionIdAndTripId(int requestType, long parkingPermissionId, long tripId) {

        AnprLog anprLog = null;

        String[] paramNames = { "requestType", "parkingPermissionId", "tripId" };
        Object[] paramValues = { requestType, parkingPermissionId, tripId };

        String hql = "FROM AnprLog anprLog WHERE anprLog.requestType = :requestType AND anprLog.parkingPermissionId = :parkingPermissionId AND anprLog.tripId = :tripId ORDER BY anprLog.id DESC";

        List<AnprLog> anprLogList = (List<AnprLog>) hibernateTemplate.findByNamedParam(hql, paramNames, paramValues);

        if (anprLogList != null && !anprLogList.isEmpty()) {
            anprLog = anprLogList.get(0);
        }

        return anprLog;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AnprLog getAnprLogByRequestTypeAndVehicleRegAndDateSlot(int requestType, String vehicleRegistration, int hoursBefore, Date dateSlotApproved) {

        Calendar calendarDateSlotApprovedMinusXHours = Calendar.getInstance();
        calendarDateSlotApprovedMinusXHours.setTime(dateSlotApproved);
        calendarDateSlotApprovedMinusXHours.add(Calendar.HOUR_OF_DAY, hoursBefore * -1);

        AnprLog anprLog = null;

        String[] paramNames = { "requestType", "vehicleRegistration", "dateSlotApprovedMinusXHours" };
        Object[] paramValues = { requestType, vehicleRegistration, calendarDateSlotApprovedMinusXHours.getTime() };

        String hql = "FROM AnprLog anprLog WHERE anprLog.requestType = :requestType AND anprLog.vehicleRegistration = :vehicleRegistration "
            + "AND (:dateSlotApprovedMinusXHours BETWEEN dateValidFrom AND dateValidTo ) ORDER BY anprLog.id DESC";

        List<AnprLog> anprLogList = (List<AnprLog>) hibernateTemplate.findByNamedParam(hql, paramNames, paramValues);

        if (anprLogList != null && !anprLogList.isEmpty()) {
            anprLog = anprLogList.get(0);
        }

        return anprLog;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AnprEntryLog> getUnsuccessfulEventsFromAnprEntryLog(Date dateFrom, Date dateTo) {

        List<AnprEntryLog> anprEntryLogList = new ArrayList<>();

        Session currentSession = sessionFactory.getCurrentSession();

        anprEntryLogList = currentSession
            .createQuery(
                "FROM AnprEntryLog ael where ael.isProcessed=:is_processed and ael.dateCreated>:date_created_from and ael.dateCreated<=:date_created_to ORDER BY ael.id DESC")
            .setParameter("is_processed", ServerConstants.PROCESS_FAILED).setParameter("date_created_from", dateFrom, TimestampType.INSTANCE)
            .setParameter("date_created_to", dateTo, TimestampType.INSTANCE).list();

        return anprEntryLogList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isVehicleProcessedParkingEntryThroughANPRInTheLastHour(Trip trip, long entryLaneId) {

        List<AnprEntryLog> anprEntryLogList = new ArrayList<>();

        Session currentSession = sessionFactory.getCurrentSession();

        anprEntryLogList = currentSession.createQuery(
            "FROM AnprEntryLog ael where ael.isProcessed=:is_processed and ael.responseCode=:response_code and ael.plateNumber=:vehicle_registration and ael.tripId=:trip_id and ael.laneId=:lane_id "
                + "and ael.entryEventTypeId=:entry_event_type_id and ael.zoneIdFrom=:zone_id_from and ael.zoneIdTo=:zone_id_to and ael.dateProcessed>:date_today_minus_1hr ORDER BY ael.id DESC")
            .setParameter("is_processed", ServerConstants.PROCESS_PROCESSED).setParameter("response_code", ServerResponseConstants.SUCCESS_CODE)
            .setParameter("vehicle_registration", trip.getVehicleRegistration()).setParameter("trip_id", trip.getId()).setParameter("lane_id", entryLaneId)
            .setParameter("entry_event_type_id", ServerConstants.ENTRY_EVENT_TYPE_ID_APPROVED).setParameter("zone_id_from", this.anprParameter.getAnprZoneIdOutside())
            .setParameter("zone_id_to", this.anprParameter.getAnprZoneIdAgsparking())
            .setParameter("date_today_minus_1hr", new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(60)), TimestampType.INSTANCE).list();

        return (anprEntryLogList != null && !anprEntryLogList.isEmpty()) ? true : false;
    }

    @Override
    public void updateAnprLog(AnprLog anprLog) {

        logger.info("updateAnprLog#" + anprLog.toString());

        hibernateTemplate.update(anprLog);
    }

    @Override
    public void deleteScheduledAnpr(long anprId) {

        hibernateTemplate.delete(hibernateTemplate.get(AnprScheduler.class, anprId));
    }

    @Override
    public String getEntryLaneVideoFeddUrlByLaneNumber(int laneNumber) {

        switch (laneNumber) {
            case 1:
                return this.getAnprParameter().getAgsparkingEntryLane1VideoFeedUrl();

            case 2:
                return this.getAnprParameter().getAgsparkingEntryLane2VideoFeedUrl();

            case 3:
                return this.getAnprParameter().getAgsparkingEntryLane3VideoFeedUrl();

            case 4:
                return this.getAnprParameter().getAgsparkingEntryLane4VideoFeedUrl();

            case 5:
                return this.getAnprParameter().getAgsparkingEntryLane5VideoFeedUrl();

            default:
                return ServerConstants.DEFAULT_STRING;
        }
    }
}
