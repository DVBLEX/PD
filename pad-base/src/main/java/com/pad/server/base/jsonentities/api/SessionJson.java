package com.pad.server.base.jsonentities.api;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionJson {

    private String     code;
    private Integer    type;
    private Integer    status;
    private String     kioskOperatorCode;
    private String     kioskOperatorName;
    private Integer    laneNumber;
    private Long       laneId;
    private String     cashBagNumber;
    private BigDecimal cashAmountStart;
    private String     cashAmountStartString;
    private BigDecimal cashAmountEnd;
    private String     cashAmountEndString;
    private BigDecimal cashAmountEndExpected;
    private BigDecimal cashAmountStartToEndDifference;
    private int        validationStep;

    private int        noAccountCashTransactionCount;
    private int        noAccountOnlineTransactionCount;
    private int        accountCashTransactionCount;
    private int        accountOnlineTransactionCount;
    private int        accountDeductTransactionCount;
    private int        exitOnlySessionCount;
    private int        adhocTripsCreatedCount;
    private int        adhocTripsCancelledCount;

    private BigDecimal noAccountCashTransactionTotalAmount;
    private BigDecimal noAccountOnlineTransactionTotalAmount;
    private BigDecimal accountCashTransactionTotalAmount;
    private BigDecimal accountOnlineTransactionTotalAmount;
    private BigDecimal accountDeductTransactionTotalAmount;
    private BigDecimal cashChangeGivenTotalAmount;

    private String     dateCreatedString;
    private String     dateStartString;
    private String     dateEndString;
    private String     reasonAmountUnexpected;

    // used for pagination
    private int        currentPage;
    private int        pageCount;
    private String     sortColumn;
    private boolean    sortAsc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getKioskOperatorCode() {
        return kioskOperatorCode;
    }

    public void setKioskOperatorCode(String kioskOperatorCode) {
        this.kioskOperatorCode = kioskOperatorCode;
    }

    public String getKioskOperatorName() {
        return kioskOperatorName;
    }

    public void setKioskOperatorName(String kioskOperatorName) {
        this.kioskOperatorName = kioskOperatorName;
    }

    public Integer getLaneNumber() {
        return laneNumber;
    }

    public void setLaneNumber(Integer laneNumber) {
        this.laneNumber = laneNumber;
    }

    public Long getLaneId() {
        return laneId;
    }

    public void setLaneId(Long laneId) {
        this.laneId = laneId;
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

    public String getCashAmountStartString() {
        return cashAmountStartString;
    }

    public void setCashAmountStartString(String cashAmountStartString) {
        this.cashAmountStartString = cashAmountStartString;
    }

    public BigDecimal getCashAmountEnd() {
        return cashAmountEnd;
    }

    public void setCashAmountEnd(BigDecimal cashAmountEnd) {
        this.cashAmountEnd = cashAmountEnd;
    }

    public String getCashAmountEndString() {
        return cashAmountEndString;
    }

    public void setCashAmountEndString(String cashAmountEndString) {
        this.cashAmountEndString = cashAmountEndString;
    }

    public BigDecimal getCashAmountEndExpected() {
        return cashAmountEndExpected;
    }

    public void setCashAmountEndExpected(BigDecimal cashAmountEndExpected) {
        this.cashAmountEndExpected = cashAmountEndExpected;
    }

    public BigDecimal getCashAmountStartToEndDifference() {
        return cashAmountStartToEndDifference;
    }

    public void setCashAmountStartToEndDifference(BigDecimal cashAmountStartToEndDifference) {
        this.cashAmountStartToEndDifference = cashAmountStartToEndDifference;
    }

    public int getValidationStep() {
        return validationStep;
    }

    public void setValidationStep(int validationStep) {
        this.validationStep = validationStep;
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

    public String getDateCreatedString() {
        return dateCreatedString;
    }

    public void setDateCreatedString(String dateCreatedString) {
        this.dateCreatedString = dateCreatedString;
    }

    public String getDateStartString() {
        return dateStartString;
    }

    public void setDateStartString(String dateStartString) {
        this.dateStartString = dateStartString;
    }

    public String getDateEndString() {
        return dateEndString;
    }

    public void setDateEndString(String dateEndString) {
        this.dateEndString = dateEndString;
    }

    public String getReasonAmountUnexpected() {
        return reasonAmountUnexpected;
    }

    public void setReasonAmountUnexpected(String reasonAmountUnexpected) {
        this.reasonAmountUnexpected = reasonAmountUnexpected;
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
        builder.append("SessionJson [code=");
        builder.append(code);
        builder.append(", type=");
        builder.append(type);
        builder.append(", status=");
        builder.append(status);
        builder.append(", kioskOperatorCode=");
        builder.append(kioskOperatorCode);
        builder.append(", kioskOperatorName=");
        builder.append(kioskOperatorName);
        builder.append(", laneNumber=");
        builder.append(laneNumber);
        builder.append(", laneId=");
        builder.append(laneId);
        builder.append(", cashBagNumber=");
        builder.append(cashBagNumber);
        builder.append(", cashAmountStart=");
        builder.append(cashAmountStart);
        builder.append(", cashAmountStartString=");
        builder.append(cashAmountStartString);
        builder.append(", cashAmountEnd=");
        builder.append(cashAmountEnd);
        builder.append(", cashAmountEndString=");
        builder.append(cashAmountEndString);
        builder.append(", cashAmountEndExpected=");
        builder.append(cashAmountEndExpected);
        builder.append(", cashAmountStartToEndDifference=");
        builder.append(cashAmountStartToEndDifference);
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
        builder.append(", dateCreatedString=");
        builder.append(dateCreatedString);
        builder.append(", dateStartString=");
        builder.append(dateStartString);
        builder.append(", dateEndString=");
        builder.append(dateEndString);
        builder.append(", reasonAmountUnexpected=");
        builder.append(reasonAmountUnexpected);
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
