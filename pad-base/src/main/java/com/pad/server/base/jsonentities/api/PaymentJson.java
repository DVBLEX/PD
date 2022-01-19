package com.pad.server.base.jsonentities.api;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentJson {

    private String     code;
    private String     tripCode;
    private String     accountCode;
    private Integer    paymentOption;
    private Long       paymentAmount;
    private Long       feeDueAmount;
    private Long       changeDueAmount;
    private Long       topupAmount;
    private String     firstName;
    private String     lastName;
    private String     msisdn;

    private long       mnoId;
    private String     onlinePaymentCode;
    private BigDecimal accountBalanceAmount;
    private Boolean    isPrintReceipt;
    private String     referenceAggregator;
    private String     currencyCode;
    private BigDecimal amount;
    private BigDecimal feeAggregator;
    private String     statusAggregator;
    private String     errorAggregator;
    private String     dateRequestString;
    private String     dateCallbackResponseString;
    private String     paymentNote;

    public PaymentJson() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public Integer getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(Integer paymentOption) {
        this.paymentOption = paymentOption;
    }

    public Long getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Long paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Long getFeeDueAmount() {
        return feeDueAmount;
    }

    public void setFeeDueAmount(Long feeDueAmount) {
        this.feeDueAmount = feeDueAmount;
    }

    public Long getChangeDueAmount() {
        return changeDueAmount;
    }

    public void setChangeDueAmount(Long changeDueAmount) {
        this.changeDueAmount = changeDueAmount;
    }

    public Long getTopupAmount() {
        return topupAmount;
    }

    public void setTopupAmount(Long topupAmount) {
        this.topupAmount = topupAmount;
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

    public long getMnoId() {
        return mnoId;
    }

    public void setMnoId(long mnoId) {
        this.mnoId = mnoId;
    }

    public String getOnlinePaymentCode() {
        return onlinePaymentCode;
    }

    public void setOnlinePaymentCode(String onlinePaymentCode) {
        this.onlinePaymentCode = onlinePaymentCode;
    }

    public BigDecimal getAccountBalanceAmount() {
        return accountBalanceAmount;
    }

    public void setAccountBalanceAmount(BigDecimal accountBalanceAmount) {
        this.accountBalanceAmount = accountBalanceAmount;
    }

    public Boolean getIsPrintReceipt() {
        return isPrintReceipt;
    }

    public void setIsPrintReceipt(Boolean isPrintReceipt) {
        this.isPrintReceipt = isPrintReceipt;
    }

    public String getReferenceAggregator() {
        return referenceAggregator;
    }

    public void setReferenceAggregator(String referenceAggregator) {
        this.referenceAggregator = referenceAggregator;
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

    public String getPaymentNote() {
        return paymentNote;
    }

    public void setPaymentNote(String paymentNote) {
        this.paymentNote = paymentNote;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PaymentJson [code=");
        builder.append(code);
        builder.append(", tripCode=");
        builder.append(tripCode);
        builder.append(", accountCode=");
        builder.append(accountCode);
        builder.append(", paymentOption=");
        builder.append(paymentOption);
        builder.append(", paymentAmount=");
        builder.append(paymentAmount);
        builder.append(", feeDueAmount=");
        builder.append(feeDueAmount);
        builder.append(", changeDueAmount=");
        builder.append(changeDueAmount);
        builder.append(", topupAmount=");
        builder.append(topupAmount);
        builder.append(", firstName=");
        builder.append(firstName);
        builder.append(", lastName=");
        builder.append(lastName);
        builder.append(", msisdn=");
        builder.append(msisdn);
        builder.append(", mnoId=");
        builder.append(mnoId);
        builder.append(", onlinePaymentCode=");
        builder.append(onlinePaymentCode);
        builder.append(", accountBalanceAmount=");
        builder.append(accountBalanceAmount);
        builder.append(", isPrintReceipt=");
        builder.append(isPrintReceipt);
        builder.append(", referenceAggregator=");
        builder.append(referenceAggregator);
        builder.append(", currencyCode=");
        builder.append(currencyCode);
        builder.append(", amount=");
        builder.append(amount);
        builder.append(", feeAggregator=");
        builder.append(feeAggregator);
        builder.append(", statusAggregator=");
        builder.append(statusAggregator);
        builder.append(", errorAggregator=");
        builder.append(errorAggregator);
        builder.append(", dateRequestString=");
        builder.append(dateRequestString);
        builder.append(", dateCallbackResponseString=");
        builder.append(dateCallbackResponseString);
        builder.append(", paymentNote=");
        builder.append(paymentNote);
        builder.append("]");
        return builder.toString();
    }

}
