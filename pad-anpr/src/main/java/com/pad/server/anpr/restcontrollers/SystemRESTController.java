package com.pad.server.anpr.restcontrollers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.anpr.jsonentities.TimeoutJson;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.entities.AnprParameter;
import com.pad.server.base.entities.PortOperator;
import com.pad.server.base.entities.PortOperatorGate;
import com.pad.server.base.entities.PortOperatorTransactionType;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.PortOperatorTransactionTypeJson;
import com.pad.server.base.jsonentities.api.SystemParamJson;
import com.pad.server.base.services.anpr.AnprBaseService;
import com.pad.server.base.services.system.SystemService;

@RestController
@RequestMapping("/system")
public class SystemRESTController {

    private static final Logger logger = Logger.getLogger(SystemRESTController.class);

    @Autowired
    private AnprBaseService     anprBaseService;

    @Autowired
    private SystemService       systemService;

    @Value("${anpr.remote.address}")
    private String              anprRemoteAddress;

    @Value("${padanpr.api.client.id}")
    private String              padanprApiClientId;

    @Value("${padanpr.api.secret}")
    private String              padanprApiSecret;

    @RequestMapping(value = "/uptime/check", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity checkUptime(HttpServletRequest request, HttpServletResponse response, @RequestParam("status") String status) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        StringBuilder builder = new StringBuilder();
        builder.append("CheckUptime");
        builder.append("Request: ");
        builder.append(request.getRemoteAddr());
        builder.append("#");
        builder.append("[status=");
        builder.append(status);
        builder.append("]");
        logger.info(builder.toString());

        // TODO when checking IP do it based on range
        if (!request.getRemoteAddr().equals(anprRemoteAddress) && !request.getRemoteAddr().equals("127.0.0.1"))
            throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "");

        AnprParameter anprParameter = anprBaseService.getAnprParameter();
        List<TimeoutJson> dataList = new ArrayList<>();

        TimeoutJson timeoutJson = new TimeoutJson();
        timeoutJson.setBtDowntimeSecondsLimit(anprParameter.getBtDowntimeSecondsLimit());
        timeoutJson.setBtUptimeSecondsLimit(anprParameter.getBtUptimeSecondsLimit());
        timeoutJson.setIsIISServerEnabled(anprParameter.getIsIISServerEnabled());

        if (!anprParameter.getiISServerState().equalsIgnoreCase(status)) {

            anprParameter.setiISServerState(status);
            anprBaseService.updateAnprParameter(anprParameter);
        }

        dataList.add(timeoutJson);

        apiResponse.setDataList(dataList);
        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/transaction/type/flag/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updateTransactionTypeFlag(HttpServletResponse response, @RequestBody PortOperatorTransactionTypeJson portOperatorTransactionTypeJson)
        throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (!portOperatorTransactionTypeJson.getApiClientId().equals(padanprApiClientId))
            throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "client id is invalid");

        if (!portOperatorTransactionTypeJson.getApiClientSecret().equals(padanprApiSecret))
            throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "secret is invalid");

        PortOperator portOperator = systemService.getPortOperatorFromMap(portOperatorTransactionTypeJson.getPortOperatorId());

        if (portOperator == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "");

        for (PortOperatorTransactionType portOperatorTransactionType : portOperator.getPortOperatorTransactionTypesList()) {

            if (portOperatorTransactionType.getTransactionType() == portOperatorTransactionTypeJson.getTransactionType()) {

                portOperatorTransactionType.setIsAllowedForParkingAndKioskOp(portOperatorTransactionTypeJson.getIsAllowedForParkingAndKioskOp());
                portOperatorTransactionType.setIsAllowedForVirtualKioskOp(portOperatorTransactionTypeJson.getIsAllowedForVirtualKioskOp());
                portOperatorTransactionType.setIsAutoReleaseParking(portOperatorTransactionTypeJson.getIsAutoReleaseParking());
                portOperatorTransactionType.setIsDirectToPort(portOperatorTransactionTypeJson.getIsDirectToPort());
                portOperatorTransactionType.setIsAllowMultipleEntries(portOperatorTransactionTypeJson.getIsAllowMultipleEntries());
                portOperatorTransactionType.setMissionCancelSystemAfterMinutes(portOperatorTransactionTypeJson.getMissionCancelSystemAfterMinutes());
                portOperatorTransactionType.setIsTripCancelSystem(portOperatorTransactionTypeJson.getIsTripCancelSystem());
                portOperatorTransactionType.setTripCancelSystemAfterMinutes(Boolean.TRUE.equals(portOperatorTransactionTypeJson.getIsTripCancelSystem()) ? portOperatorTransactionTypeJson.getTripCancelSystemAfterMinutes() : 0);
                if (portOperatorTransactionTypeJson.getPortOperatorGateId() != null) {
                    PortOperatorGate portOperatorGate = systemService.getPortOperatorGateById(portOperatorTransactionTypeJson.getPortOperatorGateId());
                    portOperatorTransactionType.setPortOperatorGate(portOperatorGate);
                }

                systemService.updatePortOperatorTransactionType(portOperatorTransactionType);
                break;
            }
        }

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/parameter/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updateSystemParameter(HttpServletResponse response, @RequestBody SystemParamJson systemParamJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (!systemParamJson.getApiClientId().equals(padanprApiClientId))
            throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "client id is invalid");

        if (!systemParamJson.getApiClientSecret().equals(padanprApiSecret))
            throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "secret is invalid");

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

        anprBaseService.getSystemParameter().setIsBookingLimitCheckEnabled(systemParamJson.getIsBookingLimitCheckEnabled());
        anprBaseService.getSystemParameter().setIsPortEntryFiltering(systemParamJson.getIsPortEntryFiltering());
        anprBaseService.getSystemParameter().setAutoReleaseExitCapacityPercentage(systemParamJson.getAutoReleaseExitCapacityPercentage());
        anprBaseService.getSystemParameter().setDropOffEmptyNightMissionStartTime(systemParamJson.getDropOffEmptyNightMissionStartTime());
        anprBaseService.getSystemParameter().setDropOffEmptyNightMissionEndTime(systemParamJson.getDropOffEmptyNightMissionEndTime());
        anprBaseService.getSystemParameter().setDropOffEmptyTriangleMissionStartTime(systemParamJson.getDropOffEmptyTriangleMissionStartTime());
        anprBaseService.getSystemParameter().setDropOffEmptyTriangleMissionEndTime(systemParamJson.getDropOffEmptyTriangleMissionEndTime());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }
}
