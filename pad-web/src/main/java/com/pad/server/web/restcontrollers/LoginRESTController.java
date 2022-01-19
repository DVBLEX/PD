package com.pad.server.web.restcontrollers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.entities.SMSCodeRequest;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.LoginJson;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.recaptcha.CaptchaService;
import com.pad.server.base.services.registration.RegistrationService;
import com.pad.server.base.services.system.SystemService;

@RestController
@RequestMapping("/login")
public class LoginRESTController {

    private static final ZoneId   currentZone = ZoneId.systemDefault();

    @Autowired
    private CaptchaService        captchaService;

    @Autowired
    private RegistrationService   registrationService;

    @Autowired
    private SystemService         systemService;

    @Autowired
    private OperatorService       operatorService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(value = "/password/forgot/send", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity forgotPasswordSend(HttpServletResponse response, @RequestBody LoginJson loginJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (StringUtils.isBlank(loginJson.getAccountType()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "accountType#1");

        int accountType = ServerConstants.DEFAULT_INT;
        String email = "";
        String msisdn = "";

        try {
            accountType = Integer.parseInt(loginJson.getAccountType());

        } catch (NumberFormatException nfe) {
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "accountType#2");
        }

        captchaService.processResponse(loginJson.getRecaptchaResponse());

        switch (accountType) {

            case ServerConstants.ACCOUNT_TYPE_COMPANY:

                email = loginJson.getInput1();

                if (!email.matches(ServerConstants.REGEX_EMAIL))
                    throw new PADException(ServerResponseConstants.INVALID_EMAIL_CODE, ServerResponseConstants.INVALID_EMAIL_TEXT, "RegexFormatCheck#Email");

                else if (!systemService.isCountRequestForgotPasswordUnderLimit(email, systemService.getSystemParameter().getPasswordForgotEmailLimit()))
                    throw new PADException(ServerResponseConstants.LIMIT_EXCEEDED_REQUEST_FORGOT_PASSWORD_CODE, ServerResponseConstants.LIMIT_EXCEEDED_REQUEST_FORGOT_PASSWORD_TEXT,
                        "LimitExceeded#ForgotPassword");
                else {
                    if (systemService.isUsernameRegisteredAlready(email)) {
                        operatorService.sendPasswdForgotEmail(email, ServerUtil.getLanguageIdByCode(loginJson.getLanguage()));
                    }
                }

                break;

            case ServerConstants.ACCOUNT_TYPE_INDIVIDUAL:

                msisdn = ServerUtil.getValidNumber(loginJson.getInput1(), "forgotPasswordSend");

                if (!msisdn.matches(ServerConstants.REGEX_MSISDN))
                    throw new PADException(ServerResponseConstants.INVALID_MSISDN_CODE, ServerResponseConstants.INVALID_MSISDN_TEXT, "RegexFormatCheck#Msisdn");

                else if (!systemService.isCountRequestForgotPasswordUnderLimit(msisdn, systemService.getSystemParameter().getPasswordForgotEmailLimit()))
                    throw new PADException(ServerResponseConstants.LIMIT_EXCEEDED_REQUEST_FORGOT_PASSWORD_CODE, ServerResponseConstants.LIMIT_EXCEEDED_REQUEST_FORGOT_PASSWORD_TEXT,
                        "LimitExceeded#ForgotPassword");
                else {
                    if (systemService.isUsernameRegisteredAlready(msisdn)) {
                        operatorService.sendPasswdForgotSms(msisdn, ServerUtil.getLanguageIdByCode(loginJson.getLanguage()));
                    }
                }

                break;

            case ServerConstants.ACCOUNT_TYPE_OPERATOR:

                if (StringUtils.isBlank(loginJson.getInput1()))
                    throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "Username");

                Operator operator = operatorService.getOperator(loginJson.getInput1().trim());

                if (operator != null && operator.getIsActive() && StringUtils.isNotBlank(operator.getMsisdn())) {

                    msisdn = ServerUtil.getValidNumber(operator.getMsisdn(), "sendSmsCode");

                    registrationService.sendRegistrationCodeSMS(msisdn, operator.getLanguageId());
                }

                break;

            default:
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "accountType#3");
        }

        Operator operator = operatorService.getOperator(loginJson.getInput1());
        operatorService.unlockOperator(operator);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/password/forgot/change", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity forgotPasswordChange(HttpServletResponse response, @RequestBody LoginJson loginJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (StringUtils.isBlank(loginJson.getAccountType()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "accountType#1");

