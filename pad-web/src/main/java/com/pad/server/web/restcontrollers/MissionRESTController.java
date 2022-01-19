package com.pad.server.web.restcontrollers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.Driver;
import com.pad.server.base.entities.Mission;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.entities.Vehicle;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.MissionJson;
import com.pad.server.base.jsonentities.api.TripJson;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.driver.DriverService;
import com.pad.server.base.services.mission.MissionService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.vehicle.VehicleService;

@RestController
@RequestMapping("/mission")
public class MissionRESTController {

    @Autowired
    private AccountService  accountService;

    @Autowired
    private DriverService   driverService;

    @Autowired
    private MissionService  missionService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private VehicleService  vehicleService;

    @RequestMapping(value = "/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getMissionCount(HttpServletResponse response, @RequestBody MissionJson missionJson) throws PADException, Exception {

        Account account = null;
        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (loggedOperator.getRoleId() == ServerConstants.OPERATOR_ROLE_TRANSPORTER) {

            account = accountService.getAccountById(loggedOperator.getAccountId());
            if (account == null)
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account us null");
        }

        Date dateMissionStart = null;
        Date dateMissionEnd = null;

        if (!StringUtils.isBlank(missionJson.getDateMissionStartString())) {
            try {
                dateMissionStart = ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyy, missionJson.getDateMissionStartString());
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
            }

            Calendar calendarDayToday = Calendar.getInstance();
            calendarDayToday.set(Calendar.HOUR_OF_DAY, 0);
            calendarDayToday.set(Calendar.MINUTE, 0);
            calendarDayToday.set(Calendar.SECOND, 0);
            calendarDayToday.set(Calendar.MILLISECOND, 0);

            if (dateMissionStart.before(calendarDayToday.getTime()))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");
        }

        if (!StringUtils.isBlank(missionJson.getDateMissionEndString())) {
            try {
                dateMissionEnd = ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyy, missionJson.getDateMissionEndString());
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#3");
            }

