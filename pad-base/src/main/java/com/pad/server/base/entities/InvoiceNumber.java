package com.pad.server.base.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "invoice_numbers")
public class InvoiceNumber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "invoice_id")
    private long              invoiceId;

    @Column(name = "number")
    private String            number;

    public long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("InvoiceNumber [invoiceId=");
        builder.append(invoiceId);
        builder.append(", number=");
        builder.append(number);
        builder.append("]");
        return builder.toString();
    }

}
