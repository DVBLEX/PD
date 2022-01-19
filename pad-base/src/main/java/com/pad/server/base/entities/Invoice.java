package com.pad.server.base.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
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
@Table(name = "invoice")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "code")
    private String            code;

    @Column(name = "reference")
    private String            reference;

    @Column(name = "type")
    private int               type;

    @Column(name = "account_id")
    private long              accountId;

    @Column(name = "total_amount")
    private BigDecimal        totalAmount;

    @Column(name = "currency")
    private String            currency;

    @Column(name = "date_due")
    @Temporal(TemporalType.DATE)
    private Date              dateDue;

    @Column(name = "is_paid")
    private boolean           isPaid;

    @Column(name = "date_payment")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              datePayment;

    @Column(name = "type_payment")
    private int               typePayment;

    @Column(name = "path")
    private String            path;

    @Column(name = "operator_id")
    private long              operatorId;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    @Column(name = "date_edited")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateEdited;

    @Transient
    private String            dateDueString;

    @Transient
    private String            datePaymentString;

    @Transient
    private String            dateCreatedString;

    @Transient
    private String            companyName;

    @Transient
    private String            datePeriodStartString;

    @Transient
    private String            datePeriodEndString;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
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

    public Date getDateDue() {
        return dateDue;
    }

    public void setDateDue(Date dateDue) {
        this.dateDue = dateDue;
    }

    public boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Date getDatePayment() {
        return datePayment;
    }

    public void setDatePayment(Date datePayment) {
        this.datePayment = datePayment;
    }

    public int getTypePayment() {
        return typePayment;
    }

    public void setTypePayment(int typePayment) {
        this.typePayment = typePayment;
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

    public String getDateDueString() {
        try {
            return this.dateDue == null ? "" : ServerUtil.formatDate(ServerConstants.dateFormatddMMyyyy, this.dateDue);
        } catch (ParseException e) {
            return "";
        }
    }

    public String getDatePaymentString() {
        try {
            return this.datePayment == null ? "" : ServerUtil.formatDate(ServerConstants.dateFormatddMMyyyy, this.datePayment);
        } catch (ParseException e) {
            return "";
        }
    }

    public String getDateCreatedString() {
        try {
            return this.dateCreated == null ? "" : ServerUtil.formatDate(ServerConstants.dateFormatddMMyyyy, this.dateCreated);
        } catch (ParseException e) {
            return "";
        }
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDatePeriodStartString() {
        try {
            LocalDate datePeriodStart = this.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());

            return ServerUtil.formatDate(ServerConstants.dateFormatddMMyyyy, Date.from(datePeriodStart.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } catch (ParseException e) {
            return "";
        }
    }

    public String getDatePeriodEndString() {
        try {
            LocalDate datePeriodEnd = this.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

            return ServerUtil.formatDate(ServerConstants.dateFormatddMMyyyy, Date.from(datePeriodEnd.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } catch (ParseException e) {
            return "";
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Invoice [id=");
        builder.append(id);
        builder.append(", code=");
        builder.append(code);
        builder.append(", reference=");
        builder.append(reference);
        builder.append(", type=");
        builder.append(type);
        builder.append(", accountId=");
        builder.append(accountId);
        builder.append(", totalAmount=");
        builder.append(totalAmount);
        builder.append(", currency=");
        builder.append(currency);
        builder.append(", dateDue=");
        builder.append(dateDue);
        builder.append(", isPaid=");
        builder.append(isPaid);
        builder.append(", datePayment=");
        builder.append(datePayment);
        builder.append(", typePayment=");
        builder.append(typePayment);
        builder.append(", path=");
        builder.append(path);
        builder.append(", operatorId=");
        builder.append(operatorId);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateEdited=");
        builder.append(dateEdited);
        builder.append(", dateDueString=");
        builder.append(dateDueString);
        builder.append(", datePaymentString=");
        builder.append(datePaymentString);
        builder.append(", dateCreatedString=");
        builder.append(dateCreatedString);
        builder.append(", companyName=");
        builder.append(companyName);
        builder.append("]");
        return builder.toString();
    }

}