        String username = loginJson.getInput1();
        String newPassword = loginJson.getInput2();
        String confirmPassword = loginJson.getInput3();
        String key = loginJson.getInput4();

        captchaService.processResponse(loginJson.getRecaptchaResponse());

        switch (loginJson.getAccountType()) {

            case ServerConstants.ACCOUNT_TYPE_TRANSPORTER_COMPANY_URL_STRING:

                if (!username.matches(ServerConstants.REGEX_EMAIL))
                    // The email invalid. It might be a hacking attempt.
                    throw new PADException(ServerResponseConstants.INVALID_EMAIL_CODE, ServerResponseConstants.INVALID_EMAIL_TEXT, "RegexFormatCheck#Email");

                else if (!key.matches(ServerConstants.REGEX_SHA256))
                    throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "RegexFormatCheck#Token");

                break;

            case ServerConstants.ACCOUNT_TYPE_TRANSPORTER_INDIVIDUAL_URL_STRING:

                if (!username.matches(ServerConstants.REGEX_MSISDN))
                    // The mobile number is invalid. It might be a hacking attempt.
                    throw new PADException(ServerResponseConstants.INVALID_MSISDN_CODE, ServerResponseConstants.INVALID_MSISDN_TEXT, "RegexFormatCheck#Msisdn");

