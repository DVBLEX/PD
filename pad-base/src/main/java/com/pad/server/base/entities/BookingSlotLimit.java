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
@Table(name = "booking_slot_limits")
public class BookingSlotLimit implements Serializable {

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

    public Date getDateSlot() {
        return dateSlot;
    }

    public void setDateSlot(Date dateSlot) {
        this.dateSlot = dateSlot;
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
        builder.append("BookingSlotLimit [id=");
        builder.append(id);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", transactionType=");
        builder.append(transactionType);
        builder.append(", dateSlot=");
        builder.append(dateSlot);
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

    public enum DaysOfWeek {

        MONDAY(2, "Monday"), TUESDAY(3, "Tuesday"), WEDNESDAY(4, "Wednesday"), THURSDAY(5, "Thursday"), FRIDAY(6, "Friday"), SATURDAY(7, "Satuday"), SUNDAY(1, "Sunday");

        public final int    id;
        public final String label;

        private DaysOfWeek(int id, String label) {
            this.id = id;
            this.label = label;
        }

        public static DaysOfWeek getDayOfWeekByValue(String value) {
            for (DaysOfWeek dayWeek : values()) {
                if (dayWeek.toString().equalsIgnoreCase(value))
                    return dayWeek;
            }
            return null;
        }

        public static DaysOfWeek getDayOfWeekById(int id) {
            for (DaysOfWeek dayWeek : values()) {
                if (dayWeek.id == id)
                    return dayWeek;
            }
            return null;
        }

    }

}
