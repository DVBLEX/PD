package com.pad.server.base.jsonentities.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OnlinePaymentParameterJson {

    private long    mnoId;
    private boolean isPrintReceipt;
    private boolean isActive;
    private String  mnoName;

    public long getMnoId() {
        return mnoId;
    }

    public void setMnoId(long mnoId) {
        this.mnoId = mnoId;
    }

    public boolean getIsPrintReceipt() {
        return isPrintReceipt;
    }

    public void setIsPrintReceipt(boolean isPrintReceipt) {
        this.isPrintReceipt = isPrintReceipt;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getMnoName() {
        return mnoName;
    }

    public void setMnoName(String mnoName) {
        this.mnoName = mnoName;
    }

    @Override
    public String toString() {
        return "OnlinePaymentParameterJson [mnoId=" + mnoId + ", isPrintReceipt=" + isPrintReceipt + ", isActive=" + isActive + ", mnoName=" + mnoName + "]";
    }

}
