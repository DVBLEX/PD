package com.pad.server.base.services.driver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.PreparedJDBCQuery;
import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.Driver;
import com.pad.server.base.entities.DriverAssociation;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.DriverJson;

@Service
@Transactional
public class DriverServiceImpl implements DriverService {

    private static final Logger logger = Logger.getLogger(DriverServiceImpl.class);

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private HibernateTemplate   hibernateTemplate;

    @Override
    public long getDriverCount(long accountId, String firstName, String lastName, String email, String msisdn, String licenceNumber) {

        PreparedJDBCQuery query = getDriverQuery(PreparedJDBCQuery.QUERY_TYPE_COUNT, accountId, firstName, lastName, email, msisdn, licenceNumber);

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Long.class);
    }

    private PreparedJDBCQuery getDriverQuery(int queryType, long accountId, String firstName, String lastName, String email, String msisdn, String licenceNumber) {

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        if (queryType == PreparedJDBCQuery.QUERY_TYPE_COUNT) {

            query.append("SELECT COUNT(drivers.id)");

        } else if (queryType == PreparedJDBCQuery.QUERY_TYPE_SELECT) {

            query.append(
                "SELECT drivers.code, drivers.first_name, drivers.last_name, drivers.email, drivers.msisdn, drivers.issuing_country_iso, drivers.licence_number, drivers.language_id,  "
                    + "driverAssociations.status, driverAssociations.date_created ");
        }

        query.append(" FROM pad.drivers drivers  ");
        query.append(" INNER JOIN pad.driver_associations driverAssociations on drivers.id = driverAssociations.driver_id ");
        query.append("WHERE driverAssociations.account_id = ? AND driverAssociations.status != ?");
        query.addQueryParameter(accountId);
        query.addQueryParameter(ServerConstants.DRIVER_ASSOCIATION_STATUS_DELETED);

        if (StringUtils.isNotBlank(firstName)) {
            query.append(" AND drivers.first_name = ?");
            query.addQueryParameter(firstName);
        }

        if (StringUtils.isNotBlank(lastName)) {
            query.append(" AND drivers.last_name = ?");
            query.addQueryParameter(lastName);
        }

        if (StringUtils.isNotBlank(email)) {
            query.append(" AND drivers.email = ?");
            query.addQueryParameter(email);
        }

        if (StringUtils.isNotBlank(msisdn)) {
            query.append(" AND drivers.msisdn = ?");
            query.addQueryParameter(msisdn);
        }

        if (StringUtils.isNotBlank(licenceNumber)) {
            query.append(" AND drivers.licence_number = ?");
            query.addQueryParameter(licenceNumber);
        }

        return query;
    }

    @Override
    public List<Driver> getDriverList(long accountId, String firstName, String lastName, String email, String msisdn, String licenceNumber, String sortColumn, boolean sortAsc,
        int startLimit, int endLimit) {

        final List<Driver> driverList = new ArrayList<>();
        try {

            PreparedJDBCQuery query = getDriverQuery(PreparedJDBCQuery.QUERY_TYPE_SELECT, accountId, firstName, lastName, email, msisdn, licenceNumber);

            query.setSortParameters(sortColumn, sortAsc, "drivers", ServerConstants.DEFAULT_SORTING_FIELD, "DESC");
            query.append(" LIMIT ").append(startLimit).append(", ").append(endLimit);

            jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    Driver driver = new Driver();
                    driver.setCode(rs.getString("drivers.code"));
                    driver.setFirstName(rs.getString("drivers.first_name"));
                    driver.setLastName(rs.getString("drivers.last_name"));
                    driver.setEmail(rs.getString("drivers.email"));
                    driver.setMsisdn(rs.getString("drivers.msisdn"));
                    driver.setIssuingCountryISO(rs.getString("drivers.issuing_country_iso"));
                    driver.setLicenceNumber(rs.getString("drivers.licence_number"));
                    driver.setLanguageId(rs.getLong("drivers.language_id"));
                    driver.setDateCreated(rs.getTimestamp("driverAssociations.date_created"));
                    driver.setStatus(rs.getInt("driverAssociations.status"));

                    driverList.add(driver);
                }
            }, query.getQueryParameters());

        } catch (Exception e) {
            logger.error("getDriverList###Exception: ", e);
        }

        return driverList;
    }

    @Override
    public void saveDriver(Driver driver) {

        hibernateTemplate.save(driver);
    }

    @Override
    public void saveDriver(DriverJson driverJson, Account account, Operator operator) throws PADException, PADValidationException {

        boolean isDriverAlreadyAssociatedToAccount = false;

        // check if there exists a driver record with that licence and issuing country combination
        Driver driver = getDriverByLicenceNumberAndIssuingCountry(driverJson.getLicenceNumber(), driverJson.getIssuingCountryISO());

        DriverAssociation driverAssociation = new DriverAssociation();

        if (driver == null) {
            // create driver and associate it to transporter account

            Driver newDriver = new Driver();
            newDriver.setCode(SecurityUtil.generateUniqueCode());
            newDriver.setFirstName(driverJson.getFirstName());
            newDriver.setLastName(driverJson.getLastName());
            newDriver.setEmail(StringUtils.isBlank(driverJson.getEmail()) ? "" : driverJson.getEmail());
            newDriver.setMsisdn(driverJson.getMsisdn());
            newDriver.setIssuingCountryISO(driverJson.getIssuingCountryISO());
            newDriver.setLicenceNumber(driverJson.getLicenceNumber().toUpperCase());
            newDriver.setLanguageId(driverJson.getLanguageId());
            newDriver.setOperatorId(operator.getId());
            newDriver.setDateCreated(new Date());
            newDriver.setDateEdited(newDriver.getDateCreated());

            saveDriver(newDriver);

            driverAssociation.setDriverId(newDriver.getId());

        } else {
            // check if driver is already associated to this transporter account

            List<DriverAssociation> accountDriverAssociations = getDriverAssociationsByAccountAndDriverId(account.getId(), driver.getId());
            if (accountDriverAssociations != null && !accountDriverAssociations.isEmpty()) {

                for (DriverAssociation da : accountDriverAssociations) {
                    if (da.getStatus() == ServerConstants.DRIVER_ASSOCIATION_STATUS_APPROVED) {
                        isDriverAlreadyAssociatedToAccount = true;
                        break;
                    }
                }
            }

            if (isDriverAlreadyAssociatedToAccount)
                throw new PADValidationException(ServerResponseConstants.DRIVER_ALREADY_ASSOCIATED_TO_ACCOUNT_CODE,
                    ServerResponseConstants.DRIVER_ALREADY_ASSOCIATED_TO_ACCOUNT_TEXT, "");

            else {

                driverAssociation.setDriverId(driver.getId());
            }
        }

        // save the association of driver & transporter
        if (operator.getRoleId() == ServerConstants.OPERATOR_ROLE_TRANSPORTER) {
            driverAssociation.setStatus(ServerConstants.DRIVER_ASSOCIATION_STATUS_APPROVED);

        } else {
            driverAssociation.setStatus(ServerConstants.DRIVER_ASSOCIATION_STATUS_PENDING);
        }

        driverAssociation.setAccount(account);
        driverAssociation.setOperatorId(operator.getId());
        driverAssociation.setDateCreated(new Date());
        driverAssociation.setDateEdited(driverAssociation.getDateCreated());

        saveDriverAssociation(driverAssociation);
    }

    @Override
    public void saveDriverAssociation(DriverAssociation driverAssociation) {

        hibernateTemplate.save(driverAssociation);
    }

    @Override
    public void updateDriver(Driver driver) {

        hibernateTemplate.update(driver);
    }

    @Override
    public void updateDriverAssociation(DriverAssociation driverAssociation) {

        hibernateTemplate.update(driverAssociation);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Driver getDriverByCode(String code) {

        Driver driver = null;

        List<Driver> driverList = (List<Driver>) hibernateTemplate.findByNamedParam("FROM Driver WHERE code = :code", "code", code);

        if (driverList != null && !driverList.isEmpty()) {
            driver = driverList.get(0);
        }

        return driver;
    }

    @Override
    public Driver getDriverById(Long id) {

        Driver driver = null;

        driver = hibernateTemplate.get(Driver.class, id);

        return driver;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Driver getDriverByLicenceNumberAndIssuingCountry(String licenceNumber, String issuingCountryISO) {

        Driver driver = null;

        String[] paramNames = { "licenceNumber", "issuingCountryISO" };
        Object[] paramValues = { licenceNumber, issuingCountryISO };

        List<Driver> driverList = (List<Driver>) hibernateTemplate.findByNamedParam("FROM Driver WHERE licenceNumber = :licenceNumber AND issuingCountryISO = :issuingCountryISO",
            paramNames, paramValues);

        if (driverList != null && !driverList.isEmpty()) {
            driver = driverList.get(0);
        }

        return driver;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DriverAssociation> getDriverAssociationsByAccountAndDriverId(long accountId, long driverId) {

        String[] paramNames = { "accountId", "driverId" };
        Object[] paramValues = { accountId, driverId };

        List<DriverAssociation> driverAssociationList = (List<DriverAssociation>) hibernateTemplate
            .findByNamedParam("FROM DriverAssociation d WHERE d.account.id = :accountId AND d.driverId = :driverId", paramNames, paramValues);

        return driverAssociationList;
    }

    @Override
    public void removeDriverAssociation(long accountId, long operatorId, Driver driver) throws PADException {

        boolean isDriverAlreadyAssociatedToAccount = false;

        List<DriverAssociation> accountDriverAssociations = getDriverAssociationsByAccountAndDriverId(accountId, driver.getId());

        if (accountDriverAssociations == null || accountDriverAssociations.isEmpty())
            throw new PADException(ServerResponseConstants.DRIVER_NOT_ASSOCIATED_TO_ACCOUNT_CODE, ServerResponseConstants.DRIVER_NOT_ASSOCIATED_TO_ACCOUNT_TEXT, "#1");

        else {
            for (DriverAssociation da : accountDriverAssociations) {
                if (da.getStatus() == ServerConstants.DRIVER_ASSOCIATION_STATUS_APPROVED) {
                    isDriverAlreadyAssociatedToAccount = true;

                    da.setStatus(ServerConstants.DRIVER_ASSOCIATION_STATUS_DELETED);
                    da.setDateDeleted(new Date());
                    da.setOperatorId(operatorId);

                    updateDriverAssociation(da);

                    break;
                }
            }
        }

        if (!isDriverAlreadyAssociatedToAccount)
            throw new PADException(ServerResponseConstants.DRIVER_NOT_ASSOCIATED_TO_ACCOUNT_CODE, ServerResponseConstants.DRIVER_NOT_ASSOCIATED_TO_ACCOUNT_TEXT, "#2");

    }

}
