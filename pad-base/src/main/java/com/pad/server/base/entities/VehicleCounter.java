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
@Table(name = "vehicle_counter")
public class VehicleCounter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "device_id")
    private String            deviceId;

    @Column(name = "device_name")
    private String            deviceName;

    @Column(name = "lane_id")
    private long              laneId;

    @Column(name = "lane_number")
    private int               laneNumber;

    @Column(name = "zone_id")
    private long              zoneId;

    @Column(name = "session_id")
    private long              sessionId;

    @Column(name = "type")
    private String            type;

    @Column(name = "date_count")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCount;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public long getLaneId() {
        return laneId;
    }

    public void setLaneId(long laneId) {
        this.laneId = laneId;
    }

    public int getLaneNumber() {
        return laneNumber;
    }

    public void setLaneNumber(int laneNumber) {
        this.laneNumber = laneNumber;
    }

    public long getZoneId() {
        return zoneId;
    }

    public void setZoneId(long zoneId) {
        this.zoneId = zoneId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateCount() {
        return dateCount;
    }

    public void setDateCount(Date dateCount) {
        this.dateCount = dateCount;
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
        builder.append("VehicleCounter [id=");
        builder.append(id);
        builder.append(", deviceId=");
        builder.append(deviceId);
        builder.append(", deviceName=");
        builder.append(deviceName);
        builder.append(", laneId=");
        builder.append(laneId);
        builder.append(", laneNumber=");
        builder.append(laneNumber);
        builder.append(", zoneId=");
        builder.append(zoneId);
        builder.append(", sessionId=");
        builder.append(sessionId);
        builder.append(", type=");
        builder.append(type);
        builder.append(", dateCount=");
        builder.append(dateCount);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append("]");
        return builder.toString();
    }

}
