package com.pad.server.base.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
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
import com.pad.server.base.common.ServerUtil;

@Entity
@Table(name = "receipt")
public class Receipt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "number")
    private String            number;

    @Column(name = "account_id")
    private long              accountId;

    @Column(name = "payment_id")
    private long              paymentId;

    @Column(name = "first_name")
    private String            firstName;

    @Column(name = "last_name")
    private String            lastName;

    @Column(name = "msisdn")
    private String            msisdn;

    @Column(name = "payment_option")
    private int               paymentOption;

    @Column(name = "total_amount")
    private BigDecimal        totalAmount;

    @Column(name = "currency")
    private String            currency;

    @Column(name = "path")
    private String            path;

    @Column(name = "unique_url")
    private String            uniqueUrl;

    @Column(name = "operator_id")
    private long              operatorId;

    @Column(name = "lock_count_failed")
    private long              lockCountFailed;

    @Column(name = "date_lock")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateLock;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    @Column(name = "date_edited")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateEdited;

    @Transient
    private String            dateCreatedString;

    @Transient
    private String            paymentOptionString;

    @Transient
    private String            payerName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
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

    public int getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(int paymentOption) {
        this.paymentOption = paymentOption;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUniqueUrl() {
        return uniqueUrl;
    }

    public void setUniqueUrl(String uniqueUrl) {
        this.uniqueUrl = uniqueUrl;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public long getLockCountFailed() {
        return lockCountFailed;
    }

    public void setLockCountFailed(long lockCountFailed) {
        this.lockCountFailed = lockCountFailed;
    }

    public Date getDateLock() {
        return dateLock;
    }

    public void setDateLock(Date dateLock) {
        this.dateLock = dateLock;
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

    public String getDateCreatedString() {
        try {
            return this.dateCreated == null ? "" : ServerUtil.formatDate(ServerConstants.dateFormatddMMyyyy, this.dateCreated);
        } catch (ParseException e) {
            return "";
        }
    }

    public String getPaymentOptionString() {
        return paymentOptionString;
    }

    public void setPaymentOptionString(String paymentOptionString) {
        this.paymentOptionString = paymentOptionString;
    }

    public String getPayerName() {
        return this.getFirstName() + " " + this.lastName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Receipt [id=");
        builder.append(id);
        builder.append(", number=");
        builder.append(number);
        builder.append(", accountId=");
        builder.append(accountId);
        builder.append(", paymentId=");
        builder.append(paymentId);
        builder.append(", firstName=");
        builder.append(firstName);
        builder.append(", lastName=");
        builder.append(lastName);
        builder.append(", msisdn=");
        builder.append(msisdn);
        builder.append(", paymentOption=");
        builder.append(paymentOption);
        builder.append(", totalAmount=");
        builder.append(totalAmount);
        builder.append(", currency=");
        builder.append(currency);
        builder.append(", path=");
        builder.append(path);
        builder.append(", uniqueUrl=");
        builder.append(uniqueUrl);
        builder.append(", operatorId=");
        builder.append(operatorId);
        builder.append(", lockCountFailed=");
        builder.append(lockCountFailed);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateLock=");
        builder.append(dateLock);
        builder.append(", dateEdited=");
        builder.append(dateEdited);
        builder.append("]");
        return builder.toString();
    }

}
