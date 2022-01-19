package com.pad.server.base.tasks;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.BookingSlotCount;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.entities.Mission;
import com.pad.server.base.entities.Parking;
import com.pad.server.base.entities.PortAccess;
import com.pad.server.base.entities.PortOperatorTransactionType;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.anpr.AnprBaseService;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.mission.MissionService;
import com.pad.server.base.services.parking.ParkingService;
import com.pad.server.base.services.portaccess.PortAccessService;
import com.pad.server.base.services.system.SystemService;
import com.pad.server.base.services.trip.TripService;

@Component
@Transactional
public class TripAnprExpiryTaskExecutor implements Runnable {

    private static final Logger logger = Logger.getLogger(TripAnprExpiryTaskExecutor.class);

    @Autowired
    private AccountService      accountService;

    @Autowired
    private AnprBaseService     anprBaseService;

    @Autowired
    private EmailService        emailService;

    @Autowired
    private MissionService      missionService;

    @Autowired
    private ParkingService      parkingService;

    @Autowired
    private PortAccessService   portAccessService;

    @Autowired
    private SystemService       systemService;

    @Autowired
    private TripService         tripService;

    @Override
    @Scheduled(fixedDelay = 300000, initialDelay = 30000)
    public void run() {

        try {
            logger.info("run#");

            systemService.updateSystemTimerTaskDateLastRun(ServerConstants.SYSTEM_TIMER_TASK_TRIP_ANPR_EXPIRY_ID, new Date());

            runParkingPermissionCleanupForExpiredTrips();

            runCleanupForExpiredPortEntries();

            runCleanupForTripsInTransit();

            runCleanupForTripsExitedParkingPrematurely();

            runCleanupForExpiredUnpaidDirectToPortTripsCreatedByVirtualKiosk();

            runCleanupForOldTrips();
            
            runAmountInHold();

        } catch (DataAccessException dae) {
            logger.error("run###DataAccessException: ", dae);

            emailService.sendSystemEmail("TripAnprExpiryTaskExecutor DataAccessException", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "TripAnprExpiryTaskExecutor#run###DataAccessException:<br />" + dae.getMessage());

        } catch (Exception e) {
            logger.error("run###Exception: ", e);

            emailService.sendSystemEmail("TripAnprExpiryTaskExecutor Exception", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "TripAnprExpiryTaskExecutor#run###Exception:<br />" + e.getCause() + " <br />" + e.getMessage());
        }

    }

    private void runParkingPermissionCleanupForExpiredTrips() throws Exception {

        List<PortOperatorTransactionType> portOperatorTransactionTypeList = systemService.getPortOperatorTransactionTypes();

        for (PortOperatorTransactionType portOperatorTransactionType : portOperatorTransactionTypeList) {

            List<Mission> expiredMissionList = missionService.getExpiredMissions((int) portOperatorTransactionType.getPortOperator().getId(),
                portOperatorTransactionType.getTransactionType(),
                new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(portOperatorTransactionType.getMissionCancelSystemAfterMinutes())));

            if (expiredMissionList != null && !expiredMissionList.isEmpty()) {

                final StringBuilder missionIds = new StringBuilder("TripAnprExpiryTaskExecutor#runParkingPermissionCleanupForExpiredTrips#missionIds=[");
                final StringBuilder tripIds = new StringBuilder("TripAnprExpiryTaskExecutor#runParkingPermissionCleanupForExpiredTrips#tripIds=[");

                for (Mission mission : expiredMissionList) {

                    List<Trip> trips = mission.getTripList();

                    for (Trip trip : trips) {

                        if (trip.getStatus() == ServerConstants.TRIP_STATUS_PENDING_APPROVAL || trip.getStatus() == ServerConstants.TRIP_STATUS_PENDING
                            || trip.getStatus() == ServerConstants.TRIP_STATUS_APPROVED) {

                            if (trip.getStatus() == ServerConstants.TRIP_STATUS_APPROVED && trip.getParkingPermissionIdPortEntry() != ServerConstants.DEFAULT_LONG
                                && trip.getType() == ServerConstants.TRIP_TYPE_BOOKED) {

                                // remove the port entry parking permission
                                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_ENTRY, ServerConstants.DEFAULT_LONG, trip, null,
                                    null, new Date());

                            } else if (trip.getStatus() == ServerConstants.TRIP_STATUS_APPROVED && trip.getParkingPermissionIdParkingEntry() != ServerConstants.DEFAULT_LONG
                                && trip.getType() == ServerConstants.TRIP_TYPE_BOOKED) {

                                // remove the parking entry parking permission
                                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_ENTRY, ServerConstants.DEFAULT_LONG, trip,
                                    null, null, new Date());

                            }

                            trip.setStatus(ServerConstants.TRIP_STATUS_CANCELLED_SYSTEM);

                            tripService.updateTrip(trip);

                            tripIds.append(trip.getId() + ", ");

                        } else if (trip.getStatus() == ServerConstants.TRIP_STATUS_EXITED_PARKING_PREMATURELY) {

                            if (trip.getParkingPermissionIdParkingEntry() != ServerConstants.DEFAULT_LONG) {
                                // remove the parking entry parking permission
                                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_ENTRY, ServerConstants.DEFAULT_LONG, trip,
                                    null, null, new Date());
                            }

                        } else if (trip.getIsDirectToPort() && trip.getIsAllowMultipleEntries() && trip.getStatus() == ServerConstants.TRIP_STATUS_ENTERED_PORT) {
                            // above check is needed because if the trip is direct to port and on multiple entries, system is not going to update trip status when the port session
                            // expires

                            if (trip.getParkingPermissionIdPortEntry() != ServerConstants.DEFAULT_LONG) {
                                // remove the port entry parking permission
                                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_ENTRY, ServerConstants.DEFAULT_LONG, trip, null,
                                    null, new Date());
                            }
                            if (trip.getParkingPermissionIdPortExit() != ServerConstants.DEFAULT_LONG) {
                                // remove the port exit parking permission
                                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_EXIT, ServerConstants.DEFAULT_LONG, trip, null,
                                    null, new Date(System.currentTimeMillis() + 5l * 1000l));
                            }

