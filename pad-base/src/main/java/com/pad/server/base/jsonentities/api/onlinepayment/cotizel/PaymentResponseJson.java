package com.pad.server.base.jsonentities.api.onlinepayment.cotizel;

import org.apache.commons.codec.digest.DigestUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponseJson {

    private String amount;
    private String dateTime;
    private String fees;
    private String hashcode;
    private String idFromClient;
    private String idFromGU;
    private String recipientNumber;
    private String reference;
    private String serviceCode;
    private String status;
    private String error;

    @JsonProperty(value = "300")
    private String error300;

    @JsonProperty(value = "301")
    private String error301;

    @JsonProperty(value = "302")
    private String error302;

    @JsonProperty(value = "303")
    private String error303;

    @JsonProperty(value = "304")
    private String error304;

    @JsonProperty(value = "400")
    private String error400;

    @JsonProperty(value = "401")
    private String error401;

    @JsonProperty(value = "402")
    private String error402;

    @JsonProperty(value = "403")
    private String error403;

    @JsonProperty(value = "405")
    private String error405;

    public PaymentResponseJson() {

    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getHashcode() {
        return DigestUtils.sha256Hex("amount=" + amount + "&dateTime=" + dateTime + "&fees=" + fees + "&idFromClient=" + idFromClient + "&idFromGU=" + idFromGU
            + "&recipientNumber=" + recipientNumber + "&reference=" + reference + "&serviceCode=" + serviceCode + "&status=" + status);
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public String getIdFromClient() {
        return idFromClient;
    }

    public void setIdFromClient(String idFromClient) {
        this.idFromClient = idFromClient;
    }

    public String getIdFromGU() {
        return idFromGU;
    }

    public void setIdFromGU(String idFromGU) {
        this.idFromGU = idFromGU;
    }

    public String getRecipientNumber() {
        return recipientNumber;
    }

    public void setRecipientNumber(String recipientNumber) {
        this.recipientNumber = recipientNumber;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError300() {
        return error300;
    }

    public void setError300(String error300) {
        this.error300 = error300;
    }

    public String getError301() {
        return error301;
    }

    public void setError301(String error301) {
        this.error301 = error301;
    }

    public String getError302() {
        return error302;
    }

    public void setError302(String error302) {
        this.error302 = error302;
    }

    public String getError303() {
        return error303;
    }

    public void setError303(String error303) {
        this.error303 = error303;
    }

    public String getError304() {
        return error304;
    }

    public void setError304(String error304) {
        this.error304 = error304;
    }

    public String getError400() {
        return error400;
    }

    public void setError400(String error400) {
        this.error400 = error400;
    }

    public String getError401() {
        return error401;
    }

    public void setError401(String error401) {
        this.error401 = error401;
    }

    public String getError402() {
        return error402;
    }

    public void setError402(String error402) {
        this.error402 = error402;
    }

    public String getError403() {
        return error403;
    }

    public void setError403(String error403) {
        this.error403 = error403;
    }

    public String getError405() {
        return error405;
    }

    public void setError405(String error405) {
        this.error405 = error405;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PaymentResponseJson [amount=");
        builder.append(amount);
        builder.append(", dateTime=");
        builder.append(dateTime);
        builder.append(", fees=");
        builder.append(fees);
        builder.append(", hashcode=");
        builder.append(hashcode);
        builder.append(", idFromClient=");
        builder.append(idFromClient);
        builder.append(", idFromGU=");
        builder.append(idFromGU);
        builder.append(", recipientNumber=");
        builder.append(recipientNumber);
        builder.append(", reference=");
        builder.append(reference);
        builder.append(", serviceCode=");
        builder.append(serviceCode);
        builder.append(", status=");
        builder.append(status);
        builder.append(", error=");
        builder.append(error);
        builder.append(", error300=");
        builder.append(error300);
        builder.append(", error301=");
        builder.append(error301);
        builder.append(", error302=");
        builder.append(error302);
        builder.append(", error303=");
        builder.append(error303);
        builder.append(", error304=");
        builder.append(error304);
        builder.append(", error400=");
        builder.append(error400);
        builder.append(", error401=");
        builder.append(error401);
        builder.append(", error402=");
        builder.append(error402);
        builder.append(", error403=");
        builder.append(error403);
        builder.append(", error405=");
        builder.append(error405);
        builder.append("]");
        return builder.toString();
    }

}
