package com.pad.server.base.jsonentities.api;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PortAccessWhitelistJson {

    private long     id;
    private String   code;
    private Integer  portOperatorId;
    private Long     gateId;
    private String   dateFromString;
    private String   timeFromString;
    private String   dateToString;
    private String   timeToString;
    private String   vehicleRegistration;
    private Long     parkingPermissionId;
    private String[] vehicleRegistrationArray;

    // used for pagination
    private int      currentPage;
    private int      pageCount;
    private String   sortColumn;
    private boolean  sortAsc;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPortOperatorId() {
        return portOperatorId;
    }

    public void setPortOperatorId(Integer portOperatorId) {
        this.portOperatorId = portOperatorId;
    }

    public Long getGateId() {
        return gateId;
    }

    public void setGateId(Long gateId) {
        this.gateId = gateId;
    }

    public String getDateFromString() {
        return dateFromString;
    }

    public void setDateFromString(String dateFromString) {
        this.dateFromString = dateFromString;
    }

    public String getTimeFromString() {
        return timeFromString;
    }

    public void setTimeFromString(String timeFromString) {
        this.timeFromString = timeFromString;
    }

    public String getDateToString() {
        return dateToString;
    }

    public void setDateToString(String dateToString) {
        this.dateToString = dateToString;
    }

    public String getTimeToString() {
        return timeToString;
    }

    public void setTimeToString(String timeToString) {
        this.timeToString = timeToString;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public Long getParkingPermissionId() {
        return parkingPermissionId;
    }

    public void setParkingPermissionId(Long parkingPermissionId) {
        this.parkingPermissionId = parkingPermissionId;
    }

    public String[] getVehicleRegistrationArray() {
        return vehicleRegistrationArray;
    }

    public void setVehicleRegistrationArray(String[] vehicleRegistrationArray) {
        this.vehicleRegistrationArray = vehicleRegistrationArray;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public boolean getSortAsc() {
        return sortAsc;
    }

    public void setSortAsc(boolean sortAsc) {
        this.sortAsc = sortAsc;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PortAccessWhitelistJson [code=");
        builder.append(code);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", gateId=");
        builder.append(gateId);
        builder.append(", gateId=");
        builder.append(gateId);
        builder.append(", dateFromString=");
        builder.append(dateFromString);
        builder.append(", timeFromString=");
        builder.append(timeFromString);
        builder.append(", dateToString=");
        builder.append(dateToString);
        builder.append(", timeToString=");
        builder.append(timeToString);
        builder.append(", vehicleRegistration=");
        builder.append(vehicleRegistration);
        builder.append(", parkingPermissionId=");
        builder.append(parkingPermissionId);
        builder.append(", vehicleRegistrationArray=");
        builder.append(Arrays.toString(vehicleRegistrationArray));
        builder.append(", currentPage=");
        builder.append(currentPage);
        builder.append(", pageCount=");
        builder.append(pageCount);
        builder.append(", sortColumn=");
        builder.append(sortColumn);
        builder.append(", sortAsc=");
        builder.append(sortAsc);
        builder.append("]");
        return builder.toString();
    }

}
