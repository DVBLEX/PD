package com.pad.server.web.restcontrollers;

import java.util.ArrayList;
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
import com.pad.server.base.entities.Operator;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.DriverJson;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.driver.DriverService;
import com.pad.server.base.services.operator.OperatorService;

@RestController
@RequestMapping("/driver")
public class DriverRESTController {

    @Autowired
    private AccountService     accountService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private DriverService      driverService;

    @Autowired
    private OperatorService    operatorService;

    @RequestMapping(value = "/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getDriverCount(HttpServletResponse response, @RequestBody DriverJson driverJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Account account = accountService.getAccountById(loggedOperator.getAccountId());
        if (account == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");

        long count = driverService.getDriverCount(account.getId(), driverJson.getFirstName(), driverJson.getLastName(), driverJson.getEmail(), driverJson.getMsisdn(),
            driverJson.getLicenceNumber());

        List<Long> res = new ArrayList<>();
        res.add(count);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(res);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getDriverList(HttpServletResponse response, @RequestBody DriverJson driverJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Account account = accountService.getAccountById(loggedOperator.getAccountId());
        if (account == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");

        List<Driver> drivers = driverService.getDriverList(account.getId(), driverJson.getFirstName(), driverJson.getLastName(), driverJson.getEmail(), driverJson.getMsisdn(),
            driverJson.getLicenceNumber(), driverJson.getSortColumn(), driverJson.getSortAsc(),
            ServerUtil.getStartLimitPagination(driverJson.getCurrentPage(), driverJson.getPageCount()), driverJson.getPageCount());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(drivers);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity saveDriver(HttpServletResponse response, @RequestBody DriverJson driverJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Account account = accountService.getAccountById(loggedOperator.getAccountId());
        if (account == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");

        if (StringUtils.isBlank(driverJson.getFirstName()))
            throw new PADException(ServerResponseConstants.MISSING_FIRST_NAME_CODE, ServerResponseConstants.MISSING_FIRST_NAME_TEXT, "");

        if (StringUtils.isBlank(driverJson.getLastName()))
            throw new PADException(ServerResponseConstants.MISSING_LAST_NAME_CODE, ServerResponseConstants.MISSING_LAST_NAME_TEXT, "");

        if (StringUtils.isBlank(driverJson.getMsisdn()))
            throw new PADException(ServerResponseConstants.MISSING_MSISDN_CODE, ServerResponseConstants.MISSING_MSISDN_TEXT, "");

        if (StringUtils.isBlank(driverJson.getIssuingCountryISO()))
            throw new PADException(ServerResponseConstants.MISSING_ISSUING_COUNTRY_CODE, ServerResponseConstants.MISSING_ISSUING_COUNTRY_TEXT, "");

        if (StringUtils.isBlank(driverJson.getLicenceNumber()))
            throw new PADException(ServerResponseConstants.MISSING_LICENCE_NUMBER_CODE, ServerResponseConstants.MISSING_LICENCE_NUMBER_TEXT, "");

        if (driverJson.getLanguageId() == null || driverJson.getLanguageId() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.MISSING_LICENCE_NUMBER_CODE, ServerResponseConstants.MISSING_LICENCE_NUMBER_TEXT, "");

        if (driverJson.getFirstName().length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_64)
            throw new PADException(ServerResponseConstants.INVALID_FIRST_NAME_CODE, ServerResponseConstants.INVALID_FIRST_NAME_TEXT, "");

        if (driverJson.getLastName().length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_64)
            throw new PADException(ServerResponseConstants.INVALID_LAST_NAME_CODE, ServerResponseConstants.INVALID_LAST_NAME_TEXT, "");

        if (driverJson.getMsisdn().length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_16)
            throw new PADException(ServerResponseConstants.INVALID_MSISDN_CODE, ServerResponseConstants.INVALID_MSISDN_TEXT, "");

        if (!driverJson.getIssuingCountryISO().matches(ServerConstants.REGEX_UNIVERSAL_COUNTRY_CODE))
            throw new PADException(ServerResponseConstants.INVALID_ISSUING_COUNTRY_CODE, ServerResponseConstants.INVALID_ISSUING_COUNTRY_TEXT, "");

        if (driverJson.getLicenceNumber().length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_32)
            throw new PADException(ServerResponseConstants.INVALID_LICENCE_NUMBER_CODE, ServerResponseConstants.INVALID_LICENCE_NUMBER_TEXT, "#1");

        if (!driverJson.getLicenceNumber().matches(ServerConstants.REGEX_ALPHA_NUMERIC))
            throw new PADException(ServerResponseConstants.INVALID_LICENCE_NUMBER_CODE, ServerResponseConstants.INVALID_LICENCE_NUMBER_TEXT, "#2");

        if (!StringUtils.isBlank(driverJson.getEmail()) && !driverJson.getEmail().matches(ServerConstants.REGEX_EMAIL))
            throw new PADException(ServerResponseConstants.INVALID_EMAIL_CODE, ServerResponseConstants.INVALID_EMAIL_TEXT, "");

        driverJson.setMsisdn(ServerUtil.getValidNumber(driverJson.getMsisdn(), "saveDriver"));

        driverService.saveDriver(driverJson, account, loggedOperator);

        activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_DRIVER_ADD, loggedOperator.getId(), account.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/remove/association", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity removeDriverAssociation(HttpServletResponse response, @RequestBody DriverJson driverJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(driverJson.getCode()))
            throw new PADException(ServerResponseConstants.MISSING_DRIVER_CODE_CODE, ServerResponseConstants.MISSING_DRIVER_CODE_TEXT, "");

        Driver driver = driverService.getDriverByCode(driverJson.getCode());

        if (driver == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "");

        // remove driver association with this transporter account
        driverService.removeDriverAssociation(loggedOperator.getAccountId(), loggedOperator.getId(), driver);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }
}
