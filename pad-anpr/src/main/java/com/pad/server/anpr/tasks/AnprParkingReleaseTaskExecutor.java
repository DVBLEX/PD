package com.pad.server.anpr.tasks;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.anpr.common.ServerConstants;
import com.pad.server.base.entities.BookingSlotLimit;
import com.pad.server.base.entities.BookingSlotLimitsDefault;
import com.pad.server.base.entities.Parking;
import com.pad.server.base.entities.PortOperator;
import com.pad.server.base.entities.PortOperatorTransactionType;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.services.anpr.AnprBaseService;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.parking.ParkingService;
import com.pad.server.base.services.system.SystemService;
import com.pad.server.base.services.trip.TripService;

@Component
@Transactional
public class AnprParkingReleaseTaskExecutor implements Runnable {

    private static final Logger logger = Logger.getLogger(AnprParkingReleaseTaskExecutor.class);

    @Autowired
    private AnprBaseService     anprBaseService;

    @Autowired
    private EmailService        emailService;

    @Autowired
    private ParkingService      parkingService;

    @Autowired
    private SystemService       systemService;

    @Autowired
    private TripService         tripService;

    @Override
    @Scheduled(fixedDelay = 30000, initialDelay = 30000)
    public void run() {

        try {
            // logger.info("run#");

            systemService.updateSystemTimerTaskDateLastRun(ServerConstants.SYSTEM_TIMER_TASK_ANPR_PARKING_RELEASE_ID, new Date());

            final Date dateToday = new Date();

            Calendar calendarDateSlot = Calendar.getInstance();
            Calendar calendarDateTodayMinusFifteenMin = Calendar.getInstance();

            int hourOfSlot = ServerConstants.DEFAULT_INT;
            int dayOfSlotWeekId = ServerConstants.DEFAULT_INT;
            int bookingLimit = 0; // per hour slot
            int vehicleReleaseLimit = 0; // per 15 minutes
            int vehiclesAlreadyReleasedCount = 0;
            int vehiclesAlreadyReleasedLastFifteenMinCount = 0;

            PortOperatorTransactionType portOperatorTransactionTypeEntity = null;
            BookingSlotLimit bookingSlotLimitEntity = null;
            BookingSlotLimitsDefault bookingSlotLimitDefaultEntity = null;

            final List<Parking> parkingList = parkingService.getActiveParkingList(ServerConstants.DEFAULT_INT, ServerConstants.DEFAULT_INT, false);

            for (Parking parking : parkingList) {

                PortOperator portOperator = systemService.getPortOperatorFromMap(parking.getPortOperatorId());

                for (PortOperatorTransactionType portOperatorTransactionType : portOperator.getPortOperatorTransactionTypesList()) {

                    if (portOperatorTransactionType.getTransactionType() == parking.getTransactionType()) {
                        portOperatorTransactionTypeEntity = portOperatorTransactionType;
                        break;
                    }
                }

                // process if date slot is after time of processing or x minutes prior to slot date & time
                if ((dateToday.compareTo(parking.getDateTripSlotApproved()) >= 0 || parking.getDateTripSlotApproved().getTime() - dateToday.getTime() <= TimeUnit.MINUTES
                    .toMillis(portOperatorTransactionTypeEntity.getPortTransitDurationMinutes()))
                    && parking.getStatus() == com.pad.server.base.common.ServerConstants.PARKING_STATUS_ENTRY
                    && parking.getVehicleState() == com.pad.server.base.common.ServerConstants.VEHICLE_PARKING_STATE_NORMAL
                    && portOperatorTransactionTypeEntity.getIsAutoReleaseParking()) {

                    bookingLimit = 0;
                    vehicleReleaseLimit = 0;
                    vehiclesAlreadyReleasedCount = 0;
                    vehiclesAlreadyReleasedLastFifteenMinCount = 0;

                    calendarDateSlot.setTime(parking.getDateTripSlotApproved());
                    calendarDateTodayMinusFifteenMin.setTime(dateToday);
                    calendarDateTodayMinusFifteenMin.add(Calendar.SECOND, -900);

                    hourOfSlot = calendarDateSlot.get(Calendar.HOUR_OF_DAY);
                    dayOfSlotWeekId = calendarDateSlot.get(Calendar.DAY_OF_WEEK);

                    bookingSlotLimitEntity = systemService.getBookingSlotLimitByPortOperatorAndTransactionTypeAndDateSlotAndHourSlot(parking.getPortOperatorId(),
                        parking.getTransactionType(), parking.getDateTripSlotApproved(), hourOfSlot);

                    if (bookingSlotLimitEntity != null) {
                        bookingLimit = bookingSlotLimitEntity.getBookingLimit();

                    } else {
                        bookingSlotLimitDefaultEntity = systemService.getDefaultBookingSlotLimitByPortOperatorAndTransactionTypeAndDateSlotAndHourSlot(parking.getPortOperatorId(),
                            parking.getTransactionType(), dayOfSlotWeekId, hourOfSlot);

                        if (bookingSlotLimitDefaultEntity != null) {
                            bookingLimit = bookingSlotLimitDefaultEntity.getBookingLimit();
                        }
                    }

                    if (bookingLimit > 0 && bookingLimit < 4) {
                        vehicleReleaseLimit = 1;
                    } else {
                        vehicleReleaseLimit = (int) Math.ceil(bookingLimit / 4f);
                    }

                    if (vehicleReleaseLimit > 0) {

                        for (Parking p : parkingList) {

                            if (p.getStatus() == com.pad.server.base.common.ServerConstants.PARKING_STATUS_REMINDER_EXIT_DUE
                                && p.getVehicleState() == com.pad.server.base.common.ServerConstants.VEHICLE_PARKING_STATE_NORMAL
                                && p.getPortOperatorId() == parking.getPortOperatorId() && p.getTransactionType() == parking.getTransactionType()) {

                                vehiclesAlreadyReleasedCount++;
                            }
                        }

                        vehiclesAlreadyReleasedLastFifteenMinCount = parkingService.getVehicleReleasedCountInLastXMinutes(parking.getPortOperatorId(), parking.getTransactionType(),
                            calendarDateTodayMinusFifteenMin.getTime());

                        StringBuilder builder = new StringBuilder();
                        builder.append("[parkingId=" + parking.getId());
                        builder.append(", vehicleReg=" + parking.getVehicleRegistration());
                        builder.append(", portOperatorId=" + parking.getPortOperatorId());
                        builder.append(", transactionType=" + parking.getTransactionType());
                        builder.append(", bookingLimit=" + bookingLimit);
                        builder.append(", vehicleReleaseLimit=" + vehicleReleaseLimit);
                        builder.append(", vehiclesAlreadyReleasedCount=" + vehiclesAlreadyReleasedCount);
                        builder.append(", vehiclesAlreadyReleasedLastFifteenMinCount=" + vehiclesAlreadyReleasedLastFifteenMinCount);
                        builder.append(", autoReleaseExitCapacityPercentage=" + anprBaseService.getSystemParameter().getAutoReleaseExitCapacityPercentage());
                        builder.append("]");

                        logger.info(builder.toString());

                        // if more than x% (roundup) of the trucks were released but didn’t leave, no schedule happens.
                        // E.g. releaseLimitPerHour = 10
                        // vehicleReleaseLimitPer15Min (roundup) = releaseLimitPerHour / 4 = 3
                        // x = default 150% (roundup) of vehicleReleaseLimitPer15Min = 5 so if 5 trucks or more were released but didn't exit, don't schedule another release
                        // also, if count of vehicles already released in the last fifteen minutes equals or exceeds the vehicle release limit per 15 minutes, don't schedule
                        // another release
                        // in the case of the example, max 3 vehicles will be released every 15 minutes as long as condition 1(how many released but didn't exit) is satisfied
                        if (vehiclesAlreadyReleasedCount < (int) Math
                            .ceil((vehicleReleaseLimit * anprBaseService.getSystemParameter().getAutoReleaseExitCapacityPercentage()) / 100f)
                            && vehiclesAlreadyReleasedLastFifteenMinCount < vehicleReleaseLimit
                            && parking.getStatus() == com.pad.server.base.common.ServerConstants.PARKING_STATUS_ENTRY) {

                            Trip trip = tripService.getTripById(parking.getTripId());

                            if (trip.getStatus() == com.pad.server.base.common.ServerConstants.TRIP_STATUS_ENTERED_PARKING) {
                                parkingService.sendExitParkingSms(trip, parking, 0);

                                logger.info("Vehicle " + parking.getVehicleRegistration() + " was released: [parkingId=" + parking.getId() + ", portOperatorId="
                                    + parking.getPortOperatorId() + ", transactionType=" + parking.getTransactionType() + "]");
                            }
                        }
                    }
                }
            }

        } catch (DataAccessException dae) {
            logger.error("run###DataAccessException: ", dae);

            emailService.sendSystemEmail("AnprParkingReleaseTaskExecutor DataAccessException", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "AnprParkingReleaseTaskExecutor#run###DataAccessException:<br />" + dae.getMessage());

        } catch (Exception e) {
            logger.error("run###Exception: ", e);

            emailService.sendSystemEmail("AnprParkingReleaseTaskExecutor Exception", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "AnprParkingReleaseTaskExecutor#run###Exception:<br />" + e.getCause() + " <br />" + e.getMessage());
        }
    }
}
