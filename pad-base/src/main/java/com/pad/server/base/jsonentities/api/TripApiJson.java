package com.pad.server.base.jsonentities.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripApiJson {

    private String referenceNumber;
    private String transactionType;
    private String transporterShortName;
    private String vehicleRegNumber;
    private String containerId;
    private String containerType;
    private String dateSlotFrom;
    private String dateSlotTo;

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransporterShortName() {
        return transporterShortName;
    }

    public void setTransporterShortName(String transporterShortName) {
        this.transporterShortName = transporterShortName;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public String getDateSlotFrom() {
        return dateSlotFrom;
    }

    public void setDateSlotFrom(String dateSlotFrom) {
        this.dateSlotFrom = dateSlotFrom;
    }

    public String getDateSlotTo() {
        return dateSlotTo;
    }

    public void setDateSlotTo(String dateSlotTo) {
        this.dateSlotTo = dateSlotTo;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TripApiJson [referenceNumber=");
        builder.append(referenceNumber);
        builder.append(", transactionType=");
        builder.append(transactionType);
        builder.append(", transporterShortName=");
        builder.append(transporterShortName);
        builder.append(", vehicleRegNumber=");
        builder.append(vehicleRegNumber);
        builder.append(", containerId=");
        builder.append(containerId);
        builder.append(", containerType=");
        builder.append(containerType);
        builder.append(", dateSlotFrom=");
        builder.append(dateSlotFrom);
        builder.append(", dateSlotTo=");
        builder.append(dateSlotTo);
        builder.append("]");
        return builder.toString();
    }

}
