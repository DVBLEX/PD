package com.pad.server.web.restcontrollers;

import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.*;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.*;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.invoice.InvoiceService;
import com.pad.server.base.services.onlinepayment.OnlinePaymentService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.payment.PaymentService;
import com.pad.server.base.services.registration.RegistrationService;
import com.pad.server.base.services.sms.SmsService;
import com.pad.server.base.services.statement.StatementService;
import com.pad.server.base.services.system.SystemService;
import com.pad.server.base.services.trip.TripService;
import com.pad.server.base.services.vehicle.VehicleService;

@RestController
@RequestMapping("/account")
public class AccountRESTController {

    @Autowired
    private AccountService       accountService;

    @Autowired
    private ActivityLogService   activityLogService;

    @Autowired
    private EmailService         emailService;

    @Autowired
    private OnlinePaymentService onlinePaymentService;

    @Autowired
    private OperatorService      operatorService;

    @Autowired
    private PaymentService       paymentService;

    @Autowired
    private RegistrationService  registrationService;

    @Autowired
    private SmsService           smsService;

    @Autowired
    private StatementService     statementService;

    @Autowired
    private TripService          tripService;

    @Autowired
    private VehicleService       vehicleService;

    @Autowired
    private InvoiceService       invoiceService;

    @Autowired
    private SystemService        systemService;

    @Value("${system.url}")
    private String               systemUrl;

    @Value("${system.url.login.tp}")
    private String               loginTransporterUrl;

