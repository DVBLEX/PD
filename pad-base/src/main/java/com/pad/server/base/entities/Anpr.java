package com.pad.server.base.entities;

import java.io.Serializable;
import java.util.Date;

public class Anpr implements Serializable {

    private static final long serialVersionUID = 1L;

    private long              id;
    private int               isProcessed;
    private int               requestType;
    private long              zoneId;
    private long              missionId;
    private long              tripId;
    private long              portAccessWhitelistId;
    private String            vehicleRegistration;
    private long              parkingPermissionId;
    private Date              dateValidFrom;
    private Date              dateValidTo;
    private int               priority;
    private Date              dateCreated;
    private Date              dateScheduled;
    private int               retryCount;
    private Date              dateProcessed;
    private int               responseCode;
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

}
