package com.pad.server.web.restcontrollers.onlinepayment.cotizel;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
import com.pad.server.base.jsonentities.api.onlinepayment.cotizel.CallbackPaymentGeneralResponseJson;
import com.pad.server.base.jsonentities.api.onlinepayment.cotizel.CallbackPaymentResponseJson;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.anpr.AnprBaseService;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.onlinepayment.OnlinePaymentService;
import com.pad.server.base.services.payment.PaymentService;
import com.pad.server.base.services.receipt.ReceiptService;
import com.pad.server.base.services.session.SessionService;
import com.pad.server.base.services.statement.StatementService;
import com.pad.server.base.services.system.SystemService;
import com.pad.server.base.services.trip.TripService;

@RestController
@RequestMapping("/onlinepayment/cotizel")
public class PaymentResponseCallbackRESTController {

    private static final Logger  logger = Logger.getLogger(PaymentResponseCallbackRESTController.class);

    @Autowired
    private AccountService       accountService;
    
    @Autowired
    private EmailService        emailService;

    @Autowired
    private OnlinePaymentService onlinePaymentService;

    @Autowired
    private PaymentService       paymentService;

    @Autowired
    private ReceiptService       receiptService;

    @Autowired
    private SessionService       sessionService;

    @Autowired
    private StatementService     statementService;

    @Autowired
    private TripService          tripService;

    @Autowired
    private SystemService        systemService;

    @Autowired
    private AnprBaseService      anprBaseService;

    @Value("${cotizel.sn.response.username}")
    private String               cotizelSnResponseUsername;

    @Value("${cotizel.sn.response.password}")
    private String               cotizelSnResponsePassword;

    @RequestMapping(value = "/payment/response/callback", headers = "Accept=*/*", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CallbackPaymentGeneralResponseJson paymentResponseCallback(@Context HttpServletRequest request, HttpServletResponse response,
        @RequestBody CallbackPaymentResponseJson callbackPaymentResponseJson) throws PADException, Exception {

        CallbackPaymentGeneralResponseJson apiResponse = new CallbackPaymentGeneralResponseJson();
        Account account = null;
        Trip trip = null;

        logger.info(callbackPaymentResponseJson);

        try {
            if (callbackPaymentResponseJson == null)
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                    "callbackPaymentResponseJson is null");

            if (!callbackPaymentResponseJson.getUsername().equalsIgnoreCase(cotizelSnResponseUsername))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "username is invalid");

            if (!callbackPaymentResponseJson.getPassword().equalsIgnoreCase(cotizelSnResponsePassword))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "password is invalid");

