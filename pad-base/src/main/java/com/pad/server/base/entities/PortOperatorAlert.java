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
@Table(name = "port_operator_alerts")
public class PortOperatorAlert implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private long              id;

    @Column(name = "code")
    private String            code;

    @Column(name = "port_operator_id")
    private long              portOperatorId;

    @Column(name = "transaction_type")
    private int               transactionType;

    @Column(name = "working_capacity")
    private int               workingCapacity;

    @Column(name = "description")
    private String            description;

    @Column(name = "date_issue")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateIssue;

    @Column(name = "date_resolution_estimate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateResolutionEstimate;

    @Column(name = "name_reporter")
    private String            nameReporter;

    @Column(name = "msisdn_reporter")
    private String            msisdnReporter;

    @Column(name = "is_resolved")
    private boolean           isResolved;

    @Column(name = "date_resolution")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateResolution;

    @Column(name = "resolution_description")
    private String            resolutionDescription;

    @Column(name = "operator_id")
    private long              operatorId;

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

    public int getWorkingCapacity() {
        return workingCapacity;
    }

    public void setWorkingCapacity(int workingCapacity) {
        this.workingCapacity = workingCapacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateIssue() {
        return dateIssue;
    }

    public void setDateIssue(Date dateIssue) {
        this.dateIssue = dateIssue;
    }

    public Date getDateResolutionEstimate() {
        return dateResolutionEstimate;
    }

    public void setDateResolutionEstimate(Date dateResolutionEstimate) {
        this.dateResolutionEstimate = dateResolutionEstimate;
    }

    public String getNameReporter() {
        return nameReporter;
    }

    public void setNameReporter(String nameReporter) {
        this.nameReporter = nameReporter;
    }

    public String getMsisdnReporter() {
        return msisdnReporter;
    }

    public void setMsisdnReporter(String msisdnReporter) {
        this.msisdnReporter = msisdnReporter;
    }

    public boolean getIsResolved() {
        return isResolved;
    }

    public void setIsResolved(boolean isResolved) {
        this.isResolved = isResolved;
    }

    public Date getDateResolution() {
        return dateResolution;
    }

    public void setDateResolution(Date dateResolution) {
        this.dateResolution = dateResolution;
    }

    public String getResolutionDescription() {
        return resolutionDescription;
    }

    public void setResolutionDescription(String resolutionDescription) {
        this.resolutionDescription = resolutionDescription;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
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
        builder.append("PortOperatorAlert [id=");
        builder.append(id);
        builder.append(", code=");
        builder.append(code);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", transactionType=");
        builder.append(transactionType);
        builder.append(", workingCapacity=");
        builder.append(workingCapacity);
        builder.append(", description=");
        builder.append(description);
        builder.append(", dateIssue=");
        builder.append(dateIssue);
        builder.append(", dateResolutionEstimate=");
        builder.append(dateResolutionEstimate);
        builder.append(", nameReporter=");
        builder.append(nameReporter);
        builder.append(", msisdnReporter=");
        builder.append(msisdnReporter);
        builder.append(", isResolved=");
        builder.append(isResolved);
        builder.append(", dateResolution=");
        builder.append(dateResolution);
        builder.append(", resolutionDescription=");
        builder.append(resolutionDescription);
        builder.append(", operatorId=");
        builder.append(operatorId);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateEdited=");
        builder.append(dateEdited);
        builder.append("]");
        return builder.toString();
    }

}
