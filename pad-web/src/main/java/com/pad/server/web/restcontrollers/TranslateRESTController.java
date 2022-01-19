package com.pad.server.web.restcontrollers;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.services.translation.TranslationService;

@RestController
@RequestMapping("/translate")
public class TranslateRESTController {

    @Autowired
    private TranslationService translationService;

    @RequestMapping(value = "/get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    public ApiResponseJsonEntity getTranslation(HttpServletResponse response) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Map<String, Object> languageMap = translationService.getTranslationsMap();

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataMap(languageMap);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }
}
