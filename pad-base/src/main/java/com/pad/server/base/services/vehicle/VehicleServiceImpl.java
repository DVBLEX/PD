package com.pad.server.base.services.vehicle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.entities.Vehicle;
import com.pad.server.base.entities.VehicleAnpr;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private static final Logger logger = Logger.getLogger(VehicleServiceImpl.class);

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private HibernateTemplate   hibernateTemplate;

    @Override
    public long getVehicleCount(long accountId, String regCountryISO, String regNumber, String make, String color) {

        PreparedJDBCQuery query = getVehicleQuery(PreparedJDBCQuery.QUERY_TYPE_COUNT, accountId, regCountryISO, regNumber, make, color);

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Long.class);
    }

    private PreparedJDBCQuery getVehicleQuery(int queryType, long accountId, String regCountryISO, String regNumber, String make, String color) {

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        if (queryType == PreparedJDBCQuery.QUERY_TYPE_COUNT) {

            query.append("SELECT COUNT(vehicles.id)");
            query.append(" FROM pad.vehicles vehicles ");

        } else if (queryType == PreparedJDBCQuery.QUERY_TYPE_SELECT) {

            query.append("SELECT * FROM pad.vehicles vehicles ");
        }

        query.append("WHERE vehicles.account_id = ?");
        query.addQueryParameter(accountId);

        if (StringUtils.isNotBlank(regCountryISO)) {
            query.append(" AND vehicles.registration_country_iso = ?");
            query.addQueryParameter(regCountryISO);
        }

        if (StringUtils.isNotBlank(regNumber)) {
            query.append(" AND vehicles.vehicle_registration = ?");
            query.addQueryParameter(regNumber);
        }

        if (StringUtils.isNotBlank(make)) {
            query.append(" AND vehicles.make = ?");
            query.addQueryParameter(make);
        }

        if (StringUtils.isNotBlank(color)) {
            query.append(" AND vehicles.color = ?");
            query.addQueryParameter(color);
        }

        return query;
    }

    @Override
    public List<Vehicle> getVehicleList(long accountId, String regCountryISO, String regNumber, String make, String color, String sortColumn, boolean sortAsc, int startLimit,
        int endLimit) throws SQLException {

        final List<Vehicle> vehicleList = new ArrayList<>();

        PreparedJDBCQuery query = getVehicleQuery(PreparedJDBCQuery.QUERY_TYPE_SELECT, accountId, regCountryISO, regNumber, make, color);

        query.setSortParameters(sortColumn, sortAsc, "vehicles", ServerConstants.DEFAULT_SORTING_FIELD, "DESC");
        query.append(" LIMIT ").append(startLimit).append(", ").append(endLimit);

        jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Vehicle vehicle = new Vehicle();
                vehicle.setCode(rs.getString("code"));
                vehicle.setVehicleRegistration(rs.getString("vehicle_registration"));
                vehicle.setMake(rs.getString("make"));
                vehicle.setColor(rs.getString("color"));
                vehicle.setRegistrationCountryISO(rs.getString("registration_country_iso"));
                vehicle.setIsAddedApi(rs.getBoolean("is_added_api"));
                vehicle.setIsApproved(rs.getBoolean("is_approved"));
                vehicle.setIsActive(rs.getBoolean("is_active"));
                vehicle.setDateCreated(rs.getTimestamp("date_created"));

                vehicleList.add(vehicle);
            }
        }, query.getQueryParameters());

        return vehicleList;
    }

    @Override
    public void saveVehicle(Vehicle vehicle) {

        hibernateTemplate.save(vehicle);
    }

    @Override
    public void updateVehicle(Vehicle vehicle) {

        hibernateTemplate.update(vehicle);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Vehicle getVehicleByCode(String code) {

        Vehicle vehicle = null;

        List<Vehicle> vehicleList = (List<Vehicle>) hibernateTemplate.findByNamedParam("FROM Vehicle WHERE code = :code", "code", code);

        if (vehicleList != null && !vehicleList.isEmpty()) {
            vehicle = vehicleList.get(0);
        }

        return vehicle;
    }

    @Override
    public Vehicle getVehicleById(Long id) {

        Vehicle vehicle = null;

        vehicle = hibernateTemplate.get(Vehicle.class, id);

        return vehicle;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Vehicle> getVehicleByRegNumber(String vehicleRegistration) {

        return (List<Vehicle>) hibernateTemplate.findByNamedParam("FROM Vehicle WHERE vehicleRegistration = :vehicleRegistration", "vehicleRegistration", vehicleRegistration);

    }

    @SuppressWarnings("unchecked")
    @Override
    public VehicleAnpr getVehicleAnprByRegNumber(String vehicleRegistration) {

        VehicleAnpr vehicleAnpr = null;

        List<VehicleAnpr> vehicleAnprList = (List<VehicleAnpr>) hibernateTemplate.findByNamedParam("FROM VehicleAnpr WHERE vehicleRegistration = :vehicleRegistration",
            "vehicleRegistration", vehicleRegistration);

        if (vehicleAnprList != null && !vehicleAnprList.isEmpty()) {
            vehicleAnpr = vehicleAnprList.get(0);
        }

        return vehicleAnpr;
    }

    @Override
    public void saveVehicleAnpr(VehicleAnpr vehicleAnpr) {

        hibernateTemplate.save(vehicleAnpr);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Vehicle> getVehicleByaccountId(long accountId) {

        return (List<Vehicle>) hibernateTemplate.findByNamedParam("FROM Vehicle WHERE account.id = :accountId", "accountId", accountId);

    }

    @SuppressWarnings("unchecked")
    @Override
    public Vehicle getVehicleByAccountIdAndRegNumber(long accountId, String vehicleRegistration) {
        Vehicle vehicle = null;
        try {

            String[] paramNames = { "accountId", "vehicleRegistration" };
            Object[] paramValues = { accountId, vehicleRegistration };

            List<Vehicle> vehicleList = (List<Vehicle>) hibernateTemplate
                .findByNamedParam("FROM Vehicle WHERE account.id = :accountId AND vehicleRegistration = :vehicleRegistration", paramNames, paramValues);

            if (vehicleList != null && !vehicleList.isEmpty()) {
                vehicle = vehicleList.get(0);
            }
        } catch (Exception e) {
            logger.error("getVehicleByAccountIdAndRegNumber###Exception: ", e);
        }

        return vehicle;
    }

}
