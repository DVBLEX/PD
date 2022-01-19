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

/**
 * @author rafael
 *
 */
@Entity
@Table(name = "port_access")
public class PortAccess implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "code")
    private String            code;

    @Column(name = "status")
    private int               status;

    @Column(name = "parking_id")
    private long              parkingId;

    @Column(name = "trip_id")
    private long              tripId;

    @Column(name = "mission_id")
    private long              missionId;

    @Column(name = "vehicle_id")
    private long              vehicleId;

    @Column(name = "driver_id")
    private long              driverId;

    @Column(name = "port_operator_id")
    private long              portOperatorId;

    @Column(name = "port_operator_gate_id")
    private long              portOperatorGateId;

    @Column(name = "vehicle_registration")
    private String            vehicleRegistration;

    @Column(name = "driver_msisdn")
    private String            driverMsisdn;

    @Column(name = "reason_deny")
    private String            reasonDeny;

    @Column(name = "date_deny")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateDeny;

    @Column(name = "operator_id_entry")
    private long              operatorIdEntry;

    @Column(name = "operator_gate")
    private String            operatorGate;

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

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    @Column(name = "date_edited")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateEdited;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getParkingId() {
        return parkingId;
    }

    public void setParkingId(long parkingId) {
        this.parkingId = parkingId;
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

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public long getPortOperatorId() {
        return portOperatorId;
    }

    public void setPortOperatorId(long portOperatorId) {
        this.portOperatorId = portOperatorId;
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

    public long getPortOperatorGateId() {
        return portOperatorGateId;
    }

    public void setPortOperatorGateId(long portOperatorGateId) {
        this.portOperatorGateId = portOperatorGateId;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public String getDriverMsisdn() {
        return driverMsisdn;
    }

    public void setDriverMsisdn(String driverMsisdn) {
        this.driverMsisdn = driverMsisdn;
    }

    public String getReasonDeny() {
        return reasonDeny;
    }

    public void setReasonDeny(String reasonDeny) {
        this.reasonDeny = reasonDeny;
    }

    public Date getDateDeny() {
        return dateDeny;
    }

    public void setDateDeny(Date dateDeny) {
        this.dateDeny = dateDeny;
    }

    public long getOperatorIdEntry() {
        return operatorIdEntry;
    }

    public void setOperatorIdEntry(long operatorIdEntry) {
        this.operatorIdEntry = operatorIdEntry;
    }

    public String getOperatorGate() {
        return operatorGate;
    }

    public void setOperatorGate(String operatorGate) {
        this.operatorGate = operatorGate;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PortAccess [id=");
        builder.append(id);
        builder.append(", code=");
        builder.append(code);
        builder.append(", status=");
        builder.append(status);
        builder.append(", parkingId=");
        builder.append(parkingId);
        builder.append(", tripId=");
        builder.append(tripId);
        builder.append(", missionId=");
        builder.append(missionId);
        builder.append(", vehicleId=");
        builder.append(vehicleId);
        builder.append(", driverId=");
        builder.append(driverId);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", portOperatorGateId=");
        builder.append(portOperatorGateId);
        builder.append(", vehicleRegistration=");
        builder.append(vehicleRegistration);
        builder.append(", driverMsisdn=");
        builder.append(driverMsisdn);
        builder.append(", reasonDeny=");
        builder.append(reasonDeny);
        builder.append(", dateDeny=");
        builder.append(dateDeny);
        builder.append(", operatorIdEntry=");
        builder.append(operatorIdEntry);
        builder.append(", operatorGate=");
        builder.append(operatorGate);
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
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateEdited=");
        builder.append(dateEdited);
        builder.append("]");
        return builder.toString();
    }

}
