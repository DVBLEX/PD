package com.pad.server.anpr.aspects;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;

/**
 * PADControllerAdvice
 *
 * To convert PADException into Generic API Failure with BAD_REQUEST HTTP response code.
 * Convert PADValidationException into Generic API Failure with custom response code & text and BAD_REQUEST HTTP response code.
 * Set PADValidationException when response code/text needs to be sent back to front-end, otherwise use PADException that will log
 * response on server but only send generic response to front-end.
 */
@RestControllerAdvice
public class PADControllerAdvice {

    @ExceptionHandler({ PADException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ApiResponseJsonEntity handlePADException(PADException e) {

        ApiResponseJsonEntity result = new ApiResponseJsonEntity();
        result.setResponseCode(ServerResponseConstants.API_FAILURE_CODE);
        result.setResponseText(ServerResponseConstants.API_FAILURE_TEXT);
        result.setResponseDate(new Date());

        return result;
    }

    @ExceptionHandler({ PADValidationException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponseJsonEntity handlePADValidationException(HttpServletRequest request, PADValidationException padve) {

        ApiResponseJsonEntity result = new ApiResponseJsonEntity();
        result.setResponseCode(padve.getResponseCode());
        result.setResponseText(padve.getResponseText());
        result.setResponseDate(new Date());

        return result;
    }

    @ExceptionHandler({ Exception.class })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiResponseJsonEntity handleGenericException(Exception e) {

        ApiResponseJsonEntity result = new ApiResponseJsonEntity();
        result.setResponseCode(ServerResponseConstants.API_FAILURE_CODE);
        result.setResponseText(ServerResponseConstants.API_FAILURE_TEXT);
        result.setResponseDate(new Date());

        return result;
    }
}
