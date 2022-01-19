package com.pad.server.base.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "port_operator_transaction_types")
public class PortOperatorTransactionType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "port_operator_id")
    private PortOperator      portOperator;

    @Column(name = "transaction_type")
    private int               transactionType;

    @Column(name = "transaction_type_code")
    private String            transactionTypeCode;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "port_operator_gate_id")
    private PortOperatorGate  portOperatorGate;

    @Column(name = "translate_key")
    private String            translateKey;

    @Column(name = "translate_key_short")
    private String            translateKeyShort;

    // flag used to determine whether to enable/disable visibility of a transaction type to parking entry and kiosk op
    // It signals if a particular mission type is expected to go to the parking area
    @Column(name = "is_allowed_for_parking_and_kiosk_op")
    private boolean           isAllowedForParkingAndKioskOp;

    // flag used to determine whether to enable/disable visibility of a transaction type to virtual kiosk op
    @Column(name = "is_allowed_for_virtual_kiosk_op")
    private boolean           isAllowedForVirtualKioskOp;

    // flag used to enable auto release functionality at parking area
    @Column(name = "is_auto_release_parking")
    private boolean           isAutoReleaseParking;

    // flag used to determine whether trips under this transaction type will go direct to port
    @Column(name = "is_direct_to_port")
    private boolean           isDirectToPort;

    @Column(name = "is_allow_multiple_entries")
    private boolean           isAllowMultipleEntries;

    // used to determine how much in advance auto release system will release the vehicles in parking.
    // this is used when isAutoReleaseParking flag is enabled
    @Column(name = "port_transit_duration_minutes")
    private int               portTransitDurationMinutes;

    // used to determine how long after mission end date has expired the system will actually expire it and cancel any active trips
    @Column(name = "mission_cancel_system_after_minutes")
    private int               missionCancelSystemAfterMinutes;

    @Column(name = "is_trip_cancel_system")
    private boolean           isTripCancelSystem;

    @Column(name = "trip_cancel_system_after_minutes")
    private int               tripCancelSystemAfterMinutes;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PortOperator getPortOperator() {
        return portOperator;
    }

    public void setPortOperator(PortOperator portOperator) {
        this.portOperator = portOperator;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionTypeCode() {
        return transactionTypeCode;
    }

    public void setTransactionTypeCode(String transactionTypeCode) {
        this.transactionTypeCode = transactionTypeCode;
    }

    public PortOperatorGate getPortOperatorGate() {
        return portOperatorGate;
    }

    public void setPortOperatorGate(PortOperatorGate portOperatorGate) {
        this.portOperatorGate = portOperatorGate;
    }

    public String getTranslateKey() {
        return translateKey;
    }

    public void setTranslateKey(String translateKey) {
        this.translateKey = translateKey;
    }

    public String getTranslateKeyShort() {
        return translateKeyShort;
    }

    public void setTranslateKeyShort(String translateKeyShort) {
        this.translateKeyShort = translateKeyShort;
    }

    public boolean getIsAllowedForParkingAndKioskOp() {
        return isAllowedForParkingAndKioskOp;
    }

    public void setIsAllowedForParkingAndKioskOp(boolean isAllowedForParkingAndKioskOp) {
        this.isAllowedForParkingAndKioskOp = isAllowedForParkingAndKioskOp;
    }

    public boolean getIsAllowedForVirtualKioskOp() {
        return isAllowedForVirtualKioskOp;
    }

    public void setIsAllowedForVirtualKioskOp(boolean isAllowedForVirtualKioskOp) {
        this.isAllowedForVirtualKioskOp = isAllowedForVirtualKioskOp;
    }

    public boolean getIsAutoReleaseParking() {
        return isAutoReleaseParking;
    }

    public void setIsAutoReleaseParking(boolean isAutoReleaseParking) {
        this.isAutoReleaseParking = isAutoReleaseParking;
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

    public int getPortTransitDurationMinutes() {
        return portTransitDurationMinutes;
    }

    public void setPortTransitDurationMinutes(int portTransitDurationMinutes) {
        this.portTransitDurationMinutes = portTransitDurationMinutes;
    }

    public int getMissionCancelSystemAfterMinutes() {
        return missionCancelSystemAfterMinutes;
    }

    public void setMissionCancelSystemAfterMinutes(int missionCancelSystemAfterMinutes) {
        this.missionCancelSystemAfterMinutes = missionCancelSystemAfterMinutes;
    }

    public boolean getIsTripCancelSystem() {
        return isTripCancelSystem;
    }

    public void setIsTripCancelSystem(boolean tripCancelSystem) {
        isTripCancelSystem = tripCancelSystem;
    }

    public int getTripCancelSystemAfterMinutes() {
        return tripCancelSystemAfterMinutes;
    }

    public void setTripCancelSystemAfterMinutes(int tripCancelSystemAfterMinutes) {
        this.tripCancelSystemAfterMinutes = tripCancelSystemAfterMinutes;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PortOperatorTransactionType [id=");
        builder.append(id);
        builder.append(", portOperator=");
        builder.append(portOperator == null ? "null" : portOperator.getId());
        builder.append(", transactionType=");
        builder.append(transactionType);
        builder.append(", transactionTypeCode=");
        builder.append(transactionTypeCode);
        builder.append(", portOperatorGate=");
        builder.append(portOperatorGate == null ? "null" : portOperatorGate.getId());
        builder.append(", translateKey=");
        builder.append(translateKey);
        builder.append(", translateKeyShort=");
        builder.append(translateKeyShort);
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
        builder.append(", portTransitDurationMinutes=");
        builder.append(portTransitDurationMinutes);
        builder.append(", missionCancelSystemAfterMinutes=");
        builder.append(missionCancelSystemAfterMinutes);
        builder.append(", isTripCancelSystem=");
        builder.append(isTripCancelSystem);
        builder.append(", tripCancelSystemAfterMinutes=");
        builder.append(tripCancelSystemAfterMinutes);
        builder.append("]");
        return builder.toString();
    }

}
