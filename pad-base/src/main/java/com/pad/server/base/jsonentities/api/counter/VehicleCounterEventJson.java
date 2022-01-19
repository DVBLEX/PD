package com.pad.server.base.jsonentities.api.counter;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleCounterEventJson {

    private String type;
    private String dateCount;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateCount() {
        return dateCount;
    }

    public void setDateCount(String dateCount) {
        this.dateCount = dateCount;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("VehicleCounterRequestJson [type=");
        builder.append(type);
        builder.append(", dateCount=");
        builder.append(dateCount);
        builder.append("]");
        return builder.toString();
    }

}
