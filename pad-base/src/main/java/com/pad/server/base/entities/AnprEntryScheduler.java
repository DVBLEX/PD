package com.pad.server.base.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "anpr_entry_scheduler")
public class AnprEntryScheduler implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private long              id;

    @Column(name = "is_processed")
    private int               isProcessed;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    @Column(name = "date_scheduled")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateScheduled;

    @Column(name = "date_processed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateProcessed;

    @Column(name = "retry_count")
    private int               retryCount;

    @Column(name = "response_code")
    private int               responseCode;

    @Column(name = "response_text")
    private String            responseText;

    @Column(name = "trip_id")
    private Long              tripId;

    @Column(name = "mission_id")
    private Long              missionId;

    @Column(name = "entry_log_id")
    private Long              entryLogId;

    @Column(name = "lane_id", nullable = true)
    private Long              laneId;

    @Column(name = "plate_number")
    private String            plateNumber;

    @Column(name = "recognized_plate_number")
    private String            recognizedPlateNumber;

    @Column(name = "parking_permission_id", nullable = true)
    private Long              parkingPermissionId;

    @Column(name = "notes")
    private String            notes;

    @Column(name = "timestamp")
    private String            timestamp;

    @Column(name = "entry_event_type_id")
    private Long              entryEventTypeId;

    @Column(name = "entering_direction_id")
    private boolean           enteringDirectionId;

    @Column(name = "tenant_card_id", nullable = true)
    private Long              tenantCardId;

    @Column(name = "car_id", nullable = true)
    private Long              carId;

    @Column(name = "tenant_person_id", nullable = true)
    private Long              tenantPersonId;

    @Column(name = "tenant_company_id", nullable = true)
    private Long              tenantCompanyId;

    @Column(name = "image_path")
    private String            imagePath;

    @Column(name = "entering_event_id", nullable = true)
    private Long              enteringEventId;

    @Column(name = "blacklist_id", nullable = true)
    private Long              blacklistId;

    @Column(name = "user_id", nullable = true)
    private Long              userId;

    @Column(name = "zone_id_from")
    private Long              zoneIdFrom;

    @Column(name = "zone_from")
    private String            zoneFrom;

    @Column(name = "zone_id_to")
    private Long              zoneIdTo;

    @Column(name = "zone_to")
    private String            zoneTo;

    @Column(name = "applicants")
    private String            applicants;

    @Column(name = "user_name")
    private String            userName;

    @Column(name = "lane_name")
    private String            laneName;

    @Column(name = "company_name")
    private String            companyName;

    @Column(name = "person_name")
    private String            personName;

    @Column(name = "car_plate_number")
    private String            carPlateNumber;

    @Column(name = "card_number")
    private String            cardNumber;

    @Column(name = "entering_event_name")
    private String            enteringEventName;

    @Column(name = "entering_direction_name")
    private String            enteringDirectionName;

    @Column(name = "area_code_name")
    private String            areaCodeName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(int isProcessed) {
        this.isProcessed = isProcessed;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateScheduled() {
        return dateScheduled;
    }

    public void setDateScheduled(Date dateScheduled) {
        this.dateScheduled = dateScheduled;
    }

    public Date getDateProcessed() {
        return dateProcessed;
    }

    public void setDateProcessed(Date dateProcessed) {
        this.dateProcessed = dateProcessed;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

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
        builder.append("AnprEntryScheduler [id=");
        builder.append(id);
        builder.append(", isProcessed=");
        builder.append(isProcessed);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateScheduled=");
        builder.append(dateScheduled);
        builder.append(", dateProcessed=");
        builder.append(dateProcessed);
        builder.append(", retryCount=");
        builder.append(retryCount);
        builder.append(", responseCode=");
        builder.append(responseCode);
        builder.append(", responseText=");
        builder.append(responseText);
        builder.append(", tripId=");
        builder.append(tripId);
        builder.append(", missionId=");
        builder.append(missionId);
        builder.append(", entryLogId=");
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
