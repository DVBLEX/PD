package com.pad.server.base.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "port_operator_gates")
public class PortOperatorGate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private long              id;

    @Column(name = "gate_number")
    private String            gateNumber;

    @Column(name = "gate_number_short")
    private String            gateNumberShort;

    @Column(name = "anpr_zone_id")
    private long              anprZoneId;

    @Column(name = "anpr_zone_name")
    private String            anprZoneName;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGateNumber() {
        return gateNumber;
    }

    public void setGateNumber(String gateNumber) {
        this.gateNumber = gateNumber;
    }

    public String getGateNumberShort() {
        return gateNumberShort;
    }

    public void setGateNumberShort(String gateNumberShort) {
        this.gateNumberShort = gateNumberShort;
    }

    public long getAnprZoneId() {
        return anprZoneId;
    }

    public void setAnprZoneId(long anprZoneId) {
        this.anprZoneId = anprZoneId;
    }

    public String getAnprZoneName() {
        return anprZoneName;
    }

    public void setAnprZoneName(String anprZoneName) {
        this.anprZoneName = anprZoneName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PortOperatorGate [id=");
        builder.append(id);
        builder.append(", gateNumber=");
        builder.append(gateNumber);
        builder.append(", gateNumberShort=");
        builder.append(gateNumberShort);
        builder.append(", anprZoneId=");
        builder.append(anprZoneId);
        builder.append(", anprZoneName=");
        builder.append(anprZoneName);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append("]");
        return builder.toString();
    }

}
