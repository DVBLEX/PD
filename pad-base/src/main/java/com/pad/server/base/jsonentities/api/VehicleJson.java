package com.pad.server.base.jsonentities.api;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author rafael
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleJson {

    private String  code;
    private String  vehicleRegistration;
    private String  registrationCountryISO;
    private String  make;
    private String  color;
    private boolean isAddedApi;
    private boolean isApproved;
    private boolean isActive;

    // used for pagination
    private int     currentPage;
    private int     pageCount;
    private String  sortColumn;
    private boolean sortAsc;

    public VehicleJson() {
    }

    public VehicleJson(String code, String vehicleRegistration) {
        this.code = code;
        this.vehicleRegistration = vehicleRegistration;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public String getRegistrationCountryISO() {
        return registrationCountryISO;
    }

    public void setRegistrationCountryISO(String registrationCountryISO) {
        this.registrationCountryISO = registrationCountryISO;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean getIsAddedApi() {
        return isAddedApi;
    }

    public void setIsAddedApi(boolean isAddedApi) {
        this.isAddedApi = isAddedApi;
    }

    public boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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
        builder.append("VehicleJson [code=");
        builder.append(code);
        builder.append(", vehicleRegistration=");
        builder.append(vehicleRegistration);
        builder.append(", registrationCountryISO=");
        builder.append(registrationCountryISO);
        builder.append(", make=");
        builder.append(make);
        builder.append(", color=");
        builder.append(color);
        builder.append(", isAddedApi=");
        builder.append(isAddedApi);
        builder.append(", isApproved=");
        builder.append(isApproved);
        builder.append(", isActive=");
        builder.append(isActive);
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
