package com.pad.server.base.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "accounts")
public class Account implements Serializable {

    private static final long       serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long                    id;

    @Column(name = "code")
    private String                  code;

    @Column(name = "number")
    private long                    number;

    @Column(name = "type")
    private int                     type;

    @Column(name = "status")
    private int                     status;

    @Column(name = "first_name")
    private String                  firstName;

    @Column(name = "last_name")
    private String                  lastName;

    @Column(name = "company_name")
    private String                  companyName;

    @Column(name = "company_short_name")
    private String                  companyShortName;

    @Column(name = "company_registration")
    private String                  companyRegistration;

    @Column(name = "company_telephone")
    private String                  companyTelephone;

    @Column(name = "email_list_invoice_statement")
    private String                  emailListInvoiceStatement;

    @Column(name = "registration_country_iso")
    private String                  registrationCountryISO;

    @Column(name = "address_1")
    private String                  address1;

    @Column(name = "address_2")
    private String                  address2;

    @Column(name = "address_3")
    private String                  address3;

    @Column(name = "address_4")
    private String                  address4;

    @Column(name = "post_code")
    private String                  postCode;

    @Column(name = "msisdn")
    private String                  msisdn;

    @Column(name = "nationality_country_iso")
    private String                  nationalityCountryISO;

    @Column(name = "special_tax_status")
    private boolean                 specialTaxStatus;

    @Column(name = "payment_terms_type")
    private int                     paymentTermsType;

    @Column(name = "currency")
    private String                  currency;

    @Column(name = "amount_balance")
    private BigDecimal              balanceAmount;

    @Column(name = "amount_overdraft_limit")
    private BigDecimal              amountOverdraftLimit;

    @Column(name = "amount_hold")
    private BigDecimal              amountHold;

    @Column(name = "denial_reason")
    private String                  denialReason;

    @Column(name = "is_deleted")
    private boolean                 isDeleted;

    @Column(name = "language_id")
    private long                    languageId;

    @Column(name = "is_trip_approved_email")
    private boolean                 isTripApprovedEmail;

    @Column(name = "is_deduct_credit_registered_trucks")
    private boolean                 isDeductCreditRegisteredTrucks;

    @Column(name = "is_send_low_account_balance_warn")
    private boolean                 isSendLowAccountBalanceWarn;

    @Column(name = "amount_low_account_balance_warn")
    private BigDecimal              amountLowAccountBalanceWarn;

    @Column(name = "date_deleted")
    @Temporal(TemporalType.TIMESTAMP)
    private Date                    dateDeleted;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date                    dateCreated;

    @Column(name = "date_edited")
    @Temporal(TemporalType.TIMESTAMP)
    private Date                    dateEdited;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.PERSIST)
    private List<Vehicle>           vehicleList;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.PERSIST)
    private List<DriverAssociation> driverAssociationList;

    public long getId() {
        return id;
    }

    public void setId(long clientId) {
        this.id = clientId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String clientCode) {
        this.code = clientCode;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName;
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

    public int getPaymentTermsType() {
        return paymentTermsType;
    }

    public void setPaymentTermsType(int paymentTermsType) {
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

    public BigDecimal getAmountHold() {
        return amountHold;
    }

    public void setAmountHold(BigDecimal amountHold) {
        this.amountHold = amountHold;
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

    public long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(long languageId) {
        this.languageId = languageId;
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

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public List<DriverAssociation> getDriverAssociationList() {
        return driverAssociationList;
    }

    public void setDriverAssociationList(List<DriverAssociation> driverAssociationList) {
        this.driverAssociationList = driverAssociationList;
    }

    public String getEmailListInvoiceStatement() {
        return emailListInvoiceStatement;
    }

    public void setEmailListInvoiceStatement(String emailListInvoiceStatement) {
        this.emailListInvoiceStatement = emailListInvoiceStatement;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Account [id=");
        builder.append(id);
        builder.append(", code=");
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
        builder.append(", companyShortName=");
        builder.append(companyShortName);
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
        builder.append(", amountHold=");
        builder.append(amountHold);
        builder.append(", denialReason=");
        builder.append(denialReason);
        builder.append(", isDeleted=");
        builder.append(isDeleted);
        builder.append(", isTripApprovedEmail=");
        builder.append(isTripApprovedEmail);
        builder.append(", isSendLowAccountBalanceWarn=");
        builder.append(isSendLowAccountBalanceWarn);
        builder.append(", amountLowAccountBalanceWarn=");
        builder.append(amountLowAccountBalanceWarn);
        builder.append(", dateDeleted=");
        builder.append(dateDeleted);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateEdited=");
        builder.append(dateEdited);
        builder.append(", vehicleList=");
        builder.append(vehicleList == null ? "null" : vehicleList.size());
        builder.append(", driverAssociationList=");
        builder.append(driverAssociationList == null ? "null" : driverAssociationList.size());
        builder.append("]");
        return builder.toString();
    }

}
