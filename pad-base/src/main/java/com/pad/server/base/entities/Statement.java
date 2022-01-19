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
@Table(name = "statements")
public class Statement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "code")
    private String            code;

    @Column(name = "type")
    private int               type;

    @Column(name = "payment_id")
    private long              paymentId;

    @Column(name = "account_id")
    private long              accountId;

    @Column(name = "mission_id")
    private long              missionId;

    @Column(name = "trip_id")
    private long              tripId;

    @Column(name = "operator_id")
    private long              operatorId;

    @Column(name = "currency")
    private String            currency;

    @Column(name = "amount_credit")
    private BigDecimal        amountCredit;

    @Column(name = "amount_debit")
    private BigDecimal        amountDebit;

    @Column(name = "amount_running_balance")
    private BigDecimal        amountRunningBalance;

    @Column(name = "notes")
    private String            notes;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    @Column(name = "date_edited")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateEdited;

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

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmountCredit() {
        return amountCredit;
    }

    public void setAmountCredit(BigDecimal amountCredit) {
        this.amountCredit = amountCredit;
    }

    public BigDecimal getAmountDebit() {
        return amountDebit;
    }

    public void setAmountDebit(BigDecimal amountDebit) {
        this.amountDebit = amountDebit;
    }

    public BigDecimal getAmountRunningBalance() {
        return amountRunningBalance;
    }

    public void setAmountRunningBalance(BigDecimal amountRunningBalance) {
        this.amountRunningBalance = amountRunningBalance;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(Date dateEdited) {
        this.dateEdited = dateEdited;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Statement [id=");
        builder.append(id);
        builder.append(", code=");
        builder.append(code);
        builder.append(", type=");
        builder.append(type);
        builder.append(", paymentId=");
        builder.append(paymentId);
        builder.append(", accountId=");
        builder.append(accountId);
        builder.append(", missionId=");
        builder.append(missionId);
        builder.append(", tripId=");
        builder.append(tripId);
        builder.append(", operatorId=");
        builder.append(operatorId);
        builder.append(", currency=");
        builder.append(currency);
        builder.append(", amountCredit=");
        builder.append(amountCredit);
        builder.append(", amountDebit=");
        builder.append(amountDebit);
        builder.append(", amountRunningBalance=");
        builder.append(amountRunningBalance);
        builder.append(", notes=");
        builder.append(notes);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateEdited=");
        builder.append(dateEdited);
        builder.append("]");
        return builder.toString();
    }

}
