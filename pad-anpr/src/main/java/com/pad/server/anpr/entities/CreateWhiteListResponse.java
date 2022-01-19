package com.pad.server.anpr.entities;

public class CreateWhiteListResponse {

    private boolean success;
    private String  message;
    private long    parkingPermissionId;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getParkingPermissionId() {
        return parkingPermissionId;
    }

    public void setParkingPermissionId(long parkingPermissionId) {
        this.parkingPermissionId = parkingPermissionId;
    }

}
