package com.pad.server.base.services.parking;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.pad.server.base.entities.AnprEntryScheduler;
import com.pad.server.base.entities.Parking;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ParkingJson;

public interface ParkingService {

    public long getParkingCount(Integer portOperatorId, Integer transactionType, String vehicleRegistration, Integer type, Integer status, Boolean isInTransit, Date dateEntryFrom,
        Date dateEntryTo);

    public List<ParkingJson> getParkingList(Integer portOperatorId, Integer transactionType, String vehicleRegistration, Integer type, Integer status, Boolean isInTransit,
        Date dateEntryFrom, Date dateEntryTo, String sortColumn, boolean sortAsc, int startLimit, int endLimit) throws SQLException;

    public List<Parking> getActiveParkingList(Integer portOperatorId, Integer transactionType, boolean isOrderByDateSmsExit) throws SQLException;

    public void exportParkingList(List<ParkingJson> parkingList, OutputStream os) throws IOException;

    public List<Parking> getInTransitParkingSessionForMoreThanXMinutes(int minutes);

    public long getVehicleExitOnlyCount();

    public long getVehicleInTransitCount();

    public long getParkingCountByPortOperator(int portOpertaor);

    public Parking getParkingByVehicleRegistrationAndStatus(String vehicleRegistration, int parkingStatus);

    public Parking getEnteredParkingByVehicleRegistration(String vehicleRegistration);

    public Parking getExitedParkingByVehicleRegistration(String vehicleRegistration);

    public Parking getParkingSessionByTripId(long tripId);

    public List<String> getEnteredVehicleRegistrationList();

    public List<String> getExitedVehicleRegistrationList();

    public void saveParking(Parking parking);

    public void updateParking(Parking parking);

    public Parking getParkingById(long id);

    public Parking getParkingByCode(String code);

    public void sendExitParkingSms(Trip trip, Parking parking, int addSecondsSchedule) throws PADException;

    public ParkingJson getParkingSessionByVehicleReg(String vehicleRegistration) throws PADException;

    public AnprEntryScheduler processVehicleExit(AnprEntryScheduler anprEntryScheduler, String parkingCode, long parkingPermissionId, String vehicleRegistration, long operatorId,
        long exitLaneId, Date dateEvent) throws PADException;

    public List<ParkingJson> findExitedVehicleByRegNumber(String vehicleRegistration) throws PADException, PADValidationException;

    public String processVehicleEntry(String tripCode, long operatorId, long entryLaneId, Date dateEvent) throws PADException, PADValidationException;

    public int getVehicleReleasedCountInLastXMinutes(Long portOperatorId, Integer transactionType, Date dateTodayMinusXMinutes) throws SQLException;
}
