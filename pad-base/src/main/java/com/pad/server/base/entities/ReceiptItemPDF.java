package com.pad.server.base.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author rafael
 *
 */
public class ReceiptItemPDF {

    private String     description;
    private String     quantity;
    private BigDecimal unitaryPrice;
    private BigDecimal tax;
    private BigDecimal total;
    private String     currency;

    public ReceiptItemPDF(String description, String quantity, BigDecimal unitaryPrice, BigDecimal total, String currency) {
        super();
        this.description = description;
        this.quantity = quantity;
        this.total = calculateTax(total);
        this.tax = total.subtract(this.total);
        this.unitaryPrice = unitaryPrice.subtract(this.tax);
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitaryPrice() {
        return unitaryPrice;
    }

    public void setUnitaryPrice(BigDecimal unitaryPrice) {
        this.unitaryPrice = unitaryPrice;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    // 18%
    public BigDecimal calculateTax(BigDecimal bg1) {
        BigDecimal bg2 = new BigDecimal(100);
        BigDecimal bg3 = new BigDecimal(118);
        BigDecimal bg4 = bg1.multiply(bg2);

        return bg4.divide(bg3, 0, RoundingMode.HALF_UP);
    }

    public String getUnitaryPriceString() {
        return unitaryPrice + " " + currency;
    }

    public String getTaxString() {
        return tax + " " + currency;
    }

    public String getTotalString() {
        return total + " " + currency;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ReceiptItemPDF [description=");
        builder.append(description);
        builder.append(", quantity=");
        builder.append(quantity);
        builder.append(", unitaryPrice=");
        builder.append(unitaryPrice);
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
