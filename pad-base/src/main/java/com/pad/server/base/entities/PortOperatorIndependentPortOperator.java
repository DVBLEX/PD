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
@Table(name = "port_operator_independent_port_operator")
public class PortOperatorIndependentPortOperator implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "port_operator_id")
    private long              portOperatorId;

    @Column(name = "independent_port_operator_id")
    private long              independentPortOperatorId;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPortOperatorId() {
        return portOperatorId;
    }

    public void setPortOperatorId(long portOperatorId) {
        this.portOperatorId = portOperatorId;
    }

    public long getIndependentPortOperatorId() {
        return independentPortOperatorId;
    }

    public void setIndependentPortOperatorId(long independentPortOperatorId) {
        this.independentPortOperatorId = independentPortOperatorId;
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
        builder.append("PortOperatorIndependentPortOperator [id=");
        builder.append(id);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", independentPortOperatorId=");
        builder.append(independentPortOperatorId);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append("]");
        return builder.toString();
    }
}
