package com.pad.server.base.jsonentities.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripBookingTimeJson {

    private Long    portOperatorId;
    private Integer transactionType;
    private String  dateSlotString;
    private String  time;
    private String  timeFormat;
    private boolean isTimeSlotDisabled;

    public TripBookingTimeJson() {
    }

    public Long getPortOperatorId() {
        return portOperatorId;
    }

    public void setPortOperatorId(Long portOperatorId) {
        this.portOperatorId = portOperatorId;
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    public String getDateSlotString() {
        return dateSlotString;
    }

    public void setDateSlotString(String dateSlotString) {
        this.dateSlotString = dateSlotString;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public boolean getIsTimeSlotDisabled() {
        return isTimeSlotDisabled;
    }

    public void setIsTimeSlotDisabled(boolean isTimeSlotDisabled) {
        this.isTimeSlotDisabled = isTimeSlotDisabled;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TripBookingTimeJson [portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", transactionType=");
        builder.append(transactionType);
        builder.append(", dateSlotString=");
        builder.append(dateSlotString);
        builder.append(", time=");
        builder.append(time);
        builder.append(", timeFormat=");
        builder.append(timeFormat);
        builder.append(", isTimeSlotDisabled=");
        builder.append(isTimeSlotDisabled);
        builder.append("]");
        return builder.toString();
    }

}
