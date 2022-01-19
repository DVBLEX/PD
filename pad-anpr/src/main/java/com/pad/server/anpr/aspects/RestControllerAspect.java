package com.pad.server.anpr.aspects;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;

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

    private static final String REST_CONTROLLER_PACKAGE     = "com.pad.server.anpr.restcontrollers";
    private static final String REST_CONTROLLER_RETURN_TYPE = "com.pad.server.base.jsonentities.api.ApiResponseJsonEntity";
    private static final String POINTCUT_EXP                = "execution(public " + REST_CONTROLLER_RETURN_TYPE + " " + REST_CONTROLLER_PACKAGE + ".*.*(..))";
    private static final String POINTCUT_REF                = "rcMethodPointCut()";

    @Pointcut(POINTCUT_EXP)
    private void rcMethodPointCut() {
    }

    @Before(POINTCUT_REF)
    public void rcRequestCheck(JoinPoint joinPoint) throws PADValidationException {

        StringBuilder builder = new StringBuilder();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        builder.append(methodSignature.toShortString());
        builder.append("###loggedOperator=").append(getLoggedOperator());
        builder.append("###Request:[");

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
            }
        }

        builder.append("]");

        logger.info(builder.toString());
    }

    @AfterReturning(value = POINTCUT_REF, returning = "result")
    public void rcResponseCheck(JoinPoint joinPoint, ApiResponseJsonEntity result) {

        result.setResponseDate(new Date());

        StringBuilder builder = new StringBuilder();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        builder.append(methodSignature.toShortString());
        builder.append("###loggedOperator=").append(getLoggedOperator());
        builder.append("###Response:[");
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
    }

    @AfterThrowing(value = POINTCUT_REF, throwing = "e")
    public void rcExceptionCheck(JoinPoint joinPoint, Exception e) {

        StringBuilder builder = new StringBuilder();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        builder.append(methodSignature.toShortString());
        builder.append("###loggedOperator=").append(getLoggedOperator());
        builder.append("###Exception:[");
        builder.append(e.toString());
        builder.append("]");

        logger.error(builder.toString());
    }

    private String getLoggedOperator() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication == null)
            return "NULL_AUTH";
        else if (!authentication.isAuthenticated())
            return "NO_AUTH";
        else
            return authentication.getName();
    }
}
