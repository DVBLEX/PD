package com.pad.server.api.restcontrollers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.api.services.api.ApiService;
import com.pad.server.api.services.trip.TripService;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.TripApiJson;
import com.pad.server.base.jsonentities.api.TripApiResponseJson;

@RestController
@RequestMapping("/trip")
public class TripRESTController {

    @Autowired
    private ApiService  apiService;

    @Autowired
    private TripService tripService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity create(HttpServletRequest request, HttpServletResponse response, @RequestHeader(name = "authorization") String authHeader,
        @RequestBody TripApiJson tripApiJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator operator = apiService.validateApiRequest(authHeader, request.getRemoteAddr());

        if (StringUtils.isBlank(tripApiJson.getReferenceNumber()))
            throw new PADException(ServerResponseConstants.MISSING_REFERENCE_NUMBER_CODE, ServerResponseConstants.MISSING_REFERENCE_NUMBER_TEXT, "missing reference number");

        if (!tripApiJson.getReferenceNumber().matches(ServerConstants.REGEX_ALPHA_NUMERIC))
            throw new PADException(ServerResponseConstants.INVALID_REFERENCE_NUMBER_CODE, ServerResponseConstants.INVALID_REFERENCE_NUMBER_TEXT, "invalid reference number");

        if (tripApiJson.getReferenceNumber().length() < ServerConstants.REFERENCE_NUMBER_VALIDATION_LENGTH_MIN
            || tripApiJson.getReferenceNumber().length() > ServerConstants.REFERENCE_NUMBER_VALIDATION_LENGTH_MAX)
            throw new PADException(ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_CODE, ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_TEXT,
                "invalid reference number lenght");

        if (StringUtils.isBlank(tripApiJson.getTransactionType()))
            throw new PADException(ServerResponseConstants.MISSING_TRANSACTION_TYPE_CODE, ServerResponseConstants.MISSING_TRANSACTION_TYPE_TEXT, "missing transaction type");

        if (!tripApiJson.getTransactionType().matches(ServerConstants.REGEX_ALPHA_NUMERIC))
            throw new PADException(ServerResponseConstants.INVALID_TRANSACTION_TYPE_CODE, ServerResponseConstants.INVALID_TRANSACTION_TYPE_TEXT, "invalid transaction type");

        if (tripApiJson.getTransactionType().length() < ServerConstants.TRANSACTION_TYPE_API_VALIDATION_LENGTH_MIN
            || tripApiJson.getTransactionType().length() > ServerConstants.TRANSACTION_TYPE_API_VALIDATION_LENGTH_MAX)
            throw new PADException(ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_CODE, ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_TEXT,
                "invalid transaction type lenght");

        if (StringUtils.isBlank(tripApiJson.getTransporterShortName()))
            throw new PADException(ServerResponseConstants.MISSING_TRANSPORTER_SHORT_NAME_CODE, ServerResponseConstants.MISSING_TRANSPORTER_SHORT_NAME_TEXT,
                "missing transporter short name");

        if (tripApiJson.getTransporterShortName().length() < ServerConstants.TRANSPORTER_SHORT_NAME_VALIDATION_LENGTH_MIN
            || tripApiJson.getTransporterShortName().length() > ServerConstants.TRANSPORTER_SHORT_NAME_VALIDATION_LENGTH_MAX)
            throw new PADException(ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_CODE, ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_TEXT,
                "invalid transporter short name lenght");

        if (StringUtils.isBlank(tripApiJson.getVehicleRegNumber()))
            throw new PADException(ServerResponseConstants.MISSING_VEHICLE_REGISTRATION_NUMBER_CODE, ServerResponseConstants.MISSING_VEHICLE_REGISTRATION_NUMBER_TEXT,
                "missing vehicle registration number");

        if (!tripApiJson.getVehicleRegNumber().matches(ServerConstants.REGEX_ALPHA_NUMERIC))
            throw new PADException(ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_CODE, ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_TEXT,
                "invalid vehicle reg number");

        if (tripApiJson.getVehicleRegNumber().length() < ServerConstants.REGNUMBER_VALIDATION_LENGTH_MIN
            || tripApiJson.getVehicleRegNumber().length() > ServerConstants.REGNUMBER_VALIDATION_LENGTH_MAX)
            throw new PADException(ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_CODE, ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_TEXT,
                "invalid vehicle reg number lenght");

        tripApiJson.setVehicleRegNumber(ServerUtil.formatVehicleRegNumber(tripApiJson.getVehicleRegNumber()));

        if (StringUtils.isBlank(tripApiJson.getContainerId()))
            throw new PADException(ServerResponseConstants.MISSING_CONTAINER_ID_CODE, ServerResponseConstants.MISSING_CONTAINER_ID_TEXT, "missing container id");

        if (!tripApiJson.getContainerId().matches(ServerConstants.REGEX_ALPHA_NUMERIC))
            throw new PADException(ServerResponseConstants.INVALID_CONTAINER_ID_CODE, ServerResponseConstants.INVALID_CONTAINER_ID_TEXT, "invalid container id");

        if (tripApiJson.getContainerId().length() < ServerConstants.CONTAINER_ID_VALIDATION_LENGTH_MIN
            || tripApiJson.getContainerId().length() > ServerConstants.CONTAINER_ID_VALIDATION_LENGTH_MAX)
            throw new PADException(ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_CODE, ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_TEXT,
                "invalid container id lenght");

        if (StringUtils.isBlank(tripApiJson.getContainerType()))
            throw new PADException(ServerResponseConstants.MISSING_CONTAINER_TYPE_CODE, ServerResponseConstants.MISSING_CONTAINER_TYPE_TEXT, "missing container type");

        if (!tripApiJson.getContainerType().matches(ServerConstants.REGEX_ALPHA_NUMERIC))
            throw new PADException(ServerResponseConstants.INVALID_CONTAINER_TYPE_CODE, ServerResponseConstants.INVALID_CONTAINER_TYPE_TEXT, "invalid container type");

        if (tripApiJson.getContainerType().length() < ServerConstants.CONTAINER_TYPE_VALIDATION_LENGTH_MIN
            || tripApiJson.getContainerType().length() > ServerConstants.CONTAINER_TYPE_VALIDATION_LENGTH_MAX)
            throw new PADException(ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_CODE, ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_TEXT,
                "invalid container id lenght");

        if (StringUtils.isBlank(tripApiJson.getDateSlotFrom()))
            throw new PADException(ServerResponseConstants.MISSING_DATE_SLOT_FROM_CODE, ServerResponseConstants.MISSING_DATE_SLOT_FROM_TEXT, "missing date slot from");

        if (StringUtils.isBlank(tripApiJson.getDateSlotTo()))
            throw new PADException(ServerResponseConstants.MISSING_DATE_SLOT_TO_CODE, ServerResponseConstants.MISSING_DATE_SLOT_TO_TEXT, "missing date slot to");

        TripApiResponseJson tripApiResponseJson = tripService.addTripApi(tripApiJson, operator);

        List<TripApiResponseJson> responseList = new ArrayList<>();
        responseList.add(tripApiResponseJson);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setResponseDate(new Date());
        apiResponse.setDataList(responseList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity update(HttpServletRequest request, HttpServletResponse response, @RequestHeader(name = "authorization") String authHeader,
        @RequestBody TripApiJson tripApiJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator operator = apiService.validateApiRequest(authHeader, request.getRemoteAddr());

        if (StringUtils.isBlank(tripApiJson.getReferenceNumber()))
            throw new PADException(ServerResponseConstants.MISSING_REFERENCE_NUMBER_CODE, ServerResponseConstants.MISSING_REFERENCE_NUMBER_TEXT, "missing reference number");

        if (!tripApiJson.getReferenceNumber().matches(ServerConstants.REGEX_ALPHA_NUMERIC))
            throw new PADException(ServerResponseConstants.INVALID_REFERENCE_NUMBER_CODE, ServerResponseConstants.INVALID_REFERENCE_NUMBER_TEXT, "invalid reference number");

        if (tripApiJson.getReferenceNumber().length() < ServerConstants.REFERENCE_NUMBER_VALIDATION_LENGTH_MIN
            || tripApiJson.getReferenceNumber().length() > ServerConstants.REFERENCE_NUMBER_VALIDATION_LENGTH_MAX)
            throw new PADException(ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_CODE, ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_TEXT,
                "invalid reference number lenght");

        if (StringUtils.isNotBlank(tripApiJson.getDateSlotFrom()) && StringUtils.isBlank(tripApiJson.getDateSlotTo()))
            throw new PADException(ServerResponseConstants.MISSING_DATE_SLOT_TO_CODE, ServerResponseConstants.MISSING_DATE_SLOT_TO_TEXT, "missing date slot to");

        if (StringUtils.isNotBlank(tripApiJson.getDateSlotTo()) && StringUtils.isBlank(tripApiJson.getDateSlotFrom()))
            throw new PADException(ServerResponseConstants.MISSING_DATE_SLOT_FROM_CODE, ServerResponseConstants.MISSING_DATE_SLOT_FROM_TEXT, "missing date slot from");

        TripApiResponseJson tripApiResponseJson = tripService.updateTripApi(tripApiJson, operator);

        List<TripApiResponseJson> responseList = new ArrayList<>();
        responseList.add(tripApiResponseJson);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setResponseDate(new Date());
        apiResponse.setDataList(responseList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity cancel(HttpServletRequest request, HttpServletResponse response, @RequestHeader(name = "authorization") String authHeader,
        @RequestBody TripApiJson tripApiJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator operator = apiService.validateApiRequest(authHeader, request.getRemoteAddr());

        if (StringUtils.isBlank(tripApiJson.getReferenceNumber()))
            throw new PADException(ServerResponseConstants.MISSING_REFERENCE_NUMBER_CODE, ServerResponseConstants.MISSING_REFERENCE_NUMBER_TEXT, "missing reference number");

        if (!tripApiJson.getReferenceNumber().matches(ServerConstants.REGEX_ALPHA_NUMERIC))
            throw new PADException(ServerResponseConstants.INVALID_REFERENCE_NUMBER_CODE, ServerResponseConstants.INVALID_REFERENCE_NUMBER_TEXT, "invalid reference number");

        if (tripApiJson.getReferenceNumber().length() < ServerConstants.REFERENCE_NUMBER_VALIDATION_LENGTH_MIN
            || tripApiJson.getReferenceNumber().length() > ServerConstants.REFERENCE_NUMBER_VALIDATION_LENGTH_MAX)
            throw new PADException(ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_CODE, ServerResponseConstants.INVALID_VEHICLE_REGISTRATION_NUMBER_TEXT,
                "invalid reference number lenght");

        TripApiResponseJson tripApiResponseJson = tripService.cancelTripApi(tripApiJson, operator);

        List<TripApiResponseJson> responseList = new ArrayList<>();
        responseList.add(tripApiResponseJson);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setResponseDate(new Date());
        apiResponse.setDataList(responseList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }
}
