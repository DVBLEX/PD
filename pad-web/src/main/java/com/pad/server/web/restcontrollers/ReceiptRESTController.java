package com.pad.server.web.restcontrollers;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.entities.Receipt;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.jsonentities.api.ReceiptJson;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.receipt.ReceiptService;
import com.pad.server.base.services.system.SystemService;
import com.pad.server.web.security.MyUserDetails;

@RestController
@RequestMapping("/receipt")
public class ReceiptRESTController {

    private static final Logger logger = Logger.getLogger(ReceiptRESTController.class);

    @Autowired
    private AccountService      accountService;

    @Autowired
    private ActivityLogService  activityLogService;

    @Autowired
    private ReceiptService      receiptService;

    @Autowired
    private OperatorService     operatorService;

    @Autowired
    private SystemService       systemService;

    @RequestMapping(value = "/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getCount(HttpServletResponse response, @RequestBody ReceiptJson receiptJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        MyUserDetails authUser = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Long accountId = null;
        if (authUser.getRole() == ServerConstants.OPERATOR_ROLE_ADMIN) {
            if (StringUtils.isNotBlank(receiptJson.getAccountCode())) {
                Account account = accountService.getAccountByCode(receiptJson.getAccountCode());

                if (account == null)
                    throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account code is invalid");

                accountId = account.getId();
            }
        } else {
            Account account = accountService.getAccountById(loggedOperator.getAccountId());
            if (account == null)
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");

            accountId = account.getId();
        }

        long count = receiptService.getReceiptCount(accountId);

        List<Long> res = new ArrayList<>();
        res.add(count);

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(res);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity getList(HttpServletResponse response, @RequestBody ReceiptJson receiptJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        MyUserDetails authUser = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Long accountId = null;
        if (authUser.getRole() == ServerConstants.OPERATOR_ROLE_ADMIN) {
            if (StringUtils.isNotBlank(receiptJson.getAccountCode())) {
                Account account = accountService.getAccountByCode(receiptJson.getAccountCode());

                if (account == null)
                    throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account code is invalid");

                accountId = account.getId();
            }
            accountId = receiptJson.getAccountId();
        } else {
            Account account = accountService.getAccountById(loggedOperator.getAccountId());
            if (account == null)
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");

            accountId = account.getId();
        }

        List<Receipt> receipts = receiptService.getReceiptList(accountId, receiptJson.getSortColumn(), receiptJson.getSortAsc(),
            ServerUtil.getStartLimitPagination(receiptJson.getCurrentPage(), receiptJson.getPageCount()), receiptJson.getPageCount());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
        apiResponse.setDataList(receipts);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @RequestMapping(value = "/validate/msisdn", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity validateMsisdn(HttpServletResponse response, @RequestBody ReceiptJson receiptJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        if (StringUtils.isBlank(receiptJson.getNumber()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "number is missing");

        if (StringUtils.isBlank(receiptJson.getMsisdn()))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "msisdn is missing");

        Receipt receipt = receiptService.getReceiptByNumber(receiptJson.getNumber());

        if (receipt == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "number is invalid");

        if (receipt.getDateLock() != null) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime receiptLockDate = LocalDateTime.ofInstant(receipt.getDateLock().toInstant(), ZoneId.systemDefault());

            if (currentDateTime.isBefore(receiptLockDate))
                throw new PADValidationException(ServerResponseConstants.RECEIPT_DOWNLOAD_LOCK_CODE, ServerResponseConstants.RECEIPT_DOWNLOAD_LOCK_TEXT, "download lock");
        }

        String mobileNumber = ServerUtil.getValidNumber(receiptJson.getMsisdn(), "validateMsisdn");

        if (!receipt.getMsisdn().equalsIgnoreCase(mobileNumber)) {

            receipt.setLockCountFailed(receipt.getLockCountFailed() + 1);
            receiptService.updateReceipt(receipt);

            if (receipt.getLockCountFailed() == systemService.getSystemParameter().getReceiptLockCountFailed()) {

                LocalDateTime dateLock = LocalDateTime.now().plusHours(systemService.getSystemParameter().getReceiptLockPeriod());
                receipt.setDateLock(Date.from(dateLock.atZone(ZoneId.systemDefault()).toInstant()));

                receiptService.updateReceipt(receipt);

                throw new PADValidationException(ServerResponseConstants.RECEIPT_DOWNLOAD_LOCK_CODE, ServerResponseConstants.RECEIPT_DOWNLOAD_LOCK_TEXT, "download lock");
            }

            throw new PADValidationException(ServerResponseConstants.INVALID_MSISDN_CODE, ServerResponseConstants.INVALID_MSISDN_TEXT, "msisdn is invalid");
        }

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

    @ResponseBody
    @RequestMapping(value = "/download/transporter", headers = "Accept=*/*", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<InputStreamResource> downloadReceiptTransporter(@Context HttpServletRequest request, @FormParam("number") final String number)
        throws PADException, Exception {

        MyUserDetails authUser = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        Account account = accountService.getAccountById(loggedOperator.getAccountId());
        if (authUser.getRole() != ServerConstants.OPERATOR_ROLE_ADMIN && account == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "account is null");

        if (StringUtils.isBlank(number))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "number is missing");

        Receipt receipt = receiptService.getReceiptByNumber(number);

        if (receipt == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "number is invalid");

        File file = new File(receipt.getPath());

        InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentLength(file.length());
        responseHeaders.setContentType(MediaType.valueOf("application/pdf"));
        responseHeaders.setContentDispositionFormData(file.getName(), "AGS_RECEIPT_" + receipt.getNumber() + ".pdf");

        long accountId = account == null ? ServerConstants.DEFAULT_LONG : account.getId();
        activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_RECEIPT_DOWNLOAD, loggedOperator.getId(), accountId);

        return new ResponseEntity<>(inputStreamResource, responseHeaders, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/download/link", headers = "Accept=*/*", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<InputStreamResource> downloadReceiptLink(@Context HttpServletRequest request, @FormParam("number") final String number) throws PADException, Exception {

        logger.info("downloadReceiptLink#[Request: number: " + number);

        if (StringUtils.isBlank(number))
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "number is missing");

        Receipt receipt = receiptService.getReceiptByNumber(number);

        if (receipt == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "number is invalid");

        File file = new File(receipt.getPath());

        InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentLength(file.length());
        responseHeaders.setContentType(MediaType.valueOf("application/pdf"));
        responseHeaders.setContentDispositionFormData(file.getName(), "AGS_RECEIPT_" + receipt.getNumber() + ".pdf");

        return new ResponseEntity<>(inputStreamResource, responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/print", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity printReceipt(HttpServletResponse response, @RequestBody ReceiptJson receiptJson) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        Operator loggedOperator = operatorService.getOperator(SecurityUtil.getSystemUsername());

        if (loggedOperator == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "loggedOperator is null");

        if (StringUtils.isBlank(receiptJson.getPaymentCode()))
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "paymentCode is missing");

        receiptService.printReceipt(receiptJson, loggedOperator.getId());

        apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
        apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);

        response.setStatus(HttpServletResponse.SC_OK);

        return apiResponse;
    }

}
