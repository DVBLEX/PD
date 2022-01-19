package com.pad.server.base.jsonentities.api;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OnlinePaymentJson {

    private String     code;
    private long       mnoId;
    private String     msisdn;
    private String     currencyCode;
    private BigDecimal amount;
    private BigDecimal feeAggregator;
    private String     referenceAggregator;
    private String     statusAggregator;
    private String     errorAggregator;
    private Date       dateRequest;
    private String     dateRequestString;
    private String     dateCallbackResponseString;
    private String     accountCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Date getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(Date dateRequest) {
        this.dateRequest = dateRequest;
    }

    public String getDateRequestString() {
        return dateRequestString;
    }

    public void setDateRequestString(String dateRequestString) {
        this.dateRequestString = dateRequestString;
    }

    public String getDateCallbackResponseString() {
        return dateCallbackResponseString;
    }

    public void setDateCallbackResponseString(String dateCallbackResponseString) {
        this.dateCallbackResponseString = dateCallbackResponseString;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnlinePaymentJson [code=");
        builder.append(code);
        builder.append(", mnoId=");
        builder.append(mnoId);
        builder.append(", msisdn=");
        builder.append(msisdn);
        builder.append(", currencyCode=");
        builder.append(currencyCode);
        builder.append(", amount=");
        builder.append(amount);
        builder.append(", feeAggregator=");
        builder.append(feeAggregator);
        builder.append(", referenceAggregator=");
        builder.append(referenceAggregator);
        builder.append(", statusAggregator=");
        builder.append(statusAggregator);
        builder.append(", errorAggregator=");
        builder.append(errorAggregator);
        builder.append(", dateRequestString=");
        builder.append(dateRequestString);
        builder.append(", dateCallbackResponseString=");
        builder.append(dateCallbackResponseString);
        builder.append(", accountCode=");
        builder.append(accountCode);
        builder.append("]");
        return builder.toString();
    }

}
