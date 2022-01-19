package com.pad.server.base.services.vehicle;

import java.sql.SQLException;
import java.util.List;

import com.pad.server.base.entities.Vehicle;
import com.pad.server.base.entities.VehicleAnpr;

public interface VehicleService {

    public long getVehicleCount(long accountId, String regCountryISO, String regNumber, String make, String color) throws Exception;

    public List<Vehicle> getVehicleList(long accountId, String regCountryISO, String regNumber, String make, String color, String sortColumn, boolean sortAsc, int startLimit,
        int endLimit) throws SQLException;

    public void saveVehicle(Vehicle vehicle);

    public void updateVehicle(Vehicle vehicle);

    public Vehicle getVehicleByCode(String code);

    public Vehicle getVehicleById(Long id);

    public List<Vehicle> getVehicleByRegNumber(String vehicleRegistration);

    public VehicleAnpr getVehicleAnprByRegNumber(String vehicleRegistration);

    public void saveVehicleAnpr(VehicleAnpr vehicleAnpr);

    public List<Vehicle> getVehicleByaccountId(long accountId);

    public Vehicle getVehicleByAccountIdAndRegNumber(long accountId, String vehicleRegistration);
}
