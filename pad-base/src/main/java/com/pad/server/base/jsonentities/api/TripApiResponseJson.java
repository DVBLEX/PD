package com.pad.server.base.jsonentities.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripApiResponseJson {

    private String code;
    private String referenceNumber;

    public TripApiResponseJson(String code, String referenceNumber) {
        super();
        this.code = code;
        this.referenceNumber = referenceNumber;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TripApiResponseJson [code=");
        builder.append(code);
        builder.append(", referenceNumber=");
        builder.append(referenceNumber);
        builder.append("]");
        return builder.toString();
    }

}
