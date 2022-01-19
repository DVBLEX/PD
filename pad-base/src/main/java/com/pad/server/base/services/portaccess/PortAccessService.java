package com.pad.server.base.services.portaccess;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.pad.server.base.entities.PortAccess;
import com.pad.server.base.entities.PortAccessWhitelist;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.PortAccessJson;
import com.pad.server.base.jsonentities.api.PortAccessWhitelistJson;

public interface PortAccessService {

    public long getPortEntryCount(Integer portOperatorId, String vehicleRegistration, String referenceNumber, Integer transactionType, int status, Date dateEntryFrom,
        Date dateEntryTo);

    public List<PortAccessJson> getPortEntryList(Integer portOperatorId, String vehicleRegistration, String referenceNumber, Integer transactionType, int status,
        Date dateEntryFrom, Date dateEntryTo, String sortColumn, boolean sortAsc, int startLimit, int endLimit) throws SQLException;

    public void exportPortEntryList(List<PortAccessJson> portEntryList, OutputStream os) throws IOException;

    public long getWhitelistCount(Integer portOperatorId, Date dateFrom, Date dateTo, String vehicleRegistration) throws Exception;

    public List<PortAccessWhitelistJson> getWhitelistList(Integer portOperatorId, Date dateFrom, Date dateTo, String vehicleRegistration, String sortColumn, boolean sortAsc,
        int startLimit, int endLimit);

    public void savePortAccess(PortAccess portAccess);

    public void updatePortAccess(PortAccess portAccess);

    public void savePortAccessWhitelist(PortAccessWhitelist portAccessWhitelist);

    public void updatePortAccessWhitelist(PortAccessWhitelist portAccessWhitelist);

    public PortAccess getPortAccessByCode(String code);

    public PortAccess getPortAccessByTripId(long tripId);

    public List<String> getEnteredVehicleRegistrationList();

    public PortAccess getEnteredPortAccessByVehicleRegistration(String vehicleRegistration);

    public boolean isExistWhitlistForDates(Date dateFrom, Date dateTo, String vehicleRegistration);

    public PortAccessWhitelistJson getVehicleWhitelisted(String vehicleRegistration);

    public PortAccessWhitelist getPortAccessWhitelistById(long id);

    public PortAccessWhitelist getPortAccessWhitelistByCode(String code);

    public void deletePortAccessWhitelist(PortAccessWhitelist portAccessWhitelist);

    public long getPortEntryCountByPortOperator(int portOperator);

    public PortAccess processVehiclePortEntry(String tripCode, String parkingCode, long operatorId, long entryLaneId, Date dateEvent, String selectedZone) throws PADException, PADValidationException;

    public PortAccess processWhitelistedVehiclePortEntry(String vehicleRegNumber, long parkingPermissionId, Integer portOperatorId, Long gateId, long operatorId, long entryLaneId,
        Date dateEvent, String selectedZone) throws PADException, PADValidationException;

    public PortAccess processUrgentTripPortEntry(String tripCode, String vehicleRegNumber, long parkingPermissionId, long operatorId, long entryLaneId, Date dateEvent, String selectedZone)
        throws PADException, PADValidationException;

    public PortAccessJson findEnteredVehicleByRegNumber(String vehicleRegistration) throws PADException, PADValidationException;

    public void processVehiclePortExit(String portAccessCode, String vehicleRegistration, long operatorId, long exitLaneId, Date dateEvent)
        throws PADException, PADValidationException;

    public List<PortAccess> getExpiredPortEntries(int minutesSinceDateEntry);

    public PortAccess getLastPortAccessByRegNumber(String vehicleRegistration);

    public boolean isPortEntryAllowedInZone(String zoneName, Long gateId);
}
