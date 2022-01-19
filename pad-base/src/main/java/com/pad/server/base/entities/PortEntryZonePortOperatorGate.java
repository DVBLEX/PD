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
@Table(name = "port_entry_zones_port_operator_gates")
public class PortEntryZonePortOperatorGate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "port_entry_zone_id")
    private long              portEntryZoneId;

    @Column(name = "port_operator_gate_id")
    private long              portOperatorGateId;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PortEntryZonePortOperatorGate [id=");
        builder.append(id);
        builder.append(", portEntryZoneId=");
        builder.append(portEntryZoneId);
        builder.append(", portOperatorGateId=");
        builder.append(portOperatorGateId);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append("]");
        return builder.toString();
    }
}
