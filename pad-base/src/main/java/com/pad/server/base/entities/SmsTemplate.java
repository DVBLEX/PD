package com.pad.server.base.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "sms_templates")
public class SmsTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private long              id;

    @Column(name = "type")
    private long              type;

    @Column(name = "name")
    private String            name;

    @Column(name = "language_id")
    private long              languageId;

    @Column(name = "config_id")
    private long              configId;

    @Column(name = "source_addr")
    private String            sourceAddr;

    @Column(name = "message")
    private String            message;

    @Column(name = "variables")
    private String            variables;

    @Column(name = "priority")
    private int               priority;

    @Column(name = "operator_id")
    private long              operatorId;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    @Column(name = "date_edited")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateEdited;

    @Transient
    private String            visitorFirstName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(long languageId) {
        this.languageId = languageId;
    }

    public long getConfigId() {
        return configId;
    }

    public void setConfigId(long configId) {
        this.configId = configId;
    }

    public String getSourceAddr() {
        return sourceAddr;
    }

    public void setSourceAddr(String sourceAddr) {
        this.sourceAddr = sourceAddr;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
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

    public String getVisitorFirstName() {
        return visitorFirstName;
    }

    public void setVisitorFirstName(String visitorFirstName) {
        this.visitorFirstName = visitorFirstName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SmsTemplate [id=");
        builder.append(id);
        builder.append(", name=");
        builder.append(name);
        builder.append(", configId=");
        builder.append(configId);
        builder.append(", sourceAddr=");
        builder.append(sourceAddr);
        builder.append(", message=");
        builder.append(message);
        builder.append(", variables=");
        builder.append(variables);
        builder.append(", priority=");
        builder.append(priority);
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
