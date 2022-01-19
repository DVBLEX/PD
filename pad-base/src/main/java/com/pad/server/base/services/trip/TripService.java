package com.pad.server.base.services.trip;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.entities.PortOperatorTransactionType;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.TripJson;

public interface TripService {

    public List<Trip> getApprovedTripsInNextXHours(int parkingPermissionHoursInFuture, int parkingPermissionHoursPriorSlotDate);

    public List<Trip> getApprovedTripsWithSlotDateInFuture(long portOperatorId, int transactionType, int hoursInFuture);

    public List<String> getVehicleRegistrationListFromApprovedDirectToPortTrips();

    public Trip getTheLatestAuthorisedDirectToPortTripByVehicleRegNumber(String vehicleRegistration);

    public Trip globalCheckForActiveBookingForVehicle(String vehicleRegistration, Date dateSlotForNewTrip) throws PADException;

    public List<Trip> getTripsByVehicleRegNumberAndStatus(String vehicleRegistration, int status, int kioskSessionType);

    public Trip getApprovedTripByVehicleRegNumberAndParkingPermissionId(String vehicleRegistration, long parkingPermissionId);

    public Trip getTripByVehicleRegNumberAndParkingPermissionId(String vehicleRegistration, long parkingPermissionId, int parkingPermissionType);

    public List<String> getVehicleRegistrationListFromTrips(int kioskSessionType);

    public Trip getTripByCode(String code);

    public Trip getTripByReferenceNumber(String referenceNumber);

    public Trip getTripById(long id);

    public void updateTrip(Trip trip);

    public void updateTrip(TripJson tripJson, long loggedOperatorId) throws PADException, PADValidationException, Exception;

    public void cancelTrip(String tripCode, Operator loggedOperator) throws PADException, PADValidationException, Exception;

    public void cancelAdHocTrip(String tripCode, long loggedOperatorId) throws PADException, PADValidationException, Exception;

    public void approveTrip(String tripCode, String driverCode, long loggedOperatorId) throws PADException, PADValidationException, Exception;

    public void rejectTrip(String tripCode, long loggedOperatorId) throws PADException, PADValidationException, Exception;

    public boolean isTripEligibleForParkingWithoutPayment(Account account, BigDecimal tripFeeAmount);

    public long getTripCount(TripJson tripJson, Long accountId, Date dateSlotRequestedFrom, Date dateSlotRequestedTo, Date dateSlotApprovedStart, Date dateSlotApprovedEnd);

    public List<TripJson> getTripList(TripJson tripJson, Long accountId, Date dateSlotRequestedFrom, Date dateSlotRequestedTo, Date dateSlotApprovedFrom, Date dateSlotApprovedTo);

    public String addTrip(TripJson tripJson, long accountId, long operatorCreatedId) throws PADException, PADValidationException;

    public TripJson createAdhocTrip(TripJson tripJson, Operator loggedOperator) throws PADException, PADValidationException;

    public Trip findApprovedTripByVehicleReg(String vehicleRegistration, long parkingPermissionId) throws PADException;

    public TripJson buildTripJsonEntity(TripJson tripJson, Trip trip, Operator loggedOperator) throws PADValidationException;

    public int getTripCountByStatus(int status);

    public int getTripCountByAccountIdAndStatus(long accountId, int status);

    public List<Trip> getTripsByAccountIdAndStatus(long accountId, int status);

    public void updateExistingMissionTripsFlags(PortOperatorTransactionType portOperatorTransactionType) throws PADException, Exception;

    public List<Trip> getExpiredUnpaidDirectToPortTripsCreatedByVirtualKiosk();

    public List<Trip> getTripsExitedParkingPrematurelyForMoreThanXMinutes(int minutes);

    public Trip getTheLatestCompletedTripByVehicleRegNumber(String vehicleRegistration);

    public List<Trip> getOldApprovedTrips(long portOperatorId, int transactionType, int tripCancelSystemAfterMinutes);
    
    public BigDecimal getSumFeeAmountForApprovedAndInFlightTripsAndDateSlotRange(long accountId, int dateSlotInPastHours, int dateSlotInFutureHours);

}
