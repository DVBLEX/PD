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
@Table(name = "port_operator_trip_fee")
public class PortOperatorTripFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "port_operator_id")
    private long              portOperatorId;

    @Column(name = "transaction_type")
    private int               transactionType;

    @Column(name = "trip_amount_fee")
    private BigDecimal        tripFeeAmount;

    @Column(name = "trip_amount_fee_other")
    private BigDecimal        tripFeeOtherAmount;

    @Column(name = "operator_amount_fee")
    private BigDecimal        operatorFeeAmount;

    @Column(name = "operator_id")
    private long              operatorId;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    @Column(name = "date_edited")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateEdited;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPortOperatorId() {
        return portOperatorId;
    }

    public void setPortOperatorId(long portOperatorId) {
        this.portOperatorId = portOperatorId;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getTripFeeAmount() {
        return tripFeeAmount;
    }

    public void setTripFeeAmount(BigDecimal tripFeeAmount) {
        this.tripFeeAmount = tripFeeAmount;
    }

    public BigDecimal getTripFeeOtherAmount() {
        return tripFeeOtherAmount;
    }

    public void setTripFeeOtherAmount(BigDecimal tripFeeOtherAmount) {
        this.tripFeeOtherAmount = tripFeeOtherAmount;
    }

    public BigDecimal getOperatorFeeAmount() {
        return operatorFeeAmount;
    }

    public void setOperatorFeeAmount(BigDecimal operatorFeeAmount) {
        this.operatorFeeAmount = operatorFeeAmount;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PortOperatorTripFee [id=");
        builder.append(id);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", transactionType=");
        builder.append(transactionType);
        builder.append(", tripFeeAmount=");
        builder.append(tripFeeAmount);
        builder.append(", tripFeeOtherAmount=");
        builder.append(tripFeeOtherAmount);
        builder.append(", operatorFeeAmount=");
        builder.append(operatorFeeAmount);
        builder.append(", operatorId=");
        builder.append(operatorId);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateEdited=");
        builder.append(dateEdited);
        builder.append("]");
        return builder.toString();
    }

}
