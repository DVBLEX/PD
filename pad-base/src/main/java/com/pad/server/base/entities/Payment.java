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
@Table(name = "payments")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "code")
    private String            code;

    @Column(name = "lane_session_id")
    private long              laneSessionId;

    @Column(name = "account_id")
    private long              accountId;

    @Column(name = "mission_id")
    private long              missionId;

    @Column(name = "trip_id")
    private long              tripId;

    @Column(name = "operator_id")
    private long              operatorId;

    @Column(name = "type")
    private int               type;

    @Column(name = "payment_option")
    private int               paymentOption;

    @Column(name = "currency")
    private String            currency;

    @Column(name = "amount_due")
    private BigDecimal        amountDue;

    @Column(name = "amount_payment")
    private BigDecimal        amountPayment;

    @Column(name = "amount_change_due")
    private BigDecimal        amountChangeDue;

    @Column(name = "notes")
    private String            notes;

    @Column(name = "first_name")
    private String            firstName;

    @Column(name = "last_name")
    private String            lastName;

    @Column(name = "msisdn")
    private String            msisdn;

    @Column(name = "response_code")
    private int               responseCode;

    @Column(name = "date_response")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateResponse;

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

    public long getLaneSessionId() {
        return laneSessionId;
    }

    public void setLaneSessionId(long laneSessionId) {
        this.laneSessionId = laneSessionId;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(int paymentOption) {
        this.paymentOption = paymentOption;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(BigDecimal amountDue) {
        this.amountDue = amountDue;
    }

    public BigDecimal getAmountPayment() {
        return amountPayment;
    }

    public void setAmountPayment(BigDecimal amountPayment) {
        this.amountPayment = amountPayment;
    }

    public BigDecimal getAmountChangeDue() {
        return amountChangeDue;
    }

    public void setAmountChangeDue(BigDecimal amountChangeDue) {
        this.amountChangeDue = amountChangeDue;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Date getDateResponse() {
        return dateResponse;
    }

    public void setDateResponse(Date dateResponse) {
        this.dateResponse = dateResponse;
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
        builder.append("Payment [id=");
        builder.append(id);
        builder.append(", code=");
        builder.append(code);
        builder.append(", laneSessionId=");
        builder.append(laneSessionId);
        builder.append(", accountId=");
        builder.append(accountId);
        builder.append(", missionId=");
        builder.append(missionId);
        builder.append(", tripId=");
        builder.append(tripId);
        builder.append(", operatorId=");
        builder.append(operatorId);
        builder.append(", type=");
        builder.append(type);
        builder.append(", paymentOption=");
        builder.append(paymentOption);
        builder.append(", currency=");
        builder.append(currency);
        builder.append(", amountDue=");
        builder.append(amountDue);
        builder.append(", amountPayment=");
        builder.append(amountPayment);
        builder.append(", amountChangeDue=");
        builder.append(amountChangeDue);
        builder.append(", notes=");
        builder.append(notes);
        builder.append(", firstName=");
        builder.append(firstName);
        builder.append(", lastName=");
        builder.append(lastName);
        builder.append(", msisdn=");
        builder.append(msisdn);
        builder.append(", responseCode=");
        builder.append(responseCode);
        builder.append(", dateResponse=");
        builder.append(dateResponse);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateEdited=");
        builder.append(dateEdited);
        builder.append("]");
        return builder.toString();
    }

}
