package com.pad.server.base.jsonentities.api.printer;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrinterItemsJson {

    private String amount;
    private String description;

    public PrinterItemsJson(String amount, String description) {
        super();
        this.amount = amount;
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PrinterRequestJson [amount=");
        builder.append(amount);
        builder.append(", description=");
        builder.append(description);
        builder.append("]");
        return builder.toString();
    }
}
