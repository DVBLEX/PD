package com.pad.server.base.services.trip;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.TimestampType;
import org.springframework.beans.BeanUtils;
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
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.AnprLog;
import com.pad.server.base.entities.AnprParameter;
import com.pad.server.base.entities.BookingSlotCount;
import com.pad.server.base.entities.Driver;
import com.pad.server.base.entities.Email;
import com.pad.server.base.entities.IndependentPortOperator;
import com.pad.server.base.entities.Mission;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.entities.Parking;
import com.pad.server.base.entities.PortOperatorTransactionType;
import com.pad.server.base.entities.Sms;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.entities.Vehicle;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.AccountJson;
import com.pad.server.base.jsonentities.api.TripJson;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.anpr.AnprBaseService;
import com.pad.server.base.services.driver.DriverService;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.mission.MissionService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.parking.ParkingService;
import com.pad.server.base.services.session.SessionService;
import com.pad.server.base.services.sms.SmsService;
import com.pad.server.base.services.system.SystemService;
import com.pad.server.base.services.vehicle.VehicleService;

@Service
@Transactional
public class TripServiceImpl implements TripService {

    private static final Logger logger = Logger.getLogger(TripServiceImpl.class);

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private HibernateTemplate   hibernateTemplate;

    @Autowired
    private SessionFactory      sessionFactory;

    @Autowired
    private AccountService      accountService;

    @Autowired
    private ActivityLogService  activityLogService;

    @Autowired
    private AnprBaseService     anprBaseService;

    @Autowired
    private DriverService       driverService;

    @Autowired
    private EmailService        emailService;

    @Autowired
    private MissionService      missionService;

    @Autowired
    private OperatorService     operatorService;

    @Autowired
    private ParkingService      parkingService;

    @Autowired
    private SessionService      sessionService;

    @Autowired
    private SmsService          smsService;

    @Autowired
    private SystemService       systemService;

    @Autowired
    private VehicleService      vehicleService;

