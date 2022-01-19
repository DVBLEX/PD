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
@Table(name = "port_access_whitelist")
public class PortAccessWhitelist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "code")
    private String            code;

    @Column(name = "status")
    private int               status;

    @Column(name = "port_operator_id")
    private long              portOperatorId;

    @Column(name = "port_operator_gate_id")
    private long              portOperatorGateId;

    @Column(name = "date_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateFrom;

    @Column(name = "date_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateTo;

    @Column(name = "vehicle_registration")
    private String            vehicleRegistration;

    @Column(name = "parking_permission_id")
    private long              parkingPermissionId;

    @Column(name = "operator_id")
    private long              operatorId;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getPortOperatorId() {
        return portOperatorId;
    }

    public void setPortOperatorId(long portOperatorId) {
        this.portOperatorId = portOperatorId;
    }

    public long getPortOperatorGateId() {
        return portOperatorGateId;
    }

    public void setPortOperatorGateId(long portOperatorGateId) {
        this.portOperatorGateId = portOperatorGateId;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public long getParkingPermissionId() {
        return parkingPermissionId;
    }

    public void setParkingPermissionId(long parkingPermissionId) {
        this.parkingPermissionId = parkingPermissionId;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
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
        builder.append("PortAccessWhitelist [id=");
        builder.append(id);
        builder.append(", code=");
        builder.append(code);
        builder.append(", status=");
        builder.append(status);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", portOperatorGateId=");
        builder.append(portOperatorGateId);
        builder.append(", dateFrom=");
        builder.append(dateFrom);
        builder.append(", dateTo=");
        builder.append(dateTo);
        builder.append(", vehicleRegistration=");
        builder.append(vehicleRegistration);
        builder.append(", parkingPermissionId=");
        builder.append(parkingPermissionId);
        builder.append(", operatorId=");
        builder.append(operatorId);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateEdited=");
        builder.append(dateEdited);
        builder.append("]");
        return builder.toString();
    }

}
