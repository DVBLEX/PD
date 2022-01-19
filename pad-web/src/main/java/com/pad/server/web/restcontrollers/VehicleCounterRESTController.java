package com.pad.server.web.restcontrollers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.counter.VehicleCounterJson;
import com.pad.server.base.services.counter.vehicle.VehicleCounterService;
import com.pad.server.base.services.operator.OperatorService;

@RestController
@RequestMapping("/counter/vehicle")
public class VehicleCounterRESTController {

    @Autowired
    private VehicleCounterService vehicleCounterService;

    @Autowired
    private OperatorService       operatorService;

    @RequestMapping(value = "/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getCount(HttpServletResponse response, @RequestBody VehicleCounterJson vehicleCounterJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (!StringUtils.isBlank(vehicleCounterJson.getDateCountStartString())) {
            try {
                vehicleCounterJson.setDateCountStart(ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyy, vehicleCounterJson.getDateCountStartString()));
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
            }
        }

        if (!StringUtils.isBlank(vehicleCounterJson.getDateCountEndString())) {
            try {
                vehicleCounterJson.setDateCountEnd(ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyy, vehicleCounterJson.getDateCountEndString()));
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#3");
            }

            if (StringUtils.isNotBlank(vehicleCounterJson.getDateCountStartString()) && vehicleCounterJson.getDateCountEnd().before(vehicleCounterJson.getDateCountStart()))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#4");
        }

        long count = vehicleCounterService.getCount(vehicleCounterJson);

        List<Long> res = new ArrayList<>();
        res.add(count);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(res);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getList(HttpServletResponse response, @RequestBody VehicleCounterJson vehicleCounterJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (!StringUtils.isBlank(vehicleCounterJson.getDateCountStartString())) {
            try {
                vehicleCounterJson.setDateCountStart(ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyy, vehicleCounterJson.getDateCountStartString()));
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
            }
        }

        if (!StringUtils.isBlank(vehicleCounterJson.getDateCountEndString())) {
            try {
                vehicleCounterJson.setDateCountEnd(ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyy, vehicleCounterJson.getDateCountEndString()));
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#3");
            }

            if (StringUtils.isNotBlank(vehicleCounterJson.getDateCountStartString()) && vehicleCounterJson.getDateCountEnd().before(vehicleCounterJson.getDateCountStart()))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#4");
        }

        List<VehicleCounterJson> list = vehicleCounterService.getList(vehicleCounterJson);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(list);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/session/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getLanes(HttpServletResponse response) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(vehicleCounterService.getSessionOperatorsList());

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @ResponseBody
    @RequestMapping(value = "/export", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<InputStreamResource> exportVehicleCounter(HttpServletResponse response, @FormParam("dateCountStartString") final String dateCountStartString,
        @FormParam("dateCountEndString") final String dateCountEndString, @FormParam("device") final String device, @FormParam("lane") final Integer lane,
        @FormParam("session") final String session, @FormParam("type") final String type, @FormParam("isShowDefaultDates") final Boolean isShowDefaultDates)
        throws PADException, Exception {

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        VehicleCounterJson vehicleCounterJson = new VehicleCounterJson();

        if (!StringUtils.isBlank(dateCountStartString)) {
            try {
                vehicleCounterJson.setDateCountStart(ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyy, dateCountStartString));
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");
            }
        }

        if (!StringUtils.isBlank(dateCountEndString)) {
            try {
                vehicleCounterJson.setDateCountEnd(ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyy, dateCountEndString));
            } catch (ParseException pe) {
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#3");
            }

            if (StringUtils.isNotBlank(dateCountStartString) && vehicleCounterJson.getDateCountEnd().before(vehicleCounterJson.getDateCountStart()))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#4");
        }

        vehicleCounterJson.setDeviceId(device);
        vehicleCounterJson.setLaneNumber(lane == null ? ServerConstants.DEFAULT_INT : lane.intValue());
        vehicleCounterJson.setSessionIdString(session);
        vehicleCounterJson.setType(type);
        vehicleCounterJson.setIsShowDefaultDates(isShowDefaultDates);
        vehicleCounterJson.setCurrentPage(0);
        vehicleCounterJson.setPageCount(-1);

        List<VehicleCounterJson> list = vehicleCounterService.getList(vehicleCounterJson);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        vehicleCounterService.exportVehicleCounter(list, byteArrayOutputStream);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        InputStreamResource inputStreamResource = new InputStreamResource(byteArrayInputStream);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeaders.setContentDispositionFormData("", "AGS_PAD_VEHICLE_COUNTER_" + ServerUtil.formatDate(ServerConstants.EXPORT_yyyyMMddHHmmss, new Date()) + ".xlsx");

        return new ResponseEntity<>(inputStreamResource, responseHeaders, HttpStatus.OK);
    }

}
