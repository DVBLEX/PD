package com.pad.server.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.services.activitylog.ActivityLogService;

public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    private static final Logger logger = Logger.getLogger(MyLogoutSuccessHandler.class);

    @Autowired
    private ActivityLogService  activityLogService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        MyUserDetails user = (MyUserDetails) authentication.getPrincipal();

        String accountTypeString = request.getQueryString();
        String redirect = "";

        if (accountTypeString.equals(ServerConstants.ACCOUNT_TYPE_OPERATOR_URL_STRING)) {
            redirect = "/pad/login.htm?" + ServerConstants.ACCOUNT_TYPE_OPERATOR_URL_STRING + "&logout";

        } else if (accountTypeString.equals(ServerConstants.ACCOUNT_TYPE_TRANSPORTER_URL_STRING)) {
            redirect = "/pad/login.htm?" + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_URL_STRING + "&logout";
        }

        StringBuilder builder = new StringBuilder();

        activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_LOG_OUT, user.getId());

        builder.append("onLogoutSuccess#accountTypeString=" + accountTypeString);

        logger.warn(builder.toString());

        response.sendRedirect(redirect);
    }
}
