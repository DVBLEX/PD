package com.pad.server.web.restcontrollers;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.Invoice;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.InvoiceJson;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.invoice.InvoiceService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.web.security.MyUserDetails;

@RestController
@RequestMapping("/invoice")
public class InvoiceRESTController {

    @Autowired
    private AccountService     accountService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private InvoiceService     invoiceService;

    @Autowired
    private OperatorService    operatorService;

    @RequestMapping(value = "/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getCount(HttpServletResponse response, @RequestBody InvoiceJson invoiceJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();
        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        long count;

        if (loggedOperator.getRoleId() == ServerConstants.OPERATOR_ROLE_FINANCE_OPERATOR) {

            // TODO remove below line once we enable getting invoice data for both transporter and finance roles
            count = invoiceService.getInvoiceCount(getAccountIdByCode(invoiceJson.getAccountCode()), ServerConstants.DEFAULT_INT, invoiceJson.getAccountNumber());

        } else {
            Account account = getLoggedOperatorAccount(loggedOperator);
            count = invoiceService.getInvoiceCount(account.getId(), getAccountInvoiceType(account), invoiceJson.getAccountNumber());
        }

        List<Long> res = Collections.singletonList(count);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(res);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getList(HttpServletResponse response, @RequestBody InvoiceJson invoiceJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        List<InvoiceJson> invoices;

        if (loggedOperator.getRoleId() == ServerConstants.OPERATOR_ROLE_FINANCE_OPERATOR) {

            Integer invoiceType = invoiceJson.getType() == null ? ServerConstants.DEFAULT_INT : invoiceJson.getType();
            // TODO remove below line once we enable getting invoice data for both transporter and finance roles
            invoices = invoiceService.getInvoiceList(getAccountIdByCode(invoiceJson.getAccountCode()), invoiceType, invoiceJson);

        } else {
            Account account = getLoggedOperatorAccount(loggedOperator);
            invoices = invoiceService.getInvoiceList(account.getId(), getAccountInvoiceType(account), invoiceJson);
        }

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(invoices);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @ResponseBody
    @RequestMapping(value = "/download", headers = "Accept=*/*", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<InputStreamResource> downloadInvoice(@Context HttpServletRequest request, @FormParam("code") final String code) throws PADException, Exception {

        MyUserDetails authUser = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Account account = accountService.getAccountById(loggedOperator.getAccountId());

        if (authUser.getRole() != ServerConstants.OPERATOR_ROLE_FINANCE_OPERATOR && account == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");

        if (StringUtils.isBlank(code))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "code is missing");

        Invoice invoice = invoiceService.getInvoiceByCode(code);

        if (invoice == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "code is invalid");

        File file = new File(invoice.getPath());

        InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentLength(file.length());
        responseHeaders.setContentType(MediaType.valueOf("application/pdf"));
        responseHeaders.setContentDispositionFormData(file.getName(), "AGS_PAD_" + invoice.getCode() + ".pdf");

        long accountId = account == null ? ServerConstants.DEFAULT_LONG : account.getId();
        activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_INVOICE_DOWNLOAD, loggedOperator.getId(), accountId);

        return new ResponseEntity<>(inputStreamResource, responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/payment/confirm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity paymentConfirm(HttpServletResponse response, @RequestBody InvoiceJson invoiceJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(invoiceJson.getCode()))
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "code is null");

        Invoice invoice = invoiceService.getInvoiceByCode(invoiceJson.getCode());

        if (invoice == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "code is invalid");

        if (StringUtils.isBlank(invoiceJson.getDatePayment()))
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "date payment is null");

        Date datePayment = null;
        try {
            datePayment = ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyy, invoiceJson.getDatePayment());
        } catch (ParseException pe) {
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "date payment is invalid");
        }

        if (invoiceJson.getTypePayment() == null || invoiceJson.getTypePayment() <= ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "type payment is null");

        invoice.setIsPaid(Boolean.TRUE);
        invoice.setDatePayment(datePayment);
        invoice.setTypePayment(invoiceJson.getTypePayment());
        invoice.setOperatorId(loggedOperator.getId());
        invoice.setDateEdited(new Date());

        invoiceService.updateInvoice(invoice);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    private Account getLoggedOperatorAccount(Operator loggedOperator) throws PADException {
        Account account = accountService.getAccountById(loggedOperator.getAccountId());

        if (account == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");
        return account;
    }

    private int getAccountInvoiceType(Account account) {
        return account.getPaymentTermsType() == ServerConstants.ACCOUNT_PAYMENT_TERMS_TYPE_POSTPAY ? ServerConstants.TYPE_INVOICE : ServerConstants.TYPE_STATEMENT;
    }

    private Long getAccountIdByCode(String accountCode) throws PADException {
        Long accountId = null;
        if (StringUtils.isNotBlank(accountCode)) {
            Account account = accountService.getAccountByCode(accountCode);
            if (account == null)
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account code is invalid");

            accountId = account.getId();
        }
        return accountId;
    }
}
