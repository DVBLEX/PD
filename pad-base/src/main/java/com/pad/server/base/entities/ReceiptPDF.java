package com.pad.server.base.entities;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.pad.server.base.common.ServerUtil;

public class ReceiptPDF {

    private String               number;
    private Date                 dateReceipt;
    private String               typePaymentLabel;
    private BigDecimal           typePaymentAmount;
    private BigDecimal           paidAmount;
    private BigDecimal           changeAmount;
    private String               currency;
    private List<ReceiptItemPDF> items;

    public ReceiptPDF(String number, Date dateReceipt, String typePaymentLabel, BigDecimal typePaymentAmount, BigDecimal paidAmount, BigDecimal changeAmount, String currency,
        List<ReceiptItemPDF> items) {
        super();
        this.number = number;
        this.dateReceipt = dateReceipt;
        this.typePaymentLabel = typePaymentLabel;
        this.typePaymentAmount = typePaymentAmount;
        this.paidAmount = paidAmount;
        this.changeAmount = changeAmount;
        this.currency = currency;
        this.items = items;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDateReceipt() {
        return dateReceipt;
    }

    public void setDateReceipt(Date dateReceipt) {
        this.dateReceipt = dateReceipt;
    }

    public String getTypePaymentLabel() {
        return typePaymentLabel;
    }

    public String getTypePaymentAmount() {
        return typePaymentAmount + " " + currency;
    }

    public String getPaidAmount() {
        return paidAmount + " " + currency;
    }

    public String getChangeAmount() {
        return changeAmount + " " + currency;
    }

    public List<ReceiptItemPDF> getItems() {
        return items;
    }

    public void setItems(List<ReceiptItemPDF> items) {
        this.items = items;
    }

    public String getDateReceiptString() {
        try {
            return ServerUtil.formatDate("dd/MM/yyy HH:mm:ss", dateReceipt);
        } catch (ParseException e) {
            return "";
        }
    }

    public String getTotalHT() {
        BigDecimal total = new BigDecimal(0);

        for (ReceiptItemPDF invoiceItemPDF : this.items) {
            total = total.add(invoiceItemPDF.getTotal());
        }

        return total + " " + currency;
    }

    public String getTotalTVA() {
        BigDecimal total = new BigDecimal(0);

        for (ReceiptItemPDF invoiceItemPDF : this.items) {
            total = total.add(invoiceItemPDF.getTax());
        }

        return total + " " + currency;
    }

    public String getTotalTTC() {
        BigDecimal total = new BigDecimal(0);

        for (ReceiptItemPDF invoiceItemPDF : this.items) {
            total = total.add(invoiceItemPDF.getTax()).add(invoiceItemPDF.getTotal());
        }

        return total + " " + currency;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = new BigDecimal(0);

        for (ReceiptItemPDF invoiceItemPDF : this.items) {
            total = total.add(invoiceItemPDF.getTax()).add(invoiceItemPDF.getTotal());
        }

        return total;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("InvoicePDF [number=");
        builder.append(number);
        builder.append(", dateReceipt=");
        builder.append(dateReceipt);
        builder.append(", currency=");
        builder.append(currency);
        builder.append(", items=");
        builder.append(items);
        builder.append("]");
        return builder.toString();
    }
}
