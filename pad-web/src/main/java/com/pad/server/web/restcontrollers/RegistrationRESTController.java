package com.pad.server.web.restcontrollers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.EmailCodeRequest;
import com.pad.server.base.entities.SMSCodeRequest;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.RegistrationJson;
import com.pad.server.base.services.recaptcha.CaptchaService;
import com.pad.server.base.services.registration.RegistrationService;
import com.pad.server.base.services.system.SystemService;

@RestController
@RequestMapping("/registration")
public class RegistrationRESTController {

    private static final ZoneId currentZone = ZoneId.systemDefault();

    @Autowired
    private CaptchaService      captchaService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private SystemService       systemService;

    @RequestMapping(value = "/sendemailcode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity sendEmailCode(HttpServletResponse response, @RequestBody RegistrationJson registrationJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        captchaService.processResponse(registrationJson.getRecaptchaResponse());

        if (StringUtils.isBlank(registrationJson.getFirstName()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "FirstName");

        else if (StringUtils.isBlank(registrationJson.getLastName()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "LastName");

        else if (registrationJson.getEmail().length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_64 || !registrationJson.getEmail().matches(ServerConstants.REGEX_EMAIL))
            throw new PADValidationException(ServerResponseConstants.INVALID_EMAIL_CODE, ServerResponseConstants.INVALID_EMAIL_TEXT, "RegexFormatCheck#Email");

        else if (!registrationService.isCountEmailCodeSentUnderLimit(registrationJson.getEmail(), systemService.getSystemParameter().getRegEmailCodeSendLimit()))
            throw new PADValidationException(ServerResponseConstants.LIMIT_EXCEEDED_VERIFICATION_CODE_SENT_CODE, ServerResponseConstants.LIMIT_EXCEEDED_VERIFICATION_CODE_SENT_TEXT,
                "LimitExceeded#SendEmailCode");

        else if (systemService.isUsernameRegisteredAlready(registrationJson.getEmail()))
            throw new PADValidationException(ServerResponseConstants.CUSTOMER_EMAIL_ALREADY_REGISTERED_CODE, ServerResponseConstants.CUSTOMER_EMAIL_ALREADY_REGISTERED_TEXT,
                "Conflict#AlreadyRegisteredEmail");
        else {
            registrationService.sendRegistrationCodeEmail(registrationJson.getEmail(), registrationJson.getFirstName(), registrationJson.getLastName(),
                ServerUtil.getLanguageIdByCode(registrationJson.getLanguage()));

            apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
            apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        }

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/verifyemailcode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity verifyEmailCode(HttpServletResponse response, @RequestBody RegistrationJson registrationJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (registrationJson.getEmail().length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_64 || !registrationJson.getEmail().matches(ServerConstants.REGEX_EMAIL))
            throw new PADValidationException(ServerResponseConstants.INVALID_EMAIL_CODE, ServerResponseConstants.INVALID_EMAIL_TEXT, "RegexFormatCheck#Email");

        else if (!registrationJson.getCode().matches(ServerConstants.REGEX_REGISTRATION_CODE))
            throw new PADValidationException(ServerResponseConstants.INVALID_VERIFICATION_CODE_CODE, ServerResponseConstants.INVALID_VERIFICATION_CODE_TEXT,
                "RegexFormatCheck#EmailVerificationCode");

        EmailCodeRequest regEmailCodeRequest = registrationService.getEmailCodeRequest(registrationJson.getEmail());

        if (regEmailCodeRequest != null && regEmailCodeRequest.getCountVerified() >= systemService.getSystemParameter().getRegEmailVerificationLimit())
            throw new PADValidationException(ServerResponseConstants.LIMIT_VERIFICATION_EXCEEDED_CODE, ServerResponseConstants.LIMIT_VERIFICATION_EXCEEDED_TEXT,
                "LimitExceeded#EmailVerification");

        else if (regEmailCodeRequest != null) {

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime codeSentDate = LocalDateTime.ofInstant(regEmailCodeRequest.getDateCodeSent().toInstant(), currentZone);

            if (codeSentDate.isBefore(now.minusMinutes(systemService.getSystemParameter().getRegEmailCodeValidMinutes())))
                throw new PADValidationException(ServerResponseConstants.EXPIRED_VERIFICATION_CODE_CODE, ServerResponseConstants.EXPIRED_VERIFICATION_CODE_TEXT,
                    "Expired#EmailVerificationCode");
        }

        if (systemService.isUsernameRegisteredAlready(registrationJson.getEmail()))
            throw new PADValidationException(ServerResponseConstants.CUSTOMER_EMAIL_ALREADY_REGISTERED_CODE, ServerResponseConstants.CUSTOMER_EMAIL_ALREADY_REGISTERED_TEXT,
                "Conflict#AlreadyRegisteredEmail");

        else if (registrationService.verifyRegistrationCodeEmail(registrationJson.getEmail(), registrationJson.getCode())) {

            StringBuilder tokenSB = new StringBuilder();
            tokenSB.append(ServerConstants.SYSTEM_TOKEN_PREFIX).append(".").append(registrationJson.getEmail()).append(".").append(registrationJson.getCode()).append(".");
            tokenSB.append(RandomStringUtils.randomAlphanumeric(12)).append(".").append(RandomStringUtils.randomAlphanumeric(12));
            String token = DigestUtils.sha256Hex(tokenSB.toString());

            registrationService.setEmailToVerified(registrationJson.getEmail(), registrationJson.getCode(), token);
            apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
            apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
            apiResponse.setData(token);

        } else
            throw new PADValidationException(ServerResponseConstants.INVALID_VERIFICATION_CODE_CODE, ServerResponseConstants.INVALID_VERIFICATION_CODE_TEXT,
                "Exception#EmailVerificationCode");

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/sendsmscode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity sendSmsCode(HttpServletResponse response, @RequestBody RegistrationJson registrationJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        captchaService.processResponse(registrationJson.getRecaptchaResponse());

        if (StringUtils.isBlank(registrationJson.getFirstName()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "FirstName");

        else if (StringUtils.isBlank(registrationJson.getLastName()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "LastName");

        else if (StringUtils.isBlank(registrationJson.getMobileNumber()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "MobileNumber");

        String msisdn = ServerUtil.getValidNumber(registrationJson.getMobileNumber(), "sendSmsCode");

        if (msisdn.length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_16)
            throw new PADValidationException(ServerResponseConstants.INVALID_MSISDN_CODE, ServerResponseConstants.INVALID_MSISDN_TEXT, "");

        else if (!registrationService.isCountSmsCodeSentUnderLimit(msisdn, systemService.getSystemParameter().getRegSMSCodeSendLimit()))
            throw new PADValidationException(ServerResponseConstants.LIMIT_EXCEEDED_VERIFICATION_CODE_SENT_CODE, ServerResponseConstants.LIMIT_EXCEEDED_VERIFICATION_CODE_SENT_TEXT,
                "LimitExceeded#sendSmsCode");

        else if (systemService.isMsisdnRegisteredAlready(msisdn))
            throw new PADValidationException(ServerResponseConstants.CUSTOMER_MSISDN_ALREADY_REGISTERED_CODE, ServerResponseConstants.CUSTOMER_MSISDN_ALREADY_REGISTERED_TEXT,
                "Conflict#AlreadyRegisteredMsisdn");
        else {
            registrationService.sendRegistrationCodeSMS(msisdn, ServerUtil.getLanguageIdByCode(registrationJson.getLanguage()));

            apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
            apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        }

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/verifysmscode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity verifySmsCode(HttpServletResponse response, @RequestBody RegistrationJson registrationJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (StringUtils.isBlank(registrationJson.getMobileNumber()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "MobileNumber");

        String msisdn = ServerUtil.getValidNumber(registrationJson.getMobileNumber(), "sendSmsCode");

        if (msisdn.length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_16)
            throw new PADValidationException(ServerResponseConstants.INVALID_MSISDN_CODE, ServerResponseConstants.INVALID_MSISDN_TEXT, "");

        else if (!registrationJson.getCode().matches(ServerConstants.REGEX_REGISTRATION_CODE))
            throw new PADValidationException(ServerResponseConstants.INVALID_VERIFICATION_CODE_CODE, ServerResponseConstants.INVALID_VERIFICATION_CODE_TEXT,
                "RegexFormatCheck#SmsVerificationCode");

        SMSCodeRequest smsCodeRequest = registrationService.getSmsCodeRequest(msisdn);

        if (smsCodeRequest != null && smsCodeRequest.getCountVerified() >= systemService.getSystemParameter().getRegSMSVerificationLimit())
            throw new PADValidationException(ServerResponseConstants.LIMIT_VERIFICATION_EXCEEDED_CODE, ServerResponseConstants.LIMIT_VERIFICATION_EXCEEDED_TEXT,
                "LimitExceeded#SmsVerification");

        else if (smsCodeRequest != null) {

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime codeSentDate = LocalDateTime.ofInstant(smsCodeRequest.getDateCodeSent().toInstant(), currentZone);

            if (codeSentDate.isBefore(now.minusMinutes(systemService.getSystemParameter().getRegEmailCodeValidMinutes())))
                throw new PADValidationException(ServerResponseConstants.EXPIRED_VERIFICATION_CODE_CODE, ServerResponseConstants.EXPIRED_VERIFICATION_CODE_TEXT,
                    "Expired#SmsVerificationCode");
        }

        if (systemService.isMsisdnRegisteredAlready(msisdn))
            throw new PADValidationException(ServerResponseConstants.CUSTOMER_MSISDN_ALREADY_REGISTERED_CODE, ServerResponseConstants.CUSTOMER_MSISDN_ALREADY_REGISTERED_TEXT,
                "Conflict#AlreadyRegisteredMsisdn");

        else if (registrationService.verifyRegistrationCodeSMS(msisdn, registrationJson.getCode())) {

            StringBuilder tokenSB = new StringBuilder();
            tokenSB.append(ServerConstants.SYSTEM_TOKEN_PREFIX).append(".").append(msisdn).append(".").append(registrationJson.getCode()).append(".");
            tokenSB.append(RandomStringUtils.randomAlphanumeric(12)).append(".").append(RandomStringUtils.randomAlphanumeric(12));
            String token = DigestUtils.sha256Hex(tokenSB.toString());

            registrationService.setMsisdnToVerified(msisdn, registrationJson.getCode(), token);
            apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
            apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
            apiResponse.setData(token);

        } else
            throw new PADValidationException(ServerResponseConstants.INVALID_VERIFICATION_CODE_CODE, ServerResponseConstants.INVALID_VERIFICATION_CODE_TEXT,
                "Exception#SmsVerificationCode");

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/processregistration", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity processRegistration(HttpServletResponse response, @RequestBody RegistrationJson registrationJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        String telNumber = ServerConstants.DEFAULT_STRING;
        String emailListInvoiceStatement = ServerConstants.DEFAULT_STRING;
        String msisdn = ServerConstants.DEFAULT_STRING;

        if (registrationJson.getAccountType() == null || registrationJson.getAccountType() <= 0)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "AccountType");

        else if (StringUtils.isBlank(registrationJson.getFirstName()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "FirstName");

        else if (StringUtils.isBlank(registrationJson.getLastName()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "LastName");

        else if (!registrationJson.getPassword().matches(ServerConstants.REGEX_PASSWORD))
            throw new PADValidationException(ServerResponseConstants.INVALID_TOO_WEAK_PASSWORD_CODE, ServerResponseConstants.INVALID_TOO_WEAK_PASSWORD_TEXT,
                "RegexFormatCheck#Password");

        else if (StringUtils.isBlank(registrationJson.getAddress1()) || registrationJson.getAddress1().length() > 64)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "Address1");

        else if (StringUtils.isBlank(registrationJson.getAddress2()) || registrationJson.getAddress2().length() > 64)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "Address2");

        else if (StringUtils.isBlank(registrationJson.getPostCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "PostCode");

        else if (StringUtils.isBlank(registrationJson.getCountryCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "CountryCode");

        else if (!registrationJson.getCountryCode().matches(ServerConstants.REGEX_UNIVERSAL_COUNTRY_CODE))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "CountryCode");

        if (registrationJson.getAccountType() == ServerConstants.ACCOUNT_TYPE_COMPANY) {

            if (registrationJson.getEmail().length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_64 || !registrationJson.getEmail().matches(ServerConstants.REGEX_EMAIL))
                throw new PADException(ServerResponseConstants.INVALID_EMAIL_CODE, ServerResponseConstants.INVALID_EMAIL_TEXT, "RegexFormatCheck#Email");

            if (StringUtils.isBlank(registrationJson.getCompanyName()))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "CompanyName");

            else if (StringUtils.isBlank(registrationJson.getCompanyTelephone()))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "TelephoneNumber");

            telNumber = ServerUtil.getValidNumber(registrationJson.getCompanyTelephone(), "processRegistration");
            emailListInvoiceStatement = registrationJson.getEmail();

            if (!registrationService.isEmailVerifiedWithinHours(registrationJson.getEmail(), registrationJson.getToken(),
                systemService.getSystemParameter().getRegEmailVerificationValidHours()))
                throw new PADValidationException(ServerResponseConstants.VERIFICATION_EXPIRED_CODE, ServerResponseConstants.VERIFICATION_EXPIRED_TEXT,
                    "Expired#EmailVerificationForRegistration");

            if (systemService.isUsernameRegisteredAlready(registrationJson.getEmail()))
                throw new PADValidationException(ServerResponseConstants.CUSTOMER_EMAIL_ALREADY_REGISTERED_CODE, ServerResponseConstants.CUSTOMER_EMAIL_ALREADY_REGISTERED_TEXT,
                    "Conflict#EmailRegisteredAlready");

        } else if (registrationJson.getAccountType() == ServerConstants.ACCOUNT_TYPE_INDIVIDUAL) {

            if (StringUtils.isBlank(registrationJson.getMobileNumber()))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "MobileNumber");

            else if (StringUtils.isBlank(registrationJson.getNationalityCountryISO()))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "Nationality");

            else if (!registrationJson.getNationalityCountryISO().matches(ServerConstants.REGEX_UNIVERSAL_COUNTRY_CODE))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "Nationality");

            msisdn = ServerUtil.getValidNumber(registrationJson.getMobileNumber(), "processRegistration");

            if (!registrationService.isMsisdnVerifiedWithinHours(msisdn, registrationJson.getToken(), systemService.getSystemParameter().getRegSMSVerificationValidHours()))
                throw new PADValidationException(ServerResponseConstants.VERIFICATION_EXPIRED_CODE, ServerResponseConstants.VERIFICATION_EXPIRED_TEXT,
                    "Expired#MobileNumberVerificationForRegistration");

            if (systemService.isMsisdnRegisteredAlready(msisdn))
                throw new PADValidationException(ServerResponseConstants.CUSTOMER_MSISDN_ALREADY_REGISTERED_CODE, ServerResponseConstants.CUSTOMER_MSISDN_ALREADY_REGISTERED_TEXT,
                    "Conflict#MobileNumberRegisteredAlready");

        } else
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "AccountType");

        if (!registrationJson.getToken().matches(ServerConstants.REGEX_SHA256))
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "RegexFormatCheck#Token");

        else {

            Account account = new Account();
            account.setType(registrationJson.getAccountType());
            account.setStatus(ServerConstants.ACCOUNT_STATUS_PENDING_FOR_ACTIVATION);
            // company
            account.setCompanyName(StringUtils.isBlank(registrationJson.getCompanyName()) ? ServerConstants.DEFAULT_STRING : registrationJson.getCompanyName());
            account.setCompanyShortName(ServerConstants.DEFAULT_STRING);
            account.setCompanyRegistration(
                StringUtils.isBlank(registrationJson.getCompanyRegistration()) ? ServerConstants.DEFAULT_STRING : registrationJson.getCompanyRegistration());
            account.setCompanyTelephone(StringUtils.isBlank(telNumber) ? ServerConstants.DEFAULT_STRING : telNumber);
            account.setEmailListInvoiceStatement(StringUtils.isBlank(emailListInvoiceStatement) ? ServerConstants.DEFAULT_STRING : emailListInvoiceStatement);
            account.setRegistrationCountryISO(StringUtils.isBlank(registrationJson.getCountryCode()) ? ServerConstants.DEFAULT_STRING : registrationJson.getCountryCode());
            account.setAddress1(StringUtils.isBlank(registrationJson.getAddress1()) ? ServerConstants.DEFAULT_STRING : registrationJson.getAddress1());
            account.setAddress2(StringUtils.isBlank(registrationJson.getAddress2()) ? ServerConstants.DEFAULT_STRING : registrationJson.getAddress2());
            account.setAddress3(StringUtils.isBlank(registrationJson.getAddress3()) ? ServerConstants.DEFAULT_STRING : registrationJson.getAddress3());
            account.setAddress4(StringUtils.isBlank(registrationJson.getAddress4()) ? ServerConstants.DEFAULT_STRING : registrationJson.getAddress4());
            account.setPostCode(StringUtils.isBlank(registrationJson.getPostCode()) ? ServerConstants.DEFAULT_STRING : registrationJson.getPostCode());
            account.setSpecialTaxStatus(registrationJson.isSpecialTaxStatus());
            account.setPaymentTermsType(ServerConstants.DEFAULT_INT);
            account.setCurrency(ServerConstants.CURRENCY_CFA_FRANC);
            account.setBalanceAmount(BigDecimal.ZERO);
            account.setAmountOverdraftLimit(BigDecimal.ZERO);
            account.setAmountHold(BigDecimal.ZERO);
            account.setDenialReason(ServerConstants.DEFAULT_STRING);
            account.setLanguageId(ServerUtil.getLanguageIdByCode(registrationJson.getLanguage()));
            account.setIsTripApprovedEmail(false);
            account.setIsSendLowAccountBalanceWarn(false);
            account.setAmountLowAccountBalanceWarn(ServerConstants.DEFAULT_AMOUNT_LOW_ACCOUNT_BALANCE_WARN);

            // individual
            account.setFirstName(registrationJson.getFirstName());
            account.setLastName(registrationJson.getLastName());
            account.setMsisdn(StringUtils.isBlank(msisdn) ? ServerConstants.DEFAULT_STRING : msisdn);
            account.setNationalityCountryISO(
                StringUtils.isBlank(registrationJson.getNationalityCountryISO()) ? ServerConstants.DEFAULT_STRING : registrationJson.getNationalityCountryISO());
            account.setIsTripApprovedEmail(true);

            registrationService.processRegistration(registrationJson.getEmail(), msisdn, registrationJson.getFirstName(), registrationJson.getLastName(),
                registrationJson.getPassword(), account);

            apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
            apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        }

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }
}
