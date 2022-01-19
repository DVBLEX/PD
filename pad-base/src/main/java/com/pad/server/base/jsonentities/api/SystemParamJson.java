package com.pad.server.base.jsonentities.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemParamJson {

    private Boolean isBookingLimitCheckEnabled;
    private Boolean isPortEntryFiltering;
    private Integer autoReleaseExitCapacityPercentage;
    private String  dropOffEmptyNightMissionStartTime;
    private String  dropOffEmptyNightMissionEndTime;
    private String  dropOffEmptyTriangleMissionStartTime;
    private String  dropOffEmptyTriangleMissionEndTime;

    private String  apiClientId;
    private String  apiClientSecret;

    public SystemParamJson() {
    }

    public Boolean getIsBookingLimitCheckEnabled() {
        return isBookingLimitCheckEnabled;
    }

    public void setIsBookingLimitCheckEnabled(Boolean isBookingLimitCheckEnabled) {
        this.isBookingLimitCheckEnabled = isBookingLimitCheckEnabled;
    }

    public Boolean getIsPortEntryFiltering() {
        return isPortEntryFiltering;
    }

    public void setIsPortEntryFiltering(Boolean portEntryFiltering) {
        isPortEntryFiltering = portEntryFiltering;
    }

    public Integer getAutoReleaseExitCapacityPercentage() {
        return autoReleaseExitCapacityPercentage;
    }

    public void setAutoReleaseExitCapacityPercentage(Integer autoReleaseExitCapacityPercentage) {
        this.autoReleaseExitCapacityPercentage = autoReleaseExitCapacityPercentage;
    }

    public String getDropOffEmptyNightMissionStartTime() {
        return dropOffEmptyNightMissionStartTime;
    }

    public void setDropOffEmptyNightMissionStartTime(String dropOffEmptyNightMissionStartTime) {
        this.dropOffEmptyNightMissionStartTime = dropOffEmptyNightMissionStartTime;
    }

    public String getDropOffEmptyNightMissionEndTime() {
        return dropOffEmptyNightMissionEndTime;
    }

    public void setDropOffEmptyNightMissionEndTime(String dropOffEmptyNightMissionEndTime) {
        this.dropOffEmptyNightMissionEndTime = dropOffEmptyNightMissionEndTime;
    }

    public String getDropOffEmptyTriangleMissionStartTime() {
        return dropOffEmptyTriangleMissionStartTime;
    }

    public void setDropOffEmptyTriangleMissionStartTime(String dropOffEmptyTriangleMissionStartTime) {
        this.dropOffEmptyTriangleMissionStartTime = dropOffEmptyTriangleMissionStartTime;
    }

    public String getDropOffEmptyTriangleMissionEndTime() {
        return dropOffEmptyTriangleMissionEndTime;
    }

    public void setDropOffEmptyTriangleMissionEndTime(String dropOffEmptyTriangleMissionEndTime) {
        this.dropOffEmptyTriangleMissionEndTime = dropOffEmptyTriangleMissionEndTime;
    }

    public String getApiClientId() {
        return apiClientId;
    }

    public void setApiClientId(String apiClientId) {
        this.apiClientId = apiClientId;
    }

    public String getApiClientSecret() {
        return apiClientSecret;
    }

    public void setApiClientSecret(String apiClientSecret) {
        this.apiClientSecret = apiClientSecret;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SystemParamJson [isBookingLimitCheckEnabled=");
        builder.append(isBookingLimitCheckEnabled);
        builder.append("SystemParamJson [isPortEntryFiltering=");
        builder.append(isPortEntryFiltering);
        builder.append(", autoReleaseExitCapacityPercentage=");
        builder.append(autoReleaseExitCapacityPercentage);
        builder.append(", dropOffEmptyNightMissionStartTime=");
        builder.append(dropOffEmptyNightMissionStartTime);
        builder.append(", dropOffEmptyNightMissionEndTime=");
        builder.append(dropOffEmptyNightMissionEndTime);
        builder.append(", dropOffEmptyTriangleMissionStartTime=");
        builder.append(dropOffEmptyTriangleMissionStartTime);
        builder.append(", dropOffEmptyTriangleMissionEndTime=");
        builder.append(dropOffEmptyTriangleMissionEndTime);
        builder.append("]");
        return builder.toString();
    }

}
