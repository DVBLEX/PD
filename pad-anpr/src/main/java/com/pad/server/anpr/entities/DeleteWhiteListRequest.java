package com.pad.server.anpr.entities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class DeleteWhiteListRequest {

    private String              apiSecret;
    private long                parkingPermissionId;

    private List<NameValuePair> nameValuePairs;

    public DeleteWhiteListRequest(String apiSecret, long parkingPermissionId) {
        super();
        this.apiSecret = apiSecret;
        this.parkingPermissionId = parkingPermissionId;

        generateNameValuePairs();
    }

    private void generateNameValuePairs() {

        this.nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("apiSecret", this.getApiSecret()));
        nameValuePairs.add(new BasicNameValuePair("parkingPermissionId", this.getParkingPermissionId() + ""));
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public long getParkingPermissionId() {
        return parkingPermissionId;
    }

    public void setParkingPermissionId(long parkingPermissionId) {
        this.parkingPermissionId = parkingPermissionId;
    }

    public List<NameValuePair> getNameValuePairs() {
        return nameValuePairs;
    }

    public void setNameValuePairs(List<NameValuePair> nameValuePairs) {
        this.nameValuePairs = nameValuePairs;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DeleteWhiteListRequest [apiSecret=");
        builder.append(apiSecret == null ? "null" : "xxxxxx-" + apiSecret.length());
        builder.append(", parkingPermissionId=");
        builder.append(parkingPermissionId);
        builder.append(", nameValuePairs=");
        builder.append(nameValuePairs);
        builder.append("]");
        return builder.toString();
    }

}