                else if (!key.matches(ServerConstants.REGEX_SHA256_SHORTENED))
                    throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "RegexFormatCheck#Token");

                break;

            default:
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "accountType#2");
        }

        if (!newPassword.matches(ServerConstants.REGEX_PASSWORD))
            throw new PADValidationException(ServerResponseConstants.INVALID_TOO_WEAK_PASSWORD_CODE, ServerResponseConstants.INVALID_TOO_WEAK_PASSWORD_TEXT,
                "RegexFormatCheck#Password");

        else if (!newPassword.equals(confirmPassword))
            throw new PADValidationException(ServerResponseConstants.MISMATCH_PASSWORD_CODE, ServerResponseConstants.MISMATCH_PASSWORD_TEXT, "Mismatch#Password");

        Operator operator = operatorService.getOperator(username);

        String token = SecurityUtil.generateDateBasedToken1(username, operator.getDateLastPasswdForgotRequest());

        if (loginJson.getAccountType().equals(ServerConstants.ACCOUNT_TYPE_TRANSPORTER_INDIVIDUAL_URL_STRING)) {
            token = token.substring(0, 25);
        }

        if (!token.equals(key))
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "Conflict#TokenValidation");

        else {

            operator.setPassword(passwordEncoder.encode(newPassword));
            operator.setIsCredentialsExpired(false);
            operator.setDateLastPassword(new Date());
            operator.setLoginFailureCount(0);
            operator.setCountPasswdForgotRequests(0);
            operatorService.updateOperator(operator);

            apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
            apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        }

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/password/setup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity passwordSetUp(HttpServletResponse response, @RequestBody LoginJson loginJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        String username = loginJson.getInput1().trim();
        String newPassword = loginJson.getInput2();
        String confirmPassword = loginJson.getInput3();
        String key = loginJson.getInput4();

        if (!newPassword.matches(ServerConstants.REGEX_PASSWORD))
            throw new PADValidationException(ServerResponseConstants.INVALID_TOO_WEAK_PASSWORD_CODE, ServerResponseConstants.INVALID_TOO_WEAK_PASSWORD_TEXT,
                "RegexFormatCheck#Password");

        else if (!newPassword.equals(confirmPassword))
            throw new PADValidationException(ServerResponseConstants.MISMATCH_PASSWORD_CODE, ServerResponseConstants.MISMATCH_PASSWORD_TEXT, "Mismatch#Password");

        else if (!key.matches(ServerConstants.REGEX_SHA256) && !key.matches(ServerConstants.REGEX_SHA256_SHORTENED) && !key.matches(ServerConstants.REGEX_REGISTRATION_CODE))
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "RegexFormatCheck#Token");

        else {
            Operator operator = operatorService.getOperator(username);

            if (operator == null)
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "operator is null");

            if (key.matches(ServerConstants.REGEX_SHA256) || key.matches(ServerConstants.REGEX_SHA256_SHORTENED)) {

                String token = SecurityUtil.generateDateBasedToken1(username, operator.getDateLastPasswdSetUp());

                if (key.matches(ServerConstants.REGEX_SHA256_SHORTENED)) {
                    token = token.substring(0, 25);
                }

                if (!token.equals(key))
                    throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "Conflict#TokenValidation");

            } else {
                SMSCodeRequest smsCodeRequest = registrationService.getSmsCodeRequest(operator.getMsisdn());

                if (smsCodeRequest == null)
                    throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "smsCodeRequest is null");

                LocalDateTime now = LocalDateTime.now();
                LocalDateTime codeSentDate = LocalDateTime.ofInstant(smsCodeRequest.getDateCodeSent().toInstant(), currentZone);

                if (codeSentDate.isBefore(now.minusMinutes(systemService.getSystemParameter().getRegEmailCodeValidMinutes())))
                    throw new PADValidationException(ServerResponseConstants.EXPIRED_VERIFICATION_CODE_CODE, ServerResponseConstants.EXPIRED_VERIFICATION_CODE_TEXT,
                        "Expired#SmsVerificationCode");

                if (!registrationService.verifyRegistrationCodeSMSForPasswordReset(operator.getMsisdn(), key))
                    throw new PADValidationException(ServerResponseConstants.INVALID_VERIFICATION_CODE_CODE, ServerResponseConstants.INVALID_VERIFICATION_CODE_TEXT,
                        "Exception#SmsVerificationCode");
            }

            operator.setPassword(passwordEncoder.encode(newPassword));
            operator.setIsActive(true);
            operator.setIsCredentialsExpired(false);
            operator.setLoginFailureCount(0);
            operator.setDateLastPassword(new Date());
            operator.setDateEdited(new Date());

            operatorService.updateOperator(operator);

            apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
            apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        }

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/password/expired/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity expiredPasswordUpdate(HttpServletResponse response, @RequestBody LoginJson loginJson) throws PADException, Exception {

        captchaService.processResponse(loginJson.getRecaptchaResponse());

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        String username = loginJson.getInput1();
        String oldPassword = loginJson.getInput2();
        String newPassword = loginJson.getInput3();
        String confirmPassword = loginJson.getInput4();
        String key = loginJson.getInput5();
        String key2 = loginJson.getInput6();

        if (loginJson.getAccountType().equals(ServerConstants.ACCOUNT_TYPE_TRANSPORTER_URL_STRING) && !username.matches(ServerConstants.REGEX_EMAIL)
            && !username.matches(ServerConstants.REGEX_MSISDN))
            throw new PADException(ServerResponseConstants.INVALID_USERNAME_CODE, ServerResponseConstants.INVALID_USERNAME_TEXT, "RegexFormatCheck#Username");

        else if (!newPassword.matches(ServerConstants.REGEX_PASSWORD))
            throw new PADValidationException(ServerResponseConstants.INVALID_TOO_WEAK_PASSWORD_CODE, ServerResponseConstants.INVALID_TOO_WEAK_PASSWORD_TEXT,
                "RegexFormatCheck#Password");

        else if (!newPassword.equals(confirmPassword))
            throw new PADValidationException(ServerResponseConstants.MISMATCH_PASSWORD_CODE, ServerResponseConstants.MISMATCH_PASSWORD_TEXT, "Conflict#MismatchPassword");

        else if (!key.matches(ServerConstants.REGEX_SHA256) || !key2.matches(ServerConstants.REGEX_SHA256))
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "RegexFormatCheck#Tokens");

        else {
            Operator operator = operatorService.getOperator(username);

            String token1 = SecurityUtil.generateDateBasedToken1(username, operator.getDateLastPassword());
            String token2 = SecurityUtil.generateDateBasedToken2(username, operator.getDateLastPassword());

            if (!token1.equals(key) || !token2.equals(key2))
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "Conflict#TokenValidationMismatch");

            else {
                if (!operator.getIsCredentialsExpired())
                    // The credentials are not expired. It might be a hacking attempt.
                    throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "Conflict#CredentialsAreNotExpired");

                if (!passwordEncoder.matches(oldPassword, operator.getPassword()))
                    throw new PADValidationException(ServerResponseConstants.INVALID_OLD_PASSWORD_CODE, ServerResponseConstants.INVALID_OLD_PASSWORD_TEXT, "InvalidaOldPassword");

                else if (passwordEncoder.matches(newPassword, operator.getPassword()))
                    throw new PADValidationException(ServerResponseConstants.INVALID_NEW_PASSWORD_CODE, ServerResponseConstants.INVALID_NEW_PASSWORD_TEXT, "InvalidNewPassword");

                else {

                    operator.setPassword(passwordEncoder.encode(newPassword));
                    operator.setIsCredentialsExpired(false);
                    operator.setDateLastPassword(new Date());
                    operator.setLoginFailureCount(0);
                    operator.setCountPasswdForgotRequests(0);
                    operatorService.updateOperator(operator);

                    apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
                    apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }
}
