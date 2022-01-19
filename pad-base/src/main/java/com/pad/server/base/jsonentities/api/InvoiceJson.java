package com.pad.server.base.jsonentities.api;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceJson {

    private String     code;
    private String     reference;
    private Integer    type;
    private String     dateInvoice;
    private String     client;
    private String     datePaymentDue;
    private String     paymentMethod;
    private String     dateSaleDelivery;
    private String     totalTax;
    private BigDecimal totalAmount;
    private String     currency;
    private boolean    isPaid;
    private String     path;
    private long       operatorId;
    private String     dateCreatedString;
    private String     companyName;
    private String     datePaymentString;
    private String     dateDueString;

    private String     accountCode;
    private Long       accountNumber;
    private String     datePayment;
    private Integer    typePayment;

    // used for pagination
    private int        currentPage;
    private int        pageCount;
    private String     sortColumn;
    private boolean    sortAsc;

    public InvoiceJson() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDateInvoice() {
        return dateInvoice;
    }

    public void setDateInvoice(String dateInvoice) {
        this.dateInvoice = dateInvoice;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDatePaymentDue() {
        return datePaymentDue;
    }

    public void setDatePaymentDue(String datePaymentDue) {
        this.datePaymentDue = datePaymentDue;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDateSaleDelivery() {
        return dateSaleDelivery;
    }

    public void setDateSaleDelivery(String dateSaleDelivery) {
        this.dateSaleDelivery = dateSaleDelivery;
    }

    public String getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(String totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public String getDateCreatedString() {
        return dateCreatedString;
    }

    public void setDateCreatedString(String dateCreatedString) {
        this.dateCreatedString = dateCreatedString;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDateDueString() {
        return dateDueString;
    }

    public void setDateDueString(String dateDueString) {
        this.dateDueString = dateDueString;
    }

    public String getDatePaymentString() {
        return datePaymentString;
    }

    public void setDatePaymentString(String datePaymentString) {
        this.datePaymentString = datePaymentString;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDatePayment() {
        return datePayment;
    }

    public void setDatePayment(String datePayment) {
        this.datePayment = datePayment;
    }

    public Integer getTypePayment() {
        return typePayment;
    }

    public void setTypePayment(Integer typePayment) {
        this.typePayment = typePayment;
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
        builder.append("InvoiceJson [code=");
        builder.append(code);
        builder.append(", reference=");
        builder.append(reference);
        builder.append(", type=");
        builder.append(type);
        builder.append(", dateInvoice=");
        builder.append(dateInvoice);
        builder.append(", client=");
        builder.append(client);
        builder.append(", datePaymentDue=");
        builder.append(datePaymentDue);
        builder.append(", paymentMethod=");
        builder.append(paymentMethod);
        builder.append(", dateSaleDelivery=");
        builder.append(dateSaleDelivery);
        builder.append(", totalTax=");
        builder.append(totalTax);
        builder.append(", totalAmount=");
        builder.append(totalAmount);
        builder.append(", currency=");
        builder.append(currency);
        builder.append(", isPaid=");
        builder.append(isPaid);
        builder.append(", accountCode=");
        builder.append(accountCode);
        builder.append(", accountNumber=");
        builder.append(accountNumber);
        builder.append(", datePayment=");
        builder.append(datePayment);
        builder.append(", typePayment=");
        builder.append(typePayment);
        builder.append(", path=");
        builder.append(path);
        builder.append(", operatorId=");
        builder.append(operatorId);
        builder.append(" ,dateCreatedString=");
        builder.append(dateCreatedString);
        builder.append(", companyName=");
        builder.append(companyName);
        builder.append(", dateDueString=");
        builder.append(dateDueString);
        builder.append(", datePaymentString=");
        builder.append(datePaymentString);
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