    @RequestMapping(value = "/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getAccountCount(HttpServletResponse response, @RequestBody AccountJson accountJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        long count = accountService.getAccountsCount(accountJson);

        List<Long> res = new ArrayList<>();
        res.add(count);
        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(res);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getAccountsList(HttpServletResponse response, @RequestBody AccountJson accountJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        List<AccountJson> accounts = accountService.getAccountsList(accountJson);

        Map<Integer, Boolean> mobilePaymentOptionsMap = onlinePaymentService.getMobilePaymentOptionsMap();

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(accounts);
        apiResponse.setDataMap(mobilePaymentOptionsMap);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getAccount(HttpServletResponse response) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Account account = accountService.getAccountById(loggedOperator.getAccountId());
        if (account == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");

        List<Trip> trips = tripService.getTripsByAccountIdAndStatus(account.getId(), ServerConstants.TRIP_STATUS_APPROVED);

        BigDecimal amountApprovedTrips = BigDecimal.ZERO;
        for (Trip trip : trips) {
            amountApprovedTrips = amountApprovedTrips.add(trip.getFeeAmount());
        }

        AccountJson accountJson = new AccountJson();

        BeanUtils.copyProperties(account, accountJson);
        if (StringUtils.isBlank(account.getEmailListInvoiceStatement())) {
            accountJson.setEmailListInvoiceStatement(new String[0]);
        } else {
            accountJson.setEmailListInvoiceStatement(account.getEmailListInvoiceStatement().split(ServerConstants.EMAILS_SEPARATOR));
        }
        accountJson.setAmountApprovedTrips(amountApprovedTrips);

        List<AccountJson> accountJsonList = new ArrayList<>();
        accountJsonList.add(accountJson);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(accountJsonList);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/activate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity activateAccount(HttpServletResponse response, @RequestBody AccountJson accountJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (StringUtils.isBlank(accountJson.getCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");

        if (accountJson.getPaymentTermsType() == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");

        if (accountJson.getAmountOverdraftLimit() == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#3");

        if (accountJson.getPaymentTermsType() != ServerConstants.ACCOUNT_PAYMENT_TERMS_TYPE_PREPAY
            && accountJson.getPaymentTermsType() != ServerConstants.ACCOUNT_PAYMENT_TERMS_TYPE_POSTPAY)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#4");

        Account account = accountService.getAccountByCode(accountJson.getCode());

        if (account == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#5");

        if (account.getStatus() != ServerConstants.ACCOUNT_STATUS_PENDING_FOR_ACTIVATION)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#6");

        account.setStatus(ServerConstants.ACCOUNT_STATUS_ACTIVE);
        account.setPaymentTermsType(accountJson.getPaymentTermsType());
        account.setAmountOverdraftLimit(accountJson.getAmountOverdraftLimit());
        account.setDateEdited(new Date());

        accountService.updateAccount(account);

        Operator transporter = operatorService.getOperatorByAccountId(account.getId());

        HashMap<String, Object> params = new HashMap<>();
        params.put("accountNumber", account.getNumber());
        params.put("accountName", account.getFirstName());
        params.put("loginPageUrl", systemUrl + loginTransporterUrl);
        params.put("loginDetails", transporter.getUsername());

        if (account.getType() == ServerConstants.ACCOUNT_TYPE_COMPANY) {

            Email accountActivationEmail = new Email();
            accountActivationEmail.setEmailTo(transporter.getUsername());
            accountActivationEmail.setAccountId(account.getId());
            accountActivationEmail.setLanguageId(account.getLanguageId());
            accountActivationEmail.setMissionId(ServerConstants.DEFAULT_LONG);
            accountActivationEmail.setTripId(ServerConstants.DEFAULT_LONG);

            emailService.scheduleEmailByType(accountActivationEmail, ServerConstants.EMAIL_ACCOUNT_ACTIVATED_TEMPLATE_TYPE, params);

        } else {

            Sms scheduledSms = new Sms();
            scheduledSms.setLanguageId(account.getLanguageId());
            scheduledSms.setAccountId(account.getId());
            scheduledSms.setMissionId(ServerConstants.DEFAULT_LONG);
            scheduledSms.setTripId(ServerConstants.DEFAULT_LONG);
            scheduledSms.setMsisdn(transporter.getUsername());

            smsService.scheduleSmsByType(scheduledSms, ServerConstants.SMS_ACCOUNT_ACTIVATED_TEMPLATE_TYPE, params);
        }

        activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_ACTIVATE_ACCOUNT, transporter.getId(), account.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/deny", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity denyAccount(HttpServletResponse response, @RequestBody AccountJson accountJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (StringUtils.isBlank(accountJson.getCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");

        if (StringUtils.isBlank(accountJson.getDenialReason()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");

        Account account = accountService.getAccountByCode(accountJson.getCode());

        if (account == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#3");

        if (account.getStatus() != ServerConstants.ACCOUNT_STATUS_PENDING_FOR_ACTIVATION)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#4");

        account.setStatus(ServerConstants.ACCOUNT_STATUS_DENIED);
        account.setDenialReason(accountJson.getDenialReason());
        account.setDateEdited(new Date());

        accountService.updateAccount(account);

        Operator transporter = operatorService.getOperatorByAccountId(account.getId());

        if (account.getType() == ServerConstants.ACCOUNT_TYPE_COMPANY) {

            Email accountActivationEmail = new Email();
            accountActivationEmail.setEmailTo(transporter.getUsername());
            accountActivationEmail.setAccountId(account.getId());
            accountActivationEmail.setLanguageId(account.getLanguageId());
            accountActivationEmail.setMissionId(ServerConstants.DEFAULT_LONG);
            accountActivationEmail.setTripId(ServerConstants.DEFAULT_LONG);

            HashMap<String, Object> params = new HashMap<>();
            params.put("accountName", account.getFirstName());
            params.put("denialReason", accountJson.getDenialReason());
            emailService.scheduleEmailByType(accountActivationEmail, ServerConstants.EMAIL_ACCOUNT_DENIED_TEMPLATE_TYPE, params);

        } else {

            Sms scheduledSms = new Sms();
            scheduledSms.setLanguageId(account.getLanguageId());
            scheduledSms.setAccountId(account.getId());
            scheduledSms.setMissionId(ServerConstants.DEFAULT_LONG);
            scheduledSms.setTripId(ServerConstants.DEFAULT_LONG);
            scheduledSms.setMsisdn(transporter.getUsername());

            HashMap<String, Object> params = new HashMap<>();
            params.put("accountName", account.getFirstName() + " " + account.getLastName());

            smsService.scheduleSmsByType(scheduledSms, ServerConstants.SMS_ACCOUNT_DENIED_TEMPLATE_TYPE, params);
        }

        for (Vehicle vehicle : vehicleService.getVehicleByaccountId(account.getId())) {
            vehicle.setIsActive(false);
            vehicleService.updateVehicle(vehicle);
        }

        if (StringUtils.isNotBlank(transporter.getEmail())) {
            registrationService.deleteEmailCodeRequest(transporter.getEmail());

            transporter.setEmail(transporter.getAccountId() + "#" + transporter.getEmail());
        }

        if (StringUtils.isNotBlank(transporter.getMsisdn())) {
            registrationService.deleteSmsCodeRequest(transporter.getMsisdn());

            transporter.setMsisdn(transporter.getAccountId() + "#" + transporter.getMsisdn());
        }

        transporter.setUsername(transporter.getAccountId() + "#" + transporter.getUsername());

        operatorService.updateOperator(transporter);

        activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_DENY_ACCOUNT, transporter.getId(), account.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/getActiveAccountNames", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    public ApiResponseJsonEntity getActiveAccountNames(HttpServletResponse response) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Map<String, String> accountNamesMap = accountService.getAccountNamesMap(loggedOperator.getLanguageId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataMap(accountNamesMap);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/update/companyTelephone", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updateCompanyTelephone(HttpServletResponse response, @RequestBody AccountJson accountJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (StringUtils.isBlank(accountJson.getCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "code is missing");

        if (StringUtils.isBlank(accountJson.getCompanyTelephone()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "companyTelephone is missing");

        Account account = accountService.getAccountByCode(accountJson.getCode());

        if (account == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "account is null");

        account.setCompanyTelephone(ServerUtil.getValidNumber(accountJson.getCompanyTelephone(), "updateCompanyTelephone"));
        account.setDateEdited(new Date());

        accountService.updateAccount(account);

        Operator transporter = operatorService.getOperatorByAccountId(account.getId());

        activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_UPDATE_ACCOUNT_COMPANY_TELEPHONE, transporter.getId(), account.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/update/companyEmail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updateEmailListInvoiceStatement(HttpServletResponse response, @RequestBody AccountJson accountJson) throws PADException, Exception {
        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (StringUtils.isBlank(accountJson.getCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "code is missing");

        if (accountJson.getEmailListInvoiceStatement() == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "companyEmails is missing");

        for (String email : accountJson.getEmailListInvoiceStatement()) {
            if (StringUtils.isBlank(email))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "companyEmail is missing");
        }

        Account account = accountService.getAccountByCode(accountJson.getCode());

        if (account == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "account is null");

        account.setEmailListInvoiceStatement(
            String.join(ServerConstants.EMAILS_SEPARATOR, ServerUtil.getValidEmails(accountJson.getEmailListInvoiceStatement(), "updateCompanyEmail")));
        account.setDateEdited(new Date());

        accountService.updateAccount(account);

        Operator transporter = operatorService.getOperatorByAccountId(account.getId());

        activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_UPDATE_ACCOUNT_COMPANY_EMAIL, transporter.getId(), account.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity update(HttpServletResponse response, @RequestBody AccountJson accountJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();
        
        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());
        
        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(accountJson.getCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "code is missing");

        if (accountJson.getAmountOverdraftLimit() == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");

        if (accountJson.getAmountOverdraftLimit().intValue() < systemService.getSystemParameter().getMaximumOverdraftLimitMinAmount().intValue()
            || accountJson.getAmountOverdraftLimit().intValue() > systemService.getSystemParameter().getMaximumOverdraftLimitMaxAmount().intValue())
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");

        if (accountJson.getAmountOverdraftLimit().intValue() % 500 != 0)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#3");

        Account account = accountService.getAccountByCode(accountJson.getCode());

        if (account == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "account is null");
        
        
        account.setAmountOverdraftLimit(accountJson.getAmountOverdraftLimit());
    	account.setStatus(accountJson.getIsActive() ? ServerConstants.ACCOUNT_STATUS_ACTIVE : ServerConstants.ACCOUNT_STATUS_INACTIVE);
    	account.setDateEdited(new Date());
    	
        List<Operator> operators = operatorService.getOperatorsByAccountId(account.getId());
        
        for(Operator operator: operators) {
        	operator.setIsActive(account.getStatus() == ServerConstants.ACCOUNT_STATUS_ACTIVE);
        	
        	operatorService.updateOperator(operator);
        }
    	
        accountService.updateAccount(account);

        activityLogService.saveActivityLogWithObjectJson(ServerConstants.ACTIVITY_LOG_UPDATE_ACCOUNT, loggedOperator.getId(), account.getId(), ServerUtil.toJson(accountJson));
    
        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/update/msisdn", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updateMsisdn(HttpServletResponse response, @RequestBody AccountJson accountJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (StringUtils.isBlank(accountJson.getCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "code is missing");

        if (StringUtils.isBlank(accountJson.getMsisdn()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "msisdn is missing");

        Account account = accountService.getAccountByCode(accountJson.getCode());

        if (account == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "account is null");

        account.setMsisdn(ServerUtil.getValidNumber(accountJson.getMsisdn(), "updateMsisdn"));
        account.setDateEdited(new Date());

        accountService.updateAccount(account);

        Operator transporter = operatorService.getOperatorByAccountId(account.getId());

        activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_UPDATE_ACCOUNT_MSISDN, transporter.getId(), account.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/update/address", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updateAddress(HttpServletResponse response, @RequestBody AccountJson accountJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (StringUtils.isBlank(accountJson.getCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "code is missing");

        if (StringUtils.isBlank(accountJson.getAddress1()) || accountJson.getAddress1().length() > 64)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "address1 is missing");

        if (StringUtils.isBlank(accountJson.getAddress2()) || accountJson.getAddress2().length() > 64)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "address2 is missing");

        if (StringUtils.isBlank(accountJson.getPostCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "postCode is missing");

        if (StringUtils.isBlank(accountJson.getCountryCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "countryCode is missing");

        if (!accountJson.getCountryCode().matches(ServerConstants.REGEX_UNIVERSAL_COUNTRY_CODE))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "countryCode is invalid");

        Account account = accountService.getAccountByCode(accountJson.getCode());

        if (account == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "account is null");

        account.setRegistrationCountryISO(StringUtils.isBlank(accountJson.getCountryCode()) ? ServerConstants.DEFAULT_STRING : accountJson.getCountryCode());
        account.setAddress1(StringUtils.isBlank(accountJson.getAddress1()) ? ServerConstants.DEFAULT_STRING : accountJson.getAddress1());
        account.setAddress2(StringUtils.isBlank(accountJson.getAddress2()) ? ServerConstants.DEFAULT_STRING : accountJson.getAddress2());
        account.setAddress3(StringUtils.isBlank(accountJson.getAddress3()) ? ServerConstants.DEFAULT_STRING : accountJson.getAddress3());
        account.setAddress4(StringUtils.isBlank(accountJson.getAddress4()) ? ServerConstants.DEFAULT_STRING : accountJson.getAddress4());
        account.setPostCode(StringUtils.isBlank(accountJson.getPostCode()) ? ServerConstants.DEFAULT_STRING : accountJson.getPostCode());
        account.setDateEdited(new Date());

        accountService.updateAccount(account);

        Operator transporter = operatorService.getOperatorByAccountId(account.getId());

        activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_UPDATE_ACCOUNT_ADDRESS, transporter.getId(), account.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/update/isTripApprovedEmail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updateIsTripApprovedEmail(HttpServletResponse response, @RequestBody AccountJson accountJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Account account = findValidAccount(accountJson);

        account.setIsTripApprovedEmail(accountJson.getIsTripApprovedEmail());
        account.setDateEdited(new Date());

        accountService.updateAccount(account);

        Operator transporter = operatorService.getOperatorByAccountId(account.getId());

        activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_UPDATE_ACCOUNT_APPROVED_TRIP_EMAIL, transporter.getId(), account.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/update/isDeductCreditRegisteredTrucks", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updateIsDeductCreditRegisteredTrucks(HttpServletResponse response, @RequestBody AccountJson accountJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Account account = findValidAccount(accountJson);

        account.setIsDeductCreditRegisteredTrucks(accountJson.getIsDeductCreditRegisteredTrucks());
        account.setDateEdited(new Date());

        accountService.updateAccount(account);

        Operator transporter = operatorService.getOperatorByAccountId(account.getId());

        activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_UPDATE_ACCOUNT_DEDUCT_CREDIT_REGISTERED_TRUCKS, transporter.getId(), account.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/update/lowAccountBalanceWarn", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity updateLowAccountBalanceWarn(HttpServletResponse response, @RequestBody AccountJson accountJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Account account = findValidAccount(accountJson);

        final boolean isSendLowAccountBalanceWarn = accountJson.getIsSendLowAccountBalanceWarn();
        final BigDecimal amountLowAccountBalanceWarn = accountJson.getAmountLowAccountBalanceWarn();
        account.setIsSendLowAccountBalanceWarn(isSendLowAccountBalanceWarn);
        if (isSendLowAccountBalanceWarn && amountLowAccountBalanceWarn != null) {
            if (amountLowAccountBalanceWarn.compareTo(BigDecimal.ZERO) < 0)
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "amountLowAccountBalanceWarn is negative");
            account.setAmountLowAccountBalanceWarn(accountJson.getAmountLowAccountBalanceWarn());
        }
        account.setDateEdited(new Date());

        accountService.updateAccount(account);

        Operator transporter = operatorService.getOperatorByAccountId(account.getId());

        activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_UPDATE_ACCOUNT_LOW_BALANCE_WARN, transporter.getId(), account.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    private Account findValidAccount(AccountJson accountJson) throws PADException {
        if (StringUtils.isBlank(accountJson.getCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "code is missing");

        Account account = accountService.getAccountByCode(accountJson.getCode());

        if (account == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "account is null");
        return account;
    }

    @RequestMapping(value = "/statement/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getStatementsCount(HttpServletResponse response, @RequestBody AccountStatementJson accountStatementJson) throws PADException {
        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        List<Long> res = Collections.singletonList(statementService.getAccountStatementsCount(accountStatementJson));

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(res);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/statement/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getStatementsList(HttpServletResponse response, @RequestBody AccountStatementJson accountStatementJson) throws PADException {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        List<AccountStatementJson> statements = statementService.getAccountStatementsList(accountStatementJson);
        // Date dateFrom = getParsedDate(accountStatementJson.getDateFromString(), ServerConstants.dateFormatddMMyyyy);

        /*
         * next condition was added 30/10/2020 as PAD-474, PAD-475 implementation. Disabled on PAD-505.
         */
        // BigDecimal openingBalance = accountService.getOpeningBalance(accountStatementJson.getAccountCode(), dateFrom);
        // if (openingBalance != null) {
        // AccountStatementJson openingBalanceResponseJson = new AccountStatementJson();
        // openingBalanceResponseJson.setBalance(openingBalance);
        // openingBalanceResponseJson.setOpeningBalance(true);
        // openingBalanceResponseJson.setDateCreated(dateFrom);
        // openingBalanceResponseJson.setDateCreatedString(accountStatementJson.getDateFromString());
        // statements.add(openingBalanceResponseJson);
        // }

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(statements);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/payment/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getPaymentsCount(HttpServletResponse response, @RequestBody AccountPaymentJson accountPaymentJson) throws PADException {
        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        List<Long> res = Collections.singletonList(paymentService.getAccountPaymentsCount(accountPaymentJson));
        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(res);
        apiResponse.setDataMap(onlinePaymentService.getMobilePaymentOptionsMap());

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/payment/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getPaymentsList(HttpServletResponse response, @RequestBody AccountPaymentJson accountPaymentJson) throws PADException {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        List<AccountPaymentJson> statements = paymentService.getAccountPaymentsList(accountPaymentJson);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(statements);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/invoice/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getInvoicesCount(HttpServletResponse response, @RequestBody AccountInvoiceJson accountInvoiceJson) throws PADException {
        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        List<Long> res = Collections.singletonList(invoiceService.getAccountInvoicesCount(accountInvoiceJson));

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(res);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/invoice/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getAccountInvoices(HttpServletResponse response, @RequestBody AccountInvoiceJson accountInvoiceJson) throws PADException {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        // List<InvoiceJson> invoices = invoiceService.getAccountInvoicesList(accountInvoiceJson.getAccountId(),
        // getParsedDate(accountInvoiceJson.getDateCreated(), ServerConstants.dateFormatyyyyMMddHHmmss));

        List<AccountInvoiceJson> invoices = invoiceService.getAccountInvoicesList(accountInvoiceJson);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(invoices);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }
}
