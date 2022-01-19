package com.pad.server.base.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "sessions")
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "code")
    private String            code;

    @Column(name = "type")
    private int               type;

    @Column(name = "status")
    private int               status;

    @Column(name = "kiosk_operator_id")
    private long              kioskOperatorId;

    @Column(name = "operator_id")
    private long              operatorId;

    @Column(name = "lane_id")
    private long              laneId;

    @Column(name = "lane_number")
    private int               laneNumber;

    @Column(name = "cash_bag_number")
    private String            cashBagNumber;

    @Column(name = "cash_amount_start")
    private BigDecimal        cashAmountStart;

    @Column(name = "cash_amount_end")
    private BigDecimal        cashAmountEnd;

    @Column(name = "validation_step")
    private int               validationStep;

    @Column(name = "reason_amount_unexpected")
    private String            reasonAmountUnexpected;

    @Column(name = "no_account_cash_transaction_count")
    private int               noAccountCashTransactionCount;

    @Column(name = "no_account_online_transaction_count")
    private int               noAccountOnlineTransactionCount;

    @Column(name = "account_cash_transaction_count")
    private int               accountCashTransactionCount;

    @Column(name = "account_online_transaction_count")
    private int               accountOnlineTransactionCount;

    @Column(name = "account_deduct_transaction_count")
    private int               accountDeductTransactionCount;

    @Column(name = "exit_only_session_count")
    private int               exitOnlySessionCount;

    @Column(name = "adhoc_trips_created_count")
    private int               adhocTripsCreatedCount;

    @Column(name = "adhoc_trips_cancelled_count")
    private int               adhocTripsCancelledCount;

    @Column(name = "no_account_cash_transaction_total_amount")
    private BigDecimal        noAccountCashTransactionTotalAmount;

    @Column(name = "no_account_online_transaction_total_amount")
    private BigDecimal        noAccountOnlineTransactionTotalAmount;

    @Column(name = "account_cash_transaction_total_amount")
    private BigDecimal        accountCashTransactionTotalAmount;

    @Column(name = "account_online_transaction_total_amount")
    private BigDecimal        accountOnlineTransactionTotalAmount;

    @Column(name = "account_deduct_transaction_total_amount")
    private BigDecimal        accountDeductTransactionTotalAmount;

    @Column(name = "cash_change_given_total_amount")
    private BigDecimal        cashChangeGivenTotalAmount;

    @Column(name = "date_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateStart;

    @Column(name = "date_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateEnd;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getKioskOperatorId() {
        return kioskOperatorId;
    }

    public void setKioskOperatorId(long kioskOperatorId) {
        this.kioskOperatorId = kioskOperatorId;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public long getLaneId() {
        return laneId;
    }

    public void setLaneId(long laneId) {
        this.laneId = laneId;
    }

    public int getLaneNumber() {
        return laneNumber;
    }

    public void setLaneNumber(int laneNumber) {
        this.laneNumber = laneNumber;
    }

    public String getCashBagNumber() {
        return cashBagNumber;
    }

    public void setCashBagNumber(String cashBagNumber) {
        this.cashBagNumber = cashBagNumber;
    }

    public BigDecimal getCashAmountStart() {
        return cashAmountStart;
    }

    public void setCashAmountStart(BigDecimal cashAmountStart) {
        this.cashAmountStart = cashAmountStart;
    }

    public BigDecimal getCashAmountEnd() {
        return cashAmountEnd;
    }

    public void setCashAmountEnd(BigDecimal cashAmountEnd) {
        this.cashAmountEnd = cashAmountEnd;
    }

    public int getValidationStep() {
        return validationStep;
    }

    public void setValidationStep(int validationStep) {
        this.validationStep = validationStep;
    }

    public String getReasonAmountUnexpected() {
        return reasonAmountUnexpected;
    }

    public void setReasonAmountUnexpected(String reasonAmountUnexpected) {
        this.reasonAmountUnexpected = reasonAmountUnexpected;
    }

    public int getNoAccountCashTransactionCount() {
        return noAccountCashTransactionCount;
    }

    public void setNoAccountCashTransactionCount(int noAccountCashTransactionCount) {
        this.noAccountCashTransactionCount = noAccountCashTransactionCount;
    }

    public int getNoAccountOnlineTransactionCount() {
        return noAccountOnlineTransactionCount;
    }

    public void setNoAccountOnlineTransactionCount(int noAccountOnlineTransactionCount) {
        this.noAccountOnlineTransactionCount = noAccountOnlineTransactionCount;
    }

    public int getAccountCashTransactionCount() {
        return accountCashTransactionCount;
    }

    public void setAccountCashTransactionCount(int accountCashTransactionCount) {
        this.accountCashTransactionCount = accountCashTransactionCount;
    }

    public int getAccountOnlineTransactionCount() {
        return accountOnlineTransactionCount;
    }

    public void setAccountOnlineTransactionCount(int accountOnlineTransactionCount) {
        this.accountOnlineTransactionCount = accountOnlineTransactionCount;
    }

    public int getAccountDeductTransactionCount() {
        return accountDeductTransactionCount;
    }

    public void setAccountDeductTransactionCount(int accountDeductTransactionCount) {
        this.accountDeductTransactionCount = accountDeductTransactionCount;
    }

    public int getExitOnlySessionCount() {
        return exitOnlySessionCount;
    }

    public void setExitOnlySessionCount(int exitOnlySessionCount) {
        this.exitOnlySessionCount = exitOnlySessionCount;
    }

    public int getAdhocTripsCreatedCount() {
        return adhocTripsCreatedCount;
    }

    public void setAdhocTripsCreatedCount(int adhocTripsCreatedCount) {
        this.adhocTripsCreatedCount = adhocTripsCreatedCount;
    }

    public int getAdhocTripsCancelledCount() {
        return adhocTripsCancelledCount;
    }

    public void setAdhocTripsCancelledCount(int adhocTripsCancelledCount) {
        this.adhocTripsCancelledCount = adhocTripsCancelledCount;
    }

    public BigDecimal getNoAccountCashTransactionTotalAmount() {
        return noAccountCashTransactionTotalAmount;
    }

    public void setNoAccountCashTransactionTotalAmount(BigDecimal noAccountCashTransactionTotalAmount) {
        this.noAccountCashTransactionTotalAmount = noAccountCashTransactionTotalAmount;
    }

    public BigDecimal getNoAccountOnlineTransactionTotalAmount() {
        return noAccountOnlineTransactionTotalAmount;
    }

    public void setNoAccountOnlineTransactionTotalAmount(BigDecimal noAccountOnlineTransactionTotalAmount) {
        this.noAccountOnlineTransactionTotalAmount = noAccountOnlineTransactionTotalAmount;
    }

    public BigDecimal getAccountCashTransactionTotalAmount() {
        return accountCashTransactionTotalAmount;
    }

    public void setAccountCashTransactionTotalAmount(BigDecimal accountCashTransactionTotalAmount) {
        this.accountCashTransactionTotalAmount = accountCashTransactionTotalAmount;
    }

    public BigDecimal getAccountOnlineTransactionTotalAmount() {
        return accountOnlineTransactionTotalAmount;
    }

    public void setAccountOnlineTransactionTotalAmount(BigDecimal accountOnlineTransactionTotalAmount) {
        this.accountOnlineTransactionTotalAmount = accountOnlineTransactionTotalAmount;
    }

    public BigDecimal getAccountDeductTransactionTotalAmount() {
        return accountDeductTransactionTotalAmount;
    }

    public void setAccountDeductTransactionTotalAmount(BigDecimal accountDeductTransactionTotalAmount) {
        this.accountDeductTransactionTotalAmount = accountDeductTransactionTotalAmount;
    }

    public BigDecimal getCashChangeGivenTotalAmount() {
        return cashChangeGivenTotalAmount;
    }

    public void setCashChangeGivenTotalAmount(BigDecimal cashChangeGivenTotalAmount) {
        this.cashChangeGivenTotalAmount = cashChangeGivenTotalAmount;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Session [id=");
        builder.append(id);
        builder.append(", code=");
        builder.append(code);
        builder.append(", type=");
        builder.append(type);
        builder.append(", status=");
        builder.append(status);
        builder.append(", kioskOperatorId=");
        builder.append(kioskOperatorId);
        builder.append(", operatorId=");
        builder.append(operatorId);
        builder.append(", laneId=");
        builder.append(laneId);
        builder.append(", laneNumber=");
        builder.append(laneNumber);
        builder.append(", cashBagNumber=");
        builder.append(cashBagNumber);
        builder.append(", cashAmountStart=");
        builder.append(cashAmountStart);
        builder.append(", cashAmountEnd=");
        builder.append(cashAmountEnd);
        builder.append(", validationStep=");
        builder.append(validationStep);
        builder.append(", reasonAmountUnexpected=");
        builder.append(reasonAmountUnexpected);
        builder.append(", noAccountCashTransactionCount=");
        builder.append(noAccountCashTransactionCount);
        builder.append(", noAccountOnlineTransactionCount=");
        builder.append(noAccountOnlineTransactionCount);
        builder.append(", accountCashTransactionCount=");
        builder.append(accountCashTransactionCount);
        builder.append(", accountOnlineTransactionCount=");
        builder.append(accountOnlineTransactionCount);
        builder.append(", accountDeductTransactionCount=");
        builder.append(accountDeductTransactionCount);
        builder.append(", exitOnlySessionCount=");
        builder.append(exitOnlySessionCount);
        builder.append(", adhocTripsCreatedCount=");
        builder.append(adhocTripsCreatedCount);
        builder.append(", adhocTripsCancelledCount=");
        builder.append(adhocTripsCancelledCount);
        builder.append(", noAccountCashTransactionTotalAmount=");
        builder.append(noAccountCashTransactionTotalAmount);
        builder.append(", noAccountOnlineTransactionTotalAmount=");
        builder.append(noAccountOnlineTransactionTotalAmount);
        builder.append(", accountCashTransactionTotalAmount=");
        builder.append(accountCashTransactionTotalAmount);
        builder.append(", accountOnlineTransactionTotalAmount=");
        builder.append(accountOnlineTransactionTotalAmount);
        builder.append(", accountDeductTransactionTotalAmount=");
        builder.append(accountDeductTransactionTotalAmount);
        builder.append(", cashChangeGivenTotalAmount=");
        builder.append(cashChangeGivenTotalAmount);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateStart=");
        builder.append(dateStart);
        builder.append(", dateEnd=");
        builder.append(dateEnd);
        builder.append("]");
        return builder.toString();
    }

}
