package com.pad.server.web.restcontrollers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.ActivityLog;
import com.pad.server.base.entities.Driver;
import com.pad.server.base.entities.DriverAssociation;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.entities.PortOperator;
import com.pad.server.base.entities.PortOperatorAlert;
import com.pad.server.base.entities.PortOperatorGate;
import com.pad.server.base.entities.PortOperatorTransactionType;
import com.pad.server.base.entities.SystemParameter;
import com.pad.server.base.entities.Vehicle;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.BookingSlotLimitJson;
import com.pad.server.base.jsonentities.api.DayOfWeekJson;
import com.pad.server.base.jsonentities.api.DriverJson;
import com.pad.server.base.jsonentities.api.PortOperatorAlertJson;
import com.pad.server.base.jsonentities.api.PortOperatorGateJson;
import com.pad.server.base.jsonentities.api.PortOperatorJson;
import com.pad.server.base.jsonentities.api.PortOperatorTransactionTypeJson;
import com.pad.server.base.jsonentities.api.SystemParamJson;
import com.pad.server.base.jsonentities.api.VehicleJson;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.driver.DriverService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.system.SystemService;
import com.pad.server.base.services.trip.TripService;
import com.pad.server.web.services.padanpr.PadAnprService;

@RestController
@RequestMapping("/system")
public class SystemRESTController {

    @Autowired
    private AccountService     accountService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private DriverService      driverService;

    @Autowired
    private PadAnprService     padAnprService;

    @Autowired
    private OperatorService    operatorService;

    @Autowired
    private SystemService      systemService;

    @Autowired
    private TripService        tripService;

    @RequestMapping(value = "/list/accountdriversandvehicles", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getAccountDriverAndVehicleList(HttpServletResponse response) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();
        VehicleJson vehicleJson = new VehicleJson();
        DriverJson driverJson = new DriverJson();

        List<VehicleJson> vehicleList = new ArrayList<>();
        List<DriverJson> driverList = new ArrayList<>();
        List<Object> vehicleAndDriverList = new ArrayList<>();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Account account = accountService.getAccountById(loggedOperator.getAccountId());

        if (account == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");

        List<DriverAssociation> drivers = account.getDriverAssociationList();
        Driver driver = null;

        if (drivers != null && drivers.size() > 0) {
            for (DriverAssociation driverAssociation : drivers) {

                if (driverAssociation.getStatus() == ServerConstants.DRIVER_ASSOCIATION_STATUS_APPROVED) {

                    driver = driverService.getDriverById(driverAssociation.getDriverId());

                    driverJson = new DriverJson(driver.getCode(), driver.getFirstName() + " " + driver.getLastName(), driver.getMsisdn());
                    driverList.add(driverJson);
                }
            }
        }

        if (account.getVehicleList() != null && account.getVehicleList().size() > 0) {
            for (Vehicle vehicle : account.getVehicleList()) {
                if (vehicle.getIsActive()) {
                    vehicleJson = new VehicleJson(vehicle.getCode(), vehicle.getVehicleRegistration());
                    vehicleList.add(vehicleJson);
                }
            }
        }

        vehicleAndDriverList.add(vehicleList);
        vehicleAndDriverList.add(driverList);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(vehicleAndDriverList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/save/system/parameter", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity saveSystemParameter(HttpServletResponse response, @RequestBody SystemParamJson systemParamJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        SystemParameter systemParameter = systemService.getSystemParameter();

        if (systemParamJson.getIsBookingLimitCheckEnabled() == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "isBookingLimitCheckEnabled is missing");

        if (systemParamJson.getIsPortEntryFiltering() == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "isPortEntryFiltering is missing");

        if (systemParamJson.getAutoReleaseExitCapacityPercentage() == null || systemParamJson.getAutoReleaseExitCapacityPercentage() < 50)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "autoReleaseExitCapacityPercentage is missing");

        if (StringUtils.isBlank(systemParamJson.getDropOffEmptyNightMissionStartTime()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "dropOffEmptyNightMissionStartTime is missing");

        if (StringUtils.isBlank(systemParamJson.getDropOffEmptyNightMissionEndTime()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "dropOffEmptyNightMissionEndTime is missing");

        if (systemParamJson.getDropOffEmptyNightMissionStartTime().length() != 5)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "dropOffEmptyNightMissionStartTime is invalid");

        if (systemParamJson.getDropOffEmptyNightMissionEndTime().length() != 5)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "dropOffEmptyNightMissionEndTime is invalid");

        if (StringUtils.isBlank(systemParamJson.getDropOffEmptyTriangleMissionStartTime()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "dropOffEmptyTriangleMissionStartTime is missing");

        if (StringUtils.isBlank(systemParamJson.getDropOffEmptyTriangleMissionEndTime()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "dropOffEmptyTriangleMissionEndTime is missing");

        if (systemParamJson.getDropOffEmptyTriangleMissionStartTime().length() != 5)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "dropOffEmptyTriangleMissionStartTime is invalid");

        if (systemParamJson.getDropOffEmptyTriangleMissionEndTime().length() != 5)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "dropOffEmptyTriangleMissionEndTime is invalid");

        int hour = Integer.parseInt(systemParamJson.getDropOffEmptyNightMissionStartTime().substring(0, 2));
        if (hour < 0 || hour > 23)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "dropOffEmptyNightMissionStartTime is invalid");

        hour = Integer.parseInt(systemParamJson.getDropOffEmptyNightMissionEndTime().substring(0, 2));
        if (hour < 0 || hour > 23)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "dropOffEmptyNightMissionEndTime is invalid");

