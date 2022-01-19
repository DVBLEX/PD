package com.pad.server.base.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "vehicles_anpr")
public class VehicleAnpr implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "vehicle_registration")
    private String            vehicleRegistration;

    @Column(name = "car_id")
    private long              carId;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    @Column(name = "date_request")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateRequest;

    @Column(name = "date_response")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateResponse;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(Date dateRequest) {
        this.dateRequest = dateRequest;
    }

    public Date getDateResponse() {
        return dateResponse;
    }

    public void setDateResponse(Date dateResponse) {
        this.dateResponse = dateResponse;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("VehicleAnpr [id=");
        builder.append(id);
        builder.append(", vehicleRegistration=");
        builder.append(vehicleRegistration);
        builder.append(", carId=");
        builder.append(carId);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateRequest=");
        builder.append(dateRequest);
        builder.append(", dateResponse=");
        builder.append(dateResponse);
        builder.append("]");
        return builder.toString();
    }

}
