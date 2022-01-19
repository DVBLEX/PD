package com.pad.server.base.jsonentities.api;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountPaymentJson {

    private Date       datePayment;
    private String     datePaymentString;
    private String     operatorName;
    private Integer    laneSessionId;
    private Integer    roleId;
    private Integer    type;
    private Integer    paymentOption;
    private String     firstName;
    private String     lastName;
    private String     msisdn;
    private BigDecimal amount;
    private BigDecimal feeAggregator;
    private String     referenceAggregator;
    private String     statusAggregator;
    private String     errorAggregator;
    private String     notes;
    private Date       dateResponse;
    private String     dateResponseString;

    // request params
    private String     accountCode;
    private String     dateFromString;
    private String     dateToString;

    // used for pagination
    private int        currentPage;
    private int        pageCount;
    private String     sortColumn;
    private boolean    sortAsc;

    public Date getDatePayment() {
        return datePayment;
    }

    public void setDatePayment(Date datePayment) {
        this.datePayment = datePayment;
    }

    public String getDatePaymentString() {
        return datePaymentString;
    }

    public void setDatePaymentString(String datePaymentString) {
        this.datePaymentString = datePaymentString;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getLaneSessionId() {
        return laneSessionId;
    }

    public void setLaneSessionId(Integer laneSessionId) {
        this.laneSessionId = laneSessionId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(Integer paymentOption) {
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

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getDateResponse() {
        return dateResponse;
    }

    public void setDateResponse(Date dateResponse) {
        this.dateResponse = dateResponse;
    }

    public String getDateResponseString() {
        return dateResponseString;
    }

    public void setDateResponseString(String dateResponseString) {
        this.dateResponseString = dateResponseString;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getDateFromString() {
        return dateFromString;
    }

    public void setDateFromString(String dateFromString) {
        this.dateFromString = dateFromString;
    }

    public String getDateToString() {
        return dateToString;
    }

    public void setDateToString(String dateToString) {
        this.dateToString = dateToString;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public boolean getSortAsc() {
        return sortAsc;
    }

    public void setSortAsc(boolean sortAsc) {
        this.sortAsc = sortAsc;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("AccountPaymentJson [datePayment=");
        builder.append(datePayment);
        builder.append(", datePaymentString=");
        builder.append(datePaymentString);
        builder.append(", operatorName=");
        builder.append(operatorName);
        builder.append(", laneSessionId=");
        builder.append(laneSessionId);
        builder.append(", roleId=");
        builder.append(roleId);
        builder.append(", type=");
        builder.append(type);
        builder.append(", paymentOption=");
        builder.append(paymentOption);
        builder.append(", firstName=");
        builder.append(firstName);
        builder.append(", lastName=");
        builder.append(lastName);
        builder.append(", msisdn=");
        builder.append(msisdn);
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
        builder.append(", notes=");
        builder.append(notes);
        builder.append(", dateResponse=");
        builder.append(dateResponse);
        builder.append(", dateResponseString=");
        builder.append(dateResponseString);
        builder.append(", accountCode=");
        builder.append(accountCode);
        builder.append(", dateFromString=");
        builder.append(dateFromString);
        builder.append(", dateToString=");
        builder.append(dateToString);
        builder.append(", currentPage=");
        builder.append(currentPage);
        builder.append(", pageCount=");
        builder.append(pageCount);
        builder.append(", sortColumn=");
        builder.append(sortColumn);
        builder.append(", sortAsc=");
        builder.append(sortAsc);
        builder.append("]");
        return builder.toString();
    }
}