            if (dateMissionEnd.before(dateMissionStart))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#4");
        }

        long count = missionService.getMissionCount(account, missionJson, dateMissionStart, dateMissionEnd);

        List<Long> res = new ArrayList<>();
        res.add(count);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(res);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;

    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getMissionList(HttpServletResponse response, @RequestBody MissionJson missionJson) throws PADException, Exception {

        Account account = null;
        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (loggedOperator.getRoleId() == ServerConstants.OPERATOR_ROLE_TRANSPORTER) {

            account = accountService.getAccountById(loggedOperator.getAccountId());
            if (account == null)
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");
        }

        Date dateMissionStart = null;
        Date dateMissionEnd = null;

        if (!StringUtils.isBlank(missionJson.getDateMissionStartString())) {
            try {
                dateMissionStart = ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyy, missionJson.getDateMissionStartString());
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
            }

            Calendar calendarDayToday = Calendar.getInstance();
            calendarDayToday.set(Calendar.HOUR_OF_DAY, 0);
            calendarDayToday.set(Calendar.MINUTE, 0);
            calendarDayToday.set(Calendar.SECOND, 0);
            calendarDayToday.set(Calendar.MILLISECOND, 0);

            if (dateMissionStart.before(calendarDayToday.getTime()))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");
        }

        if (!StringUtils.isBlank(missionJson.getDateMissionEndString())) {
            try {
                dateMissionEnd = ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyy, missionJson.getDateMissionEndString());
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#3");
            }

            if (dateMissionEnd.before(dateMissionStart))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#4");
        }

        List<MissionJson> missions = missionService.getMissionList(account, missionJson, dateMissionStart, dateMissionEnd);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(missions);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/trip/get", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getMissionTrip(HttpServletResponse response, @RequestBody TripJson tripJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();
        Driver driver = null;
        Vehicle vehicle = null;
        List<TripJson> missionTrips = new ArrayList<>();

        if (StringUtils.isBlank(tripJson.getMissionCode()))
            throw new PADException(ServerResponseConstants.MISSING_MISSION_CODE_CODE, ServerResponseConstants.MISSING_MISSION_CODE_TEXT, "");

        Mission mission = missionService.getMissionByCode(tripJson.getMissionCode());
        if (mission == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "");

        if (mission.getTripList() != null && mission.getTripList().size() > 0) {
            for (Trip trip : mission.getTripList()) {

                vehicle = vehicleService.getVehicleById(trip.getVehicleId());
                driver = driverService.getDriverById(trip.getDriverId());

                if (vehicle != null && driver != null) {

                    tripJson = new TripJson();
                    tripJson.setCode(trip.getCode());
                    tripJson.setVehicleCode(vehicle.getCode());
                    tripJson.setDriverCode(driver.getCode());
                    tripJson.setVehicleRegistration(vehicle.getVehicleRegistration());
                    tripJson.setDriverName(driver.getFirstName() + " " + driver.getLastName());
                    tripJson.setStatus(trip.getStatus());
                    tripJson.setFeePaid(trip.isFeePaid());
                    tripJson.setAdHoc((trip != null && trip.getType() == ServerConstants.TRIP_TYPE_ADHOC) ? true : false);

                    try {
                        tripJson.setDateSlotString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, trip.getDateSlotRequested()));
                    } catch (ParseException e) {
                        tripJson.setDateSlotString("");
                    }

                    tripJson.setPortOperatorId(ServerConstants.DEFAULT_INT);
                    tripJson.setReferenceNumber("");
                    tripJson.setAccountName("");
                    tripJson.setCompanyName("");
                    tripJson.setCurrency("");
                    tripJson.setAccountBalance(null);
                    tripJson.setDriverMobile(trip.getDriverMsisdn());
                    tripJson.setAccount(null);
                    tripJson.setTripFeeAmount(null);
                    tripJson.setDateMissionStartString("");
                    tripJson.setDateMissionEndString("");

                    missionTrips.add(tripJson);
                } else {

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

                    tripJson.setPortOperatorId(ServerConstants.DEFAULT_INT);
                    tripJson.setReferenceNumber("");
                    tripJson.setAccountName("");
                    tripJson.setCompanyName("");
                    tripJson.setCurrency("");
                    tripJson.setAccountBalance(null);
                    tripJson.setDriverMobile(trip.getDriverMsisdn());
                    tripJson.setAccount(null);
                    tripJson.setTripFeeAmount(null);
                    tripJson.setDateMissionStartString("");
                    tripJson.setDateMissionEndString("");

                    missionTrips.add(tripJson);
                }
            }
        }

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(missionTrips);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    // used by port operator and office to create missions
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity createMission(HttpServletResponse response, @RequestBody MissionJson missionJson) throws PADException, PADValidationException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();
        String dateFormat = "";

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (missionJson.getPortOperatorId() == null)
            throw new PADException(ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_CODE, ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_TEXT, "#1");

        if ((missionJson.getPortOperatorId() == ServerConstants.PORT_OPERATOR_TM_NORTH || missionJson.getPortOperatorId() == ServerConstants.PORT_OPERATOR_TM_NORTH)
            && StringUtils.isBlank(missionJson.getIndependentPortOperatorCode()))
            throw new PADException(ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_CODE, ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_TEXT, "#2");

        if (missionJson.getGateId() == null)
            throw new PADException(ServerResponseConstants.MISSING_GATE_ID_CODE, ServerResponseConstants.MISSING_GATE_ID_TEXT, "");

        if (missionJson.getTransactionType() == null)
            throw new PADException(ServerResponseConstants.MISSING_TRANSACTION_TYPE_CODE, ServerResponseConstants.MISSING_TRANSACTION_TYPE_TEXT, "");

        if (StringUtils.isBlank(missionJson.getReferenceNumber()))
            throw new PADException(ServerResponseConstants.MISSING_REFERENCE_NUMBER_CODE, ServerResponseConstants.MISSING_REFERENCE_NUMBER_TEXT, "");

        if (missionJson.getReferenceNumber().length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_16)
            throw new PADException(ServerResponseConstants.INVALID_REFERENCE_NUMBER_CODE, ServerResponseConstants.INVALID_REFERENCE_NUMBER_TEXT, "#1");

        if (!missionJson.getReferenceNumber().matches(ServerConstants.REGEX_ALPHA_NUMERIC))
            throw new PADException(ServerResponseConstants.INVALID_REFERENCE_NUMBER_CODE, ServerResponseConstants.INVALID_REFERENCE_NUMBER_TEXT, "#2");

        if (missionJson.getTransporterComments() == null)
            throw new PADException(ServerResponseConstants.MISSING_COMMENTS_FIELD_CODE, ServerResponseConstants.MISSING_COMMENTS_FIELD_TEXT, "");

        if (StringUtils.isBlank(missionJson.getDateMissionStartString()))
            throw new PADException(ServerResponseConstants.MISSING_DATE_MISSION_START_CODE, ServerResponseConstants.MISSING_DATE_MISSION_START_TEXT, "");

        if (StringUtils.isBlank(missionJson.getDateMissionEndString()))
            throw new PADException(ServerResponseConstants.MISSING_DATE_MISSION_END_CODE, ServerResponseConstants.MISSING_DATE_MISSION_END_TEXT, "");

        if (missionJson.getAccountCodes() == null || missionJson.getAccountCodes().length <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.MISSING_ACCOUNT_CODE_CODE, ServerResponseConstants.MISSING_ACCOUNT_CODE_TEXT, "");

        if (missionJson.getTransactionType() == ServerConstants.TRANSACTION_TYPE_DROP_OFF_EMPTY_NIGHT
            || missionJson.getTransactionType() == ServerConstants.TRANSACTION_TYPE_DROP_OFF_EMPTY_TRIANGLE) {
            dateFormat = ServerConstants.dateFormatDisplayddMMyyyyHHmm;
        } else {
            dateFormat = ServerConstants.dateFormatddMMyyyy;
        }

        try {
            missionJson.setDateMissionStart(ServerUtil.parseDate(dateFormat, missionJson.getDateMissionStartString()));
        } catch (ParseException pe) {
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
        }

        Calendar calendarDayToday = Calendar.getInstance();
        calendarDayToday.set(Calendar.HOUR_OF_DAY, 0);
        calendarDayToday.set(Calendar.MINUTE, 0);
        calendarDayToday.set(Calendar.SECOND, 0);
        calendarDayToday.set(Calendar.MILLISECOND, 0);

        if (missionJson.getDateMissionStart().before(calendarDayToday.getTime()))
            throw new PADValidationException(ServerResponseConstants.MISSION_DATE_IN_PAST_CODE, ServerResponseConstants.MISSION_DATE_IN_PAST_TEXT, "");

        try {
            missionJson.setDateMissionEnd(ServerUtil.parseDate(dateFormat, missionJson.getDateMissionEndString()));
        } catch (ParseException pe) {
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");
        }

        if (missionJson.getDateMissionEnd().before(missionJson.getDateMissionStart()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#3");

        if (dateFormat.equals(ServerConstants.dateFormatddMMyyyy)) {

            Calendar calendarDateMissionEnd = Calendar.getInstance();
            calendarDateMissionEnd.setTime(missionJson.getDateMissionEnd());
            calendarDateMissionEnd.add(Calendar.DAY_OF_YEAR, 1);
            calendarDateMissionEnd.add(Calendar.SECOND, -1);

            missionJson.setDateMissionEnd(calendarDateMissionEnd.getTime());
        }

        missionService.createMission(missionJson, loggedOperator.getId(), false);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    // currently not in use, this can be used in case we decide to add dropdown of ref numbers for transporter to select from when creating trips for TVS
    @RequestMapping(value = "/get/referencenumbers", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getMissionReferenceNumbers(HttpServletResponse response, @RequestBody MissionJson missionJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (missionJson.getPortOperatorId() == null)
            throw new PADException(ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_CODE, ServerResponseConstants.MISSING_PORT_OPERATOR_TYPE_TEXT, "");

        List<String> referenceNumbers = missionService.getMissionReferenceNumbersByAccountIdAndPortOperator(loggedOperator.getAccountId(), missionJson.getPortOperatorId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(referenceNumbers);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    // used by port operator and office to cancel missions
    @RequestMapping(value = "/cancel", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity cancelMission(HttpServletResponse response, @RequestBody MissionJson missionJson) throws PADException, PADValidationException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(missionJson.getCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "Mission code is missing");

        missionService.cancelMission(missionJson, loggedOperator.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

}
