package com.pad.server.base.jsonentities.api.onlinepayment.cotizel;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CallbackPaymentGeneralResponseJson {

    private int    responseCode;
    private String responseText;
    private Date   responseDate;

    public CallbackPaymentGeneralResponseJson() {

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

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CallbackPaymentGeneralResponseJson [responseCode=");
        builder.append(responseCode);
        builder.append(", responseText=");
        builder.append(responseText);
        builder.append(", responseDate=");
        builder.append(responseDate);
        builder.append("]");
        return builder.toString();
    }

}
