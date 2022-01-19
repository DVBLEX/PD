package com.pad.server.base.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "missions")
public class Mission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "code")
    private String            code;

    @Column(name = "status")
    private int               status;

    @Column(name = "account_id")
    private long              accountId;

    @Column(name = "port_operator_id")
    private int               portOperatorId;

    @Column(name = "independent_port_operator_id")
    private long              independentPortOperatorId;

    @Column(name = "port_operator_gate_id")
    private long              portOperatorGateId;

    @Column(name = "transaction_type")
    private int               transactionType;

    @Column(name = "reference_number")
    private String            referenceNumber;

    @Column(name = "transporter_comments")
    private String            transporterComments;

    @Column(name = "count_trips_completed")
    private int               tripsCompletedCount;

    @Column(name = "count_trips_booked")
    private int               tripsBookedCount;

    @Column(name = "operator_id")
    private long              operatorId;

    @Column(name = "is_direct_to_port")
    private boolean           isDirectToPort;

    @Column(name = "is_allow_multiple_entries")
    private boolean           isAllowMultipleEntries;

    @Column(name = "date_mission_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateMissionStart;

    @Column(name = "date_mission_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateMissionEnd;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    @Column(name = "date_edited")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateEdited;

    @Transient
    private String            dateMissionStartString;

    @Transient
    private String            dateMissionEndString;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mission", cascade = CascadeType.PERSIST)
    private List<Trip>        tripList;

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

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public int getPortOperatorId() {
        return portOperatorId;
    }

    public void setPortOperatorId(int portOperatorId) {
        this.portOperatorId = portOperatorId;
    }

    public long getIndependentPortOperatorId() {
        return independentPortOperatorId;
    }

    public void setIndependentPortOperatorId(long independentPortOperatorId) {
        this.independentPortOperatorId = independentPortOperatorId;
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

    public long getPortOperatorGateId() {
        return portOperatorGateId;
    }

    public void setPortOperatorGateId(long portOperatorGateId) {
        this.portOperatorGateId = portOperatorGateId;
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

    public String getTransporterComments() {
        return transporterComments;
    }

    public void setTransporterComments(String transporterComments) {
        this.transporterComments = transporterComments;
    }

    public int getTripsCompletedCount() {
        return tripsCompletedCount;
    }

    public void setTripsCompletedCount(int tripsCompletedCount) {
        this.tripsCompletedCount = tripsCompletedCount;
    }

    public int getTripsBookedCount() {
        return tripsBookedCount;
    }

    public void setTripsBookedCount(int tripsBookedCount) {
        this.tripsBookedCount = tripsBookedCount;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public Date getDateMissionStart() {
        return dateMissionStart;
    }

    public void setDateMissionStart(Date dateMissionStart) {
        this.dateMissionStart = dateMissionStart;
    }

    public Date getDateMissionEnd() {
        return dateMissionEnd;
    }

    public void setDateMissionEnd(Date dateMissionEnd) {
        this.dateMissionEnd = dateMissionEnd;
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

    public String getDateMissionStartString() {
        return dateMissionStartString;
    }

    public void setDateMissionStartString(String dateMissionStartString) {
        this.dateMissionStartString = dateMissionStartString;
    }

    public String getDateMissionEndString() {
        return dateMissionEndString;
    }

    public void setDateMissionEndString(String dateMissionEndString) {
        this.dateMissionEndString = dateMissionEndString;
    }

    public List<Trip> getTripList() {
        return tripList;
    }

    public void setTripList(List<Trip> tripList) {
        this.tripList = tripList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mission [id=");
        builder.append(id);
        builder.append(", code=");
        builder.append(code);
        builder.append(", status=");
        builder.append(status);
        builder.append(", accountId=");
        builder.append(accountId);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", independentPortOperatorId=");
        builder.append(independentPortOperatorId);
        builder.append(", portOperatorGateId=");
        builder.append(portOperatorGateId);
        builder.append(", referenceNumber=");
        builder.append(referenceNumber);
        builder.append(", transporterComments=");
        builder.append(transporterComments);
        builder.append(", tripsCompletedCount=");
        builder.append(tripsCompletedCount);
        builder.append(", tripsBookedCount=");
        builder.append(tripsBookedCount);
        builder.append(", operatorId=");
        builder.append(operatorId);
        builder.append(", isDirectToPort=");
        builder.append(isDirectToPort);
        builder.append(", isAllowMultipleEntries=");
        builder.append(isAllowMultipleEntries);
        builder.append(", dateMissionStart=");
        builder.append(dateMissionStart);
        builder.append(", dateMissionEnd=");
        builder.append(dateMissionEnd);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateEdited=");
        builder.append(dateEdited);
        builder.append(", tripList=");
        builder.append(tripList == null ? "null" : tripList.size());
        builder.append("]");
        return builder.toString();
    }

}
