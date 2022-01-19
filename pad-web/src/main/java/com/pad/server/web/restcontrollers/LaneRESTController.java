package com.pad.server.web.restcontrollers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.entities.Lane;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.LaneJson;
import com.pad.server.base.services.lane.LaneService;
import com.pad.server.base.services.operator.OperatorService;

@RestController
@RequestMapping("/lane")
public class LaneRESTController {

    @Autowired
    private LaneService     laneService;

    @Autowired
    private OperatorService operatorService;

    @RequestMapping(value = "/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getCount(HttpServletResponse response, @RequestBody LaneJson laneJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        long count = laneService.getCount(laneJson);

        List<Long> res = new ArrayList<>();
        res.add(count);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(res);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getList(HttpServletResponse response, @RequestBody LaneJson laneJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        List<LaneJson> list = laneService.getList(laneJson);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(list);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity saveLane(HttpServletResponse response, @RequestBody LaneJson laneJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (laneJson.getLaneId() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "lane id is invalid");

        if (laneJson.getLaneNumber() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "lane number is invalid");

        if (laneJson.getZoneId() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "zone id is invalid");

        if (StringUtils.isBlank(laneJson.getDeviceId()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "device id is invalid");

        if (StringUtils.isBlank(laneJson.getDeviceName()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "device name is invalid");

        if (StringUtils.isBlank(laneJson.getAllowedHosts()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "allowed hosts is invalid");

        Lane lane = laneService.getLaneByLaneId(laneJson.getLaneId());

        if (lane != null)
            throw new PADValidationException(ServerResponseConstants.LANE_ALREADY_EXIST_CODE, ServerResponseConstants.LANE_ALREADY_EXIST_TEXT, "Lane already exist");

        lane = laneService.getLaneByDeviceId(laneJson.getDeviceId());

        if (lane != null)
            throw new PADValidationException(ServerResponseConstants.MAPPING_DEVICE_ALREADY_MAPPED_CODE, ServerResponseConstants.MAPPING_DEVICE_ALREADY_MAPPED_TEXT,
                "Device already mapped.");

        laneJson.setPrinterIp(laneJson.getPrinterIp() == null ? ServerConstants.DEFAULT_STRING : laneJson.getPrinterIp());

        lane = new Lane();

        BeanUtils.copyProperties(laneJson, lane);

        laneService.saveLane(lane);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updateLane(HttpServletResponse response, @RequestBody LaneJson laneJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (laneJson.getLaneId() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "lane id is invalid");

        if (laneJson.getLaneNumber() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "lane number is invalid");

        if (laneJson.getZoneId() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "zone id is invalid");

        if (StringUtils.isBlank(laneJson.getDeviceId()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "device id is invalid");

        if (StringUtils.isBlank(laneJson.getDeviceName()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "device name is invalid");

        if (StringUtils.isBlank(laneJson.getAllowedHosts()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "allowed hosts is invalid");

        Lane lane = laneService.getLaneById(laneJson.getId());

        if (lane == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "lane is null");

        if (laneJson.getLaneId() != lane.getLaneId() && laneService.getLaneByLaneId(laneJson.getLaneId()) != null)
            throw new PADValidationException(ServerResponseConstants.LANE_ALREADY_EXIST_CODE, ServerResponseConstants.LANE_ALREADY_EXIST_TEXT, "Lane already exist");

        if (!laneJson.getDeviceId().equalsIgnoreCase(lane.getDeviceId()) && laneService.getLaneByDeviceId(laneJson.getDeviceId()) != null)
            throw new PADValidationException(ServerResponseConstants.MAPPING_DEVICE_ALREADY_MAPPED_CODE, ServerResponseConstants.MAPPING_DEVICE_ALREADY_MAPPED_TEXT,
                "Lane/Device mapping already exist.");

        BeanUtils.copyProperties(laneJson, lane);

        laneService.updateLane(lane);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/activate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity activateLane(HttpServletResponse response, @RequestBody LaneJson laneJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (laneJson.getLaneId() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "lane id is invalid");

        Lane lane = laneService.getLaneByLaneId(laneJson.getLaneId());

        if (lane == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "lane is null");

        lane.setIsActive(laneJson.getIsActive());

        laneService.updateLane(lane);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/parking/entry/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getParkingEntryLanes(HttpServletResponse response) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        List<LaneJson> list = laneService.getParkingEntryLanes();

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(list);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

}
