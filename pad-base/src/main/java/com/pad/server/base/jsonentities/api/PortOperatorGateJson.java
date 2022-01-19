package com.pad.server.base.jsonentities.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PortOperatorGateJson {

    private long   id;
    private String gateNumber;
    private String gateNumberShort;

    public PortOperatorGateJson() {
    }

    public PortOperatorGateJson(long id, String gateNumber, String gateNumberShort) {
        this.id = id;
        this.gateNumber = gateNumber;
        this.gateNumberShort = gateNumberShort;
    }

    public PortOperatorGateJson(String gateNumber, String gateNumberShort) {
        this.gateNumber = gateNumber;
        this.gateNumberShort = gateNumberShort;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGateNumber() {
        return gateNumber;
    }

    public void setGateNumber(String gateNumber) {
        this.gateNumber = gateNumber;
    }

    public String getGateNumberShort() {
        return gateNumberShort;
    }

    public void setGateNumberShort(String gateNumberShort) {
        this.gateNumberShort = gateNumberShort;
    }

    @Override
    public int hashCode() {
        return this.gateNumberShort.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof PortOperatorGateJson) {
            PortOperatorGateJson p = (PortOperatorGateJson) obj;
            return (p.gateNumberShort.equals(this.gateNumberShort));
        } else
            return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PortOperatorGateJson [id=");
        builder.append(id);
        builder.append(", gateNumber=");
        builder.append(gateNumber);
        builder.append(", gateNumberShort=");
        builder.append(gateNumberShort);
        builder.append("]");
        return builder.toString();
    }

}
