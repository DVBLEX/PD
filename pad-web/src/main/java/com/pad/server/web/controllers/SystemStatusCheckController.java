package com.pad.server.web.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.exceptions.PADException;

@Controller
public class SystemStatusCheckController {

    private static final Logger logger = Logger.getLogger(SystemStatusCheckController.class);

    @Value("${system.check.client.id}")
    private String              systemCheckClientId;

    @Value("${system.check.client.secret}")
    private String              systemCheckClientSecret;

    @Value("${system.check.referer.header}")
    private String              systemCheckRefererHeader;

    @Value("${system.check.user.agent.header}")
    private String              systemCheckUserAgentHeader;

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @RequestMapping(value = "/system/check.htm", method = RequestMethod.POST)
    public @ResponseBody String processCheckRequest(HttpServletRequest request, @RequestParam("actions") String actions) throws Exception {

        String logName = "CheckController#";

        String userAgentHeader = request.getHeader("User-Agent");
        String refererHeader = request.getHeader("Referer");
        String authHeader = request.getHeader("Auth");

        StringBuilder builder = new StringBuilder();
        builder.append(logName);
        builder.append("Request: ");
        builder.append(request.getRemoteAddr());
        builder.append("#");
        builder.append("[actions=");
        builder.append(actions);
        builder.append("]# headers");
        builder.append("[authorization=");
        builder.append(request.getHeader("Auth"));
        builder.append(", refererHeader=");
        builder.append(refererHeader);
        builder.append(", userAgent=");
        builder.append(userAgentHeader);
        builder.append("]");
        logger.info(builder.toString());

        final StringBuilder response = new StringBuilder();
        try {
            if (StringUtils.isBlank(userAgentHeader))
                throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "#1");

            if (StringUtils.isBlank(refererHeader))
                throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "#2");

            if (StringUtils.isBlank(authHeader) || authHeader.length() < 8)
                throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "#3");

            if (!userAgentHeader.equals(systemCheckUserAgentHeader))
                throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "#4");

            if (!refererHeader.equals(systemCheckRefererHeader))
                throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "#5");

            authHeader = authHeader.substring(6, authHeader.length());

            authHeader = new String(Base64.decode(authHeader.getBytes("UTF-8")), "UTF-8");

            if (authHeader.indexOf(":") == -1 || authHeader.length() <= authHeader.indexOf(":"))
                throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "#6");

            String requestClientId = authHeader.substring(0, authHeader.indexOf(":"));
            String requestClientSecret = authHeader.substring(authHeader.indexOf(":") + 1);

            if (StringUtils.isBlank(requestClientId))
                throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "#7");

            if (StringUtils.isBlank(requestClientSecret))
                throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "#8");

            if (!requestClientId.equals(systemCheckClientId))
                throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "#9");

            if (!requestClientSecret.equals(systemCheckClientSecret))
                throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "#10");

            if (!actions.matches(ServerConstants.REGEX_ACTIONS))
                throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "#11");

            final List<Boolean> isAlertList = new ArrayList<>();
            isAlertList.add(0, false);
            try {
                jdbcTemplate.query("SELECT id, comparison_result FROM system_checks WHERE id IN (" + actions + ")", new RowCallbackHandler() {

                    @Override
                    public void processRow(ResultSet rs) throws SQLException {

                        isAlertList.set(0, (isAlertList.get(0) || rs.getBoolean("comparison_result")));
                    }
                });
            } catch (Exception e) {
                logger.error(logName + "##Exception[query]: ", e);
                isAlertList.set(0, true);
            }

            if (isAlertList.get(0)) {
                response.append("ResponseCode=");
                response.append(ServerResponseConstants.FAILURE_CODE);
                response.append(", ResponseText=");
                response.append(ServerResponseConstants.FAILURE_TEXT);
            } else {
                response.append("ResponseCode=");
                response.append(ServerResponseConstants.SUCCESS_CODE);
                response.append(", ResponseText=");
                response.append(ServerResponseConstants.SUCCESS_TEXT);
            }
        } catch (PADException pade) {
            response.append(", ResponseCode=");
            response.append(pade.getResponseCode());
            response.append(", ResponseText=");
            response.append(pade.getResponseText());
            response.append(", ResponseSource=");
            response.append(pade.getResponseSource());

        } catch (Exception e) {
            logger.error(logName + "##Exception: ", e);

            response.append(", ResponseCode=");
            response.append(ServerResponseConstants.API_FAILURE_CODE);
            response.append(", ResponseText=");
            response.append(ServerResponseConstants.API_FAILURE_TEXT);
        }

        builder = new StringBuilder();
        builder.append(logName);
        builder.append("Response: ");
        builder.append("[actions=");
        builder.append(actions);
        builder.append(", ");
        builder.append(response);
        builder.append("]");
        logger.info(builder.toString());

        return response.toString();
    }
}
