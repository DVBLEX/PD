package com.pad.server.base.entities;

import java.util.Date;

public class SMSCodeRequest {

    private long   id;
    private String msisdn;
    private String code;
    private String token;
    private int    countCodeSent;
    private int    countVerified;
    private Date   dateCodeSent;
    private Date   dateVerified;
    private Date   dateCreated;

    public SMSCodeRequest() {
    }

    public SMSCodeRequest(long id, String msisdn, String code, String token, int countCodeSent, int countVerified, Date dateCodeSent, Date dateVerified) {
        this.id = id;
        this.msisdn = msisdn;
        this.code = code;
        this.token = token;
        this.countCodeSent = countCodeSent;
        this.countVerified = countVerified;
        this.dateCodeSent = dateCodeSent;
        this.dateVerified = dateVerified;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCountCodeSent() {
        return countCodeSent;
    }

    public void setCountCodeSent(int countCodeSent) {
        this.countCodeSent = countCodeSent;
    }

    public int getCountVerified() {
        return countVerified;
    }

    public void setCountVerified(int countVerified) {
        this.countVerified = countVerified;
    }

    public Date getDateCodeSent() {
        return dateCodeSent;
    }

    public void setDateCodeSent(Date dateCodeSent) {
        this.dateCodeSent = dateCodeSent;
    }

    public Date getDateVerified() {
        return dateVerified;
    }

    public void setDateVerified(Date dateVerified) {
        this.dateVerified = dateVerified;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SMSCodeRequest [id=");
        builder.append(id);
        builder.append(", msisdn=");
        builder.append(msisdn);
        builder.append(", code=");
        builder.append(code);
        builder.append(", token=");
        builder.append(token);
        builder.append(", countCodeSent=");
        builder.append(countCodeSent);
        builder.append(", countVerified=");
        builder.append(countVerified);
        builder.append(", dateCodeSent=");
        builder.append(dateCodeSent);
        builder.append(", dateVerified=");
        builder.append(dateVerified);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append("]");
        return builder.toString();
    }

}
