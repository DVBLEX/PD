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
import javax.persistence.Transient;

import com.pad.server.base.common.ServerConstants;

@Entity
@Table(name = "online_payments")
public class OnlinePayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "code")
    private String            code;

    @Column(name = "client_id")
    private long              clientId;

    @Column(name = "aggregator_id")
    private long              aggregatorId;

    @Column(name = "account_id")
    private long              accountId;

    @Column(name = "mission_id")
    private long              missionId;

    @Column(name = "trip_id")
    private long              tripId;

    @Column(name = "driver_id")
    private long              driverId;

    @Column(name = "payment_id")
    private long              paymentId;

    @Column(name = "payment_option")
    private int               paymentOption;

    @Column(name = "first_name")
    private String            firstName;

    @Column(name = "last_name")
    private String            lastName;

    @Column(name = "mno_id")
    private long              mnoId;

    @Column(name = "msisdn")
    private String            msisdn;

    @Column(name = "currency_code")
    private String            currencyCode;

    @Column(name = "amount")
    private BigDecimal        amount;

    @Column(name = "amount_aggregator")
    private BigDecimal        amountAggregator;

    @Column(name = "fee_aggregator")
    private BigDecimal        feeAggregator;

    @Column(name = "reference_aggregator")
    private String            referenceAggregator;

    @Column(name = "transaction_aggregator")
    private String            transactionAggregator;

    @Column(name = "date_request")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateRequest;

    @Column(name = "date_response")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateResponse;

    @Column(name = "date_response_aggregator")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateResponseAggregator;

    @Column(name = "date_callback_response")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCallbackResponse;

    @Column(name = "date_payment_aggregator")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              datePaymentAggregator;

    @Column(name = "status_aggregator")
    private String            statusAggregator;

    @Column(name = "error_aggregator")
    private String            errorAggregator;

    @Column(name = "client_id_aggregator")
    private String            clientIdAggregator;

    @Column(name = "gu_id_aggregator")
    private String            guIdAggregator;

    @Column(name = "response_code")
    private int               responseCode;

    @Column(name = "request_hash")
    private String            requestHash;

    @Column(name = "response_hash")
    private String            responseHash;

    @Column(name = "response_callback_hash")
    private String            responseCallbackHash;

    @Transient
    private String            mnoName;

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

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getAggregatorId() {
        return aggregatorId;
    }

    public void setAggregatorId(long aggregatorId) {
        this.aggregatorId = aggregatorId;
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

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public int getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(int paymentOption) {
        this.paymentOption = paymentOption;
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

    public long getMnoId() {
        return mnoId;
    }

    public void setMnoId(long mnoId) {
        this.mnoId = mnoId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountAggregator() {
        return amountAggregator;
    }

    public void setAmountAggregator(BigDecimal amountAggregator) {
        this.amountAggregator = amountAggregator;
    }

    public BigDecimal getFeeAggregator() {
        return feeAggregator;
    }

    public void setFeeAggregator(BigDecimal feeAggregator) {
        this.feeAggregator = feeAggregator;
    }

    public String getReferenceAggregator() {
        return referenceAggregator;
    }

    public void setReferenceAggregator(String referenceAggregator) {
        this.referenceAggregator = referenceAggregator;
    }

    public String getTransactionAggregator() {
        return transactionAggregator;
    }

    public void setTransactionAggregator(String transactionAggregator) {
        this.transactionAggregator = transactionAggregator;
    }

    public Date getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(Date dateRequest) {
        this.dateRequest = dateRequest;
    }

    public Date getDateResponse() {
        return dateResponse;
    }

    public void setDateResponse(Date dateResponse) {
        this.dateResponse = dateResponse;
    }

    public Date getDateResponseAggregator() {
        return dateResponseAggregator;
    }

    public void setDateResponseAggregator(Date dateResponseAggregator) {
        this.dateResponseAggregator = dateResponseAggregator;
    }

    public Date getDateCallbackResponse() {
        return dateCallbackResponse;
    }

    public void setDateCallbackResponse(Date dateCallbackResponse) {
        this.dateCallbackResponse = dateCallbackResponse;
    }

    public Date getDatePaymentAggregator() {
        return datePaymentAggregator;
    }

    public void setDatePaymentAggregator(Date datePaymentAggregator) {
        this.datePaymentAggregator = datePaymentAggregator;
    }

    public String getStatusAggregator() {
        return statusAggregator;
    }

    public void setStatusAggregator(String statusAggregator) {
        this.statusAggregator = statusAggregator;
    }

    public String getErrorAggregator() {
        return errorAggregator;
    }

    public void setErrorAggregator(String errorAggregator) {
        this.errorAggregator = errorAggregator;
    }

    public String getClientIdAggregator() {
        return clientIdAggregator;
    }

    public void setClientIdAggregator(String clientIdAggregator) {
        this.clientIdAggregator = clientIdAggregator;
    }

    public String getGuIdAggregator() {
        return guIdAggregator;
    }

    public void setGuIdAggregator(String guIdAggregator) {
        this.guIdAggregator = guIdAggregator;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getRequestHash() {
        return requestHash;
    }

    public void setRequestHash(String requestHash) {
        this.requestHash = requestHash;
    }

    public String getResponseHash() {
        return responseHash;
    }

    public void setResponseHash(String responseHash) {
        this.responseHash = responseHash;
    }

    public String getResponseCallbackHash() {
        return responseCallbackHash;
    }

    public void setResponseCallbackHash(String responseCallbackHash) {
        this.responseCallbackHash = responseCallbackHash;
    }

    public String getMnoName() {

        switch ((int) this.getMnoId()) {
            case (int) ServerConstants.MNO_ID_ORANGE_MONEY:
                return ServerConstants.MNO_ORANGE_MONEY;

            case (int) ServerConstants.MNO_ID_WARI:
                return ServerConstants.MNO_WARI;

            case (int) ServerConstants.MNO_ID_FREE_MONEY:
                return ServerConstants.MNO_FREE_MONEY;

            case (int) ServerConstants.MNO_ID_E_MONEY:
                return ServerConstants.MNO_E_MONEY;

            case (int) ServerConstants.MNO_ID_ECO_BANK:
                return ServerConstants.MNO_ECO_BANK;

            default:
                return ServerConstants.DEFAULT_STRING;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnlinePayment [id=");
        builder.append(id);
        builder.append(", code=");
        builder.append(code);
        builder.append(", clientId=");
        builder.append(clientId);
        builder.append(", aggregatorId=");
        builder.append(aggregatorId);
        builder.append(", accountId=");
        builder.append(accountId);
        builder.append(", missionId=");
        builder.append(missionId);
        builder.append(", tripId=");
        builder.append(tripId);
        builder.append(", driverId=");
        builder.append(driverId);
        builder.append(", paymentId=");
        builder.append(paymentId);
        builder.append(", paymentOption=");
        builder.append(paymentOption);
        builder.append(", firstName=");
        builder.append(firstName);
        builder.append(", lastName=");
        builder.append(lastName);
        builder.append(", mnoId=");
        builder.append(mnoId);
        builder.append(", msisdn=");
        builder.append(msisdn);
        builder.append(", currencyCode=");
        builder.append(currencyCode);
        builder.append(", amount=");
        builder.append(amount);
        builder.append(", amountAggregator=");
        builder.append(amountAggregator);
        builder.append(", feeAggregator=");
        builder.append(feeAggregator);
        builder.append(", referenceAggregator=");
        builder.append(referenceAggregator);
        builder.append(", transactionAggregator=");
        builder.append(transactionAggregator);
        builder.append(", dateRequest=");
        builder.append(dateRequest);
        builder.append(", dateResponse=");
        builder.append(dateResponse);
        builder.append(", dateResponseAggregator=");
        builder.append(dateResponseAggregator);
        builder.append(", dateCallbackResponse=");
        builder.append(dateCallbackResponse);
        builder.append(", datePaymentAggregator=");
        builder.append(datePaymentAggregator);
        builder.append(", statusAggregator=");
        builder.append(statusAggregator);
        builder.append(", errorAggregator=");
        builder.append(errorAggregator);
        builder.append(", clientIdAggregator=");
        builder.append(clientIdAggregator);
        builder.append(", guIdAggregator=");
        builder.append(guIdAggregator);
        builder.append(", responseCode=");
        builder.append(responseCode);
        builder.append(", requestHash=");
        builder.append(requestHash);
        builder.append(", responseHash=");
        builder.append(responseHash);
        builder.append(", responseCallbackHash=");
        builder.append(responseCallbackHash);
        builder.append(", mnoName=");
        builder.append(mnoName);
        builder.append("]");
        return builder.toString();
    }

}
