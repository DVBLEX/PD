package com.pad.server.base.jsonentities.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PortAccessJson {

    private String  code;
    private Integer status;
    private Long    gateId;
    private String  gateNumber;
    private String  gateNumberShort;
    private String  vehicleRegistration;
    private String  datePortEntryString;
    private String  datePortExitString;
    private String  dateParkingExitString;
    private int     transactionType;
    private String  referenceNumber;
    private String  containerId;
    private String  driverMobile;
    private String  driverName;
    private String  driverLicenceNumber;
    private long    accountNumber;
    private int     accountType;
    private String  accountName;
    private String  companyName;
    private String  dateSlotString;
    private Integer portOperator;
    private String  independentPortOperatorCode;
    private boolean isAdHoc;
    private String  vehicleMaker;
    private String  vehicleColor;
    private String  reasonDeny;
    private String  dateStringDeny;
    private boolean isInTransit;
    private String  tripCode;
    private String  dateEntryFromString;
    private String  dateEntryToString;
    private String  timeEntryFromString;
    private String  timeEntryToString;
    private Long    minutesFromLastPortEntry;
    private String  independentPortOperatorName;
    private String  selectedZone;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public String getDatePortEntryString() {
        return datePortEntryString;
    }

    public void setDatePortEntryString(String datePortEntryString) {
        this.datePortEntryString = datePortEntryString;
    }

    public String getDatePortExitString() {
        return datePortExitString;
    }

    public void setDatePortExitString(String datePortExitString) {
        this.datePortExitString = datePortExitString;
    }

    public String getDateParkingExitString() {
        return dateParkingExitString;
    }

    public void setDateParkingExitString(String dateParkingExitString) {
        this.dateParkingExitString = dateParkingExitString;
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

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getDriverMobile() {
        return driverMobile;
    }

    public void setDriverMobile(String driverMobile) {
        this.driverMobile = driverMobile;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverLicenceNumber() {
        return driverLicenceNumber;
    }

    public void setDriverLicenceNumber(String driverLicenceNumber) {
        this.driverLicenceNumber = driverLicenceNumber;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDateSlotString() {
        return dateSlotString;
    }

    public void setDateSlotString(String dateSlotString) {
        this.dateSlotString = dateSlotString;
    }

    public Integer getPortOperator() {
        return portOperator;
    }

    public void setPortOperator(Integer portOperator) {
        this.portOperator = portOperator;
    }

    public String getIndependentPortOperatorCode() {
        return independentPortOperatorCode;
    }

    public void setIndependentPortOperatorCode(String independentPortOperatorCode) {
        this.independentPortOperatorCode = independentPortOperatorCode;
    }

    public boolean getIsAdHoc() {
        return isAdHoc;
    }

    public void setIsAdHoc(boolean isAdHoc) {
        this.isAdHoc = isAdHoc;
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

    public String getVehicleMaker() {
        return vehicleMaker;
    }

    public void setVehicleMaker(String vehicleMaker) {
        this.vehicleMaker = vehicleMaker;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getReasonDeny() {
        return reasonDeny;
    }

    public void setReasonDeny(String reasonDeny) {
        this.reasonDeny = reasonDeny;
    }

    public String getDateStringDeny() {
        return dateStringDeny;
    }

    public void setDateStringDeny(String dateStringDeny) {
        this.dateStringDeny = dateStringDeny;
    }

    public boolean getIsInTransit() {
        return isInTransit;
    }

    public void setIsInTransit(boolean isInTransit) {
        this.isInTransit = isInTransit;
    }

    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
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

    public String getDateEntryFromString() {
        return dateEntryFromString;
    }

    public void setDateEntryFromString(String dateEntryFromString) {
        this.dateEntryFromString = dateEntryFromString;
    }

    public String getDateEntryToString() {
        return dateEntryToString;
    }

    public void setDateEntryToString(String dateEntryToString) {
        this.dateEntryToString = dateEntryToString;
    }

    public String getTimeEntryFromString() {
        return timeEntryFromString;
    }

    public void setTimeEntryFromString(String timeEntryFromString) {
        this.timeEntryFromString = timeEntryFromString;
    }

    public String getTimeEntryToString() {
        return timeEntryToString;
    }

    public void setTimeEntryToString(String timeEntryToString) {
        this.timeEntryToString = timeEntryToString;
    }

    public Long getMinutesFromLastPortEntry() {
        return minutesFromLastPortEntry;
    }

    public void setMinutesFromLastPortEntry(Long minutesFromLastPortEntry) {
        this.minutesFromLastPortEntry = minutesFromLastPortEntry;
    }

    public String getIndependentPortOperatorName() {
        return independentPortOperatorName;
    }

    public void setIndependentPortOperatorName(String independentPortOperatorName) {
        this.independentPortOperatorName = independentPortOperatorName;
    }

    public String getSelectedZone() {
        return selectedZone;
    }

    public void setSelectedZone(String selectedZone) {
        this.selectedZone = selectedZone;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PortAccessJson [code=");
        builder.append(code);
        builder.append(", status=");
        builder.append(status);
        builder.append(", gateId=");
        builder.append(gateId);
        builder.append(", gateNumber=");
        builder.append(gateNumber);
        builder.append(", gateNumberShort=");
        builder.append(gateNumberShort);
        builder.append(", vehicleRegistration=");
        builder.append(vehicleRegistration);
        builder.append(", datePortEntryString=");
        builder.append(datePortEntryString);
        builder.append(", datePortExitString=");
        builder.append(datePortExitString);
        builder.append(", dateParkingExitString=");
        builder.append(dateParkingExitString);
        builder.append(", transactionType=");
        builder.append(transactionType);
        builder.append(", referenceNumber=");
        builder.append(referenceNumber);
        builder.append(", containerId=");
        builder.append(containerId);
        builder.append(", driverMobile=");
        builder.append(driverMobile);
        builder.append(", driverName=");
        builder.append(driverName);
        builder.append(", driverLicenceNumber=");
        builder.append(driverLicenceNumber);
        builder.append(", accountNumber=");
        builder.append(accountNumber);
        builder.append(", accountType=");
        builder.append(accountType);
        builder.append(", accountName=");
        builder.append(accountName);
        builder.append(", companyName=");
        builder.append(companyName);
        builder.append(", dateSlotString=");
        builder.append(dateSlotString);
        builder.append(", portOperator=");
        builder.append(portOperator);
        builder.append(", independentPortOperatorCode=");
        builder.append(independentPortOperatorCode);
        builder.append(", isAdHoc=");
        builder.append(isAdHoc);
        builder.append(", vehicleMaker=");
        builder.append(vehicleMaker);
        builder.append(", vehicleColor=");
        builder.append(vehicleColor);
        builder.append(", reasonDeny=");
        builder.append(reasonDeny);
        builder.append(", dateStringDeny=");
        builder.append(dateStringDeny);
        builder.append(", isInTransit=");
        builder.append(isInTransit);
        builder.append(", tripCode=");
        builder.append(tripCode);
        builder.append(", dateEntryFromString=");
        builder.append(dateEntryFromString);
        builder.append(", dateEntryToString=");
        builder.append(dateEntryToString);
        builder.append(", timeEntryFromString=");
        builder.append(timeEntryFromString);
        builder.append(", timeEntryToString=");
        builder.append(timeEntryToString);
        builder.append(", minutesFromLastPortEntry=");
        builder.append(minutesFromLastPortEntry);
        builder.append(", independentPortOperatorName=");
        builder.append(independentPortOperatorName);
        builder.append(", selectedZone=");
        builder.append(selectedZone);
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
