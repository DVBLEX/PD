package com.pad.server.base.jsonentities.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverJson {

    private String  code;
    private String  firstName;
    private String  lastName;
    private String  email;
    private String  msisdn;
    private String  issuingCountryISO;
    private String  licenceNumber;
    private Long    languageId;

    // used on front-end
    private String  name;
    private String  driverMsisdn;

    // used for pagination
    private int     currentPage;
    private int     pageCount;
    private String  sortColumn;
    private boolean sortAsc;

    public DriverJson() {
    }

    public DriverJson(String code, String name, String driverMsisdn) {
        this.code = code;
        this.name = name;
        this.driverMsisdn = driverMsisdn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getIssuingCountryISO() {
        return issuingCountryISO;
    }

    public void setIssuingCountryISO(String issuingCountryISO) {
        this.issuingCountryISO = issuingCountryISO;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriverMsisdn() {
        return driverMsisdn;
    }

    public void setDriverMsisdn(String driverMsisdn) {
        this.driverMsisdn = driverMsisdn;
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
        builder.append("DriverJson [code=");
        builder.append(code);
        builder.append(", firstName=");
        builder.append(firstName);
        builder.append(", lastName=");
        builder.append(lastName);
        builder.append(", email=");
        builder.append(email);
        builder.append(", msisdn=");
        builder.append(msisdn);
        builder.append(", issuingCountryISO=");
        builder.append(issuingCountryISO);
        builder.append(", licenceNumber=");
        builder.append(licenceNumber);
        builder.append(", languageId=");
        builder.append(languageId);
        builder.append(", name=");
        builder.append(name);
        builder.append(", driverMsisdn=");
        builder.append(driverMsisdn);
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
