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
@Table(name = "anpr_scheduler")
public class AnprScheduler implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private long              id;

    @Column(name = "is_processed")
    private int               isProcessed;

    @Column(name = "request_type")
    private int               requestType;

    @Column(name = "zone_id")
    private long              zoneId;

    @Column(name = "mission_id")
    private long              missionId;

    @Column(name = "trip_id")
    private long              tripId;

    @Column(name = "port_access_whitelist_id")
    private long              portAccessWhitelistId;

    @Column(name = "vehicle_registration")
    private String            vehicleRegistration;

    @Column(name = "parking_permission_id")
    private long              parkingPermissionId;

    @Column(name = "date_valid_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateValidFrom;

    @Column(name = "date_valid_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateValidTo;

    @Column(name = "priority")
    private int               priority;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    @Column(name = "date_scheduled")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateScheduled;

    @Column(name = "retry_count")
    private int               retryCount;

    @Column(name = "date_processed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateProcessed;

    @Column(name = "response_code")
    private int               responseCode;

    @Column(name = "response_text")
    private String            responseText;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(int isProcessed) {
        this.isProcessed = isProcessed;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public long getZoneId() {
        return zoneId;
    }

    public void setZoneId(long zoneId) {
        this.zoneId = zoneId;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public long getPortAccessWhitelistId() {
        return portAccessWhitelistId;
    }

    public void setPortAccessWhitelistId(long portAccessWhitelistId) {
        this.portAccessWhitelistId = portAccessWhitelistId;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public long getParkingPermissionId() {
        return parkingPermissionId;
    }

    public void setParkingPermissionId(long parkingPermissionId) {
        this.parkingPermissionId = parkingPermissionId;
    }

    public Date getDateValidFrom() {
        return dateValidFrom;
    }

    public void setDateValidFrom(Date dateValidFrom) {
        this.dateValidFrom = dateValidFrom;
    }

    public Date getDateValidTo() {
        return dateValidTo;
    }

    public void setDateValidTo(Date dateValidTo) {
        this.dateValidTo = dateValidTo;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateScheduled() {
        return dateScheduled;
    }

    public void setDateScheduled(Date dateScheduled) {
        this.dateScheduled = dateScheduled;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public Date getDateProcessed() {
        return dateProcessed;
    }

    public void setDateProcessed(Date dateProcessed) {
        this.dateProcessed = dateProcessed;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AnprScheduler [id=");
        builder.append(id);
        builder.append(", isProcessed=");
        builder.append(isProcessed);
        builder.append(", requestType=");
        builder.append(requestType);
        builder.append(", zoneId=");
        builder.append(zoneId);
        builder.append(", missionId=");
        builder.append(missionId);
        builder.append(", tripId=");
        builder.append(tripId);
        builder.append(", portAccessWhitelistId=");
        builder.append(portAccessWhitelistId);
        builder.append(", vehicleRegistration=");
        builder.append(vehicleRegistration);
        builder.append(", parkingPermissionId=");
        builder.append(parkingPermissionId);
        builder.append(", dateValidFrom=");
        builder.append(dateValidFrom);
        builder.append(", dateValidTo=");
        builder.append(dateValidTo);
        builder.append(", priority=");
        builder.append(priority);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateScheduled=");
        builder.append(dateScheduled);
        builder.append(", retryCount=");
        builder.append(retryCount);
        builder.append(", dateProcessed=");
        builder.append(dateProcessed);
        builder.append(", responseCode=");
        builder.append(responseCode);
        builder.append(", responseText=");
        builder.append(responseText);
        builder.append("]");
        return builder.toString();
    }

}
