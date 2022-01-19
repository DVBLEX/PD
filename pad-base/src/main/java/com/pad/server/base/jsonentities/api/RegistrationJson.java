package com.pad.server.base.jsonentities.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationJson {

    private String  code;
    private String  email;
    private String  firstName;
    private String  lastName;
    private String  password;
    private Integer accountType;
    private String  companyName;
    private String  companyRegistration;
    private String  address1;
    private String  address2;
    private String  address3;
    private String  address4;
    private String  postCode;
    private String  countryCode;
    private String  companyTelephone;
    private String  mobileNumber;
    private String  nationalityCountryISO;
    private boolean specialTaxStatus;
    private String  language;
    private String  token;
    private String  recaptchaResponse;

    public RegistrationJson() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyRegistration() {
        return companyRegistration;
    }

    public void setCompanyRegistration(String companyRegistration) {
        this.companyRegistration = companyRegistration;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCompanyTelephone() {
        return companyTelephone;
    }

    public void setCompanyTelephone(String companyTelephone) {
        this.companyTelephone = companyTelephone;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getNationalityCountryISO() {
        return nationalityCountryISO;
    }

    public void setNationalityCountryISO(String nationalityCountryISO) {
        this.nationalityCountryISO = nationalityCountryISO;
    }

    public boolean isSpecialTaxStatus() {
        return specialTaxStatus;
    }

    public void setSpecialTaxStatus(boolean specialTaxStatus) {
        this.specialTaxStatus = specialTaxStatus;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRecaptchaResponse() {
        return recaptchaResponse;
    }

    public void setRecaptchaResponse(String recaptchaResponse) {
        this.recaptchaResponse = recaptchaResponse;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RegistrationJson [code=");
        builder.append(code);
        builder.append(", email=");
        builder.append(email);
        builder.append(", firstName=");
        builder.append(firstName);
        builder.append(", lastName=");
        builder.append(lastName);
        builder.append(", password=");
        builder.append(password == null ? "null" : "xxxxxx-" + password.length());
        builder.append(", accountType=");
        builder.append(accountType);
        builder.append(", companyName=");
        builder.append(companyName);
        builder.append(", companyRegistration=");
        builder.append(companyRegistration);
        builder.append(", address1=");
        builder.append(address1);
        builder.append(", address2=");
        builder.append(address2);
        builder.append(", address3=");
        builder.append(address3);
        builder.append(", address4=");
        builder.append(address4);
        builder.append(", postCode=");
        builder.append(postCode);
        builder.append(", countryCode=");
        builder.append(countryCode);
        builder.append(", companyTelephone=");
        builder.append(companyTelephone);
        builder.append(", mobileNumber=");
        builder.append(mobileNumber);
        builder.append(", nationalityCountryISO=");
        builder.append(nationalityCountryISO);
        builder.append(", specialTaxStatus=");
        builder.append(specialTaxStatus);
        builder.append(", language=");
        builder.append(language);
        builder.append(", token=");
        builder.append(token);
        builder.append(", recaptchaResponse=");
        builder.append(recaptchaResponse);
        builder.append("]");
        return builder.toString();
    }

}
