package com.pad.server.web.restcontrollers;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.AnprLog;
import com.pad.server.base.entities.Mission;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.entities.Parking;
import com.pad.server.base.entities.PortOperator;
import com.pad.server.base.entities.PortOperatorGate;
import com.pad.server.base.entities.PortOperatorTransactionType;
import com.pad.server.base.entities.Session;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.entities.Vehicle;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.PortAccessWhitelistJson;
import com.pad.server.base.jsonentities.api.TripBookingTimeJson;
import com.pad.server.base.jsonentities.api.TripJson;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.anpr.AnprBaseService;
import com.pad.server.base.services.mission.MissionService;
import com.pad.server.base.services.onlinepayment.OnlinePaymentService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.parking.ParkingService;
import com.pad.server.base.services.portaccess.PortAccessService;
import com.pad.server.base.services.session.SessionService;
import com.pad.server.base.services.statement.StatementService;
import com.pad.server.base.services.system.SystemService;
import com.pad.server.base.services.trip.TripService;
import com.pad.server.base.services.vehicle.VehicleService;

@RestController
@RequestMapping("/trip")
public class TripRESTController {

    private static final Logger  logger = Logger.getLogger(TripRESTController.class);

    @Autowired
    private AccountService       accountService;

    @Autowired
    private ActivityLogService   activityLogService;

    @Autowired
    private AnprBaseService      anprBaseService;

    @Autowired
    private MissionService       missionService;

    @Autowired
    private OnlinePaymentService onlinePaymentService;

    @Autowired
    private OperatorService      operatorService;

    @Autowired
    private ParkingService       parkingService;

    @Autowired
    private PortAccessService    portAccessService;

    @Autowired
    private SessionService       sessionService;

    @Autowired
    private StatementService     statementService;

    @Autowired
    private SystemService        systemService;

    @Autowired
    private TripService          tripService;

    @Autowired
    private VehicleService       vehicleService;

    @RequestMapping(value = "/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getTripCount(HttpServletResponse response, @RequestBody TripJson tripJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Account account = null;
        long accountId = ServerConstants.DEFAULT_LONG;

        if (StringUtils.isNotBlank(tripJson.getAccountCode())) {
            account = accountService.getAccountByCode(tripJson.getAccountCode());

        } else {
            account = accountService.getAccountById(loggedOperator.getAccountId());
        }

        // if account exits then load the trips for that transporter, otherwise load all trips on system for port & office operator
        if (account != null) {
            accountId = account.getId();
        }

        Date dateSlotRequestedFrom = null;
        Date dateSlotRequestedTo = null;
        Date dateSlotApprovedFrom = null;
        Date dateSlotApprovedTo = null;

        String timeSlotRequestedFrom = "00:00";
        String timeSlotRequestedTo = "00:00";
        String timeSlotApprovedFrom = "00:00";
        String timeSlotApprovedTo = "00:00";

        if (!StringUtils.isBlank(tripJson.getTimeSlotRequestedFromString())) {
            timeSlotRequestedFrom = tripJson.getTimeSlotRequestedFromString();
        }

        if (!StringUtils.isBlank(tripJson.getTimeSlotRequestedToString())) {
            timeSlotRequestedTo = tripJson.getTimeSlotRequestedToString();
        }

        if (!StringUtils.isBlank(tripJson.getTimeSlotApprovedFromString())) {
            timeSlotApprovedFrom = tripJson.getTimeSlotApprovedFromString();
        }

        if (!StringUtils.isBlank(tripJson.getTimeSlotApprovedToString())) {
            timeSlotApprovedTo = tripJson.getTimeSlotApprovedToString();
        }

        if (!StringUtils.isBlank(tripJson.getDateSlotRequestedFromString())) {
            try {
                dateSlotRequestedFrom = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm,
                    tripJson.getDateSlotRequestedFromString() + " " + timeSlotRequestedFrom);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
            }
        }

