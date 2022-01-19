package com.pad.server.base.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "receipt_numbers")
public class ReceiptNumber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "receipt_id")
    private long              receiptId;

    @Column(name = "number")
    private String            number;

    public long getReceiptid() {
        return receiptId;
    }

    public void setReceiptid(long receiptId) {
        this.receiptId = receiptId;
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
        builder.append("ReceiptNumber [receiptId=");
        builder.append(receiptId);
        builder.append(", number=");
        builder.append(number);
        builder.append("]");
        return builder.toString();
    }

}
