package com.pad.server.base.jsonentities.api;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripJson {

    private String       code;
    private String       missionCode;
    private String       vehicleCode;
    private String       driverCode;
    private List<String> tripCodes;
    private String       vehicleRegistration;
    private String       vehicleRegistrationCountryISO;
    private String       driverName;
    private int          status;
    private boolean      isFeePaid;
    private boolean      isAdHoc;
    private String       dateSlotString;
    private String       timeSlotString;
    private Integer      portOperatorId;
    private Integer      independentPortOperatorId;
    private String       independentPortOperatorCode;
    private Long         gateId;
    private String       gateNumber;
    private String       gateNumberShort;
    private Integer      transactionType;
    private String       referenceNumber;
    private String       containerId;
    private String       containerType;
    private String       accountName;
    private String       companyName;
    private String       currency;
    private BigDecimal   accountBalance;
    private String       driverMobile;
    private long         driverLanguageId;
    private AccountJson  account;
    private BigDecimal   tripFeeAmount;
    private BigDecimal   tripTaxAmount;
    private String       dateMissionStartString;
    private String       dateMissionEndString;
    private String       dateSlotRequestedFromString;
    private String       dateSlotRequestedToString;
    private String       timeSlotRequestedFromString;
    private String       timeSlotRequestedToString;
    private String       dateCreatedString;

    private String       dateSlotApprovedFromString;
    private String       dateSlotApprovedToString;
    private String       timeSlotApprovedFromString;
    private String       timeSlotApprovedToString;

    private String       dateSlotApprovedString;
    private boolean      isEligibleForParkingWithoutPayment;

    private String       getTripResponseCode;
    private Integer      actionType;
    private String       reasonDeny;
    private String       dateApprovedDeniedString;
    private String       operatorName;
    private String       independentPortOperatorName;

    private String       accountCode;
    private int          accountNumber;
    private boolean      isVehicleAddedApi;
    private boolean      isVehicleApproved;
    private boolean      isVehicleActive;
    private boolean      isTripInTransit;
    private boolean      isShowReceiptMessage;
    private boolean      isDirectToPort;
    private boolean      isAllowMultipleEntries;
    private boolean      isVehicleProcessedParkingEntryThroughANPR;
    private boolean      isVehicleAllowedInParkingWithoutPaymentAfterPrematureExit;

    private String       dateParkingEntryString;
    private String       datePortEntryString;
    private int          parkingStatus;

    // used for pagination
    private int          currentPage;
    private int          pageCount;
    private String       sortColumn;
    private boolean      sortAsc;

    public TripJson() {
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMissionCode() {
        return missionCode;
    }

    public void setMissionCode(String missionCode) {
        this.missionCode = missionCode;
    }

    public String getVehicleCode() {
        return vehicleCode;
    }

    public void setVehicleCode(String vehicleCode) {
        this.vehicleCode = vehicleCode;
    }

    public String getDriverCode() {
        return driverCode;
    }

    public void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }

    public List<String> getTripCodes() {
        return tripCodes;
    }

    public void setTripCodes(List<String> tripCodes) {
        this.tripCodes = tripCodes;
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

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isFeePaid() {
        return isFeePaid;
    }

    public void setFeePaid(boolean isFeePaid) {
        this.isFeePaid = isFeePaid;
    }

    public boolean isAdHoc() {
        return isAdHoc;
    }

    public void setAdHoc(boolean isAdHoc) {
        this.isAdHoc = isAdHoc;
    }

    public String getDateSlotString() {
        return dateSlotString;
    }

    public void setDateSlotString(String dateSlotString) {
        this.dateSlotString = dateSlotString;
    }

    public String getTimeSlotString() {
        return timeSlotString;
    }

    public void setTimeSlotString(String timeSlotString) {
        this.timeSlotString = timeSlotString;
    }

    public Integer getPortOperatorId() {
        return portOperatorId;
    }

    public void setPortOperatorId(Integer portOperatorId) {
        this.portOperatorId = portOperatorId;
    }

    public Integer getIndependentPortOperatorId() {
        return independentPortOperatorId;
    }

    public void setIndependentPortOperatorId(Integer independentPortOperatorId) {
        this.independentPortOperatorId = independentPortOperatorId;
    }

    public String getIndependentPortOperatorCode() {
        return independentPortOperatorCode;
    }

    public void setIndependentPortOperatorCode(String independentPortOperatorCode) {
        this.independentPortOperatorCode = independentPortOperatorCode;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getDriverMobile() {
        return driverMobile;
    }

    public void setDriverMobile(String driverMobile) {
        this.driverMobile = driverMobile;
    }

    public long getDriverLanguageId() {
        return driverLanguageId;
    }

    public void setDriverLanguageId(long driverLanguageId) {
        this.driverLanguageId = driverLanguageId;
    }

    public AccountJson getAccount() {
        return account;
    }

    public void setAccount(AccountJson account) {
        this.account = account;
    }

    public BigDecimal getTripFeeAmount() {
        return tripFeeAmount;
    }

    public void setTripFeeAmount(BigDecimal tripFeeAmount) {
        this.tripFeeAmount = tripFeeAmount;
    }

    public BigDecimal getTripTaxAmount() {
        return tripTaxAmount;
    }

    public void setTripTaxAmount(BigDecimal tripTaxAmount) {
        this.tripTaxAmount = tripTaxAmount;
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

    public String getDateSlotRequestedFromString() {
        return dateSlotRequestedFromString;
    }

    public void setDateSlotRequestedFromString(String dateSlotRequestedFromString) {
        this.dateSlotRequestedFromString = dateSlotRequestedFromString;
    }

    public String getDateSlotRequestedToString() {
        return dateSlotRequestedToString;
    }

    public void setDateSlotRequestedToString(String dateSlotRequestedToString) {
        this.dateSlotRequestedToString = dateSlotRequestedToString;
    }

    public String getTimeSlotRequestedFromString() {
        return timeSlotRequestedFromString;
    }

    public void setTimeSlotRequestedFromString(String timeSlotRequestedFromString) {
        this.timeSlotRequestedFromString = timeSlotRequestedFromString;
    }

    public String getTimeSlotRequestedToString() {
        return timeSlotRequestedToString;
    }

    public void setTimeSlotRequestedToString(String timeSlotRequestedToString) {
        this.timeSlotRequestedToString = timeSlotRequestedToString;
    }

    public String getDateCreatedString() {
        return dateCreatedString;
    }

    public void setDateCreatedString(String dateCreatedString) {
        this.dateCreatedString = dateCreatedString;
    }

    public String getDateSlotApprovedFromString() {
        return dateSlotApprovedFromString;
    }

    public void setDateSlotApprovedFromString(String dateSlotApprovedFromString) {
        this.dateSlotApprovedFromString = dateSlotApprovedFromString;
    }

    public String getDateSlotApprovedToString() {
        return dateSlotApprovedToString;
    }

    public void setDateSlotApprovedToString(String dateSlotApprovedToString) {
        this.dateSlotApprovedToString = dateSlotApprovedToString;
    }

    public String getTimeSlotApprovedFromString() {
        return timeSlotApprovedFromString;
    }

    public void setTimeSlotApprovedFromString(String timeSlotApprovedFromString) {
        this.timeSlotApprovedFromString = timeSlotApprovedFromString;
    }

    public String getTimeSlotApprovedToString() {
        return timeSlotApprovedToString;
    }

    public void setTimeSlotApprovedToString(String timeSlotApprovedToString) {
        this.timeSlotApprovedToString = timeSlotApprovedToString;
    }

    public String getDateSlotApprovedString() {
        return dateSlotApprovedString;
    }

    public void setDateSlotApprovedString(String dateSlotApprovedString) {
        this.dateSlotApprovedString = dateSlotApprovedString;
    }

    public boolean getIsEligibleForParkingWithoutPayment() {
        return isEligibleForParkingWithoutPayment;
    }

    public void setIsEligibleForParkingWithoutPayment(boolean isEligibleForParkingWithoutPayment) {
        this.isEligibleForParkingWithoutPayment = isEligibleForParkingWithoutPayment;
    }

    public String getGetTripResponseCode() {
        return getTripResponseCode;
    }

    public void setGetTripResponseCode(String getTripResponseCode) {
        this.getTripResponseCode = getTripResponseCode;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public String getReasonDeny() {
        return reasonDeny;
    }

    public void setReasonDeny(String reasonDeny) {
        this.reasonDeny = reasonDeny;
    }

    public String getDateApprovedDeniedString() {
        return dateApprovedDeniedString;
    }

    public void setDateApprovedDeniedString(String dateApprovedDeniedString) {
        this.dateApprovedDeniedString = dateApprovedDeniedString;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getIndependentPortOperatorName() {
        return independentPortOperatorName;
    }

    public void setIndependentPortOperatorName(String independentPortOperatorName) {
        this.independentPortOperatorName = independentPortOperatorName;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public boolean getIsVehicleAddedApi() {
        return isVehicleAddedApi;
    }

    public void setIsVehicleAddedApi(boolean isVehicleAddedApi) {
        this.isVehicleAddedApi = isVehicleAddedApi;
    }

    public boolean getIsVehicleApproved() {
        return isVehicleApproved;
    }

    public void setIsVehicleApproved(boolean isVehicleApproved) {
        this.isVehicleApproved = isVehicleApproved;
    }

    public boolean getIsVehicleActive() {
        return isVehicleActive;
    }

    public void setIsVehicleActive(boolean isVehicleActive) {
        this.isVehicleActive = isVehicleActive;
    }

    public boolean getIsTripInTransit() {
        return isTripInTransit;
    }

    public void setIsTripInTransit(boolean isTripInTransit) {
        this.isTripInTransit = isTripInTransit;
    }

    public boolean getIsShowReceiptMessage() {
        return isShowReceiptMessage;
    }

    public void setIsShowReceiptMessage(boolean isShowReceiptMessage) {
        this.isShowReceiptMessage = isShowReceiptMessage;
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

    public boolean getIsVehicleProcessedParkingEntryThroughANPR() {
        return isVehicleProcessedParkingEntryThroughANPR;
    }

    public void setIsVehicleProcessedParkingEntryThroughANPR(boolean isVehicleProcessedParkingEntryThroughANPR) {
        this.isVehicleProcessedParkingEntryThroughANPR = isVehicleProcessedParkingEntryThroughANPR;
    }

    public boolean getIsVehicleAllowedInParkingWithoutPaymentAfterPrematureExit() {
        return isVehicleAllowedInParkingWithoutPaymentAfterPrematureExit;
    }

    public void setIsVehicleAllowedInParkingWithoutPaymentAfterPrematureExit(boolean isVehicleAllowedInParkingWithoutPaymentAfterPrematureExit) {
        this.isVehicleAllowedInParkingWithoutPaymentAfterPrematureExit = isVehicleAllowedInParkingWithoutPaymentAfterPrematureExit;
    }

    public String getDateParkingEntryString() {
        return dateParkingEntryString;
    }

    public void setDateParkingEntryString(String dateParkingEntryString) {
        this.dateParkingEntryString = dateParkingEntryString;
    }

    public String getDatePortEntryString() {
        return datePortEntryString;
    }

    public void setDatePortEntryString(String datePortEntryString) {
        this.datePortEntryString = datePortEntryString;
    }

    public int getParkingStatus() {
        return parkingStatus;
    }

    public void setParkingStatus(int parkingStatus) {
        this.parkingStatus = parkingStatus;
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
        builder.append("TripJson [code=");
        builder.append(code);
        builder.append(", missionCode=");
        builder.append(missionCode);
        builder.append(", vehicleCode=");
        builder.append(vehicleCode);
        builder.append(", driverCode=");
        builder.append(driverCode);
        builder.append(", tripCodes=");
        builder.append(tripCodes);
        builder.append(", vehicleRegistration=");
        builder.append(vehicleRegistration);
        builder.append(", vehicleRegistrationCountryISO=");
        builder.append(vehicleRegistrationCountryISO);
        builder.append(", driverName=");
        builder.append(driverName);
        builder.append(", status=");
        builder.append(status);
        builder.append(", isFeePaid=");
        builder.append(isFeePaid);
        builder.append(", isAdHoc=");
        builder.append(isAdHoc);
        builder.append(", dateSlotString=");
        builder.append(dateSlotString);
        builder.append(", timeSlotString=");
        builder.append(timeSlotString);
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
        builder.append(", containerId=");
        builder.append(containerId);
        builder.append(", containerType=");
        builder.append(containerType);
        builder.append(", accountName=");
        builder.append(accountName);
        builder.append(", companyName=");
        builder.append(companyName);
        builder.append(", currency=");
        builder.append(currency);
        builder.append(", accountBalance=");
        builder.append(accountBalance);
        builder.append(", driverMobile=");
        builder.append(driverMobile);
        builder.append(", driverLanguageId=");
        builder.append(driverLanguageId);
        builder.append(", account=");
        builder.append(account);
        builder.append(", tripFeeAmount=");
        builder.append(tripFeeAmount);
        builder.append(", tripTaxAmount=");
        builder.append(tripTaxAmount);
        builder.append(", dateMissionStartString=");
        builder.append(dateMissionStartString);
        builder.append(", dateMissionEndString=");
        builder.append(dateMissionEndString);
        builder.append(", dateSlotRequestedFromString=");
        builder.append(dateSlotRequestedFromString);
        builder.append(", dateSlotRequestedToString=");
        builder.append(dateSlotRequestedToString);
        builder.append(", timeSlotRequestedFromString=");
        builder.append(timeSlotRequestedFromString);
        builder.append(", timeSlotRequestedToString=");
        builder.append(timeSlotRequestedToString);
        builder.append(", dateCreatedString=");
        builder.append(dateCreatedString);
        builder.append(", dateSlotApprovedFromString=");
        builder.append(dateSlotApprovedFromString);
        builder.append(", dateSlotApprovedToString=");
        builder.append(dateSlotApprovedToString);
        builder.append(", timeSlotApprovedFromString=");
        builder.append(timeSlotApprovedFromString);
        builder.append(", timeSlotApprovedToString=");
        builder.append(timeSlotApprovedToString);
        builder.append(", dateSlotApprovedString=");
        builder.append(dateSlotApprovedString);
        builder.append(", isEligibleForParkingWithoutPayment=");
        builder.append(isEligibleForParkingWithoutPayment);
        builder.append(", getTripResponseCode=");
        builder.append(getTripResponseCode);
        builder.append(", actionType=");
        builder.append(actionType);
        builder.append(", reasonDeny=");
        builder.append(reasonDeny);
        builder.append(", dateApprovedDeniedString=");
        builder.append(dateApprovedDeniedString);
        builder.append(", operatorName=");
        builder.append(operatorName);
        builder.append(", accountCode=");
        builder.append(accountCode);
        builder.append(", isVehicleAddedApi=");
        builder.append(isVehicleAddedApi);
        builder.append(", isVehicleApproved=");
        builder.append(isVehicleApproved);
        builder.append(", isVehicleActive=");
        builder.append(isVehicleActive);
        builder.append(", isTripInTransit=");
        builder.append(isTripInTransit);
        builder.append(", isShowReceiptMessage=");
        builder.append(isShowReceiptMessage);
        builder.append(", isDirectToPort=");
        builder.append(isDirectToPort);
        builder.append(", isAllowMultipleEntries=");
        builder.append(isAllowMultipleEntries);
        builder.append(", isVehicleProcessedParkingEntryThroughANPR=");
        builder.append(isVehicleProcessedParkingEntryThroughANPR);
        builder.append(", isVehicleAllowedInParkingWithoutPaymentAfterPrematureExit=");
        builder.append(isVehicleAllowedInParkingWithoutPaymentAfterPrematureExit);
        builder.append(", dateParkingEntryString=");
        builder.append(dateParkingEntryString);
        builder.append(", datePortEntryString=");
        builder.append(datePortEntryString);
        builder.append(", parkingStatus=");
        builder.append(parkingStatus);
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
