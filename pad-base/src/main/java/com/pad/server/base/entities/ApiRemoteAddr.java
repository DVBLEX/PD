package com.pad.server.base.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "api_remote_addr")
public class ApiRemoteAddr implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long              id;

    @Column(name = "operator_id")
    private Long              operatorId;

    @Column(name = "remote_addr")
    private String            remoteAddr;

    @Column(name = "is_allowed")
    private boolean           isAllowed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public boolean getIsAllowed() {
        return isAllowed;
    }

    public void setIsAllowed(boolean isAllowed) {
        this.isAllowed = isAllowed;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ApiRemoteAddr [id=");
        builder.append(id);
        builder.append(", operatorId=");
        builder.append(operatorId);
        builder.append(", remoteAddr=");
        builder.append(remoteAddr);
        builder.append(", isAllowed=");
        builder.append(isAllowed);
        builder.append("]");
        return builder.toString();
    }

}
