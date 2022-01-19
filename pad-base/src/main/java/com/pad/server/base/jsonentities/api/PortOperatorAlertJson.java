package com.pad.server.base.jsonentities.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PortOperatorAlertJson {

    private String  code;
    private Long    portOperatorId;
    private Integer transactionType;
    private Integer workingCapacityPercentage;
    private String  description;
    private String  issueDateString;
    private String  issueTimeString;
    private String  estimatedResolutionDateString;
    private String  estimatedResolutionTimeString;
    private String  nameReporter;
    private String  msisdnReporter;
    private boolean isResolved;
    private String  dateResolutionString;
    private String  timeResolutionString;
    private String  resolutionDescription;
    private String  dateCreatedString;
    private String  dateEditedString;

    // used for pagination
    private int     currentPage;
    private int     pageCount;
    private String  sortColumn;
    private boolean sortAsc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getPortOperatorId() {
        return portOperatorId;
    }

    public void setPortOperatorId(Long portOperatorId) {
        this.portOperatorId = portOperatorId;
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getWorkingCapacityPercentage() {
        return workingCapacityPercentage;
    }

    public void setWorkingCapacityPercentage(Integer workingCapacityPercentage) {
        this.workingCapacityPercentage = workingCapacityPercentage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEstimatedResolutionDateString() {
        return estimatedResolutionDateString;
    }

    public String getIssueDateString() {
        return issueDateString;
    }

    public void setIssueDateString(String issueDateString) {
        this.issueDateString = issueDateString;
    }

    public String getIssueTimeString() {
        return issueTimeString;
    }

    public void setIssueTimeString(String issueTimeString) {
        this.issueTimeString = issueTimeString;
    }

    public void setEstimatedResolutionDateString(String estimatedResolutionDateString) {
        this.estimatedResolutionDateString = estimatedResolutionDateString;
    }

    public String getEstimatedResolutionTimeString() {
        return estimatedResolutionTimeString;
    }

    public void setEstimatedResolutionTimeString(String estimatedResolutionTimeString) {
        this.estimatedResolutionTimeString = estimatedResolutionTimeString;
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

    public String getDateResolutionString() {
        return dateResolutionString;
    }

    public void setDateResolutionString(String dateResolutionString) {
        this.dateResolutionString = dateResolutionString;
    }

    public String getTimeResolutionString() {
        return timeResolutionString;
    }

    public void setTimeResolutionString(String timeResolutionString) {
        this.timeResolutionString = timeResolutionString;
    }

    public String getResolutionDescription() {
        return resolutionDescription;
    }

    public void setResolutionDescription(String resolutionDescription) {
        this.resolutionDescription = resolutionDescription;
    }

    public String getDateCreatedString() {
        return dateCreatedString;
    }

    public void setDateCreatedString(String dateCreatedString) {
        this.dateCreatedString = dateCreatedString;
    }

    public String getDateEditedString() {
        return dateEditedString;
    }

    public void setDateEditedString(String dateEditedString) {
        this.dateEditedString = dateEditedString;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PortOperatorAlertJson [code=");
        builder.append(code);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", transactionType=");
        builder.append(transactionType);
        builder.append(", workingCapacityPercentage=");
        builder.append(workingCapacityPercentage);
        builder.append(", description=");
        builder.append(description);
        builder.append(", issueDateString=");
        builder.append(issueDateString);
        builder.append(", issueTimeString=");
        builder.append(issueTimeString);
        builder.append(", estimatedResolutionDateString=");
        builder.append(estimatedResolutionDateString);
        builder.append(", estimatedResolutionTimeString=");
        builder.append(estimatedResolutionTimeString);
        builder.append(", nameReporter=");
        builder.append(nameReporter);
        builder.append(", msisdnReporter=");
        builder.append(msisdnReporter);
        builder.append(", isResolved=");
        builder.append(isResolved);
        builder.append(", dateResolutionString=");
        builder.append(dateResolutionString);
        builder.append(", timeResolutionString=");
        builder.append(timeResolutionString);
        builder.append(", resolutionDescription=");
        builder.append(resolutionDescription);
        builder.append(", dateCreatedString=");
        builder.append(dateCreatedString);
        builder.append(", dateEditedString=");
        builder.append(dateEditedString);
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
