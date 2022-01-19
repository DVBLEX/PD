package com.pad.server.base.services.driver;

import java.util.List;

import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.Driver;
import com.pad.server.base.entities.DriverAssociation;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.DriverJson;

public interface DriverService {

    public long getDriverCount(long accountId, String firstName, String lastName, String email, String msisdn, String licenceNumber);

    public List<Driver> getDriverList(long accountId, String firstName, String lastName, String email, String msisdn, String licenceNumber, String sortColumn, boolean sortAsc,
        int startLimit, int endLimit);

    public void saveDriver(Driver driver);

    public void saveDriver(DriverJson driverJson, Account account, Operator operator) throws PADException, PADValidationException;

    public void saveDriverAssociation(DriverAssociation driverAssociation);

    public void updateDriver(Driver driver);

    public void updateDriverAssociation(DriverAssociation driverAssociation);

    public Driver getDriverByCode(String code);

    public Driver getDriverById(Long id);

    public Driver getDriverByLicenceNumberAndIssuingCountry(String licenceNumber, String issuingCountryISO);

    public List<DriverAssociation> getDriverAssociationsByAccountAndDriverId(long accountId, long driverId);

    public void removeDriverAssociation(long accountId, long operatorId, Driver driver) throws PADException;
}
