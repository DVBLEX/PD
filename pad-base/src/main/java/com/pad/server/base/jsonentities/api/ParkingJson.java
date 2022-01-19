package com.pad.server.base.jsonentities.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParkingJson {

    private String   code;
    private Integer  type;
    private Integer  status;
    private Integer  vehicleState;
    private String   vehicleRegistration;
    private String   dateEntryString;
    private String   dateExitString;
    private int      transactionType;
    private String   referenceNumber;
    private String   containerId;
    private String   driverMobile;
    private String   driverName;
    private String   driverLicenceNumber;
    private int      driverLanguageId;
    private long     accountNumber;
    private int      accountType;
    private String   accountName;
    private String   companyName;
    private boolean  isAdHoc;
    private String   missionCode;
    private String   tripCode;
    private String   vehicleCode;
    private String   vehicleMaker;
    private String   vehicleColor;
    private String   driverCode;
    private String   dateSlotString;
    private String   dateExitSmsString;
    private Integer  portOperator;
    private String   gateNumber;
    private String   gateNumberShort;
    private Long     gateId;
    private Integer  addSecondsSchedule;
    private boolean  isWhitelisted;
    private String[] exitParkingCodes;
    private Boolean  isInTransit;
    private String   languageISOCode;
    private long     entryLaneId;
    private long     entryLaneNumber;
    private int      parkingExitMessageId;
    private String   dateEntryFromString;
    private String   dateEntryToString;
    private String   timeEntryFromString;
    private String   timeEntryToString;
    private boolean  isAutoReleaseOn;
    private Integer  vehiclesAlreadyReleasedCount;
    private Integer  bookingLimitCount;
    private Integer  releaseCount;
    private Boolean  isParkingSupervisorReadOnlyEnabled;
    private String   independentOperatorCode;
    private String   independentPortOperatorName;
    private Boolean  isPortEntryReadOnly;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getVehicleState() {
        return vehicleState;
    }

    public void setVehicleState(Integer vehicleState) {
        this.vehicleState = vehicleState;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public String getDateEntryString() {
        return dateEntryString;
    }

    public void setDateEntryString(String dateEntryString) {
        this.dateEntryString = dateEntryString;
    }

    public String getDateExitString() {
        return dateExitString;
    }

    public void setDateExitString(String dateExitString) {
        this.dateExitString = dateExitString;
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

    public int getDriverLanguageId() {
        return driverLanguageId;
    }

    public void setDriverLanguageId(int driverLanguageId) {
        this.driverLanguageId = driverLanguageId;
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

    public boolean getIsAdHoc() {
        return isAdHoc;
    }

    public void setIsAdHoc(boolean isAdHoc) {
        this.isAdHoc = isAdHoc;
    }

    public String getMissionCode() {
        return missionCode;
    }

    public void setMissionCode(String missionCode) {
        this.missionCode = missionCode;
    }

    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
    }

    public String getVehicleCode() {
        return vehicleCode;
    }

    public void setVehicleCode(String vehicleCode) {
        this.vehicleCode = vehicleCode;
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

    public String getDriverCode() {
        return driverCode;
    }

    public void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }

    public String getDateSlotString() {
        return dateSlotString;
    }

    public void setDateSlotString(String dateSlotString) {
        this.dateSlotString = dateSlotString;
    }

    public String getDateExitSmsString() {
        return dateExitSmsString;
    }

    public void setDateExitSmsString(String dateExitSmsString) {
        this.dateExitSmsString = dateExitSmsString;
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

    public Integer getPortOperator() {
        return portOperator;
    }

    public void setPortOperator(Integer portOperator) {
        this.portOperator = portOperator;
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

    public Long getGateId() {
        return gateId;
    }

    public void setGateId(Long gateId) {
        this.gateId = gateId;
    }

    public Integer getAddSecondsSchedule() {
        return addSecondsSchedule;
    }

    public void setAddSecondsSchedule(Integer addSecondsSchedule) {
        this.addSecondsSchedule = addSecondsSchedule;
    }

    public boolean getIsWhitelisted() {
        return isWhitelisted;
    }

    public void setIsWhitelisted(boolean isWhitelisted) {
        this.isWhitelisted = isWhitelisted;
    }

    public String[] getExitParkingCodes() {
        return exitParkingCodes;
    }

    public void setExitParkingCodes(String[] exitParkingCodes) {
        this.exitParkingCodes = exitParkingCodes;
    }

    public Boolean getIsInTransit() {
        return isInTransit;
    }

    public void setIsInTransit(Boolean isInTransit) {
        this.isInTransit = isInTransit;
    }

    public String getLanguageISOCode() {
        return languageISOCode;
    }

    public void setLanguageISOCode(String languageISOCode) {
        this.languageISOCode = languageISOCode;
    }

    public long getEntryLaneId() {
        return entryLaneId;
    }

    public void setEntryLaneId(long entryLaneId) {
        this.entryLaneId = entryLaneId;
    }

    public long getEntryLaneNumber() {
        return entryLaneNumber;
    }

    public void setEntryLaneNumber(long entryLaneNumber) {
        this.entryLaneNumber = entryLaneNumber;
    }

    public int getParkingExitMessageId() {
        return parkingExitMessageId;
    }

    public void setParkingExitMessageId(int parkingExitMessageId) {
        this.parkingExitMessageId = parkingExitMessageId;
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

    public boolean getIsAutoReleaseOn() {
        return isAutoReleaseOn;
    }

    public void setIsAutoReleaseOn(boolean isAutoReleaseOn) {
        this.isAutoReleaseOn = isAutoReleaseOn;
    }

    public Integer getVehiclesAlreadyReleasedCount() {
        return vehiclesAlreadyReleasedCount;
    }

    public void setVehiclesAlreadyReleasedCount(Integer vehiclesAlreadyReleasedCount) {
        this.vehiclesAlreadyReleasedCount = vehiclesAlreadyReleasedCount;
    }

    public Integer getBookingLimitCount() {
        return bookingLimitCount;
    }

    public void setBookingLimitCount(Integer bookingLimitCount) {
        this.bookingLimitCount = bookingLimitCount;
    }

    public Integer getReleaseCount() {
        return releaseCount;
    }

    public void setReleaseCount(Integer releaseCount) {
        this.releaseCount = releaseCount;
    }

    public Boolean getIsParkingSupervisorReadOnlyEnabled() {
        return isParkingSupervisorReadOnlyEnabled;
    }

    public void setIsParkingSupervisorReadOnlyEnabled(Boolean isParkingSupervisorReadOnlyEnabled) {
        this.isParkingSupervisorReadOnlyEnabled = isParkingSupervisorReadOnlyEnabled;
    }

    public String getIndependentOperatorCode() {
        return independentOperatorCode;
    }

    public void setIndependentOperatorCode(String independentOperatorCode) {
        this.independentOperatorCode = independentOperatorCode;
    }

    public String getIndependentPortOperatorName() {
        return independentPortOperatorName;
    }

    public void setIndependentPortOperatorName(String independentPortOperatorName) {
        this.independentPortOperatorName = independentPortOperatorName;
    }

    public Boolean getIsPortEntryReadOnly() {
        return isPortEntryReadOnly;
    }

    public void setIsPortEntryReadOnly(Boolean portEntryReadOnly) {
        isPortEntryReadOnly = portEntryReadOnly;
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
        builder.append("ParkingJson [code=");
        builder.append(code);
        builder.append(", type=");
        builder.append(type);
        builder.append(", status=");
        builder.append(status);
        builder.append(", vehicleState=");
        builder.append(vehicleState);
        builder.append(", vehicleRegistration=");
        builder.append(vehicleRegistration);
        builder.append(", dateEntryString=");
        builder.append(dateEntryString);
        builder.append(", dateExitString=");
        builder.append(dateExitString);
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
        builder.append(", driverLanguageId=");
        builder.append(driverLanguageId);
        builder.append(", accountNumber=");
        builder.append(accountNumber);
        builder.append(", accountType=");
        builder.append(accountType);
        builder.append(", accountName=");
        builder.append(accountName);
        builder.append(", companyName=");
        builder.append(companyName);
        builder.append(", missionCode=");
        builder.append(missionCode);
        builder.append(", tripCode=");
        builder.append(tripCode);
        builder.append(", vehicleCode=");
        builder.append(vehicleCode);
        builder.append(", vehicleMaker=");
        builder.append(vehicleMaker);
        builder.append(", vehicleColor=");
        builder.append(vehicleColor);
        builder.append(", driverCode=");
        builder.append(driverCode);
        builder.append(", dateSlotString=");
        builder.append(dateSlotString);
        builder.append(", dateExitSmsString=");
        builder.append(dateExitSmsString);
        builder.append(", currentPage=");
        builder.append(currentPage);
        builder.append(", pageCount=");
        builder.append(pageCount);
        builder.append(", portOperator=");
        builder.append(portOperator);
        builder.append(", gateNumber=");
        builder.append(gateNumber);
        builder.append(", gateNumberShort=");
        builder.append(gateNumberShort);
        builder.append(", gateId=");
        builder.append(gateId);
        builder.append(", addSecondsSchedule=");
        builder.append(addSecondsSchedule);
        builder.append(", exitParkingCodes=");
        builder.append(exitParkingCodes == null ? "null" : exitParkingCodes.length);
        builder.append(", languageISOCode=");
        builder.append(languageISOCode);
        builder.append(", entryLaneId=");
        builder.append(entryLaneId);
        builder.append(", entryLaneNumber=");
        builder.append(entryLaneNumber);
        builder.append(", dateEntryFromString=");
        builder.append(dateEntryFromString);
        builder.append(", dateEntryToString=");
        builder.append(dateEntryToString);
        builder.append(", timeEntryFromString=");
        builder.append(timeEntryFromString);
        builder.append(", timeEntryToString=");
        builder.append(timeEntryToString);
        builder.append(", isAutoReleaseOn=");
        builder.append(isAutoReleaseOn);
        builder.append(", vehiclesAlreadyReleasedCount=");
        builder.append(vehiclesAlreadyReleasedCount);
        builder.append(", bookingLimitCount=");
        builder.append(bookingLimitCount);
        builder.append(", releaseCount=");
        builder.append(releaseCount);
        builder.append(", isParkingSupervisorReadOnlyEnabled=");
        builder.append(isParkingSupervisorReadOnlyEnabled);
        builder.append(", isPortEntryReadOnly=");
        builder.append(isPortEntryReadOnly);
        builder.append(", sortColumn=");
        builder.append(sortColumn);
        builder.append(", sortAsc=");
        builder.append(sortAsc);
        builder.append("]");
        return builder.toString();
    }

}
