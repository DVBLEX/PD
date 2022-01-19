package com.pad.server.base.jsonentities.api;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MissionJson {

    private String   code;
    private Integer  status;
    private Integer  portOperatorId;
    private Integer  independentPortOperatorId;
    private String   independentPortOperatorCode;
    private Long     gateId;
    private String   gateNumber;
    private String   gateNumberShort;
    private Integer  transactionType;
    private String   referenceNumber;
    private String   transporterComments;
    private String   containerId;
    private Integer  tripsCompletedCount;
    private Integer  tripsBookedCount;
    private String   dateCreatedString;
    private String   dateMissionStartString;
    private String   dateMissionEndString;
    private String   reasonRejected;

    private Date     dateCreated;
    private Date     dateMissionStart;
    private Date     dateMissionEnd;

    private String[] accountCodes;
    private long     transporterAccountNumber;
    private String   transporterAccountName;

    // used for pagination
    private int      currentPage;
    private int      pageCount;
    private String   sortColumn;
    private boolean  sortAsc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPortOperatorId() {
        return portOperatorId;
    }

    public void setPortOperatorId(Integer portOperatorId) {
        this.portOperatorId = portOperatorId;
    }

    public String getIndependentPortOperatorCode() {
        return independentPortOperatorCode;
    }

    public void setIndependentPortOperatorCode(String independentPortOperatorCode) {
        this.independentPortOperatorCode = independentPortOperatorCode;
    }

    public Integer getIndependentPortOperatorId() {
        return independentPortOperatorId;
    }

    public void setIndependentPortOperatorId(Integer independentPortOperatorId) {
        this.independentPortOperatorId = independentPortOperatorId;
    }

    public Long getGateId() {
        return gateId;
    }

    public void setGateId(Long gateId) {
        this.gateId = gateId;
    }

    public String getGateNumber() {
        return gateNumber;
    }

    public void setGateNumber(String gateNumber) {
        this.gateNumber = gateNumber;
    }

    public String getGateNumberShort() {
        return gateNumberShort;
    }

    public void setGateNumberShort(String gateNumberShort) {
        this.gateNumberShort = gateNumberShort;
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
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

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public Integer getTripsCompletedCount() {
        return tripsCompletedCount;
    }

    public void setTripsCompletedCount(Integer tripsCompletedCount) {
        this.tripsCompletedCount = tripsCompletedCount;
    }

    public Integer getTripsBookedCount() {
        return tripsBookedCount;
    }

    public void setTripsBookedCount(Integer tripsBookedCount) {
        this.tripsBookedCount = tripsBookedCount;
    }

    public String getDateCreatedString() {
        return dateCreatedString;
    }

    public void setDateCreatedString(String dateCreatedString) {
        this.dateCreatedString = dateCreatedString;
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

    public String getReasonRejected() {
        return reasonRejected;
    }

    public void setReasonRejected(String reasonRejected) {
        this.reasonRejected = reasonRejected;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
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

    public String[] getAccountCodes() {
        return accountCodes;
    }

    public void setAccountCodes(String[] accountCodes) {
        this.accountCodes = accountCodes;
    }

    public long getTransporterAccountNumber() {
        return transporterAccountNumber;
    }

    public void setTransporterAccountNumber(long transporterAccountNumber) {
        this.transporterAccountNumber = transporterAccountNumber;
    }

    public String getTransporterAccountName() {
        return transporterAccountName;
    }

    public void setTransporterAccountName(String transporterAccountName) {
        this.transporterAccountName = transporterAccountName;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public boolean getSortAsc() {
        return sortAsc;
    }

    public void setSortAsc(boolean sortAsc) {
        this.sortAsc = sortAsc;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MissionJson [code=");
        builder.append(code);
        builder.append(", status=");
        builder.append(status);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", independentPortOperatorId=");
        builder.append(independentPortOperatorId);
        builder.append(", independentPortOperatorCode=");
        builder.append(independentPortOperatorCode);
        builder.append(", gateId=");
        builder.append(gateId);
        builder.append(", gateNumber=");
        builder.append(gateNumber);
        builder.append(", gateNumberShort=");
        builder.append(gateNumberShort);
        builder.append(", transactionType=");
        builder.append(transactionType);
        builder.append(", referenceNumber=");
        builder.append(referenceNumber);
        builder.append(", transporterComments=");
        builder.append(transporterComments);
        builder.append(", containerId=");
        builder.append(containerId);
        builder.append(", tripsCompletedCount=");
        builder.append(tripsCompletedCount);
        builder.append(", tripsBookedCount=");
        builder.append(tripsBookedCount);
        builder.append(", dateCreatedString=");
        builder.append(dateCreatedString);
        builder.append(", dateMissionStartString=");
        builder.append(dateMissionStartString);
        builder.append(", dateMissionEndString=");
        builder.append(dateMissionEndString);
        builder.append(", reasonRejected=");
        builder.append(reasonRejected);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateMissionStart=");
        builder.append(dateMissionStart);
        builder.append(", dateMissionEnd=");
        builder.append(dateMissionEnd);
        builder.append(", transporterAccountNumber=");
        builder.append(transporterAccountNumber);
        builder.append(", transporterAccountName=");
        builder.append(transporterAccountName);
        builder.append(", currentPage=");
        builder.append(currentPage);
        builder.append(", pageCount=");
        builder.append(pageCount);
        builder.append(", sortColumn=");
        builder.append(sortColumn);
        builder.append(", sortAsc=");
        builder.append(sortAsc);
        builder.append("]");
        return builder.toString();
    }

}
