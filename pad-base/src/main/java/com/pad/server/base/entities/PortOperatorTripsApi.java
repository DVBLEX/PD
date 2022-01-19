package com.pad.server.base.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "port_operator_trips_api")
public class PortOperatorTripsApi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "type")
    private String            type;

    @Column(name = "reference_number")
    private String            referenceNumber;

    @Column(name = "transaction_type")
    private String            transactionType;

    @Column(name = "transporter_short_name")
    private String            transporterShortName;

    @Column(name = "vehicle_reg_number")
    private String            vehicleRegNumber;

    @Column(name = "container_id")
    private String            containerId;

    @Column(name = "container_type")
    private String            containerType;

    @Column(name = "date_slot_from")
    private String            dateSlotFrom;

    @Column(name = "date_slot_to")
    private String            dateSlotTo;

    @Column(name = "date_request")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateRequest;

    @Column(name = "trip_id")
    private long              tripId;

    @Column(name = "trip_code")
    private String            tripCode;

    @Column(name = "response_code")
    private int               responseCode;

    @Column(name = "response_text")
    private String            responseText;

    @Column(name = "date_response")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateResponse;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public Date getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(Date dateRequest) {
        this.dateRequest = dateRequest;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public Date getDateResponse() {
        return dateResponse;
    }

    public void setDateResponse(Date dateResponse) {
        this.dateResponse = dateResponse;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PortOperatorTripsApi [id=");
        builder.append(id);
        builder.append(", type=");
        builder.append(type);
        builder.append(", referenceNumber=");
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
        builder.append(", dateRequest=");
        builder.append(dateRequest);
        builder.append(", tripId=");
        builder.append(tripId);
        builder.append(", tripCode=");
        builder.append(tripCode);
        builder.append(", responseCode=");
        builder.append(responseCode);
        builder.append(", responseText=");
        builder.append(responseText);
        builder.append(", dateResponse=");
        builder.append(dateResponse);
        builder.append("]");
        return builder.toString();
    }

}
