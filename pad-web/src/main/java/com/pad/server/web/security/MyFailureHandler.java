package com.pad.server.web.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.system.SystemService;

public class MyFailureHandler implements AuthenticationFailureHandler {

    private static final Logger logger = Logger.getLogger(MyFailureHandler.class);

    @Autowired
    private OperatorService     operatorService;

    @Autowired
    private SystemService       systemService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        int accountType = ServerConstants.DEFAULT_INT;

        try {
            accountType = Integer.parseInt(request.getParameter(MyLoginParameterFilter.PARAMETER_NAME_ACCOUNT_TYPE));

        } catch (NumberFormatException nfe) {
            throw new UsernameNotFoundException("Invalid account type provided");
        }

        String userName = request.getParameter("input1");
        String redirect = "";

        if (accountType == ServerConstants.ACCOUNT_TYPE_COMPANY) {
            redirect = "/pad/login.htm?" + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_URL_STRING + "&failure&a=" + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_COMPANY_URL_STRING;

        } else if (accountType == ServerConstants.ACCOUNT_TYPE_INDIVIDUAL) {
            redirect = "/pad/login.htm?" + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_URL_STRING + "&failure&a=" + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_INDIVIDUAL_URL_STRING;

            try {
                userName = ServerUtil.getValidNumber(userName, "onAuthenticationFailure");

            } catch (PADException | PADValidationException pade) {
                throw new UsernameNotFoundException("Invalid username provided");
            }

        } else if (accountType == ServerConstants.ACCOUNT_TYPE_OPERATOR) {
            redirect = "/pad/login.htm?" + ServerConstants.ACCOUNT_TYPE_OPERATOR_URL_STRING + "&failure";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("onAuthenticationFailure#accountType=" + accountType + ", username=" + userName);

        try {
            Operator operator = operatorService.getOperator(userName);

            if (operator != null) {

                operator.setDateLastAttempt(new Date());

                if (exception instanceof CredentialsExpiredException) {

                    operator.setIsCredentialsExpired(true);

                    String token1 = SecurityUtil.generateDateBasedToken1(userName, operator.getDateLastPassword());
                    String token2 = SecurityUtil.generateDateBasedToken2(userName, operator.getDateLastPassword());

                    if (accountType == ServerConstants.ACCOUNT_TYPE_COMPANY || accountType == ServerConstants.ACCOUNT_TYPE_INDIVIDUAL) {
                        redirect = "/pad/credentialsExpired.htm?" + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_URL_STRING + "&a="
                            + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_URL_STRING + "&u=" + userName + "&t=" + token1 + "&t2=" + token2;

                    } else if (accountType == ServerConstants.ACCOUNT_TYPE_OPERATOR) {
                        redirect = "/pad/credentialsExpired.htm?" + ServerConstants.ACCOUNT_TYPE_OPERATOR_URL_STRING + "&a=" + ServerConstants.ACCOUNT_TYPE_OPERATOR_URL_STRING
                            + "&u=" + userName + "&t=" + token1 + "&t2=" + token2;
                    }

                    builder.append("[credentials expired]");

                } else if (exception instanceof DisabledException) {
                    operator.setLoginFailureCount(operator.getLoginFailureCount() + 1);

                    if (accountType == ServerConstants.ACCOUNT_TYPE_COMPANY) {
                        redirect = "/pad/login.htm?" + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_URL_STRING + "&disabled&a="
                            + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_COMPANY_URL_STRING;

                    } else if (accountType == ServerConstants.ACCOUNT_TYPE_INDIVIDUAL) {
                        redirect = "/pad/login.htm?" + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_URL_STRING + "&disabled&a="
                            + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_INDIVIDUAL_URL_STRING;

                    } else if (accountType == ServerConstants.ACCOUNT_TYPE_OPERATOR) {
                        redirect = "/pad/login.htm?" + ServerConstants.ACCOUNT_TYPE_OPERATOR_URL_STRING + "&disabled";
                    }

                    builder.append("[disabled]");

                } else if (operator.getIsLocked()) {
                    operator.setLoginFailureCount(operator.getLoginFailureCount() + 1);

                    if (accountType == ServerConstants.ACCOUNT_TYPE_COMPANY) {
                        redirect = "/pad/login.htm?" + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_URL_STRING + "&locked&a="
                            + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_COMPANY_URL_STRING;

                    } else if (accountType == ServerConstants.ACCOUNT_TYPE_INDIVIDUAL) {
                        redirect = "/pad/login.htm?" + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_URL_STRING + "&locked&a="
                            + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_INDIVIDUAL_URL_STRING;

                    } else if (accountType == ServerConstants.ACCOUNT_TYPE_OPERATOR) {
                        redirect = "/pad/login.htm?" + ServerConstants.ACCOUNT_TYPE_OPERATOR_URL_STRING + "&locked";
                    }

                    builder.append("[locked]");

                } else if (operator.getLoginFailureCount() >= systemService.getSystemParameter().getLoginLockCountFailed()) {
                    operator.setIsLocked(true);
                    operator.setLoginFailureCount(operator.getLoginFailureCount() + 1);
                    operator.setDateLocked(new Date());

                    if (accountType == ServerConstants.ACCOUNT_TYPE_COMPANY) {
                        redirect = "/pad/login.htm?" + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_URL_STRING + "&locked&a="
                            + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_COMPANY_URL_STRING;

                    } else if (accountType == ServerConstants.ACCOUNT_TYPE_INDIVIDUAL) {
                        redirect = "/pad/login.htm?" + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_URL_STRING + "&locked&a="
                            + ServerConstants.ACCOUNT_TYPE_TRANSPORTER_INDIVIDUAL_URL_STRING;

                    } else if (accountType == ServerConstants.ACCOUNT_TYPE_OPERATOR) {
                        redirect = "/pad/login.htm?" + ServerConstants.ACCOUNT_TYPE_OPERATOR_URL_STRING + "&locked";
                    }

                    builder.append("[lock]");
                } else {
                    operator.setLoginFailureCount(operator.getLoginFailureCount() + 1);
                    builder.append("[bad credentials]");
                }

                operatorService.updateOperator(operator);
            }

            logger.warn(builder.toString());

        } catch (Exception e) {

            logger.error("onAuthenticationFailure#username=" + userName + "###Exception: " + e.getMessage());
        }

        response.sendRedirect(redirect);
    }
}