                            trip.setStatus(ServerConstants.TRIP_STATUS_PORT_EXIT_EXPIRED);

                            tripService.updateTrip(trip);

                            tripIds.append(trip.getId() + ", ");

                        } else if (trip.getIsDirectToPort() && trip.getType() == ServerConstants.TRIP_TYPE_ADHOC && trip.getStatus() == ServerConstants.TRIP_STATUS_IN_TRANSIT
                            && trip.getParkingExitCount() == 0 && trip.getDateExitParking() == null) {
                            // expire IN TRANSIT ad hoc trips created by the virtual kiosk operator. We dont expire them from in transit expiry process because we want to expire
                            // them at the end of the calendar day when the mission expires

                            if (trip.getParkingPermissionIdPortEntry() != ServerConstants.DEFAULT_LONG) {
                                // remove the expired port entry parking permission
                                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_ENTRY, ServerConstants.DEFAULT_LONG, trip, null,
                                    null, new Date());
                            }

                            trip.setStatus(ServerConstants.TRIP_STATUS_IN_TRANSIT_EXPIRED);

                            tripService.updateTrip(trip);

                            tripIds.append(trip.getId() + ", ");
                        }
                    }

                    mission.setStatus(ServerConstants.MISSION_STATUS_EXPIRED);
                    mission.setDateEdited(new Date());

                    missionService.updateMission(mission);

                    missionIds.append(mission.getId() + ", ");
                }

                missionIds.append("]");
                tripIds.append("]");

                logger.info(missionIds.toString());
                logger.info(tripIds.toString());
            }
        }
    }

    private void runCleanupForExpiredPortEntries() throws Exception {

        Date dateToday = new Date();
        List<PortAccess> portAccessList = portAccessService.getExpiredPortEntries(ServerConstants.TRIP_PORT_ENTERED_EXPIRY_MINUTES);

        if (portAccessList != null && !portAccessList.isEmpty()) {

            final StringBuilder portAccessIds = new StringBuilder("TripAnprExpiryTaskExecutor#runCleanupForExpiredPortEntries#portAccessIds=[");

            for (PortAccess portAccess : portAccessList) {

                // Removing North Zone (Mole 4 - gateId 1004 / Mole 8 - gateId 1005) from automatic expiring process. AGS will have staff to exit trucks manually.
                if (portAccess.getPortOperatorGateId() != 1004 && portAccess.getPortOperatorGateId() != 1005) {

                    portAccess.setStatus(ServerConstants.PORT_ACCESS_STATUS_EXIT_CLOSED_BY_SYSTEM);
                    portAccess.setOperatorId(ServerConstants.DEFAULT_LONG);
                    portAccess.setExitLaneId(ServerConstants.DEFAULT_LONG);
                    portAccess.setDateExit(dateToday);
                    portAccess.setDateEdited(dateToday);

                    portAccessService.updatePortAccess(portAccess);

                    portAccessIds.append(portAccess.getId() + ", ");

                    Trip previousTrip = tripService.getTripById(portAccess.getTripId());

                    if (previousTrip != null) {

                        if (previousTrip.getIsDirectToPort() && previousTrip.getIsAllowMultipleEntries()) {

                            previousTrip.setStatus(ServerConstants.TRIP_STATUS_COMPLETED_SYSTEM);

                        } else {
                            previousTrip.setStatus(ServerConstants.TRIP_STATUS_PORT_EXIT_EXPIRED);

                            if (previousTrip.getParkingPermissionIdPortEntry() != ServerConstants.DEFAULT_LONG) {
                                // remove the port entry parking permission
                                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_ENTRY, ServerConstants.DEFAULT_LONG, previousTrip,
                                    null, null, new Date());
                            }
                            if (previousTrip.getParkingPermissionIdPortExit() != ServerConstants.DEFAULT_LONG) {
                                // remove the port exit parking permission
                                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_EXIT, ServerConstants.DEFAULT_LONG, previousTrip,
                                    null, null, new Date(System.currentTimeMillis() + 5l * 1000l));
                            }
                        }

                        previousTrip.setPortExitCount(previousTrip.getPortExitCount() + 1);
                        previousTrip.setDateExitPort(dateToday);

                        tripService.updateTrip(previousTrip);

                        Mission mission = previousTrip.getMission();
                        mission.setTripsCompletedCount(mission.getTripsCompletedCount() + 1);

                        missionService.updateMission(mission);
                    }

                }

            }

            portAccessIds.append("]");
            logger.info(portAccessIds.toString());
        }
    }

    private void runCleanupForTripsInTransit() throws Exception {

        try {
            int tripInTransitValidityMinutes = systemService.getSystemParameter().getInTransitValidityMinutes();

            List<Parking> parkingSessionList = parkingService.getInTransitParkingSessionForMoreThanXMinutes(tripInTransitValidityMinutes);

            final StringBuilder tripIds = new StringBuilder(
                "TripAnprExpiryTaskExecutor#run#Trips IN TRANSIT#tripIdsInTransitForMoreThan" + tripInTransitValidityMinutes + "Minutes=[");

            final StringBuilder parkingSessionIds = new StringBuilder(
                "TripAnprExpiryTaskExecutor#run#Parking Sessions IN TRANSIT#parkingIdsInTransitForMoreThan" + tripInTransitValidityMinutes + "Minutes=[");

            if (parkingSessionList != null && parkingSessionList.size() > 0) {

                for (Parking parkingSession : parkingSessionList) {

                    parkingSession.setStatus(ServerConstants.PARKING_STATUS_IN_TRANSIT_EXPIRED);
                    parkingSession.setDateEdited(new Date());

                    parkingService.updateParking(parkingSession);

                    Trip trip = tripService.getTripById(parkingSession.getTripId());

                    if (trip != null && trip.getStatus() == ServerConstants.TRIP_STATUS_IN_TRANSIT) {

                        if (trip.getParkingPermissionIdPortEntry() != ServerConstants.DEFAULT_LONG) {
                            // remove the expired port entry parking permission
                            anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_ENTRY, ServerConstants.DEFAULT_LONG, trip, null, null,
                                new Date());

                        }

                        trip.setStatus(ServerConstants.TRIP_STATUS_IN_TRANSIT_EXPIRED);

                        tripService.updateTrip(trip);

                        tripIds.append(trip.getId() + ", ");
                    }

                    parkingSessionIds.append(parkingSession.getId() + ", ");
                }

                tripIds.append("]");
                parkingSessionIds.append("]");

                logger.info(tripIds.toString());
                logger.info(parkingSessionIds.toString());
            }

        } catch (HibernateException he) {
            logger.error("runCleanupForTripsInTransit###HibernateException: ", he);

            emailService.sendSystemEmail("runCleanupForTripsInTransit " + he.getClass().getSimpleName(), EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "TripAnprExpiryTaskExecutor#runCleanupForTripsInTransit###" + he.getClass().getSimpleName() + ":<br />Cause: " + he.getCause() + "<br />Message: " + he.getMessage()
                    + "<br />Stacktrace: " + he.getStackTrace());

            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "runCleanupForTripsInTransit#1");

        } catch (Exception e) {
            logger.error("runCleanupForTripsInTransit###Exception: ", e);

            emailService.sendSystemEmail("runCleanupForTripsInTransit " + e.getClass().getSimpleName(), EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "TripAnprExpiryTaskExecutor#runCleanupForTripsInTransit###" + e.getClass().getSimpleName() + ":<br />Cause: " + e.getCause() + "<br />Message: " + e.getMessage()
                    + "<br />Stacktrace: " + e.getStackTrace());

            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "runCleanupForTripsInTransit#2");
        }
    }

    private void runCleanupForTripsExitedParkingPrematurely() throws Exception {

        try {
            int tripExitedParkingPrematurelyValidityMinutes = systemService.getSystemParameter().getExitPrematurelyValidityMinutes();

            List<Trip> tripsList = tripService.getTripsExitedParkingPrematurelyForMoreThanXMinutes(tripExitedParkingPrematurelyValidityMinutes);

            final StringBuilder tripIds = new StringBuilder(
                "TripAnprExpiryTaskExecutor#run#Trips EXITED PARKING PREMATURELY#tripIdsExitedPrematurelyForMoreThan" + tripExitedParkingPrematurelyValidityMinutes + "Minutes=[");

            final StringBuilder parkingSessionIds = new StringBuilder("TripAnprExpiryTaskExecutor#run#Parking Sessions EXITED PREMATURELY#parkingIdsExitedPrematurelyForMoreThan"
                + tripExitedParkingPrematurelyValidityMinutes + "Minutes=[");

            if (tripsList != null && tripsList.size() > 0) {

                for (Trip exitedParkingPrematurelyTrip : tripsList) {

                    if (exitedParkingPrematurelyTrip.getParkingPermissionIdParkingEntry() != ServerConstants.DEFAULT_LONG) {
                        // remove the expired parking entry parking permission
                        anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_ENTRY, ServerConstants.DEFAULT_LONG,
                            exitedParkingPrematurelyTrip, null, null, new Date());
                    }

                    exitedParkingPrematurelyTrip.setStatus(ServerConstants.TRIP_STATUS_EXITED_PARKING_PREMATURELY_EXPIRED);

                    tripService.updateTrip(exitedParkingPrematurelyTrip);

                    tripIds.append(exitedParkingPrematurelyTrip.getId() + ", ");

                    Parking parking = parkingService.getParkingSessionByTripId(exitedParkingPrematurelyTrip.getId());

                    if (parking != null && parking.getStatus() == ServerConstants.PARKING_STATUS_EXIT) {
                        parking.setStatus(ServerConstants.PARKING_STATUS_EXITED_PREMATURELY_EXPIRED);
                        parking.setDateEdited(new Date());

                        parkingService.updateParking(parking);

                        parkingSessionIds.append(parking.getId() + ", ");
                    }

                }

                tripIds.append("]");
                parkingSessionIds.append("]");

                logger.info(tripIds.toString());
                logger.info(parkingSessionIds.toString());
            }

        } catch (HibernateException he) {
            logger.error("runCleanupForTripsExitedParkingPrematurely###HibernateException: ", he);

            emailService.sendSystemEmail("runCleanupForTripsExitedParkingPrematurely " + he.getClass().getSimpleName(), EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "TripAnprExpiryTaskExecutor#runCleanupForTripsExitedParkingPrematurely###" + he.getClass().getSimpleName() + ":<br />Cause: " + he.getCause() + "<br />Message: "
                    + he.getMessage() + "<br />Stacktrace: " + he.getStackTrace());

            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "runCleanupForTripsExitedParkingPrematurely#1");

        } catch (Exception e) {
            logger.error("runCleanupForTripsExitedParkingPrematurely###Exception: ", e);

            emailService.sendSystemEmail("runCleanupForTripsExitedParkingPrematurely " + e.getClass().getSimpleName(), EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "TripAnprExpiryTaskExecutor#runCleanupForTripsExitedParkingPrematurely###" + e.getClass().getSimpleName() + ":<br />Cause: " + e.getCause() + "<br />Message: "
                    + e.getMessage() + "<br />Stacktrace: " + e.getStackTrace());

            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "runCleanupForTripsExitedParkingPrematurely#2");
        }
    }

    private void runCleanupForExpiredUnpaidDirectToPortTripsCreatedByVirtualKiosk() throws Exception {

        List<Trip> expiredTripList = tripService.getExpiredUnpaidDirectToPortTripsCreatedByVirtualKiosk();

        if (expiredTripList != null && !expiredTripList.isEmpty()) {

            final StringBuilder tripIds = new StringBuilder("TripAnprExpiryTaskExecutor#runCleanupForExpiredUnpaidDirectToPortTripsCreatedByVirtualKiosk#tripIds=[");

            for (Trip trip : expiredTripList) {
                trip.setStatus(ServerConstants.TRIP_STATUS_CANCELLED_SYSTEM);

                tripService.updateTrip(trip);

                tripIds.append(trip.getId() + ", ");
            }

            tripIds.append("]");
            logger.info(tripIds.toString());
        }
    }

    private void runCleanupForOldTrips() throws Exception {
        List<PortOperatorTransactionType> portOperatorTransactionTypeList = systemService.getPortOperatorTransactionTypes();

        final StringBuilder tripIds = new StringBuilder(
            "TripAnprExpiryTaskExecutor#run#runCleanupForOldTrips#tripIds=[");

        for (PortOperatorTransactionType portOperatorTransactionType : portOperatorTransactionTypeList) {
            if (portOperatorTransactionType.getIsTripCancelSystem()) {
                long portOperatorId = portOperatorTransactionType.getPortOperator().getId();
                int transactionType = portOperatorTransactionType.getTransactionType();
                int tripCancelSystemAfterMinutes = portOperatorTransactionType.getTripCancelSystemAfterMinutes();
                List<Trip> oldApprovedTrips = tripService.getOldApprovedTrips(portOperatorId, transactionType, tripCancelSystemAfterMinutes);
                for (Trip trip : oldApprovedTrips) {
                    updateBookingSlotCounts(trip);

                    trip.setStatus(ServerConstants.TRIP_STATUS_CANCELLED_SYSTEM);
                    tripService.updateTrip(trip);
                    tripIds.append(trip.getId()).append(", ");
                }
            }
        }

        tripIds.append("]");
        logger.info(tripIds.toString());
    }
    
    private void runAmountInHold() throws Exception {
        
        final StringBuilder accountNumbersLog = new StringBuilder(
            "TripAnprExpiryTaskExecutor#run#runAmountInHold#accountNumber=amountHold[");

        for (Account account : accountService.getActiveAccounts()) {
        	
            BigDecimal sumFeeAmount = tripService.getSumFeeAmountForApprovedAndInFlightTripsAndDateSlotRange(account.getId(), ServerConstants.AMOUNT_HOLD_DATE_SLOT_PAST_HOURS, ServerConstants.AMOUNT_HOLD_DATE_SLOT_FUTURE_HOURS);
                      
            if(account.getAmountHold().compareTo(sumFeeAmount) != 0) {
	            account.setAmountHold(sumFeeAmount == null ? BigDecimal.ZERO : sumFeeAmount);
	            	
	            accountService.updateAccount(account);
	            	
	            accountNumbersLog.append(account.getNumber()).append("=").append(account.getAmountHold()).append(", ");
            }
        }

        accountNumbersLog.append("]");
        logger.info(accountNumbersLog.toString());
    }

    private void updateBookingSlotCounts(Trip trip) {
        Mission mission = trip.getMission();
        mission.setTripsBookedCount(mission.getTripsBookedCount() > 0 ? mission.getTripsBookedCount() - 1 : 0);
        mission.setStatus(mission.getTripsBookedCount() > 0 ? ServerConstants.MISSION_STATUS_TRIPS_BOOKED : ServerConstants.MISSION_STATUS_TRIPS_PENDING);
        mission.setDateEdited(new Date());

        missionService.updateMission(mission);

        if (systemService.getSystemParameter().getIsBookingLimitCheckEnabled()) {

            Calendar calendarDateSlot = Calendar.getInstance();
            calendarDateSlot.setTime(trip.getDateSlotApproved() != null ? trip.getDateSlotApproved() : trip.getDateSlotRequested());

            int hourOfDay = calendarDateSlot.get(Calendar.HOUR_OF_DAY);

            BookingSlotCount bookingSlotCountEntity = systemService.getBookingSlotCountByPortOperatorAndTransactionTypeAndHourSlot(mission.getPortOperatorId(),
                mission.getTransactionType(), calendarDateSlot.getTime(), hourOfDay);

            if (bookingSlotCountEntity != null && bookingSlotCountEntity.getTripsBookedCount() > 0) {

                bookingSlotCountEntity.setTripsBookedCount(bookingSlotCountEntity.getTripsBookedCount() - 1);

                systemService.updateBookingSlot(bookingSlotCountEntity);
            }
        }
    }

}
