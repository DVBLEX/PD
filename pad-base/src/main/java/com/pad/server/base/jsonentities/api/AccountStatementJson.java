package com.pad.server.base.jsonentities.api;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author matos
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountStatementJson {

    private Date       dateCreated;
    private String     dateCreatedString;
    private Integer    type;
    private Integer    portOperatorId;
    private Integer    independentPortOperatorId;
    private String     transactionType;
    private String     referenceNumber;
    private String     registration;
    private String     country;
    private BigDecimal credit;
    private BigDecimal debit;
    private BigDecimal balance;
    private boolean    openingBalance;

    // request params
    private String     accountCode;
    private String     dateFromString;
    private String     dateToString;

    // used for pagination
    private int        currentPage;
    private int        pageCount;
    private String     sortColumn;
    private boolean    sortAsc;

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateCreatedString() {
        return dateCreatedString;
    }

    public void setDateCreatedString(String dateCreatedString) {
        this.dateCreatedString = dateCreatedString;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPortOperatorId() {
        return portOperatorId;
    }

    public void setPortOperatorId(Integer portOperatorId) {
        this.portOperatorId = portOperatorId;
    }

    public Integer getIndependentPortOperatorId() {
        return independentPortOperatorId;
    }

    public void setIndependentPortOperatorId(Integer independentPortOperatorId) {
        this.independentPortOperatorId = independentPortOperatorId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean getIsOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(boolean openingBalance) {
        this.openingBalance = openingBalance;
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
        StringBuilder builder = new StringBuilder();
        builder.append("AccountStatementResponseJson [dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateCreatedString=");
        builder.append(dateCreatedString);
        builder.append(", type=");
        builder.append(type);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", transactionType=");
        builder.append(transactionType);
        builder.append(", referenceNumber=");
        builder.append(referenceNumber);
        builder.append(", registration=");
        builder.append(registration);
        builder.append(", country=");
        builder.append(country);
        builder.append(", credit=");
        builder.append(credit);
        builder.append(", debit=");
        builder.append(debit);
        builder.append(", balance=");
        builder.append(balance);
        builder.append(", openingBalance=");
        builder.append(openingBalance);
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
