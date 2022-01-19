package com.pad.server.web.restcontrollers;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.entities.*;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.OnlinePaymentJson;
import com.pad.server.base.jsonentities.api.OnlinePaymentParameterJson;
import com.pad.server.base.jsonentities.api.PaymentJson;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.onlinepayment.OnlinePaymentService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.payment.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentRESTController {

    @Autowired
    private AccountService       accountService;

    @Autowired
    private OnlinePaymentService onlinePaymentService;

    @Autowired
    private OperatorService      operatorService;

    @Autowired
    private PaymentService       paymentService;

    // used by kiosk operator
    @RequestMapping(value = "/topup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity makeTopup(HttpServletResponse response, @RequestBody PaymentJson paymentJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(paymentJson.getTripCode()))
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");

        if (paymentJson.getPaymentOption() == null)
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");

        if (paymentJson.getPaymentOption() != ServerConstants.PAYMENT_OPTION_CASH && paymentJson.getPaymentOption() != ServerConstants.PAYMENT_OPTION_ORANGE_MONEY
            && paymentJson.getPaymentOption() != ServerConstants.PAYMENT_OPTION_FREE_MONEY)
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#3");

        if (paymentJson.getPaymentAmount() == null)
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#4");

        if (StringUtils.isBlank(paymentJson.getFirstName()) || StringUtils.isBlank(paymentJson.getLastName()))
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#5");

        if (StringUtils.isBlank(paymentJson.getMsisdn()))
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#6");

        String accountBalanceAmount = ServerConstants.DEFAULT_STRING;

        switch (paymentJson.getPaymentOption()) {

            case ServerConstants.PAYMENT_OPTION_CASH:

                if (paymentJson.getFeeDueAmount() == null)
                    throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#8");

                if (paymentJson.getChangeDueAmount() == null)
                    throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#9");

                accountBalanceAmount = paymentService.saveTransporterPayment(paymentJson, loggedOperator.getId());

                if (!StringUtils.isBlank(accountBalanceAmount)) {
                    paymentJson.setAccountBalanceAmount(new BigDecimal(accountBalanceAmount));
                }

                break;

            case ServerConstants.PAYMENT_OPTION_ORANGE_MONEY:
            case ServerConstants.PAYMENT_OPTION_FREE_MONEY:

                String onlinePaymentCode = paymentService.saveTransporterPayment(paymentJson, loggedOperator.getId());

                paymentJson.setOnlinePaymentCode(onlinePaymentCode);

                break;
        }

        apiResponse.setData(paymentJson);
        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    // used by finance operator
    @RequestMapping(value = "/topup/account", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity topupAccount(HttpServletResponse response, @RequestBody PaymentJson paymentJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(paymentJson.getAccountCode()))
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");

        if (paymentJson.getPaymentOption() == null)
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");

        if (paymentJson.getPaymentOption() != ServerConstants.PAYMENT_OPTION_CASH && paymentJson.getPaymentOption() != ServerConstants.PAYMENT_OPTION_ORANGE_MONEY
            && paymentJson.getPaymentOption() != ServerConstants.PAYMENT_OPTION_WARI && paymentJson.getPaymentOption() != ServerConstants.PAYMENT_OPTION_FREE_MONEY
            && paymentJson.getPaymentOption() != ServerConstants.PAYMENT_OPTION_E_MONEY && paymentJson.getPaymentOption() != ServerConstants.PAYMENT_OPTION_ECOBANK
            && paymentJson.getPaymentOption() != ServerConstants.PAYMENT_OPTION_ACCOUNT_CREDIT && paymentJson.getPaymentOption() != ServerConstants.PAYMENT_OPTION_CASH_REFUND
            && paymentJson.getPaymentOption() != ServerConstants.PAYMENT_OPTION_BANK_TRANSFER && paymentJson.getPaymentOption() != ServerConstants.PAYMENT_OPTION_CHEQUE
            && paymentJson.getPaymentOption() != ServerConstants.PAYMENT_OPTION_ACCOUNT_DEBIT)
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#3");

        if (paymentJson.getTopupAmount() == null)
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#4");

        if (StringUtils.isBlank(paymentJson.getFirstName()) || StringUtils.isBlank(paymentJson.getLastName()))
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#5");

        if (StringUtils.isBlank(paymentJson.getMsisdn()))
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#6");

        switch (paymentJson.getPaymentOption()) {

            case ServerConstants.PAYMENT_OPTION_CASH:

                if (paymentJson.getPaymentAmount() == null)
                    throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#7");

                if (paymentJson.getChangeDueAmount() == null)
                    throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#8");

                break;

            case ServerConstants.PAYMENT_OPTION_ACCOUNT_CREDIT:
            case ServerConstants.PAYMENT_OPTION_CASH_REFUND:
            case ServerConstants.PAYMENT_OPTION_BANK_TRANSFER:
            case ServerConstants.PAYMENT_OPTION_CHEQUE:
            case ServerConstants.PAYMENT_OPTION_ACCOUNT_DEBIT:

                if (paymentJson.getTopupAmount() == null)
                    throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#9");

                if (StringUtils.isBlank(paymentJson.getPaymentNote()))
                    throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#10");

                if (paymentJson.getPaymentNote().length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_256)
                    throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#11");

                break;

        }

        if (paymentJson.getPaymentAmount() != null && paymentJson.getPaymentAmount() % 500 != 0)
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#12");

        if (paymentJson.getTopupAmount() != null && paymentJson.getTopupAmount() % 500 != 0)
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#13");

        String onlinePaymentCode = paymentService.accountTopup(paymentJson, loggedOperator.getId());

        apiResponse.setData(onlinePaymentCode);
        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/status/check", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity checkPaymentStatus(HttpServletResponse response, @RequestBody PaymentJson paymentJson) throws PADException, PADValidationException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (StringUtils.isBlank(paymentJson.getOnlinePaymentCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "");

        OnlinePayment onlinePayment = onlinePaymentService.getOnlinePaymentByCode(paymentJson.getOnlinePaymentCode());

        if (onlinePaymentService.isOnlinePaymentSuccessful(onlinePayment)) {

            OnlinePaymentParameter onlinePaymentParameter = onlinePaymentService.getOnlinePaymentParameterFromMap(onlinePayment.getMnoId());
            Payment payment = paymentService.getPaymentById(onlinePayment.getPaymentId());

            paymentJson.setIsPrintReceipt(onlinePaymentParameter.getIsPrintReceipt());
            paymentJson.setReferenceAggregator(onlinePayment.getReferenceAggregator());
            paymentJson.setCode(payment == null ? "" : payment.getCode());

            apiResponse.setData(paymentJson);
            apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
            apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

            response.setStatus(HttpServletResponse.SC_OK);

        } else if (onlinePaymentService.isOnlinePaymentFailure(onlinePayment))
            throw new PADValidationException(ServerResponseConstants.COTIZEL_PAYMENT_FAILED_CODE, ServerResponseConstants.COTIZEL_PAYMENT_FAILED_TEXT, "");
        else
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "");

        return apiResponse;
    }

    @RequestMapping(value = "/reference/get", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getPaymentReference(HttpServletResponse response, @RequestBody PaymentJson paymentJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (StringUtils.isBlank(paymentJson.getOnlinePaymentCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "");

        OnlinePayment onlinePayment = onlinePaymentService.getOnlinePaymentByCode(paymentJson.getOnlinePaymentCode());

        if (onlinePayment != null && StringUtils.isNotBlank(onlinePayment.getReferenceAggregator())) {

            apiResponse.setData(onlinePayment.getReferenceAggregator());
            apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
            apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

            response.setStatus(HttpServletResponse.SC_OK);

        } else
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "");

        return apiResponse;
    }

    @RequestMapping(value = "/save/online/payment/parameter", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity saveOnlinePaymentParameter(HttpServletResponse response, @RequestBody OnlinePaymentParameterJson onlinePaymentParameterJson)
        throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        onlinePaymentService.updateOnlinePaymentParameter(onlinePaymentParameterJson);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(onlinePaymentService.getOnlinePaymentParameterJsonList());

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/online/payment/parameter/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getOnlinePaymentParameterList(HttpServletResponse response) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(onlinePaymentService.getOnlinePaymentParameterJsonList());

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;

    }

    @RequestMapping(value = "/company/get", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getCompanyPayments(HttpServletResponse response, @RequestBody OnlinePaymentJson onlinePaymentJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (StringUtils.isBlank(onlinePaymentJson.getAccountCode()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "");

        Account account = accountService.getAccountByCode(onlinePaymentJson.getAccountCode());

        if (account == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "");

        List<PaymentJson> paymentList = paymentService.getAccountPayments(account.getId());

        apiResponse.setDataList(paymentList);
        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }
}
