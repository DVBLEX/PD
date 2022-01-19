package com.pad.server.base.jsonentities.api;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnprParameterJson {

    private Long    lastEntryLogId;
    private Integer entryLogPageSize;
    private Integer entryLogConnectTimeout;
    private Integer entryLogSocketTimeout;
    private Integer entryLogConnRequestTimeout;
    private Integer defaultConnectTimeout;
    private Integer defaultSocketTimeout;
    private Integer defaultConnRequestTimeout;
    private Integer parkingPermissionHoursInFuture;
    private Integer parkingPermissionHoursPriorSlotDate;
    private Integer parkingPermissionHoursAfterSlotDate;
    private Integer parkingPermissionHoursAfterExitDate;
    private Integer btDowntimeSecondsLimit;
    private Integer btUptimeSecondsLimit;
    private Boolean isIISServerEnabled;
    private Date    dateEdited;
    private String  agsparkingEntryLane1VideoFeedUrl;
    private String  agsparkingEntryLane2VideoFeedUrl;
    private String  agsparkingEntryLane3VideoFeedUrl;
    private String  agsparkingEntryLane4VideoFeedUrl;
    private String  agsparkingEntryLane5VideoFeedUrl;

    private String  apiCientId;
    private String  apiSecret;

    public Long getLastEntryLogId() {
        return lastEntryLogId;
    }

    public void setLastEntryLogId(Long lastEntryLogId) {
        this.lastEntryLogId = lastEntryLogId;
    }

    public Integer getEntryLogPageSize() {
        return entryLogPageSize;
    }

    public void setEntryLogPageSize(Integer entryLogPageSize) {
        this.entryLogPageSize = entryLogPageSize;
    }

    public Integer getEntryLogConnectTimeout() {
        return entryLogConnectTimeout;
    }

    public void setEntryLogConnectTimeout(Integer entryLogConnectTimeout) {
        this.entryLogConnectTimeout = entryLogConnectTimeout;
    }

    public Integer getEntryLogSocketTimeout() {
        return entryLogSocketTimeout;
    }

    public void setEntryLogSocketTimeout(Integer entryLogSocketTimeout) {
        this.entryLogSocketTimeout = entryLogSocketTimeout;
    }

    public Integer getEntryLogConnRequestTimeout() {
        return entryLogConnRequestTimeout;
    }

    public void setEntryLogConnRequestTimeout(Integer entryLogConnRequestTimeout) {
        this.entryLogConnRequestTimeout = entryLogConnRequestTimeout;
    }

    public Integer getDefaultConnectTimeout() {
        return defaultConnectTimeout;
    }

    public void setDefaultConnectTimeout(Integer defaultConnectTimeout) {
        this.defaultConnectTimeout = defaultConnectTimeout;
    }

    public Integer getDefaultSocketTimeout() {
        return defaultSocketTimeout;
    }

    public void setDefaultSocketTimeout(Integer defaultSocketTimeout) {
        this.defaultSocketTimeout = defaultSocketTimeout;
    }

    public Integer getDefaultConnRequestTimeout() {
        return defaultConnRequestTimeout;
    }

    public void setDefaultConnRequestTimeout(Integer defaultConnRequestTimeout) {
        this.defaultConnRequestTimeout = defaultConnRequestTimeout;
    }

    public Integer getParkingPermissionHoursInFuture() {
        return parkingPermissionHoursInFuture;
    }

    public void setParkingPermissionHoursInFuture(Integer parkingPermissionHoursInFuture) {
        this.parkingPermissionHoursInFuture = parkingPermissionHoursInFuture;
    }

    public Integer getParkingPermissionHoursPriorSlotDate() {
        return parkingPermissionHoursPriorSlotDate;
    }

    public void setParkingPermissionHoursPriorSlotDate(Integer parkingPermissionHoursPriorSlotDate) {
        this.parkingPermissionHoursPriorSlotDate = parkingPermissionHoursPriorSlotDate;
    }

    public Integer getParkingPermissionHoursAfterSlotDate() {
        return parkingPermissionHoursAfterSlotDate;
    }

    public void setParkingPermissionHoursAfterSlotDate(Integer parkingPermissionHoursAfterSlotDate) {
        this.parkingPermissionHoursAfterSlotDate = parkingPermissionHoursAfterSlotDate;
    }

    public Integer getParkingPermissionHoursAfterExitDate() {
        return parkingPermissionHoursAfterExitDate;
    }

    public void setParkingPermissionHoursAfterExitDate(Integer parkingPermissionHoursAfterExitDate) {
        this.parkingPermissionHoursAfterExitDate = parkingPermissionHoursAfterExitDate;
    }

    public Integer getBtDowntimeSecondsLimit() {
        return btDowntimeSecondsLimit;
    }

    public void setBtDowntimeSecondsLimit(Integer btDowntimeSecondsLimit) {
        this.btDowntimeSecondsLimit = btDowntimeSecondsLimit;
    }

    public Integer getBtUptimeSecondsLimit() {
        return btUptimeSecondsLimit;
    }

    public void setBtUptimeSecondsLimit(Integer btUptimeSecondsLimit) {
        this.btUptimeSecondsLimit = btUptimeSecondsLimit;
    }

    public Boolean getIsIISServerEnabled() {
        return isIISServerEnabled;
    }

    public void setIsIISServerEnabled(Boolean isIISServerEnabled) {
        this.isIISServerEnabled = isIISServerEnabled;
    }

    public String getAgsparkingEntryLane1VideoFeedUrl() {
        return agsparkingEntryLane1VideoFeedUrl;
    }

    public void setAgsparkingEntryLane1VideoFeedUrl(String agsparkingEntryLane1VideoFeedUrl) {
        this.agsparkingEntryLane1VideoFeedUrl = agsparkingEntryLane1VideoFeedUrl;
    }

    public String getAgsparkingEntryLane2VideoFeedUrl() {
        return agsparkingEntryLane2VideoFeedUrl;
    }

    public void setAgsparkingEntryLane2VideoFeedUrl(String agsparkingEntryLane2VideoFeedUrl) {
        this.agsparkingEntryLane2VideoFeedUrl = agsparkingEntryLane2VideoFeedUrl;
    }

    public String getAgsparkingEntryLane3VideoFeedUrl() {
        return agsparkingEntryLane3VideoFeedUrl;
    }

    public void setAgsparkingEntryLane3VideoFeedUrl(String agsparkingEntryLane3VideoFeedUrl) {
        this.agsparkingEntryLane3VideoFeedUrl = agsparkingEntryLane3VideoFeedUrl;
    }

    public String getAgsparkingEntryLane4VideoFeedUrl() {
        return agsparkingEntryLane4VideoFeedUrl;
    }

    public void setAgsparkingEntryLane4VideoFeedUrl(String agsparkingEntryLane4VideoFeedUrl) {
        this.agsparkingEntryLane4VideoFeedUrl = agsparkingEntryLane4VideoFeedUrl;
    }

    public String getAgsparkingEntryLane5VideoFeedUrl() {
        return agsparkingEntryLane5VideoFeedUrl;
    }

    public void setAgsparkingEntryLane5VideoFeedUrl(String agsparkingEntryLane5VideoFeedUrl) {
        this.agsparkingEntryLane5VideoFeedUrl = agsparkingEntryLane5VideoFeedUrl;
    }

    public Date getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(Date dateEdited) {
        this.dateEdited = dateEdited;
    }

    public String getApiCientId() {
        return apiCientId;
    }

    public void setApiCientId(String apiCientId) {
        this.apiCientId = apiCientId;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AnprParameterJson [lastEntryLogId=");
        builder.append(lastEntryLogId);
        builder.append(", entryLogPageSize=");
        builder.append(entryLogPageSize);
        builder.append(", entryLogConnectTimeout=");
        builder.append(entryLogConnectTimeout);
        builder.append(", entryLogSocketTimeout=");
        builder.append(entryLogSocketTimeout);
        builder.append(", entryLogConnRequestTimeout=");
        builder.append(entryLogConnRequestTimeout);
        builder.append(", defaultConnectTimeout=");
        builder.append(defaultConnectTimeout);
        builder.append(", defaultSocketTimeout=");
        builder.append(defaultSocketTimeout);
        builder.append(", defaultConnRequestTimeout=");
        builder.append(defaultConnRequestTimeout);
        builder.append(", parkingPermissionHoursInFuture=");
        builder.append(parkingPermissionHoursInFuture);
        builder.append(", parkingPermissionHoursPriorSlotDate=");
        builder.append(parkingPermissionHoursPriorSlotDate);
        builder.append(", parkingPermissionHoursAfterSlotDate=");
        builder.append(parkingPermissionHoursAfterSlotDate);
        builder.append(", parkingPermissionHoursAfterExitDate=");
        builder.append(parkingPermissionHoursAfterExitDate);
        builder.append(", btDowntimeSecondsLimit=");
        builder.append(btDowntimeSecondsLimit);
        builder.append(", btUptimeSecondsLimit=");
        builder.append(btUptimeSecondsLimit);
        builder.append(", isIISServerEnabled=");
        builder.append(isIISServerEnabled);
        builder.append(", dateEdited=");
        builder.append(dateEdited);
        builder.append(", agsparkingEntryLane1VideoFeedUrl=");
        builder.append(agsparkingEntryLane1VideoFeedUrl);
        builder.append(", agsparkingEntryLane2VideoFeedUrl=");
        builder.append(agsparkingEntryLane2VideoFeedUrl);
        builder.append(", agsparkingEntryLane3VideoFeedUrl=");
        builder.append(agsparkingEntryLane3VideoFeedUrl);
        builder.append(", agsparkingEntryLane4VideoFeedUrl=");
        builder.append(agsparkingEntryLane4VideoFeedUrl);
        builder.append(", agsparkingEntryLane5VideoFeedUrl=");
        builder.append(agsparkingEntryLane5VideoFeedUrl);
        builder.append("]");
        return builder.toString();
    }

}
