package com.pad.server.base.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "booking_slot_counts")
public class BookingSlotCount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private long              id;

    @Column(name = "port_operator_id")
    private long              portOperatorId;

    @Column(name = "transaction_type")
    private int               transactionType;

    @Column(name = "date_slot")
    @Temporal(TemporalType.DATE)
    private Date              dateSlot;

    @Column(name = "hour_slot_from")
    private int               hourSlotFrom;

    @Column(name = "hour_slot_to")
    private int               hourSlotTo;

    @Column(name = "trips_booked_count")
    private int               tripsBookedCount;

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

    public Date getDateSlot() {
        return dateSlot;
    }

    public void setDateSlot(Date dateSlot) {
        this.dateSlot = dateSlot;
    }

    public int getHourSlotFrom() {
        return hourSlotFrom;
    }

    public void setHourSlotFrom(int hourSlotFrom) {
        this.hourSlotFrom = hourSlotFrom;
    }

    public int getHourSlotTo() {
        return hourSlotTo;
    }

    public void setHourSlotTo(int hourSlotTo) {
        this.hourSlotTo = hourSlotTo;
    }

    public int getTripsBookedCount() {
        return tripsBookedCount;
    }

    public void setTripsBookedCount(int tripsBookedCount) {
        this.tripsBookedCount = tripsBookedCount;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BookingSlotCount [id=");
        builder.append(id);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", transactionType=");
        builder.append(transactionType);
        builder.append(", dateSlot=");
        builder.append(dateSlot);
        builder.append(", hourSlotFrom=");
        builder.append(hourSlotFrom);
        builder.append(", hourSlotTo=");
        builder.append(hourSlotTo);
        builder.append(", tripsBookedCount=");
        builder.append(tripsBookedCount);
        builder.append("]");
        return builder.toString();
    }

}
