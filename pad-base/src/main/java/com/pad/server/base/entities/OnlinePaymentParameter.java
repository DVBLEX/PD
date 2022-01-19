package com.pad.server.base.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "online_payment_parameters")
public class OnlinePaymentParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private long              id;

    @Column(name = "mno_id")
    private long              mnoId;

    @Column(name = "default_connect_timeout")
    private int               defaultConnectTimeout;

    @Column(name = "default_socket_timeout")
    private int               defaultSocketTimeout;

    @Column(name = "default_conn_request_timeout")
    private int               defaultConnRequestTimeout;

    @Column(name = "is_print_receipt")
    private boolean           isPrintReceipt;

    @Column(name = "is_active")
    private boolean           isActive;

    @Column(name = "date_edited")
    private Date              dateEdited;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMnoId() {
        return mnoId;
    }

    public void setMnoId(long mnoId) {
        this.mnoId = mnoId;
    }

    public int getDefaultConnectTimeout() {
        return defaultConnectTimeout;
    }

    public void setDefaultConnectTimeout(int defaultConnectTimeout) {
        this.defaultConnectTimeout = defaultConnectTimeout;
    }

    public int getDefaultSocketTimeout() {
        return defaultSocketTimeout;
    }

    public void setDefaultSocketTimeout(int defaultSocketTimeout) {
        this.defaultSocketTimeout = defaultSocketTimeout;
    }

    public int getDefaultConnRequestTimeout() {
        return defaultConnRequestTimeout;
    }

    public void setDefaultConnRequestTimeout(int defaultConnRequestTimeout) {
        this.defaultConnRequestTimeout = defaultConnRequestTimeout;
    }

    public boolean getIsPrintReceipt() {
        return isPrintReceipt;
    }

    public void setIsPrintReceipt(boolean isPrintReceipt) {
        this.isPrintReceipt = isPrintReceipt;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Date getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(Date dateEdited) {
        this.dateEdited = dateEdited;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnlinePaymentParameter [id=");
        builder.append(id);
        builder.append(", mnoId=");
        builder.append(mnoId);
        builder.append(", defaultConnectTimeout=");
        builder.append(defaultConnectTimeout);
        builder.append(", defaultSocketTimeout=");
        builder.append(defaultSocketTimeout);
        builder.append(", defaultConnRequestTimeout=");
        builder.append(defaultConnRequestTimeout);
        builder.append(", isPrintReceipt=");
        builder.append(isPrintReceipt);
        builder.append(", isActive=");
        builder.append(isActive);
        builder.append(", dateEdited=");
        builder.append(dateEdited);
        builder.append("]");
        return builder.toString();
    }

}
