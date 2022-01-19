package com.pad.server.api.restcontrollers;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.api.services.api.ApiService;
import com.pad.server.api.services.counter.vehicle.VehicleCounterService;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.counter.VehicleCounterRequestJson;
import com.pad.server.base.services.email.EmailService;

@RestController
@RequestMapping("/counter/vehicle")
public class VehicleCounterRESTController {

    @Autowired
    private ApiService            apiService;

    @Autowired
    private EmailService          emailService;

    @Autowired
    private VehicleCounterService vehicleCounterService;

    @RequestMapping(value = "/log", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity logVehicleCount(@Context HttpServletRequest request, HttpServletResponse response, @RequestHeader(name = "authorization") String authHeader,
        @RequestBody VehicleCounterRequestJson vehicleCounterRequestJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        apiService.validateApiRequest(authHeader, request.getRemoteAddr());

        try {
            if (vehicleCounterRequestJson == null)
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                    "vehicleCounterRequestJson is missing");

            if (StringUtils.isBlank(vehicleCounterRequestJson.getDeviceId()))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "deviceId is missing.");

            if (vehicleCounterRequestJson.getEvents() == null || vehicleCounterRequestJson.getEvents().size() <= ServerConstants.ZERO_INT)
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "events are missing.");

            vehicleCounterService.log(vehicleCounterRequestJson);

            apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
            apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
            apiResponse.setResponseDate(new Date());

            response.setStatus(HttpServletResponse.SC_OK);

        } catch (PADException pade) {
            emailService.sendSystemEmail("logVehicleCount " + pade.getClass().getSimpleName(), EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "LaneRESTController#logVehicleCount###" + pade.getClass().getSimpleName() + ":<br />Cause: " + pade.getCause() + "<br />Message: " + pade.getMessage()
                    + "<br />Stacktrace: " + pade.getStackTrace());

            throw pade;

        } catch (Exception e) {
            emailService.sendSystemEmail("logVehicleCount " + e.getClass().getSimpleName(), EmailService.EMAIL_TYPE_EXCEPTION, null, null, "LaneRESTController#logVehicleCount###"
                + e.getClass().getSimpleName() + ":<br />Cause: " + e.getCause() + "<br />Message: " + e.getMessage() + "<br />Stacktrace: " + e.getStackTrace());

            throw e;
        }

        return apiResponse;
    }

}
