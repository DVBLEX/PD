package com.pad.server.web.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.operator.OperatorService;

public class MySuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = Logger.getLogger(MySuccessHandler.class);

    @Autowired
    private OperatorService     operatorService;

    @Autowired
    private ActivityLogService  activityLogService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication)
        throws IOException, ServletException {

        MyUserDetails user = (MyUserDetails) authentication.getPrincipal();

        String redirect = "";

        if (user.getAccountStatus() == ServerConstants.ACCOUNT_STATUS_DENIED) {
            int accountType = Integer.parseInt(httpServletRequest.getParameter(MyLoginParameterFilter.PARAMETER_NAME_ACCOUNT_TYPE));

            if (accountType == ServerConstants.ACCOUNT_TYPE_OPERATOR) {
                redirect = "/pad/login.htm?" + ServerConstants.ACCOUNT_TYPE_OPERATOR_URL_STRING + "&accountDenied";
            } else {
                redirect = "/pad/login.htm?" + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_URL_STRING + "&accountDenied";
            }

        } else {

            try {
                Operator operator = operatorService.getOperator(user.getUsername());
                operator.setLoginFailureCount(0);
                operator.setDateLastLogin(new Date());
                operator.setDateLastAttempt(new Date());
                operator.setIsLocked(false);
                operator.setIsCredentialsExpired(false);

                operatorService.updateOperator(operator);

                activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_LOG_IN, operator.getId());

                logger.info("onAuthenticationSuccess#username=" + user.getUsername());

                redirect = "index.htm";

            } catch (Exception e) {

                logger.error("onAuthenticationSuccess#username=" + user.getUsername() + "###Exception: " + e.getMessage());
                redirect = "errorPage.htm";
            }
        }

        httpServletResponse.sendRedirect(redirect);
    }
}
