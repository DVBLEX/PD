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
@Table(name = "dpw_trips")
public class DpwTrip implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "code")
    private String            code;

    @Column(name = "reference_number")
    private String            referenceNumber;

    @Column(name = "transaction_type")
    private String            transactionType;

    @Column(name = "transporter_short_name")
    private String            transporterShortName;

    @Column(name = "vehicle_registration")
    private String            vehicleRegNumber;

    @Column(name = "container_id")
    private String            containerId;

    @Column(name = "container_type")
    private String            containerType;

    @Column(name = "status")
    private int               status;

    @Column(name = "date_slot_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateSlotFrom;

    @Column(name = "date_slot_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateSlotTo;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    @Column(name = "date_edited")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateEdited;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getDateSlotFrom() {
        return dateSlotFrom;
    }

    public void setDateSlotFrom(Date dateSlotFrom) {
        this.dateSlotFrom = dateSlotFrom;
    }

    public Date getDateSlotTo() {
        return dateSlotTo;
    }

    public void setDateSlotTo(Date dateSlotTo) {
        this.dateSlotTo = dateSlotTo;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(Date dateEdited) {
        this.dateEdited = dateEdited;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DpwTrip [id=");
        builder.append(id);
        builder.append(", code=");
        builder.append(code);
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
        builder.append(", status=");
        builder.append(status);
        builder.append(", dateSlotFrom=");
        builder.append(dateSlotFrom);
        builder.append(", dateSlotTo=");
        builder.append(dateSlotTo);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateEdited=");
        builder.append(dateEdited);
        builder.append("]");
        return builder.toString();
    }

}
