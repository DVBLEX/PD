package com.pad.server.base.services.anpr;

import java.util.Date;
import java.util.List;

import com.pad.server.base.entities.AnprEntryLog;
import com.pad.server.base.entities.AnprLog;
import com.pad.server.base.entities.AnprParameter;
import com.pad.server.base.entities.PortAccessWhitelist;
import com.pad.server.base.entities.SystemParameter;
import com.pad.server.base.entities.Trip;

public interface AnprBaseService {

    public AnprParameter getAnprParameter();

    public void updateAnprParameter(AnprParameter anprParameter);

    public SystemParameter getSystemParameter();

    public void updateSystemParameter(SystemParameter systemParameter);

    public void scheduleAnpr(int requestType, long zoneId, Trip trip, PortAccessWhitelist portAccessWhitelist, Date parkingExitDate, Date dateScheduled) throws Exception;

    public AnprLog getAnprLogByRequestTypeAndParkingPermissionIdAndTripId(int requestType, long parkingPermissionId, long tripId);

    public AnprLog getAnprLogByRequestTypeAndVehicleRegAndDateSlot(int requestType, String vehicleRegistration, int hoursBefore, Date dateSlotApproved);

    public List<AnprEntryLog> getUnsuccessfulEventsFromAnprEntryLog(Date dateFrom, Date dateTo);

    public boolean isVehicleProcessedParkingEntryThroughANPRInTheLastHour(Trip trip, long entryLaneId);

    public void updateAnprLog(AnprLog anprLog);

    public void deleteScheduledAnpr(long anprId);

    public String getEntryLaneVideoFeddUrlByLaneNumber(int laneNumber);
}
