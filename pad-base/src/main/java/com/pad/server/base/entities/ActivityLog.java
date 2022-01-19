package com.pad.server.base.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.pad.server.base.common.ServerConstants;

@Entity
@Table(name = "activity_log")
public class ActivityLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "operator_id")
    private long              operatorId;

    @Column(name = "activity_id")
    private long              activityId;

    @Column(name = "account_id")
    private long              accountId;

    @Column(name = "mission_id")
    private long              missionId;

    @Column(name = "trip_id")
    private long              tripId;

    @Column(name = "parking_id")
    private long              parkingId;

    @Column(name = "port_access_id")
    private long              portAccessId;

    @Column(name = "port_access_whitelist_id")
    private long              portAccessWhitelistId;

    @Column(name = "statement_id")
    private long              statementId;

    @Column(name = "session_id")
    private long              sessionId;

    @Column(name = "new_updated_operator_id")
    private long              newUpdatedOperatorId;

    @Column(name = "vehicle_registration")
    private String            vehicleRegistration;

    @Column(name = "port_operator_id")
    private long              portOperatorId;

    @Column(name = "transaction_type")
    private int               transactionType;

    @Column(name = "reference_number")
    private String            referenceNumber;

    @Column(name = "is_auto_release_parking")
    private boolean           isAutoReleaseParking;

    @Column(name = "vehicle_release_count")
    private int               vehicleReleaseCount;

    @Column(name = "object_json")
    private String            objectJson;

    @Column(name = "date_log")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateLog;

    @Transient
    private String            activityName;

    @Transient
    private String            operatorUsername;

    public ActivityLog() {
        super();
    }

    public ActivityLog(long activityId, long operatorId) {
        this.activityId = activityId;
        this.operatorId = operatorId;
        this.accountId = ServerConstants.DEFAULT_LONG;
        this.missionId = ServerConstants.DEFAULT_LONG;
        this.tripId = ServerConstants.DEFAULT_LONG;
        this.parkingId = ServerConstants.DEFAULT_LONG;
        this.portAccessId = ServerConstants.DEFAULT_LONG;
        this.portAccessWhitelistId = ServerConstants.DEFAULT_LONG;
        this.statementId = ServerConstants.DEFAULT_LONG;
        this.sessionId = ServerConstants.DEFAULT_LONG;
        this.newUpdatedOperatorId = ServerConstants.DEFAULT_LONG;
        this.vehicleRegistration = ServerConstants.DEFAULT_STRING;
        this.portOperatorId = ServerConstants.DEFAULT_LONG;
        this.transactionType = ServerConstants.DEFAULT_INT;
        this.referenceNumber = ServerConstants.DEFAULT_STRING;
        this.isAutoReleaseParking = false;
        this.vehicleReleaseCount = ServerConstants.DEFAULT_INT;
        this.objectJson = ServerConstants.DEFAULT_STRING;
        this.dateLog = new Date();
    }

    public ActivityLog(long activityId, long operatorId, String objectJson) {
        this.activityId = activityId;
        this.operatorId = operatorId;
        this.accountId = ServerConstants.DEFAULT_LONG;
        this.missionId = ServerConstants.DEFAULT_LONG;
        this.tripId = ServerConstants.DEFAULT_LONG;
        this.parkingId = ServerConstants.DEFAULT_LONG;
        this.portAccessId = ServerConstants.DEFAULT_LONG;
        this.portAccessWhitelistId = ServerConstants.DEFAULT_LONG;
        this.statementId = ServerConstants.DEFAULT_LONG;
        this.sessionId = ServerConstants.DEFAULT_LONG;
        this.newUpdatedOperatorId = ServerConstants.DEFAULT_LONG;
        this.vehicleRegistration = ServerConstants.DEFAULT_STRING;
        this.portOperatorId = ServerConstants.DEFAULT_LONG;
        this.transactionType = ServerConstants.DEFAULT_INT;
        this.referenceNumber = ServerConstants.DEFAULT_STRING;
        this.isAutoReleaseParking = false;
        this.vehicleReleaseCount = ServerConstants.DEFAULT_INT;
        this.objectJson = objectJson;
        this.dateLog = new Date();
    }

    public ActivityLog(long activityId, long operatorId, long accountId) {
        this.activityId = activityId;
        this.operatorId = operatorId;
        this.accountId = accountId;
        this.missionId = ServerConstants.DEFAULT_LONG;
        this.tripId = ServerConstants.DEFAULT_LONG;
        this.parkingId = ServerConstants.DEFAULT_LONG;
        this.portAccessId = ServerConstants.DEFAULT_LONG;
        this.portAccessWhitelistId = ServerConstants.DEFAULT_LONG;
        this.statementId = ServerConstants.DEFAULT_LONG;
        this.sessionId = ServerConstants.DEFAULT_LONG;
        this.newUpdatedOperatorId = ServerConstants.DEFAULT_LONG;
        this.vehicleRegistration = ServerConstants.DEFAULT_STRING;
        this.portOperatorId = ServerConstants.DEFAULT_LONG;
        this.transactionType = ServerConstants.DEFAULT_INT;
        this.referenceNumber = ServerConstants.DEFAULT_STRING;
        this.isAutoReleaseParking = false;
        this.vehicleReleaseCount = ServerConstants.DEFAULT_INT;
        this.objectJson = ServerConstants.DEFAULT_STRING;
        this.dateLog = new Date();
    }

    public ActivityLog(long activityId, long operatorId, long accountId, long newUpdatedOperatorId) {
        this.activityId = activityId;
        this.operatorId = operatorId;
        this.accountId = accountId;
        this.missionId = ServerConstants.DEFAULT_LONG;
        this.tripId = ServerConstants.DEFAULT_LONG;
        this.parkingId = ServerConstants.DEFAULT_LONG;
        this.portAccessId = ServerConstants.DEFAULT_LONG;
        this.portAccessWhitelistId = ServerConstants.DEFAULT_LONG;
        this.statementId = ServerConstants.DEFAULT_LONG;
        this.sessionId = ServerConstants.DEFAULT_LONG;
        this.newUpdatedOperatorId = newUpdatedOperatorId;
        this.vehicleRegistration = ServerConstants.DEFAULT_STRING;
        this.portOperatorId = ServerConstants.DEFAULT_LONG;
        this.transactionType = ServerConstants.DEFAULT_INT;
        this.referenceNumber = ServerConstants.DEFAULT_STRING;
        this.isAutoReleaseParking = false;
        this.vehicleReleaseCount = ServerConstants.DEFAULT_INT;
        this.objectJson = ServerConstants.DEFAULT_STRING;
        this.dateLog = new Date();
    }

    public ActivityLog(long activityId, long operatorId, long missionId, long tripId, long parkingId, long portAccessId, long portAccessWhiteListId, long statementId,
        long sessionId) {
        this.activityId = activityId;
        this.operatorId = operatorId;
        this.accountId = ServerConstants.DEFAULT_LONG;
        this.missionId = missionId;
        this.tripId = tripId;
        this.parkingId = parkingId;
        this.portAccessId = portAccessId;
        this.portAccessWhitelistId = portAccessWhiteListId;
        this.statementId = statementId;
        this.sessionId = sessionId;
        this.newUpdatedOperatorId = ServerConstants.DEFAULT_LONG;
        this.vehicleRegistration = ServerConstants.DEFAULT_STRING;
        this.portOperatorId = ServerConstants.DEFAULT_LONG;
        this.transactionType = ServerConstants.DEFAULT_INT;
        this.referenceNumber = ServerConstants.DEFAULT_STRING;
        this.isAutoReleaseParking = false;
        this.vehicleReleaseCount = ServerConstants.DEFAULT_INT;
        this.objectJson = ServerConstants.DEFAULT_STRING;
        this.dateLog = new Date();
    }

    public ActivityLog(long activityId, long operatorId, String vehicleRegistration, long portOperatorId, int transactionType, String referenceNumber) {
        this.activityId = activityId;
        this.operatorId = operatorId;
        this.accountId = ServerConstants.DEFAULT_LONG;
        this.missionId = ServerConstants.DEFAULT_LONG;
        this.tripId = ServerConstants.DEFAULT_LONG;
        this.parkingId = ServerConstants.DEFAULT_LONG;
        this.portAccessId = ServerConstants.DEFAULT_LONG;
        this.portAccessWhitelistId = ServerConstants.DEFAULT_LONG;
        this.statementId = ServerConstants.DEFAULT_LONG;
        this.sessionId = ServerConstants.DEFAULT_LONG;
        this.newUpdatedOperatorId = ServerConstants.DEFAULT_LONG;
        this.vehicleRegistration = vehicleRegistration;
        this.portOperatorId = portOperatorId;
        this.transactionType = transactionType;
        this.referenceNumber = referenceNumber;
        this.isAutoReleaseParking = false;
        this.vehicleReleaseCount = ServerConstants.DEFAULT_INT;
        this.objectJson = ServerConstants.DEFAULT_STRING;
        this.dateLog = new Date();
    }

    public ActivityLog(long activityId, long operatorId, long portOperatorId, int transactionType, boolean isAutoReleaseParking) {
        this.activityId = activityId;
        this.operatorId = operatorId;
        this.accountId = ServerConstants.DEFAULT_LONG;
        this.missionId = ServerConstants.DEFAULT_LONG;
        this.tripId = ServerConstants.DEFAULT_LONG;
        this.parkingId = ServerConstants.DEFAULT_LONG;
        this.portAccessId = ServerConstants.DEFAULT_LONG;
        this.portAccessWhitelistId = ServerConstants.DEFAULT_LONG;
        this.statementId = ServerConstants.DEFAULT_LONG;
        this.sessionId = ServerConstants.DEFAULT_LONG;
        this.newUpdatedOperatorId = ServerConstants.DEFAULT_LONG;
        this.vehicleRegistration = "";
        this.portOperatorId = portOperatorId;
        this.transactionType = transactionType;
        this.referenceNumber = "";
        this.isAutoReleaseParking = isAutoReleaseParking;
        this.vehicleReleaseCount = ServerConstants.DEFAULT_INT;
        this.objectJson = ServerConstants.DEFAULT_STRING;
        this.dateLog = new Date();
    }

    public ActivityLog(long activityId, long operatorId, long portOperatorId, int transactionType, int vehicleReleaseCount) {
        this.activityId = activityId;
        this.operatorId = operatorId;
        this.accountId = ServerConstants.DEFAULT_LONG;
        this.missionId = ServerConstants.DEFAULT_LONG;
        this.tripId = ServerConstants.DEFAULT_LONG;
        this.parkingId = ServerConstants.DEFAULT_LONG;
        this.portAccessId = ServerConstants.DEFAULT_LONG;
        this.portAccessWhitelistId = ServerConstants.DEFAULT_LONG;
        this.statementId = ServerConstants.DEFAULT_LONG;
        this.sessionId = ServerConstants.DEFAULT_LONG;
        this.newUpdatedOperatorId = ServerConstants.DEFAULT_LONG;
        this.vehicleRegistration = "";
        this.portOperatorId = portOperatorId;
        this.transactionType = transactionType;
        this.referenceNumber = "";
        this.isAutoReleaseParking = false;
        this.vehicleReleaseCount = vehicleReleaseCount;
        this.objectJson = ServerConstants.DEFAULT_STRING;
        this.dateLog = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public long getParkingId() {
        return parkingId;
    }

    public void setParkingId(long parkingId) {
        this.parkingId = parkingId;
    }

    public long getPortAccessId() {
        return portAccessId;
    }

    public void setPortAccessId(long portAccessId) {
        this.portAccessId = portAccessId;
    }

    public long getPortAccessWhitelistId() {
        return portAccessWhitelistId;
    }

    public void setPortAccessWhitelistId(long portAccessWhitelistId) {
        this.portAccessWhitelistId = portAccessWhitelistId;
    }

    public long getStatementId() {
        return statementId;
    }

    public void setStatementId(long statementId) {
        this.statementId = statementId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getNewUpdatedOperatorId() {
        return newUpdatedOperatorId;
    }

    public void setNewUpdatedOperatorId(long newUpdatedOperatorId) {
        this.newUpdatedOperatorId = newUpdatedOperatorId;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public long getPortOperatorId() {
        return portOperatorId;
    }

    public void setPortOperatorId(long portOperatorId) {
        this.portOperatorId = portOperatorId;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public boolean getIsAutoReleaseParking() {
        return isAutoReleaseParking;
    }

    public void setIsAutoReleaseParking(boolean isAutoReleaseParking) {
        this.isAutoReleaseParking = isAutoReleaseParking;
    }

    public int getVehicleReleaseCount() {
        return vehicleReleaseCount;
    }

    public void setVehicleReleaseCount(int vehicleReleaseCount) {
        this.vehicleReleaseCount = vehicleReleaseCount;
    }

    public String getObjectJson() {
        return objectJson;
    }

    public void setObjectJson(String objectJson) {
        this.objectJson = objectJson;
    }

    public Date getDateLog() {
        return dateLog;
    }

    public void setDateLog(Date dateLog) {
        this.dateLog = dateLog;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getOperatorUsername() {
        return operatorUsername;
    }

    public void setOperatorUsername(String operatorUsername) {
        this.operatorUsername = operatorUsername;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ActivityLog [id=");
        builder.append(id);
        builder.append(", operatorId=");
        builder.append(operatorId);
        builder.append(", activityId=");
        builder.append(activityId);
        builder.append(", accountId=");
        builder.append(accountId);
        builder.append(", missionId=");
        builder.append(missionId);
        builder.append(", tripId=");
        builder.append(tripId);
        builder.append(", parkingId=");
        builder.append(parkingId);
        builder.append(", portAccessId=");
        builder.append(portAccessId);
        builder.append(", portAccessWhitelistId=");
        builder.append(portAccessWhitelistId);
        builder.append(", statementId=");
        builder.append(statementId);
        builder.append(", sessionId=");
        builder.append(sessionId);
        builder.append(", newUpdatedOperatorId=");
        builder.append(newUpdatedOperatorId);
        builder.append(", vehicleRegistration=");
        builder.append(vehicleRegistration);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", transactionType=");
        builder.append(transactionType);
        builder.append(", referenceNumber=");
        builder.append(referenceNumber);
        builder.append(", isAutoReleaseParking=");
        builder.append(isAutoReleaseParking);
        builder.append(", vehicleReleaseCount=");
        builder.append(vehicleReleaseCount);
        builder.append(", objectJson=");
        builder.append(objectJson);
        builder.append(", dateLog=");
        builder.append(dateLog);
        builder.append(", activityName=");
        builder.append(activityName);
        builder.append(", operatorUsername=");
        builder.append(operatorUsername);
        builder.append("]");
        return builder.toString();
    }

}
