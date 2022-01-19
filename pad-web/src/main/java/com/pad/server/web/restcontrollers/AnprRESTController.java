package com.pad.server.web.restcontrollers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.entities.AnprParameter;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.AnprParameterJson;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.services.anpr.AnprBaseService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.web.services.padanpr.PadAnprService;

@RestController
@RequestMapping("/anpr")
public class AnprRESTController {

    @Autowired
    private AnprBaseService anprBaseService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private PadAnprService  padAnprService;

    @RequestMapping(value = "/parameter/get", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getOperatorList(HttpServletResponse response) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        AnprParameter anprParameter = anprBaseService.getAnprParameter();

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setData(anprParameter);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/parameter/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updateAnprParameter(HttpServletResponse response, @RequestBody AnprParameterJson anprParameterJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (anprParameterJson.getEntryLogPageSize() == null || anprParameterJson.getEntryLogPageSize() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "entryLogPageSize is invalid");

        if (anprParameterJson.getEntryLogConnectTimeout() == null || anprParameterJson.getEntryLogConnectTimeout() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "entryLogConnectTimeout is missing");

        if (anprParameterJson.getEntryLogSocketTimeout() == null || anprParameterJson.getEntryLogSocketTimeout() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "entryLogSocketTimeout is missing");

        if (anprParameterJson.getEntryLogConnRequestTimeout() == null || anprParameterJson.getEntryLogConnRequestTimeout() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "entryLogConnRequestTimeout is missing");

        if (anprParameterJson.getDefaultConnectTimeout() == null || anprParameterJson.getDefaultConnectTimeout() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "defaultConnectTimeout is missing");

        if (anprParameterJson.getDefaultSocketTimeout() == null || anprParameterJson.getDefaultSocketTimeout() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "defaultSocketTimeou is missing");

        if (anprParameterJson.getDefaultConnRequestTimeout() == null || anprParameterJson.getDefaultConnRequestTimeout() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "defaultConnRequestTimeout is missing");

        if (anprParameterJson.getParkingPermissionHoursInFuture() == null || anprParameterJson.getParkingPermissionHoursInFuture() < ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "parkingPermissionHoursInFuture is missing");

        if (anprParameterJson.getParkingPermissionHoursPriorSlotDate() == null || anprParameterJson.getParkingPermissionHoursPriorSlotDate() < ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "parkingPermissionHoursPriorSlotDate is missing");

        if (anprParameterJson.getParkingPermissionHoursAfterSlotDate() == null || anprParameterJson.getParkingPermissionHoursAfterSlotDate() < ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "parkingPermissionHoursAfterSlotDate is missing");

        if (anprParameterJson.getParkingPermissionHoursAfterExitDate() == null || anprParameterJson.getParkingPermissionHoursAfterExitDate() < ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                "parkingPermissionHoursAfterExitDate is missing");

        if (anprParameterJson.getBtDowntimeSecondsLimit() == null || anprParameterJson.getBtDowntimeSecondsLimit() < ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "btDowntimeSecondsLimit is missing");

        if (anprParameterJson.getBtUptimeSecondsLimit() == null || anprParameterJson.getBtUptimeSecondsLimit() < ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "btUptimeSecondsLimit is missing");

        padAnprService.updateAnprParameterPadanpr(anprParameterJson);

        AnprParameter anprParameter = anprBaseService.getAnprParameter();
        anprParameter.setEntryLogPageSize(anprParameterJson.getEntryLogPageSize());
        anprParameter.setEntryLogConnectTimeout(anprParameterJson.getEntryLogConnectTimeout());
        anprParameter.setEntryLogSocketTimeout(anprParameterJson.getEntryLogSocketTimeout());
        anprParameter.setEntryLogConnRequestTimeout(anprParameterJson.getEntryLogConnRequestTimeout());
        anprParameter.setDefaultConnectTimeout(anprParameterJson.getDefaultConnectTimeout());
        anprParameter.setDefaultSocketTimeout(anprParameterJson.getDefaultSocketTimeout());
        anprParameter.setDefaultConnRequestTimeout(anprParameterJson.getDefaultConnRequestTimeout());
        anprParameter.setParkingPermissionHoursInFuture(anprParameterJson.getParkingPermissionHoursInFuture());
        anprParameter.setParkingPermissionHoursPriorSlotDate(anprParameterJson.getParkingPermissionHoursPriorSlotDate());
        anprParameter.setParkingPermissionHoursAfterSlotDate(anprParameterJson.getParkingPermissionHoursAfterSlotDate());
        anprParameter.setParkingPermissionHoursAfterExitDate(anprParameterJson.getParkingPermissionHoursAfterExitDate());
        anprParameter.setBtDowntimeSecondsLimit(anprParameterJson.getBtDowntimeSecondsLimit());
        anprParameter.setBtUptimeSecondsLimit(anprParameterJson.getBtUptimeSecondsLimit());

        if (anprParameterJson.getIsIISServerEnabled() != null) {
            anprParameter.setIsIISServerEnabled(anprParameterJson.getIsIISServerEnabled());
        }

        anprParameter.setAgsparkingEntryLane1VideoFeedUrl(anprParameterJson.getAgsparkingEntryLane1VideoFeedUrl());
        anprParameter.setAgsparkingEntryLane2VideoFeedUrl(anprParameterJson.getAgsparkingEntryLane2VideoFeedUrl());
        anprParameter.setAgsparkingEntryLane3VideoFeedUrl(anprParameterJson.getAgsparkingEntryLane3VideoFeedUrl());
        anprParameter.setAgsparkingEntryLane4VideoFeedUrl(anprParameterJson.getAgsparkingEntryLane4VideoFeedUrl());
        anprParameter.setAgsparkingEntryLane5VideoFeedUrl(anprParameterJson.getAgsparkingEntryLane5VideoFeedUrl());

        anprBaseService.updateAnprParameter(anprParameter);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

}
