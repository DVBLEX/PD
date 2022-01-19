package com.pad.server.base.jsonentities.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginJson {

    private String input1;
    private String input2;
    private String input3;
    private String input4;
    private String input5;
    private String input6;
    private String language;
    private String recaptchaResponse;
    private String accountType;

    public String getInput1() {
        return input1;
    }

    public void setInput1(String input1) {
        this.input1 = input1;
    }

    public String getInput2() {
        return input2;
    }

    public void setInput2(String input2) {
        this.input2 = input2;
    }

    public String getInput3() {
        return input3;
    }

    public void setInput3(String input3) {
        this.input3 = input3;
    }

    public String getInput4() {
        return input4;
    }

    public void setInput4(String input4) {
        this.input4 = input4;
    }

    public String getInput5() {
        return input5;
    }

    public String getInput6() {
        return input6;
    }

    public void setInput6(String input6) {
        this.input6 = input6;
    }

    public void setInput5(String input5) {
        this.input5 = input5;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRecaptchaResponse() {
        return recaptchaResponse;
    }

    public void setRecaptchaResponse(String recaptchaResponse) {
        this.recaptchaResponse = recaptchaResponse;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LoginJson [input1=");
        builder.append(input1);
        builder.append(", input2=");
        builder.append(input2 == null ? "null" : "xxxxxx-" + input2.length());
        builder.append(", input3=");
        builder.append(input3 == null ? "null" : "xxxxxx-" + input3.length());
        builder.append(", input4=");
        builder.append(input4 == null ? "null" : "xxxxxx-" + input4.length());
        builder.append(", input5=");
        builder.append(input5);
        builder.append(", input6=");
        builder.append(input6);
        builder.append(", language=");
        builder.append(language);
        builder.append(", recaptchaResponse=");
        builder.append(recaptchaResponse);
        builder.append(", accountType=");
        builder.append(accountType);
        builder.append("]");
        return builder.toString();
    }

}
