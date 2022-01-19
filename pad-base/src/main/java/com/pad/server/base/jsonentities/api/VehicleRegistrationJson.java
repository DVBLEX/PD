package com.pad.server.base.jsonentities.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleRegistrationJson {

    private String vehicleRegistration;
    private String colourCode;

    public VehicleRegistrationJson() {
    }

    public VehicleRegistrationJson(String vehicleRegistration, String colourCode) {
        this.vehicleRegistration = vehicleRegistration;
        this.colourCode = colourCode;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public String getColourCode() {
        return colourCode;
    }

    public void setColourCode(String colourCode) {
        this.colourCode = colourCode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("VehicleRegistrationJson [vehicleRegistration=");
        builder.append(vehicleRegistration);
        builder.append(", colourCode=");
        builder.append(colourCode);
        builder.append("]");
        return builder.toString();
    }

}
