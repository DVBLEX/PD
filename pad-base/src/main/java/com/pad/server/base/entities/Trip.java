package com.pad.server.base.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "trips")
public class Trip implements Serializable {

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

    @Column(name = "account_id")
    private long              accountId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "mission_id")
    private Mission           mission;

    @Column(name = "vehicle_id")
    private long              vehicleId;

    @Column(name = "driver_id")
    private long              driverId;

    @Column(name = "port_operator_id")
    private long              portOperatorId;

    @Column(name = "independent_port_operator_id")
    private long              independentPortOperatorId;

    @Column(name = "transaction_type")
    private int               transactionType;

    @Column(name = "port_operator_gate_id")
    private long              portOperatorGateId;

    @Column(name = "parking_permission_id")
    private long              parkingPermissionId;

    @Column(name = "parking_permission_id_parking_entry_first")
    private long              parkingPermissionIdParkingEntryFirst;

    @Column(name = "parking_permission_id_parking_entry")
    private long              parkingPermissionIdParkingEntry;

    @Column(name = "parking_permission_id_parking_exit")
    private long              parkingPermissionIdParkingExit;

    @Column(name = "parking_permission_id_port_entry")
    private long              parkingPermissionIdPortEntry;

    @Column(name = "parking_permission_id_port_exit")
    private long              parkingPermissionIdPortExit;

    @Column(name = "reference_number")
    private String            referenceNumber;

    @Column(name = "container_id")
    private String            containerId;

    @Column(name = "container_type")
    private String            containerType;

    @Column(name = "vehicle_registration")
    private String            vehicleRegistration;

    @Column(name = "vehicle_registration_country_iso")
    private String            vehicleRegistrationCountryISO;

    @Column(name = "driver_msisdn")
    private String            driverMsisdn;

    @Column(name = "driver_language_id")
    private long              driverLanguageId;

    @Column(name = "company_name")
    private String            companyName;

    @Column(name = "operator_id_created")
    private long              operatorIdCreated;

    @Column(name = "operator_id")
    private long              operatorId;

    @Column(name = "lane_session_id")
    private long              laneSessionId;

    @Column(name = "is_fee_paid")
    private boolean           isFeePaid;

    @Column(name = "currency")
    private String            currency;

    @Column(name = "amount_fee")
    private BigDecimal        feeAmount;

    @Column(name = "operator_amount_fee")
    private BigDecimal        operatorFeeAmount;

    @Column(name = "date_fee_paid")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateFeePaid;

    @Column(name = "date_slot_requested")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateSlotRequested;

    @Column(name = "parking_entry_count")
    private int               parkingEntryCount;

    @Column(name = "parking_exit_count")
    private int               parkingExitCount;

    @Column(name = "port_entry_count")
    private int               portEntryCount;

    @Column(name = "port_exit_count")
    private int               portExitCount;

    @Column(name = "is_direct_to_port")
    private boolean           isDirectToPort;

    @Column(name = "is_allow_multiple_entries")
    private boolean           isAllowMultipleEntries;

    @Column(name = "date_slot_approved")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateSlotApproved;

    @Column(name = "reason_deny")
    private String            reasonDeny;

    @Column(name = "date_approved_denied")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateApprovedDenied;

    @Column(name = "date_entry_parking")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateEntryParking;

    @Column(name = "date_exit_parking")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateExitParking;

    @Column(name = "date_entry_port")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateEntryPort;

    @Column(name = "date_exit_port")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateExitPort;

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

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
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

    public long getIndependentPortOperatorId() {
        return independentPortOperatorId;
    }

    public void setIndependentPortOperatorId(long independentPortOperatorId) {
        this.independentPortOperatorId = independentPortOperatorId;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public long getPortOperatorGateId() {
        return portOperatorGateId;
    }

    public void setPortOperatorGateId(long portOperatorGateId) {
        this.portOperatorGateId = portOperatorGateId;
    }

    public long getParkingPermissionId() {
        return parkingPermissionId;
    }

    public void setParkingPermissionId(long parkingPermissionId) {
        this.parkingPermissionId = parkingPermissionId;
    }

    public long getParkingPermissionIdParkingEntryFirst() {
        return parkingPermissionIdParkingEntryFirst;
    }

    public void setParkingPermissionIdParkingEntryFirst(long parkingPermissionIdParkingEntryFirst) {
        this.parkingPermissionIdParkingEntryFirst = parkingPermissionIdParkingEntryFirst;
    }

    public long getParkingPermissionIdParkingEntry() {
        return parkingPermissionIdParkingEntry;
    }

    public void setParkingPermissionIdParkingEntry(long parkingPermissionIdParkingEntry) {
        this.parkingPermissionIdParkingEntry = parkingPermissionIdParkingEntry;
    }

    public long getParkingPermissionIdParkingExit() {
        return parkingPermissionIdParkingExit;
    }

    public void setParkingPermissionIdParkingExit(long parkingPermissionIdParkingExit) {
        this.parkingPermissionIdParkingExit = parkingPermissionIdParkingExit;
    }

    public long getParkingPermissionIdPortEntry() {
        return parkingPermissionIdPortEntry;
    }

    public void setParkingPermissionIdPortEntry(long parkingPermissionIdPortEntry) {
        this.parkingPermissionIdPortEntry = parkingPermissionIdPortEntry;
    }

    public long getParkingPermissionIdPortExit() {
        return parkingPermissionIdPortExit;
    }

    public void setParkingPermissionIdPortExit(long parkingPermissionIdPortExit) {
        this.parkingPermissionIdPortExit = parkingPermissionIdPortExit;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public String getVehicleRegistrationCountryISO() {
        return vehicleRegistrationCountryISO;
    }

    public void setVehicleRegistrationCountryISO(String vehicleRegistrationCountryISO) {
        this.vehicleRegistrationCountryISO = vehicleRegistrationCountryISO;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public long getOperatorIdCreated() {
        return operatorIdCreated;
    }

    public void setOperatorIdCreated(long operatorIdCreated) {
        this.operatorIdCreated = operatorIdCreated;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public long getLaneSessionId() {
        return laneSessionId;
    }

    public void setLaneSessionId(long laneSessionId) {
        this.laneSessionId = laneSessionId;
    }

    public boolean isFeePaid() {
        return isFeePaid;
    }

    public void setFeePaid(boolean isFeePaid) {
        this.isFeePaid = isFeePaid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public BigDecimal getOperatorFeeAmount() {
        return operatorFeeAmount;
    }

    public void setOperatorFeeAmount(BigDecimal operatorFeeAmount) {
        this.operatorFeeAmount = operatorFeeAmount;
    }

    public Date getDateFeePaid() {
        return dateFeePaid;
    }

    public void setDateFeePaid(Date dateFeePaid) {
        this.dateFeePaid = dateFeePaid;
    }

    public Date getDateSlotRequested() {
        return dateSlotRequested;
    }

    public void setDateSlotRequested(Date dateSlotRequested) {
        this.dateSlotRequested = dateSlotRequested;
    }

    public int getParkingEntryCount() {
        return parkingEntryCount;
    }

    public void setParkingEntryCount(int parkingEntryCount) {
        this.parkingEntryCount = parkingEntryCount;
    }

    public int getParkingExitCount() {
        return parkingExitCount;
    }

    public void setParkingExitCount(int parkingExitCount) {
        this.parkingExitCount = parkingExitCount;
    }

    public int getPortEntryCount() {
        return portEntryCount;
    }

    public void setPortEntryCount(int portEntryCount) {
        this.portEntryCount = portEntryCount;
    }

    public int getPortExitCount() {
        return portExitCount;
    }

    public void setPortExitCount(int portExitCount) {
        this.portExitCount = portExitCount;
    }

    public boolean getIsDirectToPort() {
        return isDirectToPort;
    }

    public void setIsDirectToPort(boolean isDirectToPort) {
        this.isDirectToPort = isDirectToPort;
    }

    public boolean getIsAllowMultipleEntries() {
        return isAllowMultipleEntries;
    }

    public void setIsAllowMultipleEntries(boolean isAllowMultipleEntries) {
        this.isAllowMultipleEntries = isAllowMultipleEntries;
    }

    public Date getDateSlotApproved() {
        return dateSlotApproved;
    }

    public void setDateSlotApproved(Date dateSlotApproved) {
        this.dateSlotApproved = dateSlotApproved;
    }

    public String getReasonDeny() {
        return reasonDeny;
    }

    public void setReasonDeny(String reasonDeny) {
        this.reasonDeny = reasonDeny;
    }

    public Date getDateApprovedDenied() {
        return dateApprovedDenied;
    }

    public void setDateApprovedDenied(Date dateApprovedDenied) {
        this.dateApprovedDenied = dateApprovedDenied;
    }

    public Date getDateEntryParking() {
        return dateEntryParking;
    }

    public void setDateEntryParking(Date dateEntryParking) {
        this.dateEntryParking = dateEntryParking;
    }

    public Date getDateExitParking() {
        return dateExitParking;
    }

    public void setDateExitParking(Date dateExitParking) {
        this.dateExitParking = dateExitParking;
    }

    public Date getDateEntryPort() {
        return dateEntryPort;
    }

    public void setDateEntryPort(Date dateEntryPort) {
        this.dateEntryPort = dateEntryPort;
    }

    public Date getDateExitPort() {
        return dateExitPort;
    }

    public void setDateExitPort(Date dateExitPort) {
        this.dateExitPort = dateExitPort;
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
        builder.append("Trip [id=");
        builder.append(id);
        builder.append(", code=");
        builder.append(code);
        builder.append(", type=");
        builder.append(type);
        builder.append(", status=");
        builder.append(status);
        builder.append(", accountId=");
        builder.append(accountId);
        builder.append(", mission=");
        builder.append(mission);
        builder.append(", vehicleId=");
        builder.append(vehicleId);
        builder.append(", driverId=");
        builder.append(driverId);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", independentPortOperatorId=");
        builder.append(independentPortOperatorId);
        builder.append(", transactionType=");
        builder.append(transactionType);
        builder.append(", portOperatorGateId=");
        builder.append(portOperatorGateId);
        builder.append(", parkingPermissionId=");
        builder.append(parkingPermissionId);
        builder.append(", parkingPermissionIdParkingEntryFirst=");
        builder.append(parkingPermissionIdParkingEntryFirst);
        builder.append(", parkingPermissionIdParkingEntry=");
        builder.append(parkingPermissionIdParkingEntry);
        builder.append(", parkingPermissionIdParkingExit=");
        builder.append(parkingPermissionIdParkingExit);
        builder.append(", parkingPermissionIdPortEntry=");
        builder.append(parkingPermissionIdPortEntry);
        builder.append(", parkingPermissionIdPortExit=");
        builder.append(parkingPermissionIdPortExit);
        builder.append(", referenceNumber=");
        builder.append(referenceNumber);
        builder.append(", containerId=");
        builder.append(containerId);
        builder.append(", containerType=");
        builder.append(containerType);
        builder.append(", vehicleRegistration=");
        builder.append(vehicleRegistration);
        builder.append(", vehicleRegistrationCountryISO=");
        builder.append(vehicleRegistrationCountryISO);
        builder.append(", driverMsisdn=");
        builder.append(driverMsisdn);
        builder.append(", driverLanguageId=");
        builder.append(driverLanguageId);
        builder.append(", companyName=");
        builder.append(companyName);
        builder.append(", operatorIdCreated=");
        builder.append(operatorIdCreated);
        builder.append(", operatorId=");
        builder.append(operatorId);
        builder.append(", laneSessionId=");
        builder.append(laneSessionId);
        builder.append(", isFeePaid=");
        builder.append(isFeePaid);
        builder.append(", currency=");
        builder.append(currency);
        builder.append(", feeAmount=");
        builder.append(feeAmount);
        builder.append(", operatorFeeAmount=");
        builder.append(operatorFeeAmount);
        builder.append(", dateFeePaid=");
        builder.append(dateFeePaid);
        builder.append(", dateSlotRequested=");
        builder.append(dateSlotRequested);
        builder.append(", parkingEntryCount=");
        builder.append(parkingEntryCount);
        builder.append(", parkingExitCount=");
        builder.append(parkingExitCount);
        builder.append(", portEntryCount=");
        builder.append(portEntryCount);
        builder.append(", portExitCount=");
        builder.append(portExitCount);
        builder.append(", isDirectToPort=");
        builder.append(isDirectToPort);
        builder.append(", isAllowMultipleEntries=");
        builder.append(isAllowMultipleEntries);
        builder.append(", dateSlotApproved=");
        builder.append(dateSlotApproved);
        builder.append(", reasonDeny=");
        builder.append(reasonDeny);
        builder.append(", dateApprovedDenied=");
        builder.append(dateApprovedDenied);
        builder.append(", dateEntryParking=");
        builder.append(dateEntryParking);
        builder.append(", dateExitParking=");
        builder.append(dateExitParking);
        builder.append(", dateEntryPort=");
        builder.append(dateEntryPort);
        builder.append(", dateExitPort=");
        builder.append(dateExitPort);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateEdited=");
        builder.append(dateEdited);
        builder.append("]");
        return builder.toString();
    }

}