        if (!StringUtils.isBlank(tripJson.getDateSlotRequestedToString())) {
            try {
                dateSlotRequestedTo = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, tripJson.getDateSlotRequestedToString() + " " + timeSlotRequestedTo);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");
            }
        }

        if (!StringUtils.isBlank(tripJson.getDateSlotApprovedFromString())) {
            try {
                dateSlotApprovedFrom = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, tripJson.getDateSlotApprovedFromString() + " " + timeSlotApprovedFrom);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
            }
        }

        if (!StringUtils.isBlank(tripJson.getDateSlotApprovedToString())) {
            try {
                dateSlotApprovedTo = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, tripJson.getDateSlotApprovedToString() + " " + timeSlotApprovedTo);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");
            }
        }

        long count = tripService.getTripCount(tripJson, accountId, dateSlotRequestedFrom, dateSlotRequestedTo, dateSlotApprovedFrom, dateSlotApprovedTo);

        List<Long> res = new ArrayList<>();
        res.add(count);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(res);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;

    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getTripList(HttpServletResponse response, @RequestBody TripJson tripJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Account account = null;
        long accountId = ServerConstants.DEFAULT_LONG;

        if (StringUtils.isNotBlank(tripJson.getAccountCode())) {
            account = accountService.getAccountByCode(tripJson.getAccountCode());

        } else {
            account = accountService.getAccountById(loggedOperator.getAccountId());
        }

        // if account exits then load the trips for that transporter, otherwise load all trips on system for port & office operator
        if (account != null) {
            accountId = account.getId();
        }

        Date dateSlotRequestedFrom = null;
        Date dateSlotRequestedTo = null;
        Date dateSlotApprovedFrom = null;
        Date dateSlotApprovedTo = null;

        String timeSlotRequestedFrom = "00:00";
        String timeSlotRequestedTo = "00:00";
        String timeSlotApprovedFrom = "00:00";
        String timeSlotApprovedTo = "00:00";

        if (!StringUtils.isBlank(tripJson.getTimeSlotRequestedFromString())) {
            timeSlotRequestedFrom = tripJson.getTimeSlotRequestedFromString();
        }

        if (!StringUtils.isBlank(tripJson.getTimeSlotRequestedToString())) {
            timeSlotRequestedTo = tripJson.getTimeSlotRequestedToString();
        }

        if (!StringUtils.isBlank(tripJson.getTimeSlotApprovedFromString())) {
            timeSlotApprovedFrom = tripJson.getTimeSlotApprovedFromString();
        }

        if (!StringUtils.isBlank(tripJson.getTimeSlotApprovedToString())) {
            timeSlotApprovedTo = tripJson.getTimeSlotApprovedToString();
        }

        if (!StringUtils.isBlank(tripJson.getDateSlotRequestedFromString())) {
            try {
                dateSlotRequestedFrom = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm,
                    tripJson.getDateSlotRequestedFromString() + " " + timeSlotRequestedFrom);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
            }
        }

        if (!StringUtils.isBlank(tripJson.getDateSlotRequestedToString())) {
            try {
                dateSlotRequestedTo = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, tripJson.getDateSlotRequestedToString() + " " + timeSlotRequestedTo);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");
            }
        }

        if (!StringUtils.isBlank(tripJson.getDateSlotApprovedFromString())) {
            try {
                dateSlotApprovedFrom = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, tripJson.getDateSlotApprovedFromString() + " " + timeSlotApprovedFrom);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
            }
        }

        if (!StringUtils.isBlank(tripJson.getDateSlotApprovedToString())) {
            try {
                dateSlotApprovedTo = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, tripJson.getDateSlotApprovedToString() + " " + timeSlotApprovedTo);
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");
            }
        }

        List<TripJson> trips = tripService.getTripList(tripJson, accountId, dateSlotRequestedFrom, dateSlotRequestedTo, dateSlotApprovedFrom, dateSlotApprovedTo);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(trips);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity addTrip(HttpServletResponse response, @RequestBody TripJson tripJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(tripJson.getReferenceNumber()))
            throw new PADException(ServerResponseConstants.MISSING_REFERENCE_NUMBER_CODE, ServerResponseConstants.MISSING_REFERENCE_NUMBER_TEXT, "");

        if (tripJson.getTransactionType() == null)
            throw new PADException(ServerResponseConstants.MISSING_TRANSACTION_TYPE_CODE, ServerResponseConstants.MISSING_TRANSACTION_TYPE_TEXT, "");

        if (tripJson.getPortOperatorId() == null)
            throw new PADException(ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_CODE, ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_TEXT, "");

        if (StringUtils.isBlank(tripJson.getVehicleCode()))
            throw new PADException(ServerResponseConstants.MISSING_VEHICLE_CODE_CODE, ServerResponseConstants.MISSING_VEHICLE_CODE_TEXT, "");

        if (StringUtils.isBlank(tripJson.getMissionCode()))
            throw new PADException(ServerResponseConstants.MISSING_MISSION_CODE_CODE, ServerResponseConstants.MISSING_MISSION_CODE_TEXT, "");

        if (StringUtils.isBlank(tripJson.getDriverCode()))
            throw new PADException(ServerResponseConstants.MISSING_DRIVER_CODE_CODE, ServerResponseConstants.MISSING_DRIVER_CODE_TEXT, "");

        if (StringUtils.isBlank(tripJson.getDateSlotString()))
            throw new PADException(ServerResponseConstants.MISSING_DATE_SLOT_CODE, ServerResponseConstants.MISSING_DATE_SLOT_TEXT, "");

        if (StringUtils.isBlank(tripJson.getTimeSlotString()))
            throw new PADException(ServerResponseConstants.MISSING_TIME_SLOT_CODE, ServerResponseConstants.MISSING_TIME_SLOT_TEXT, "");

        if (StringUtils.isBlank(tripJson.getDateMissionEndString()))
            throw new PADException(ServerResponseConstants.MISSING_DATE_MISSION_END_CODE, ServerResponseConstants.MISSING_DATE_MISSION_END_TEXT, "");

        String tripCode = "";

        try {
            tripCode = tripService.addTrip(tripJson, loggedOperator.getAccountId(), loggedOperator.getId());

        } catch (PADValidationException padve) {

            if (padve.getResponseCode() == ServerResponseConstants.TRIP_FOR_VEHICLE_ALREADY_BOOKED_CODE) {

                Trip existingTrip = tripService.getTripByCode(padve.getResponseSource());
                Account account = accountService.getAccountById(loggedOperator.getAccountId());

                String responseContent = "";

                String rangeReservedFrom = ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm,
                    new Date((existingTrip.getDateSlotApproved() == null ? existingTrip.getDateSlotRequested().getTime() : existingTrip.getDateSlotApproved().getTime())
                        - TimeUnit.HOURS.toMillis(systemService.getSystemParameter().getBookingCheckDateSlotApprovedInAdvanceHours())));

                String rangeReservedTo = ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm,
                    new Date((existingTrip.getDateSlotApproved() == null ? existingTrip.getDateSlotRequested().getTime() : existingTrip.getDateSlotApproved().getTime())
                        + TimeUnit.HOURS.toMillis(systemService.getSystemParameter().getBookingCheckDateSlotApprovedAfterwardsHours())));

                if (existingTrip.getAccountId() == account.getId()) {

                    if (account.getLanguageId() == ServerConstants.LANGUAGE_FR_ID) {
                        responseContent = "Ce véhicule est déjà réservé pour " + existingTrip.getReferenceNumber() + " au "
                            + ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm,
                                existingTrip.getDateSlotApproved() == null ? existingTrip.getDateSlotRequested() : existingTrip.getDateSlotApproved())
                            + ". La plage horaire suivante a déjà été réservée [" + rangeReservedFrom + " - " + rangeReservedTo + "]. Veuillez sélectionner un autre emplacement.";

                    } else {
                        responseContent = "This vehicle is already booked for " + existingTrip.getReferenceNumber() + " at "
                            + ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm,
                                existingTrip.getDateSlotApproved() == null ? existingTrip.getDateSlotRequested() : existingTrip.getDateSlotApproved())
                            + ". The following slot time range has already been reserved [" + rangeReservedFrom + " - " + rangeReservedTo + "]. Please select a different slot.";
                    }

                } else {
                    if (account.getLanguageId() == ServerConstants.LANGUAGE_FR_ID) {
                        responseContent = "Ce véhicule est déjà réservé par une autre société de transport à l'adresse "
                            + ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm,
                                existingTrip.getDateSlotApproved() == null ? existingTrip.getDateSlotRequested() : existingTrip.getDateSlotApproved())
                            + ". La plage horaire suivante a déjà été réservée [" + rangeReservedFrom + " - " + rangeReservedTo + "]. Veuillez sélectionner un autre emplacement.";

                    } else {
                        responseContent = "This vehicle is already booked by another transporter company at "
                            + ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm,
                                existingTrip.getDateSlotApproved() == null ? existingTrip.getDateSlotRequested() : existingTrip.getDateSlotApproved())
                            + ". The following slot time range has already been reserved [" + rangeReservedFrom + " - " + rangeReservedTo + "]. Please select a different slot.";
                    }
                }

                apiResponse.setResponseCode(ServerResponseConstants.TRIP_FOR_VEHICLE_ALREADY_BOOKED_CODE);
                apiResponse.setResponseText(ServerResponseConstants.TRIP_FOR_VEHICLE_ALREADY_BOOKED_TEXT);
                apiResponse.setData(responseContent);

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                return apiResponse;

            } else
                throw padve;

        }

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setData(tripCode);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity cancelTrip(HttpServletResponse response, @RequestBody TripJson tripJson) throws PADException, PADValidationException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(tripJson.getCode()))
            throw new PADException(ServerResponseConstants.MISSING_TRIP_CODE_CODE, ServerResponseConstants.MISSING_TRIP_CODE_TEXT, "");

        tripService.cancelTrip(tripJson.getCode(), loggedOperator);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/cancel/adhoc", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity cancelAdHocTrip(HttpServletResponse response, @RequestBody TripJson tripJson) throws PADException, PADValidationException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(tripJson.getCode()))
            throw new PADException(ServerResponseConstants.MISSING_TRIP_CODE_CODE, ServerResponseConstants.MISSING_TRIP_CODE_TEXT, "");

        tripService.cancelAdHocTrip(tripJson.getCode(), loggedOperator.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    // used by transporter when accepting a DPW trip created by the DPW API
    @RequestMapping(value = "/approve", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity approveTrip(HttpServletResponse response, @RequestBody TripJson tripJson) throws PADException, PADValidationException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(tripJson.getCode()))
            throw new PADException(ServerResponseConstants.MISSING_TRIP_CODE_CODE, ServerResponseConstants.MISSING_TRIP_CODE_TEXT, "");

        if (StringUtils.isBlank(tripJson.getDriverCode()))
            throw new PADException(ServerResponseConstants.MISSING_DRIVER_CODE_CODE, ServerResponseConstants.MISSING_DRIVER_CODE_TEXT, "");

        tripService.approveTrip(tripJson.getCode(), tripJson.getDriverCode(), loggedOperator.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/reject", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity rejectTrip(HttpServletResponse response, @RequestBody TripJson tripJson) throws PADException, PADValidationException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(tripJson.getCode()))
            throw new PADException(ServerResponseConstants.MISSING_TRIP_CODE_CODE, ServerResponseConstants.MISSING_TRIP_CODE_TEXT, "");

        tripService.rejectTrip(tripJson.getCode(), loggedOperator.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getTrip(HttpServletResponse response, @RequestBody TripJson tripJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();
        List<Trip> tripList = null;
        List<TripJson> tripJsonList = new ArrayList<>();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        Session kioskSession = null;

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        // in case kiosk operator is logged in, get the kiosk session
        if (loggedOperator.getRoleId() == ServerConstants.OPERATOR_ROLE_PARKING_KIOSK_OPERATOR) {
            kioskSession = sessionService.getLastSessionByKioskOperatorId(loggedOperator.getId());
        }

        int kioskSessionType = kioskSession != null ? kioskSession.getType() : ServerConstants.DEFAULT_INT;

        if (StringUtils.isBlank(tripJson.getVehicleRegistration())) {

            if (tripJson.getPortOperatorId() == null)
                throw new PADException(ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_CODE, ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_TEXT, "");

            if (StringUtils.isBlank(tripJson.getReferenceNumber()))
                throw new PADValidationException(ServerResponseConstants.MISSING_REFERENCE_NUMBER_CODE, ServerResponseConstants.MISSING_REFERENCE_NUMBER_TEXT, "");

        } else if (StringUtils.isNotBlank(tripJson.getVehicleRegistration()) && tripJson.getTransactionType() == null
            && (tripJson.getPortOperatorId() == null || StringUtils.isBlank(tripJson.getReferenceNumber()))) {
            // do first search based on vehicle reg

            String vehicleRegNumber = ServerUtil.formatVehicleRegNumber(tripJson.getVehicleRegistration());

            activityLogService.saveActivityLogTrip(ServerConstants.ACTIVITY_LOG_TRIP_SEARCH, loggedOperator.getId(), vehicleRegNumber, ServerConstants.DEFAULT_LONG,
                ServerConstants.DEFAULT_INT, ServerConstants.DEFAULT_STRING);

            // check if the vehicle has already entered the parking and is currently in the car park
            Parking parking = parkingService.getEnteredParkingByVehicleRegistration(vehicleRegNumber);

            if (parking != null) {

                if (parking.getType() == ServerConstants.PARKING_TYPE_EXIT_ONLY) {
                    apiResponse.setResponseCode(ServerResponseConstants.VEHICLE_ALREADY_ENTERED_PARKING_AREA_CODE);
                    apiResponse.setResponseText(ServerResponseConstants.VEHICLE_ALREADY_ENTERED_PARKING_AREA_TEXT);

                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                    return apiResponse;
                }

                Trip tripForParkedVehicle = tripService.getTripById(parking.getTripId());

                tripJson = tripService.buildTripJsonEntity(tripJson, tripForParkedVehicle, loggedOperator);
                tripJson.setDateParkingEntryString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, parking.getDateEntry()));
                tripJson.setParkingStatus(parking.getStatus());

                if (loggedOperator.getRoleId() == ServerConstants.OPERATOR_ROLE_PARKING_KIOSK_OPERATOR) {

                    tripJson.setIsVehicleProcessedParkingEntryThroughANPR(
                        kioskSession != null ? anprBaseService.isVehicleProcessedParkingEntryThroughANPRInTheLastHour(tripForParkedVehicle, kioskSession.getLaneId()) : false);
                } else {
                    tripJson.setIsVehicleProcessedParkingEntryThroughANPR(false);
                }

                tripJsonList.add(tripJson);

                apiResponse.setResponseCode(ServerResponseConstants.VEHICLE_ALREADY_PARKED_CODE);
                apiResponse.setResponseText(ServerResponseConstants.VEHICLE_ALREADY_PARKED_TEXT);
                apiResponse.setDataList(tripJsonList);

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                return apiResponse;
            }

            tripList = tripService.getTripsByVehicleRegNumberAndStatus(vehicleRegNumber, ServerConstants.TRIP_STATUS_APPROVED, kioskSessionType);

            /*
             * if (tripList == null || tripList.size() == 0) {
             * tripList = tripService.getTripsByVehicleRegNumberAndStatus(vehicleRegNumber, ServerConstants.TRIP_STATUS_ENTERED_PORT, kioskSessionType);
             * }
             */

            if (tripList == null || tripList.size() == 0) {

                // no approved trip found for vehicle. perform another search to check if theres a trip in transit associated with vehicle
                List<Trip> tripListInTransit = tripService.getTripsByVehicleRegNumberAndStatus(vehicleRegNumber, ServerConstants.TRIP_STATUS_IN_TRANSIT, kioskSessionType);

                if (tripListInTransit == null || tripListInTransit.size() == 0) {

                    List<Trip> tripListExitedParkingPrematurely = tripService.getTripsByVehicleRegNumberAndStatus(vehicleRegNumber,
                        ServerConstants.TRIP_STATUS_EXITED_PARKING_PREMATURELY, kioskSessionType);

                    if (tripListExitedParkingPrematurely != null && tripListExitedParkingPrematurely.size() > 0) {
                        Trip latestTripExitedPrematurely = tripListExitedParkingPrematurely.get(tripListExitedParkingPrematurely.size() - 1);

                        // do not return the trip if vehicle exited prematurely and return to the kiosk after one hour
                        if (latestTripExitedPrematurely.getDateExitParking().compareTo(new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(60))) >= 0) {

                            tripJson = tripService.buildTripJsonEntity(tripJson, latestTripExitedPrematurely, loggedOperator);

                            tripJsonList.add(tripJson);

                            apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
                            apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
                            apiResponse.setDataList(tripJsonList);

                            response.setStatus(HttpServletResponse.SC_OK);

                            return apiResponse;
                        }
                    }

                    // check whether there exists a port entry whitelist for this vehicle registration number
                    PortAccessWhitelistJson portAccessWhitelistJson = portAccessService.getVehicleWhitelisted(vehicleRegNumber);

                    if (portAccessWhitelistJson != null) {
                        // there is a whitelist so system will tell kiosk operator that vehicle is authorised to enter the port
                        apiResponse.setResponseCode(ServerResponseConstants.VEHICLE_REGISTRATION_ALREADY_WHITELISTED_CODE);
                        apiResponse.setResponseText(ServerResponseConstants.VEHICLE_REGISTRATION_ALREADY_WHITELISTED_TEXT);

                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                        return apiResponse;

                    } else
                        throw new PADValidationException(ServerResponseConstants.NO_TRIPS_ASSOCIATED_WITH_VEHICLE_CODE,
                            ServerResponseConstants.NO_TRIPS_ASSOCIATED_WITH_VEHICLE_TEXT, "getTrip#1");

                } else {

                    Trip latestTripInTransit = tripListInTransit.get(tripListInTransit.size() - 1);
                    Date dateToday = new Date();
                    // get the port entry parking permission for this trip
                    AnprLog anprLog = anprBaseService.getAnprLogByRequestTypeAndParkingPermissionIdAndTripId(
                        ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY, latestTripInTransit.getParkingPermissionIdPortEntry(),
                        latestTripInTransit.getId());

                    if (anprLog == null)
                        throw new PADValidationException(ServerResponseConstants.NO_TRIPS_ASSOCIATED_WITH_VEHICLE_CODE,
                            ServerResponseConstants.NO_TRIPS_ASSOCIATED_WITH_VEHICLE_TEXT, "getTrip#2");

                    else if (anprLog != null && (dateToday.compareTo(anprLog.getDateValidFrom()) < 0 || dateToday.compareTo(anprLog.getDateValidTo()) > 0))
                        throw new PADValidationException(ServerResponseConstants.NO_TRIPS_ASSOCIATED_WITH_VEHICLE_CODE,
                            ServerResponseConstants.NO_TRIPS_ASSOCIATED_WITH_VEHICLE_TEXT, "getTrip#3");

                    else {
                        tripJson = tripService.buildTripJsonEntity(tripJson, latestTripInTransit, loggedOperator);
                        tripJson.setIsTripInTransit(true);

                        tripJsonList.add(tripJson);

                        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
                        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
                        apiResponse.setDataList(tripJsonList);

                        response.setStatus(HttpServletResponse.SC_OK);

                        return apiResponse;
                    }
                }

            } else if (tripList.size() == 1) {

                Trip trip = tripList.get(0);

                tripJson = tripService.buildTripJsonEntity(tripJson, trip, loggedOperator);

                tripJsonList.add(tripJson);

                apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
                apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
                apiResponse.setDataList(tripJsonList);

            } else {
                // more than 1 trip found, so ask for port operator, transaction type and reference number number
                if (tripJson.getPortOperatorId() == null || StringUtils.isBlank(tripJson.getReferenceNumber())) {

                    apiResponse.setResponseCode(ServerResponseConstants.MISSING_REFERENCE_NUMBER_CODE);
                    apiResponse.setResponseText(ServerResponseConstants.MISSING_REFERENCE_NUMBER_TEXT);

                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                    return apiResponse;
                }
            }

        } else if (StringUtils.isNotBlank(tripJson.getVehicleRegistration()) && tripJson.getPortOperatorId() != null && tripJson.getTransactionType() != null
            && StringUtils.isBlank(tripJson.getReferenceNumber())) {

            activityLogService.saveActivityLogTrip(ServerConstants.ACTIVITY_LOG_TRIP_SEARCH, loggedOperator.getId(), tripJson.getVehicleRegistration(),
                tripJson.getPortOperatorId(), tripJson.getTransactionType(), ServerConstants.DEFAULT_STRING);

            tripList = tripService.getTripsByVehicleRegNumberAndStatus(ServerUtil.formatVehicleRegNumber(tripJson.getVehicleRegistration()), ServerConstants.TRIP_STATUS_APPROVED,
                kioskSessionType);

            if (tripList == null || tripList.size() == 0)
                // new ad-hoc trip and mission need to be created
                throw new PADValidationException(ServerResponseConstants.CREATE_ADHOC_TRIP_AND_MISSION_CODE, ServerResponseConstants.CREATE_ADHOC_TRIP_AND_MISSION_TEXT, "");

            List<Trip> tripsWithMission = new ArrayList<>();

            for (Trip trip : tripList) {
                if (trip.getPortOperatorId() == tripJson.getPortOperatorId() && trip.getMission().getTransactionType() == tripJson.getTransactionType()) {
                    tripsWithMission.add(trip);
                }
            }

            if (tripsWithMission.size() == 0)
                // new ad-hoc trip and mission need to be created
                throw new PADValidationException(ServerResponseConstants.CREATE_ADHOC_TRIP_AND_MISSION_CODE, ServerResponseConstants.CREATE_ADHOC_TRIP_AND_MISSION_TEXT, "");
            else if (tripsWithMission.size() == 1) {

                Trip trip = tripsWithMission.get(0);

                tripJson = tripService.buildTripJsonEntity(tripJson, trip, loggedOperator);

                tripJsonList.add(tripJson);

                apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
                apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
                apiResponse.setDataList(tripJsonList);

                response.setStatus(HttpServletResponse.SC_OK);

                return apiResponse;

            } else {
                List<String> referenceNumbers = new ArrayList<>();

                for (Trip trip : tripsWithMission) {

                    referenceNumbers.add(trip.getMission().getReferenceNumber());
                }

                apiResponse.setResponseCode(ServerResponseConstants.MISSING_REFERENCE_NUMBER_CODE);
                apiResponse.setResponseText(ServerResponseConstants.MISSING_REFERENCE_NUMBER_TEXT);
                apiResponse.setDataList(referenceNumbers.stream().distinct().collect(Collectors.toList()));

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                return apiResponse;
            }

        } else if (StringUtils.isNotBlank(tripJson.getVehicleRegistration()) && tripJson.getPortOperatorId() != null && tripJson.getTransactionType() != null
            && StringUtils.isNotBlank(tripJson.getReferenceNumber())) {
            // do second search based on port operator, transaction type and reference number

            activityLogService.saveActivityLogTrip(ServerConstants.ACTIVITY_LOG_TRIP_SEARCH, loggedOperator.getId(), tripJson.getVehicleRegistration(),
                tripJson.getPortOperatorId(), tripJson.getTransactionType(), tripJson.getReferenceNumber());

            Mission mission = null;
            if (StringUtils.isNotBlank(tripJson.getReferenceNumber())) {
                mission = missionService.getMissionByPortOperatorIdAndTransactionTypeAndReferenceNumber(tripJson.getPortOperatorId(), tripJson.getTransactionType(),
                    tripJson.getReferenceNumber());
            }

            if (mission == null || mission.getStatus() == ServerConstants.MISSION_STATUS_CANCELLED || mission.getStatus() == ServerConstants.MISSION_STATUS_EXPIRED)
                // new ad-hoc trip and mission need to be created
                throw new PADValidationException(ServerResponseConstants.CREATE_ADHOC_TRIP_AND_MISSION_CODE, ServerResponseConstants.CREATE_ADHOC_TRIP_AND_MISSION_TEXT, "");
            else {
                // in case more than 1 valid trip was found for vehicle reg, search the trips based on reg and then compare based on mission id
                String vehicleRegNumber = ServerUtil.formatVehicleRegNumber(tripJson.getVehicleRegistration());

                tripList = tripService.getTripsByVehicleRegNumberAndStatus(vehicleRegNumber, ServerConstants.TRIP_STATUS_APPROVED, kioskSessionType);

                boolean tripFoundForRegAndReferenceNumber = false;

                Account account = accountService.getAccountById(mission.getAccountId());

                for (Trip trip : tripList) {
                    if (trip.getMission().getId() == mission.getId()) {

                        tripFoundForRegAndReferenceNumber = true;

                        tripJson = tripService.buildTripJsonEntity(tripJson, trip, loggedOperator);

                        tripJsonList.add(tripJson);

                        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
                        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
                        apiResponse.setDataList(tripJsonList);

                        break;
                    }
                }

                if (!tripFoundForRegAndReferenceNumber) {
                    // no trip found for reg and reference number so new ad-hoc trip needs to be created

                    tripJson = new TripJson();
                    tripJson.setGateNumber(systemService.getPortOperatorGateNumberById(mission.getPortOperatorGateId()));
                    tripJson.setAccountName(account == null ? "" : account.getFirstName() + " " + account.getLastName());
                    tripJson.setCompanyName(account == null ? "" : account.getCompanyName());
                    tripJson.setAccountBalance(account == null ? BigDecimal.ZERO : account.getBalanceAmount());
                    tripJson.setCurrency(account == null ? "" : account.getCurrency());
                    tripJson.setDateMissionStartString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, mission.getDateMissionStart()));
                    tripJson.setDateMissionEndString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, mission.getDateMissionEnd()));
                    tripJson.setIsShowReceiptMessage(account == null ? true : (account.getPaymentTermsType() == ServerConstants.ACCOUNT_PAYMENT_TERMS_TYPE_PREPAY));

                    tripJsonList.add(tripJson);

                    apiResponse.setResponseCode(ServerResponseConstants.CREATE_ADHOC_TRIP_CODE);
                    apiResponse.setResponseText(ServerResponseConstants.CREATE_ADHOC_TRIP_TEXT);
                    apiResponse.setDataList(tripJsonList);

                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                    return apiResponse;
                }
            }

        } else
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "");

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/vehicle/registration/list", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getVehicleRegistrationList(HttpServletRequest request, HttpServletResponse response) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Session kioskSession = null;

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (loggedOperator.getRoleId() == ServerConstants.OPERATOR_ROLE_PARKING_KIOSK_OPERATOR) {

            kioskSession = sessionService.getLastSessionByKioskOperatorId(loggedOperator.getId());

            if (kioskSession == null)
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "kioskSession is null");

            if (kioskSession.getStatus() == ServerConstants.SESSION_STATUS_END) {
                // logout the kiosk user because the kiosk session has already been ended from the finance operator
                new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            }
        }

        List<String> vehicleRegistrationList = tripService
            .getVehicleRegistrationListFromTrips(kioskSession != null ? kioskSession.getType() : ServerConstants.SESSION_TYPE_PARKING);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(vehicleRegistrationList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/adhoc/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity createAdhocTrip(HttpServletResponse response, @RequestBody TripJson tripJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();
        List<TripJson> tripJsonList = new ArrayList<>();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(tripJson.getVehicleRegistration()))
            throw new PADException(ServerResponseConstants.MISSING_VEHICLE_REGISTRATION_NUMBER_CODE, ServerResponseConstants.MISSING_VEHICLE_REGISTRATION_NUMBER_TEXT, "");

        if (StringUtils.isBlank(tripJson.getVehicleRegistrationCountryISO()))
            throw new PADException(ServerResponseConstants.MISSING_VEHICLE_REGISTRATION_COUNTRY_CODE, ServerResponseConstants.MISSING_VEHICLE_REGISTRATION_COUNTRY_TEXT, "");

        if (tripJson.getPortOperatorId() == null)
            throw new PADException(ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_CODE, ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_TEXT, "#1");

        if ((tripJson.getPortOperatorId() == ServerConstants.PORT_OPERATOR_TM_NORTH || tripJson.getPortOperatorId() == ServerConstants.PORT_OPERATOR_TM_NORTH)
            && StringUtils.isBlank(tripJson.getIndependentPortOperatorCode()))
            throw new PADException(ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_CODE, ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_TEXT, "#2");

        if (tripJson.getTransactionType() == null)
            throw new PADException(ServerResponseConstants.MISSING_TRANSACTION_TYPE_CODE, ServerResponseConstants.MISSING_TRANSACTION_TYPE_TEXT, "");

        if (StringUtils.isBlank(tripJson.getDriverMobile()))
            throw new PADException(ServerResponseConstants.MISSING_MSISDN_CODE, ServerResponseConstants.MISSING_MSISDN_TEXT, "");

        if (tripJson.getDriverLanguageId() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.MISSING_LANGUAGE_ID_CODE, ServerResponseConstants.MISSING_LANGUAGE_ID_TEXT, "");

        if (StringUtils.isBlank(tripJson.getDateSlotString()))
            throw new PADException(ServerResponseConstants.MISSING_DATE_SLOT_CODE, ServerResponseConstants.MISSING_DATE_SLOT_TEXT, "");

        if (StringUtils.isBlank(tripJson.getTimeSlotString()))
            throw new PADException(ServerResponseConstants.MISSING_TIME_SLOT_CODE, ServerResponseConstants.MISSING_TIME_SLOT_TEXT, "");

        // if (StringUtils.isBlank(tripJson.getCompanyName()))
        // throw new PADException(ServerResponseConstants.MISSING_COMPANY_NAME_CODE, ServerResponseConstants.MISSING_COMPANY_NAME_TEXT, "");

        if (StringUtils.isBlank(tripJson.getGetTripResponseCode()))
            throw new PADException(ServerResponseConstants.MISSING_FIND_TRIP_RESPONSE_CODE_CODE, ServerResponseConstants.MISSING_FIND_TRIP_RESPONSE_CODE_TEXT, "");

        tripJson = tripService.createAdhocTrip(tripJson, loggedOperator);

        tripJsonList.add(tripJson);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(tripJsonList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    // used when the parking operator searches for trip details to determine whether the driver has to pay (proceed to parking / proceed to payment)
    @RequestMapping(value = "/balance/check", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity checkBalanceForTrip(HttpServletResponse response, @RequestBody TripJson tripJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (StringUtils.isBlank(tripJson.getCode()))
            throw new PADException(ServerResponseConstants.MISSING_TRIP_CODE_CODE, ServerResponseConstants.MISSING_TRIP_CODE_TEXT, "");

        Trip trip = tripService.getTripByCode(tripJson.getCode());

        if (trip == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "");

        if (trip.getMission() == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "");

        if (trip.getStatus() != ServerConstants.TRIP_STATUS_APPROVED)
            throw new PADException(ServerResponseConstants.UNEXPECTED_TRIP_STATUS_CODE, ServerResponseConstants.UNEXPECTED_TRIP_STATUS_TEXT, "");

        // check if trip is associated to account
        if (trip.getAccountId() == ServerConstants.DEFAULT_LONG)
            throw new PADValidationException(ServerResponseConstants.NO_ACCOUNT_ASSOCIATED_WITH_TRIP_CODE, ServerResponseConstants.NO_ACCOUNT_ASSOCIATED_WITH_TRIP_TEXT, "");

        else {
            // check if fee is already paid
            if (trip.isFeePaid()) {
                apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
                apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

            } else {
                // check if enough balance in the account
                Account account = accountService.getAccountById(trip.getAccountId());
                if (account == null)
                    throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "");

                BigDecimal tripFeeAmount = trip.getFeeAmount();
                BigDecimal accountAmountHold = account.getAmountHold().abs();
                BigDecimal accountBalanceMinusTripFee = account.getBalanceAmount().subtract(tripFeeAmount);
                BigDecimal accountBalanceMinusTripFeeMinusAmountHold = accountBalanceMinusTripFee.subtract(accountAmountHold);

                // below addition is to handle the condition the calculated balance would exceed the amount overdraft limit not more than by the fee of the trip. If that is the
                // case the system would still allow the deduct from credit option
                accountBalanceMinusTripFeeMinusAmountHold = accountBalanceMinusTripFeeMinusAmountHold.add(tripFeeAmount);

                if (accountBalanceMinusTripFeeMinusAmountHold.compareTo(BigDecimal.ZERO) < 0) {

                    if (accountBalanceMinusTripFeeMinusAmountHold.abs().compareTo(account.getAmountOverdraftLimit()) > 0)
                        throw new PADValidationException(ServerResponseConstants.ACCOUNT_BALANCE_LOW_CODE, ServerResponseConstants.ACCOUNT_BALANCE_LOW_TEXT, "");
                    else {
                        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
                        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
                    }

                } else {
                    apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
                    apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/calc/amount/due", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity calcAmountDueForTrip(HttpServletResponse response, @RequestBody TripJson tripJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();
        BigDecimal accountBalanceMinusTripFee = BigDecimal.ZERO;
        BigDecimal accountAmountHold = BigDecimal.ZERO;
        BigDecimal accountBalanceMinusTripFeeMinusAmountHold = BigDecimal.ZERO;
        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (loggedOperator.getRoleId() != ServerConstants.OPERATOR_ROLE_PARKING_OPERATOR && loggedOperator.getRoleId() != ServerConstants.OPERATOR_ROLE_PARKING_KIOSK_OPERATOR)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "");

        if (StringUtils.isBlank(tripJson.getCode()))
            throw new PADException(ServerResponseConstants.MISSING_TRIP_CODE_CODE, ServerResponseConstants.MISSING_TRIP_CODE_TEXT, "");

        Trip trip = tripService.getTripByCode(tripJson.getCode());

        if (trip == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "trip is null");

        if (trip.getMission() == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "mission is null");

        if (trip.getStatus() != ServerConstants.TRIP_STATUS_APPROVED && trip.getStatus() != ServerConstants.TRIP_STATUS_ENTERED_PARKING
            && trip.getStatus() != ServerConstants.TRIP_STATUS_EXITED_PARKING_PREMATURELY && trip.getStatus() != ServerConstants.TRIP_STATUS_IN_TRANSIT)
            throw new PADException(ServerResponseConstants.UNEXPECTED_TRIP_STATUS_CODE, ServerResponseConstants.UNEXPECTED_TRIP_STATUS_TEXT, "");

        BigDecimal tripFeeAmount = trip.getFeeAmount();

        // check if trip is associated to account. if no account then fee is the single trip fee
        if (trip.getAccountId() == ServerConstants.DEFAULT_LONG) {
            // Non-account ADHOC trip

            if (trip.getStatus() == ServerConstants.TRIP_STATUS_EXITED_PARKING_PREMATURELY && trip.isFeePaid() && !trip.getIsDirectToPort() && trip.getDateExitParking() != null) {

                if (new Date().compareTo(new Date(trip.getDateExitParking().getTime() + TimeUnit.MINUTES.toMillis(60))) > 0) {
                    tripJson.setTripFeeAmount(tripFeeAmount);
                } else {
                    tripJson.setTripFeeAmount(BigDecimal.ZERO);
                }

            } else {
                tripJson.setTripFeeAmount(trip.isFeePaid() ? BigDecimal.ZERO : tripFeeAmount);
            }

        } else {
            Account account = accountService.getAccountById(trip.getAccountId());
            if (account == null)
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");

            accountBalanceMinusTripFee = account.getBalanceAmount().subtract(tripFeeAmount);

            if (trip.getType() == ServerConstants.TRIP_TYPE_ADHOC) {
                // Account ADHOC trip - so no amount hold consideration

                if (accountBalanceMinusTripFee.compareTo(BigDecimal.ZERO) < 0) {

                    if (accountBalanceMinusTripFee.abs().compareTo(account.getAmountOverdraftLimit()) > 0) {
                        tripJson.setTripFeeAmount(tripFeeAmount);

                    } else {
                        tripJson.setTripFeeAmount(BigDecimal.ZERO);
                    }

                } else {
                    tripJson.setTripFeeAmount(BigDecimal.ZERO);
                }

            } else {
                // Account BOOKED trip - consider amount hold

                accountAmountHold = account.getAmountHold().abs();
                accountBalanceMinusTripFeeMinusAmountHold = accountBalanceMinusTripFee.subtract(accountAmountHold);

                // below addition is to handle the condition the calculated balance would exceed the amount overdraft limit not more than by the fee of the trip. If that is the
                // case
                // the system would still allow the deduct from credit option
                accountBalanceMinusTripFeeMinusAmountHold = accountBalanceMinusTripFeeMinusAmountHold.add(tripFeeAmount);

                if (accountBalanceMinusTripFeeMinusAmountHold.compareTo(BigDecimal.ZERO) < 0) {

                    if (accountBalanceMinusTripFeeMinusAmountHold.abs().compareTo(account.getAmountOverdraftLimit()) > 0) {
                        tripJson.setTripFeeAmount(tripFeeAmount);

                    } else {
                        tripJson.setTripFeeAmount(BigDecimal.ZERO);
                    }

                } else {
                    tripJson.setTripFeeAmount(BigDecimal.ZERO);
                }
            }
        }

        tripJson.setTripTaxAmount(ServerUtil.calculateAmountTax(tripJson.getTripFeeAmount(), systemService.getSystemParameter().getTaxPercentage()));

        Map<Integer, Boolean> mobilePaymentOptionsMap = onlinePaymentService.getMobilePaymentOptionsMap();

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setData(tripJson);
        apiResponse.setDataMap(mobilePaymentOptionsMap);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/update/driver/mobile", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updateDriverMobile(HttpServletResponse response, @RequestBody TripJson tripJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (loggedOperator.getRoleId() != ServerConstants.OPERATOR_ROLE_PARKING_OPERATOR)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "");

        if (StringUtils.isBlank(tripJson.getCode()))
            throw new PADException(ServerResponseConstants.MISSING_TRIP_CODE_CODE, ServerResponseConstants.MISSING_TRIP_CODE_TEXT, "");

        if (StringUtils.isBlank(tripJson.getDriverMobile()))
            throw new PADException(ServerResponseConstants.MISSING_MSISDN_CODE, ServerResponseConstants.MISSING_MSISDN_TEXT, "");

        if (tripJson.getDriverLanguageId() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.MISSING_LANGUAGE_ID_CODE, ServerResponseConstants.MISSING_LANGUAGE_ID_TEXT, "");

        Trip trip = tripService.getTripByCode(tripJson.getCode());

        if (trip == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "trip is null");

        if (trip.getStatus() != ServerConstants.TRIP_STATUS_APPROVED)
            throw new PADException(ServerResponseConstants.UNEXPECTED_TRIP_STATUS_CODE, ServerResponseConstants.UNEXPECTED_TRIP_STATUS_TEXT, "");

        String msisdn = ServerUtil.getValidNumber(tripJson.getDriverMobile(), "#updateDriverMobile");

        trip.setDriverMsisdn(msisdn);
        trip.setDriverLanguageId(tripJson.getDriverLanguageId());
        trip.setOperatorId(loggedOperator.getId());
        trip.setDateEdited(new Date());

        tripService.updateTrip(trip);

        activityLogService.saveActivityLogTrip(ServerConstants.ACTIVITY_LOG_TRIP_DRIVER_MOBILE_UPDATE, loggedOperator.getId(), trip.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/update/vehicle/country/iso", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updateVehicleRegCountryISO(HttpServletResponse response, @RequestBody TripJson tripJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (loggedOperator.getRoleId() != ServerConstants.OPERATOR_ROLE_PARKING_OPERATOR)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "");

        if (StringUtils.isBlank(tripJson.getCode()))
            throw new PADException(ServerResponseConstants.MISSING_TRIP_CODE_CODE, ServerResponseConstants.MISSING_TRIP_CODE_TEXT, "");

        if (StringUtils.isBlank(tripJson.getVehicleRegistrationCountryISO()))
            throw new PADException(ServerResponseConstants.MISSING_VEHICLE_REGISTRATION_COUNTRY_CODE, ServerResponseConstants.MISSING_VEHICLE_REGISTRATION_COUNTRY_TEXT, "");

        if (!tripJson.getVehicleRegistrationCountryISO().matches(ServerConstants.REGEX_UNIVERSAL_COUNTRY_CODE))
            throw new PADException(ServerResponseConstants.INVALID_COUNTRY_CODE, ServerResponseConstants.INVALID_COUNTRY_TEXT, "");

        Trip trip = tripService.getTripByCode(tripJson.getCode());

        if (trip == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "trip is null");

        if (trip.getStatus() != ServerConstants.TRIP_STATUS_APPROVED)
            throw new PADException(ServerResponseConstants.UNEXPECTED_TRIP_STATUS_CODE, ServerResponseConstants.UNEXPECTED_TRIP_STATUS_TEXT, "");

        Vehicle vehicle = vehicleService.getVehicleById(trip.getVehicleId());

        if (vehicle != null && !vehicle.getRegistrationCountryISO().equalsIgnoreCase(tripJson.getVehicleRegistrationCountryISO())) {

            vehicle.setRegistrationCountryISO(tripJson.getVehicleRegistrationCountryISO());

            vehicleService.updateVehicle(vehicle);
        }

        trip.setVehicleRegistrationCountryISO(tripJson.getVehicleRegistrationCountryISO());
        trip.setFeeAmount(systemService.getTripFeeAmount(trip.getPortOperatorId(), trip.getMission().getTransactionType(), trip.getVehicleRegistrationCountryISO()));
        trip.setOperatorId(loggedOperator.getId());
        trip.setDateEdited(new Date());

        tripService.updateTrip(trip);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity update(HttpServletResponse response, @RequestBody TripJson tripJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();
        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (tripJson.getActionType() == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "missing actionType");

        if (tripJson.getActionType() != ServerConstants.ACTION_TYPE_APPROVE_TRIP && tripJson.getActionType() != ServerConstants.ACTION_TYPE_DENY_TRIP
            && tripJson.getActionType() != ServerConstants.ACTION_TYPE_UPDATE_TRIP_SLOT_DATETIME)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "invalid actionType");

        tripService.updateTrip(tripJson, loggedOperator.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/validate/referencenumber", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity validateReferenceNumber(HttpServletResponse response, @RequestBody TripJson tripJson) throws PADException, PADValidationException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Date dateToday = new Date();
        List<TripJson> tripJsonList = new ArrayList<>();

        PortOperatorTransactionType portOperatorTransactionTypeEntity = null;

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (loggedOperator.getRoleId() != ServerConstants.OPERATOR_ROLE_TRANSPORTER)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "");

        if (tripJson.getPortOperatorId() == null)
            throw new PADValidationException(ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_CODE, ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_TEXT, "");

        if (StringUtils.isBlank(tripJson.getReferenceNumber()))
            throw new PADValidationException(ServerResponseConstants.MISSING_REFERENCE_NUMBER_CODE, ServerResponseConstants.MISSING_REFERENCE_NUMBER_TEXT, "");

        /*
         * if (tripJson.getPortOperatorId() == ServerConstants.PORT_OPERATOR_DPWORLD) {
         * // search in trips_port_operator table
         * TripsPortOperator tripByPortOperator = tripService.getPortOperatorTripByReferenceNumber(tripJson.getReferenceNumber(), tripJson.getPortOperatorId());
         * if (tripByPortOperator == null)
         * throw new PADValidationException(ServerResponseConstants.TRIP_ID_NOT_FOUND_CODE, ServerResponseConstants.TRIP_ID_NOT_FOUND_TEXT, "");
         * //if (dateToday.before(tripByPortOperator.getDateSlotStart()))
         * // throw new PADValidationException(ServerResponseConstants.TRIP_CREATE_DATE_START_VIOLATION_CODE, ServerResponseConstants.TRIP_CREATE_DATE_START_VIOLATION_TEXT, "");
         * if (dateToday.after(tripByPortOperator.getDateSlotEnd()))
         * throw new PADValidationException(ServerResponseConstants.TRIP_CREATE_DATE_END_VIOLATION_CODE, ServerResponseConstants.TRIP_CREATE_DATE_END_VIOLATION_TEXT, "");
         * // check if that reference number had already been used to book a trip
         * Trip trip = tripService.getTripByReferenceNumber(tripJson.getReferenceNumber());
         * if (trip != null)
         * throw new PADValidationException(ServerResponseConstants.TRIP_ALREADY_BOOKED_CODE, ServerResponseConstants.TRIP_ALREADY_BOOKED_TEXT, "");
         * TripJson tripJsonResult = new TripJson();
         * tripJsonResult.setPortOperatorId(tripJson.getPortOperatorId());
         * tripJsonResult.setTransactionType(tripByPortOperator.getTransactionType());
         * tripJsonResult.setReferenceNumber(tripJson.getReferenceNumber());
         * tripJsonResult.setContainerId(tripByPortOperator.getCtrId());
         * tripJsonResult.setVehicleRegistration(tripByPortOperator.getVehicleRegistration());
         * tripJsonResult.setDateMissionStartString(formatter.format(tripByPortOperator.getDateSlotStart()));
         * tripJsonResult.setDateMissionEndString(formatter.format(tripByPortOperator.getDateSlotEnd()));
         * tripJsonList.add(tripJsonResult);
         * }
         */

        Mission mission = missionService.getActiveMissionByReferenceNumberAndAccountId(tripJson.getReferenceNumber(), loggedOperator.getAccountId(), tripJson.getPortOperatorId());
        if (mission == null)
            throw new PADValidationException(ServerResponseConstants.MISSION_WITH_REFERENCE_NOT_FOUND_CODE, ServerResponseConstants.MISSION_WITH_REFERENCE_NOT_FOUND_TEXT, "");

        if (dateToday.after(mission.getDateMissionEnd()))
            throw new PADValidationException(ServerResponseConstants.MISSION_DATE_EXPIRED_CODE, ServerResponseConstants.MISSION_DATE_EXPIRED_TEXT, "");

        portOperatorTransactionTypeEntity = systemService.getPortOperatorTransactionTypeEntity(mission.getPortOperatorId(), mission.getTransactionType());

        TripJson tripJsonResult = new TripJson();
        tripJsonResult.setPortOperatorId(tripJson.getPortOperatorId());
        tripJsonResult.setTransactionType(mission.getTransactionType());
        tripJsonResult.setReferenceNumber(tripJson.getReferenceNumber());
        tripJsonResult.setContainerId(tripJson.getContainerId());
        tripJsonResult.setVehicleRegistration("");
        tripJsonResult.setDateMissionStartString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, mission.getDateMissionStart()));
        tripJsonResult.setDateMissionEndString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, mission.getDateMissionEnd()));
        tripJsonResult.setMissionCode(mission.getCode());
        tripJsonResult.setIsDirectToPort(portOperatorTransactionTypeEntity.getIsDirectToPort());
        tripJsonResult.setIsAllowMultipleEntries(portOperatorTransactionTypeEntity.getIsAllowMultipleEntries());

        PortOperator portOperator = systemService.getPortOperatorFromMap(tripJsonResult.getPortOperatorId());

        for (PortOperatorTransactionType portOperatorTransactionType : portOperator.getPortOperatorTransactionTypesList()) {

            PortOperatorGate gate = portOperatorTransactionType.getPortOperatorGate();

            if (gate.getId() == mission.getPortOperatorGateId() && portOperatorTransactionType.getTransactionType() == mission.getTransactionType()) {
                tripJsonResult.setGateNumber(" - " + gate.getGateNumber());
                tripJsonResult.setGateNumberShort(" - " + gate.getGateNumberShort());
                break;
            }
        }

        tripJsonList.add(tripJsonResult);

        activityLogService.saveActivityLogTrip(ServerConstants.ACTIVITY_LOG_TRIP_VALIDATE_REFERENCE_NUMBER, loggedOperator.getId(), ServerConstants.DEFAULT_STRING,
            tripJson.getPortOperatorId(), ServerConstants.DEFAULT_INT, tripJson.getReferenceNumber());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(tripJsonList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/get/available/booking/hours", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getAvailableBookingHours(HttpServletResponse response, @RequestBody TripBookingTimeJson tripBookingTimeJson)
        throws PADException, PADValidationException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (tripBookingTimeJson.getPortOperatorId() == null)
            throw new PADException(ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_CODE, ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_TEXT, "");

        if (tripBookingTimeJson.getTransactionType() == null)
            throw new PADException(ServerResponseConstants.MISSING_TRANSACTION_TYPE_CODE, ServerResponseConstants.MISSING_TRANSACTION_TYPE_TEXT, "");

        if (StringUtils.isBlank(tripBookingTimeJson.getDateSlotString()))
            throw new PADException(ServerResponseConstants.MISSING_DATE_SLOT_CODE, ServerResponseConstants.MISSING_DATE_SLOT_TEXT, "");

        Date dateSlot = null;
        try {
            dateSlot = ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyy, tripBookingTimeJson.getDateSlotString());
        } catch (ParseException pe) {
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "getAvailableBookngHours#ParseException");
        }

        List<TripBookingTimeJson> tripBookingTimeJsonList = systemService.getTripBookingHoursAvailability(tripBookingTimeJson.getPortOperatorId(),
            tripBookingTimeJson.getTransactionType(), dateSlot, loggedOperator.getLanguageId(), loggedOperator.getRoleId());

        // if the slot date is todays date & a kiosk operator creates adhoc trip, preselect the slot time to the closest available slot
        if (loggedOperator.getRoleId() == ServerConstants.OPERATOR_ROLE_PARKING_KIOSK_OPERATOR) {

            LocalDate localDateSlot = dateSlot.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localDateToday = LocalDate.now();
            LocalTime localTimeToday = LocalTime.now();

            if (localDateSlot.isEqual(localDateToday)) {
                for (TripBookingTimeJson tripBookingTime : tripBookingTimeJsonList) {

                    if (!tripBookingTime.getIsTimeSlotDisabled() && LocalTime.parse(tripBookingTime.getTimeFormat()).isAfter(localTimeToday)) {

                        apiResponse.setData(tripBookingTime.getTime());
                        break;
                    }
                }
            }
        }

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(tripBookingTimeJsonList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    // used by the virtual kiosk operator
    @RequestMapping(value = "/charge/fee", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity chargeTripFeeToTransporter(HttpServletResponse response, @RequestBody TripJson tripJson) throws PADException, PADValidationException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();
        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (loggedOperator.getRoleId() != ServerConstants.OPERATOR_ROLE_PARKING_KIOSK_OPERATOR)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "");

        if (StringUtils.isBlank(tripJson.getCode()))
            throw new PADException(ServerResponseConstants.MISSING_TRIP_CODE_CODE, ServerResponseConstants.MISSING_TRIP_CODE_TEXT, "");

        Trip trip = tripService.getTripByCode(tripJson.getCode());

        if (trip == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "trip is null");

        if (trip.getAccountId() == ServerConstants.DEFAULT_LONG)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "trip is not associated with transporter account");

        if (trip.getStatus() != ServerConstants.TRIP_STATUS_APPROVED)
            throw new PADValidationException(ServerResponseConstants.UNEXPECTED_TRIP_STATUS_CODE, ServerResponseConstants.UNEXPECTED_TRIP_STATUS_TEXT, "");

        Session session = sessionService.getLastSessionByKioskOperatorId(loggedOperator.getId());
        if (session == null)
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "session is null");

        statementService.chargeParkingFee(trip, loggedOperator.getId());

        if (trip.getType() == ServerConstants.TRIP_TYPE_ADHOC) {
            // create port entry permission
            try {
                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_DIRECT,
                    systemService.getPortOperatorAnprZoneIdById(trip.getPortOperatorGateId()), trip, null, null, new Date());

            } catch (Exception e) {
                logger.error("chargeTripFeeToTransporter#scheduleAnpr###Exception: ", e);
            }
        }

        trip.setLaneSessionId(session.getId());
        trip.setStatus(ServerConstants.TRIP_STATUS_IN_TRANSIT);

        tripService.updateTrip(trip);

        session.setAccountDeductTransactionCount(session.getAccountDeductTransactionCount() + 1);
        session.setAccountDeductTransactionTotalAmount(session.getAccountDeductTransactionTotalAmount().add(trip.getFeeAmount()));

        sessionService.updateSession(session);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }
}
