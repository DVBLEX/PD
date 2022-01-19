package com.pad.server.base.jsonentities.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OperatorJson {

    private String  code;

    private String  firstName;
    private String  lastName;
    private String  email;
    private String  msisdn;
    private String  username;
    private Integer roleId;
    private Boolean isActive;
    private Boolean isDeleted;
    private Boolean isLocked;

    private Integer loginFailureCount;

    private Integer portOperatorId;
    private String  independentPortOperatorCode;
    private String  language;
    private Long    languageId;

    private Boolean active;
    private Long    activityId;
    private String  dateFromString;
    private String  dateToString;
    private String  dateLastLoginString;
    private String  dateCreatedString;

    private String  currentPassword;
    private String  password;
    private String  confirmPassword;
    private String  recaptchaResponse;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Integer getLoginFailureCount() {
        return loginFailureCount;
    }

    public void setLoginFailureCount(Integer loginFailureCount) {
        this.loginFailureCount = loginFailureCount;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getDateFromString() {
        return dateFromString;
    }

    public void setDateFromString(String dateFromString) {
        this.dateFromString = dateFromString;
    }

    public String getDateToString() {
        return dateToString;
    }

    public void setDateToString(String dateToString) {
        this.dateToString = dateToString;
    }

    public String getDateLastLoginString() {
        return dateLastLoginString;
    }

    public void setDateLastLoginString(String dateLastLoginString) {
        this.dateLastLoginString = dateLastLoginString;
    }

    public String getDateCreatedString() {
        return dateCreatedString;
    }

    public void setDateCreatedString(String dateCreatedString) {
        this.dateCreatedString = dateCreatedString;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRecaptchaResponse() {
        return recaptchaResponse;
    }

    public void setRecaptchaResponse(String recaptchaResponse) {
        this.recaptchaResponse = recaptchaResponse;
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
        builder.append("OperatorJson [code=");
        builder.append(code);
        builder.append(", firstName=");
        builder.append(firstName);
        builder.append(", lastName=");
        builder.append(lastName);
        builder.append(", email=");
        builder.append(email);
        builder.append(", msisdn=");
        builder.append(msisdn);
        builder.append(", username=");
        builder.append(username);
        builder.append(", roleId=");
        builder.append(roleId);
        builder.append(", isActive=");
        builder.append(isActive);
        builder.append(", isDeleted=");
        builder.append(isDeleted);
        builder.append(", isLocked=");
        builder.append(isLocked);
        builder.append(", loginFailureCount=");
        builder.append(loginFailureCount);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", independentPortOperatorCode=");
        builder.append(independentPortOperatorCode);
        builder.append(", language=");
        builder.append(language);
        builder.append(", languageId=");
        builder.append(languageId);
        builder.append(", active=");
        builder.append(active);
        builder.append(", activityId=");
        builder.append(activityId);
        builder.append(", dateFromString=");
        builder.append(dateFromString);
        builder.append(", dateToString=");
        builder.append(dateToString);
        builder.append(", dateLastLoginString=");
        builder.append(dateLastLoginString);
        builder.append(", dateCreatedString=");
        builder.append(dateCreatedString);
        builder.append(", currentPassword=");
        builder.append(currentPassword == null ? "null" : "xxxxxx-" + currentPassword.length());
        builder.append(", password=");
        builder.append(password == null ? "null" : "xxxxxx-" + password.length());
        builder.append(", confirmPassword=");
        builder.append(confirmPassword == null ? "null" : "xxxxxx-" + confirmPassword.length());
        builder.append(", recaptchaResponse=");
        builder.append(recaptchaResponse);
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
