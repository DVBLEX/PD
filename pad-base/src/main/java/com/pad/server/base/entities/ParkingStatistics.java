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
@Table(name = "parking_statistics")
public class ParkingStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date              date;

    @Column(name = "port_operator_id")
    private long              portOperatorId;

    @Column(name = "port_operator_name")
    private String            portOperatorName;

    @Column(name = "transaction_type")
    private int               transactionType;

    @Column(name = "count_entry")
    private int               countEntry;

    @Column(name = "amount_total_trip_fee")
    private BigDecimal        totalTripFeeAmount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getPortOperatorId() {
        return portOperatorId;
    }

    public void setPortOperatorId(long portOperatorId) {
        this.portOperatorId = portOperatorId;
    }

    public String getPortOperatorName() {
        return portOperatorName;
    }

    public void setPortOperatorName(String portOperatorName) {
        this.portOperatorName = portOperatorName;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public int getCountEntry() {
        return countEntry;
    }

    public void setCountEntry(int countEntry) {
        this.countEntry = countEntry;
    }

    public BigDecimal getTotalTripFeeAmount() {
        return totalTripFeeAmount;
    }

    public void setTotalTripFeeAmount(BigDecimal totalTripFeeAmount) {
        this.totalTripFeeAmount = totalTripFeeAmount;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ParkingStatistics [id=");
        builder.append(id);
        builder.append(", date=");
        builder.append(date);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", portOperatorName=");
        builder.append(portOperatorName);
        builder.append(", transactionType=");
        builder.append(transactionType);
        builder.append(", countEntry=");
        builder.append(countEntry);
        builder.append(", totalTripFeeAmount=");
        builder.append(totalTripFeeAmount);
        builder.append("]");
        return builder.toString();
    }

}
