package com.pad.server.base.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "booking_slot_limits_default")
public class BookingSlotLimitsDefault implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private long              id;

    @Column(name = "port_operator_id")
    private long              portOperatorId;

    @Column(name = "transaction_type")
    private int               transactionType;

    @Column(name = "day_of_week_id")
    private int               dayOfWeekId;

    @Column(name = "day_of_week_name")
    private String            dayOfWeekName;

    @Column(name = "hour_slot_from")
    private int               hourSlotFrom;

    @Column(name = "hour_slot_to")
    private int               hourSlotTo;

    @Column(name = "booking_limit")
    private int               bookingLimit;

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

    public int getDayOfWeekId() {
        return dayOfWeekId;
    }

    public void setDayOfWeekId(int dayOfWeekId) {
        this.dayOfWeekId = dayOfWeekId;
    }

    public String getDayOfWeekName() {
        return dayOfWeekName;
    }

    public void setDayOfWeekName(String dayOfWeekName) {
        this.dayOfWeekName = dayOfWeekName;
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

    public int getBookingLimit() {
        return bookingLimit;
    }

    public void setBookingLimit(int bookingLimit) {
        this.bookingLimit = bookingLimit;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BookingSlotLimitsDefault [id=");
        builder.append(id);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", transactionType=");
        builder.append(transactionType);
        builder.append(", dayOfWeekId=");
        builder.append(dayOfWeekId);
        builder.append(", dayOfWeekName=");
        builder.append(dayOfWeekName);
        builder.append(", hourSlotFrom=");
        builder.append(hourSlotFrom);
        builder.append(", hourSlotTo=");
        builder.append(hourSlotTo);
        builder.append(", bookingLimit=");
        builder.append(bookingLimit);
        builder.append("]");
        return builder.toString();
    }

}
