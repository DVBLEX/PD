package com.pad.server.base.entities;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.pad.server.base.common.ServerUtil;

public class PDF {

    public enum Type {
        STATEMENT(1, "Relevé de Compte"), INVOICE(2, "Facture"), RECEIPT(3, "Le Reçu");

        private int    id;
        private String label;

        private Type(int id, String label) {
            this.id = id;
            this.label = label;
        }

        public int getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }
    }

    private String        reference;
    private Type          type;
    private Date          date;
    private String        client;
    private Date          datePaymentDue;
    private String        currency;
    private Date          datePeriodStart;
    private Date          datePeriodEnd;
    private BigDecimal    accountBalance;
    private Date          dateAccountBalance;

    // RECEIPTS
    private String        typePaymentLabel;
    private BigDecimal    typePaymentAmount;
    private BigDecimal    paidAmount;
    private BigDecimal    changeAmount;
    private String        vehicleRegNumber;

    private List<PDFItem> items;

    public PDF(String client, Date datePaymentDue, String currency, Date datePeriodStart, Date datePeriodEnd, BigDecimal accountBalance, Date dateAccountBalance,
        List<PDFItem> items) {
        super();
        this.type = Type.INVOICE;
        this.date = new Date();
        this.client = client;
        this.datePaymentDue = datePaymentDue;
        this.currency = currency;
        this.datePeriodStart = datePeriodStart;
        this.datePeriodEnd = datePeriodEnd;
        this.accountBalance = accountBalance;
        this.dateAccountBalance = dateAccountBalance;
        this.items = items;
    }

    public PDF(String client, String currency, Date datePeriodStart, Date datePeriodEnd, BigDecimal accountBalance, Date dateAccountBalance, List<PDFItem> items) {
        super();
        this.type = Type.STATEMENT;
        this.date = new Date();
        this.client = client;
        this.currency = currency;
        this.datePeriodStart = datePeriodStart;
        this.datePeriodEnd = datePeriodEnd;
        this.accountBalance = accountBalance;
        this.dateAccountBalance = dateAccountBalance;
        this.items = items;
    }

    public PDF(String typePaymentLabel, BigDecimal typePaymentAmount, BigDecimal paidAmount, BigDecimal changeAmount, String currency, String vehicleRegNumber,
        List<PDFItem> items) {
        super();
        this.type = Type.RECEIPT;
        this.date = new Date();
        this.typePaymentLabel = typePaymentLabel;
        this.typePaymentAmount = typePaymentAmount;
        this.paidAmount = paidAmount;
        this.changeAmount = changeAmount;
        this.currency = currency;
        this.vehicleRegNumber = vehicleRegNumber;
        this.items = items;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Type getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public String getClient() {
        return client;
    }

    public Date getDatePaymentDue() {
        return datePaymentDue;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public List<PDFItem> getItems() {
        return items;
    }

    public String getDateString() {
        try {
            return ServerUtil.formatDate("dd/MM/yyy", date);
        } catch (ParseException e) {
            return "";
        }
    }

    public String getDateTimeString() {
        try {
            return ServerUtil.formatDate("dd/MM/yyy HH:mm:ss", date);
        } catch (ParseException e) {
            return "";
        }
    }

    public String getDatePaymentDueString() {
        try {
            return ServerUtil.formatDate("dd/MM/yyy", datePaymentDue);
        } catch (ParseException e) {
            return "";
        }
    }

    public String getTotalHT() {
        BigDecimal total = new BigDecimal(0);

        for (PDFItem invoiceItemPDF : this.items) {
            total = total.add(invoiceItemPDF.getTotal());
        }

        return ServerUtil.formatBigDecimal(total) + " " + currency;
    }

    public String getTotalTVA() {
        BigDecimal total = new BigDecimal(0);

        for (PDFItem invoiceItemPDF : this.items) {
            total = total.add(invoiceItemPDF.getTax());
        }

        return ServerUtil.formatBigDecimal(total) + " " + currency;
    }

    public String getTotalTTC() {
        BigDecimal total = new BigDecimal(0);

        for (PDFItem invoiceItemPDF : this.items) {
            total = total.add(invoiceItemPDF.getTax()).add(invoiceItemPDF.getTotal());
        }

        return ServerUtil.formatBigDecimal(total) + " " + currency;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = new BigDecimal(0);

        for (PDFItem invoiceItemPDF : this.items) {
            total = total.add(invoiceItemPDF.getTax()).add(invoiceItemPDF.getTotal());
        }

        return total;
    }

    public String getDateAccountBalanceString() {
        try {
            return ServerUtil.formatDate("dd/MM/yyy HH:mm", dateAccountBalance);
        } catch (ParseException e) {
            return "";
        }
    }

    public String getDatePeriodString() {
        try {
            return ServerUtil.formatDate("dd/MM/yyy", datePeriodStart) + " au " + ServerUtil.formatDate("dd/MM/yyy", datePeriodEnd);
        } catch (ParseException e) {
            return "";
        }
    }

    public String getAccountBalance() {
        try {
            return ServerUtil.formatBigDecimal(accountBalance) + " " + currency;
        } catch (Exception e) {
            return "";
        }
    }

    public String getTypePaymentLabel() {
        return typePaymentLabel;
    }

    public String getTypePaymentAmount() {
        return ServerUtil.formatBigDecimal(typePaymentAmount) + " " + currency;
    }

    public String getPaidAmount() {
        return ServerUtil.formatBigDecimal(paidAmount) + " " + currency;
    }

    public String getChangeAmount() {
        return ServerUtil.formatBigDecimal(changeAmount) + " " + currency;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PDF [reference=");
        builder.append(reference);
        builder.append(", type=");
        builder.append(type);
        builder.append(", date=");
        builder.append(date);
        builder.append(", client=");
        builder.append(client);
        builder.append(", datePaymentDue=");
        builder.append(datePaymentDue);
        builder.append(", currency=");
        builder.append(currency);
        builder.append(", datePeriodStart=");
        builder.append(datePeriodStart);
        builder.append(", datePeriodEnd=");
        builder.append(datePeriodEnd);
        builder.append(", typePaymentLabel=");
        builder.append(typePaymentLabel);
        builder.append(", typePaymentAmount=");
        builder.append(typePaymentAmount);
        builder.append(", paidAmount=");
        builder.append(paidAmount);
        builder.append(", changeAmount=");
        builder.append(changeAmount);
        builder.append(", items=");
        builder.append(items);
        builder.append("]");
        return builder.toString();
    }

}
