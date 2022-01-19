package com.pad.server.base.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "vehicles")
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "code")
    private String            code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id")
    private Account           account;

    @Column(name = "registration_country_iso")
    private String            registrationCountryISO;

    @Column(name = "vehicle_registration")
    private String            vehicleRegistration;

    @Column(name = "make")
    private String            make;

    @Column(name = "color")
    private String            color;

    @Column(name = "operator_id")
    private long              operatorId;

    @Column(name = "is_added_api")
    private boolean           isAddedApi;

    @Column(name = "is_approved")
    private boolean           isApproved;

    @Column(name = "is_active")
    private boolean           isActive;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    @Column(name = "date_edited")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateEdited;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getRegistrationCountryISO() {
        return registrationCountryISO;
    }

    public void setRegistrationCountryISO(String registrationCountryISO) {
        this.registrationCountryISO = registrationCountryISO;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public boolean getIsAddedApi() {
        return isAddedApi;
    }

    public void setIsAddedApi(boolean isAddedApi) {
        this.isAddedApi = isAddedApi;
    }

    public boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
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
        builder.append("Vehicle [id=");
        builder.append(id);
        builder.append(", code=");
        builder.append(code);
        builder.append(", account=");
        builder.append(account == null ? "null" : account.getId());
        builder.append(", registrationCountryISO=");
        builder.append(registrationCountryISO);
        builder.append(", vehicleRegistration=");
        builder.append(vehicleRegistration);
        builder.append(", make=");
        builder.append(make);
        builder.append(", color=");
        builder.append(color);
        builder.append(", operatorId=");
        builder.append(operatorId);
        builder.append(", isAddedApi=");
        builder.append(isAddedApi);
        builder.append(", isApproved=");
        builder.append(isApproved);
        builder.append(", isActive=");
        builder.append(isActive);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateEdited=");
        builder.append(dateEdited);
        builder.append("]");
        return builder.toString();
    }

}
