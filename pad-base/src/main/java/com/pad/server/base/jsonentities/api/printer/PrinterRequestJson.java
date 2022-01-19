package com.pad.server.base.jsonentities.api.printer;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrinterRequestJson {

    private String                 apiClientId;
    private String                 apiSecret;
    private String                 ip;
    private String                 vrn;
    private List<PrinterItemsJson> items;
    private String                 total;
    private String                 money_received;
    private String                 change_given;
    private String                 HT;
    private String                 TVA;
    private String                 TTC;
    private String                 receipt_id;
    private String                 date;
    private String                 time;

    public String getApiClientId() {
        return apiClientId;
    }

    public void setApiClientId(String apiClientId) {
        this.apiClientId = apiClientId;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getVrn() {
        return vrn;
    }

    public void setVrn(String vrn) {
        this.vrn = vrn;
    }

    public List<PrinterItemsJson> getItems() {
        return items;
    }

    public void setItems(List<PrinterItemsJson> items) {
        this.items = items;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMoney_received() {
        return money_received;
    }

    public void setMoney_received(String money_received) {
        this.money_received = money_received;
    }

    public String getChange_given() {
        return change_given;
    }

    public void setChange_given(String change_given) {
        this.change_given = change_given;
    }

    public String getHT() {
        return HT;
    }

    @JsonProperty("HT")
    public void setHT(String hT) {
        HT = hT;
    }

    public String getTVA() {
        return TVA;
    }

    @JsonProperty("TVA")
    public void setTVA(String tVA) {
        TVA = tVA;
    }

    public String getTTC() {
        return TTC;
    }

    @JsonProperty("TTC")
    public void setTTC(String tTC) {
        TTC = tTC;
    }

    public String getReceipt_id() {
        return receipt_id;
    }

    public void setReceipt_id(String receipt_id) {
        this.receipt_id = receipt_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PrinterItemsJson [apiClientId=");
        builder.append(apiClientId);
        builder.append(", apiSecret=");
        builder.append(apiSecret);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", vrn=");
        builder.append(vrn);
        builder.append(", items=");
        builder.append(items);
        builder.append(", total=");
        builder.append(total);
        builder.append(", money_received=");
        builder.append(money_received);
        builder.append(", change_given=");
        builder.append(change_given);
        builder.append(", HT=");
        builder.append(HT);
        builder.append(", TVA=");
        builder.append(TVA);
        builder.append(", TTC=");
        builder.append(TTC);
        builder.append(", receipt_id=");
        builder.append(receipt_id);
        builder.append(", date=");
        builder.append(date);
        builder.append(", time=");
        builder.append(time);
        builder.append("]");
        return builder.toString();
    }

}
