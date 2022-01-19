package com.pad.server.base.jsonentities.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PortOperatorTransactionTypeJson {

    private int     transactionType;
    private String  translateKey;

    private Integer portOperatorId;
    private String  portOperatorName;
    private String  portOperatorNameShort;
    private Long    portOperatorGateId;
    private String  portOperatorGateName;
    private String  portOperatorGateNameShort;

    private Boolean isAllowedForParkingAndKioskOp;
    private Boolean isAllowedForVirtualKioskOp;
    private Boolean isAutoReleaseParking;
    private Boolean isDirectToPort;
    private Boolean isAllowMultipleEntries;
    private Boolean isTripCancelSystem;

    private Integer missionCancelSystemAfterMinutes;
    private Integer tripCancelSystemAfterMinutes;

    private String  apiClientId;
    private String  apiClientSecret;

    public PortOperatorTransactionTypeJson() {
    }

    public PortOperatorTransactionTypeJson(String portOperatorName, String portOperatorNameShort, Long portOperatorGateId, String portOperatorGateName,
        String portOperatorGateNameShort, int transactionType, String translateKey, Boolean isAllowedForParkingAndKioskOp, Boolean isAllowedForVirtualKioskOp,
        Boolean isDirectToPort, Boolean isAllowMultipleEntries, Boolean isTripCancelSystem, Integer missionCancelSystemAfterMinutes, Integer tripCancelSystemAfterMinutes) {
        this.portOperatorName = portOperatorName;
        this.portOperatorNameShort = portOperatorNameShort;
        this.portOperatorGateId = portOperatorGateId;
        this.portOperatorGateName = portOperatorGateName;
        this.portOperatorGateNameShort = portOperatorGateNameShort;
        this.transactionType = transactionType;
        this.translateKey = translateKey;
        this.isAllowedForParkingAndKioskOp = isAllowedForParkingAndKioskOp;
        this.isAllowedForVirtualKioskOp = isAllowedForVirtualKioskOp;
        this.isDirectToPort = isDirectToPort;
        this.isAllowMultipleEntries = isAllowMultipleEntries;
        this.isTripCancelSystem = isTripCancelSystem;
        this.missionCancelSystemAfterMinutes = missionCancelSystemAfterMinutes;
        this.tripCancelSystemAfterMinutes = tripCancelSystemAfterMinutes;
    }

    public PortOperatorTransactionTypeJson(int transactionType, int portOperatorId, Boolean isAllowedForParkingAndKioskOp, Boolean isAllowedForVirtualKioskOp,
        Boolean isAutoReleaseParking, Boolean isDirectToPort, Boolean isAllowMultipleEntries, Boolean isTripCancelSystem,
        Integer missionCancelSystemAfterMinutes, Integer tripCancelSystemAfterMinutes, Long portOperatorGateId) {
        this.transactionType = transactionType;
        this.portOperatorId = portOperatorId;
        this.isAllowedForParkingAndKioskOp = isAllowedForParkingAndKioskOp;
        this.isAllowedForVirtualKioskOp = isAllowedForVirtualKioskOp;
        this.isAutoReleaseParking = isAutoReleaseParking;
        this.isDirectToPort = isDirectToPort;
        this.isAllowMultipleEntries = isAllowMultipleEntries;
        this.isTripCancelSystem = isTripCancelSystem;
        this.missionCancelSystemAfterMinutes = missionCancelSystemAfterMinutes;
        this.tripCancelSystemAfterMinutes = tripCancelSystemAfterMinutes;
        this.portOperatorGateId = portOperatorGateId;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public String getTranslateKey() {
        return translateKey;
    }

    public void setTranslateKey(String translateKey) {
        this.translateKey = translateKey;
    }

    public Integer getPortOperatorId() {
        return portOperatorId;
    }

    public void setPortOperatorId(Integer portOperatorId) {
        this.portOperatorId = portOperatorId;
    }

    public String getPortOperatorName() {
        return portOperatorName;
    }

    public void setPortOperatorName(String portOperatorName) {
        this.portOperatorName = portOperatorName;
    }

    public String getPortOperatorNameShort() {
        return portOperatorNameShort;
    }

    public void setPortOperatorNameShort(String portOperatorNameShort) {
        this.portOperatorNameShort = portOperatorNameShort;
    }

    public Long getPortOperatorGateId() {
        return portOperatorGateId;
    }

    public void setPortOperatorGateId(Long portOperatorGateId) {
        this.portOperatorGateId = portOperatorGateId;
    }

    public String getPortOperatorGateName() {
        return portOperatorGateName;
    }

    public void setPortOperatorGateName(String portOperatorGateName) {
        this.portOperatorGateName = portOperatorGateName;
    }

    public String getPortOperatorGateNameShort() {
        return portOperatorGateNameShort;
    }

    public void setPortOperatorGateNameShort(String portOperatorGateNameShort) {
        this.portOperatorGateNameShort = portOperatorGateNameShort;
    }

    public Boolean getIsAllowedForParkingAndKioskOp() {
        return isAllowedForParkingAndKioskOp;
    }

    public void setIsAllowedForParkingAndKioskOp(Boolean isAllowedForParkingAndKioskOp) {
        this.isAllowedForParkingAndKioskOp = isAllowedForParkingAndKioskOp;
    }

    public Boolean getIsAllowedForVirtualKioskOp() {
        return isAllowedForVirtualKioskOp;
    }

    public void setIsAllowedForVirtualKioskOp(Boolean isAllowedForVirtualKioskOp) {
        this.isAllowedForVirtualKioskOp = isAllowedForVirtualKioskOp;
    }

    public Boolean getIsAutoReleaseParking() {
        return isAutoReleaseParking;
    }

    public void setIsAutoReleaseParking(Boolean isAutoReleaseParking) {
        this.isAutoReleaseParking = isAutoReleaseParking;
    }

    public Boolean getIsDirectToPort() {
        return isDirectToPort;
    }

    public void setIsDirectToPort(Boolean isDirectToPort) {
        this.isDirectToPort = isDirectToPort;
    }

    public Boolean getIsAllowMultipleEntries() {
        return isAllowMultipleEntries;
    }

    public void setIsAllowMultipleEntries(Boolean isAllowMultipleEntries) {
        this.isAllowMultipleEntries = isAllowMultipleEntries;
    }

    public Boolean getIsTripCancelSystem() {
        return isTripCancelSystem;
    }

    public void setIsTripCancelSystem(Boolean tripCancelSystem) {
        isTripCancelSystem = tripCancelSystem;
    }

    public Integer getMissionCancelSystemAfterMinutes() {
        return missionCancelSystemAfterMinutes;
    }

    public void setMissionCancelSystemAfterMinutes(Integer missionCancelSystemAfterMinutes) {
        this.missionCancelSystemAfterMinutes = missionCancelSystemAfterMinutes;
    }

    public Integer getTripCancelSystemAfterMinutes() {
        return tripCancelSystemAfterMinutes;
    }

    public void setTripCancelSystemAfterMinutes(Integer tripCancelSystemAfterMinutes) {
        this.tripCancelSystemAfterMinutes = tripCancelSystemAfterMinutes;
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
        builder.append("PortOperatorTransactionTypeJson [transactionType=");
        builder.append(transactionType);
        builder.append(", translateKey=");
        builder.append(translateKey);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", portOperatorName=");
        builder.append(portOperatorName);
        builder.append(", portOperatorNameShort=");
        builder.append(portOperatorNameShort);
        builder.append(", portOperatorGateId=");
        builder.append(portOperatorGateId);
        builder.append(", portOperatorGateName=");
        builder.append(portOperatorGateName);
        builder.append(", portOperatorGateNameShort=");
        builder.append(portOperatorGateNameShort);
        builder.append(", isAllowedForParkingAndKioskOp=");
        builder.append(isAllowedForParkingAndKioskOp);
        builder.append(", isAllowedForVirtualKioskOp=");
        builder.append(isAllowedForVirtualKioskOp);
        builder.append(", isAutoReleaseParking=");
        builder.append(isAutoReleaseParking);
        builder.append(", isDirectToPort=");
        builder.append(isDirectToPort);
        builder.append(", isAllowMultipleEntries=");
        builder.append(isAllowMultipleEntries);
        builder.append(", isTripCancelSystem=");
        builder.append(isTripCancelSystem);
        builder.append(", missionCancelSystemAfterMinutes=");
        builder.append(missionCancelSystemAfterMinutes);
        builder.append(", tripCancelSystemAfterMinutes=");
        builder.append(tripCancelSystemAfterMinutes);
        builder.append("]");
        return builder.toString();
    }

}
