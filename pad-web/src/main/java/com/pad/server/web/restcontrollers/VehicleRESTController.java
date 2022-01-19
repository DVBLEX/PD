package com.pad.server.web.restcontrollers;

import java.util.ArrayList;
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
import com.pad.server.base.entities.Operator;
import com.pad.server.base.entities.Vehicle;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.VehicleJson;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.vehicle.VehicleService;

@RestController
@RequestMapping("/vehicle")
public class VehicleRESTController {

    @Autowired
    private AccountService     accountService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private OperatorService    operatorService;

    @Autowired
    private VehicleService     vehicleService;

    @RequestMapping(value = "/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getVehicleCount(HttpServletResponse response, @RequestBody VehicleJson vehicleJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Account account = accountService.getAccountById(loggedOperator.getAccountId());
        if (account == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");

        long count = vehicleService.getVehicleCount(account.getId(), vehicleJson.getRegistrationCountryISO(), vehicleJson.getVehicleRegistration(), vehicleJson.getMake(),
            vehicleJson.getColor());

        List<Long> res = new ArrayList<>();
        res.add(count);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(res);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getVehicleList(HttpServletResponse response, @RequestBody VehicleJson vehicleJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Account account = accountService.getAccountById(loggedOperator.getAccountId());
        if (account == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");

        List<Vehicle> vehicles = vehicleService.getVehicleList(account.getId(), vehicleJson.getRegistrationCountryISO(), vehicleJson.getVehicleRegistration(),
            vehicleJson.getMake(), vehicleJson.getColor(), vehicleJson.getSortColumn(), vehicleJson.getSortAsc(),
            ServerUtil.getStartLimitPagination(vehicleJson.getCurrentPage(), vehicleJson.getPageCount()), vehicleJson.getPageCount());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(vehicles);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity saveVehicle(HttpServletResponse response, @RequestBody VehicleJson vehicleJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Account account = accountService.getAccountById(loggedOperator.getAccountId());
        if (account == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");

        if (StringUtils.isBlank(vehicleJson.getRegistrationCountryISO()))
            throw new PADException(ServerResponseConstants.MISSING_VEHICLE_REGISTRATION_COUNTRY_CODE, ServerResponseConstants.MISSING_VEHICLE_REGISTRATION_COUNTRY_TEXT, "");

        if (StringUtils.isBlank(vehicleJson.getVehicleRegistration()))
            throw new PADException(ServerResponseConstants.MISSING_VEHICLE_REGISTRATION_NUMBER_CODE, ServerResponseConstants.MISSING_VEHICLE_REGISTRATION_NUMBER_TEXT, "");

        if (StringUtils.isBlank(vehicleJson.getMake()))
            throw new PADException(ServerResponseConstants.INVALID_VEHICLE_MAKE_CODE, ServerResponseConstants.INVALID_VEHICLE_MAKE_TEXT, "");

        if (!vehicleJson.getRegistrationCountryISO().matches(ServerConstants.REGEX_UNIVERSAL_COUNTRY_CODE))
            throw new PADException(ServerResponseConstants.INVALID_COUNTRY_CODE, ServerResponseConstants.INVALID_COUNTRY_TEXT, "");

        if (vehicleJson.getVehicleRegistration().length() < ServerConstants.REGNUMBER_VALIDATION_LENGTH_MIN
            || vehicleJson.getVehicleRegistration().length() > ServerConstants.REGNUMBER_VALIDATION_LENGTH_MAX)
            throw new PADException(ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_CODE, ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_TEXT, "#1");

        if (!vehicleJson.getVehicleRegistration().matches(ServerConstants.REGEX_ALPHA_NUMERIC))
            throw new PADException(ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_CODE, ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_TEXT, "#2");

        if (vehicleJson.getMake().length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_32)
            throw new PADException(ServerResponseConstants.INVALID_VEHICLE_MAKE_CODE, ServerResponseConstants.INVALID_VEHICLE_MAKE_TEXT, "");

        String vehicleRegNumber = ServerUtil.formatVehicleRegNumber(vehicleJson.getVehicleRegistration());

        // check account vehicles
        List<Vehicle> vehicleList = account.getVehicleList();
        if (vehicleList.size() > 0) {
            for (Vehicle vehicle : vehicleList) {
                if (vehicle.getVehicleRegistration().equals(vehicleRegNumber))
                    throw new PADValidationException(ServerResponseConstants.VEHICLE_ALREADY_ADDED_TO_ACCOUNT_CODE, ServerResponseConstants.VEHICLE_ALREADY_ADDED_TO_ACCOUNT_TEXT,
                        "");
            }
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setCode(SecurityUtil.generateUniqueCode());
        vehicle.setAccount(account);
        vehicle.setRegistrationCountryISO(vehicleJson.getRegistrationCountryISO());
        vehicle.setVehicleRegistration(vehicleRegNumber);
        vehicle.setMake(vehicleJson.getMake());
        vehicle.setColor(StringUtils.isBlank(vehicleJson.getColor()) ? ServerConstants.DEFAULT_STRING : vehicleJson.getColor());
        vehicle.setOperatorId(loggedOperator.getId());
        vehicle.setIsAddedApi(Boolean.FALSE);
        vehicle.setIsApproved(Boolean.TRUE);
        vehicle.setIsActive(Boolean.TRUE);
        vehicle.setDateCreated(new Date());
        vehicle.setDateEdited(vehicle.getDateCreated());

        vehicleService.saveVehicle(vehicle);

        activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_VEHICLE_ADD, loggedOperator.getId(), account.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updateVehicle(HttpServletResponse response, @RequestBody VehicleJson vehicleJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Account account = accountService.getAccountById(loggedOperator.getAccountId());
        if (account == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");

        if (StringUtils.isBlank(vehicleJson.getRegistrationCountryISO()))
            throw new PADException(ServerResponseConstants.MISSING_VEHICLE_REGISTRATION_COUNTRY_CODE, ServerResponseConstants.MISSING_VEHICLE_REGISTRATION_COUNTRY_TEXT, "");

        if (StringUtils.isBlank(vehicleJson.getMake()))
            throw new PADException(ServerResponseConstants.INVALID_VEHICLE_MAKE_CODE, ServerResponseConstants.INVALID_VEHICLE_MAKE_TEXT, "");

        if (!vehicleJson.getRegistrationCountryISO().matches(ServerConstants.REGEX_UNIVERSAL_COUNTRY_CODE))
            throw new PADException(ServerResponseConstants.INVALID_COUNTRY_CODE, ServerResponseConstants.INVALID_COUNTRY_TEXT, "");

        if (vehicleJson.getMake().length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_32)
            throw new PADException(ServerResponseConstants.INVALID_VEHICLE_MAKE_CODE, ServerResponseConstants.INVALID_VEHICLE_MAKE_TEXT, "");

        Vehicle vehicle = vehicleService.getVehicleByCode(vehicleJson.getCode());

        if (vehicle == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "vehicle is null");

        vehicle.setRegistrationCountryISO(vehicleJson.getRegistrationCountryISO());
        vehicle.setMake(vehicleJson.getMake());
        vehicle.setColor(StringUtils.isBlank(vehicleJson.getColor()) ? ServerConstants.DEFAULT_STRING : vehicleJson.getColor());
        vehicle.setOperatorId(loggedOperator.getId());
        vehicle.setIsApproved(Boolean.TRUE);
        vehicle.setIsActive(vehicleJson.getIsActive());
        vehicle.setDateEdited(new Date());

        vehicleService.updateVehicle(vehicle);

        activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_VEHICLE_UPDATE, loggedOperator.getId(), account.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/activate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity activateVehicle(HttpServletResponse response, @RequestBody VehicleJson vehicleJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Account account = accountService.getAccountById(loggedOperator.getAccountId());
        if (account == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");

        if (StringUtils.isBlank(vehicleJson.getCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "vehicleCode is missing");

        Vehicle vehicle = vehicleService.getVehicleByCode(vehicleJson.getCode());

        if (vehicle == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "vehicleCode is invalid");

        vehicle.setIsApproved(true);
        vehicle.setIsActive(vehicleJson.getIsActive());

        vehicleService.updateVehicle(vehicle);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }
}
