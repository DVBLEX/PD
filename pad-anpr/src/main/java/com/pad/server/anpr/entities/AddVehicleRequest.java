package com.pad.server.anpr.entities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class AddVehicleRequest {

    private String              apiSecret;
    private String              plateNumber;
    private long                companyId;
    private long                typeId;
    private long                colorId;

    private List<NameValuePair> nameValuePairs;

    public AddVehicleRequest(String apiSecret, String plateNumber, long companyId, long typeId, long colorId) {
        super();
        this.apiSecret = apiSecret;
        this.plateNumber = plateNumber;
        this.companyId = companyId;
        this.typeId = typeId;
        this.colorId = colorId;

        generateNameValuePairs();
    }

    private void generateNameValuePairs() {

        this.nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("apiSecret", this.getApiSecret()));
        nameValuePairs.add(new BasicNameValuePair("plateNumber", this.getPlateNumber()));
        nameValuePairs.add(new BasicNameValuePair("companyId", String.valueOf(this.getCompanyId())));
        nameValuePairs.add(new BasicNameValuePair("typeId", String.valueOf(this.getTypeId())));
        nameValuePairs.add(new BasicNameValuePair("colorId", String.valueOf(this.getColorId())));
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public long getColorId() {
        return colorId;
    }

    public void setColorId(long colorId) {
        this.colorId = colorId;
    }

    public List<NameValuePair> getNameValuePairs() {
        return nameValuePairs;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AddVehicleRequest [apiSecret=");
        builder.append(apiSecret == null ? "null" : "xxxxxx-" + apiSecret.length());
        builder.append(", plateNumber=");
        builder.append(plateNumber);
        builder.append(", companyId=");
        builder.append(companyId);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append(", colorId=");
        builder.append(colorId);
        builder.append(", nameValuePairs=");
        builder.append(nameValuePairs);
        builder.append("]");
        return builder.toString();
    }

}
