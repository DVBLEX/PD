package com.pad.server.base.services.recaptcha;

import java.net.URI;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.recaptcha.GoogleResponse;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    private static final Logger     logger           = Logger.getLogger(CaptchaServiceImpl.class);

    @Autowired
    private HttpServletRequest      request;

    @Autowired
    private ReCaptchaAttemptService reCaptchaAttemptService;

    @Autowired
    private RestOperations          restTemplate;

    @Value("${google.recaptcha.key.site}")
    private String                  site;

    @Value("${google.recaptcha.key.secret}")
    private String                  secret;

    @Value("${google.recaptcha.verify.url}")
    private String                  verify_url;

    private static final Pattern    response_pattern = Pattern.compile("[A-Za-z0-9_-]+");

    @Override
    public void processResponse(String response) throws PADException {

        if (!responseSanityCheck(response))
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "Captcha response contains invalid characters.");

        if (reCaptchaAttemptService.isBlocked(getClientIP()))
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT,
                "Account exceeded the maximum number of captcha failed attempts.");

        final URI verifyUri = URI.create(String.format(verify_url, getReCaptchaSecret(), response, getClientIP()));
        try {
            final GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);
            logger.info("Google's response: {} " + googleResponse.toString());

            if (!googleResponse.getSuccess()) {
                if (googleResponse.hasClientError()) {
                    reCaptchaAttemptService.reCaptchaFailed(getClientIP());
                }
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "Captcha was not successfully validated.");
            }
        } catch (RestClientException rce) {
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "ReCaptcha service unavailable.");
        }
        reCaptchaAttemptService.reCaptchaSucceeded(getClientIP());
    }

    private boolean responseSanityCheck(String response) {
        return StringUtils.isNotBlank(response) && response_pattern.matcher(response).matches();
    }

    @Override
    public String getReCaptchaSite() {
        return site;
    }

    @Override
    public String getReCaptchaSecret() {
        return secret;
    }

    private String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null)
            return request.getRemoteAddr();
        return xfHeader.split(",")[0];
    }
}
