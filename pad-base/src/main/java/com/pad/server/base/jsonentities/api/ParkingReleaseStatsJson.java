package com.pad.server.base.jsonentities.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParkingReleaseStatsJson {

    private int                           transactionType;
    private String                        translateKey;
    private String                        translateKeyShort;
    private int                           parkingSessionsCount;
    private boolean                       isAutoReleaseOn;
    private int                           bookingLimitCount;
    private int                           vehiclesAlreadyReleasedCount;
    private List<VehicleRegistrationJson> releasedVehicleRegistrationList;

    public ParkingReleaseStatsJson() {
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public String getTranslateKey() {
        return translateKey;
    }

    public void setTranslateKey(String translateKey) {
        this.translateKey = translateKey;
    }

    public String getTranslateKeyShort() {
        return translateKeyShort;
    }

    public void setTranslateKeyShort(String translateKeyShort) {
        this.translateKeyShort = translateKeyShort;
    }

    public int getParkingSessionsCount() {
        return parkingSessionsCount;
    }

    public void setParkingSessionsCount(int parkingSessionsCount) {
        this.parkingSessionsCount = parkingSessionsCount;
    }

    public boolean getIsAutoReleaseOn() {
        return isAutoReleaseOn;
    }

    public void setIsAutoReleaseOn(boolean isAutoReleaseOn) {
        this.isAutoReleaseOn = isAutoReleaseOn;
    }

    public int getBookingLimitCount() {
        return bookingLimitCount;
    }

    public void setBookingLimitCount(int bookingLimitCount) {
        this.bookingLimitCount = bookingLimitCount;
    }

    public int getVehiclesAlreadyReleasedCount() {
        return vehiclesAlreadyReleasedCount;
    }

    public void setVehiclesAlreadyReleasedCount(int vehiclesAlreadyReleasedCount) {
        this.vehiclesAlreadyReleasedCount = vehiclesAlreadyReleasedCount;
    }

    public List<VehicleRegistrationJson> getReleasedVehicleRegistrationList() {
        return releasedVehicleRegistrationList;
    }

    public void setReleasedVehicleRegistrationList(List<VehicleRegistrationJson> releasedVehicleRegistrationList) {
        this.releasedVehicleRegistrationList = releasedVehicleRegistrationList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ParkingReleaseStatsJson [transactionType=");
        builder.append(transactionType);
        builder.append(", translateKey=");
        builder.append(translateKey);
        builder.append(", translateKeyShort=");
        builder.append(translateKeyShort);
        builder.append(", parkingSessionsCount=");
        builder.append(parkingSessionsCount);
        builder.append(", isAutoReleaseOn=");
        builder.append(isAutoReleaseOn);
        builder.append(", bookingLimitCount=");
        builder.append(bookingLimitCount);
        builder.append(", vehiclesAlreadyReleasedCount=");
        builder.append(vehiclesAlreadyReleasedCount);
        builder.append(", releasedVehicleRegistrationList=");
        builder.append(releasedVehicleRegistrationList == null ? "null" : releasedVehicleRegistrationList.size());
        builder.append("]");
        return builder.toString();
    }

}
