package com.pad.server.base.jsonentities.api.counter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleCounterRequestJson {

    public static final String            TYPE_AUTOMATIC = "A";
    public static final String            TYPE_MANUAL    = "M";
    public static final String            TYPE_UNKNOWN   = "U";
    public static final String            TYPE_HEARTBEAT = "H";

    private String                        deviceId;
    private List<VehicleCounterEventJson> events;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<VehicleCounterEventJson> getEvents() {
        return events;
    }

    public void setEvents(List<VehicleCounterEventJson> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("VehicleCounterRequestJson [deviceId=");
        builder.append(deviceId);
        builder.append(", events=");
        builder.append(events);
        builder.append("]");
        return builder.toString();
    }

}
