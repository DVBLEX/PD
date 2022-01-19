package com.pad.server.api.aspects;

import java.util.Date;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;

@RestControllerAdvice
public class PADControllerAdvice {

    @ExceptionHandler({ PADException.class })
    protected ApiResponseJsonEntity handlePADException(PADException e) {

        ApiResponseJsonEntity result = new ApiResponseJsonEntity();
        result.setResponseCode(e.getResponseCode());
        result.setResponseText(e.getResponseText());
        result.setResponseDate(new Date());

        return result;
    }

    @ExceptionHandler({ PADValidationException.class })
    protected ApiResponseJsonEntity handlePADValidationException(PADValidationException e) {

        ApiResponseJsonEntity result = new ApiResponseJsonEntity();
        result.setResponseCode(e.getResponseCode());
        result.setResponseText(e.getResponseText());
        result.setResponseDate(new Date());

        return result;
    }

    @ExceptionHandler({ Exception.class })
    protected ApiResponseJsonEntity handleGenericException(Exception e) {

        ApiResponseJsonEntity result = new ApiResponseJsonEntity();
        result.setResponseCode(ServerResponseConstants.API_FAILURE_CODE);
        result.setResponseText(ServerResponseConstants.API_FAILURE_TEXT);
        result.setResponseDate(new Date());

        return result;
    }
}
