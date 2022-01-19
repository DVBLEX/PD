package com.pad.server.base.jsonentities.api;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author rafael
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountJson {

    private String     code;
    private Long       number;
    private int        type;
    private Integer    status;
    private String     firstName;
    private String     lastName;
    private String     companyName;
    private String     accountName;
    private String     companyRegistration;
    private String     companyTelephone;
    private String[]   emailListInvoiceStatement;
    private String     registrationCountryISO;
    private String     address1;
    private String     address2;
    private String     address3;
    private String     address4;
    private String     postCode;
    private String     countryCode;
    private String     msisdn;
    private String     nationalityCountryISO;
    private boolean    specialTaxStatus;
    private Integer    paymentTermsType;
    private String     currency;
    private BigDecimal balanceAmount;
    private BigDecimal amountOverdraftLimit;
    private BigDecimal amountApprovedTrips;
    private String     denialReason;
    private boolean    isDeleted;
    private boolean    isActive;
    private Date       dateDeleted;
    private Date       dateCreated;
    private Date       dateEdited;
    private boolean    isTripApprovedEmail;
    private boolean    isDeductCreditRegisteredTrucks;
    private boolean    isSendLowAccountBalanceWarn;
    private BigDecimal amountLowAccountBalanceWarn;

    private String     dateCreatedString;

    private BigDecimal amountHold;

    // used for pagination
    private int        currentPage;
    private int        pageCount;
    private String     sortColumn;
    private boolean    sortAsc;

    public String[] getEmailListInvoiceStatement() {
        return emailListInvoiceStatement;
    }

    public void setEmailListInvoiceStatement(String[] emailListInvoiceStatement) {
        this.emailListInvoiceStatement = emailListInvoiceStatement;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String clientCode) {
        this.code = clientCode;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCompanyRegistration() {
        return companyRegistration;
    }

    public void setCompanyRegistration(String companyRegistration) {
        this.companyRegistration = companyRegistration;
    }

    public String getCompanyTelephone() {
        return companyTelephone;
    }

    public void setCompanyTelephone(String companyTelephone) {
        this.companyTelephone = companyTelephone;
    }

    public String getRegistrationCountryISO() {
        return registrationCountryISO;
    }

    public void setRegistrationCountryISO(String registrationCountryISO) {
        this.registrationCountryISO = registrationCountryISO;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getNationalityCountryISO() {
        return nationalityCountryISO;
    }

    public void setNationalityCountryISO(String nationalityCountryISO) {
        this.nationalityCountryISO = nationalityCountryISO;
    }

    public boolean getSpecialTaxStatus() {
        return specialTaxStatus;
    }

    public void setSpecialTaxStatus(boolean specialTaxStatus) {
        this.specialTaxStatus = specialTaxStatus;
    }

    public Integer getPaymentTermsType() {
        return paymentTermsType;
    }

    public void setPaymentTermsType(Integer paymentTermsType) {
        this.paymentTermsType = paymentTermsType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public BigDecimal getAmountOverdraftLimit() {
        return amountOverdraftLimit;
    }

    public void setAmountOverdraftLimit(BigDecimal amountOverdraftLimit) {
        this.amountOverdraftLimit = amountOverdraftLimit;
    }

    public BigDecimal getAmountApprovedTrips() {
        return amountApprovedTrips;
    }

    public void setAmountApprovedTrips(BigDecimal amountApprovedTrips) {
        this.amountApprovedTrips = amountApprovedTrips;
    }

    public String getDenialReason() {
        return denialReason;
    }

    public void setDenialReason(String denialReason) {
        this.denialReason = denialReason;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    

    public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Date getDateDeleted() {
        return dateDeleted;
    }

    public void setDateDeleted(Date dateDeleted) {
        this.dateDeleted = dateDeleted;
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

    public boolean getIsTripApprovedEmail() {
        return isTripApprovedEmail;
    }

    public void setIsTripApprovedEmail(boolean isTripApprovedEmail) {
        this.isTripApprovedEmail = isTripApprovedEmail;
    }

    public boolean getIsDeductCreditRegisteredTrucks() {
        return isDeductCreditRegisteredTrucks;
    }

    public void setIsDeductCreditRegisteredTrucks(boolean deductCreditRegisteredTrucks) {
        isDeductCreditRegisteredTrucks = deductCreditRegisteredTrucks;
    }

    public boolean getIsSendLowAccountBalanceWarn() {
        return isSendLowAccountBalanceWarn;
    }

    public void setIsSendLowAccountBalanceWarn(boolean isSendLowAccountBalanceWarn) {
        this.isSendLowAccountBalanceWarn = isSendLowAccountBalanceWarn;
    }

    public BigDecimal getAmountLowAccountBalanceWarn() {
        return amountLowAccountBalanceWarn;
    }

    public void setAmountLowAccountBalanceWarn(BigDecimal amountLowAccountBalanceWarn) {
        this.amountLowAccountBalanceWarn = amountLowAccountBalanceWarn;
    }

    public String getDateCreatedString() {
        return dateCreatedString;
    }

    public void setDateCreatedString(String dateCreatedString) {
        this.dateCreatedString = dateCreatedString;
    }

    public BigDecimal getAmountHold() {
        return amountHold;
    }

    public void setAmountHold(BigDecimal amountHold) {
        this.amountHold = amountHold;
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
        builder.append("AccountJson [code=");
        builder.append(code);
        builder.append(", number=");
        builder.append(number);
        builder.append(", type=");
        builder.append(type);
        builder.append(", status=");
        builder.append(status);
        builder.append(", firstName=");
        builder.append(firstName);
        builder.append(", lastName=");
        builder.append(lastName);
        builder.append(", companyName=");
        builder.append(companyName);
        builder.append(", accountName=");
        builder.append(accountName);
        builder.append(", companyRegistration=");
        builder.append(companyRegistration);
        builder.append(", companyTelephone=");
        builder.append(companyTelephone);
        builder.append(", emailListInvoiceStatement=");
        builder.append(emailListInvoiceStatement);
        builder.append(", registrationCountryISO=");
        builder.append(registrationCountryISO);
        builder.append(", address1=");
        builder.append(address1);
        builder.append(", address2=");
        builder.append(address2);
        builder.append(", address3=");
        builder.append(address3);
        builder.append(", address4=");
        builder.append(address4);
        builder.append(", postCode=");
        builder.append(postCode);
        builder.append(", countryCode=");
        builder.append(countryCode);
        builder.append(", msisdn=");
        builder.append(msisdn);
        builder.append(", nationalityCountryISO=");
        builder.append(nationalityCountryISO);
        builder.append(", specialTaxStatus=");
        builder.append(specialTaxStatus);
        builder.append(", paymentTermsType=");
        builder.append(paymentTermsType);
        builder.append(", currency=");
        builder.append(currency);
        builder.append(", balanceAmount=");
        builder.append(balanceAmount);
        builder.append(", amountOverdraftLimit=");
        builder.append(amountOverdraftLimit);
        builder.append(", amountApprovedTrips=");
        builder.append(amountApprovedTrips);
        builder.append(", denialReason=");
        builder.append(denialReason);
        builder.append(", isDeleted=");
        builder.append(isDeleted);
        builder.append(", isActive=");
        builder.append(isActive);
        builder.append(", dateDeleted=");
        builder.append(dateDeleted);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateEdited=");
        builder.append(dateEdited);
        builder.append(", isTripApprovedEmail=");
        builder.append(isTripApprovedEmail);
        builder.append(", isSendLowAccountBalanceWarn=");
        builder.append(isSendLowAccountBalanceWarn);
        builder.append(", amountLowAccountBalanceWarn=");
        builder.append(amountLowAccountBalanceWarn);
        builder.append(", dateCreatedString=");
        builder.append(dateCreatedString);
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