            if (!callbackPaymentResponseJson.getHashcode().equalsIgnoreCase(callbackPaymentResponseJson.getSha256Hashcode()))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "hashcode is invalid");

            if (StringUtils.isBlank(callbackPaymentResponseJson.getStatus()))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "status is missing");

            OnlinePayment onlinePayment = onlinePaymentService.getOnlinePaymentById(Long.parseLong(callbackPaymentResponseJson.getCodeags()));

            if (onlinePayment == null)
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "reference is invalid");

            if (!onlinePayment.getStatusAggregator().equalsIgnoreCase("INITIATED") && !onlinePayment.getStatusAggregator().equalsIgnoreCase("PENDING")
                && !onlinePayment.getStatusAggregator().equalsIgnoreCase("")) {
            	
            	if(onlinePayment.getStatusAggregator().equalsIgnoreCase("FAILED") && callbackPaymentResponseJson.getStatus().equalsIgnoreCase("SUCCESSFUL"))
            		sendSystemEmail(callbackPaymentResponseJson, onlinePayment);
            	
            	throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                        "aggregator status = " + onlinePayment.getStatusAggregator());
            }
                

            onlinePayment.setReferenceAggregator(callbackPaymentResponseJson.getReference());
            onlinePayment.setTransactionAggregator(callbackPaymentResponseJson.getTransaction());
            onlinePayment.setDateCallbackResponse(new Date());

            final LocalDateTime localDatePayment = LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.valueOf(callbackPaymentResponseJson.getDatetimepaiement())),
                ZoneId.systemDefault());
            final Date datePayment = Date.from(localDatePayment.atZone(ZoneId.systemDefault()).toInstant());

            onlinePayment.setDatePaymentAggregator(datePayment);
            onlinePayment.setResponseCallbackHash(callbackPaymentResponseJson.getHashcode());
            onlinePayment.setStatusAggregator(callbackPaymentResponseJson.getStatus());

            int responseCode = callbackPaymentResponseJson.getStatus().equalsIgnoreCase(ServerConstants.COTIZEL_CALLBACK_STATUS_SUCCESS)
                ? ServerResponseConstants.MNO_RESPONSE_CODE_SUCCESS
                : ServerResponseConstants.MNO_RESPONSE_CODE_CALLBACK_FAILURE;
            onlinePayment.setResponseCode(responseCode);

            Payment payment = paymentService.getPaymentById(onlinePayment.getPaymentId());

            if (payment != null) {
                payment.setResponseCode(onlinePayment.getResponseCode());
                payment.setDateResponse(onlinePayment.getDateCallbackResponse());

                paymentService.updatePayment(payment);
            }

            onlinePaymentService.updateOnlinePayment(onlinePayment);

            if (callbackPaymentResponseJson.getStatus().equalsIgnoreCase(ServerConstants.COTIZEL_CALLBACK_STATUS_SUCCESS)) {

                int paymentOption = ServerConstants.DEFAULT_INT;

                if (onlinePayment.getMnoId() == ServerConstants.MNO_ID_ORANGE_MONEY) {
                    paymentOption = ServerConstants.PAYMENT_OPTION_ORANGE_MONEY;
                } else if (onlinePayment.getMnoId() == ServerConstants.MNO_ID_FREE_MONEY) {
                    paymentOption = ServerConstants.PAYMENT_OPTION_FREE_MONEY;
                } else if (onlinePayment.getMnoId() == ServerConstants.MNO_ID_WARI) {
                    paymentOption = ServerConstants.PAYMENT_OPTION_WARI;
                } else if (onlinePayment.getMnoId() == ServerConstants.MNO_ID_E_MONEY) {
                    paymentOption = ServerConstants.PAYMENT_OPTION_E_MONEY;
                } else if (onlinePayment.getMnoId() == ServerConstants.MNO_ID_ECO_BANK) {
                    paymentOption = ServerConstants.PAYMENT_OPTION_ECOBANK;
                }

                if (onlinePayment.getTripId() != ServerConstants.DEFAULT_LONG) {
                    // payment through kiosk operator

                    trip = tripService.getTripById(onlinePayment.getTripId());

                    if (trip == null)
                        throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "trip is null");

                    Session kioskSession = sessionService.getSessionById(trip.getLaneSessionId());

                    if (trip.getAccountId() == ServerConstants.DEFAULT_LONG) {
                        // no account, adhoc trip. In that case we don't make entry to statements

                        if (kioskSession != null) {
                            kioskSession.setNoAccountOnlineTransactionCount(kioskSession.getNoAccountOnlineTransactionCount() + 1);
                            kioskSession.setNoAccountOnlineTransactionTotalAmount(kioskSession.getNoAccountOnlineTransactionTotalAmount().add(onlinePayment.getAmount()));
                        }

                    } else {
                        // account, adhoc trip
                        if (kioskSession != null) {
                            kioskSession.setAccountOnlineTransactionCount(kioskSession.getAccountOnlineTransactionCount() + 1);
                            kioskSession.setAccountOnlineTransactionTotalAmount(kioskSession.getAccountOnlineTransactionTotalAmount().add(onlinePayment.getAmount()));
                        }

                        Statement statement = new Statement();
                        statement.setCode(SecurityUtil.generateUniqueCode());
                        statement.setType(ServerConstants.PAYMENT_TYPE_ACCOUNT_TOPUP);
                        statement.setPaymentId(onlinePayment.getPaymentId());
                        statement.setMissionId(trip.getMission().getId());
                        statement.setTripId(trip.getId());
                        statement.setOperatorId(ServerConstants.DEFAULT_LONG);
                        statement.setCurrency(onlinePayment.getCurrencyCode());
                        statement.setNotes(ServerConstants.DEFAULT_STRING);
                        statement.setDateCreated(new Date());
                        statement.setDateEdited(statement.getDateCreated());

                        account = accountService.getAccountById(trip.getAccountId());
                        if (account == null)
                            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                                "account associated with trip is null");

                        BigDecimal accountBalanceAmount = account.getBalanceAmount();
                        BigDecimal topupAmount = onlinePayment.getAmount();

                        accountBalanceAmount = accountBalanceAmount.add(topupAmount);

                        statement.setAmountCredit(topupAmount);
                        statement.setAccountId(account.getId());
                        statement.setAmountDebit(BigDecimal.ZERO);
                        statement.setAmountRunningBalance(accountBalanceAmount);

                        account.setBalanceAmount(accountBalanceAmount);
                        account.setDateEdited(new Date());

                        accountService.updateAccount(account);

                        statementService.saveStatement(statement);
                    }

                    if (kioskSession != null && kioskSession.getType() == ServerConstants.SESSION_TYPE_VIRTUAL) {

                        trip.setStatus(ServerConstants.TRIP_STATUS_IN_TRANSIT);

                        // create port entry permission
                        anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_DIRECT,
                            systemService.getPortOperatorAnprZoneIdById(trip.getPortOperatorGateId()), trip, null, null, new Date());
                    }

                    if (trip.getAccountId() == ServerConstants.DEFAULT_LONG) {

                        trip.setFeePaid(true);
                        trip.setDateFeePaid(new Date());

                    } else {
                        if (kioskSession != null && kioskSession.getType() == ServerConstants.SESSION_TYPE_VIRTUAL) {

                            statementService.chargeParkingFee(trip, kioskSession.getKioskOperatorId());
                        }
                    }

                    tripService.updateTrip(trip);

                    if (kioskSession != null) {
                        sessionService.updateSession(kioskSession);
                    }

                } else {
                    // payment through office operator

                    account = accountService.getAccountById(onlinePayment.getAccountId());

                    if (account == null)
                        throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "account is null");

                    BigDecimal accountBalanceAmount = account.getBalanceAmount();

                    account.setBalanceAmount(accountBalanceAmount.add(onlinePayment.getAmount()));
                    account.setDateEdited(new Date());

                    Statement statement = new Statement();
                    statement.setCode(SecurityUtil.generateUniqueCode());
                    statement.setType(ServerConstants.PAYMENT_TYPE_ACCOUNT_TOPUP);
                    statement.setPaymentId(onlinePayment.getPaymentId());
                    statement.setAccountId(account.getId());
                    statement.setMissionId(ServerConstants.DEFAULT_LONG);
                    statement.setTripId(ServerConstants.DEFAULT_LONG);
                    statement.setOperatorId(ServerConstants.DEFAULT_LONG);
                    statement.setCurrency(onlinePayment.getCurrencyCode());
                    statement.setAmountDebit(BigDecimal.ZERO);
                    statement.setAmountCredit(onlinePayment.getAmount());
                    statement.setAmountRunningBalance(account.getBalanceAmount());
                    statement.setNotes(ServerConstants.DEFAULT_STRING);
                    statement.setDateCreated(new Date());
                    statement.setDateEdited(statement.getDateCreated());

                    statementService.saveStatement(statement);

                    accountService.updateAccount(account);

                    paymentService.sendTransactionNotifyEmail(account, payment);
                }

                String typePaymentString = ServerUtil.getPaymentOptionDescriptionById(paymentOption, ServerConstants.LANGUAGE_FR_ID);
                String vehicleRegNumber = trip == null ? ServerConstants.DEFAULT_STRING : trip.getVehicleRegistration();
                String vehicleRegCountryISO = trip == null ? ServerConstants.DEFAULT_STRING : trip.getVehicleRegistrationCountryISO();

                String itemDescription;
                if (account == null) {
                    if (trip != null) { // IDE says that condition is always true
                        String portOperatorName = systemService.getPortOperatorNameById(trip.getPortOperatorId());
                        String transactionType = ServerUtil.getTransactionTypeName(trip.getTransactionType(), ServerConstants.LANGUAGE_FR_ID);
                        itemDescription = "Voyage pour opérateur portuaire: " + portOperatorName + "; Type: " + transactionType + ";";
                    } else {
                        itemDescription = "Paiement des frais de voyage";
                    }
                } else {
                    itemDescription = "Recharge de compte";
                }

                receiptService.createReceipt(account, onlinePayment.getPaymentId(), onlinePayment.getFirstName(), onlinePayment.getLastName(), onlinePayment.getMsisdn(),
                    paymentOption, typePaymentString, itemDescription, 1, onlinePayment.getAmount(), ServerConstants.CURRENCY_CFA_FRANC, onlinePayment.getAmount(),
                    onlinePayment.getAmount(), BigDecimal.valueOf(ServerConstants.ZERO_INT), vehicleRegNumber, vehicleRegCountryISO, ServerConstants.DEFAULT_LONG);

            }

            apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
            apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
            apiResponse.setResponseDate(new Date());

            logger.info(apiResponse);

            response.setStatus(HttpServletResponse.SC_OK);

        } catch (PADException pade) {
            logger.error("PADException: ", pade);

            apiResponse.setResponseCode(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE);
            apiResponse.setResponseText(ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT);
            apiResponse.setResponseDate(new Date());

            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            logger.error("Exception: ", e);

            apiResponse.setResponseCode(ServerResponseConstants.API_FAILURE_CODE);
            apiResponse.setResponseText(ServerResponseConstants.API_FAILURE_TEXT);
            apiResponse.setResponseDate(new Date());

            response.setStatus(HttpServletResponse.SC_OK);
        }

        return apiResponse;
    }

    private void sendSystemEmail(CallbackPaymentResponseJson callbackPaymentResponseJson, OnlinePayment onlinePayment) {
    	
    	String emailSubject = "Cotizel Callback Issue - 'SUCCESSFUL' callback after 'FAILURE' - Reference: " + onlinePayment.getReferenceAggregator();
        String emailHeader = "Cotizel Callback Issue. There was a 'FAILURE'callback from Cotizel and now a 'SUCCESSFUL' one. Find the details below:";
        String emailFooter = "PaymentResponseCallbackRESTController.paymentResponseCallback()";
        
        List<NameValuePair> validationIssues = new ArrayList<>();
        validationIssues.add(new NameValuePair("Reference", onlinePayment.getReferenceAggregator()));
        validationIssues.add(new NameValuePair("Online Payment ID", onlinePayment.getId()));
        validationIssues.add(new NameValuePair("Amount", onlinePayment.getAmount()));
        validationIssues.add(new NameValuePair("Amount Aggregator", onlinePayment.getAmountAggregator()));
        validationIssues.add(new NameValuePair("Fee Aggregator", onlinePayment.getFeeAggregator()));
        
        if(onlinePayment.getAccountId() != ServerConstants.DEFAULT_LONG) {
        	
        	Account account = accountService.getAccountById(onlinePayment.getAccountId());
        	
        	if(account != null)
        		validationIssues.add(new NameValuePair("Account Number", account.getNumber()));
        }
        
        validationIssues.add(new NameValuePair("Mission ID", onlinePayment.getMissionId()));
        validationIssues.add(new NameValuePair("Trip ID", onlinePayment.getTripId()));
        try {
	        validationIssues.add(new NameValuePair("Online Payment Request Date", ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmmss, onlinePayment.getDateRequest())));
	        validationIssues.add(new NameValuePair("FAILURE callback date", ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmmss, onlinePayment.getDateCallbackResponse())));
        }catch (Exception e) {
			
		}

        emailService.sendSystemEmail(emailSubject, EmailService.EMAIL_TYPE_EXCEPTION, emailHeader, validationIssues, emailFooter);
    }
    
}