    @SuppressWarnings("unchecked")
    @Override
    public List<Trip> getApprovedTripsInNextXHours(int parkingPermissionHoursInFuture, int parkingPermissionHoursPriorSlotDate) {

        Date dateToday = new Date();

        Calendar calendarDateFrom = Calendar.getInstance();
        calendarDateFrom.setTime(dateToday);
        calendarDateFrom.add(Calendar.HOUR_OF_DAY, parkingPermissionHoursPriorSlotDate * -1);

        Calendar calendarDateTo = Calendar.getInstance();
        calendarDateTo.setTime(dateToday);
        calendarDateTo.add(Calendar.HOUR_OF_DAY, parkingPermissionHoursInFuture);

        List<Trip> tripList = new ArrayList<>();

        Session currentSession = sessionFactory.getCurrentSession();

        tripList = currentSession.createQuery(
            "FROM Trip t where t.type=:type and t.status=:status and t.parkingPermissionId=:parking_permission_id and t.parkingPermissionIdParkingEntry=:parking_permission_id_parking_entry and t.parkingPermissionIdPortEntry=:parking_permission_id_port_entry "
                + "and t.accountId!=:account_id and t.driverId!=:driver_id and t.dateSlotApproved >:date_from and t.dateSlotApproved<=:date_to and t.laneSessionId=:lane_session_id "
                + "ORDER BY t.dateSlotApproved ASC")
            .setParameter("type", ServerConstants.TRIP_TYPE_BOOKED).setParameter("status", ServerConstants.TRIP_STATUS_APPROVED)
            .setParameter("parking_permission_id", ServerConstants.DEFAULT_LONG).setParameter("parking_permission_id_parking_entry", ServerConstants.DEFAULT_LONG)
            .setParameter("parking_permission_id_port_entry", ServerConstants.DEFAULT_LONG).setParameter("account_id", ServerConstants.DEFAULT_LONG)
            .setParameter("driver_id", ServerConstants.DEFAULT_LONG).setParameter("date_from", calendarDateFrom.getTime(), TimestampType.INSTANCE)
            .setParameter("date_to", calendarDateTo.getTime(), TimestampType.INSTANCE).setParameter("lane_session_id", ServerConstants.DEFAULT_LONG).list();

        return tripList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Trip> getApprovedTripsWithSlotDateInFuture(long portOperatorId, int transactionType, int hoursInFuture) {

        List<Trip> tripList = new ArrayList<>();

        Session currentSession = sessionFactory.getCurrentSession();

        tripList = currentSession.createQuery(
            "SELECT t FROM Trip as t left join t.mission m where m.portOperatorId=:port_operator_id and m.transactionType=:transaction_type and t.status=:status and t.parkingPermissionId=:parking_permission_id "
                + "and t.dateSlotApproved >:date_slot_from")
            .setParameter("port_operator_id", (int) portOperatorId).setParameter("transaction_type", transactionType).setParameter("status", ServerConstants.TRIP_STATUS_APPROVED)
            .setParameter("parking_permission_id", ServerConstants.DEFAULT_LONG)
            .setParameter("date_slot_from", new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(hoursInFuture)), TimestampType.INSTANCE).list();

        return tripList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getVehicleRegistrationListFromApprovedDirectToPortTrips() {

        List<String> vehicleRegistrationList = new ArrayList<>();

        String[] paramNames = { "datetimeNow" };
        Object[] paramValues = { new Date() };

        List<Mission> missionList = (List<Mission>) hibernateTemplate
            .findByNamedParam("FROM Mission WHERE isDirectToPort = 1 AND dateMissionStart <= :datetimeNow AND dateMissionEnd >= :datetimeNow", paramNames, paramValues);

        if (missionList != null && !missionList.isEmpty()) {

            for (Mission m : missionList) {

                List<Trip> tripList = m.getTripList();

                for (Trip t : tripList) {
                    if (t.getIsDirectToPort() && t.getLaneSessionId() == ServerConstants.DEFAULT_LONG && t.getStatus() == ServerConstants.TRIP_STATUS_APPROVED) {
                        vehicleRegistrationList.add(t.getVehicleRegistration());

                    } else if (t.getIsDirectToPort() && t.getStatus() == ServerConstants.TRIP_STATUS_IN_TRANSIT) {
                        vehicleRegistrationList.add(t.getVehicleRegistration());

                    } else if (t.getIsDirectToPort() && t.getIsAllowMultipleEntries()
                        && (t.getStatus() == ServerConstants.TRIP_STATUS_COMPLETED || t.getStatus() == ServerConstants.TRIP_STATUS_COMPLETED_SYSTEM)) {
                        vehicleRegistrationList.add(t.getVehicleRegistration());
                    }
                }
            }
        }

        return vehicleRegistrationList.stream().distinct().collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Trip getTheLatestAuthorisedDirectToPortTripByVehicleRegNumber(String vehicleRegistration) {

        List<Trip> directToPortTripList = new ArrayList<>();

        Session currentSession = sessionFactory.getCurrentSession();

        // fetch the latest trips with that reg that is direct to port and the corresponding mission is still active
        directToPortTripList = currentSession.createQuery(
            "SELECT t FROM Trip as t left join t.mission m where m.status IN(:statusTripsPending, :statusTripsBooked) and m.isDirectToPort = 1 and m.dateMissionStart<=:date_time_now and m.dateMissionEnd>=:date_time_now "
                + "and t.vehicleRegistration=:vehicle_registration ORDER BY t.id DESC")
            .setParameter("statusTripsPending", ServerConstants.MISSION_STATUS_TRIPS_PENDING).setParameter("statusTripsBooked", ServerConstants.MISSION_STATUS_TRIPS_BOOKED)
            .setParameter("date_time_now", new Date(), TimestampType.INSTANCE).setParameter("vehicle_registration", vehicleRegistration).list();

        if (directToPortTripList != null && !directToPortTripList.isEmpty()) {

        	for(Trip trip: directToPortTripList) {
	
	            if (trip.getIsAllowMultipleEntries() && (trip.getStatus() == ServerConstants.TRIP_STATUS_APPROVED || trip.getStatus() == ServerConstants.TRIP_STATUS_IN_TRANSIT
	                || trip.getStatus() == ServerConstants.TRIP_STATUS_ENTERED_PORT || trip.getStatus() == ServerConstants.TRIP_STATUS_COMPLETED
	                || trip.getStatus() == ServerConstants.TRIP_STATUS_COMPLETED_SYSTEM || trip.getStatus() == ServerConstants.TRIP_STATUS_IN_FLIGHT))
	                return trip;
	            else if (!trip.getIsAllowMultipleEntries() && (trip.getStatus() == ServerConstants.TRIP_STATUS_APPROVED || trip.getStatus() == ServerConstants.TRIP_STATUS_IN_TRANSIT
	                || trip.getStatus() == ServerConstants.TRIP_STATUS_IN_FLIGHT))
	                return trip;
        	}
        	
        	return null;

        } else
            return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Trip getTheLatestCompletedTripByVehicleRegNumber(String vehicleRegistration) {

        List<Trip> directToPortTripList = new ArrayList<>();

        Session currentSession = sessionFactory.getCurrentSession();

        directToPortTripList = currentSession.createQuery(
            "SELECT t FROM Trip as t left join t.mission m where m.status IN(:statusTripsPending, :statusTripsBooked) and m.isDirectToPort = 1 and m.dateMissionStart<=:date_time_now and m.dateMissionEnd>=:date_time_now "
                + "and t.vehicleRegistration=:vehicle_registration and t.status IN (:tripStatusCompleted, :tripStatusCompletedSystem) ORDER BY t.id DESC")
            .setParameter("statusTripsPending", ServerConstants.MISSION_STATUS_TRIPS_PENDING).setParameter("statusTripsBooked", ServerConstants.MISSION_STATUS_TRIPS_BOOKED)
            .setParameter("date_time_now", new Date(), TimestampType.INSTANCE).setParameter("vehicle_registration", vehicleRegistration)
            .setParameter("tripStatusCompleted", ServerConstants.TRIP_STATUS_COMPLETED).setParameter("tripStatusCompletedSystem", ServerConstants.TRIP_STATUS_COMPLETED_SYSTEM)
            .setMaxResults(1).list();

        if (directToPortTripList != null && !directToPortTripList.isEmpty()) {

            Trip trip = directToPortTripList.get(0);

            return trip;

        } else
            return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Trip globalCheckForActiveBookingForVehicle(String vehicleRegistration, Date dateSlotForNewTrip) throws PADException {

        List<Trip> tripList = new ArrayList<>();
        Trip tripOnActiveBooking = null;

        Calendar calendarDateSlotApprovedMinusXHours = Calendar.getInstance();
        Calendar calendarDateSlotApprovedPlusXHours = Calendar.getInstance();

        Session currentSession = sessionFactory.getCurrentSession();

        tripList = currentSession
            .createQuery("FROM Trip t where t.vehicleRegistration=:vehicle_registration and t.status IN (:status1, :status2, :status3, :status4, :status5, :status6, :status7)")
            .setParameter("vehicle_registration", vehicleRegistration).setParameter("status1", ServerConstants.TRIP_STATUS_APPROVED)
            .setParameter("status2", ServerConstants.TRIP_STATUS_ENTERED_PARKING).setParameter("status3", ServerConstants.TRIP_STATUS_EXITED_PARKING_PREMATURELY)
            .setParameter("status4", ServerConstants.TRIP_STATUS_IN_TRANSIT).setParameter("status5", ServerConstants.TRIP_STATUS_ENTERED_PORT)
            .setParameter("status6", ServerConstants.TRIP_STATUS_PENDING_APPROVAL).setParameter("status7", ServerConstants.TRIP_STATUS_PENDING).list();

        for (Trip trip : tripList) {

            Mission mission = trip.getMission();

            if (dateSlotForNewTrip.compareTo(mission.getDateMissionStart()) >= 0 && dateSlotForNewTrip.compareTo(mission.getDateMissionEnd()) <= 0) {

                if (trip.getIsDirectToPort() && trip.getIsAllowMultipleEntries()) {
                    // there is a trip on a mission allowed multiple port entries already booked for that vehicle
                    tripOnActiveBooking = trip;
                    break;

                } else {
                    calendarDateSlotApprovedMinusXHours.setTime(trip.getDateSlotApproved() == null ? trip.getDateSlotRequested() : trip.getDateSlotApproved());
                    calendarDateSlotApprovedMinusXHours.add(Calendar.HOUR_OF_DAY, systemService.getSystemParameter().getBookingCheckDateSlotApprovedInAdvanceHours() * -1);

                    calendarDateSlotApprovedPlusXHours.setTime(trip.getDateSlotApproved() == null ? trip.getDateSlotRequested() : trip.getDateSlotApproved());
                    calendarDateSlotApprovedPlusXHours.add(Calendar.HOUR_OF_DAY, systemService.getSystemParameter().getBookingCheckDateSlotApprovedAfterwardsHours());

                    if (dateSlotForNewTrip.compareTo(calendarDateSlotApprovedMinusXHours.getTime()) > 0
                        && dateSlotForNewTrip.compareTo(calendarDateSlotApprovedPlusXHours.getTime()) < 0) {
                        tripOnActiveBooking = trip;
                        break;
                    }
                }
            }
        }

        return tripOnActiveBooking;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Trip> getTripsByVehicleRegNumberAndStatus(String vehicleRegistration, int status, int kioskSessionType) {

        List<Trip> tripList = new ArrayList<>();

        Session currentSession = sessionFactory.getCurrentSession();

        if (kioskSessionType == ServerConstants.SESSION_TYPE_VIRTUAL) {
            tripList = currentSession.createQuery("FROM Trip t where t.vehicleRegistration=:vehicle_registration and t.status=:status and t.isDirectToPort=1")
                .setParameter("vehicle_registration", vehicleRegistration).setParameter("status", status).list();

        } else {
            tripList = currentSession.createQuery("FROM Trip t where t.vehicleRegistration=:vehicle_registration and t.status=:status")
                .setParameter("vehicle_registration", vehicleRegistration).setParameter("status", status).list();
        }

        return tripList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Trip getApprovedTripByVehicleRegNumberAndParkingPermissionId(String vehicleRegistration, long parkingPermissionId) {

        Trip trip = null;
        List<Trip> tripList = new ArrayList<>();

        Session currentSession = sessionFactory.getCurrentSession();

        tripList = currentSession.createQuery(
            "FROM Trip t where t.vehicleRegistration=:vehicle_registration and t.status IN (:statusApproved, :statusExitedParkingPrematurely) and t.parkingPermissionIdParkingEntry=:parking_permission_id_parking_entry")
            .setParameter("vehicle_registration", vehicleRegistration).setParameter("statusApproved", ServerConstants.TRIP_STATUS_APPROVED)
            .setParameter("statusExitedParkingPrematurely", ServerConstants.TRIP_STATUS_EXITED_PARKING_PREMATURELY)
            .setParameter("parking_permission_id_parking_entry", parkingPermissionId).list();

        if (tripList != null && !tripList.isEmpty()) {
            trip = tripList.get(0);
        }

        return trip;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Trip getTripByVehicleRegNumberAndParkingPermissionId(String vehicleRegistration, long parkingPermissionId, int parkingPermissionType) {

        Trip trip = null;
        List<Trip> tripList = new ArrayList<>();

        Session currentSession = sessionFactory.getCurrentSession();

        switch (parkingPermissionType) {

            case ServerConstants.PARKING_PERMISSION_TYPE_PARKING_ENTRY:
                tripList = currentSession.createQuery(
                    "FROM Trip t where t.vehicleRegistration=:vehicle_registration and t.parkingPermissionIdParkingEntry=:parking_permission_id_parking_entry ORDER BY t.id DESC")
                    .setParameter("vehicle_registration", vehicleRegistration).setParameter("parking_permission_id_parking_entry", parkingPermissionId).list();
                break;

            case ServerConstants.PARKING_PERMISSION_TYPE_PARKING_EXIT:
                tripList = currentSession
                    .createQuery(
                        "FROM Trip t where t.vehicleRegistration=:vehicle_registration and t.parkingPermissionIdParkingExit=:parking_permission_id_parking_exit ORDER BY t.id DESC")
                    .setParameter("vehicle_registration", vehicleRegistration).setParameter("parking_permission_id_parking_exit", parkingPermissionId).list();
                break;

            case ServerConstants.PARKING_PERMISSION_TYPE_PORT_ENTRY:
                tripList = currentSession
                    .createQuery(
                        "FROM Trip t where t.vehicleRegistration=:vehicle_registration and t.parkingPermissionIdPortEntry=:parking_permission_id_port_entry ORDER BY t.id DESC")
                    .setParameter("vehicle_registration", vehicleRegistration).setParameter("parking_permission_id_port_entry", parkingPermissionId).list();
                break;

            case ServerConstants.PARKING_PERMISSION_TYPE_PORT_EXIT:
                tripList = currentSession
                    .createQuery(
                        "FROM Trip t where t.vehicleRegistration=:vehicle_registration and t.parkingPermissionIdPortExit=:parking_permission_id_port_exit ORDER BY t.id DESC")
                    .setParameter("vehicle_registration", vehicleRegistration).setParameter("parking_permission_id_port_exit", parkingPermissionId).list();
                break;

            default:
                break;
        }

        if (tripList != null && !tripList.isEmpty()) {
            trip = tripList.get(0);
        }

        return trip;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getVehicleRegistrationListFromTrips(int kioskSessionType) {

        List<Trip> tripList = new ArrayList<>();
        List<String> vehicleRegistrationList = new ArrayList<>();

        Session currentSession = sessionFactory.getCurrentSession();

        // Date dateToday = new Date();
        // Calendar calendarDateDateTodayMinusXDays = Calendar.getInstance();
        // calendarDateDateTodayMinusXDays.setTime(dateToday);
        // calendarDateDateTodayMinusXDays.add(Calendar.DAY_OF_YEAR, systemService.getSystemParameter().getTripSlotStartRangeDays() * -1);
        //
        // tripList = currentSession.createQuery("FROM Trip t where t.status=:status and t.dateSlotApproved>:date_slot_from").setParameter("status",
        // ServerConstants.TRIP_STATUS_APPROVED).setParameter("date_slot_from", calendarDateDateTodayMinusXDays.getTime(), TimestampType.INSTANCE).list();

        if (kioskSessionType == ServerConstants.SESSION_TYPE_PARKING) {
            tripList = currentSession.createQuery("FROM Trip t where t.status=:status").setParameter("status", ServerConstants.TRIP_STATUS_APPROVED).list();

        } else {
            tripList = currentSession.createQuery("FROM Trip t where t.status=:status and t.isDirectToPort=1").setParameter("status", ServerConstants.TRIP_STATUS_APPROVED).list();
        }

        if (tripList != null && !tripList.isEmpty()) {
            vehicleRegistrationList = tripList.stream().map(result -> result.getVehicleRegistration()).distinct().collect(Collectors.toList());
        }

        return vehicleRegistrationList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Trip getTripByCode(String code) {

        Trip trip = null;

        List<Trip> tripList = (List<Trip>) hibernateTemplate.findByNamedParam("FROM Trip WHERE code = :code", "code", code);

        if (tripList != null && !tripList.isEmpty()) {
            trip = tripList.get(0);
        }

        return trip;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Trip getTripByReferenceNumber(String referenceNumber) {

        Trip trip = null;

        List<Trip> tripList = (List<Trip>) hibernateTemplate.findByNamedParam("FROM Trip WHERE referenceNumber = :referenceNumber", "referenceNumber", referenceNumber);

        if (tripList != null && !tripList.isEmpty()) {
            trip = tripList.get(0);
        }

        return trip;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Trip getTripById(long id) {

        Trip trip = null;

        List<Trip> tripList = (List<Trip>) hibernateTemplate.findByNamedParam("FROM Trip WHERE id = :id", "id", id);

        if (tripList != null && !tripList.isEmpty()) {
            trip = tripList.get(0);
        }

        return trip;
    }

    @Override
    public void updateTrip(Trip trip) {

        trip.setDateEdited(new Date());

        hibernateTemplate.update(trip);
    }

    @Override
    @Transactional(rollbackFor = { PADException.class, PADValidationException.class, Exception.class })
    public void updateTrip(TripJson tripJson, long loggedOperatorId) throws PADException, PADValidationException, Exception {

        Trip trip = null;
        Account account = null;
        Driver driver = null;
        Operator transporter = null;
        Date today = new Date();

        PortOperatorTransactionType portOperatorTransactionType = null;

        switch (tripJson.getActionType()) {

            case ServerConstants.ACTION_TYPE_UPDATE_TRIP_API:

                trip = getTripByCode(tripJson.getCode());

                if (trip == null)
                    throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "trip is null");

                if (trip.getStatus() != ServerConstants.TRIP_STATUS_PENDING && trip.getStatus() != ServerConstants.TRIP_STATUS_PENDING_APPROVAL
                    && trip.getStatus() != ServerConstants.TRIP_STATUS_APPROVED)
                    throw new PADException(ServerResponseConstants.UNEXPECTED_TRIP_STATUS_CODE, ServerResponseConstants.UNEXPECTED_TRIP_STATUS_TEXT, "");

                Date dateMissionStart;
                Date dateMissionEnd;

                if (StringUtils.isNotBlank(tripJson.getDateMissionStartString())) {
                    try {
                        dateMissionStart = ServerUtil.parseDate(ServerConstants.dateFormatyyyyMMddHHmm, tripJson.getDateMissionStartString());
                    } catch (ParseException e) {
                        throw new PADException(ServerResponseConstants.INVALID_DATE_SLOT_FROM_CODE, ServerResponseConstants.INVALID_DATE_SLOT_FROM_TEXT, "invalid date slot from");
                    }
                    try {
                        dateMissionEnd = ServerUtil.parseDate(ServerConstants.dateFormatyyyyMMddHHmm, tripJson.getDateMissionEndString());
                    } catch (ParseException e) {
                        throw new PADException(ServerResponseConstants.INVALID_DATE_SLOT_TO_CODE, ServerResponseConstants.INVALID_DATE_SLOT_TO_TEXT, "invalid date slot to");
                    }

                    Calendar calendarDayToday = Calendar.getInstance();
                    calendarDayToday.set(Calendar.HOUR_OF_DAY, 0);
                    calendarDayToday.set(Calendar.MINUTE, 0);
                    calendarDayToday.set(Calendar.SECOND, 0);
                    calendarDayToday.set(Calendar.MILLISECOND, 0);

                    if (dateMissionStart.before(calendarDayToday.getTime()))
                        throw new PADValidationException(ServerResponseConstants.MISSION_DATE_IN_PAST_CODE, ServerResponseConstants.MISSION_DATE_IN_PAST_TEXT,
                            "Mission date is past");

                    if (dateMissionEnd.before(dateMissionStart))
                        throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#3");

                    Trip existingActiveTripForVehicle = globalCheckForActiveBookingForVehicle(trip.getVehicleRegistration(), dateMissionStart);

                    if (existingActiveTripForVehicle != null)
                        throw new PADValidationException(ServerResponseConstants.TRIP_FOR_VEHICLE_ALREADY_BOOKED_CODE, ServerResponseConstants.TRIP_FOR_VEHICLE_ALREADY_BOOKED_TEXT,
                            existingActiveTripForVehicle.getCode());

                    if (systemService.getSystemParameter().getIsBookingLimitCheckEnabled()) {
                        // update booking slot counts
                        Calendar calendarDateSlotOld = Calendar.getInstance();
                        calendarDateSlotOld.setTime(trip.getDateSlotApproved() != null ? trip.getDateSlotApproved() : trip.getDateSlotRequested());

                        int hourOfDay = calendarDateSlotOld.get(Calendar.HOUR_OF_DAY);

                        BookingSlotCount bookingSlotCountEntity = systemService.getBookingSlotCountByPortOperatorAndTransactionTypeAndHourSlot(
                            trip.getMission().getPortOperatorId(), trip.getMission().getTransactionType(), calendarDateSlotOld.getTime(), hourOfDay);

                        if (bookingSlotCountEntity != null && bookingSlotCountEntity.getTripsBookedCount() > 0) {

                            bookingSlotCountEntity.setTripsBookedCount(bookingSlotCountEntity.getTripsBookedCount() - 1);

                            systemService.updateBookingSlot(bookingSlotCountEntity);
                        }

                        systemService.reserveBookingSlot(trip.getMission().getPortOperatorId(), trip.getMission().getTransactionType(), dateMissionStart);
                    }

                    trip.getMission().setDateMissionStart(dateMissionStart);
                    trip.getMission().setDateMissionEnd(dateMissionEnd);
                    trip.setDateSlotRequested(dateMissionStart);
                    trip.setDateSlotApproved(null);
                }

                if (StringUtils.isNotBlank(tripJson.getVehicleCode())) {
                    Vehicle vehicle = vehicleService.getVehicleByCode(tripJson.getVehicleCode());

                    trip.setVehicleId(vehicle.getId());
                    trip.setVehicleRegistration(vehicle.getVehicleRegistration());
                }

                trip.setContainerId(StringUtils.isNotBlank(tripJson.getContainerId()) ? tripJson.getContainerId() : trip.getContainerId());
                trip.setContainerType(StringUtils.isNotBlank(tripJson.getContainerType()) ? tripJson.getContainerType() : trip.getContainerType());
                trip.setStatus(ServerConstants.TRIP_STATUS_PENDING);
                trip.setDriverId(ServerConstants.DEFAULT_LONG);
                trip.setDriverMsisdn(ServerConstants.DEFAULT_STRING);
                trip.setDriverLanguageId(ServerConstants.DEFAULT_LONG);
                trip.setOperatorId(loggedOperatorId);
                trip.setDateEdited(new Date());

                if (trip.getStatus() == ServerConstants.TRIP_STATUS_APPROVED && !trip.getIsDirectToPort()) {

                    AnprLog anprLog = anprBaseService.getAnprLogByRequestTypeAndParkingPermissionIdAndTripId(
                        ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_ENTRY, trip.getParkingPermissionIdParkingEntry(), trip.getId());

                    if (anprLog != null) {
                        anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_ENTRY, ServerConstants.DEFAULT_LONG, trip, null, null,
                            new Date());
                    }

                } else if (trip.getStatus() == ServerConstants.TRIP_STATUS_APPROVED && trip.getIsDirectToPort() && !trip.getIsAllowMultipleEntries()) {

                    AnprLog anprLog = anprBaseService.getAnprLogByRequestTypeAndParkingPermissionIdAndTripId(
                        ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_DIRECT, trip.getParkingPermissionIdPortEntry(), trip.getId());

                    if (anprLog != null) {
                        anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_ENTRY, ServerConstants.DEFAULT_LONG, trip, null, null,
                            new Date());
                    }
                }

                updateTrip(trip);

                break;

            case ServerConstants.ACTION_TYPE_UPDATE_TRIP_SLOT_DATETIME:

                if (StringUtils.isBlank(tripJson.getCode()))
                    throw new PADException(ServerResponseConstants.MISSING_TRIP_CODE_CODE, ServerResponseConstants.MISSING_TRIP_CODE_TEXT, "");

                if (StringUtils.isBlank(tripJson.getDateSlotString()))
                    throw new PADException(ServerResponseConstants.MISSING_DATE_SLOT_CODE, ServerResponseConstants.MISSING_DATE_SLOT_TEXT, "");

                if (StringUtils.isBlank(tripJson.getTimeSlotString()))
                    throw new PADException(ServerResponseConstants.MISSING_TIME_SLOT_CODE, ServerResponseConstants.MISSING_TIME_SLOT_TEXT, "");

                Date dateSlotApproved = null;
                try {
                    dateSlotApproved = ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyyHHmm, tripJson.getDateSlotString() + " " + tripJson.getTimeSlotString());
                } catch (ParseException pe) {
                    throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "updateSlotDatetime#ParseException");
                }

                if (dateSlotApproved.getTime() < today.getTime())
                    throw new PADValidationException(ServerResponseConstants.SLOT_DATE_IN_PAST_CODE, ServerResponseConstants.SLOT_DATE_IN_PAST_TEXT, "");

                // the trip to update
                trip = getTripByCode(tripJson.getCode());

                if (trip == null)
                    throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "trip is null");

                if (trip.getStatus() != ServerConstants.TRIP_STATUS_PENDING_APPROVAL)
                    throw new PADException(ServerResponseConstants.UNEXPECTED_TRIP_STATUS_CODE, ServerResponseConstants.UNEXPECTED_TRIP_STATUS_TEXT, "");

                Mission mission = trip.getMission();

                if (mission == null)
                    throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "mission is null");

                if (dateSlotApproved.before(mission.getDateMissionStart()) || dateSlotApproved.after(mission.getDateMissionEnd()))
                    throw new PADValidationException(ServerResponseConstants.INVALID_SLOT_DATE_RANGE_CODE, ServerResponseConstants.INVALID_SLOT_DATE_RANGE_TEXT, "");

                Trip existingActiveTripForVehicle = globalCheckForActiveBookingForVehicle(trip.getVehicleRegistration(), dateSlotApproved);

                // if there is existing active trip for that vehicle and that existing trip is not the same as the trip getting updated, throw exception
                if (existingActiveTripForVehicle != null && existingActiveTripForVehicle.getId() != trip.getId())
                    throw new PADValidationException(ServerResponseConstants.TRIP_FOR_VEHICLE_ALREADY_BOOKED_CODE, ServerResponseConstants.TRIP_FOR_VEHICLE_ALREADY_BOOKED_TEXT,
                        existingActiveTripForVehicle.getCode());

                if (systemService.getSystemParameter().getIsBookingLimitCheckEnabled()) {
                    // update booking slot counts
                    Calendar calendarDateSlotOld = Calendar.getInstance();
                    calendarDateSlotOld.setTime(trip.getDateSlotApproved() != null ? trip.getDateSlotApproved() : trip.getDateSlotRequested());

                    int hourOfDay = calendarDateSlotOld.get(Calendar.HOUR_OF_DAY);

                    BookingSlotCount bookingSlotCountEntity = systemService.getBookingSlotCountByPortOperatorAndTransactionTypeAndHourSlot(mission.getPortOperatorId(),
                        mission.getTransactionType(), calendarDateSlotOld.getTime(), hourOfDay);

                    if (bookingSlotCountEntity != null && bookingSlotCountEntity.getTripsBookedCount() > 0) {

                        bookingSlotCountEntity.setTripsBookedCount(bookingSlotCountEntity.getTripsBookedCount() - 1);

                        systemService.updateBookingSlot(bookingSlotCountEntity);
                    }

                    systemService.reserveBookingSlot(mission.getPortOperatorId(), mission.getTransactionType(), dateSlotApproved);
                }

                trip.setDateSlotApproved(dateSlotApproved);
                trip.setOperatorId(loggedOperatorId);
                trip.setDateEdited(new Date());

                updateTrip(trip);

                activityLogService.saveActivityLogTrip(ServerConstants.ACTIVITY_LOG_TRIP_UPDATE, loggedOperatorId, trip.getId());

                break;

            case ServerConstants.ACTION_TYPE_APPROVE_TRIP:
            case ServerConstants.ACTION_TYPE_DENY_TRIP:

                if (tripJson.getTripCodes() == null || tripJson.getTripCodes().isEmpty())
                    throw new PADException(ServerResponseConstants.MISSING_TRIP_CODE_CODE, ServerResponseConstants.MISSING_TRIP_CODE_TEXT, "");

                if (tripJson.getActionType() == ServerConstants.ACTION_TYPE_DENY_TRIP && StringUtils.isEmpty(tripJson.getReasonDeny()))
                    throw new PADException(ServerResponseConstants.MISSING_REASON_REJECTED_CODE, ServerResponseConstants.MISSING_REASON_REJECTED_TEXT, "");

                for (String tripCode : tripJson.getTripCodes()) {

                    trip = getTripByCode(tripCode);

                    if (trip == null)
                        throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "trip is null");

                    if (trip.getStatus() != ServerConstants.TRIP_STATUS_PENDING_APPROVAL)
                        throw new PADException(ServerResponseConstants.UNEXPECTED_TRIP_STATUS_CODE, ServerResponseConstants.UNEXPECTED_TRIP_STATUS_TEXT, "");

                    account = accountService.getAccountById(trip.getAccountId());
                    if (account == null)
                        throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");

                    transporter = operatorService.getOperatorByAccountId(account.getId());

                    if (tripJson.getActionType() == ServerConstants.ACTION_TYPE_APPROVE_TRIP && trip.getStatus() == ServerConstants.TRIP_STATUS_PENDING_APPROVAL) {
                        // office operator approval

                        portOperatorTransactionType = systemService.getPortOperatorTransactionTypeEntity(trip.getPortOperatorId(), trip.getMission().getTransactionType());

                        Mission m = trip.getMission();
                        if (m == null)
                            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "mission is null#2");

                        m.setIsDirectToPort(portOperatorTransactionType.getIsDirectToPort());
                        m.setIsAllowMultipleEntries(portOperatorTransactionType.getIsAllowMultipleEntries());

                        trip.setMission(m);
                        trip.setStatus(ServerConstants.TRIP_STATUS_APPROVED);
                        trip.setIsDirectToPort(portOperatorTransactionType.getIsDirectToPort());
                        trip.setIsAllowMultipleEntries(portOperatorTransactionType.getIsAllowMultipleEntries());
                        trip.setDateApprovedDenied(new Date());

                        if (trip.getDateSlotApproved() == null) {
                            trip.setDateSlotApproved(trip.getDateSlotRequested());
                        }

                        AnprParameter anprParameter = anprBaseService.getAnprParameter();

                        driver = driverService.getDriverById(trip.getDriverId());

                        // Transporter Parameters
                        HashMap<String, Object> paramsTransporter = new HashMap<>();
                        paramsTransporter.put("referenceNumber", trip.getMission().getReferenceNumber());
                        paramsTransporter.put("vehicleReg", trip.getVehicleRegistration());

                        try {
                            paramsTransporter.put("slotDateTime", ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, trip.getDateSlotApproved()));
                        } catch (ParseException e) {
                            paramsTransporter.put("slotDateTime", "");
                        }

                        paramsTransporter.put("driverName", driver.getFirstName() + " " + driver.getLastName());
                        paramsTransporter.put("accountName", account.getFirstName());
                        paramsTransporter.put("portOperator", systemService.getPortOperatorNameById(trip.getMission().getPortOperatorId()));

                        if (account.getLanguageId() == ServerConstants.LANGUAGE_FR_ID) {
                            paramsTransporter.put("referenceLabel", "BAD / Réservation Export");
                        } else {
                            paramsTransporter.put("referenceLabel", "BAD / Booking Export");
                        }

                        // Driver Parameters
                        HashMap<String, Object> paramsDriver = new HashMap<>();
                        paramsDriver.put("driverName", driver.getFirstName() + " " + driver.getLastName());

                        try {
                            paramsDriver.put("slotDateTime", ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, trip.getDateSlotApproved()));
                        } catch (ParseException e) {
                            paramsDriver.put("slotDateTime", "");
                        }

                        paramsDriver.put("vehicleReg", trip.getVehicleRegistration());
                        paramsDriver.put("referenceNumber", trip.getMission().getReferenceNumber());

                        if (driver.getLanguageId() == ServerConstants.LANGUAGE_EN_ID) {
                            paramsDriver.put("referenceLabel", "BAD / Booking Export");

                            if (portOperatorTransactionType.getIsDirectToPort()) {
                                paramsDriver.put("parkingOrPortMessage", "Proceed directly to the port at the given time");

                            } else {
                                paramsDriver.put("parkingOrPortMessage",
                                    "You are allowed to enter the parking area " + anprParameter.getParkingPermissionHoursPriorSlotDate() + " hour in advance");
                            }
                        } else if (driver.getLanguageId() == ServerConstants.LANGUAGE_WO_ID) {
                            // TODO add tranlations
                            paramsDriver.put("referenceLabel", "BAD / Réservation Export");

                            if (portOperatorTransactionType.getIsDirectToPort()) {
                                paramsDriver.put("parkingOrPortMessage", "Meune ga déme port si wakhtou bi nioula diokh");

                            } else {
                                paramsDriver.put("parkingOrPortMessage",
                                    "Maye nanioula " + anprParameter.getParkingPermissionHoursPriorSlotDate() + " wakhtou nguir ga nieuw dougou si biir parking bi");
                            }

                        } else if (driver.getLanguageId() == ServerConstants.LANGUAGE_BM_ID) {
                            // TODO add tranlations
                            paramsDriver.put("referenceLabel", "BAD / Réservation Export");

                            if (portOperatorTransactionType.getIsDirectToPort()) {
                                paramsDriver.put("parkingOrPortMessage", "Proceed directly to the port at the given time");

                            } else {
                                paramsDriver.put("parkingOrPortMessage",
                                    "You are allowed to enter the parking area " + anprParameter.getParkingPermissionHoursPriorSlotDate() + " hour in advance");
                            }

                        } else {
                            paramsDriver.put("referenceLabel", "BAD / Réservation Export");

                            if (portOperatorTransactionType.getIsDirectToPort()) {
                                paramsDriver.put("parkingOrPortMessage", "Procédez directement au port à l'heure indiquée");

                            } else {
                                paramsDriver.put("parkingOrPortMessage",
                                    "Vous êtes autorisé à entrer dans le parking " + anprParameter.getParkingPermissionHoursPriorSlotDate() + " heure à l'avance");
                            }
                        }

                        if (account.getType() == ServerConstants.ACCOUNT_TYPE_COMPANY) {
                            // email transporter

                            Email scheduledEmail = new Email();
                            scheduledEmail.setEmailTo(transporter.getUsername());
                            scheduledEmail.setLanguageId(account.getLanguageId());
                            scheduledEmail.setAccountId(account.getId());
                            scheduledEmail.setMissionId(m.getId());
                            scheduledEmail.setTripId(trip.getId());

                            if (account.getIsTripApprovedEmail()) {
                                emailService.scheduleEmailByType(scheduledEmail, ServerConstants.EMAIL_TRIP_APPROVED_NOTIFICATION_TEMPLATE_TYPE, paramsTransporter);
                            }

                        } else {
                            // send sms to individual transporter
                            Sms scheduledSms = new Sms();
                            scheduledSms.setLanguageId(account.getLanguageId());
                            scheduledSms.setAccountId(account.getId());
                            scheduledSms.setMissionId(m.getId());
                            scheduledSms.setTripId(trip.getId());
                            scheduledSms.setMsisdn(transporter.getUsername());

                            smsService.scheduleSmsByType(scheduledSms, ServerConstants.SMS_TRIP_APPROVED_TRANSPORTER_NOTIFICATION_TEMPLATE_TYPE, paramsTransporter);
                        }

                        // text driver with the approved date slot
                        Sms scheduledSms = new Sms();
                        scheduledSms.setLanguageId(driver.getLanguageId());
                        scheduledSms.setAccountId(account.getId());
                        scheduledSms.setMissionId(m.getId());
                        scheduledSms.setTripId(trip.getId());
                        scheduledSms.setMsisdn(trip.getDriverMsisdn());

                        smsService.scheduleSmsByType(scheduledSms, ServerConstants.SMS_TRIP_APPROVED_DRIVER_NOTIFICATION_TEMPLATE_TYPE, paramsDriver);

                    } else if (tripJson.getActionType() == ServerConstants.ACTION_TYPE_DENY_TRIP) {

                        trip.setStatus(ServerConstants.TRIP_STATUS_DENIED_BY_OFFICE_OPERATOR);
                        trip.setReasonDeny(tripJson.getReasonDeny().length() >= ServerConstants.DEFAULT_VALIDATION_LENGTH_128
                            ? tripJson.getReasonDeny().substring(0, ServerConstants.DEFAULT_VALIDATION_LENGTH_128 - 1)
                            : tripJson.getReasonDeny());
                        trip.setDateApprovedDenied(new Date());

                        HashMap<String, Object> params = new HashMap<>();

                        if (account.getLanguageId() == ServerConstants.LANGUAGE_FR_ID) {
                            params.put("referenceLabel", "BAD / Réservation Export");
                        } else {
                            params.put("referenceLabel", "BAD / Booking Export");
                        }

                        driver = driverService.getDriverById(trip.getDriverId());

                        params.put("accountName", account.getFirstName());
                        params.put("portOperator", systemService.getPortOperatorNameById(trip.getMission().getPortOperatorId()));
                        params.put("referenceNumber", trip.getMission().getReferenceNumber());
                        params.put("DriverName", driver.getFirstName() + " " + driver.getLastName());
                        params.put("vehicleReg", trip.getVehicleRegistration());

                        try {
                            params.put("slotDateTime", ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, trip.getDateSlotRequested()));
                        } catch (ParseException e) {
                            params.put("slotDateTime", "");
                        }

                        params.put("reasonDenial", trip.getReasonDeny());

                        if (account.getType() == ServerConstants.ACCOUNT_TYPE_COMPANY) {
                            // email transporter

                            Email scheduledEmail = new Email();
                            scheduledEmail.setEmailTo(transporter.getUsername());
                            scheduledEmail.setLanguageId(account.getLanguageId());
                            scheduledEmail.setAccountId(account.getId());
                            scheduledEmail.setMissionId(trip.getMission().getId());
                            scheduledEmail.setTripId(trip.getId());

                            emailService.scheduleEmailByType(scheduledEmail, ServerConstants.EMAIL_TRIP_DENIED_NOTIFICATION_TEMPLATE_TYPE, params);

                        } else {
                            // send sms to individual transporter
                            Sms scheduledSms = new Sms();
                            scheduledSms.setLanguageId(account.getLanguageId());
                            scheduledSms.setAccountId(account.getId());
                            scheduledSms.setMissionId(trip.getMission().getId());
                            scheduledSms.setTripId(trip.getId());
                            scheduledSms.setMsisdn(transporter.getUsername());

                            smsService.scheduleSmsByType(scheduledSms, ServerConstants.SMS_TRIP_DENIED_TRANSPORTER_NOTIFICATION_TEMPLATE_TYPE, params);
                        }

                        if (systemService.getSystemParameter().getIsBookingLimitCheckEnabled()) {
                            // update booking slot counts
                            Calendar calendarDateSlotOld = Calendar.getInstance();
                            calendarDateSlotOld.setTime(trip.getDateSlotApproved() != null ? trip.getDateSlotApproved() : trip.getDateSlotRequested());

                            int hourOfDay = calendarDateSlotOld.get(Calendar.HOUR_OF_DAY);

                            BookingSlotCount bookingSlotCountEntity = systemService.getBookingSlotCountByPortOperatorAndTransactionTypeAndHourSlot(
                                trip.getMission().getPortOperatorId(), trip.getMission().getTransactionType(), calendarDateSlotOld.getTime(), hourOfDay);

                            if (bookingSlotCountEntity != null && bookingSlotCountEntity.getTripsBookedCount() > 0) {

                                bookingSlotCountEntity.setTripsBookedCount(bookingSlotCountEntity.getTripsBookedCount() - 1);

                                systemService.updateBookingSlot(bookingSlotCountEntity);
                            }
                        }
                    }

                    trip.setOperatorId(loggedOperatorId);
                    trip.setDateEdited(new Date());

                    updateTrip(trip);

                    activityLogService.saveActivityLogTrip(ServerConstants.ACTIVITY_LOG_TRIP_UPDATE, loggedOperatorId, trip.getId());
                }

                break;

            default:
                break;
        }
    }

    // for both direct and indirect to port & not multiple port entry eligible trips (non urgent trips)
    @Override
    public void cancelTrip(String tripCode, Operator loggedOperator) throws PADException, PADValidationException, Exception {

        Trip trip = getTripByCode(tripCode);

        if (trip == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "");

        if (trip.getStatus() != ServerConstants.TRIP_STATUS_PENDING && trip.getStatus() != ServerConstants.TRIP_STATUS_PENDING_APPROVAL
            && trip.getStatus() != ServerConstants.TRIP_STATUS_APPROVED && trip.getStatus() != ServerConstants.TRIP_STATUS_ENTERED_PORT
            && trip.getStatus() != ServerConstants.TRIP_STATUS_IN_FLIGHT && trip.getStatus() != ServerConstants.TRIP_STATUS_IN_TRANSIT)
            throw new PADValidationException(ServerResponseConstants.UNEXPECTED_TRIP_STATUS_CODE, ServerResponseConstants.UNEXPECTED_TRIP_STATUS_TEXT, "");

        if (trip.getParkingPermissionIdParkingEntry() != ServerConstants.DEFAULT_LONG) {

            anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_ENTRY, ServerConstants.DEFAULT_LONG, trip, null, null, new Date());
        }

        if (trip.getParkingPermissionIdPortEntry() != ServerConstants.DEFAULT_LONG) {

            anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_ENTRY, ServerConstants.DEFAULT_LONG, trip, null, null, new Date());
        }

        trip.setStatus(loggedOperator.getRoleId() == ServerConstants.OPERATOR_ROLE_PARKING_KIOSK_OPERATOR ? ServerConstants.TRIP_STATUS_CANCELLED_BY_KIOSK_OPERATOR
            : ServerConstants.TRIP_STATUS_CANCELLED);
        updateTrip(trip);

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

        activityLogService.saveActivityLogTrip(ServerConstants.ACTIVITY_LOG_TRIP_CANCEL, loggedOperator.getId(), trip.getId());
    }

    @Override
    public void cancelAdHocTrip(String tripCode, long loggedOperatorId) throws PADException, PADValidationException, Exception {

        Trip trip = getTripByCode(tripCode);

        if (trip == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "trip is null");

        if (trip.getType() != ServerConstants.TRIP_TYPE_ADHOC)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "trips is not adhoc");

        if (trip.getStatus() != ServerConstants.TRIP_STATUS_APPROVED)
            throw new PADValidationException(ServerResponseConstants.UNEXPECTED_TRIP_STATUS_CODE, ServerResponseConstants.UNEXPECTED_TRIP_STATUS_TEXT, "unexpected status");

        com.pad.server.base.entities.Session session = sessionService.getLastSessionByKioskOperatorId(loggedOperatorId);
        if (session == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#14");

        trip.setStatus(ServerConstants.TRIP_STATUS_CANCELLED);
        trip.setOperatorId(loggedOperatorId);
        trip.setDateEdited(new Date());
        updateTrip(trip);

        session.setAdhocTripsCancelledCount(session.getAdhocTripsCancelledCount() + 1);
        sessionService.updateSession(session);

        if (systemService.getSystemParameter().getIsBookingLimitCheckEnabled()) {

            Calendar calendarDateSlot = Calendar.getInstance();
            calendarDateSlot.setTime(trip.getDateSlotApproved() != null ? trip.getDateSlotApproved() : trip.getDateSlotRequested());

            int hourOfDay = calendarDateSlot.get(Calendar.HOUR_OF_DAY);

            BookingSlotCount bookingSlotCountEntity = systemService.getBookingSlotCountByPortOperatorAndTransactionTypeAndHourSlot(trip.getMission().getPortOperatorId(),
                trip.getMission().getTransactionType(), calendarDateSlot.getTime(), hourOfDay);

            if (bookingSlotCountEntity != null && bookingSlotCountEntity.getTripsBookedCount() > 0) {

                bookingSlotCountEntity.setTripsBookedCount(bookingSlotCountEntity.getTripsBookedCount() - 1);

                systemService.updateBookingSlot(bookingSlotCountEntity);
            }
        }

        activityLogService.saveActivityLogTrip(ServerConstants.ACTIVITY_LOG_TRIP_ADHOC_CANCEL, loggedOperatorId, trip.getId());
    }

    @Override
    public boolean isTripEligibleForParkingWithoutPayment(Account account, BigDecimal tripFeeAmount) {

        boolean isEligible = false;

        if (account != null) {
            // check whether this trip is eligible to enter parking area without payment given account has sufficient balance

            BigDecimal accountAmountHold = account.getAmountHold().abs();
            BigDecimal accountBalanceMinusTripFee = account.getBalanceAmount().subtract(tripFeeAmount);
            BigDecimal accountBalanceMinusTripFeeMinusAmountHold = accountBalanceMinusTripFee.subtract(accountAmountHold);

            if (accountBalanceMinusTripFeeMinusAmountHold.compareTo(BigDecimal.ZERO) < 0) {

                if (accountBalanceMinusTripFeeMinusAmountHold.abs().compareTo(account.getAmountOverdraftLimit()) > 0) {
                    isEligible = false;

                } else {
                    isEligible = true;
                }

            } else {
                isEligible = true;
            }
        } else {

            isEligible = false;
        }

        return isEligible;
    }

    @Override
    public long getTripCount(TripJson tripJson, Long accountId, Date dateSlotRequestedStart, Date dateSlotRequestedEnd, Date dateSlotApprovedStart, Date dateSlotApprovedEnd) {

        PreparedJDBCQuery query = getTripsQuery(PreparedJDBCQuery.QUERY_TYPE_COUNT, tripJson, accountId, dateSlotRequestedStart, dateSlotRequestedEnd, dateSlotApprovedStart,
            dateSlotApprovedEnd);

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Long.class);
    }

    @Override
    public List<TripJson> getTripList(TripJson tripJson, Long accountId, Date dateSlotRequestedFrom, Date dateSlotRequestedTo, Date dateSlotApprovedFrom, Date dateSlotApprovedTo) {

        final List<TripJson> tripList = new ArrayList<>();

        PreparedJDBCQuery query = getTripsQuery(PreparedJDBCQuery.QUERY_TYPE_SELECT, tripJson, accountId, dateSlotRequestedFrom, dateSlotRequestedTo, dateSlotApprovedFrom,
            dateSlotApprovedTo);

        if (StringUtils.isBlank(tripJson.getSortColumn())) {
            query.append(" ORDER BY trips.id DESC");
        } else {
            query.append(" ORDER BY ").append(tripJson.getSortColumn()).append(tripJson.getSortAsc() ? " ASC" : " DESC");
        }

        query.append(" LIMIT ").append(ServerUtil.getStartLimitPagination(tripJson.getCurrentPage(), tripJson.getPageCount())).append(", ").append(tripJson.getPageCount());

        jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {

                AccountJson accountJson = new AccountJson();
                accountJson.setNumber(rs.getLong("accounts.number"));
                accountJson.setType(rs.getInt("accounts.type"));
                accountJson.setCompanyName(rs.getString("accounts.company_name"));
                accountJson.setFirstName(rs.getString("accounts.first_name"));
                accountJson.setLastName(rs.getString("accounts.last_name"));
                accountJson.setAccountName(rs.getString("accountName"));

                TripJson tripJson = new TripJson();
                tripJson.setCode(rs.getString("trips.code"));
                tripJson.setAccount(accountJson);
                tripJson.setPortOperatorId(rs.getInt("missions.port_operator_id"));
                tripJson.setIndependentPortOperatorCode(systemService.getIndependentPortOperatorCodeById(rs.getLong("missions.independent_port_operator_id")));
                tripJson.setTransactionType(rs.getInt("missions.transaction_type"));
                tripJson.setReferenceNumber(rs.getString("missions.reference_number"));
                tripJson.setContainerId(rs.getString("trips.container_id"));
                tripJson.setVehicleRegistration(rs.getString("trips.vehicle_registration"));
                tripJson.setVehicleCode(rs.getString("vehicles.code"));
                tripJson.setIsVehicleAddedApi(rs.getBoolean("vehicles.is_added_api"));
                tripJson.setIsVehicleApproved(rs.getBoolean("vehicles.is_approved"));
                tripJson.setIsVehicleActive(rs.getBoolean("vehicles.is_active"));

                if (StringUtils.isNotBlank(rs.getString("drivers.first_name"))) {
                    tripJson.setDriverName(rs.getString("drivers.first_name") + " " + rs.getString("drivers.last_name"));
                } else {
                    tripJson.setDriverName("");
                }

                if (rs.getLong("trips.type") == ServerConstants.TRIP_TYPE_ADHOC) {
                    tripJson.setAdHoc(true);
                } else {
                    tripJson.setAdHoc(false);
                }

                tripJson.setStatus(rs.getInt("trips.status"));

                try {
                    tripJson.setDateSlotString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("trips.date_slot_requested")));
                } catch (ParseException e) {
                    tripJson.setDateSlotString("");
                }

                try {
                    tripJson.setDateSlotApprovedString(rs.getTimestamp("trips.date_slot_approved") == null ? ""
                        : ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("trips.date_slot_approved")));
                } catch (ParseException e) {
                    tripJson.setDateSlotApprovedString("");
                }

                try {
                    tripJson.setDateCreatedString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("trips.date_created")));
                } catch (ParseException e) {
                    tripJson.setDateCreatedString("");
                }

                try {
                    tripJson.setDateApprovedDeniedString(rs.getTimestamp("trips.date_approved_denied") == null ? ""
                        : ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("trips.date_approved_denied")));
                } catch (ParseException e) {
                    tripJson.setDateApprovedDeniedString("");
                }

                try {
                    tripJson.setDateParkingEntryString(rs.getTimestamp("trips.date_entry_parking") == null ? ""
                        : ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("trips.date_entry_parking")));
                } catch (ParseException e) {
                    tripJson.setDateParkingEntryString("");
                }

                try {
                    tripJson.setDatePortEntryString(rs.getTimestamp("trips.date_entry_port") == null ? ""
                        : ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("trips.date_entry_port")));
                } catch (ParseException e) {
                    tripJson.setDatePortEntryString("");
                }

                try {
                    tripJson.setDateMissionStartString(rs.getTimestamp("missions.date_mission_start") == null ? ""
                        : ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("missions.date_mission_start")));
                } catch (ParseException e) {
                    tripJson.setDateMissionStartString("");
                }

                try {
                    tripJson.setDateMissionEndString(rs.getTimestamp("missions.date_mission_end") == null ? ""
                        : ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("missions.date_mission_end")));
                } catch (ParseException e) {
                    tripJson.setDateMissionEndString("");
                }

                if (tripJson.getStatus() == ServerConstants.TRIP_STATUS_DENIED_BY_OFFICE_OPERATOR) {
                    tripJson.setReasonDeny(rs.getString("trips.reason_deny"));
                } else if (tripJson.getStatus() == ServerConstants.TRIP_STATUS_DENIED_PORT_ENTRY) {
                    tripJson.setReasonDeny(rs.getString("port_access.reason_deny"));
                }

                tripJson.setFeePaid(rs.getBoolean("trips.is_fee_paid"));
                tripJson.setCurrency(rs.getString("trips.currency"));
                tripJson.setTripFeeAmount(rs.getBigDecimal("trips.amount_fee"));
                tripJson.setIsDirectToPort(rs.getBoolean("trips.is_direct_to_port"));
                tripJson.setIsAllowMultipleEntries(rs.getBoolean("trips.is_allow_multiple_entries"));
                tripJson.setOperatorName(rs.getString("operatorName"));

                tripList.add(tripJson);
            }
        }, query.getQueryParameters());

        return tripList;
    }

    private PreparedJDBCQuery getTripsQuery(int queryType, TripJson tripJson, Long accountId, Date dateSlotRequestedFrom, Date dateSlotRequestedTo, Date dateSlotApprovedFrom,
        Date dateSlotApprovedTo) {

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        if (queryType == PreparedJDBCQuery.QUERY_TYPE_COUNT) {
            query.append(" SELECT COUNT(trips.id) ");

        } else if (queryType == PreparedJDBCQuery.QUERY_TYPE_SELECT) {
            query.append(
                "SELECT accounts.number, accounts.type, accounts.company_name, accounts.first_name, accounts.last_name, IF(accounts.type=1, accounts.company_name, CONCAT(accounts.first_name, ' ', accounts.last_name)) AS accountName,  "
                    + "missions.port_operator_id, missions.independent_port_operator_id, missions.port_operator_gate_id, missions.transaction_type, missions.reference_number, missions.date_mission_start, missions.date_mission_end, "
                    + "trips.container_id, trips.code, trips.vehicle_registration, trips.type, trips.status, trips.driver_id, drivers.first_name, drivers.last_name, trips.date_slot_requested, trips.date_slot_approved, "
                    + "trips.date_entry_parking, trips.date_entry_port, trips.date_created, trips.date_approved_denied, trips.reason_deny, trips.is_fee_paid, trips.currency, trips.amount_fee, trips.is_direct_to_port, trips.is_allow_multiple_entries, "
                    + "CONCAT(operators.first_name, ' ', operators.last_name) AS operatorName, port_access.reason_deny, "
                    + "vehicles.code, vehicles.is_added_api, vehicles.is_approved, vehicles.is_active");
        }

        query.append(" FROM trips trips ");
        query.append(" LEFT JOIN missions missions ON trips.mission_id = missions.id ");
        query.append(" LEFT JOIN independent_port_operators independent_port_operators ON trips.independent_port_operator_id = independent_port_operators.id ");
        query.append(" LEFT JOIN accounts accounts ON trips.account_id = accounts.id ");
        query.append(" LEFT JOIN drivers drivers ON trips.driver_id = drivers.id ");
        query.append(" LEFT JOIN operators operators ON trips.operator_id = operators.id ");
        query.append(" LEFT JOIN port_access port_access ON trips.id = port_access.trip_id ");
        query.append(" LEFT JOIN vehicles vehicles ON trips.vehicle_id = vehicles.id ");
        query.append(" WHERE (1 = 1) ");

        if (tripJson.getStatus() != ServerConstants.DEFAULT_INT) {
            query.append(" AND trips.status = ?");
            query.addQueryParameter(tripJson.getStatus());
        }

        if (tripJson.getPortOperatorId() != null && tripJson.getPortOperatorId() != ServerConstants.DEFAULT_INT) {
            if (tripJson.getPortOperatorId() == ServerConstants.PORT_OPERATOR_TM) {
                if (tripJson.getIndependentPortOperatorId() == ServerConstants.INDEPENDENT_PORT_OPERATOR_ISTAMCO) {
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
                query.addQueryParameter(tripJson.getPortOperatorId());
            }
        }

        if (tripJson.getIndependentPortOperatorId() != null && tripJson.getIndependentPortOperatorId() != ServerConstants.DEFAULT_INT) {
            if (tripJson.getIndependentPortOperatorId() == ServerConstants.INDEPENDENT_PORT_OPERATOR_ISTAMCO) {
                query.append(" AND trips.independent_port_operator_id IN (?, ?)");
                query.addQueryParameter(tripJson.getIndependentPortOperatorId());
                query.addQueryParameter(ServerConstants.DEFAULT_LONG);
            } else {
                query.append(" AND trips.independent_port_operator_id = ?");
                query.addQueryParameter(tripJson.getIndependentPortOperatorId());
            }
        }

        if (StringUtils.isNotBlank(tripJson.getIndependentPortOperatorCode())) {
            query.append(" AND independent_port_operators.code = ?");
            query.addQueryParameter(tripJson.getIndependentPortOperatorCode());
        }

        if (StringUtils.isNotBlank(tripJson.getIndependentPortOperatorName()) && StringUtils.isBlank(tripJson.getIndependentPortOperatorCode())) {
            query.append(" AND trips.independent_port_operator_id != -1");
            query.append(" AND independent_port_operators.is_active != 0");
            query.append(" AND independent_port_operators.name LIKE ?");
            query.addQueryParameter("%" + tripJson.getIndependentPortOperatorName() + "%");
        }

        if (tripJson.getTransactionType() != null && tripJson.getTransactionType() != ServerConstants.DEFAULT_INT) {
            query.append(" AND missions.transaction_type = ?");
            query.addQueryParameter(tripJson.getTransactionType());
        }

        if (accountId != ServerConstants.DEFAULT_LONG) {
            query.append(" AND trips.account_id = ?");
            query.addQueryParameter(accountId);
        }

        if (StringUtils.isNotBlank(tripJson.getReferenceNumber())) {
            query.append(" AND missions.reference_number = ?");
            query.addQueryParameter(tripJson.getReferenceNumber());
        }

        if (StringUtils.isNotBlank(tripJson.getContainerId())) {
            query.append(" AND trips.container_id = ?");
            query.addQueryParameter(tripJson.getContainerId());
        }

        if (StringUtils.isNotBlank(tripJson.getVehicleRegistration())) {
            query.append(" AND trips.vehicle_registration = ?");
            query.addQueryParameter(tripJson.getVehicleRegistration());
        }

        if (dateSlotRequestedFrom != null) {
            query.append(" AND trips.date_slot_requested >= ?");
            query.addQueryParameter(dateSlotRequestedFrom);
        }

        if (dateSlotRequestedTo != null) {
            query.append(" AND trips.date_slot_requested <= ?");
            query.addQueryParameter(dateSlotRequestedTo);
        }

        if (dateSlotApprovedFrom != null) {
            query.append(" AND trips.date_slot_approved >= ?");
            query.addQueryParameter(dateSlotApprovedFrom);
        }

        if (dateSlotApprovedTo != null) {
            query.append(" AND trips.date_slot_approved <= ?");
            query.addQueryParameter(dateSlotApprovedTo);
        }

        if (tripJson.getAccountNumber() >= ServerConstants.ACCOUNT_NUMBER_MIN && tripJson.getAccountNumber() <= ServerConstants.ACCOUNT_NUMBER_MAX) {
            query.append(" AND accounts.number = ?");
            query.addQueryParameter(tripJson.getAccountNumber());
        }

        return query;
    }

    @Override
    @Transactional(rollbackFor = { PADException.class, PADValidationException.class, Exception.class })
    public String addTrip(TripJson tripJson, long accountId, long operatorCreatedId) throws PADException, PADValidationException {

        Vehicle vehicle = null;
        Mission mission = null;

        PortOperatorTransactionType portOperatorTransactionType = null;

        Account account = accountService.getAccountById(accountId);
        if (account == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");

        vehicle = vehicleService.getVehicleByCode(tripJson.getVehicleCode());

        if (vehicle == null)
            throw new PADException(ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_CODE, ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_TEXT,
                "vehicle is null");

        if (!vehicle.getIsActive() && !vehicle.getIsAddedApi())
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "vehicle is not active");

        portOperatorTransactionType = systemService.getPortOperatorTransactionTypeEntity(tripJson.getPortOperatorId(), tripJson.getTransactionType());

        if (portOperatorTransactionType.getIsDirectToPort()) {

            BigDecimal tripFeeAmount = systemService.getTripFeeAmount(tripJson.getPortOperatorId(), tripJson.getTransactionType(), vehicle.getRegistrationCountryISO());
            BigDecimal accountAmountHold = account.getAmountHold().abs();
            BigDecimal accountBalanceMinusTripFee = account.getBalanceAmount().subtract(tripFeeAmount);
            BigDecimal accountBalanceMinusTripFeeMinusAmountHold = accountBalanceMinusTripFee.subtract(accountAmountHold);

            if (accountBalanceMinusTripFeeMinusAmountHold.compareTo(BigDecimal.ZERO) < 0) {

                if (accountBalanceMinusTripFeeMinusAmountHold.abs().compareTo(account.getAmountOverdraftLimit()) > 0)
                    throw new PADValidationException(ServerResponseConstants.ACCOUNT_BALANCE_LOW_CODE, ServerResponseConstants.ACCOUNT_BALANCE_LOW_TEXT, "");
            }
        }

        Date dateSlot = null;
        try {
            dateSlot = ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyyHHmm, tripJson.getDateSlotString() + " " + tripJson.getTimeSlotString());
        } catch (ParseException pe) {
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "");
        }

        mission = missionService.getMissionByCode(tripJson.getMissionCode());

        if (mission == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "mission is null");

        // for (Trip trip : mission.getTripList()) {
        // // allow trip for vehicle to be added only if there are no other scheduled trips for this vehicle on this mission
        // if (trip.getVehicleId() == vehicle.getId()
        // && (trip.getStatus() == ServerConstants.TRIP_STATUS_PENDING_APPROVAL || trip.getStatus() == ServerConstants.TRIP_STATUS_APPROVED
        // || trip.getStatus() == ServerConstants.TRIP_STATUS_ENTERED_PARKING || trip.getStatus() == ServerConstants.TRIP_STATUS_EXITED_PARKING_PREMATURELY
        // || trip.getStatus() == ServerConstants.TRIP_STATUS_IN_TRANSIT || trip.getStatus() == ServerConstants.TRIP_STATUS_ENTERED_PORT))
        // throw new PADValidationException(ServerResponseConstants.VEHICLE_ON_ACTIVE_TRIP_CODE, ServerResponseConstants.VEHICLE_ON_ACTIVE_TRIP_TEXT, "");
        // }

        Date today = new Date();
        if (dateSlot.getTime() < today.getTime())
            throw new PADValidationException(ServerResponseConstants.SLOT_DATE_IN_PAST_CODE, ServerResponseConstants.SLOT_DATE_IN_PAST_TEXT, "");

        if (dateSlot.before(mission.getDateMissionStart()) || dateSlot.after(mission.getDateMissionEnd()))
            throw new PADValidationException(ServerResponseConstants.INVALID_SLOT_DATE_RANGE_CODE, ServerResponseConstants.INVALID_SLOT_DATE_RANGE_TEXT, "");

        Trip existingActiveTripForVehicle = globalCheckForActiveBookingForVehicle(vehicle.getVehicleRegistration(), dateSlot);

        if (existingActiveTripForVehicle != null)
            throw new PADValidationException(ServerResponseConstants.TRIP_FOR_VEHICLE_ALREADY_BOOKED_CODE, ServerResponseConstants.TRIP_FOR_VEHICLE_ALREADY_BOOKED_TEXT,
                existingActiveTripForVehicle.getCode());

        systemService.reserveBookingSlot(tripJson.getPortOperatorId(), tripJson.getTransactionType(), dateSlot);

        Driver driver = driverService.getDriverByCode(tripJson.getDriverCode());

        Trip trip = new Trip();
        trip.setCode(SecurityUtil.generateUniqueCode());
        trip.setType(ServerConstants.TRIP_TYPE_BOOKED);
        trip.setStatus(driver == null ? ServerConstants.TRIP_STATUS_PENDING : ServerConstants.TRIP_STATUS_PENDING_APPROVAL);
        trip.setParkingPermissionIdPortEntry(ServerConstants.DEFAULT_LONG);

        if (portOperatorTransactionType.getIsDirectToPort() && portOperatorTransactionType.getIsAllowMultipleEntries()) {

            List<Trip> tripList = mission.getTripList();

            for (Trip t : tripList) {
                if (t.getVehicleId() == vehicle.getId() && t.getStatus() != ServerConstants.TRIP_STATUS_PENDING_APPROVAL
                    && t.getStatus() != ServerConstants.TRIP_STATUS_DENIED_BY_OFFICE_OPERATOR) {

                    // there was already a trip booked for this vehicle on this urgent mission. In that case any subsequent trips booked for that vehicle will be automatically
                    // approved
                    // no need to schedule port entry permission request since there is already a request being created when the first trip was booked for this vehicle on this
                    // urgent mission
                    trip.setStatus(ServerConstants.TRIP_STATUS_APPROVED);
                    trip.setParkingPermissionIdPortEntry(t.getParkingPermissionIdPortEntry());
                    trip.setDateSlotApproved(dateSlot);

                    break;
                }
            }
        }

        trip.setAccountId(accountId);
        trip.setMission(mission);
        trip.setVehicleId(vehicle.getId());
        trip.setDriverId(driver == null ? ServerConstants.DEFAULT_LONG : driver.getId());
        trip.setReferenceNumber(mission.getReferenceNumber());
        trip.setVehicleRegistration(vehicle.getVehicleRegistration());
        trip.setVehicleRegistrationCountryISO(vehicle.getRegistrationCountryISO());
        trip.setContainerId(tripJson.getContainerId() == null ? ServerConstants.DEFAULT_STRING : tripJson.getContainerId());
        trip.setContainerType(tripJson.getContainerType() == null ? ServerConstants.DEFAULT_STRING : tripJson.getContainerType());
        trip.setPortOperatorId(mission.getPortOperatorId());
        trip.setIndependentPortOperatorId(mission.getIndependentPortOperatorId());
        trip.setTransactionType(mission.getTransactionType());
        trip.setPortOperatorGateId(mission.getPortOperatorGateId());
        trip.setParkingPermissionId(ServerConstants.DEFAULT_LONG);
        trip.setParkingPermissionIdParkingEntryFirst(ServerConstants.DEFAULT_LONG);
        trip.setParkingPermissionIdParkingEntry(ServerConstants.DEFAULT_LONG);
        trip.setParkingPermissionIdParkingExit(ServerConstants.DEFAULT_LONG);
        trip.setParkingPermissionIdPortExit(ServerConstants.DEFAULT_LONG);
        trip.setDriverMsisdn(driver == null ? ServerConstants.DEFAULT_STRING : driver.getMsisdn());
        trip.setDriverLanguageId(driver == null ? ServerConstants.DEFAULT_LONG : driver.getLanguageId());
        trip.setCompanyName(tripJson.getCompanyName() == null ? ServerConstants.DEFAULT_STRING : tripJson.getCompanyName());
        trip.setOperatorIdCreated(operatorCreatedId);
        trip.setOperatorId(ServerConstants.DEFAULT_LONG);
        trip.setLaneSessionId(ServerConstants.DEFAULT_LONG);
        trip.setFeePaid(false);
        trip.setCurrency(ServerConstants.CURRENCY_CFA_FRANC);
        trip.setFeeAmount(systemService.getTripFeeAmount(trip.getPortOperatorId(), mission.getTransactionType(), trip.getVehicleRegistrationCountryISO()));
        trip.setOperatorFeeAmount(systemService.getOperatorTripFeeAmount(trip.getPortOperatorId(), mission.getTransactionType()));
        trip.setDateSlotRequested(dateSlot);
        trip.setParkingEntryCount(0);
        trip.setParkingExitCount(0);
        trip.setPortEntryCount(0);
        trip.setPortExitCount(0);
        trip.setIsDirectToPort(portOperatorTransactionType.getIsDirectToPort());
        trip.setIsAllowMultipleEntries(portOperatorTransactionType.getIsAllowMultipleEntries());
        trip.setReasonDeny(ServerConstants.DEFAULT_STRING);
        trip.setDateCreated(new Date());
        trip.setDateEdited(trip.getDateCreated());

        missionService.saveMissionTrip(trip);

        mission.setTripsBookedCount(mission.getTripsBookedCount() + 1);
        mission.setStatus(ServerConstants.MISSION_STATUS_TRIPS_BOOKED);
        mission.setDateEdited(new Date());

        missionService.updateMission(mission);

        activityLogService.saveActivityLogTrip(ServerConstants.ACTIVITY_LOG_TRIP_ADD, operatorCreatedId, trip.getId());

        return trip.getCode();
    }

    @Override
    @Transactional(rollbackFor = { PADException.class, PADValidationException.class, Exception.class })
    public TripJson createAdhocTrip(TripJson tripJson, Operator loggedOperator) throws PADException, PADValidationException {

        Mission mission = null;
        Trip trip = null;

        com.pad.server.base.entities.Session kioskSession = null;

        PortOperatorTransactionType portOperatorTransactionType = null;

        Date dateSlot = null;
        try {
            dateSlot = ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyyHHmm, tripJson.getDateSlotString() + " " + tripJson.getTimeSlotString());
        } catch (ParseException pe) {
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "createAdhocTrip#ParseException");
        }

        Date today = new Date();
        if (dateSlot.getTime() < today.getTime())
            throw new PADValidationException(ServerResponseConstants.SLOT_DATE_IN_PAST_CODE, ServerResponseConstants.SLOT_DATE_IN_PAST_TEXT, "");

        portOperatorTransactionType = systemService.getPortOperatorTransactionTypeEntity(tripJson.getPortOperatorId(), tripJson.getTransactionType());

        if (loggedOperator.getRoleId() == ServerConstants.OPERATOR_ROLE_PARKING_KIOSK_OPERATOR) {

            kioskSession = sessionService.getLastSessionByKioskOperatorId(loggedOperator.getId());

            if (kioskSession == null)
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "kioskSession is null");

            if (kioskSession.getType() == ServerConstants.SESSION_TYPE_PARKING && !portOperatorTransactionType.getIsAllowedForParkingAndKioskOp())
                throw new PADValidationException(ServerResponseConstants.ADHOC_TRIP_CREATE_NOT_ALLOWED_CODE, ServerResponseConstants.ADHOC_TRIP_CREATE_NOT_ALLOWED_TEXT, "#1");

            else if (kioskSession.getType() == ServerConstants.SESSION_TYPE_VIRTUAL && !portOperatorTransactionType.getIsAllowedForVirtualKioskOp())
                throw new PADValidationException(ServerResponseConstants.ADHOC_TRIP_CREATE_NOT_ALLOWED_CODE, ServerResponseConstants.ADHOC_TRIP_CREATE_NOT_ALLOWED_TEXT, "#2");

            if (kioskSession.getType() == ServerConstants.SESSION_TYPE_VIRTUAL && tripJson.getPortOperatorId() != ServerConstants.PORT_OPERATOR_DAKAR_TERMINAL
                && tripJson.getPortOperatorId() != ServerConstants.PORT_OPERATOR_TM_SOUTH && portOperatorTransactionType.getIsDirectToPort()
                && portOperatorTransactionType.getIsAllowMultipleEntries())
                throw new PADValidationException(ServerResponseConstants.ADHOC_TRIP_CREATE_NOT_ALLOWED_CODE, ServerResponseConstants.ADHOC_TRIP_CREATE_NOT_ALLOWED_TEXT, "#3");

            if (kioskSession.getType() == ServerConstants.SESSION_TYPE_VIRTUAL && dateSlot.getTime() > (today.getTime() + TimeUnit.HOURS.toMillis(24)))
                throw new PADValidationException(ServerResponseConstants.ADHOC_TRIP_DATE_SLOT_MAX_LIMIT_EXCEEDED_CODE,
                    ServerResponseConstants.ADHOC_TRIP_DATE_SLOT_MAX_LIMIT_EXCEEDED_TEXT, "");
        }

        String vehicleRegNumber = ServerUtil.formatVehicleRegNumber(tripJson.getVehicleRegistration());
        String driverMsisdn = ServerUtil.getValidNumber(tripJson.getDriverMobile(), "createAdhocTrip#");
        int responseCode = Integer.parseInt(tripJson.getGetTripResponseCode());

        Trip existingActiveTripForVehicle = globalCheckForActiveBookingForVehicle(vehicleRegNumber, dateSlot);

        if (existingActiveTripForVehicle != null)
            throw new PADValidationException(ServerResponseConstants.TRIP_FOR_VEHICLE_ALREADY_BOOKED_CODE, ServerResponseConstants.TRIP_FOR_VEHICLE_ALREADY_BOOKED_TEXT,
                existingActiveTripForVehicle.getCode());

        systemService.reserveBookingSlot(tripJson.getPortOperatorId(), tripJson.getTransactionType(), dateSlot);

        switch (responseCode) {
            case ServerResponseConstants.CREATE_ADHOC_TRIP_CODE:

                mission = missionService.getMissionByPortOperatorIdAndTransactionTypeAndReferenceNumber(tripJson.getPortOperatorId(), tripJson.getTransactionType(),
                    tripJson.getReferenceNumber());

                if (mission != null) {

                    if (dateSlot.before(mission.getDateMissionStart()) || dateSlot.after(mission.getDateMissionEnd()))
                        throw new PADValidationException(ServerResponseConstants.INVALID_SLOT_DATE_RANGE_CODE, ServerResponseConstants.INVALID_SLOT_DATE_RANGE_TEXT, "");

                    Vehicle vehicle = vehicleService.getVehicleByAccountIdAndRegNumber(mission.getAccountId(), vehicleRegNumber);

                    trip = new Trip();
                    trip.setCode(SecurityUtil.generateUniqueCode());
                    trip.setType(ServerConstants.TRIP_TYPE_ADHOC);
                    trip.setStatus(ServerConstants.TRIP_STATUS_APPROVED);
                    trip.setAccountId(mission.getAccountId());
                    trip.setMission(mission);
                    trip.setVehicleId(vehicle == null ? ServerConstants.DEFAULT_LONG : vehicle.getId());
                    trip.setDriverId(ServerConstants.DEFAULT_LONG);
                    trip.setPortOperatorId(mission.getPortOperatorId());
                    trip.setIndependentPortOperatorId(mission.getIndependentPortOperatorId());
                    trip.setTransactionType(mission.getTransactionType());
                    trip.setPortOperatorGateId(mission.getPortOperatorGateId());
                    trip.setParkingPermissionId(ServerConstants.DEFAULT_LONG);
                    trip.setParkingPermissionIdParkingEntryFirst(ServerConstants.DEFAULT_LONG);
                    trip.setParkingPermissionIdParkingEntry(ServerConstants.DEFAULT_LONG);
                    trip.setParkingPermissionIdParkingExit(ServerConstants.DEFAULT_LONG);
                    trip.setParkingPermissionIdPortEntry(ServerConstants.DEFAULT_LONG);
                    trip.setParkingPermissionIdPortExit(ServerConstants.DEFAULT_LONG);
                    trip.setReferenceNumber(mission.getReferenceNumber());
                    trip.setVehicleRegistration(vehicleRegNumber);
                    trip.setVehicleRegistrationCountryISO(tripJson.getVehicleRegistrationCountryISO());
                    trip.setContainerId(tripJson.getContainerId() == null ? ServerConstants.DEFAULT_STRING : tripJson.getContainerId());
                    trip.setContainerType(tripJson.getContainerType() == null ? ServerConstants.DEFAULT_STRING : tripJson.getContainerType());
                    trip.setDriverMsisdn(driverMsisdn);
                    trip.setDriverLanguageId(tripJson.getDriverLanguageId());
                    trip.setCompanyName(tripJson.getCompanyName() == null ? ServerConstants.DEFAULT_STRING : tripJson.getCompanyName());
                    trip.setOperatorIdCreated(loggedOperator.getId());
                    trip.setOperatorId(loggedOperator.getId());
                    trip.setLaneSessionId(ServerConstants.DEFAULT_LONG);
                    trip.setFeePaid(false);
                    trip.setCurrency(ServerConstants.CURRENCY_CFA_FRANC);
                    trip.setFeeAmount(systemService.getTripFeeAmount(mission.getPortOperatorId(), mission.getTransactionType(), trip.getVehicleRegistrationCountryISO()));
                    trip.setOperatorFeeAmount(systemService.getOperatorTripFeeAmount(trip.getPortOperatorId(), mission.getTransactionType()));
                    trip.setDateSlotRequested(dateSlot);
                    trip.setDateSlotApproved(dateSlot);
                    trip.setParkingEntryCount(0);
                    trip.setParkingExitCount(0);
                    trip.setPortEntryCount(0);
                    trip.setPortExitCount(0);
                    trip.setIsDirectToPort(portOperatorTransactionType.getIsDirectToPort());
                    trip.setIsAllowMultipleEntries(portOperatorTransactionType.getIsAllowMultipleEntries());
                    trip.setReasonDeny(ServerConstants.DEFAULT_STRING);
                    trip.setDateCreated(new Date());
                    trip.setDateEdited(trip.getDateCreated());

                    missionService.saveMissionTrip(trip);

                    mission.setTripsBookedCount(mission.getTripsBookedCount() + 1);
                    missionService.updateMission(mission);

                    kioskSession.setAdhocTripsCreatedCount(kioskSession.getAdhocTripsCreatedCount() + 1);
                    sessionService.updateSession(kioskSession);

                    Account account = accountService.getAccountById(trip.getAccountId());

                    tripJson = new TripJson();
                    tripJson.setCode(trip.getCode());
                    tripJson.setVehicleCode("");
                    tripJson.setDriverCode("");
                    tripJson.setVehicleRegistration(trip.getVehicleRegistration());
                    tripJson.setDriverName("");
                    tripJson.setStatus(trip.getStatus());
                    tripJson.setFeePaid(trip.isFeePaid());
                    tripJson.setAdHoc((trip != null && trip.getType() == ServerConstants.TRIP_TYPE_ADHOC) ? true : false);

                    try {
                        tripJson.setDateSlotString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, trip.getDateSlotRequested()));
                    } catch (ParseException e) {
                        tripJson.setDateSlotString("");
                    }

                    tripJson.setPortOperatorId(trip.getMission().getPortOperatorId());
                    tripJson.setReferenceNumber(trip.getMission().getReferenceNumber());
                    tripJson.setContainerId(trip.getContainerId());
                    tripJson.setAccountName((account == null ? "" : account.getFirstName() + " " + account.getLastName()));
                    tripJson.setCompanyName((account == null ? tripJson.getCompanyName() : account.getCompanyName()));
                    tripJson.setCurrency((account == null ? "" : account.getCurrency()));
                    tripJson.setAccountBalance((account == null ? BigDecimal.ZERO : account.getBalanceAmount()));
                    tripJson.setDriverMobile(trip.getDriverMsisdn());
                    tripJson.setDriverLanguageId(trip.getDriverLanguageId());
                    tripJson.setAccount(null);
                    tripJson.setTripFeeAmount(trip.getFeeAmount());

                    try {
                        tripJson.setDateMissionStartString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, trip.getMission().getDateMissionStart()));
                    } catch (ParseException e) {
                        tripJson.setDateMissionStartString("");
                    }

                    try {
                        tripJson.setDateMissionEndString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, trip.getMission().getDateMissionEnd()));
                    } catch (ParseException e) {
                        tripJson.setDateMissionEndString("");
                    }

                    tripJson.setIsVehicleActive(false);

                    // set account info for parking kiosk operator
                    if (loggedOperator.getRoleId() == ServerConstants.OPERATOR_ROLE_PARKING_KIOSK_OPERATOR && account != null) {

                        AccountJson accountJson = new AccountJson();
                        BeanUtils.copyProperties(account, accountJson);

                        tripJson.setAccount(accountJson);

                        tripJson.setIsVehicleActive(vehicle != null && vehicle.getIsActive());
                    }

                } else
                    throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "createAdhocTrip#Mission is null");

                activityLogService.saveActivityLogTrip(ServerConstants.ACTIVITY_LOG_TRIP_ADHOC_ADD, loggedOperator.getId(), trip.getId());

                break;

            case ServerResponseConstants.CREATE_ADHOC_TRIP_AND_MISSION_CODE:
            case ServerResponseConstants.NO_MISSION_ASSOCIATED_WITH_TRIP_CODE:

                IndependentPortOperator independentPortOperator = StringUtils.isBlank(tripJson.getIndependentPortOperatorCode()) ? null
                    : systemService.getIndependentPortOperatorByCode(tripJson.getIndependentPortOperatorCode());

                if ((tripJson.getPortOperatorId() == ServerConstants.PORT_OPERATOR_TM_NORTH || tripJson.getPortOperatorId() == ServerConstants.PORT_OPERATOR_TM_SOUTH)
                    && independentPortOperator == null)
                    throw new PADException(ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_CODE, ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_TEXT, "");

                if (tripJson.getGateId() == null)
                    throw new PADException(ServerResponseConstants.MISSING_GATE_ID_CODE, ServerResponseConstants.MISSING_GATE_ID_TEXT, "");

                if (tripJson.getTransactionType() == null)
                    throw new PADException(ServerResponseConstants.MISSING_TRANSACTION_TYPE_CODE, ServerResponseConstants.MISSING_TRANSACTION_TYPE_TEXT, "");

                if (StringUtils.isBlank(tripJson.getReferenceNumber()))
                    throw new PADException(ServerResponseConstants.MISSING_REFERENCE_NUMBER_CODE, ServerResponseConstants.MISSING_REFERENCE_NUMBER_TEXT, "");

                if (tripJson.getReferenceNumber().length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_16)
                    throw new PADException(ServerResponseConstants.INVALID_REFERENCE_NUMBER_CODE, ServerResponseConstants.INVALID_REFERENCE_NUMBER_TEXT, "#1");

                if (!tripJson.getReferenceNumber().matches(ServerConstants.REGEX_ALPHA_NUMERIC))
                    throw new PADException(ServerResponseConstants.INVALID_REFERENCE_NUMBER_CODE, ServerResponseConstants.INVALID_REFERENCE_NUMBER_TEXT, "#2");

                mission = new Mission();
                mission.setCode(SecurityUtil.generateUniqueCode());
                mission.setStatus(ServerConstants.MISSION_STATUS_TRIPS_BOOKED);
                mission.setAccountId(ServerConstants.DEFAULT_LONG);
                mission.setPortOperatorId(tripJson.getPortOperatorId());
                mission.setIndependentPortOperatorId(independentPortOperator == null ? ServerConstants.DEFAULT_LONG : independentPortOperator.getId());
                mission.setPortOperatorGateId(tripJson.getGateId());
                mission.setTransactionType(tripJson.getTransactionType());
                mission.setReferenceNumber(tripJson.getReferenceNumber() == null ? ServerConstants.DEFAULT_STRING : tripJson.getReferenceNumber());
                mission.setTripsCompletedCount(0);
                mission.setTripsBookedCount(1);
                mission.setOperatorId(loggedOperator.getId());
                mission.setIsDirectToPort(portOperatorTransactionType.getIsDirectToPort());
                mission.setIsAllowMultipleEntries(portOperatorTransactionType.getIsAllowMultipleEntries());
                mission.setDateMissionStart(new Date());

                Calendar calendarDateMissionEnd = Calendar.getInstance();
                calendarDateMissionEnd.setTime(dateSlot);
                calendarDateMissionEnd.set(Calendar.SECOND, 59);
                calendarDateMissionEnd.set(Calendar.MINUTE, 59);
                calendarDateMissionEnd.set(Calendar.HOUR_OF_DAY, 23);

                mission.setDateMissionEnd(calendarDateMissionEnd.getTime());
                mission.setDateCreated(new Date());
                mission.setDateEdited(mission.getDateCreated());

                missionService.saveMission(mission);

                trip = new Trip();
                trip.setCode(SecurityUtil.generateUniqueCode());
                trip.setType(ServerConstants.TRIP_TYPE_ADHOC);
                trip.setStatus(ServerConstants.TRIP_STATUS_APPROVED);
                trip.setAccountId(ServerConstants.DEFAULT_LONG);
                trip.setMission(mission);
                trip.setVehicleId(ServerConstants.DEFAULT_LONG);
                trip.setDriverId(ServerConstants.DEFAULT_LONG);
                trip.setReferenceNumber(mission.getReferenceNumber());
                trip.setContainerId(tripJson.getContainerId() == null ? ServerConstants.DEFAULT_STRING : tripJson.getContainerId());
                trip.setContainerType(tripJson.getContainerType() == null ? ServerConstants.DEFAULT_STRING : tripJson.getContainerType());
                trip.setPortOperatorId(mission.getPortOperatorId());
                trip.setIndependentPortOperatorId(mission.getIndependentPortOperatorId());
                trip.setTransactionType(mission.getTransactionType());
                trip.setPortOperatorGateId(mission.getPortOperatorGateId());
                trip.setParkingPermissionId(ServerConstants.DEFAULT_LONG);
                trip.setParkingPermissionIdParkingEntryFirst(ServerConstants.DEFAULT_LONG);
                trip.setParkingPermissionIdParkingEntry(ServerConstants.DEFAULT_LONG);
                trip.setParkingPermissionIdParkingExit(ServerConstants.DEFAULT_LONG);
                trip.setParkingPermissionIdPortEntry(ServerConstants.DEFAULT_LONG);
                trip.setParkingPermissionIdPortExit(ServerConstants.DEFAULT_LONG);
                trip.setVehicleRegistration(vehicleRegNumber);
                trip.setVehicleRegistrationCountryISO(tripJson.getVehicleRegistrationCountryISO());
                trip.setDriverMsisdn(driverMsisdn);
                trip.setDriverLanguageId(tripJson.getDriverLanguageId());
                trip.setCompanyName(tripJson.getCompanyName() == null ? ServerConstants.DEFAULT_STRING : tripJson.getCompanyName());
                trip.setOperatorIdCreated(loggedOperator.getId());
                trip.setOperatorId(loggedOperator.getId());
                trip.setLaneSessionId(ServerConstants.DEFAULT_LONG);
                trip.setFeePaid(false);
                trip.setCurrency(ServerConstants.CURRENCY_CFA_FRANC);
                trip.setFeeAmount(systemService.getTripFeeAmount(mission.getPortOperatorId(), mission.getTransactionType(), trip.getVehicleRegistrationCountryISO()));
                trip.setOperatorFeeAmount(systemService.getOperatorTripFeeAmount(trip.getPortOperatorId(), mission.getTransactionType()));
                trip.setDateSlotRequested(dateSlot);
                trip.setDateSlotApproved(dateSlot);
                trip.setParkingEntryCount(0);
                trip.setParkingExitCount(0);
                trip.setPortEntryCount(0);
                trip.setPortExitCount(0);
                trip.setIsDirectToPort(portOperatorTransactionType.getIsDirectToPort());
                trip.setIsAllowMultipleEntries(portOperatorTransactionType.getIsAllowMultipleEntries());
                trip.setReasonDeny(ServerConstants.DEFAULT_STRING);
                trip.setDateCreated(new Date());
                trip.setDateEdited(trip.getDateCreated());

                missionService.saveMissionTrip(trip);

                kioskSession.setAdhocTripsCreatedCount(kioskSession.getAdhocTripsCreatedCount() + 1);
                sessionService.updateSession(kioskSession);

                tripJson = new TripJson();
                tripJson.setCode(trip.getCode());
                tripJson.setCurrency(ServerConstants.CURRENCY_CFA_FRANC);
                tripJson.setAccountBalance(BigDecimal.ZERO);
                tripJson.setCompanyName(trip.getCompanyName());
                tripJson.setAdHoc((trip != null && trip.getType() == ServerConstants.TRIP_TYPE_ADHOC) ? true : false);
                tripJson.setIsVehicleActive(false);

                try {
                    tripJson.setDateSlotString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, trip.getDateSlotRequested()));
                } catch (ParseException e) {
                    tripJson.setDateSlotString("");
                }

                tripJson.setTripFeeAmount(trip.getFeeAmount());

                activityLogService.saveActivityLogTrip(ServerConstants.ACTIVITY_LOG_TRIP_ADHOC_ADD, loggedOperator.getId(), trip.getId());

                break;

            default:
                break;
        }

        return tripJson;
    }

    @Override
    public Trip findApprovedTripByVehicleReg(String vehicleRegistration, long parkingPermissionId) throws PADException {

        Trip trip = null;

        try {
            Parking parking = parkingService.getEnteredParkingByVehicleRegistration(vehicleRegistration);

            if (parking != null)
                throw new PADException(ServerResponseConstants.VEHICLE_ALREADY_PARKED_CODE, ServerResponseConstants.VEHICLE_ALREADY_PARKED_TEXT, "");

            trip = getApprovedTripByVehicleRegNumberAndParkingPermissionId(vehicleRegistration, parkingPermissionId);

            if (trip == null)
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE,
                    "No approved trip associated with vehicle: " + vehicleRegistration + " and parking permission id " + parkingPermissionId, "");

        } catch (PADException pade) {
            throw pade;

        } catch (Exception e) {
            logger.error("findApprovedTripByVehicleReg###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "findApprovedTripByVehicleReg#");
        }

        return trip;
    }

    @Override
    public TripJson buildTripJsonEntity(TripJson tripJson, Trip trip, Operator loggedOperator) throws PADValidationException {

        if (trip.getMission() == null)
            throw new PADValidationException(ServerResponseConstants.NO_MISSION_ASSOCIATED_WITH_TRIP_CODE, ServerResponseConstants.NO_MISSION_ASSOCIATED_WITH_TRIP_TEXT, "");

        Account account = accountService.getAccountById(trip.getAccountId());

        tripJson = new TripJson();
        tripJson.setCode(trip.getCode());
        tripJson.setVehicleCode("");
        tripJson.setDriverCode("");
        tripJson.setVehicleRegistration(trip.getVehicleRegistration());
        tripJson.setVehicleRegistrationCountryISO(trip.getVehicleRegistrationCountryISO());
        tripJson.setDriverName("");
        tripJson.setStatus(trip.getStatus());
        tripJson.setFeePaid(trip.isFeePaid());
        tripJson.setAdHoc((trip != null && trip.getType() == ServerConstants.TRIP_TYPE_ADHOC) ? true : false);
        tripJson.setPortOperatorId(trip.getMission().getPortOperatorId());

        try {
            tripJson.setDateSlotString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, trip.getDateSlotApproved()));
        } catch (ParseException e) {
            tripJson.setDateSlotString("");
        }

        long independentPortOperatorId = trip.getMission().getIndependentPortOperatorId();

        IndependentPortOperator independentPortOperator = independentPortOperatorId == ServerConstants.DEFAULT_LONG ? null
            : systemService.getIndependentPortOperatorById(independentPortOperatorId);

        tripJson.setIndependentPortOperatorName(independentPortOperator == null ? "" : independentPortOperator.getName());
        tripJson.setGateNumber(systemService.getPortOperatorGateNumberById(trip.getMission().getPortOperatorGateId()));
        tripJson.setGateNumberShort(systemService.getPortOperatorGateNumberShortById(trip.getMission().getPortOperatorGateId()));
        tripJson.setTransactionType(trip.getMission().getTransactionType());
        tripJson.setReferenceNumber(trip.getMission().getReferenceNumber());
        tripJson.setContainerId(trip.getContainerId());
        tripJson.setAccountName((account == null ? "" : account.getFirstName() + " " + account.getLastName()));
        tripJson.setCompanyName((account == null ? trip.getCompanyName() : account.getCompanyName()));
        tripJson.setCurrency((account == null ? "" : account.getCurrency()));
        tripJson.setAccountBalance((account == null ? BigDecimal.ZERO : account.getBalanceAmount()));
        tripJson.setDriverMobile(trip.getDriverMsisdn());
        tripJson.setDriverLanguageId(trip.getDriverLanguageId());
        tripJson.setAccount(null);
        tripJson.setTripFeeAmount(trip.getFeeAmount());

        try {
            tripJson.setDateMissionStartString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, trip.getMission().getDateMissionStart()));
        } catch (ParseException e) {
            tripJson.setDateMissionStartString("");
        }

        try {
            tripJson.setDateMissionEndString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, trip.getMission().getDateMissionEnd()));
        } catch (ParseException e) {
            tripJson.setDateMissionEndString("");
        }

        tripJson.setIsShowReceiptMessage(account == null ? true : (account.getPaymentTermsType() == ServerConstants.ACCOUNT_PAYMENT_TERMS_TYPE_PREPAY));

        // check whether this trip is eligible to enter parking area without payment given account balance is not over the overdraft limit
        tripJson.setIsEligibleForParkingWithoutPayment(isTripEligibleForParkingWithoutPayment(account, tripJson.getTripFeeAmount()));
        tripJson.setIsDirectToPort(trip.getIsDirectToPort());
        tripJson.setIsAllowMultipleEntries(trip.getIsAllowMultipleEntries());
        tripJson.setIsVehicleActive(true);

        // set account info for parking kiosk operator
        if (loggedOperator.getRoleId() == ServerConstants.OPERATOR_ROLE_PARKING_KIOSK_OPERATOR && account != null) {

            AccountJson accountJson = new AccountJson();
            BeanUtils.copyProperties(account, accountJson);

            tripJson.setAccount(accountJson);
        }

        if (trip.getStatus() == ServerConstants.TRIP_STATUS_EXITED_PARKING_PREMATURELY && trip.isFeePaid() && !trip.getIsDirectToPort() && trip.getDateExitParking() != null) {

            if (new Date().compareTo(new Date(trip.getDateExitParking().getTime() + TimeUnit.MINUTES.toMillis(60))) > 0) {
                tripJson.setIsVehicleAllowedInParkingWithoutPaymentAfterPrematureExit(false);
            } else {
                tripJson.setIsVehicleAllowedInParkingWithoutPaymentAfterPrematureExit(true);
            }
        }

        return tripJson;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getTripCountByStatus(int status) {

        List<Trip> tripList = new ArrayList<>();

        Session currentSession = sessionFactory.getCurrentSession();

        tripList = currentSession.createQuery("FROM Trip t where t.status=:status").setParameter("status", status).list();

        return tripList.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getTripCountByAccountIdAndStatus(long accountId, int status) {

        List<Trip> tripList = new ArrayList<>();

        Session currentSession = sessionFactory.getCurrentSession();

        tripList = currentSession.createQuery("FROM Trip t WHERE t.accountId=:accountId AND t.status=:status").setParameter("accountId", accountId).setParameter("status", status)
            .list();

        return tripList.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Trip> getTripsByAccountIdAndStatus(long accountId, int status) {

        List<Trip> tripList = new ArrayList<>();

        Session currentSession = sessionFactory.getCurrentSession();

        tripList = currentSession.createQuery("FROM Trip t WHERE t.accountId=:accountId AND t.status=:status").setParameter("accountId", accountId).setParameter("status", status)
            .list();

        return tripList;
    }

    @Override
    public void updateExistingMissionTripsFlags(PortOperatorTransactionType portOperatorTransactionType) throws PADException, Exception {

        AnprParameter anprParameter = anprBaseService.getAnprParameter();

        systemService.updatePortOperatorTransactionType(portOperatorTransactionType);

        List<Trip> tripList = getApprovedTripsWithSlotDateInFuture(portOperatorTransactionType.getPortOperator().getId(), portOperatorTransactionType.getTransactionType(),
            anprParameter.getParkingPermissionHoursInFuture());

        if (tripList != null && tripList.size() > 0) {

            for (Trip trip : tripList) {

                Mission mission = trip.getMission();
                mission.setIsDirectToPort(portOperatorTransactionType.getIsDirectToPort());
                mission.setIsAllowMultipleEntries(portOperatorTransactionType.getIsAllowMultipleEntries());
                mission.setDateEdited(new Date());

                trip.setMission(mission);
                trip.setIsDirectToPort(portOperatorTransactionType.getIsDirectToPort());
                trip.setIsAllowMultipleEntries(portOperatorTransactionType.getIsAllowMultipleEntries());
                trip.setDateEdited(new Date());

                hibernateTemplate.update(trip);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveTrip(String tripCode, String driverCode, long loggedOperatorId) throws PADException, PADValidationException, Exception {

        Trip trip = getTripByCode(tripCode);

        if (trip == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "trip is null");

        if (trip.getStatus() != ServerConstants.TRIP_STATUS_PENDING)
            throw new PADValidationException(ServerResponseConstants.UNEXPECTED_TRIP_STATUS_CODE, ServerResponseConstants.UNEXPECTED_TRIP_STATUS_TEXT, "");

        Driver driver = driverService.getDriverByCode(driverCode);

        if (driver == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "driver is null");

        trip.setDriverId(driver.getId());
        trip.setStatus(ServerConstants.TRIP_STATUS_PENDING_APPROVAL);
        trip.setDriverMsisdn(driver.getMsisdn());
        trip.setDriverLanguageId(driver.getLanguageId());
        trip.setOperatorId(loggedOperatorId);
        trip.setDateEdited(new Date());

        updateTrip(trip);

        activityLogService.saveActivityLogTrip(ServerConstants.ACTIVITY_LOG_TRIP_APPROVE, loggedOperatorId, trip.getId());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectTrip(String tripCode, long loggedOperatorId) throws PADException, PADValidationException, Exception {

        Trip trip = getTripByCode(tripCode);

        if (trip == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "");

        if (trip.getStatus() != ServerConstants.TRIP_STATUS_PENDING)
            throw new PADValidationException(ServerResponseConstants.UNEXPECTED_TRIP_STATUS_CODE, ServerResponseConstants.UNEXPECTED_TRIP_STATUS_TEXT, "");

        trip.setStatus(ServerConstants.TRIP_STATUS_CANCELLED);
        trip.setOperatorId(loggedOperatorId);
        trip.setDateEdited(new Date());

        updateTrip(trip);

        Mission mission = trip.getMission();
        mission.setStatus(ServerConstants.MISSION_STATUS_CANCELLED);
        mission.setOperatorId(loggedOperatorId);
        mission.setDateEdited(new Date());

        missionService.updateMission(mission);

        Account transporterAccount = accountService.getAccountById(trip.getAccountId());

        Operator portOperatorOperator = operatorService.getOperatorById(trip.getOperatorIdCreated());

        HashMap<String, Object> params = new HashMap<>();

        params.put("transporterShortName", transporterAccount.getCompanyShortName());
        params.put("vehicleRegNumber", trip.getVehicleRegistration());
        params.put("referenceNumber", trip.getReferenceNumber());
        params.put("transactionType", ServerUtil.getTransactionTypeName(mission.getTransactionType(), portOperatorOperator.getLanguageId()));

        try {
            params.put("startDate", ServerUtil.formatDate(ServerConstants.dateFormatyyyyMMddHHmmss, mission.getDateMissionStart()));
        } catch (ParseException e) {
            params.put("startDate", "");
        }

        try {
            params.put("endDate", ServerUtil.formatDate(ServerConstants.dateFormatyyyyMMddHHmmss, mission.getDateMissionEnd()));
        } catch (ParseException e) {
            params.put("endDate", "");
        }

        Email scheduledEmail = new Email();
        scheduledEmail.setEmailTo(portOperatorOperator.getEmail());
        scheduledEmail.setLanguageId(portOperatorOperator.getLanguageId());
        scheduledEmail.setAccountId(transporterAccount == null ? ServerConstants.DEFAULT_LONG : transporterAccount.getId());
        scheduledEmail.setMissionId(mission.getId());
        scheduledEmail.setTripId(trip.getId());

        emailService.scheduleEmailByType(scheduledEmail, ServerConstants.EMAIL_TRIP_REJECTED_NOTIFICATION_TEMPLATE_TYPE, params);

        activityLogService.saveActivityLogTrip(ServerConstants.ACTIVITY_LOG_TRIP_REJECT, loggedOperatorId, trip.getId());

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Trip> getExpiredUnpaidDirectToPortTripsCreatedByVirtualKiosk() {

        List<Trip> tripList = new ArrayList<>();

        Session currentSession = sessionFactory.getCurrentSession();

        tripList = currentSession.createQuery(
            "SELECT t FROM Trip as t where t.type=:typeAdhoc and t.status=:statusApproved and t.isDirectToPort=1 and t.parkingPermissionId=:parking_permission_id and t.laneSessionId=:lane_session_id and t.isFeePaid=0 "
                + "and t.dateCreated <:date_created_to")
            .setParameter("typeAdhoc", ServerConstants.TRIP_TYPE_ADHOC).setParameter("statusApproved", ServerConstants.TRIP_STATUS_APPROVED)
            .setParameter("parking_permission_id", ServerConstants.DEFAULT_LONG).setParameter("lane_session_id", ServerConstants.DEFAULT_LONG)
            .setParameter("date_created_to", new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(15)), TimestampType.INSTANCE).list();

        return tripList;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Trip> getTripsExitedParkingPrematurelyForMoreThanXMinutes(int minutes) {

        List<Trip> tripList = new ArrayList<>();

        Session currentSession = sessionFactory.getCurrentSession();

        tripList = currentSession.createQuery("FROM Trip t where t.status=:status and t.dateExitParking<=:date_exit_parking ORDER BY t.dateEdited")
            .setParameter("status", ServerConstants.TRIP_STATUS_EXITED_PARKING_PREMATURELY)
            .setParameter("date_exit_parking", new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(minutes)), TimestampType.INSTANCE).list();

        return tripList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Trip> getOldApprovedTrips(long portOperatorId, int transactionType, int tripCancelSystemAfterMinutes) {
        List<Trip> tripList;

        Session currentSession = sessionFactory.getCurrentSession();

        tripList = currentSession.createQuery("FROM Trip t WHERE t.portOperatorId=:port_operator_id AND t.transactionType=:transaction_type AND t.status=:status and t.dateSlotApproved<:date_slot_approved ORDER BY t.dateSlotApproved")
            .setParameter("port_operator_id", portOperatorId)
            .setParameter("transaction_type", transactionType)
            .setParameter("status", ServerConstants.TRIP_STATUS_APPROVED)
            .setParameter("date_slot_approved", new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(tripCancelSystemAfterMinutes)), TimestampType.INSTANCE).list();

        return tripList;
    }
    
    @Override
    public BigDecimal getSumFeeAmountForApprovedAndInFlightTripsAndDateSlotRange(long accountId, int dateSlotInPastHours, int dateSlotInFutureHours) {

    	StringBuilder query = new StringBuilder();
    	query.append("SELECT SUM(amount_fee) FROM trips WHERE account_id = ? AND (status = ? OR status = ?) AND date_slot_approved >= ? AND date_slot_approved <= ?");
    	
    	List<Object> queryParameters = new ArrayList<>();
    	queryParameters.add(accountId);
    	queryParameters.add(ServerConstants.TRIP_STATUS_APPROVED);
    	queryParameters.add(ServerConstants.TRIP_STATUS_IN_FLIGHT);
    	queryParameters.add(new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(dateSlotInPastHours)));
    	queryParameters.add(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(dateSlotInFutureHours)));
    	
    	BigDecimal amountHold = jdbcTemplate.queryForObject(query.toString(), queryParameters.toArray(), BigDecimal.class);
    	
        return amountHold == null ? BigDecimal.ZERO : amountHold;
    }
}
