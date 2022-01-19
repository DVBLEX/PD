package com.pad.server.anpr.tasks;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.anpr.common.ServerConstants;
import com.pad.server.anpr.services.anpr.AnprService;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.Anpr;
import com.pad.server.base.entities.AnprLog;
import com.pad.server.base.entities.AnprParameter;
import com.pad.server.base.entities.Mission;
import com.pad.server.base.entities.PortAccess;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.anpr.AnprBaseService;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.mission.MissionService;
import com.pad.server.base.services.portaccess.PortAccessService;
import com.pad.server.base.services.system.SystemService;
import com.pad.server.base.services.trip.TripService;

@Component
@Transactional
public class AnprParkingPermissionTaskExecutor implements Runnable {

    private static final Logger logger = Logger.getLogger(AnprParkingPermissionTaskExecutor.class);

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private AccountService      accountService;

    @Autowired
    private EmailService        emailService;

    @Autowired
    private MissionService      missionService;

    @Autowired
    private PortAccessService   portAccessService;

    @Autowired
    private SystemService       systemService;

    @Autowired
    private TripService         tripService;

    @Autowired
    private AnprBaseService     anprBaseService;

    @Autowired
    private AnprService         anprService;

    @Override
    @Scheduled(fixedDelay = 60000, initialDelay = 20000)
    public void run() {

        try {
            // logger.info("run#");

            systemService.updateSystemTimerTaskDateLastRun(ServerConstants.SYSTEM_TIMER_TASK_ANPR_PARKING_PERMISSION_ID, new Date());

            AnprParameter anprParameter = anprBaseService.getAnprParameter();

            List<Trip> tripList = tripService.getApprovedTripsInNextXHours(anprParameter.getParkingPermissionHoursInFuture(),
                anprParameter.getParkingPermissionHoursPriorSlotDate());

            Set<String> vehicleRegHashSet = new HashSet<>();

            AnprLog anprLogEntryRequestForVehicle = null;
            AnprLog anprLogDeletePermissionRequest = null;
            AnprLog anprLogPortExitPermissionRequest = null;

            for (Trip trip : tripList) {

                if (vehicleRegHashSet.add(trip.getVehicleRegistration()) == false) {
                    // competing booking found in the next x hours for the same vehicle, ignore processing it in this run.

                } else {

                    if (trip.getIsDirectToPort()) {
                        // GOES DIRECT TO PORT

                        // check for any parking permission request already scheduled for this vehicle with overlapping time for the parking permission that is to be
                        // created
                        anprLogEntryRequestForVehicle = anprBaseService.getAnprLogByRequestTypeAndVehicleRegAndDateSlot(
                            (trip.getIsAllowMultipleEntries() ? ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_URGENT
                                : ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_DIRECT),
                            trip.getVehicleRegistration(), anprParameter.getParkingPermissionHoursPriorSlotDate(), trip.getDateSlotApproved());

                        if (anprLogEntryRequestForVehicle == null) {

                            // check if theres any port exit permission for that vehicle. if there is then disable it and update the corresponding trip status
                            anprLogPortExitPermissionRequest = anprBaseService.getAnprLogByRequestTypeAndVehicleRegAndDateSlot(
                                ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_EXIT, trip.getVehicleRegistration(),
                                anprParameter.getParkingPermissionHoursPriorSlotDate(), trip.getDateSlotApproved());

                            if (anprLogPortExitPermissionRequest != null) {
                                Trip oldTrip = tripService.getTripById(anprLogPortExitPermissionRequest.getTripId());

                                if (oldTrip != null && oldTrip.getStatus() == com.pad.server.base.common.ServerConstants.TRIP_STATUS_ENTERED_PORT) {

                                    // remove the disabled port entry parking permission
                                    anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_ENTRY, ServerConstants.DEFAULT_LONG, oldTrip,
                                        null, null, new Date());

                                    // disable the port exit parking permission
                                    anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_EXIT_STATUS_DISABLED,
                                        ServerConstants.DEFAULT_LONG, oldTrip, null, null, new Date(System.currentTimeMillis() + 5l * 1000l));

                                    // remove the disabled port exit parking permission
                                    anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_EXIT, ServerConstants.DEFAULT_LONG, oldTrip,
                                        null, null, new Date(System.currentTimeMillis() + 5l * 1000l));

                                    PortAccess portAccess = portAccessService.getPortAccessByTripId(oldTrip.getId());

                                    if (portAccess != null && portAccess.getStatus() == com.pad.server.base.common.ServerConstants.PORT_ACCESS_STATUS_ENTRY) {
                                        portAccess.setStatus(com.pad.server.base.common.ServerConstants.PORT_ACCESS_STATUS_EXIT_CLOSED_BY_SYSTEM);
                                        portAccess.setOperatorId(ServerConstants.DEFAULT_LONG);
                                        portAccess.setExitLaneId(ServerConstants.DEFAULT_LONG);
                                        portAccess.setDateExit(new Date());
                                        portAccess.setDateEdited(new Date());

                                        portAccessService.updatePortAccess(portAccess);
                                    }

                                    oldTrip.setStatus(com.pad.server.base.common.ServerConstants.TRIP_STATUS_COMPLETED_SYSTEM);
                                    oldTrip.setDateEdited(new Date());

                                    tripService.updateTrip(oldTrip);

                                    Mission mission = oldTrip.getMission();
                                    mission.setTripsCompletedCount(mission.getTripsCompletedCount() + 1);

                                    missionService.updateMission(mission);
                                }
                            }

                            schedulePortEntryPermission(trip);

                        } else {
                            if (anprLogEntryRequestForVehicle.getResponseCode() == ServerResponseConstants.SUCCESS_CODE) {
                                // there was a previous parking permission request already scheduled for this vehicle which overlaps the time for the parking permission
                                // that is to be created
                                // in that case delete the parking permission for the previous trip and create a port entry permission for the new trip
                                Trip oldTrip = tripService.getTripById(anprLogEntryRequestForVehicle.getTripId());

                                // check if parking permission was already scheduled for deletion
                                anprLogDeletePermissionRequest = anprBaseService.getAnprLogByRequestTypeAndParkingPermissionIdAndTripId(
                                    ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_ENTRY, oldTrip.getParkingPermissionIdPortEntry(), oldTrip.getId());

                                if (anprLogDeletePermissionRequest == null) {
                                    // remove the parking permission
                                    anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_ENTRY, ServerConstants.DEFAULT_LONG, oldTrip,
                                        null, null, new Date());

                                    anprLogPortExitPermissionRequest = anprBaseService.getAnprLogByRequestTypeAndParkingPermissionIdAndTripId(
                                        ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_EXIT, oldTrip.getParkingPermissionIdPortExit(), oldTrip.getId());

                                    if (anprLogPortExitPermissionRequest != null) {
                                        // disable the last port exit permission for this vehicle
                                        anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_EXIT_STATUS_DISABLED,
                                            ServerConstants.DEFAULT_LONG, oldTrip, null, null, new Date());

                                        // remove the disabled port exit parking permission
                                        anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_EXIT, ServerConstants.DEFAULT_LONG,
                                            oldTrip, null, null, new Date(System.currentTimeMillis() + 5l * 1000l));
                                    }
                                }

                                schedulePortEntryPermission(trip);

                            } else {
                                if (anprLogEntryRequestForVehicle.getIsProcessed() != ServerConstants.PROCESS_PROCESSED) {
                                    anprLogEntryRequestForVehicle.setIsProcessed(ServerConstants.PROCESS_PROCESSED);

                                    Anpr anpr = new Anpr();
                                    BeanUtils.copyProperties(anprLogEntryRequestForVehicle, anpr);

                                    anprService.updateAnpr(anpr);
                                    anprService.deleteScheduledAnpr(anpr.getId());
                                }
                            }
                        }

                    } else {
                        // GOES THROUGH PARKING

                        // check for any parking permission request already scheduled for this vehicle with overlapping time for the parking permission that is to be
                        // created
                        anprLogEntryRequestForVehicle = anprBaseService.getAnprLogByRequestTypeAndVehicleRegAndDateSlot(
                            ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_ENTRY, trip.getVehicleRegistration(),
                            anprParameter.getParkingPermissionHoursPriorSlotDate(), trip.getDateSlotApproved());

                        if (anprLogEntryRequestForVehicle == null) {
                            scheduleParkingEntryPermission(trip);

                        } else {
                            if (anprLogEntryRequestForVehicle.getResponseCode() == ServerResponseConstants.SUCCESS_CODE) {
                                // there was a previous parking permission request already scheduled for this vehicle which overlaps the time for the parking permission
                                // that is to be created
                                // in that case delete the parking permission for the previous trip and don't create a parking permission for the new trip
                                Trip oldTrip = tripService.getTripById(anprLogEntryRequestForVehicle.getTripId());

                                // check if parking permission was already scheduled for deletion

                                anprLogDeletePermissionRequest = anprBaseService.getAnprLogByRequestTypeAndParkingPermissionIdAndTripId(
                                    ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_ENTRY, oldTrip.getParkingPermissionIdParkingEntry(), oldTrip.getId());

                                if (anprLogDeletePermissionRequest == null) {
                                    // remove the parking permission

                                    anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_ENTRY, ServerConstants.DEFAULT_LONG,
                                        oldTrip, null, null, new Date());

                                    // set parking permission id in trip that was excluded from creating its parking permission. in that way it won't get picked up again by
                                    // timer task
                                    trip.setParkingPermissionId(oldTrip.getParkingPermissionIdParkingEntry());

                                    tripService.updateTrip(trip);
                                }

                            } else {
                                if (anprLogEntryRequestForVehicle.getIsProcessed() != ServerConstants.PROCESS_PROCESSED) {
                                    anprLogEntryRequestForVehicle.setIsProcessed(ServerConstants.PROCESS_PROCESSED);

                                    Anpr anpr = new Anpr();
                                    BeanUtils.copyProperties(anprLogEntryRequestForVehicle, anpr);

                                    anprService.updateAnpr(anpr);
                                    anprService.deleteScheduledAnpr(anpr.getId());
                                }
                            }
                        }
                    }
                }
            }

        } catch (DataAccessException dae) {
            logger.error("run###DataAccessException: ", dae);

            emailService.sendSystemEmail("AnprParkingPermissionTaskExecutor DataAccessException", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "AnprParkingPermissionTaskExecutor#run###DataAccessException:<br />" + dae.getMessage());

        } catch (Exception e) {
            logger.error("run###Exception: ", e);

            emailService.sendSystemEmail("AnprParkingPermissionTaskExecutor Exception", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "AnprParkingPermissionTaskExecutor#run###Exception:<br />" + e.getCause() + " <br />" + e.getMessage());
        }
    }

    private void scheduleParkingEntryPermission(Trip trip) throws DataAccessException, Exception {

        AnprParameter anprParameter = anprBaseService.getAnprParameter();

        if (isAnprParkingPermissionAllowed(trip)) {

            jdbcTemplate.update("UPDATE trips SET parking_permission_id = " + ServerConstants.PARKING_PERMISSION_ID_PICKED_FOR_PROCESSING + " WHERE id = ?", trip.getId());

            anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_ENTRY, anprParameter.getAnprZoneIdAgsparking(), trip, null, null,
                new Date());
        }
    }

    private void schedulePortEntryPermission(Trip trip) throws DataAccessException, Exception {

        if (isAnprParkingPermissionAllowed(trip)) {

            jdbcTemplate.update("UPDATE trips SET parking_permission_id = " + ServerConstants.PARKING_PERMISSION_ID_PICKED_FOR_PROCESSING + " WHERE id = ?", trip.getId());

            if (trip.getIsAllowMultipleEntries()) {
                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_URGENT,
                    systemService.getPortOperatorAnprZoneIdById(trip.getPortOperatorGateId()), trip, null, null, new Date());
            } else {
                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_DIRECT,
                    systemService.getPortOperatorAnprZoneIdById(trip.getPortOperatorGateId()), trip, null, null, new Date());
            }
        }
    }

    private boolean isAnprParkingPermissionAllowed(Trip trip) {

        boolean isAnprParkingPermissionAllowed = false;

        if (trip != null && trip.getAccountId() != ServerConstants.DEFAULT_LONG) {

            Account account = accountService.getAccountById(trip.getAccountId());

            if (account != null) {

                BigDecimal tripFeeAmount = trip.getFeeAmount();
                BigDecimal accountAmountHold = account.getAmountHold().abs();
                BigDecimal accountBalanceMinusTripFee = account.getBalanceAmount().subtract(tripFeeAmount);
                BigDecimal accountBalanceMinusTripFeeMinusAmountHold = accountBalanceMinusTripFee.subtract(accountAmountHold);

                if (accountBalanceMinusTripFeeMinusAmountHold.compareTo(BigDecimal.ZERO) < 0) {

                    if (accountBalanceMinusTripFeeMinusAmountHold.abs().compareTo(account.getAmountOverdraftLimit()) > 0) {

                        isAnprParkingPermissionAllowed = false;

                    } else {
                        isAnprParkingPermissionAllowed = true;
                    }

                } else {
                    isAnprParkingPermissionAllowed = true;
                }
            }
        }

        return isAnprParkingPermissionAllowed;
    }
}
