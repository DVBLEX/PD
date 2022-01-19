package com.pad.server.anpr.restcontrollers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.anpr.services.anpr.AnprService;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.entities.Anpr;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;

@RestController
@RequestMapping("/parkpermission")
public class ParkPermissionRESTController {

    private static final Logger logger           = Logger.getLogger(ParkPermissionRESTController.class);
    private static final long   DELAY_IN_SECONDS = 7;
    private static final String ALLOWED_SUBNET   = "10.11.100.0/24";

    @Autowired
    private AnprService         anprService;

    @RequestMapping(value = "/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity deletePermissions(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "parkingPermissionIdStart") long permissionIdStart, @RequestParam(value = "parkingPermissionIdEnd") long permissionIdEnd) throws PADException {
        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (isRemoteAddressAllowed(request.getRemoteAddr())) {
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            Anpr anpr = new Anpr();
            for (long permissionId = permissionIdStart; permissionId <= permissionIdEnd; permissionId++) {
                anpr.setParkingPermissionId(permissionId);
                executorService.schedule(() -> {
                    try {
                        anprService.deleteWhiteList(anpr);
                    } catch (PADException e) {
                        logger.error("PADException: ", e);
                    }
                }, DELAY_IN_SECONDS, TimeUnit.SECONDS);
            }
        } else {
            throw new PADException(ServerResponseConstants.ACCESS_DENIED_CODE, ServerResponseConstants.ACCESS_DENIED_TEXT, "Remote IP address is not allowed.");
        }

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    private boolean isRemoteAddressAllowed(String ip) {
        IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(ALLOWED_SUBNET);
        return ipAddressMatcher.matches(ip);
    }
}
