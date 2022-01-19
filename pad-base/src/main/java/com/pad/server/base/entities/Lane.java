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
@Table(name = "lanes")
public class Lane implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "lane_id")
    private long              laneId;

    @Column(name = "lane_number")
    private int               laneNumber;

    @Column(name = "zone_id")
    private long              zoneId;

    @Column(name = "device_id")
    private String            deviceId;

    @Column(name = "device_name")
    private String            deviceName;

    @Column(name = "allowed_hosts")
    private String            allowedHosts;

    @Column(name = "date_last_request")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateLastRequest;

    @Column(name = "is_active")
    private boolean           isActive;

    @Column(name = "printer_ip")
    private String            printerIp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getAllowedHosts() {
        return allowedHosts;
    }

    public void setAllowedHosts(String allowedHosts) {
        this.allowedHosts = allowedHosts;
    }

    public Date getDateLastRequest() {
        return dateLastRequest;
    }

    public void setDateLastRequest(Date dateLastRequest) {
        this.dateLastRequest = dateLastRequest;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getPrinterIp() {
        return printerIp;
    }

    public void setPrinterIp(String printerIp) {
        this.printerIp = printerIp;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Lane [id=");
        builder.append(id);
        builder.append(", laneId=");
        builder.append(laneId);
        builder.append(", laneNumber=");
        builder.append(laneNumber);
        builder.append(", zoneId=");
        builder.append(zoneId);
        builder.append(", deviceId=");
        builder.append(deviceId);
        builder.append(", deviceName=");
        builder.append(deviceName);
        builder.append(", allowedHosts=");
        builder.append(allowedHosts);
        builder.append(", dateLastRequest=");
        builder.append(dateLastRequest);
        builder.append(", isActive=");
        builder.append(isActive);
        builder.append(", printerIp=");
        builder.append(printerIp);
        builder.append("]");
        return builder.toString();
    }

}
