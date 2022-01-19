package com.pad.server.web.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.services.operator.OperatorService;

@Controller
public class LoginController {

    private static final Logger logger      = Logger.getLogger(LoginController.class);

    private static final ZoneId currentZone = ZoneId.systemDefault();

    @Value("${system.environment}")
    private String              environment;

    @Value("${google.recaptcha.key.site}")
    private String              recaptchKeySite;

    @Value("${transporter.manual.file.name}")
    private String              transporterManualFileName;

    @Value("${transporter.manual.directory}")
    private String              transporterManualDirectory;

    @Autowired
    private OperatorService     operatorService;

    @RequestMapping(value = "/login.htm", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "failure", required = false) String error, @RequestParam(value = "logout", required = false) String logout,
        @RequestParam(value = "denied", required = false) String denied, @RequestParam(value = "locked", required = false) String locked,
        @RequestParam(value = "disabled", required = false) String disabled, @RequestParam(value = "accountDenied", required = false) String accountDenied,
        @RequestParam(value = ServerConstants.ACCOUNT_TYPE_TRANSPORTER_URL_STRING, required = false) String transporter,
        @RequestParam(value = ServerConstants.ACCOUNT_TYPE_OPERATOR_URL_STRING, required = false) String operator,
        @RequestParam(value = "a", required = false) String accountType) {

        ModelAndView mav = new ModelAndView();

        if (error != null) {
            mav.addObject("failure", "failure");
        }

        if (logout != null) {
            mav.addObject("logout", "logout");
        }

        if (denied != null) {
            mav.addObject("denied", "denied");
        }

        if (locked != null) {
            mav.addObject("locked", "locked");
        }

        if (disabled != null) {
            mav.addObject("disabled", "disabled");
        }

        if (accountDenied != null) {
            mav.addObject("accountDenied", "accountDenied");
        }

        if (environment.equals(ServerConstants.SYSTEM_ENVIRONMENT_LOCAL) || environment.equals(ServerConstants.SYSTEM_ENVIRONMENT_DEV)) {
            mav.addObject("isTestEnvironment", true);
        } else {
            mav.addObject("isTestEnvironment", false);
        }

        if (transporter != null) {
            mav.addObject(ServerConstants.ACCOUNT_TYPE_TRANSPORTER_URL_STRING, true);
            mav.addObject("accountType", accountType);

        } else if (operator != null) {
            mav.addObject(ServerConstants.ACCOUNT_TYPE_OPERATOR_URL_STRING, true);

        } else {
            // if user types /login.htm URL in browser, just redirect to transporter login
            mav.addObject(ServerConstants.ACCOUNT_TYPE_TRANSPORTER_URL_STRING, true);
        }

        mav.setViewName("login");

        return mav;
    }

    @RequestMapping(value = "/denied.htm", method = RequestMethod.GET)
    public void denied(HttpServletResponse httpServletResponse) throws IOException {

        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpServletResponse.sendRedirect("login.htm?denied");
    }

    @RequestMapping(value = "/passwordForgot.htm", method = RequestMethod.GET)
    public ModelAndView passwordForgot(HttpServletRequest request, @RequestParam(value = "a") String accountType) {

        String responseSource = "passwordForgot#";
        responseSource = responseSource + request.getRemoteAddr();

        StringBuilder builder = new StringBuilder();
        builder.append(responseSource);
        builder.append("#Request: ");
        builder.append("[accountType=");
        builder.append(accountType);
        builder.append("]");
        logger.info(builder.toString());

        if (!accountType.equals(ServerConstants.ACCOUNT_TYPE_TRANSPORTER_COMPANY_URL_STRING) && !accountType.equals(ServerConstants.ACCOUNT_TYPE_TRANSPORTER_INDIVIDUAL_URL_STRING)
            && !accountType.equals(ServerConstants.ACCOUNT_TYPE_OPERATOR_URL_STRING))
            return new ModelAndView("redirect:login.htm");

        ModelAndView mav = new ModelAndView();
        mav.addObject("recaptchaKey", "'" + recaptchKeySite + "'");

        if (environment.equals(ServerConstants.SYSTEM_ENVIRONMENT_LOCAL) || environment.equals(ServerConstants.SYSTEM_ENVIRONMENT_DEV)) {
            mav.addObject("isTestEnvironment", true);
        } else {
            mav.addObject("isTestEnvironment", false);
        }

        mav.addObject("accountType", accountType);

        mav.setViewName("passwordForgot");
        return mav;
    }

    @RequestMapping(value = "/passwordForgotChange.htm", method = RequestMethod.GET)
    public ModelAndView passwordForgotChange(HttpServletRequest request, @RequestParam(value = "u") String username, @RequestParam(value = "a") String accountType,
        @RequestParam(value = "t") String token) {

        String responseSource = "passwordForgotChange#";
        responseSource = responseSource + request.getRemoteAddr();

        StringBuilder builder = new StringBuilder();
        builder.append(responseSource);
        builder.append("#Request: ");
        builder.append("[username=");
        builder.append(username);
        builder.append(", accountType=");
        builder.append(accountType);
        builder.append("]");
        logger.info(builder.toString());

        if (!accountType.equals(ServerConstants.ACCOUNT_TYPE_TRANSPORTER_COMPANY_URL_STRING) && !accountType.equals(ServerConstants.ACCOUNT_TYPE_TRANSPORTER_INDIVIDUAL_URL_STRING))
            return new ModelAndView("redirect:login.htm?denied");

        else if (accountType.equals(ServerConstants.ACCOUNT_TYPE_TRANSPORTER_COMPANY_URL_STRING)
            && (!username.matches(ServerConstants.REGEX_EMAIL) || !token.matches(ServerConstants.REGEX_SHA256)))
            // The details are invalid. It might be a hacking attempt.
            return new ModelAndView("redirect:login.htm?denied");

        else if (accountType.equals(ServerConstants.ACCOUNT_TYPE_TRANSPORTER_INDIVIDUAL_URL_STRING)
            && (!username.matches(ServerConstants.REGEX_MSISDN) || !token.matches(ServerConstants.REGEX_SHA256_SHORTENED)))
            // The details are invalid. It might be a hacking attempt.
            return new ModelAndView("redirect:login.htm?denied");

        ModelAndView mav = new ModelAndView();
        try {

            Operator operator = operatorService.getOperator(username);

            if (operator != null) {

                LocalDateTime dateLastPasswordForgotRequest = LocalDateTime.ofInstant(operator.getDateLastPasswdForgotRequest().toInstant(), currentZone);
                LocalDateTime datePasswordForgotRequestPlusValidMinutes = dateLastPasswordForgotRequest.plusMinutes(ServerConstants.FORGOT_PASSWORD_LINK_VALID_MINUTES);
                LocalDateTime now = LocalDateTime.ofInstant(new Date().toInstant(), currentZone);

                if (now.isBefore(datePasswordForgotRequestPlusValidMinutes)) {

                    String tokenCheck = SecurityUtil.generateDateBasedToken1(username, operator.getDateLastPasswdForgotRequest());

                    if (accountType.equals(ServerConstants.ACCOUNT_TYPE_TRANSPORTER_COMPANY_URL_STRING) && token.equals(tokenCheck)
                        || accountType.equals(ServerConstants.ACCOUNT_TYPE_TRANSPORTER_INDIVIDUAL_URL_STRING) && token.equals(tokenCheck.substring(0, 25))) {

                        mav.addObject("accountType", "'" + accountType + "'");
                        mav.addObject("recaptchaKey", "'" + recaptchKeySite + "'");
                        mav.addObject("userName", "'" + username + "'");
                        mav.addObject("key", "'" + token + "'");

                        if (environment.equals(ServerConstants.SYSTEM_ENVIRONMENT_LOCAL) || environment.equals(ServerConstants.SYSTEM_ENVIRONMENT_DEV)) {
                            mav.addObject("isTestEnvironment", true);
                        } else {
                            mav.addObject("isTestEnvironment", false);
                        }

                        mav.setViewName("passwordForgotChange");
                        return mav;
                    }
                }
            }
        } catch (Exception e) {
        }

        mav.setViewName("errorPage");
        return mav;
    }

    // the password reset page for transporters
    @RequestMapping(value = "/passwordSetUp.htm", method = RequestMethod.GET)
    public ModelAndView passwordSetUp(HttpServletRequest request, @RequestParam(value = "u") String username, @RequestParam(value = "t") String token) {

        String responseSource = "passwordSetUp#";
        responseSource = responseSource + request.getRemoteAddr();

        StringBuilder builder = new StringBuilder();
        builder.append(responseSource);
        builder.append("#Request: ");
        builder.append("[username=");
        builder.append(username);
        builder.append("]");
        logger.info(builder.toString());

        ModelAndView mav = new ModelAndView();

        try {
            Operator operator = operatorService.getOperator(username);

            if (operator != null) {

                LocalDateTime datePasswordSetUp = LocalDateTime.ofInstant(operator.getDateLastPasswdSetUp().toInstant(), currentZone);
                LocalDateTime datePasswordSetUpPlusValidHours = datePasswordSetUp.plusHours(ServerConstants.SET_UP_PASSWORD_LINK_VALID_HOURS);
                LocalDateTime now = LocalDateTime.ofInstant(new Date().toInstant(), currentZone);

                if (now.isBefore(datePasswordSetUpPlusValidHours)) {

                    String tokenCheck = SecurityUtil.generateDateBasedToken1(username, operator.getDateLastPasswdSetUp());

                    if (token.matches(ServerConstants.REGEX_SHA256_SHORTENED)) {
                        tokenCheck = tokenCheck.substring(0, 25);
                    }

                    if (token.equals(tokenCheck)) {

                        mav.addObject("userName", "'" + username + "'");
                        mav.addObject("key", "'" + token + "'");
                        mav.addObject("loginType", operator.getAccountId() == ServerConstants.DEFAULT_LONG ? "op" : "tp");

                        if (environment.equals(ServerConstants.SYSTEM_ENVIRONMENT_LOCAL) || environment.equals(ServerConstants.SYSTEM_ENVIRONMENT_DEV)) {
                            mav.addObject("isTestEnvironment", true);
                        } else {
                            mav.addObject("isTestEnvironment", false);
                        }

                        mav.setViewName("passwordSetUp");
                        return mav;
                    }
                }
            }

        } catch (Exception e) {
        }

        mav.setViewName("errorPage");
        return mav;
    }

    // the password reset page for operators
    @RequestMapping(value = "/operatorPasswordSetUp.htm", method = RequestMethod.GET)
    public ModelAndView operatorPasswordSetUp(HttpServletRequest request, @RequestParam(value = "u") String username) {

        String responseSource = "passwordSetUp#";
        responseSource = responseSource + request.getRemoteAddr();

        StringBuilder builder = new StringBuilder();
        builder.append(responseSource);
        builder.append("#Request: ");
        builder.append("[username=");
        builder.append(username);
        builder.append("]");
        logger.info(builder.toString());

        ModelAndView mav = new ModelAndView();

        try {
            username = username.trim();

            Operator operator = operatorService.getOperator(username);

            if (operator != null) {

                if (environment.equals(ServerConstants.SYSTEM_ENVIRONMENT_LOCAL) || environment.equals(ServerConstants.SYSTEM_ENVIRONMENT_DEV)) {
                    mav.addObject("isTestEnvironment", true);
                } else {
                    mav.addObject("isTestEnvironment", false);
                }

                mav.addObject("userName", "'" + username + "'");
                mav.setViewName("operatorPasswordSetUp");

                return mav;
            }

        } catch (Exception e) {
        }

        mav.setViewName("errorPage");
        return mav;
    }

    @RequestMapping(value = "/registration.htm", method = RequestMethod.GET)
    public ModelAndView register() {

        ModelAndView mav = new ModelAndView();
        mav.addObject("recaptchaKey", "'" + recaptchKeySite + "'");

        if (environment.equals(ServerConstants.SYSTEM_ENVIRONMENT_LOCAL) || environment.equals(ServerConstants.SYSTEM_ENVIRONMENT_DEV)) {
            mav.addObject("isTestEnvironment", true);
        } else {
            mav.addObject("isTestEnvironment", false);
        }

        mav.setViewName("registration");

        return mav;
    }

    @RequestMapping(value = "/credentialsExpired.htm", method = RequestMethod.GET)
    public ModelAndView credentialsExpired(@RequestParam(value = "a", required = true) String accountType, @RequestParam(value = "u", required = true) String username,
        @RequestParam(value = "t") String token1, @RequestParam(value = "t2") String token2) {

        String responseSource = "credentialsExpired#";

        StringBuilder builder = new StringBuilder();
        builder.append(responseSource);
        builder.append("#Request: ");
        builder.append("[username=");
        builder.append(username);
        builder.append(", accountType=");
        builder.append(accountType);
        builder.append(", token1=");
        builder.append(token1);
        builder.append(", token2=");
        builder.append(token2);
        builder.append("]");
        logger.info(builder.toString());

        if (!token1.matches(ServerConstants.REGEX_SHA256) || !token2.matches(ServerConstants.REGEX_SHA256))
            // The details are invalid. It might be a hacking attempt.
            return new ModelAndView("redirect:login.htm?denied");

        else if (!accountType.equals(ServerConstants.ACCOUNT_TYPE_TRANSPORTER_URL_STRING) && !accountType.equals(ServerConstants.ACCOUNT_TYPE_OPERATOR_URL_STRING))
            return new ModelAndView("redirect:login.htm?denied");

        else if (accountType.equals(ServerConstants.ACCOUNT_TYPE_TRANSPORTER_URL_STRING) && !username.matches(ServerConstants.REGEX_EMAIL)
            && !username.matches(ServerConstants.REGEX_MSISDN))
            // The details are invalid. It might be a hacking attempt.
            return new ModelAndView("redirect:login.htm?denied");

        ModelAndView mav = new ModelAndView();

        try {
            Operator operator = operatorService.getOperator(username);

            if (operator != null) {

                String tokenCheck1 = SecurityUtil.generateDateBasedToken1(username, operator.getDateLastPassword());
                String tokenCheck2 = SecurityUtil.generateDateBasedToken2(username, operator.getDateLastPassword());

                if (token1.equals(tokenCheck1) && token2.equals(tokenCheck2)) {

                    mav.addObject("accountType", "'" + accountType + "'");
                    mav.addObject("userName", "'" + username + "'");
                    mav.addObject("key", "'" + token1 + "'");
                    mav.addObject("key2", "'" + token2 + "'");
                    mav.addObject("recaptchaKey", "'" + recaptchKeySite + "'");

                    if (environment.equals(ServerConstants.SYSTEM_ENVIRONMENT_LOCAL) || environment.equals(ServerConstants.SYSTEM_ENVIRONMENT_DEV)) {
                        mav.addObject("isTestEnvironment", true);
                    } else {
                        mav.addObject("isTestEnvironment", false);
                    }

                    mav.setViewName("credentialsExpired");
                    return mav;
                }
            }

        } catch (Exception e) {
        }

        mav.setViewName("errorPage");
        return mav;
    }

    @RequestMapping(value = "/userGuide.htm", method = RequestMethod.GET)
    public ModelAndView userGuide(HttpServletResponse response) {

        ModelAndView mav = new ModelAndView();
        mav.addObject("filePath", transporterManualDirectory + transporterManualFileName);
        mav.setViewName("userGuide");

        return mav;
    }
}
