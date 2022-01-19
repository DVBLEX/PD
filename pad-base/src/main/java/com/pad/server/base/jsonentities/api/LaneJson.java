package com.pad.server.base.jsonentities.api;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LaneJson {

    private long    id;
    private long    laneId;
    private int     laneNumber;
    private long    zoneId;
    private String  deviceId;
    private String  deviceName;
    private String  allowedHosts;
    private boolean isActive;
    private String  printerIp;

    private Date    dateLastRequest;
    private String  dateLastRequestString;

    // used for pagination
    private int     currentPage;
    private int     pageCount;
    private String  sortColumn;
    private boolean sortAsc;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLaneId() {
        return laneId;
    }

    public void setLaneId(long laneId) {
        this.laneId = laneId;
    }

    public int getLaneNumber() {
        return laneNumber;
    }

    public void setLaneNumber(int laneNumber) {
        this.laneNumber = laneNumber;
    }

    public long getZoneId() {
        return zoneId;
    }

    public void setZoneId(long zoneId) {
        this.zoneId = zoneId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAllowedHosts() {
        return allowedHosts;
    }

    public void setAllowedHosts(String allowedHosts) {
        this.allowedHosts = allowedHosts;
    }

    public Date getDateLastRequest() {
        return dateLastRequest;
    }

    public void setDateLastRequest(Date dateLastRequest) {
        this.dateLastRequest = dateLastRequest;
    }

    public String getDateLastRequestString() {
        try {
            return ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmmss, this.dateLastRequest);
        } catch (Exception e) {
            return ServerConstants.DEFAULT_STRING;
        }
    }

    public void setDateLastRequestString(String dateLastRequestString) {
        this.dateLastRequestString = dateLastRequestString;
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

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getPrinterIp() {
        return printerIp;
    }

    public void setPrinterIp(String printerIp) {
        this.printerIp = printerIp;
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
        builder.append("LaneJson [id=");
        builder.append(id);
        builder.append(", laneId=");
        builder.append(laneId);
        builder.append(", laneNumber=");
        builder.append(laneNumber);
        builder.append(", zoneId=");
        builder.append(zoneId);
        builder.append(", deviceId=");
        builder.append(deviceId);
        builder.append(", deviceName=");
        builder.append(deviceName);
        builder.append(", allowedHosts=");
        builder.append(allowedHosts);
        builder.append(", isActive=");
        builder.append(isActive);
        builder.append(", printerIp=");
        builder.append(printerIp);
        builder.append(", dateLastRequest=");
        builder.append(dateLastRequest);
        builder.append(", dateLastRequestString=");
        builder.append(dateLastRequestString);
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
