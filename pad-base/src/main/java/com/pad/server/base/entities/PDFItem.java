package com.pad.server.base.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.pad.server.base.common.ServerUtil;

public class PDFItem {

    private String     description;
    private String     vehicleRegCountryISO;
    private String     quantity;
    private BigDecimal total;
    private BigDecimal tax;
    private String     currency;

    public PDFItem(String description, String vehicleRegCountryISO, String quantity, BigDecimal total, String currency) {
        super();
        this.description = description;
        this.vehicleRegCountryISO = vehicleRegCountryISO;
        this.quantity = quantity;
        this.total = calculateTax(total);
        this.tax = total.subtract(this.total);
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public String getVehicleRegCountryISO() {
        return vehicleRegCountryISO;
    }

    public String getQuantity() {
        return quantity;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getCurrency() {
        return currency;
    }

    // 18%
    public BigDecimal calculateTax(BigDecimal bg1) {

        BigDecimal bg2 = new BigDecimal(100);
        BigDecimal bg3 = new BigDecimal(118);
        BigDecimal bg4 = bg1.multiply(bg2);

        return bg4.divide(bg3, 0, RoundingMode.HALF_UP);
    }

    public String getTaxString() {
        return ServerUtil.formatBigDecimal(tax) + " " + currency;
    }

    public String getTotalString() {
        return ServerUtil.formatBigDecimal(total) + " " + currency;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("InvoiceItemPDF [description=");
        builder.append(description);
        builder.append(", vehicleRegCountryISO=");
        builder.append(vehicleRegCountryISO);
        builder.append(", quantity=");
        builder.append(quantity);
        builder.append(", tax=");
        builder.append(tax);
        builder.append(", total=");
        builder.append(total);
        builder.append(", currency=");
        builder.append(currency);
        builder.append("]");
        return builder.toString();
    }
}
