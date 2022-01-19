package com.pad.server.base.jsonentities.api.counter;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerUtil;

/**
 * @author rafael
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleCounterJson {

    private String  deviceId;
    private String  deviceName;
    private int     laneNumber;
    private long    sessionId;
    private String  type;
    private Date    dateCount;
    private Date    dateCreated;

    // used on front-end
    private String  sessionIdString;
    private String  operatorName;
    private String  operatorUsername;
    private String  dateCountStartString;
    private String  dateCountEndString;
    private Date    dateCountStart;
    private Date    dateCountEnd;
    private boolean isShowDefaultDates;

    // used for pagination
    private int     currentPage;
    private int     pageCount;
    private String  sortColumn;
    private boolean sortAsc;

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

    public int getLaneNumber() {
        return laneNumber;
    }

    public void setLaneNumber(int laneNumber) {
        this.laneNumber = laneNumber;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateCount() {
        return dateCount;
    }

    public void setDateCount(Date dateCount) {
        this.dateCount = dateCount;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getSessionIdString() {
        return sessionIdString;
    }

    public void setSessionIdString(String sessionIdString) {
        this.sessionIdString = sessionIdString;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorUsername() {
        return operatorUsername;
    }

    public void setOperatorUsername(String operatorUsername) {
        this.operatorUsername = operatorUsername;
    }

    public String getSessionDisplay() {

        if (StringUtils.isNotBlank(this.operatorName))
            return this.operatorName + " - " + this.operatorUsername;
        else
            return ServerConstants.DEFAULT_STRING;
    }

    public void setSessionDisplay(String sessionDisplay) {
    }

    public String getDateCountStartString() {
        return dateCountStartString;
    }

    public void setDateCountStartString(String dateCountStartString) {
        this.dateCountStartString = dateCountStartString;
    }

    public String getDateCountEndString() {
        return dateCountEndString;
    }

    public void setDateCountEndString(String dateCountEndString) {
        this.dateCountEndString = dateCountEndString;
    }

    public boolean getIsShowDefaultDates() {
        return isShowDefaultDates;
    }

    public void setIsShowDefaultDates(boolean isShowDefaultDates) {
        this.isShowDefaultDates = isShowDefaultDates;
    }

    public Date getDateCountStart() {
        return dateCountStart;
    }

    public void setDateCountStart(Date dateCountStart) {
        this.dateCountStart = dateCountStart;
    }

    public Date getDateCountEnd() {
        return dateCountEnd;
    }

    public void setDateCountEnd(Date dateCountEnd) {
        this.dateCountEnd = dateCountEnd;
    }

    public String getDateCountString() {
        try {
            return ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmmss, this.dateCount);
        } catch (Exception e) {
            return ServerConstants.DEFAULT_STRING;
        }
    }

    public void setDateCountString(String dateCountString) {
    }

    public String getDateCreatedString() {
        try {
            return ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmmss, this.dateCreated);
        } catch (Exception e) {
            return ServerConstants.DEFAULT_STRING;
        }
    }

    public void setDateCreatedString(String dateCreatedString) {
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
        builder.append("VehicleCounterJson [deviceId=");
        builder.append(deviceId);
        builder.append(", deviceName=");
        builder.append(deviceName);
        builder.append(", laneNumber=");
        builder.append(laneNumber);
        builder.append(", sessionId=");
        builder.append(sessionId);
        builder.append(", type=");
        builder.append(type);
        builder.append(", dateCount=");
        builder.append(dateCount);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateCountStart=");
        builder.append(dateCountStart);
        builder.append(", dateCountEnd=");
        builder.append(dateCountEnd);
        builder.append(", isShowDefaultDates=");
        builder.append(isShowDefaultDates);
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
