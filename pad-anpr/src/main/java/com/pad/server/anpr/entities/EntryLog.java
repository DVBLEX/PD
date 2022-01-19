package com.pad.server.anpr.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EntryLog {

    @JsonProperty("EntryLogId")
    private Long    entryLogId;

    @JsonProperty("LaneId")
    private Long    laneId;

    @JsonProperty("PlateNumber")
    private String  plateNumber;

    @JsonProperty("RecognizedPlateNumber")
    private String  recognizedPlateNumber;

    @JsonProperty("ParkingPermissionId")
    private Long    parkingPermissionId;

    @JsonProperty("Notes")
    private String  notes;

    @JsonProperty("Timestamp")
    private String  timestamp;

    @JsonProperty("EntryEventTypeId")
    private Long    entryEventTypeId;

    @JsonProperty("EnteringDirectionId")
    private boolean enteringDirectionId;

    @JsonProperty("TenantCardId")
    private Long    tenantCardId;

    @JsonProperty("CarId")
    private Long    carId;

    @JsonProperty("TenantPersonId")
    private Long    tenantPersonId;

    @JsonProperty("TenantCompanyId")
    private Long    tenantCompanyId;

    @JsonProperty("ImagePath")
    private String  imagePath;

    @JsonProperty("EnteringEventId")
    private Long    enteringEventId;

    @JsonProperty("BlacklistId")
    private Long    blacklistId;

    @JsonProperty("UserId")
    private Long    userId;

    @JsonProperty("ZoneIdFrom")
    private Long    zoneIdFrom;

    @JsonProperty("ZoneFrom")
    private String  zoneFrom;

    @JsonProperty("ZoneIdTo")
    private Long    zoneIdTo;

    @JsonProperty("ZoneTo")
    private String  zoneTo;

    @JsonProperty("Applicants")
    private String  applicants;

    @JsonProperty("UserName")
    private String  userName;

    @JsonProperty("LaneName")
    private String  laneName;

    @JsonProperty("CompanyName")
    private String  companyName;

    @JsonProperty("PersonName")
    private String  personName;

    @JsonProperty("CarPlateNumber")
    private String  carPlateNumber;

    @JsonProperty("CardNumber")
    private String  cardNumber;

    @JsonProperty("EnteringEventName")
    private String  enteringEventName;

    @JsonProperty("EnteringDirectionName")
    private String  enteringDirectionName;

    @JsonProperty("AreaCodeName")
    private String  areaCodeName;

    public Long getEntryLogId() {
        return entryLogId;
    }

    public void setEntryLogId(Long entryLogId) {
        this.entryLogId = entryLogId;
    }

    public Long getLaneId() {
        return laneId;
    }

    public void setLaneId(Long laneId) {
        this.laneId = laneId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getRecognizedPlateNumber() {
        return recognizedPlateNumber;
    }

    public void setRecognizedPlateNumber(String recognizedPlateNumber) {
        this.recognizedPlateNumber = recognizedPlateNumber;
    }

    public Long getParkingPermissionId() {
        return parkingPermissionId;
    }

    public void setParkingPermissionId(Long parkingPermissionId) {
        this.parkingPermissionId = parkingPermissionId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Long getEntryEventTypeId() {
        return entryEventTypeId;
    }

    public void setEntryEventTypeId(Long entryEventTypeId) {
        this.entryEventTypeId = entryEventTypeId;
    }

    public boolean getEnteringDirectionId() {
        return enteringDirectionId;
    }

    public void setEnteringDirectionId(boolean enteringDirectionId) {
        this.enteringDirectionId = enteringDirectionId;
    }

    public Long getTenantCardId() {
        return tenantCardId;
    }

    public void setTenantCardId(Long tenantCardId) {
        this.tenantCardId = tenantCardId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public Long getTenantPersonId() {
        return tenantPersonId;
    }

    public void setTenantPersonId(Long tenantPersonId) {
        this.tenantPersonId = tenantPersonId;
    }

    public Long getTenantCompanyId() {
        return tenantCompanyId;
    }

    public void setTenantCompanyId(Long tenantCompanyId) {
        this.tenantCompanyId = tenantCompanyId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Long getEnteringEventId() {
        return enteringEventId;
    }

    public void setEnteringEventId(Long enteringEventId) {
        this.enteringEventId = enteringEventId;
    }

    public Long getBlacklistId() {
        return blacklistId;
    }

    public void setBlacklistId(Long blacklistId) {
        this.blacklistId = blacklistId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getZoneIdFrom() {
        return zoneIdFrom;
    }

    public void setZoneIdFrom(Long zoneIdFrom) {
        this.zoneIdFrom = zoneIdFrom;
    }

    public String getZoneFrom() {
        return zoneFrom;
    }

    public void setZoneFrom(String zoneFrom) {
        this.zoneFrom = zoneFrom;
    }

    public Long getZoneIdTo() {
        return zoneIdTo;
    }

    public void setZoneIdTo(Long zoneIdTo) {
        this.zoneIdTo = zoneIdTo;
    }

    public String getZoneTo() {
        return zoneTo;
    }

    public void setZoneTo(String zoneTo) {
        this.zoneTo = zoneTo;
    }

    public String getApplicants() {
        return applicants;
    }

    public void setApplicants(String applicants) {
        this.applicants = applicants;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLaneName() {
        return laneName;
    }

    public void setLaneName(String laneName) {
        this.laneName = laneName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getCarPlateNumber() {
        return carPlateNumber;
    }

    public void setCarPlateNumber(String carPlateNumber) {
        this.carPlateNumber = carPlateNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getEnteringEventName() {
        return enteringEventName;
    }

    public void setEnteringEventName(String enteringEventName) {
        this.enteringEventName = enteringEventName;
    }

    public String getEnteringDirectionName() {
        return enteringDirectionName;
    }

    public void setEnteringDirectionName(String enteringDirectionName) {
        this.enteringDirectionName = enteringDirectionName;
    }

    public String getAreaCodeName() {
        return areaCodeName;
    }

    public void setAreaCodeName(String areaCodeName) {
        this.areaCodeName = areaCodeName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EntryLog [entryLogId=");
        builder.append(entryLogId);
        builder.append(", laneId=");
        builder.append(laneId);
        builder.append(", plateNumber=");
        builder.append(plateNumber);
        builder.append(", recognizedPlateNumber=");
        builder.append(recognizedPlateNumber);
        builder.append(", parkingPermissionId=");
        builder.append(parkingPermissionId);
        builder.append(", notes=");
        builder.append(notes);
        builder.append(", timestamp=");
        builder.append(timestamp);
        builder.append(", entryEventTypeId=");
        builder.append(entryEventTypeId);
        builder.append(", enteringDirectionId=");
        builder.append(enteringDirectionId);
        builder.append(", tenantCardId=");
        builder.append(tenantCardId);
        builder.append(", carId=");
        builder.append(carId);
        builder.append(", tenantPersonId=");
        builder.append(tenantPersonId);
        builder.append(", tenantCompanyId=");
        builder.append(tenantCompanyId);
        builder.append(", imagePath=");
        builder.append(imagePath);
        builder.append(", enteringEventId=");
        builder.append(enteringEventId);
        builder.append(", blacklistId=");
        builder.append(blacklistId);
        builder.append(", userId=");
        builder.append(userId);
        builder.append(", zoneIdFrom=");
        builder.append(zoneIdFrom);
        builder.append(", zoneFrom=");
        builder.append(zoneFrom);
        builder.append(", zoneIdTo=");
        builder.append(zoneIdTo);
        builder.append(", zoneTo=");
        builder.append(zoneTo);
        builder.append(", applicants=");
        builder.append(applicants);
        builder.append(", userName=");
        builder.append(userName);
        builder.append(", laneName=");
        builder.append(laneName);
        builder.append(", companyName=");
        builder.append(companyName);
        builder.append(", personName=");
        builder.append(personName);
        builder.append(", carPlateNumber=");
        builder.append(carPlateNumber);
        builder.append(", cardNumber=");
        builder.append(cardNumber);
        builder.append(", enteringEventName=");
        builder.append(enteringEventName);
        builder.append(", enteringDirectionName=");
        builder.append(enteringDirectionName);
        builder.append(", areaCodeName=");
        builder.append(areaCodeName);
        builder.append("]");
        return builder.toString();
    }

}
