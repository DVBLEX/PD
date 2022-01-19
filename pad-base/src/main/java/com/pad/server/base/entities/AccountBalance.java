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

@Entity
@Table(name = "account_balances")
public class AccountBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "account_id")
    private long              accountId;

    @Column(name = "payment_terms_type")
    private int               paymentTermsType;

    @Column(name = "amount_balance")
    private BigDecimal        amountBalance;

    @Column(name = "amount_overdraft_limit")
    private BigDecimal        amountOverdraftLimit;

    @Column(name = "amount_hold")
    private BigDecimal        amountHold;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public int getPaymentTermsType() {
        return paymentTermsType;
    }

    public void setPaymentTermsType(int paymentTermsType) {
        this.paymentTermsType = paymentTermsType;
    }

    public BigDecimal getAmountBalance() {
        return amountBalance;
    }

    public void setAmountBalance(BigDecimal amountBalance) {
        this.amountBalance = amountBalance;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AccountBalance [id=");
        builder.append(id);
        builder.append(", accountId=");
        builder.append(accountId);
        builder.append(", paymentTermsType=");
        builder.append(paymentTermsType);
        builder.append(", amountBalance=");
        builder.append(amountBalance);
        builder.append(", amountOverdraftLimit=");
        builder.append(amountOverdraftLimit);
        builder.append(", amountHold=");
        builder.append(amountHold);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append("]");
        return builder.toString();
    }

}
