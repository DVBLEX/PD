package com.pad.server.base.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "port_operators")
public class PortOperator implements Serializable {

    private static final long                 serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private long                              id;

    @Column(name = "name")
    private String                            name;

    @Column(name = "name_short")
    private String                            nameShort;

    @Column(name = "is_active")
    private boolean                           isActive;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date                              dateCreated;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "portOperator", cascade = CascadeType.PERSIST)
    private List<PortOperatorTransactionType> portOperatorTransactionTypesList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
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

    public List<PortOperatorTransactionType> getPortOperatorTransactionTypesList() {
        return portOperatorTransactionTypesList;
    }

    public void setPortOperatorTransactionTypesList(List<PortOperatorTransactionType> portOperatorTransactionTypesList) {
        this.portOperatorTransactionTypesList = portOperatorTransactionTypesList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PortOperator [id=");
        builder.append(id);
        builder.append(", name=");
        builder.append(name);
        builder.append(", nameShort=");
        builder.append(nameShort);
        builder.append(", isActive=");
        builder.append(isActive);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", portOperatorTransactionTypesList=");
        builder.append(portOperatorTransactionTypesList == null ? "null" : portOperatorTransactionTypesList.size());
        builder.append("]");
        return builder.toString();
    }

}
