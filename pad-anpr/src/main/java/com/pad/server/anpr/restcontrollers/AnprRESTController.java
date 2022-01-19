package com.pad.server.anpr.restcontrollers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.AnprParameterJson;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.services.anpr.AnprBaseService;

@RestController
@RequestMapping("/anpr")
public class AnprRESTController {

    @Autowired
    private AnprBaseService anprBaseService;

    @Value("${padanpr.api.client.id}")
    private String          padanprApiClientId;

    @Value("${padanpr.api.secret}")
    private String          padanprApiSecret;

    @RequestMapping(value = "/parameter/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updateAnperParameter(HttpServletResponse response, @RequestBody AnprParameterJson anprParameterJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (!anprParameterJson.getApiCientId().equals(padanprApiClientId))
            throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "client id is invalid");

        if (!anprParameterJson.getApiSecret().equals(padanprApiSecret))
            throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "secret is invalid");

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

        anprBaseService.getAnprParameter().setEntryLogPageSize(anprParameterJson.getEntryLogPageSize());
        anprBaseService.getAnprParameter().setEntryLogConnectTimeout(anprParameterJson.getEntryLogConnectTimeout());
        anprBaseService.getAnprParameter().setEntryLogSocketTimeout(anprParameterJson.getEntryLogSocketTimeout());
        anprBaseService.getAnprParameter().setEntryLogConnRequestTimeout(anprParameterJson.getEntryLogConnRequestTimeout());
        anprBaseService.getAnprParameter().setDefaultConnectTimeout(anprParameterJson.getDefaultConnectTimeout());
        anprBaseService.getAnprParameter().setDefaultSocketTimeout(anprParameterJson.getDefaultSocketTimeout());
        anprBaseService.getAnprParameter().setDefaultConnRequestTimeout(anprParameterJson.getDefaultConnRequestTimeout());
        anprBaseService.getAnprParameter().setParkingPermissionHoursInFuture(anprParameterJson.getParkingPermissionHoursInFuture());
        anprBaseService.getAnprParameter().setParkingPermissionHoursPriorSlotDate(anprParameterJson.getParkingPermissionHoursPriorSlotDate());
        anprBaseService.getAnprParameter().setParkingPermissionHoursAfterSlotDate(anprParameterJson.getParkingPermissionHoursAfterSlotDate());
        anprBaseService.getAnprParameter().setParkingPermissionHoursAfterExitDate(anprParameterJson.getParkingPermissionHoursAfterExitDate());
        anprBaseService.getAnprParameter().setBtDowntimeSecondsLimit(anprParameterJson.getBtDowntimeSecondsLimit());
        anprBaseService.getAnprParameter().setBtUptimeSecondsLimit(anprParameterJson.getBtUptimeSecondsLimit());

        if (anprParameterJson.getIsIISServerEnabled() != null) {
            anprBaseService.getAnprParameter().setIsIISServerEnabled(anprParameterJson.getIsIISServerEnabled());
        }

        anprBaseService.getAnprParameter().setAgsparkingEntryLane1VideoFeedUrl(anprParameterJson.getAgsparkingEntryLane1VideoFeedUrl());
        anprBaseService.getAnprParameter().setAgsparkingEntryLane2VideoFeedUrl(anprParameterJson.getAgsparkingEntryLane2VideoFeedUrl());
        anprBaseService.getAnprParameter().setAgsparkingEntryLane3VideoFeedUrl(anprParameterJson.getAgsparkingEntryLane3VideoFeedUrl());
        anprBaseService.getAnprParameter().setAgsparkingEntryLane4VideoFeedUrl(anprParameterJson.getAgsparkingEntryLane4VideoFeedUrl());
        anprBaseService.getAnprParameter().setAgsparkingEntryLane5VideoFeedUrl(anprParameterJson.getAgsparkingEntryLane5VideoFeedUrl());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

}