        hour = Integer.parseInt(systemParamJson.getDropOffEmptyTriangleMissionStartTime().substring(0, 2));
        if (hour < 0 || hour > 23)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "dropOffEmptyTriangleMissionStartTime is invalid");

        hour = Integer.parseInt(systemParamJson.getDropOffEmptyTriangleMissionEndTime().substring(0, 2));
        if (hour < 0 || hour > 23)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "dropOffEmptyTriangleMissionEndTime is invalid");

        systemParameter.setIsBookingLimitCheckEnabled(systemParamJson.getIsBookingLimitCheckEnabled());
        systemParameter.setIsPortEntryFiltering(systemParamJson.getIsPortEntryFiltering());
        systemParameter.setAutoReleaseExitCapacityPercentage(systemParamJson.getAutoReleaseExitCapacityPercentage());
        systemParameter.setDropOffEmptyNightMissionStartTime(systemParamJson.getDropOffEmptyNightMissionStartTime());
        systemParameter.setDropOffEmptyNightMissionEndTime(systemParamJson.getDropOffEmptyNightMissionEndTime());
        systemParameter.setDropOffEmptyTriangleMissionStartTime(systemParamJson.getDropOffEmptyTriangleMissionStartTime());
        systemParameter.setDropOffEmptyTriangleMissionEndTime(systemParamJson.getDropOffEmptyTriangleMissionEndTime());

        padAnprService.updateSystemParameterPadanpr(systemParamJson);

        systemService.updateSystemParameter(systemParameter);

        systemParamJson = new SystemParamJson();

        BeanUtils.copyProperties(systemParameter, systemParamJson);

        List<SystemParamJson> systemParamJsonList = new ArrayList<>();
        systemParamJsonList.add(systemParamJson);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(systemParamJsonList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/getPortOperators", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    public ApiResponseJsonEntity getPortOperators(HttpServletResponse response) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        List<PortOperatorJson> portOperators = systemService.getPortOperatorJsonList();

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(portOperators);
        apiResponse.setDataMap(systemService.getPortOperatorTransactionTypesMap(loggedOperator.getRoleId()));

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/getParkingReleaseStats", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    public ApiResponseJsonEntity getParkingReleaseStats(HttpServletResponse response) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        List<PortOperatorJson> portOperators = systemService.getPortOperatorJsonList();

        SystemParameter systemParameter = systemService.getSystemParameter();

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(portOperators);
        apiResponse.setDataMap(systemService.getParkingReleaseStatsMap());
        apiResponse.setData(systemParameter.getIsParkingSupervisorReadonlyEnabled());

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/booking/slot/limit/periods/get", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getBookingSlotLimitPeriods(HttpServletResponse response) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(systemService.getBookingSlotLimitPeriods());

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/booking/slot/limit/map", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getBookingSlotLimitMap(HttpServletResponse response, @RequestBody BookingSlotLimitJson bookingSlotLimitJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (bookingSlotLimitJson.getPortOperatorId() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "portOperatorId is missing");

        if (bookingSlotLimitJson.getTransactionType() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "transactionType is missing");

        if (StringUtils.isBlank(bookingSlotLimitJson.getPeriod()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "period is missing");

        Map<String, List<BookingSlotLimitJson>> bookingLimitMap = new LinkedHashMap<>();

        List<DayOfWeekJson> daysWeekList = new ArrayList<>();

        systemService.getBookingSlotLimit(bookingSlotLimitJson, bookingLimitMap, daysWeekList);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataMap(bookingLimitMap);
        apiResponse.setDataList(daysWeekList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/booking/slot/limit/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity saveBookingSlotLimit(HttpServletResponse response, @RequestBody BookingSlotLimitJson bookingSlotLimitJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (bookingSlotLimitJson.getPortOperatorId() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "portOperatorId is missing");

        if (bookingSlotLimitJson.getTransactionType() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "transactionType is missing");

        if (StringUtils.isBlank(bookingSlotLimitJson.getPeriod()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "period is missing");

        if (bookingSlotLimitJson.getBookingLimit() < ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "booking Limit is missing");

        if (bookingSlotLimitJson.getHourSlotFrom() < ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "hour slot from is missing");

        if (bookingSlotLimitJson.getHourSlotTo() < ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "hour slot to is missing");

        if (bookingSlotLimitJson.getPeriod().equalsIgnoreCase("DEFAULT")) {
            systemService.saveBookingSlotLimitDefault(bookingSlotLimitJson);
        } else {
            try {
                bookingSlotLimitJson.setDateSlot(ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyy, bookingSlotLimitJson.getPeriod()));
            } catch (Exception e) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "period is invalid");
            }

            systemService.saveBookingSlotLimit(bookingSlotLimitJson);
        }

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/update/transaction/type/flags", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updateTransactionTypeFlags(HttpServletResponse response, @RequestBody PortOperatorTransactionTypeJson portOperatorTransactionTypeJson)
        throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (portOperatorTransactionTypeJson.getPortOperatorId() == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "updateTransactionTypeFlags#1");

        if (portOperatorTransactionTypeJson.getTransactionType() == ServerConstants.DEFAULT_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "updateTransactionTypeFlags#2");

        if (portOperatorTransactionTypeJson.getMissionCancelSystemAfterMinutes() < 0)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "updateTransactionTypeFlags#3");

        PortOperator portOperator = systemService.getPortOperatorFromMap(portOperatorTransactionTypeJson.getPortOperatorId());

        if (portOperator == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "updateTransactionTypeFlags#4");

        for (PortOperatorTransactionType portOperatorTransactionType : portOperator.getPortOperatorTransactionTypesList()) {

            if (portOperatorTransactionType.getTransactionType() == portOperatorTransactionTypeJson.getTransactionType()) {

                portOperatorTransactionType.setIsDirectToPort(portOperatorTransactionTypeJson.getIsDirectToPort());
                portOperatorTransactionType.setIsAllowMultipleEntries(portOperatorTransactionTypeJson.getIsAllowMultipleEntries());
                portOperatorTransactionType.setMissionCancelSystemAfterMinutes(portOperatorTransactionTypeJson.getMissionCancelSystemAfterMinutes());
                portOperatorTransactionType.setIsTripCancelSystem(portOperatorTransactionTypeJson.getIsTripCancelSystem());
                portOperatorTransactionType.setTripCancelSystemAfterMinutes(Boolean.TRUE.equals(portOperatorTransactionTypeJson.getIsTripCancelSystem()) ? portOperatorTransactionTypeJson.getTripCancelSystemAfterMinutes() : 0);

                if (portOperatorTransactionTypeJson.getPortOperatorGateId() != null) {
                    PortOperatorGate portOperatorGate = systemService.getPortOperatorGateById(portOperatorTransactionTypeJson.getPortOperatorGateId());
                    portOperatorTransactionType.setPortOperatorGate(portOperatorGate);
                }

                if (portOperatorTransactionType.getIsDirectToPort()) {
                    portOperatorTransactionType.setIsAllowedForParkingAndKioskOp(false);
                    portOperatorTransactionType.setIsAllowedForVirtualKioskOp(true);
                } else {
                    portOperatorTransactionType.setIsAllowedForParkingAndKioskOp(true);
                    portOperatorTransactionType.setIsAllowedForVirtualKioskOp(false);
                }

                tripService.updateExistingMissionTripsFlags(portOperatorTransactionType);

                portOperatorTransactionTypeJson = new PortOperatorTransactionTypeJson(portOperatorTransactionType.getTransactionType(),
                    portOperatorTransactionTypeJson.getPortOperatorId(), portOperatorTransactionType.getIsAllowedForParkingAndKioskOp(),
                    portOperatorTransactionType.getIsAllowedForVirtualKioskOp(), portOperatorTransactionType.getIsAutoReleaseParking(),
                    portOperatorTransactionType.getIsDirectToPort(), portOperatorTransactionType.getIsAllowMultipleEntries(), portOperatorTransactionTypeJson.getIsTripCancelSystem(),
                    portOperatorTransactionType.getMissionCancelSystemAfterMinutes(), portOperatorTransactionType.getTripCancelSystemAfterMinutes(),
                    portOperatorTransactionType.getPortOperatorGate() == null ? null : portOperatorTransactionType.getPortOperatorGate().getId());

                padAnprService.updateTransactionTypeFlagPadanpr(portOperatorTransactionTypeJson);

                activityLogService.saveActivityLog(new ActivityLog(ServerConstants.ACTIVITY_LOG_UPDATE_TRANSACTION_TYPE_FLAG, loggedOperator.getId(),
                    new ObjectMapper().writeValueAsString(portOperatorTransactionTypeJson)));

                break;
            }
        }

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/report/port/issue", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity reportPortIssue(HttpServletResponse response, @RequestBody PortOperatorAlertJson portOperatorAlertJson)
        throws PADException, PADValidationException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (portOperatorAlertJson.getPortOperatorId() == null || portOperatorAlertJson.getPortOperatorId() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "portOperatorId is missing");

        if (portOperatorAlertJson.getTransactionType() == null || portOperatorAlertJson.getTransactionType() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "transactionType is missing");

        if (portOperatorAlertJson.getWorkingCapacityPercentage() == null || portOperatorAlertJson.getWorkingCapacityPercentage() < ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "working capacity percentage is missing");

        if (StringUtils.isBlank(portOperatorAlertJson.getDescription()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "description is missing");

        if (portOperatorAlertJson.getDescription().length() > 255)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "description is too long");

        if (StringUtils.isBlank(portOperatorAlertJson.getNameReporter()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "reporter name is missing");

        if (StringUtils.isBlank(portOperatorAlertJson.getMsisdnReporter()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "reporter mobile is missing");

        if (StringUtils.isBlank(portOperatorAlertJson.getIssueDateString()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "issue date is missing");

        if (StringUtils.isBlank(portOperatorAlertJson.getIssueTimeString()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "issue time is missing");

        Date dateIssue = null;

        try {
            dateIssue = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm,
                portOperatorAlertJson.getIssueDateString() + " " + portOperatorAlertJson.getIssueTimeString());
        } catch (ParseException pe) {
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
        }

        Date dateEstimatedResolution = null;

        if (StringUtils.isNotBlank(portOperatorAlertJson.getEstimatedResolutionDateString()) && StringUtils.isNotBlank(portOperatorAlertJson.getEstimatedResolutionTimeString())) {

            try {
                dateEstimatedResolution = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm,
                    portOperatorAlertJson.getEstimatedResolutionDateString() + " " + portOperatorAlertJson.getEstimatedResolutionTimeString());
            } catch (ParseException pe) {
                throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
            }

            if (dateEstimatedResolution.getTime() < System.currentTimeMillis())
                throw new PADValidationException(ServerResponseConstants.ESTIMATED_RESOLUTION_DATE_IN_PAST_CODE, ServerResponseConstants.ESTIMATED_RESOLUTION_DATE_IN_PAST_TEXT,
                    "#2");

            if (dateEstimatedResolution.getTime() < dateIssue.getTime())
                throw new PADValidationException(ServerResponseConstants.ESTIMATED_RESOLUTION_DATE_IN_PAST_CODE, ServerResponseConstants.ESTIMATED_RESOLUTION_DATE_IN_PAST_TEXT,
                    "#3");
        }

        PortOperatorAlert portOperatorAlert = new PortOperatorAlert();
        portOperatorAlert.setCode(SecurityUtil.generateUniqueCode());
        portOperatorAlert.setPortOperatorId(portOperatorAlertJson.getPortOperatorId());
        portOperatorAlert.setTransactionType(portOperatorAlertJson.getTransactionType());
        portOperatorAlert.setWorkingCapacity(portOperatorAlertJson.getWorkingCapacityPercentage());
        portOperatorAlert.setDateIssue(dateIssue);
        portOperatorAlert.setDescription(portOperatorAlertJson.getDescription());
        portOperatorAlert.setDateResolutionEstimate(dateEstimatedResolution);
        portOperatorAlert.setNameReporter(portOperatorAlertJson.getNameReporter());
        portOperatorAlert.setMsisdnReporter(portOperatorAlertJson.getMsisdnReporter());
        portOperatorAlert.setResolutionDescription(ServerConstants.DEFAULT_STRING);
        portOperatorAlert.setOperatorId(loggedOperator.getId());

        systemService.savePortOperatorAlert(portOperatorAlert);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/report/port/issue/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getIssueCount(HttpServletResponse response, @RequestBody PortOperatorAlertJson portOperatorAlertJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        long count = systemService.getIssueCount(portOperatorAlertJson.getPortOperatorId());

        List<Long> res = new ArrayList<>();
        res.add(count);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(res);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;

    }

    @RequestMapping(value = "/report/port/issue/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getIssueList(HttpServletResponse response, @RequestBody PortOperatorAlertJson portOperatorAlertJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        List<PortOperatorAlertJson> issues = systemService.getIssueList(portOperatorAlertJson.getPortOperatorId(), portOperatorAlertJson.getSortColumn(),
            portOperatorAlertJson.getSortAsc(), ServerUtil.getStartLimitPagination(portOperatorAlertJson.getCurrentPage(), portOperatorAlertJson.getPageCount()),
            portOperatorAlertJson.getPageCount());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(issues);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/report/port/issue/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updatePortIssue(HttpServletResponse response, @RequestBody PortOperatorAlertJson portOperatorAlertJson)
        throws PADException, PADValidationException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(portOperatorAlertJson.getCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "code is missing");

        if (portOperatorAlertJson.getWorkingCapacityPercentage() == null || portOperatorAlertJson.getWorkingCapacityPercentage() < ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "working capacity percentage is missing");

        if (StringUtils.isBlank(portOperatorAlertJson.getDescription()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "description is missing");

        if (portOperatorAlertJson.getDescription().length() > 255)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "description is too long");

        if (StringUtils.isBlank(portOperatorAlertJson.getNameReporter()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "reporter name is missing");

        if (StringUtils.isBlank(portOperatorAlertJson.getMsisdnReporter()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "reporter mobile is missing");

        if (StringUtils.isBlank(portOperatorAlertJson.getIssueDateString()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "issue date is missing");

        if (StringUtils.isBlank(portOperatorAlertJson.getIssueTimeString()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "issue time is missing");

        Date dateIssue = null;

        try {
            dateIssue = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm,
                portOperatorAlertJson.getIssueDateString() + " " + portOperatorAlertJson.getIssueTimeString());
        } catch (ParseException pe) {
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
        }

        Date dateEstimatedResolution = null;

        if (StringUtils.isNotBlank(portOperatorAlertJson.getEstimatedResolutionDateString()) && StringUtils.isNotBlank(portOperatorAlertJson.getEstimatedResolutionTimeString())) {

            try {
                dateEstimatedResolution = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm,
                    portOperatorAlertJson.getEstimatedResolutionDateString() + " " + portOperatorAlertJson.getEstimatedResolutionTimeString());
            } catch (ParseException pe) {
                throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
            }

            if (dateEstimatedResolution.getTime() < System.currentTimeMillis())
                throw new PADValidationException(ServerResponseConstants.ESTIMATED_RESOLUTION_DATE_IN_PAST_CODE, ServerResponseConstants.ESTIMATED_RESOLUTION_DATE_IN_PAST_TEXT,
                    "#2");

            if (dateEstimatedResolution.getTime() < dateIssue.getTime())
                throw new PADValidationException(ServerResponseConstants.ESTIMATED_RESOLUTION_DATE_IN_PAST_CODE, ServerResponseConstants.ESTIMATED_RESOLUTION_DATE_IN_PAST_TEXT,
                    "#3");
        }

        PortOperatorAlert portOperatorAlert = systemService.getPortOperatorAlertByCode(portOperatorAlertJson.getCode());

        if (portOperatorAlert == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "portOperatorAlert is null");

        portOperatorAlert.setWorkingCapacity(portOperatorAlertJson.getWorkingCapacityPercentage());
        portOperatorAlert.setDateIssue(dateIssue);
        portOperatorAlert.setDescription(portOperatorAlertJson.getDescription());
        portOperatorAlert.setDateResolutionEstimate(dateEstimatedResolution);
        portOperatorAlert.setNameReporter(portOperatorAlertJson.getNameReporter());
        portOperatorAlert.setMsisdnReporter(portOperatorAlertJson.getMsisdnReporter());
        portOperatorAlert.setResolutionDescription(ServerConstants.DEFAULT_STRING);
        portOperatorAlert.setOperatorId(loggedOperator.getId());

        systemService.updatePortOperatorAlert(portOperatorAlert);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/report/port/issue/resolve", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity resolvePortIssue(HttpServletResponse response, @RequestBody PortOperatorAlertJson portOperatorAlertJson)
        throws PADException, PADValidationException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(portOperatorAlertJson.getCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "code is missing");

        if (StringUtils.isBlank(portOperatorAlertJson.getResolutionDescription()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "resolution description is missing");

        if (StringUtils.isBlank(portOperatorAlertJson.getDateResolutionString()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "resolution date is missing");

        if (StringUtils.isBlank(portOperatorAlertJson.getTimeResolutionString()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "resolution time is missing");

        Date dateResolution = null;

        try {
            dateResolution = ServerUtil.parseDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm,
                portOperatorAlertJson.getDateResolutionString() + " " + portOperatorAlertJson.getTimeResolutionString());
        } catch (ParseException pe) {
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "date Resolution is invalid");
        }

        PortOperatorAlert portOperatorAlert = systemService.getPortOperatorAlertByCode(portOperatorAlertJson.getCode());

        if (portOperatorAlert == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "portOperatorAlert is null");

        if (dateResolution.getTime() < portOperatorAlert.getDateIssue().getTime())
            throw new PADValidationException(ServerResponseConstants.RESOLUTION_DATE_BEFORE_ISSUE_DATE_CODE, ServerResponseConstants.RESOLUTION_DATE_BEFORE_ISSUE_DATE_TEXT, "#3");

        portOperatorAlert.setDateResolution(dateResolution);
        portOperatorAlert.setResolutionDescription(portOperatorAlertJson.getResolutionDescription());
        portOperatorAlert.setOperatorId(loggedOperator.getId());

        systemService.resolvePortOperatorAlert(portOperatorAlert);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/getPortOperatorGates", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    public ApiResponseJsonEntity getPortOperatorGates(HttpServletResponse response) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");


        List<PortOperatorGateJson> portOperatorGateJsonList;
        List<PortOperatorGate> portOperatorGates = systemService.getPortOperatorGates();
        portOperatorGateJsonList = portOperatorGates
            .stream().map(portOperatorGate -> new PortOperatorGateJson(portOperatorGate.getId(), portOperatorGate.getGateNumber(), portOperatorGate.getGateNumberShort()))
            .collect(Collectors.toList());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(portOperatorGateJsonList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }
}
