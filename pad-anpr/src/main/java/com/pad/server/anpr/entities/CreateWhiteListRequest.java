package com.pad.server.anpr.entities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class CreateWhiteListRequest {

    private String              apiSecret;
    private String              validFrom;
    private String              validTo;
    private long                zoneId;
    private long                parkingSpotCount;
    private long                companyId;
    private long                parkingPermissionStatusId;
    private long                personId;
    private long                carId;
    private String              vip;

    private List<NameValuePair> nameValuePairs;

    public CreateWhiteListRequest(String apiSecret, String validFrom, String validTo, long zoneId, long parkingSpotCount, long companyId, long parkingPermissionStatusId,
        long personId, long carId, String vip) {
        super();
        this.apiSecret = apiSecret;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.zoneId = zoneId;
        this.parkingSpotCount = parkingSpotCount;
        this.companyId = companyId;
        this.parkingPermissionStatusId = parkingPermissionStatusId;
        this.personId = personId;
        this.carId = carId;
        this.vip = vip;

        generateNameValuePairs();
    }

    private void generateNameValuePairs() {

        this.nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("apiSecret", this.getApiSecret()));
        nameValuePairs.add(new BasicNameValuePair("validFrom", this.getValidFrom()));
        nameValuePairs.add(new BasicNameValuePair("validTo", this.getValidTo()));
        nameValuePairs.add(new BasicNameValuePair("zoneId", String.valueOf(this.getZoneId())));
        nameValuePairs.add(new BasicNameValuePair("parkingSpotCount", String.valueOf(this.getParkingSpotCount())));
        nameValuePairs.add(new BasicNameValuePair("companyId", String.valueOf(this.getCompanyId())));
        nameValuePairs.add(new BasicNameValuePair("parkingPermissionStatusId", String.valueOf(this.getParkingPermissionStatusId())));
        nameValuePairs.add(new BasicNameValuePair("personId", String.valueOf(this.getPersonId())));
        nameValuePairs.add(new BasicNameValuePair("carId", String.valueOf(this.getCarId())));
        nameValuePairs.add(new BasicNameValuePair("vip", this.getVip()));
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public long getZoneId() {
        return zoneId;
    }

    public void setZoneId(long zoneId) {
        this.zoneId = zoneId;
    }

    public long getParkingSpotCount() {
        return parkingSpotCount;
    }

    public void setParkingSpotCount(long parkingSpotCount) {
        this.parkingSpotCount = parkingSpotCount;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getParkingPermissionStatusId() {
        return parkingPermissionStatusId;
    }

    public void setParkingPermissionStatusId(long parkingPermissionStatusId) {
        this.parkingPermissionStatusId = parkingPermissionStatusId;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public List<NameValuePair> getNameValuePairs() {
        return nameValuePairs;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CreateWhiteListRequest [apiSecret=");
        builder.append(apiSecret == null ? "null" : "xxxxxx-" + apiSecret.length());
        builder.append(", validFrom=");
        builder.append(validFrom);
        builder.append(", validTo=");
        builder.append(validTo);
        builder.append(", zoneId=");
        builder.append(zoneId);
        builder.append(", parkingSpotCount=");
        builder.append(parkingSpotCount);
        builder.append(", companyId=");
        builder.append(companyId);
        builder.append(", parkingPermissionStatusId=");
        builder.append(parkingPermissionStatusId);
        builder.append(", personId=");
        builder.append(personId);
        builder.append(", carId=");
        builder.append(carId);
        builder.append(", vip=");
        builder.append(vip);
        builder.append(", nameValuePairs=");
        builder.append(nameValuePairs);
        builder.append("]");
        return builder.toString();
    }

}
