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

@Entity
@Table(name = "parking")
public class Parking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "code")
    private String            code;

    @Column(name = "type")
    private int               type;

    @Column(name = "status")
    private int               status;

    @Column(name = "is_eligible_port_entry")
    private boolean           isEligiblePortEntry;

    @Column(name = "trip_id")
    private long              tripId;

    @Column(name = "mission_id")
    private long              missionId;

    @Column(name = "port_access_whitelist_id")
    private long              portAccessWhitelistId;

    @Column(name = "vehicle_id")
    private long              vehicleId;

    @Column(name = "driver_id")
    private long              driverId;

    @Column(name = "port_operator_id")
    private long              portOperatorId;

    @Column(name = "port_operator_gate_id")
    private long              portOperatorGateId;

    @Column(name = "vehicle_state")
    private int               vehicleState;

    @Column(name = "vehicle_registration")
    private String            vehicleRegistration;

    @Column(name = "vehicle_color")
    private String            vehicleColor;

    @Column(name = "driver_msisdn")
    private String            driverMsisdn;

    @Column(name = "driver_language_id")
    private long              driverLanguageId;

    @Column(name = "operator_id")
    private long              operatorId;

    @Column(name = "entry_lane_id")
    private long              entryLaneId;

    @Column(name = "entry_lane_number")
    private int               entryLaneNumber;

    @Column(name = "exit_lane_id")
    private long              exitLaneId;

    @Column(name = "exit_lane_number")
    private int               exitLaneNumber;

    @Column(name = "date_entry")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateEntry;

    @Column(name = "date_exit")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateExit;

    @Column(name = "date_sms_exit")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateSmsExit;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    @Column(name = "date_edited")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateEdited;

    @Transient
    private int               transactionType;

    @Transient
    private String            missionReferenceNumber;

    @Transient
    private Date              dateTripSlotApproved;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean getIsEligiblePortEntry() {
        return isEligiblePortEntry;
    }

    public void setIsEligiblePortEntry(boolean isEligiblePortEntry) {
        this.isEligiblePortEntry = isEligiblePortEntry;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public long getPortAccessWhitelistId() {
        return portAccessWhitelistId;
    }

    public void setPortAccessWhitelistId(long portAccessWhitelistId) {
        this.portAccessWhitelistId = portAccessWhitelistId;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public long getDriverId() {
        return driverId;
    }

    public long getPortOperatorId() {
        return portOperatorId;
    }

    public void setPortOperatorId(long portOperatorId) {
        this.portOperatorId = portOperatorId;
    }

    public long getPortOperatorGateId() {
        return portOperatorGateId;
    }

    public void setPortOperatorGateId(long portOperatorGateId) {
        this.portOperatorGateId = portOperatorGateId;
    }

    public int getVehicleState() {
        return vehicleState;
    }

    public void setVehicleState(int vehicleState) {
        this.vehicleState = vehicleState;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getDriverMsisdn() {
        return driverMsisdn;
    }

    public void setDriverMsisdn(String driverMsisdn) {
        this.driverMsisdn = driverMsisdn;
    }

    public long getDriverLanguageId() {
        return driverLanguageId;
    }

    public void setDriverLanguageId(long driverLanguageId) {
        this.driverLanguageId = driverLanguageId;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public long getEntryLaneId() {
        return entryLaneId;
    }

    public void setEntryLaneId(long entryLaneId) {
        this.entryLaneId = entryLaneId;
    }

    public int getEntryLaneNumber() {
        return entryLaneNumber;
    }

    public void setEntryLaneNumber(int entryLaneNumber) {
        this.entryLaneNumber = entryLaneNumber;
    }

    public long getExitLaneId() {
        return exitLaneId;
    }

    public void setExitLaneId(long exitLaneId) {
        this.exitLaneId = exitLaneId;
    }

    public int getExitLaneNumber() {
        return exitLaneNumber;
    }

    public void setExitLaneNumber(int exitLaneNumber) {
        this.exitLaneNumber = exitLaneNumber;
    }

    public Date getDateEntry() {
        return dateEntry;
    }

    public void setDateEntry(Date dateEntry) {
        this.dateEntry = dateEntry;
    }

    public Date getDateExit() {
        return dateExit;
    }

    public void setDateExit(Date dateExit) {
        this.dateExit = dateExit;
    }

    public Date getDateSmsExit() {
        return dateSmsExit;
    }

    public void setDateSmsExit(Date dateSmsExit) {
        this.dateSmsExit = dateSmsExit;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(Date dateEdited) {
        this.dateEdited = dateEdited;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public String getMissionReferenceNumber() {
        return missionReferenceNumber;
    }

    public void setMissionReferenceNumber(String missionReferenceNumber) {
        this.missionReferenceNumber = missionReferenceNumber;
    }

    public Date getDateTripSlotApproved() {
        return dateTripSlotApproved;
    }

    public void setDateTripSlotApproved(Date dateTripSlotApproved) {
        this.dateTripSlotApproved = dateTripSlotApproved;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Parking [id=");
        builder.append(id);
        builder.append(", code=");
        builder.append(code);
        builder.append(", type=");
        builder.append(type);
        builder.append(", status=");
        builder.append(status);
        builder.append(", isEligiblePortEntry=");
        builder.append(isEligiblePortEntry);
        builder.append(", tripId=");
        builder.append(tripId);
        builder.append(", missionId=");
        builder.append(missionId);
        builder.append(", portAccessWhitelistId=");
        builder.append(portAccessWhitelistId);
        builder.append(", vehicleId=");
        builder.append(vehicleId);
        builder.append(", driverId=");
        builder.append(driverId);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", portOperatorGateId=");
        builder.append(portOperatorGateId);
        builder.append(", vehicleState=");
        builder.append(vehicleState);
        builder.append(", vehicleRegistration=");
        builder.append(vehicleRegistration);
        builder.append(", vehicleColor=");
        builder.append(vehicleColor);
        builder.append(", driverMsisdn=");
        builder.append(driverMsisdn);
        builder.append(", driverLanguageId=");
        builder.append(driverLanguageId);
        builder.append(", operatorId=");
        builder.append(operatorId);
        builder.append(", entryLaneId=");
        builder.append(entryLaneId);
        builder.append(", entryLaneNumber=");
        builder.append(entryLaneNumber);
        builder.append(", exitLaneId=");
        builder.append(exitLaneId);
        builder.append(", exitLaneNumber=");
        builder.append(exitLaneNumber);
        builder.append(", dateEntry=");
        builder.append(dateEntry);
        builder.append(", dateExit=");
        builder.append(dateExit);
        builder.append(", dateSmsExit=");
        builder.append(dateSmsExit);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateEdited=");
        builder.append(dateEdited);
        builder.append("]");
        return builder.toString();
    }

}
