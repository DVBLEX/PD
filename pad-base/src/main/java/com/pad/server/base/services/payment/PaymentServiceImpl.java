package com.pad.server.base.services.payment;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.*;
import com.pad.server.base.entities.*;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADOnlinePaymentException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.AccountPaymentJson;
import com.pad.server.base.jsonentities.api.PaymentJson;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.anpr.AnprBaseService;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.onlinepayment.OnlinePaymentService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.receipt.ReceiptService;
import com.pad.server.base.services.session.SessionService;
import com.pad.server.base.services.statement.StatementService;
import com.pad.server.base.services.system.SystemService;
import com.pad.server.base.services.trip.TripService;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private static final Logger  logger = Logger.getLogger(PaymentServiceImpl.class);

    @Autowired
    private HibernateTemplate    hibernateTemplate;

    @Autowired
    private JdbcTemplate         jdbcTemplate;

    @Autowired
    private ActivityLogService   activityLogService;

    @Autowired
    private AccountService       accountService;

    @Autowired
    private OnlinePaymentService onlinePaymentService;

    @Autowired
    private ReceiptService       receiptService;

    @Autowired
    private SessionService       sessionService;

    @Autowired
    private StatementService     statementService;

    @Autowired
    private SystemService        systemService;

    @Autowired
    private TripService          tripService;

    @Autowired
    private AnprBaseService      anprBaseService;

    @Autowired
    private EmailService         emailService;

    @Autowired
    private OperatorService      operatorService;

    @Override
    public void savePayment(Payment payment) {

        hibernateTemplate.save(payment);
    }

    @Override
    public void updatePayment(Payment payment) {

        payment.setDateEdited(new Date());

        hibernateTemplate.update(payment);
    }

    @Override
    @Transactional(rollbackFor = { PADException.class, PADValidationException.class, Exception.class }, noRollbackFor = { PADOnlinePaymentException.class })
    public String saveTransporterPayment(PaymentJson paymentJson, long operatorId) throws PADException, PADValidationException, PADOnlinePaymentException {

        Account account = null;
        BigDecimal paymentAmount = BigDecimal.valueOf(paymentJson.getPaymentAmount());
        BigDecimal dueAmount = null;
        BigDecimal changeDueAmount = null;
        BigDecimal accountBalanceAmount = null;
        Payment payment = null;

        String result = "";

        Trip trip = tripService.getTripByCode(paymentJson.getTripCode());

        if (trip == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#10");

        if (trip.getMission() == null)
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "#11");

        if (trip.getStatus() != ServerConstants.TRIP_STATUS_APPROVED && trip.getStatus() != ServerConstants.TRIP_STATUS_EXITED_PARKING_PREMATURELY)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#12");

        if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#13");

        Session session = sessionService.getLastSessionByKioskOperatorId(operatorId);
        if (session == null)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#14");

        switch (paymentJson.getPaymentOption()) {

            case ServerConstants.PAYMENT_OPTION_CASH:

                dueAmount = BigDecimal.valueOf(paymentJson.getFeeDueAmount());
                changeDueAmount = BigDecimal.valueOf(paymentJson.getChangeDueAmount());

                if (dueAmount.compareTo(BigDecimal.ZERO) < 0 || changeDueAmount.compareTo(BigDecimal.ZERO) < 0)
                    throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#15");

                if (trip.getAccountId() == ServerConstants.DEFAULT_LONG) {
                    if (dueAmount.compareTo(trip.getFeeAmount()) != ServerConstants.ZERO_INT)
                        throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#16");

                    if (paymentAmount.subtract(dueAmount).compareTo(changeDueAmount) != ServerConstants.ZERO_INT)
                        throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#17");
                }

                payment = new Payment();
                initDefaultPayment(payment, paymentJson, trip.getAccountId(), operatorId, paymentAmount);
                payment.setLaneSessionId(session.getId());
                payment.setMissionId(trip.getMission().getId());
                payment.setTripId(trip.getId());
                payment.setType(
                    trip.getAccountId() == ServerConstants.DEFAULT_LONG ? ServerConstants.PAYMENT_TYPE_NO_ACCOUNT_ADHOC_TRIP_FEE : ServerConstants.PAYMENT_TYPE_ACCOUNT_TOPUP);
                payment.setAmountDue(dueAmount);
                payment.setAmountChangeDue(changeDueAmount);
                payment.setMsisdn(ServerUtil.getValidNumber(paymentJson.getMsisdn(), "saveTransporterPayment"));

                savePayment(payment);

                if (trip.getAccountId() == ServerConstants.DEFAULT_LONG) {

                    // no account, adhoc trip
                    if (dueAmount
                        .compareTo(systemService.getTripFeeAmount(trip.getPortOperatorId(), trip.getMission().getTransactionType(), trip.getVehicleRegistrationCountryISO())) != 0)
                        throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#18");

                    session.setNoAccountCashTransactionCount(session.getNoAccountCashTransactionCount() + 1);
                    session.setNoAccountCashTransactionTotalAmount(session.getNoAccountCashTransactionTotalAmount().add(dueAmount));
                    session.setCashChangeGivenTotalAmount(session.getCashChangeGivenTotalAmount().add(changeDueAmount));

                } else {
                    Statement statement = initDefaultStatement(payment, ServerConstants.DEFAULT_STRING, trip.getAccountId(), null, operatorId, null);
                    statement.setType(ServerConstants.PAYMENT_TYPE_ACCOUNT_TOPUP);
                    statement.setMissionId(trip.getMission().getId());
                    statement.setTripId(trip.getId());

                    account = accountService.getAccountById(trip.getAccountId());
                    if (account == null)
                        throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#19");

                    accountBalanceAmount = account.getBalanceAmount();

                    BigDecimal topupAmount = paymentAmount.subtract(changeDueAmount);
                    accountBalanceAmount = accountBalanceAmount.add(topupAmount);

                    statement.setAmountCredit(topupAmount);

                    session.setAccountCashTransactionCount(session.getAccountCashTransactionCount() + 1);
                    session.setAccountCashTransactionTotalAmount(session.getAccountCashTransactionTotalAmount().add(topupAmount));

                    account.setBalanceAmount(accountBalanceAmount);
                    account.setDateEdited(new Date());

                    accountService.updateAccount(account);

                    statement.setAmountDebit(BigDecimal.ZERO);
                    statement.setAmountRunningBalance(accountBalanceAmount);

                    statementService.saveStatement(statement);

                    result = accountBalanceAmount.toString();
                }

                if (session.getType() == ServerConstants.SESSION_TYPE_VIRTUAL) {

                    trip.setStatus(ServerConstants.TRIP_STATUS_IN_TRANSIT);
                    // create port entry permission
                    try {
                        anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_DIRECT,
                            systemService.getPortOperatorAnprZoneIdById(trip.getPortOperatorGateId()), trip, null, null, new Date());

                    } catch (Exception e) {
                        logger.error("saveTransporterPayment#scheduleAnpr###Exception: ", e);
                    }
                }
                trip.setLaneSessionId(session.getId());

                if (trip.getAccountId() == ServerConstants.DEFAULT_LONG) {
                    trip.setFeePaid(true);
                    trip.setDateFeePaid(new Date());

                } else {
                    if (session.getType() == ServerConstants.SESSION_TYPE_VIRTUAL) {
                        statementService.chargeParkingFee(trip, operatorId);

                        session.setAccountDeductTransactionCount(session.getAccountDeductTransactionCount() + 1);
                        session.setAccountDeductTransactionTotalAmount(session.getAccountDeductTransactionTotalAmount().add(trip.getFeeAmount()));

                    } else {
                        trip.setFeePaid(true);
                        trip.setDateFeePaid(new Date());
                    }
                }

                tripService.updateTrip(trip);

                paymentJson.setCode(payment.getCode());

                sessionService.updateSession(session);

                String typePaymentString = ServerUtil.getPaymentOptionDescriptionById(paymentJson.getPaymentOption(), ServerConstants.LANGUAGE_FR_ID);
                String mobileNumber = ServerUtil.getValidNumber(paymentJson.getMsisdn(), "saveTransporterPayment");

                String itemDescription = ServerConstants.DEFAULT_STRING;
                if (account == null) {
                    String portOperatorName = systemService.getPortOperatorNameById(trip.getPortOperatorId());
                    String transactionType = ServerUtil.getTransactionTypeName(trip.getTransactionType(), ServerConstants.LANGUAGE_FR_ID);
                    itemDescription = "Voyage pour opérateur portuaire: " + portOperatorName + "; Type: " + transactionType + ";";
                } else {
                    itemDescription = "Recharge de compte";
                }

                receiptService.createReceipt(account, payment.getId(), paymentJson.getFirstName(), paymentJson.getLastName(), mobileNumber, paymentJson.getPaymentOption(),
                    typePaymentString, itemDescription, 1, dueAmount, ServerConstants.CURRENCY_CFA_FRANC, dueAmount, paymentAmount, changeDueAmount, trip.getVehicleRegistration(),
                    trip.getVehicleRegistrationCountryISO(), operatorId);

                activityLogService.saveActivityLogTrip(ServerConstants.ACTIVITY_LOG_TOP_UP, operatorId, trip.getId());

                break;

            case ServerConstants.PAYMENT_OPTION_ORANGE_MONEY:
            case ServerConstants.PAYMENT_OPTION_FREE_MONEY:

                paymentJson.setMnoId(ServerUtil.getMnoIdFromPaymentOption(paymentJson.getPaymentOption()));

                trip.setLaneSessionId(session.getId());

                tripService.updateTrip(trip);

                payment = new Payment();
                initDefaultPayment(payment, paymentJson, trip.getAccountId(), operatorId, paymentAmount);
                payment.setLaneSessionId(session.getId());
                payment.setMissionId(trip.getMission().getId());
                payment.setTripId(trip.getId());
                payment.setType(
                    trip.getAccountId() == ServerConstants.DEFAULT_LONG ? ServerConstants.PAYMENT_TYPE_NO_ACCOUNT_ADHOC_TRIP_FEE : ServerConstants.PAYMENT_TYPE_ACCOUNT_TOPUP);
                payment.setMsisdn(ServerUtil.getValidNumber(paymentJson.getMsisdn(), "saveTransporterPayment"));
                payment.setResponseCode(ServerConstants.DEFAULT_INT);
                payment.setDateResponse(null);

                savePayment(payment);

                result = onlinePaymentService.processOnlinePayment(trip, paymentJson, paymentAmount, payment.getId());

                activityLogService.saveActivityLogTrip(ServerConstants.ACTIVITY_LOG_TOP_UP, operatorId, trip.getId());

                break;
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = { PADException.class, PADValidationException.class, Exception.class }, noRollbackFor = { PADOnlinePaymentException.class })
    public String accountTopup(PaymentJson paymentJson, long operatorId) throws PADException, PADValidationException, PADOnlinePaymentException {

        String onlinePaymentCode = "";

        BigDecimal topupAmount = BigDecimal.valueOf(paymentJson.getTopupAmount());

        Account account = accountService.getAccountByCode(paymentJson.getAccountCode());

        if (account == null)
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#1");

        if (account.getStatus() != ServerConstants.ACCOUNT_STATUS_ACTIVE)
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#2");

        Payment payment = new Payment();

        switch (paymentJson.getPaymentOption()) {
            case ServerConstants.PAYMENT_OPTION_CASH:
                makeCashPayment(account, payment, paymentJson, operatorId, topupAmount);
                sendTransactionNotifyEmail(account, payment);
                break;

            case ServerConstants.PAYMENT_OPTION_ORANGE_MONEY:
            case ServerConstants.PAYMENT_OPTION_WARI:
            case ServerConstants.PAYMENT_OPTION_FREE_MONEY:
            case ServerConstants.PAYMENT_OPTION_E_MONEY:
            case ServerConstants.PAYMENT_OPTION_ECOBANK:
                onlinePaymentCode = makeOnlinePayment(account, payment, paymentJson, operatorId, topupAmount);
                break;

            case ServerConstants.PAYMENT_OPTION_ACCOUNT_CREDIT:
            case ServerConstants.PAYMENT_OPTION_BANK_TRANSFER:
            case ServerConstants.PAYMENT_OPTION_CHEQUE:
                makeCreditOrBankTransferOrChequePayment(account, payment, paymentJson, operatorId, topupAmount);
                sendTransactionNotifyEmail(account, payment);
                break;

            case ServerConstants.PAYMENT_OPTION_CASH_REFUND:
            case ServerConstants.PAYMENT_OPTION_ACCOUNT_DEBIT:
                makeRefundOrDebit(account, payment, paymentJson, operatorId, topupAmount);
                sendTransactionNotifyEmail(account, payment);
                break;
        }

        return onlinePaymentCode;
    }

    private void makeCashPayment(Account account, Payment payment, PaymentJson paymentJson, long operatorId, BigDecimal topupAmount) throws PADValidationException, PADException {
        BigDecimal cashGivenAmount = BigDecimal.valueOf(paymentJson.getPaymentAmount());
        BigDecimal changeDueAmount = BigDecimal.valueOf(paymentJson.getChangeDueAmount());

        if (cashGivenAmount.compareTo(BigDecimal.ZERO) <= 0 || topupAmount.compareTo(BigDecimal.ZERO) < 0 || changeDueAmount.compareTo(BigDecimal.ZERO) < 0)
            throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#3");

        if (cashGivenAmount.subtract(topupAmount).compareTo(changeDueAmount) != ServerConstants.ZERO_INT)
            throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#4");

        BigDecimal accountBalanceAmount = account.getBalanceAmount();

        account.setBalanceAmount(accountBalanceAmount.add(topupAmount));
        account.setDateEdited(new Date());

        initDefaultPayment(payment, paymentJson, account.getId(), operatorId, topupAmount);
        payment.setAmountPayment(cashGivenAmount);
        payment.setAmountChangeDue(changeDueAmount);
        savePayment(payment);

        Statement statement = initDefaultStatement(payment, paymentJson.getPaymentNote(), account.getId(), account.getBalanceAmount(), operatorId, topupAmount);
        statement.setType(ServerConstants.PAYMENT_TYPE_ACCOUNT_TOPUP);
        statement.setNotes(ServerConstants.DEFAULT_STRING);
        statementService.saveStatement(statement);

        accountService.updateAccount(account);

        String typePaymentString = ServerUtil.getPaymentOptionDescriptionById(paymentJson.getPaymentOption(), ServerConstants.LANGUAGE_FR_ID);
        String itemDescription = "Recharge de compte";
        String mobileNumber = ServerUtil.getValidNumber(paymentJson.getMsisdn(), "accountTopup");

        receiptService.createReceipt(account, payment.getId(), paymentJson.getFirstName(), paymentJson.getLastName(), mobileNumber, paymentJson.getPaymentOption(),
            typePaymentString, itemDescription, 1, topupAmount, ServerConstants.CURRENCY_CFA_FRANC, topupAmount, cashGivenAmount, changeDueAmount, ServerConstants.DEFAULT_STRING,
            ServerConstants.DEFAULT_STRING, operatorId);

        activityLogService.saveActivityLogStatement(ServerConstants.ACTIVITY_LOG_TOP_UP_FINANCE_OPERATOR, operatorId, statement.getId());
    }

    private String makeOnlinePayment(Account account, Payment payment, PaymentJson paymentJson, long operatorId, BigDecimal topupAmount)
        throws PADValidationException, PADException, PADOnlinePaymentException {
        Trip tmpTrip = new Trip();
        tmpTrip.setId(ServerConstants.DEFAULT_LONG);
        tmpTrip.setAccountId(account.getId());
        tmpTrip.setDriverId(ServerConstants.DEFAULT_LONG);

        Mission tmpMission = new Mission();
        tmpMission.setId(ServerConstants.DEFAULT_LONG);

        tmpTrip.setMission(tmpMission);

        paymentJson.setMnoId(ServerUtil.getMnoIdFromPaymentOption(paymentJson.getPaymentOption()));

        initDefaultPayment(payment, paymentJson, account.getId(), operatorId, topupAmount);
        payment.setResponseCode(ServerConstants.DEFAULT_INT);
        payment.setDateResponse(null); // will init later
        savePayment(payment);

        String onlinePaymentCode = onlinePaymentService.processOnlinePayment(tmpTrip, paymentJson, topupAmount, payment.getId());

        activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_TOP_UP_FINANCE_OPERATOR, operatorId, account.getId());

        return onlinePaymentCode;
    }

    private void makeCreditOrBankTransferOrChequePayment(Account account, Payment payment, PaymentJson paymentJson, long operatorId, BigDecimal topupAmount)
        throws PADValidationException, PADException {
        account.setBalanceAmount(account.getBalanceAmount().add(topupAmount));
        account.setDateEdited(new Date());

        initDefaultPayment(payment, paymentJson, account.getId(), operatorId, topupAmount);
        payment.setType(ServerConstants.PAYMENT_TYPE_ACCOUNT_CREDIT);
        payment.setNotes(paymentJson.getPaymentNote());
        savePayment(payment);

        Statement statement = initDefaultStatement(payment, paymentJson.getPaymentNote(), account.getId(), account.getBalanceAmount(), operatorId, topupAmount);
        statement.setType(ServerConstants.PAYMENT_TYPE_ACCOUNT_CREDIT);
        statementService.saveStatement(statement);

        accountService.updateAccount(account);
        activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_ACCOUNT_CREDIT, operatorId, account.getId());
    }

    private void makeRefundOrDebit(Account account, Payment payment, PaymentJson paymentJson, long operatorId, BigDecimal topupAmount) throws PADValidationException, PADException {
        if (paymentJson.getPaymentOption() == ServerConstants.PAYMENT_OPTION_CASH_REFUND) {
            BigDecimal accountBalanceMinusRefundAmount = account.getBalanceAmount().subtract(topupAmount);

            if (accountBalanceMinusRefundAmount.compareTo(BigDecimal.ZERO) < 0) {

                if (accountBalanceMinusRefundAmount.abs().compareTo(account.getAmountOverdraftLimit()) > 0)
                    throw new PADValidationException(ServerResponseConstants.ACCOUNT_REFUND_AMOUNT_EXCEED_AVAILABLE_CREDIT_CODE,
                        ServerResponseConstants.ACCOUNT_REFUND_AMOUNT_EXCEED_AVAILABLE_CREDIT_TEXT, "");
            }
        }
        BigDecimal newBalanceAmount = account.getBalanceAmount().subtract(topupAmount);
        accountService.notifyAccountWhenLowBalance(account, newBalanceAmount);
        account.setBalanceAmount(newBalanceAmount);
        account.setDateEdited(new Date());

        initDefaultPayment(payment, paymentJson, account.getId(), operatorId, topupAmount);
        payment.setType(ServerConstants.PAYMENT_TYPE_ACCOUNT_DEBIT);
        payment.setAmountPayment(topupAmount.negate());
        payment.setAmountDue(topupAmount.negate());
        payment.setNotes(paymentJson.getPaymentNote());
        savePayment(payment);

        Statement statement = initDefaultStatement(payment, paymentJson.getPaymentNote(), account.getId(), account.getBalanceAmount(), operatorId, topupAmount);
        statement.setType(ServerConstants.PAYMENT_TYPE_ACCOUNT_DEBIT);
        statement.setAmountDebit(topupAmount);
        statement.setAmountCredit(BigDecimal.ZERO);
        statementService.saveStatement(statement);

        accountService.updateAccount(account);
        activityLogService.saveActivityLog(ServerConstants.ACTIVITY_LOG_ACCOUNT_DEBIT, operatorId, account.getId());
    }

    private Statement initDefaultStatement(Payment payment, String paymentJsonNote, long accountId, BigDecimal balanceAmount, long operatorId, BigDecimal topupAmount) {
        Statement statement = new Statement();
        statement.setCode(SecurityUtil.generateUniqueCode());
        statement.setPaymentId(payment.getId());
        statement.setAccountId(accountId);
        statement.setMissionId(ServerConstants.DEFAULT_LONG);
        statement.setTripId(ServerConstants.DEFAULT_LONG);
        statement.setOperatorId(operatorId);
        statement.setCurrency(ServerConstants.CURRENCY_CFA_FRANC);
        statement.setAmountDebit(BigDecimal.ZERO);
        statement.setAmountCredit(topupAmount);
        statement.setAmountRunningBalance(balanceAmount);
        statement.setNotes(paymentJsonNote);
        statement.setDateCreated(new Date());
        statement.setDateEdited(statement.getDateCreated());
        return statement;
    }

    private void initDefaultPayment(Payment payment, PaymentJson paymentJson, long accountId, long operatorId, BigDecimal topupAmount) throws PADException, PADValidationException {
        payment.setCode(SecurityUtil.generateUniqueCode());
        payment.setLaneSessionId(ServerConstants.DEFAULT_LONG);
        payment.setAccountId(accountId);
        payment.setMissionId(ServerConstants.DEFAULT_LONG);
        payment.setTripId(ServerConstants.DEFAULT_LONG);
        payment.setOperatorId(operatorId);
        payment.setType(ServerConstants.PAYMENT_TYPE_ACCOUNT_TOPUP);
        payment.setPaymentOption(paymentJson.getPaymentOption());
        payment.setCurrency(ServerConstants.CURRENCY_CFA_FRANC);
        payment.setAmountPayment(topupAmount);
        payment.setAmountDue(topupAmount);
        payment.setAmountChangeDue(BigDecimal.ZERO);
        payment.setNotes(ServerConstants.DEFAULT_STRING);
        payment.setFirstName(paymentJson.getFirstName());
        payment.setLastName(paymentJson.getLastName());
        payment.setMsisdn(ServerUtil.getValidNumber(paymentJson.getMsisdn(), "accountTopup"));
        payment.setResponseCode(ServerResponseConstants.MNO_RESPONSE_CODE_SUCCESS);
        payment.setDateResponse(new Date());
        payment.setDateCreated(new Date());
        payment.setDateEdited(payment.getDateCreated());
    }

    @Override
    public void sendTransactionNotifyEmail(Account account, Payment payment) throws PADException {
        if (payment == null) throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "#20");
        Email email = new Email();
        email.setEmailTo(systemService.getSystemParameter().getAgsFinanceEmail());
        email.setLanguageId(ServerConstants.LANGUAGE_FR_ID);
        email.setAccountId(account == null ? ServerConstants.DEFAULT_INT : account.getId());
        email.setAttachmentPath(ServerConstants.DEFAULT_STRING);
        email.setDateScheduled(new Date());
        email.setMissionId(ServerConstants.DEFAULT_LONG);
        email.setTripId(ServerConstants.DEFAULT_LONG);
        HashMap<String, Object> params = new HashMap<>();
        params.put("paymentOptionDescription", getPaymentOptionDescription(payment.getPaymentOption(), email.getLanguageId()));
        Operator operator = operatorService.getOperator(SecurityUtil.getSystemUsername());
        params.put("operatorName", operator == null ? ServerConstants.DEFAULT_STRING : operator.getFirstName() + " " + operator.getLastName());
        params.put("amount", payment.getAmountDue());
        params.put("accountNumber", account == null ? ServerConstants.DEFAULT_LONG : account.getNumber());
        if (account != null) {
            if (account.getType() == ServerConstants.ACCOUNT_TYPE_INDIVIDUAL) {
                params.put("accountName", account.getFirstName() + " " + account.getLastName());
            } else {
                params.put("accountName", account.getCompanyName());
            }
        } else {
            params.put("accountName", ServerConstants.DEFAULT_STRING);
        }
        params.put("comments", payment.getNotes());
        emailService.scheduleEmailByType(email, ServerConstants.EMAIL_FINANCE_USER_TRANSACTIONS_NOTIFICATION_TYPE, params);
    }

    private String getPaymentOptionDescription(int index, long languageId) {
        if (languageId == ServerConstants.LANGUAGE_FR_ID) {
            switch (index) {
                case 1:
                    return "En espèces";

                case 2:
                    return "Orange Money";

                case 3:
                    return "Wari";

                case 4:
                    return "Free Money";

                case 5:
                    return "E-money";

                case 6:
                    return "Ecobank";

                case 7:
                    return "Virement";

                case 8:
                    return "Chèque";

                case 9:
                    return "Сrédit de compte";

                case 10:
                    return "Remboursement en espèces";

                case 11:
                    return "Débit du compte";
            }
        } else {
            switch (index) {
                case 1:
                    return "Cash";

                case 2:
                    return "Orange Money";

                case 3:
                    return "Wari";

                case 4:
                    return "Free Money";

                case 5:
                    return "E-money";

                case 6:
                    return "Ecobank";

                case 7:
                    return "Bank transfer";

                case 8:
                    return "Cheque";

                case 9:
                    return "Account credit";

                case 10:
                    return "Cash refund";

                case 11:
                    return "Account debit";
            }
        }

        return "";
    }

    @SuppressWarnings("unchecked")
    @Override
    public Payment getPaymentByCode(String paymentCode) {
        Payment payment = null;

        List<Payment> paymentList = (List<Payment>) hibernateTemplate.findByNamedParam("FROM Payment WHERE code = :code", "code", paymentCode);

        if (paymentList != null && !paymentList.isEmpty()) {
            payment = paymentList.get(0);
        }
        return payment;
    }

    @Override
    public Payment getPaymentById(long paymentId) {

        return hibernateTemplate.get(Payment.class, paymentId);
    }

    @Override
    public List<PaymentJson> getAccountPayments(long accountId) {

        List<PaymentJson> payments = new ArrayList<>();

        StringBuffer query = new StringBuffer();
        query.append(" SELECT p.id, p.payment_option, p.amount_due, p.currency, p.msisdn, p.notes, p.date_created, op.mno_id, ");
        query.append("      op.fee_aggregator, op.reference_aggregator, op.status_aggregator, op.error_aggregator, op.date_callback_response ");
        query.append(" FROM pad.payments p ");
        query.append(" LEFT JOIN pad.online_payments op ON p.id = op.payment_id ");
        query.append(" WHERE p.account_id = ? ");
        query.append(" ORDER BY p.id DESC LIMIT 10 ");

        jdbcTemplate.query(query.toString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {

                PaymentJson paymentJson = new PaymentJson();
                paymentJson.setMnoId(rs.getObject("mno_id") == null ? ServerConstants.DEFAULT_LONG : rs.getLong("mno_id"));
                paymentJson.setPaymentOption(rs.getInt("payment_option"));
                paymentJson.setMsisdn(rs.getString("msisdn"));
                paymentJson.setPaymentNote(rs.getString("notes"));
                paymentJson.setCurrencyCode(rs.getString("currency"));
                paymentJson.setAmount(rs.getBigDecimal("amount_due"));
                paymentJson.setFeeAggregator(rs.getBigDecimal("fee_aggregator") == null ? new BigDecimal(ServerConstants.ZERO_INT) : rs.getBigDecimal("fee_aggregator"));
                paymentJson.setReferenceAggregator(rs.getString("reference_aggregator") == null ? ServerConstants.DEFAULT_STRING : rs.getString("reference_aggregator"));
                try {
                    paymentJson.setDateRequestString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmmss, rs.getTimestamp("date_created")));
                } catch (ParseException e) {
                    paymentJson.setDateRequestString(ServerConstants.DEFAULT_STRING);
                }
                paymentJson.setStatusAggregator(rs.getString("status_aggregator") == null ? ServerConstants.DEFAULT_STRING : rs.getString("status_aggregator"));
                paymentJson.setErrorAggregator(rs.getString("error_aggregator") == null ? ServerConstants.DEFAULT_STRING : rs.getString("error_aggregator"));
                try {
                    paymentJson.setDateCallbackResponseString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmmss, rs.getTimestamp("date_callback_response")));
                } catch (Exception e) {
                    paymentJson.setDateCallbackResponseString(ServerConstants.DEFAULT_STRING);
                }

                payments.add(paymentJson);
            }
        }, accountId);

        return payments;

    }

    @Override
    public Long getAccountPaymentsCount(AccountPaymentJson accountPaymentJson) throws PADException {
        PreparedJDBCQuery query = getAccountPaymentsQuery(PreparedJDBCQuery.QUERY_TYPE_COUNT, accountPaymentJson);

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Long.class);
    }

    @Override
    public List<AccountPaymentJson> getAccountPaymentsList(AccountPaymentJson accountPaymentJson) throws PADException {
        PreparedJDBCQuery query = getAccountPaymentsQuery(PreparedJDBCQuery.QUERY_TYPE_SELECT, accountPaymentJson);

        if (StringUtils.isBlank(accountPaymentJson.getSortColumn())) {
            query.append(" ORDER BY p.id DESC");
        } else {
            String sortColumn = accountPaymentJson.getSortColumn();
            String order = accountPaymentJson.getSortAsc() ? " ASC" : " DESC";
            if ("channel".equals(sortColumn)) {
                query.append(" ORDER BY FIELD(lane_session_id, -1) ").append(order).append(", FIELD(role_id, 1) ").append(order);
            } else if ("notes".equals(sortColumn)) {
                query.append(" ORDER BY p.notes ").append(order).append(", op.error_aggregator ").append(order);
            } else {
                query.append(" ORDER BY ").append(sortColumn).append(order);
            }
        }
        query.setLimitParameters(accountPaymentJson.getCurrentPage(), accountPaymentJson.getPageCount());

        List<AccountPaymentJson> paymentList = new ArrayList<>();
        jdbcTemplate.query(query.getQueryString(), rs -> {
            AccountPaymentJson paymentJson = new AccountPaymentJson();

            paymentJson.setDatePayment(rs.getTimestamp("date_created"));
            try {
                paymentJson.setDatePaymentString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("date_created")));
            } catch (ParseException e) {
                paymentJson.setDatePaymentString("");
            }
            paymentJson.setOperatorName(rs.getString("operator"));
            paymentJson.setLaneSessionId(rs.getInt("lane_session_id"));
            paymentJson.setRoleId(rs.getInt("role_id"));
            paymentJson.setType(rs.getInt("type"));
            paymentJson.setPaymentOption(rs.getInt("payment_option"));
            paymentJson.setFirstName(rs.getString("first_name"));
            paymentJson.setLastName(rs.getString("last_name"));
            paymentJson.setMsisdn(rs.getString("msisdn"));
            paymentJson.setAmount(rs.getBigDecimal("amount_due"));
            paymentJson.setFeeAggregator(rs.getBigDecimal("fee_aggregator"));
            paymentJson.setReferenceAggregator(rs.getString("reference_aggregator"));
            paymentJson.setStatusAggregator(rs.getString("status_aggregator"));
            paymentJson.setErrorAggregator(rs.getString("error_aggregator"));
            paymentJson.setNotes(rs.getString("notes"));
            paymentJson.setDateResponse(rs.getTimestamp("date_response"));
            try {
                paymentJson.setDateResponseString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("date_response")));
            } catch (ParseException e) {
                paymentJson.setDatePaymentString("");
            }

            paymentList.add(paymentJson);
        }, query.getQueryParameters());
        return paymentList;
    }

    private PreparedJDBCQuery getAccountPaymentsQuery(int queryType, AccountPaymentJson accountPaymentJson) throws PADException {

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        if (queryType == PreparedJDBCQuery.QUERY_TYPE_COUNT) {
            query.append("SELECT COUNT(p.id)");
            query.append(" FROM pad.payments p ");
        } else if (queryType == PreparedJDBCQuery.QUERY_TYPE_SELECT) {
            query.append("SELECT p.date_created, CONCAT(o.first_name, ' ', o.last_name) AS operator, p.lane_session_id, o.role_id, p.type, ");
            query.append("p.payment_option, p.first_name, p.last_name, p.msisdn, p.amount_due, op.fee_aggregator, ");
            query.append("op.reference_aggregator, op.status_aggregator, op.error_aggregator, p.notes, p.date_response ");
            query.append("FROM pad.payments p ");
            query.append("LEFT JOIN pad.online_payments op ON op.payment_id = p.id ");
            query.append("LEFT JOIN pad.operators o ON o.id = p.operator_id ");
        }
        query.append("WHERE p.account_id = ? AND p.date_created >= ? AND p.date_created < ?");

        Account account = accountService.getAccountByCode(accountPaymentJson.getAccountCode());
        query.setAccountParameters(account, accountPaymentJson.getDateFromString(), accountPaymentJson.getDateToString());

        if (accountPaymentJson.getType() != null) {
            query.append(" AND p.type = ?");
            query.addQueryParameter(accountPaymentJson.getType());
        }
        if (accountPaymentJson.getPaymentOption() != null) {
            query.append(" AND p.payment_option = ?");
            query.addQueryParameter(accountPaymentJson.getPaymentOption());
        }

        return query;
    }
}
