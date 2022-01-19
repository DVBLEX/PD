package com.pad.server.base.jsonentities.api;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author rafael
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingSlotLimitJson {

    private long    portOperatorId;
    private int     transactionType;
    private int     dayOfWeekId;
    private String  dayOfWeekName;
    private int     hourSlotFrom;
    private int     hourSlotTo;
    private int     bookingLimit;
    private int     bookingCount;
    private String  period;
    private String  hourSlotFromString;
    private String  hourSlotToString;
    private Date    dateSlot;
    private boolean isAllowUpdate;

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

    public int getBookingCount() {
        return bookingCount;
    }

    public void setBookingCount(int bookingCount) {
        this.bookingCount = bookingCount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getHourSlotFromString() {

        if (this.hourSlotFrom < 10) {
            hourSlotFromString = "0" + this.hourSlotFrom + ":00";

        } else {
            hourSlotFromString = this.hourSlotFrom + ":00";
        }

        return hourSlotFromString;
    }

    public String getHourSlotToString() {

        if (this.hourSlotTo < 10) {
            hourSlotToString = "0" + this.hourSlotTo + ":00";

        } else {
            hourSlotToString = this.hourSlotTo + ":00";
        }

        return hourSlotToString;
    }

    public Date getDateSlot() {
        return dateSlot;
    }

    public void setDateSlot(Date dateSlot) {
        this.dateSlot = dateSlot;
    }

    public boolean getIsAllowUpdate() {
        return isAllowUpdate;
    }

    public void setIsAllowUpdate(boolean isAllowUpdate) {
        this.isAllowUpdate = isAllowUpdate;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BookingSlotLimitJson [portOperatorId=");
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
        builder.append(", bookingCount=");
        builder.append(bookingCount);
        builder.append(", period=");
        builder.append(period);
        builder.append("]");
        return builder.toString();
    }

}
