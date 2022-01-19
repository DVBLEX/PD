package com.pad.server.base.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "anpr_parameters")
public class AnprParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private long              id;

    @Column(name = "last_entry_log_id")
    private long              lastEntryLogId;

    @Column(name = "entry_log_page_size")
    private int               entryLogPageSize;

    @Column(name = "entry_log_connect_timeout")
    private int               entryLogConnectTimeout;

    @Column(name = "entry_log_socket_timeout")
    private int               entryLogSocketTimeout;

    @Column(name = "entry_log_conn_request_timeout")
    private int               entryLogConnRequestTimeout;

    @Column(name = "default_connect_timeout")
    private int               defaultConnectTimeout;

    @Column(name = "default_socket_timeout")
    private int               defaultSocketTimeout;

    @Column(name = "default_conn_request_timeout")
    private int               defaultConnRequestTimeout;

    @Column(name = "anpr_zone_id_outside")
    private long              anprZoneIdOutside;

    @Column(name = "anpr_zone_id_agsparking")
    private long              anprZoneIdAgsparking;

    @Column(name = "anpr_zone_id_mole2")
    private long              anprZoneIdMole2;

    @Column(name = "anpr_zone_id_mole4")
    private long              anprZoneIdMole4;

    @Column(name = "anpr_zone_id_mole8")
    private long              anprZoneIdMole8;

    @Column(name = "agsparking_anpr_entry_lane1_id")
    private long              agsparkingAnprEntryLane1Id;

    @Column(name = "agsparking_anpr_entry_lane2_id")
    private long              agsparkingAnprEntryLane2Id;

    @Column(name = "agsparking_anpr_entry_lane3_id")
    private long              agsparkingAnprEntryLane3Id;

    @Column(name = "agsparking_anpr_entry_lane4_id")
    private long              agsparkingAnprEntryLane4Id;

    @Column(name = "agsparking_anpr_entry_lane5_id")
    private long              agsparkingAnprEntryLane5Id;

    @Column(name = "agsparking_anpr_exit_lane6_id")
    private long              agsparkingAnprExitLane6Id;

    @Column(name = "agsparking_anpr_exit_lane7_id")
    private long              agsparkingAnprExitLane7Id;

    @Column(name = "port_anpr_entry_gate_mole2_id")
    private long              portAnprEntryGateMole2Id;

    @Column(name = "port_anpr_entry_gate_mole3_id")
    private long              portAnprEntryGateMole3Id;

    @Column(name = "port_anpr_entry_gate_mole4_id")
    private long              portAnprEntryGateMole4Id;

    @Column(name = "port_anpr_entry_gate_mole8_id")
    private long              portAnprEntryGateMole8Id;

    @Column(name = "port_anpr_exit_gate_mole1_id")
    private long              portAnprExitGateMole1Id;

    @Column(name = "port_anpr_exit_gate_mole4_id")
    private long              portAnprExitGateMole4Id;

    @Column(name = "port_anpr_exit_gate_mole8_id")
    private long              portAnprExitGateMole8Id;

    @Column(name = "parking_permission_hours_in_future")
    private int               parkingPermissionHoursInFuture;

    @Column(name = "parking_permission_hours_prior_slot_date")
    private int               parkingPermissionHoursPriorSlotDate;

    @Column(name = "parking_permission_hours_after_slot_date")
    private int               parkingPermissionHoursAfterSlotDate;

    @Column(name = "parking_permission_hours_after_exit_date")
    private int               parkingPermissionHoursAfterExitDate;

    @Column(name = "bt_downtime_seconds_limit")
    private int               btDowntimeSecondsLimit;

    @Column(name = "bt_uptime_seconds_limit")
    private int               btUptimeSecondsLimit;

    @Column(name = "is_iis_server_enabled")
    private boolean           isIISServerEnabled;

    @Column(name = "iis_server_state")
    private String            iISServerState;

    @Column(name = "agsparking_entry_lane1_video_feed_url")
    private String            agsparkingEntryLane1VideoFeedUrl;

    @Column(name = "agsparking_entry_lane2_video_feed_url")
    private String            agsparkingEntryLane2VideoFeedUrl;

    @Column(name = "agsparking_entry_lane3_video_feed_url")
    private String            agsparkingEntryLane3VideoFeedUrl;

    @Column(name = "agsparking_entry_lane4_video_feed_url")
    private String            agsparkingEntryLane4VideoFeedUrl;

    @Column(name = "agsparking_entry_lane5_video_feed_url")
    private String            agsparkingEntryLane5VideoFeedUrl;

    @Column(name = "date_edited")
    private Date              dateEdited;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLastEntryLogId() {
        return lastEntryLogId;
    }

    public void setLastEntryLogId(long lastEntryLogId) {
        this.lastEntryLogId = lastEntryLogId;
    }

    public int getEntryLogPageSize() {
        return entryLogPageSize;
    }

    public void setEntryLogPageSize(int entryLogPageSize) {
        this.entryLogPageSize = entryLogPageSize;
    }

    public int getEntryLogConnectTimeout() {
        return entryLogConnectTimeout;
    }

    public void setEntryLogConnectTimeout(int entryLogConnectTimeout) {
        this.entryLogConnectTimeout = entryLogConnectTimeout;
    }

    public int getEntryLogSocketTimeout() {
        return entryLogSocketTimeout;
    }

    public void setEntryLogSocketTimeout(int entryLogSocketTimeout) {
        this.entryLogSocketTimeout = entryLogSocketTimeout;
    }

    public int getEntryLogConnRequestTimeout() {
        return entryLogConnRequestTimeout;
    }

    public void setEntryLogConnRequestTimeout(int entryLogConnRequestTimeout) {
        this.entryLogConnRequestTimeout = entryLogConnRequestTimeout;
    }

    public int getDefaultConnectTimeout() {
        return defaultConnectTimeout;
    }

    public void setDefaultConnectTimeout(int defaultConnectTimeout) {
        this.defaultConnectTimeout = defaultConnectTimeout;
    }

    public int getDefaultSocketTimeout() {
        return defaultSocketTimeout;
    }

    public void setDefaultSocketTimeout(int defaultSocketTimeout) {
        this.defaultSocketTimeout = defaultSocketTimeout;
    }

    public int getDefaultConnRequestTimeout() {
        return defaultConnRequestTimeout;
    }

    public void setDefaultConnRequestTimeout(int defaultConnRequestTimeout) {
        this.defaultConnRequestTimeout = defaultConnRequestTimeout;
    }

    public long getAnprZoneIdOutside() {
        return anprZoneIdOutside;
    }

    public void setAnprZoneIdOutside(long anprZoneIdOutside) {
        this.anprZoneIdOutside = anprZoneIdOutside;
    }

    public long getAnprZoneIdAgsparking() {
        return anprZoneIdAgsparking;
    }

    public void setAnprZoneIdAgsparking(long anprZoneIdAgsparking) {
        this.anprZoneIdAgsparking = anprZoneIdAgsparking;
    }

    public long getAnprZoneIdMole2() {
        return anprZoneIdMole2;
    }

    public void setAnprZoneIdMole2(long anprZoneIdMole2) {
        this.anprZoneIdMole2 = anprZoneIdMole2;
    }

    public long getAnprZoneIdMole4() {
        return anprZoneIdMole4;
    }

    public void setAnprZoneIdMole4(long anprZoneIdMole4) {
        this.anprZoneIdMole4 = anprZoneIdMole4;
    }

    public long getAnprZoneIdMole8() {
        return anprZoneIdMole8;
    }

    public void setAnprZoneIdMole8(long anprZoneIdMole8) {
        this.anprZoneIdMole8 = anprZoneIdMole8;
    }

    public long getAgsparkingAnprEntryLane1Id() {
        return agsparkingAnprEntryLane1Id;
    }

    public void setAgsparkingAnprEntryLane1Id(long agsparkingAnprEntryLane1Id) {
        this.agsparkingAnprEntryLane1Id = agsparkingAnprEntryLane1Id;
    }

    public long getAgsparkingAnprEntryLane2Id() {
        return agsparkingAnprEntryLane2Id;
    }

    public void setAgsparkingAnprEntryLane2Id(long agsparkingAnprEntryLane2Id) {
        this.agsparkingAnprEntryLane2Id = agsparkingAnprEntryLane2Id;
    }

    public long getAgsparkingAnprEntryLane3Id() {
        return agsparkingAnprEntryLane3Id;
    }

    public void setAgsparkingAnprEntryLane3Id(long agsparkingAnprEntryLane3Id) {
        this.agsparkingAnprEntryLane3Id = agsparkingAnprEntryLane3Id;
    }

    public long getAgsparkingAnprEntryLane4Id() {
        return agsparkingAnprEntryLane4Id;
    }

    public void setAgsparkingAnprEntryLane4Id(long agsparkingAnprEntryLane4Id) {
        this.agsparkingAnprEntryLane4Id = agsparkingAnprEntryLane4Id;
    }

    public long getAgsparkingAnprEntryLane5Id() {
        return agsparkingAnprEntryLane5Id;
    }

    public void setAgsparkingAnprEntryLane5Id(long agsparkingAnprEntryLane5Id) {
        this.agsparkingAnprEntryLane5Id = agsparkingAnprEntryLane5Id;
    }

    public long getAgsparkingAnprExitLane6Id() {
        return agsparkingAnprExitLane6Id;
    }

    public void setAgsparkingAnprExitLane6Id(long agsparkingAnprExitLane6Id) {
        this.agsparkingAnprExitLane6Id = agsparkingAnprExitLane6Id;
    }

    public long getAgsparkingAnprExitLane7Id() {
        return agsparkingAnprExitLane7Id;
    }

    public void setAgsparkingAnprExitLane7Id(long agsparkingAnprExitLane7Id) {
        this.agsparkingAnprExitLane7Id = agsparkingAnprExitLane7Id;
    }

    public long getPortAnprEntryGateMole2Id() {
        return portAnprEntryGateMole2Id;
    }

    public void setPortAnprEntryGateMole2Id(long portAnprEntryGateMole2Id) {
        this.portAnprEntryGateMole2Id = portAnprEntryGateMole2Id;
    }

    public long getPortAnprEntryGateMole3Id() {
        return portAnprEntryGateMole3Id;
    }

    public void setPortAnprEntryGateMole3Id(long portAnprEntryGateMole3Id) {
        this.portAnprEntryGateMole3Id = portAnprEntryGateMole3Id;
    }

    public long getPortAnprEntryGateMole4Id() {
        return portAnprEntryGateMole4Id;
    }

    public void setPortAnprEntryGateMole4Id(long portAnprEntryGateMole4Id) {
        this.portAnprEntryGateMole4Id = portAnprEntryGateMole4Id;
    }

    public long getPortAnprEntryGateMole8Id() {
        return portAnprEntryGateMole8Id;
    }

    public void setPortAnprEntryGateMole8Id(long portAnprEntryGateMole8Id) {
        this.portAnprEntryGateMole8Id = portAnprEntryGateMole8Id;
    }

    public long getPortAnprExitGateMole1Id() {
        return portAnprExitGateMole1Id;
    }

    public void setPortAnprExitGateMole1Id(long portAnprExitGateMole1Id) {
        this.portAnprExitGateMole1Id = portAnprExitGateMole1Id;
    }

    public long getPortAnprExitGateMole4Id() {
        return portAnprExitGateMole4Id;
    }

    public void setPortAnprExitGateMole4Id(long portAnprExitGateMole4Id) {
        this.portAnprExitGateMole4Id = portAnprExitGateMole4Id;
    }

    public long getPortAnprExitGateMole8Id() {
        return portAnprExitGateMole8Id;
    }

    public void setPortAnprExitGateMole8Id(long portAnprExitGateMole8Id) {
        this.portAnprExitGateMole8Id = portAnprExitGateMole8Id;
    }

    public int getParkingPermissionHoursInFuture() {
        return parkingPermissionHoursInFuture;
    }

    public void setParkingPermissionHoursInFuture(int parkingPermissionHoursInFuture) {
        this.parkingPermissionHoursInFuture = parkingPermissionHoursInFuture;
    }

    public int getParkingPermissionHoursPriorSlotDate() {
        return parkingPermissionHoursPriorSlotDate;
    }

    public void setParkingPermissionHoursPriorSlotDate(int parkingPermissionHoursPriorSlotDate) {
        this.parkingPermissionHoursPriorSlotDate = parkingPermissionHoursPriorSlotDate;
    }

    public int getParkingPermissionHoursAfterSlotDate() {
        return parkingPermissionHoursAfterSlotDate;
    }

    public void setParkingPermissionHoursAfterSlotDate(int parkingPermissionHoursAfterSlotDate) {
        this.parkingPermissionHoursAfterSlotDate = parkingPermissionHoursAfterSlotDate;
    }

    public int getParkingPermissionHoursAfterExitDate() {
        return parkingPermissionHoursAfterExitDate;
    }

    public void setParkingPermissionHoursAfterExitDate(int parkingPermissionHoursAfterExitDate) {
        this.parkingPermissionHoursAfterExitDate = parkingPermissionHoursAfterExitDate;
    }

    public int getBtDowntimeSecondsLimit() {
        return btDowntimeSecondsLimit;
    }

    public void setBtDowntimeSecondsLimit(int btDowntimeSecondsLimit) {
        this.btDowntimeSecondsLimit = btDowntimeSecondsLimit;
    }

    public int getBtUptimeSecondsLimit() {
        return btUptimeSecondsLimit;
    }

    public void setBtUptimeSecondsLimit(int btUptimeSecondsLimit) {
        this.btUptimeSecondsLimit = btUptimeSecondsLimit;
    }

    public boolean getIsIISServerEnabled() {
        return isIISServerEnabled;
    }

    public void setIsIISServerEnabled(boolean isIISServerEnabled) {
        this.isIISServerEnabled = isIISServerEnabled;
    }

    public String getiISServerState() {
        return iISServerState;
    }

    public void setiISServerState(String iISServerState) {
        this.iISServerState = iISServerState;
    }

    public String getAgsparkingEntryLane1VideoFeedUrl() {
        return agsparkingEntryLane1VideoFeedUrl;
    }

    public void setAgsparkingEntryLane1VideoFeedUrl(String agsparkingEntryLane1VideoFeedUrl) {
        this.agsparkingEntryLane1VideoFeedUrl = agsparkingEntryLane1VideoFeedUrl;
    }

    public String getAgsparkingEntryLane2VideoFeedUrl() {
        return agsparkingEntryLane2VideoFeedUrl;
    }

    public void setAgsparkingEntryLane2VideoFeedUrl(String agsparkingEntryLane2VideoFeedUrl) {
        this.agsparkingEntryLane2VideoFeedUrl = agsparkingEntryLane2VideoFeedUrl;
    }

    public String getAgsparkingEntryLane3VideoFeedUrl() {
        return agsparkingEntryLane3VideoFeedUrl;
    }

    public void setAgsparkingEntryLane3VideoFeedUrl(String agsparkingEntryLane3VideoFeedUrl) {
        this.agsparkingEntryLane3VideoFeedUrl = agsparkingEntryLane3VideoFeedUrl;
    }

    public String getAgsparkingEntryLane4VideoFeedUrl() {
        return agsparkingEntryLane4VideoFeedUrl;
    }

    public void setAgsparkingEntryLane4VideoFeedUrl(String agsparkingEntryLane4VideoFeedUrl) {
        this.agsparkingEntryLane4VideoFeedUrl = agsparkingEntryLane4VideoFeedUrl;
    }

    public String getAgsparkingEntryLane5VideoFeedUrl() {
        return agsparkingEntryLane5VideoFeedUrl;
    }

    public void setAgsparkingEntryLane5VideoFeedUrl(String agsparkingEntryLane5VideoFeedUrl) {
        this.agsparkingEntryLane5VideoFeedUrl = agsparkingEntryLane5VideoFeedUrl;
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
        builder.append("AnprParameter [id=");
        builder.append(id);
        builder.append(", lastEntryLogId=");
        builder.append(lastEntryLogId);
        builder.append(", entryLogPageSize=");
        builder.append(entryLogPageSize);
        builder.append(", entryLogConnectTimeout=");
        builder.append(entryLogConnectTimeout);
        builder.append(", entryLogSocketTimeout=");
        builder.append(entryLogSocketTimeout);
        builder.append(", entryLogConnRequestTimeout=");
        builder.append(entryLogConnRequestTimeout);
        builder.append(", defaultConnectTimeout=");
        builder.append(defaultConnectTimeout);
        builder.append(", defaultSocketTimeout=");
        builder.append(defaultSocketTimeout);
        builder.append(", defaultConnRequestTimeout=");
        builder.append(defaultConnRequestTimeout);
        builder.append(", anprZoneIdOutside=");
        builder.append(anprZoneIdOutside);
        builder.append(", anprZoneIdAgsparking=");
        builder.append(anprZoneIdAgsparking);
        builder.append(", anprZoneIdMole2=");
        builder.append(anprZoneIdMole2);
        builder.append(", anprZoneIdMole4=");
        builder.append(anprZoneIdMole4);
        builder.append(", anprZoneIdMole8=");
        builder.append(anprZoneIdMole8);
        builder.append(", agsparkingAnprEntryLane1Id=");
        builder.append(agsparkingAnprEntryLane1Id);
        builder.append(", agsparkingAnprEntryLane2Id=");
        builder.append(agsparkingAnprEntryLane2Id);
        builder.append(", agsparkingAnprEntryLane3Id=");
        builder.append(agsparkingAnprEntryLane3Id);
        builder.append(", agsparkingAnprEntryLane4Id=");
        builder.append(agsparkingAnprEntryLane4Id);
        builder.append(", agsparkingAnprEntryLane5Id=");
        builder.append(agsparkingAnprEntryLane5Id);
        builder.append(", agsparkingAnprExitLane6Id=");
        builder.append(agsparkingAnprExitLane6Id);
        builder.append(", agsparkingAnprExitLane7Id=");
        builder.append(agsparkingAnprExitLane7Id);
        builder.append(", portAnprEntryGateMole2Id=");
        builder.append(portAnprEntryGateMole2Id);
        builder.append(", portAnprEntryGateMole3Id=");
        builder.append(portAnprEntryGateMole3Id);
        builder.append(", portAnprEntryGateMole4Id=");
        builder.append(portAnprEntryGateMole4Id);
        builder.append(", portAnprEntryGateMole8Id=");
        builder.append(portAnprEntryGateMole8Id);
        builder.append(", portAnprExitGateMole1Id=");
        builder.append(portAnprExitGateMole1Id);
        builder.append(", portAnprExitGateMole4Id=");
        builder.append(portAnprExitGateMole4Id);
        builder.append(", portAnprExitGateMole8Id=");
        builder.append(portAnprExitGateMole8Id);
        builder.append(", parkingPermissionHoursInFuture=");
        builder.append(parkingPermissionHoursInFuture);
        builder.append(", parkingPermissionHoursPriorSlotDate=");
        builder.append(parkingPermissionHoursPriorSlotDate);
        builder.append(", parkingPermissionHoursAfterSlotDate=");
        builder.append(parkingPermissionHoursAfterSlotDate);
        builder.append(", parkingPermissionHoursAfterExitDate=");
        builder.append(parkingPermissionHoursAfterExitDate);
        builder.append(", btDowntimeSecondsLimit=");
        builder.append(btDowntimeSecondsLimit);
        builder.append(", btUptimeSecondsLimit=");
        builder.append(btUptimeSecondsLimit);
        builder.append(", isIISServerEnabled=");
        builder.append(isIISServerEnabled);
        builder.append(", iISServerState=");
        builder.append(iISServerState);
        builder.append(", agsparkingEntryLane1VideoFeedUrl=");
        builder.append(agsparkingEntryLane1VideoFeedUrl);
        builder.append(", agsparkingEntryLane2VideoFeedUrl=");
        builder.append(agsparkingEntryLane2VideoFeedUrl);
        builder.append(", agsparkingEntryLane3VideoFeedUrl=");
        builder.append(agsparkingEntryLane3VideoFeedUrl);
        builder.append(", agsparkingEntryLane4VideoFeedUrl=");
        builder.append(agsparkingEntryLane4VideoFeedUrl);
        builder.append(", agsparkingEntryLane5VideoFeedUrl=");
        builder.append(agsparkingEntryLane5VideoFeedUrl);
        builder.append(", dateEdited=");
        builder.append(dateEdited);
        builder.append("]");
        return builder.toString();
    }

}
