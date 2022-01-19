package com.pad.server.api.aspects;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.pad.server.api.services.trip.TripService;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.entities.PortOperatorTripsApi;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.TripApiJson;
import com.pad.server.base.jsonentities.api.TripApiResponseJson;

/**
 * RestControllerAspect
 *
 * Logging the requests and responses of any RestController API method.
 *
 */
@Aspect
@Component
public class RestControllerAspect {

    private static final Logger logger                      = Logger.getLogger("RestController");

    @Autowired
    private TripService         tripService;

    private static final String REST_CONTROLLER_PACKAGE     = "com.pad.server.api.restcontrollers";
    private static final String REST_CONTROLLER_RETURN_TYPE = "com.pad.server.base.jsonentities.api.ApiResponseJsonEntity";
    private static final String POINTCUT_EXP                = "execution(public " + REST_CONTROLLER_RETURN_TYPE + " " + REST_CONTROLLER_PACKAGE + ".*.*(..))";
    private static final String POINTCUT_REF                = "rcMethodPointCut()";

    @Pointcut(POINTCUT_EXP)
    private void rcMethodPointCut() {
    }

    @Before(POINTCUT_REF)
    public void rcRequestCheck(JoinPoint joinPoint) throws PADValidationException {

        TripApiJson tripApiJson = null;
        StringBuilder builder = new StringBuilder();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        builder.append(methodSignature.toShortString());
        builder.append("###Request: ");
        builder.append(request.getRemoteAddr());
        builder.append("[");

        String[] paramNames = methodSignature.getParameterNames();
        int argLength = joinPoint.getArgs().length;

        for (int i = 0; i < argLength; i++) {

            Object arg = joinPoint.getArgs()[i];

            if (arg != null && (arg instanceof HttpServletResponse || arg instanceof HttpServletRequest)) {
            } else {

                if (paramNames != null && paramNames.length > i) {

                    builder.append(paramNames[i]).append("=");

                } else {

                    builder.append("arg").append(i).append("=");
                }

                builder.append(arg == null ? "null" : arg.toString());
                builder.append(i == (argLength - 1) ? "" : ", ");

                if (arg instanceof TripApiJson) {
                    tripApiJson = (TripApiJson) arg;
                }
            }
        }

        builder.append("]");

        logger.info(builder.toString());

        if (tripApiJson != null) {
            PortOperatorTripsApi portOperatorTripsApi = tripService.saveRequest(tripApiJson, methodSignature.getMethod().getName());

            request.setAttribute("portOperatorTripApiId", portOperatorTripsApi.getId());
        }

    }

    @AfterReturning(value = POINTCUT_REF, returning = "result")
    public void rcResponseCheck(JoinPoint joinPoint, ApiResponseJsonEntity result) {

        result.setResponseDate(new Date());

        StringBuilder builder = new StringBuilder();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        builder.append(methodSignature.toShortString());
        builder.append("###Response: ");
        builder.append(request.getRemoteAddr());
        builder.append("[");
        builder.append("responseCode=").append(result.getResponseCode()).append(", ");
        builder.append("responseText=").append(result.getResponseText()).append(", ");
        if (result.getData() != null) {
            builder.append("data=").append(result.getData().toString()).append(", ");
        }
        if (result.getDataList() != null) {
            builder.append("dataList=").append(result.getDataList().size());
            if (result.getDataList().size() == 1) {
                builder.append("-").append(result.getDataList().get(0).toString());
            }
            builder.append(", ");
        }
        if (result.getDataMap() != null) {
            builder.append("dataMap=").append(result.getDataMap().size()).append(", ");
        }
        builder.append("responseDate=").append(result.getResponseDate());
        builder.append("]");

        logger.info(builder.toString());

        if (request.getAttribute("portOperatorTripApiId") != null) {

            long portOperatorTripApiId = (long) request.getAttribute("portOperatorTripApiId");

            TripApiResponseJson tripApiResponseJson = (TripApiResponseJson) result.getDataList().get(0);

            tripService.saveResponse(portOperatorTripApiId, result.getResponseCode(), result.getResponseText(), tripApiResponseJson.getCode());
        }

    }

    @AfterThrowing(value = POINTCUT_REF, throwing = "e")
    public void rcExceptionCheck(JoinPoint joinPoint, Exception e) {

        StringBuilder builder = new StringBuilder();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        builder.append(methodSignature.toShortString());
        builder.append("###Request: ");
        builder.append(request.getRemoteAddr());
        builder.append("###Exception:[");
        builder.append(e.toString());
        builder.append("]");

        logger.error(builder.toString());

        if (request.getAttribute("portOperatorTripApiId") != null) {

            int responseCode = ServerConstants.DEFAULT_INT;
            String responseText = ServerConstants.DEFAULT_STRING;

            if (e instanceof PADException) {
                responseCode = ((PADException) e).getResponseCode();
                responseText = ((PADException) e).getResponseText();

            } else if (e instanceof PADValidationException) {
                responseCode = ((PADValidationException) e).getResponseCode();
                responseText = ((PADValidationException) e).getResponseText();
            } else {
                responseCode = ServerResponseConstants.API_FAILURE_CODE;
                responseText = ServerResponseConstants.API_FAILURE_TEXT;
            }

            long portOperatorTripApiId = (long) request.getAttribute("portOperatorTripApiId");

            tripService.saveResponse(portOperatorTripApiId, responseCode, responseText, ServerConstants.DEFAULT_STRING);
        }

    }
}
