package com.pad.server.base.services.portaccess;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.AnprLog;
import com.pad.server.base.entities.Driver;
import com.pad.server.base.entities.IndependentPortOperator;
import com.pad.server.base.entities.Lane;
import com.pad.server.base.entities.Mission;
import com.pad.server.base.entities.Parking;
import com.pad.server.base.entities.PortAccess;
import com.pad.server.base.entities.PortAccessWhitelist;
import com.pad.server.base.entities.PortOperatorTransactionType;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.PortAccessJson;
import com.pad.server.base.jsonentities.api.PortAccessWhitelistJson;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.anpr.AnprBaseService;
import com.pad.server.base.services.driver.DriverService;
import com.pad.server.base.services.lane.LaneService;
import com.pad.server.base.services.mission.MissionService;
import com.pad.server.base.services.parking.ParkingService;
import com.pad.server.base.services.statement.StatementService;
import com.pad.server.base.services.system.SystemService;
import com.pad.server.base.services.trip.TripService;

@Service
@Transactional
public class PortAccessServiceImpl implements PortAccessService {

    private static final Logger logger = Logger.getLogger(PortAccessServiceImpl.class);

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private HibernateTemplate   hibernateTemplate;

    @Autowired
    private AccountService      accountService;

    @Autowired
    private ActivityLogService  activityLogService;

    @Autowired
    private AnprBaseService     anprBaseService;

    @Autowired
    private DriverService       driverService;

    @Autowired
    private LaneService         laneService;

    @Autowired
    private MissionService      missionService;

    @Autowired
    private ParkingService      parkingService;

    @Autowired
    private StatementService    statementService;

    @Autowired
    private SystemService       systemService;

    @Autowired
    private TripService         tripService;

    @Override
    public long getPortEntryCount(Integer portOperatorId, String vehicleRegistration, String referenceNumber, Integer transactionType, int status, Date dateEntryFrom,
        Date dateEntryTo) {

        PreparedJDBCQuery query = getPortQuery(PreparedJDBCQuery.QUERY_TYPE_COUNT, portOperatorId, vehicleRegistration, referenceNumber, transactionType, status, dateEntryFrom,
            dateEntryTo);

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Long.class);
    }

    private PreparedJDBCQuery getPortQuery(int queryType, Integer portOperatorId, String vehicleRegistration, String referenceNumber, Integer transactionType, int status,
        Date dateEntryFrom, Date dateEntryTo) {

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        if (queryType == PreparedJDBCQuery.QUERY_TYPE_COUNT) {

            query.append("SELECT COUNT(portaccess.id)");
            query.append(" FROM pad.port_access portaccess  ");

        } else if (queryType == PreparedJDBCQuery.QUERY_TYPE_SELECT) {

            query.append("SELECT * FROM pad.port_access portaccess ");

        }

        query.append(" INNER JOIN pad.missions mission on portaccess.mission_id = mission.id");
        query.append(" INNER JOIN pad.trips trip on portaccess.trip_id = trip.id");
        query.append(" LEFT JOIN pad.drivers driver on portaccess.driver_id = driver.id");
        query.append(" LEFT JOIN pad.vehicles vehicle on portaccess.vehicle_id = vehicle.id");
        query.append(" LEFT JOIN pad.parking parking on portaccess.parking_id = parking.id");
        query.append(" WHERE (1=1)");

        if (status != ServerConstants.DEFAULT_INT) {
            query.append(" AND portaccess.status = ? ");
            query.addQueryParameter(status);
        }

        if (StringUtils.isNotBlank(vehicleRegistration)) {
            query.append(" AND portaccess.vehicle_registration LIKE ?");
            query.addQueryParameter("%" + vehicleRegistration + "%");
        }

        if (StringUtils.isNotBlank(referenceNumber)) {
            query.append(" AND mission.reference_number LIKE ?");
            query.addQueryParameter("%" + referenceNumber + "%");
        }

        if (transactionType != null && transactionType != ServerConstants.DEFAULT_INT) {
            query.append(" AND mission.transaction_type = ?");
            query.addQueryParameter(transactionType);
        }

        if (portOperatorId != null) {
            query.append(" AND mission.port_operator_id = ?");
            query.addQueryParameter(portOperatorId);
        }

        // On the front end we're allowing range of 3 months tops and all statuses are based on this date (date_entry)

        if (dateEntryFrom != null) {
            query.append(" AND portaccess.date_entry >= ?");
            query.addQueryParameter(dateEntryFrom);
        }

        if (dateEntryTo != null) {
            query.append(" AND portaccess.date_entry <= ?");
            query.addQueryParameter(dateEntryTo);
        }

        return query;
    }

    @Override
    public List<PortAccessJson> getPortEntryList(Integer portOperatorId, String vehicleRegistration, String referenceNumber, Integer transactionType, int status,
        Date dateEntryFrom, Date dateEntryTo, String sortColumn, boolean sortAsc, int startLimit, int endLimit) throws SQLException {

        final List<PortAccessJson> portAccessList = new ArrayList<>();

        PreparedJDBCQuery query = getPortQuery(PreparedJDBCQuery.QUERY_TYPE_SELECT, portOperatorId, vehicleRegistration, referenceNumber, transactionType, status, dateEntryFrom,
            dateEntryTo);

        String tableAlias = sortColumn.contains(".") ? sortColumn.substring(0, sortColumn.indexOf(".")) : "portaccess";
        String sortBy = sortColumn.contains(".") ? sortColumn.substring(sortColumn.indexOf(".") + 1) : sortColumn;
        query.setSortParameters(sortBy, sortAsc, tableAlias, ServerConstants.DEFAULT_SORTING_FIELD, "DESC");

        if (endLimit != ServerConstants.DEFAULT_INT) {
            query.append(" LIMIT ").append(startLimit).append(", ").append(endLimit);
        }

        jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                PortAccessJson portAccess = new PortAccessJson();
                portAccess.setCode(rs.getString("portaccess.code"));
                portAccess.setStatus(rs.getInt("portaccess.status"));
                portAccess.setVehicleMaker(rs.getString("vehicle.make"));

                try {
                    portAccess.setDateSlotString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("trip.date_slot_approved")));
                } catch (ParseException e) {
                    portAccess.setDateSlotString("");
                }

                portAccess.setPortOperator(rs.getInt("mission.port_operator_id"));
                portAccess.setIndependentPortOperatorCode(systemService.getIndependentPortOperatorCodeById(rs.getLong("mission.independent_port_operator_id")));
                portAccess.setTransactionType(rs.getInt("mission.transaction_type"));
                portAccess.setReferenceNumber(rs.getString("mission.reference_number"));
                portAccess.setDriverName(StringUtils.isNotBlank(rs.getString("driver.first_name")) ? rs.getString("driver.first_name") + " " + rs.getString("driver.last_name")
                    : ServerConstants.DEFAULT_STRING);
                portAccess.setVehicleRegistration(rs.getString("portaccess.vehicle_registration"));
                portAccess.setVehicleColor(rs.getString("parking.vehicle_color"));
                portAccess.setDriverMobile(rs.getString("parking.driver_msisdn"));
                portAccess.setIsInTransit(rs.getBoolean("parking.is_eligible_port_entry"));

                if (rs.getTimestamp("portaccess.date_entry") != null) {

                    try {
                        portAccess.setDatePortEntryString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("portaccess.date_entry")));
                    } catch (ParseException e) {
                        portAccess.setDatePortEntryString("");
                    }
                }

                if (rs.getTimestamp("portaccess.date_exit") != null) {

                    try {
                        portAccess.setDatePortExitString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("portaccess.date_exit")));
                    } catch (ParseException e) {
                        portAccess.setDatePortExitString("");
                    }
                }

                if (rs.getTimestamp("parking.date_exit") != null) {

                    try {
                        portAccess.setDateParkingExitString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("parking.date_exit")));
                    } catch (ParseException e) {
                        portAccess.setDateParkingExitString("");
                    }
                }

                portAccess.setReasonDeny(rs.getString("portaccess.reason_deny"));

                if (rs.getTimestamp("portaccess.date_deny") != null) {

                    try {
                        portAccess.setDateStringDeny(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("portaccess.date_deny")));
                    } catch (ParseException e) {
                        portAccess.setDateStringDeny("");
                    }
                }

                portAccessList.add(portAccess);
            }
        }, query.getQueryParameters());

        return portAccessList;
    }

    @Override public void exportPortEntryList(List<PortAccessJson> portEntryList, OutputStream os) throws IOException {
        SXSSFWorkbook wb = new SXSSFWorkbook(1000);

        Font font = wb.createFont();
        font.setBold(true);

        CellStyle cellStyleBold = wb.createCellStyle();
        cellStyleBold.setFont(font);

        CellStyle cellStyleAmount = wb.createCellStyle();
        cellStyleAmount.setDataFormat((short) 2);

        Sheet sheet = wb.createSheet("Port Entry Data");
        sheet.createFreezePane(0, 1);

        int row = 0;
        int column = 0;

        Row headerRow = sheet.createRow(row++);

        headerRow.createCell(column).setCellValue("Operator");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Vehicle");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Reference Number");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Transaction Type");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Mobile Number");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Status");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Reason for Denial");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Denial Date");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Slot Date");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Parking Exit");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Port Entry");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Port Exit");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        Row dataRow = null;
        for (PortAccessJson portAccessJson : portEntryList) {

            column = 0;

            dataRow = sheet.createRow(row++);

            dataRow.createCell(column++).setCellValue(systemService.getPortOperatorNameById(portAccessJson.getPortOperator()) + " "
                + systemService.getIndependentPortOperatorNameByCode(portAccessJson.getIndependentPortOperatorCode()));
            dataRow.createCell(column++).setCellValue(portAccessJson.getVehicleRegistration());
            dataRow.createCell(column++).setCellValue(portAccessJson.getReferenceNumber());
            dataRow.createCell(column++).setCellValue(ServerUtil.getTransactionTypeName(portAccessJson.getTransactionType(), ServerConstants.LANGUAGE_EN_ID));
            dataRow.createCell(column++).setCellValue(portAccessJson.getDriverMobile());
            dataRow.createCell(column++).setCellValue(ServerUtil.getPortAccessStatusNameByStatusId(portAccessJson.getStatus()));
            dataRow.createCell(column++).setCellValue(portAccessJson.getReasonDeny());
            dataRow.createCell(column++).setCellValue(portAccessJson.getDateStringDeny());
            dataRow.createCell(column++).setCellValue(portAccessJson.getDateSlotString());
            dataRow.createCell(column++).setCellValue(portAccessJson.getDateParkingExitString());
            dataRow.createCell(column++).setCellValue(portAccessJson.getDatePortEntryString());
            dataRow.createCell(column++).setCellValue(portAccessJson.getDatePortExitString());
        }

        for (int i = 0; i < column; i++) {
            try {
                sheet.autoSizeColumn(i);
            } catch (Exception ex) {
                logger.error(ex);
            }
        }

        wb.write(os);

        wb.dispose();
        wb.close();
    }

    @Override
    public long getWhitelistCount(Integer portOperatorId, Date dateFrom, Date dateTo, String vehicleRegistration) {

        PreparedJDBCQuery query = getQuery(PreparedJDBCQuery.QUERY_TYPE_COUNT, portOperatorId, dateFrom, dateTo, vehicleRegistration);

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Long.class);
    }

    private PreparedJDBCQuery getQuery(int queryType, Integer portOperatorId, Date dateFrom, Date dateTo, String vehicleRegistration) {

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        if (queryType == PreparedJDBCQuery.QUERY_TYPE_COUNT) {

            query.append(" SELECT COUNT(paw.id) ");
            query.append(" FROM pad.port_access_whitelist paw  ");

        } else if (queryType == PreparedJDBCQuery.QUERY_TYPE_SELECT) {

            query.append("SELECT paw.* ");
            query.append(" FROM pad.port_access_whitelist paw ");
        }

        query.append(" WHERE paw.status = " + ServerConstants.PORT_ACCESS_WHITELIST_STATUS_ACTIVE);

        if (portOperatorId != null) {
            query.append(" AND paw.port_operator_id = ?");
            query.addQueryParameter(portOperatorId);
        }

        if (dateFrom != null) {
            query.append(" AND paw.date_from >= ?");
            query.addQueryParameter(dateFrom);
        }

        if (dateTo != null) {
            query.append(" AND paw.date_to <= ?");
            query.addQueryParameter(dateTo);
        }

        if (StringUtils.isNotBlank(vehicleRegistration)) {
            query.append(" AND paw.vehicle_registration = ?");
            query.addQueryParameter(vehicleRegistration);
        }

        return query;
    }

    @Override
    public long getPortEntryCountByPortOperator(int portOperator) {

        StringBuffer query = new StringBuffer();
        List<Object> queryParameters = new ArrayList<>();

        query.append("SELECT COUNT(portAccess.id)");
        query.append(" FROM pad.port_access portAccess  ");
        query.append(" INNER JOIN pad.missions mission on portAccess.mission_id = mission.id");
        query.append(" WHERE portAccess.status = ? ");
        query.append(" AND mission.port_operator_id = ? ");

        queryParameters.add(ServerConstants.PORT_ACCESS_STATUS_ENTRY);
        queryParameters.add(portOperator);

        return jdbcTemplate.queryForObject(query.toString(), queryParameters.toArray(), Long.class);
    }

    @Override
    public List<PortAccessWhitelistJson> getWhitelistList(Integer portOperatorId, Date dateFrom, Date dateTo, String vehicleRegistration, String sortColumn, boolean sortAsc,
        int startLimit, int endLimit) {

        final List<PortAccessWhitelistJson> list = new ArrayList<>();

        PreparedJDBCQuery query = getQuery(PreparedJDBCQuery.QUERY_TYPE_SELECT, portOperatorId, dateFrom, dateTo, vehicleRegistration);

        if (StringUtils.isBlank(sortColumn)) {
            query.append(" ORDER BY paw.id DESC");
        } else {
            query.append(" ORDER BY ").append(sortColumn).append(sortAsc ? " ASC" : " DESC");
        }

        query.append(" LIMIT ").append(startLimit).append(", ").append(endLimit);

        jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                PortAccessWhitelistJson portAccessWhitelistJson = new PortAccessWhitelistJson();
                portAccessWhitelistJson.setCode(rs.getString("code"));
                portAccessWhitelistJson.setPortOperatorId(rs.getInt("port_operator_id"));

                try {
                    portAccessWhitelistJson.setDateFromString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("date_from")));
                } catch (ParseException e) {
                    portAccessWhitelistJson.setDateFromString("");
                }

                try {
                    portAccessWhitelistJson.setDateToString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("date_to")));
                } catch (ParseException e) {
                    portAccessWhitelistJson.setDateToString("");
                }

                portAccessWhitelistJson.setVehicleRegistration(rs.getString("vehicle_registration"));

                list.add(portAccessWhitelistJson);
            }
        }, query.getQueryParameters());

        return list;
    }

    @Override
    public void savePortAccess(PortAccess portAccess) {

        hibernateTemplate.save(portAccess);
    }

    @Override
    public void updatePortAccess(PortAccess portAccess) {

        hibernateTemplate.update(portAccess);
    }

    @Override
    public void savePortAccessWhitelist(PortAccessWhitelist portAccessWhitelist) {

        hibernateTemplate.save(portAccessWhitelist);
    }

    @Override
    public void updatePortAccessWhitelist(PortAccessWhitelist portAccessWhitelist) {

        hibernateTemplate.update(portAccessWhitelist);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PortAccess getPortAccessByCode(String code) {

        PortAccess portAccess = null;

        List<PortAccess> portAccessList = (List<PortAccess>) hibernateTemplate.findByNamedParam("FROM PortAccess WHERE code = :code", "code", code);

        if (portAccessList != null && !portAccessList.isEmpty()) {
            portAccess = portAccessList.get(0);
        }

        return portAccess;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PortAccess getPortAccessByTripId(long tripId) {

        PortAccess portAccess = null;

        List<PortAccess> portAccessList = (List<PortAccess>) hibernateTemplate.findByNamedParam("FROM PortAccess WHERE tripId = :tripId", "tripId", tripId);

        if (portAccessList != null && !portAccessList.isEmpty()) {
            portAccess = portAccessList.get(0);
        }

        return portAccess;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getEnteredVehicleRegistrationList() {

        List<String> vehicleRegistrationList = new ArrayList<>();

        String[] paramNames = { "statusEntry" };
        Object[] paramValues = { ServerConstants.PORT_ACCESS_STATUS_ENTRY };

        List<PortAccess> portAccessList = (List<PortAccess>) hibernateTemplate.findByNamedParam("FROM PortAccess WHERE status = :statusEntry", paramNames, paramValues);

        if (portAccessList != null && !portAccessList.isEmpty()) {
            vehicleRegistrationList = portAccessList.stream().map(result -> result.getVehicleRegistration()).distinct().collect(Collectors.toList());
        }

        return vehicleRegistrationList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PortAccess getEnteredPortAccessByVehicleRegistration(String vehicleRegistration) {

        PortAccess portAccess = null;

        String[] paramNames = { "vehicleRegistration", "statusEntry" };
        Object[] paramValues = { vehicleRegistration, ServerConstants.PORT_ACCESS_STATUS_ENTRY };

        List<PortAccess> portAccessList = (List<PortAccess>) hibernateTemplate
            .findByNamedParam("FROM PortAccess WHERE vehicleRegistration = :vehicleRegistration AND status = :statusEntry", paramNames, paramValues);

        if (portAccessList != null && !portAccessList.isEmpty()) {
            portAccess = portAccessList.get(0);
        }

        return portAccess;
    }

    @Override
    public boolean isExistWhitlistForDates(Date dateFrom, Date dateTo, String vehicleRegistration) {

        StringBuffer query = new StringBuffer();
        query.append(" SELECT COUNT(paw.id) FROM pad.port_access_whitelist paw ");
        query.append(" WHERE paw.status = " + ServerConstants.PORT_ACCESS_WHITELIST_STATUS_ACTIVE);
        query.append(" AND paw.vehicle_registration = ? AND (");
        query.append(" (paw.date_from BETWEEN ? AND ?) OR ");
        query.append(" (paw.date_to BETWEEN ? AND ?) OR ");
        query.append(" (paw.date_from <= ? AND paw.date_to >= ?))");

        List<Object> parms = new ArrayList<>();
        parms.add(vehicleRegistration);
        parms.add(dateFrom);
        parms.add(dateTo);
        parms.add(dateFrom);
        parms.add(dateTo);
        parms.add(dateFrom);
        parms.add(dateTo);

        return (jdbcTemplate.queryForObject(query.toString(), parms.toArray(), Long.class) > 0);
    }

    @Override
    public PortAccessWhitelistJson getVehicleWhitelisted(String vehicleRegistration) {

        List<PortAccessWhitelistJson> list = new ArrayList<>();

        StringBuffer query = new StringBuffer();
        query.append(" SELECT * FROM pad.port_access_whitelist ");
        query.append(" WHERE status = " + ServerConstants.PORT_ACCESS_WHITELIST_STATUS_ACTIVE);
        query.append(" AND current_timestamp() BETWEEN date_from AND date_to ");
        query.append(" AND vehicle_registration = ? ");

        jdbcTemplate.query(query.toString(), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                PortAccessWhitelistJson portAccessWhitelistJson = new PortAccessWhitelistJson();
                portAccessWhitelistJson.setId(rs.getLong("id"));
                portAccessWhitelistJson.setPortOperatorId(rs.getInt("port_operator_id"));
                portAccessWhitelistJson.setGateId(rs.getLong("port_operator_gate_id"));
                try {
                    portAccessWhitelistJson.setDateFromString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("date_from")));
                } catch (ParseException e) {
                    portAccessWhitelistJson.setDateFromString("");
                }
                try {
                    portAccessWhitelistJson.setDateToString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("date_to")));
                } catch (ParseException e) {
                    portAccessWhitelistJson.setDateToString("");
                }
                portAccessWhitelistJson.setVehicleRegistration(rs.getString("vehicle_registration"));
                portAccessWhitelistJson.setParkingPermissionId(rs.getLong("parking_permission_id"));
                list.add(portAccessWhitelistJson);
            }
        }, vehicleRegistration);

        return list.size() > 0 ? list.get(0) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PortAccessWhitelist getPortAccessWhitelistById(long id) {

        PortAccessWhitelist portAccessWhitelist = null;

        List<PortAccessWhitelist> portAccessWhitelistList = (List<PortAccessWhitelist>) hibernateTemplate.findByNamedParam("FROM PortAccessWhitelist WHERE id = :id", "id", id);

        if (portAccessWhitelistList != null && !portAccessWhitelistList.isEmpty()) {
            portAccessWhitelist = portAccessWhitelistList.get(0);
        }

        return portAccessWhitelist;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PortAccessWhitelist getPortAccessWhitelistByCode(String code) {

        PortAccessWhitelist portAccessWhitelist = null;

        List<PortAccessWhitelist> portAccessWhitelistList = (List<PortAccessWhitelist>) hibernateTemplate.findByNamedParam("FROM PortAccessWhitelist WHERE code = :code", "code",
            code);

        if (portAccessWhitelistList != null && !portAccessWhitelistList.isEmpty()) {
            portAccessWhitelist = portAccessWhitelistList.get(0);
        }

        return portAccessWhitelist;
    }

    @Override
    public void deletePortAccessWhitelist(PortAccessWhitelist portAccessWhitelist) {

        portAccessWhitelist.setStatus(ServerConstants.PORT_ACCESS_WHITELIST_STATUS_DELETED);
        portAccessWhitelist.setDateEdited(new Date());

        updatePortAccessWhitelist(portAccessWhitelist);
    }

    @Override
    @Transactional(rollbackFor = { PADException.class, PADValidationException.class, Exception.class })
    public PortAccess processVehiclePortEntry(String tripCode, String parkingCode, long operatorId, long entryLaneId, Date dateEvent, String selectedZone)
        throws PADException, PADValidationException {

        // process port entry for trips that are not associated to urgent missions and not allowed multiple port entries
        PortAccess portAccess = null;
        AnprLog anprLogForParkingPermissionPortEntry = null;
        Parking parking = null;

        boolean isDirectToPort = false;

        try {
            if (StringUtils.isBlank(tripCode))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                    "processVehiclePortEntry#tripCode is blank");

            Trip trip = tripService.getTripByCode(tripCode);

            if (trip == null)
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                    "processVehiclePortEntry#trip is null");

            if (trip.getStatus() == ServerConstants.TRIP_STATUS_ENTERED_PORT)
                throw new PADValidationException(ServerResponseConstants.VEHICLE_ALREADY_ENTERED_PORT_AREA_CODE, ServerResponseConstants.VEHICLE_ALREADY_ENTERED_PORT_AREA_TEXT,
                    "processVehiclePortEntry#Vehicle has already entered the port");

            Lane lane = laneService.getLaneByLaneId(entryLaneId);

            // TODO remove check 'entryLaneId > 0'. This check was added to avoid exception while there's no way to know the right laneId (event triggered manually)
            if (lane == null && entryLaneId > ServerConstants.ZERO_INT)
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "processVehiclePortEntry#lane is null");

            isDirectToPort = trip.getIsDirectToPort();

            if (!isDirectToPort) {

                if (StringUtils.isBlank(parkingCode))
                    throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                        "processVehiclePortEntry#parkingCode is blank");

                parking = parkingService.getParkingByCode(parkingCode);

                if (parking == null)
                    throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                        "processVehiclePortEntry#parking is null");

                if (!parking.getIsEligiblePortEntry())
                    throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                        "processVehiclePortEntry#vehicle is not elegible to enter port");
            }

            expireOldPortAccessAndTrip(trip);

            portAccess = new PortAccess();
            portAccess.setCode(SecurityUtil.generateUniqueCode());
            portAccess.setStatus(ServerConstants.PORT_ACCESS_STATUS_ENTRY);
            portAccess.setParkingId(isDirectToPort == true ? ServerConstants.DEFAULT_LONG : parking.getId());
            portAccess.setTripId(trip.getId());
            portAccess.setMissionId(trip.getMission() == null ? ServerConstants.DEFAULT_LONG : trip.getMission().getId());
            portAccess.setVehicleId(trip.getVehicleId());
            portAccess.setDriverId(trip.getDriverId());
            portAccess.setPortOperatorId(trip.getPortOperatorId());
            portAccess.setPortOperatorGateId(trip.getPortOperatorGateId());
            portAccess.setVehicleRegistration(trip.getVehicleRegistration());
            portAccess.setDriverMsisdn(trip.getDriverMsisdn());
            portAccess.setReasonDeny(ServerConstants.DEFAULT_STRING);
            portAccess.setDateDeny(null);
            portAccess.setOperatorIdEntry(operatorId);
            portAccess.setOperatorGate(selectedZone);
            portAccess.setOperatorId(operatorId);
            portAccess.setEntryLaneId(entryLaneId == ServerConstants.ZERO_INT ? ServerConstants.DEFAULT_LONG : entryLaneId);
            portAccess.setEntryLaneNumber(lane == null ? ServerConstants.DEFAULT_INT : lane.getLaneNumber()); // TODO Remove null check after passing right laneId
            portAccess.setExitLaneId(ServerConstants.DEFAULT_LONG);
            portAccess.setExitLaneNumber(ServerConstants.DEFAULT_INT);
            portAccess.setDateEntry(dateEvent);
            portAccess.setDateExit(null);
            portAccess.setDateCreated(new Date());
            portAccess.setDateEdited(portAccess.getDateCreated());

            savePortAccess(portAccess);

            if (!isDirectToPort) {

                parking.setStatus(ServerConstants.PARKING_STATUS_ENTERED_PORT);

                parkingService.updateParking(parking);
            }

            if (trip.getParkingPermissionIdPortEntry() != ServerConstants.DEFAULT_LONG) {
                // update port entry parking permission status to disabled for vehicle at port area
                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_ENTRY_STATUS_DISABLED, ServerConstants.DEFAULT_LONG, trip, null,
                    null, new Date());

            } else {
                // check if there exists a port entry request in queue that has not been processed yet. This can happen in case ParkIT is down.
                // if that's the case then mark the request as processed since vehicle already entered port manually.

                if (isDirectToPort) {

                    anprLogForParkingPermissionPortEntry = anprBaseService.getAnprLogByRequestTypeAndParkingPermissionIdAndTripId(
                        ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY_DIRECT, ServerConstants.DEFAULT_LONG, trip.getId());

                } else {
                    anprLogForParkingPermissionPortEntry = anprBaseService.getAnprLogByRequestTypeAndParkingPermissionIdAndTripId(
                        ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY, ServerConstants.DEFAULT_LONG, trip.getId());
                }

                if (anprLogForParkingPermissionPortEntry != null) {

                    anprLogForParkingPermissionPortEntry.setIsProcessed(ServerConstants.PROCESS_PROCESSED);
                    anprLogForParkingPermissionPortEntry.setDateProcessed(new Date());
                    anprLogForParkingPermissionPortEntry.setResponseCode(ServerResponseConstants.PARKING_PERMISSION_MANUAL_PORT_ENTRY_CODE);
                    anprLogForParkingPermissionPortEntry.setResponseText("Manual port entry. Scheduled ANPR permission request cancelled");

                    anprBaseService.updateAnprLog(anprLogForParkingPermissionPortEntry);
                    anprBaseService.deleteScheduledAnpr(anprLogForParkingPermissionPortEntry.getId());
                }
            }

            // add the vehicle reg number to port exit whitelist on ANPR system
            anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_EXIT,
                systemService.getPortOperatorAnprZoneIdById(trip.getPortOperatorGateId()), trip, null, null, new Date(System.currentTimeMillis() + 10l * 1000l));

            if (trip.getIsAllowMultipleEntries() && trip.getType() == ServerConstants.TRIP_TYPE_BOOKED) {
                // create a new trip record for the original pre-booked trip so that when vehicle goes to parking again the next time it will show up during kiosk trip search
                Trip newTrip = new Trip();
                newTrip.setCode(SecurityUtil.generateUniqueCode());
                newTrip.setType(ServerConstants.TRIP_TYPE_BOOKED);
                newTrip.setStatus(trip.getIsDirectToPort() ? ServerConstants.TRIP_STATUS_IN_FLIGHT : ServerConstants.TRIP_STATUS_APPROVED);
                newTrip.setAccountId(trip.getAccountId());
                newTrip.setMission(trip.getMission());
                newTrip.setVehicleId(trip.getVehicleId());
                newTrip.setDriverId(trip.getDriverId());
                newTrip.setPortOperatorId(trip.getMission().getPortOperatorId());
                newTrip.setIndependentPortOperatorId(trip.getMission().getIndependentPortOperatorId());
                newTrip.setTransactionType(trip.getMission().getTransactionType());
                newTrip.setPortOperatorGateId(trip.getMission().getPortOperatorGateId());
                newTrip.setParkingPermissionId(trip.getParkingPermissionIdParkingEntry());
                newTrip.setParkingPermissionIdParkingEntryFirst(trip.getParkingPermissionIdParkingEntryFirst());
                newTrip.setParkingPermissionIdParkingEntry(trip.getParkingPermissionIdParkingEntry());
                newTrip.setParkingPermissionIdParkingExit(ServerConstants.DEFAULT_LONG);
                newTrip.setParkingPermissionIdPortEntry(ServerConstants.DEFAULT_LONG);
                newTrip.setParkingPermissionIdPortExit(ServerConstants.DEFAULT_LONG);
                newTrip.setReferenceNumber(trip.getReferenceNumber());
                newTrip.setVehicleRegistration(trip.getVehicleRegistration());
                newTrip.setVehicleRegistrationCountryISO(trip.getVehicleRegistrationCountryISO());
                newTrip.setContainerId(trip.getContainerId() == null ? ServerConstants.DEFAULT_STRING : trip.getContainerId());
                newTrip.setContainerType(trip.getContainerType() == null ? ServerConstants.DEFAULT_STRING : trip.getContainerType());
                newTrip.setDriverMsisdn(trip.getDriverMsisdn());
                newTrip.setDriverLanguageId(trip.getDriverLanguageId());
                newTrip.setCompanyName(trip.getCompanyName() == null ? ServerConstants.DEFAULT_STRING : trip.getCompanyName());
                newTrip.setOperatorIdCreated(operatorId);
                newTrip.setOperatorId(operatorId);
                newTrip.setLaneSessionId(ServerConstants.DEFAULT_LONG);
                newTrip.setFeePaid(false);
                newTrip.setCurrency(ServerConstants.CURRENCY_CFA_FRANC);
                newTrip.setFeeAmount(systemService.getTripFeeAmount(trip.getPortOperatorId(), trip.getMission().getTransactionType(), trip.getVehicleRegistrationCountryISO()));
                newTrip.setOperatorFeeAmount(systemService.getOperatorTripFeeAmount(trip.getPortOperatorId(), trip.getMission().getTransactionType()));
                newTrip.setDateSlotRequested(dateEvent);
                newTrip.setDateSlotApproved(dateEvent);
                newTrip.setParkingEntryCount(0);
                newTrip.setParkingExitCount(0);
                newTrip.setPortEntryCount(0);
                newTrip.setPortExitCount(0);
                newTrip.setIsDirectToPort(trip.getIsDirectToPort());
                newTrip.setIsAllowMultipleEntries(trip.getIsAllowMultipleEntries());
                newTrip.setReasonDeny(ServerConstants.DEFAULT_STRING);
                newTrip.setDateCreated(new Date());
                newTrip.setDateEdited(newTrip.getDateCreated());

                missionService.saveMissionTrip(newTrip);

            }

            if (isDirectToPort && !trip.isFeePaid()) {
                statementService.chargeParkingFee(trip, operatorId);
            }

            trip.setStatus(ServerConstants.TRIP_STATUS_ENTERED_PORT);
            trip.setPortEntryCount(trip.getPortEntryCount() + 1);
            trip.setDateEntryPort(dateEvent);

            tripService.updateTrip(trip);

            if (operatorId != ServerConstants.DEFAULT_LONG) {
                activityLogService.saveActivityLogPortAccess(ServerConstants.ACTIVITY_LOG_PORT_ENTRY, operatorId, portAccess.getId());
            }

        } catch (PADException | PADValidationException pade) {
            throw pade;

        } catch (Exception e) {
            logger.error("processVehiclePortEntry###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "processVehiclePortEntry#Exception#" + e.getMessage());
        }

        return portAccess;
    }

    private void expireOldPortAccessAndTrip(Trip trip) throws Exception {

        // find out if there are any existing port entries for that reg and expire them. similar to the timer task that expires port entries
        // check if theres any port exit permission for that vehicle. if there is then delete it and update the corresponding trip status
        List<PortAccess> portAccessList = getExpiredPortEntries(0);

        if (portAccessList != null && !portAccessList.isEmpty()) {

            for (PortAccess previousPortAccess : portAccessList) {
                if (previousPortAccess.getVehicleRegistration().equals(trip.getVehicleRegistration())) {

                    previousPortAccess.setStatus(ServerConstants.PORT_ACCESS_STATUS_EXIT_CLOSED_BY_SYSTEM);
                    previousPortAccess.setOperatorId(ServerConstants.DEFAULT_LONG);
                    previousPortAccess.setExitLaneId(ServerConstants.DEFAULT_LONG);
                    previousPortAccess.setExitLaneNumber(ServerConstants.DEFAULT_INT);
                    previousPortAccess.setDateExit(new Date());
                    previousPortAccess.setDateEdited(new Date());

                    updatePortAccess(previousPortAccess);

                    Trip previousTrip = tripService.getTripById(previousPortAccess.getTripId());

                    if (previousTrip != null) {

                        if (previousTrip.getStatus() == ServerConstants.TRIP_STATUS_ENTERED_PORT) {

                            previousTrip.setDateEdited(new Date());
                            previousTrip.setStatus(ServerConstants.TRIP_STATUS_PORT_EXIT_EXPIRED);
                            previousTrip.setPortExitCount(trip.getPortExitCount() + 1);
                            previousTrip.setDateExitPort(new Date());

                            tripService.updateTrip(previousTrip);

                            Mission mission = previousTrip.getMission();
                            mission.setTripsCompletedCount(mission.getTripsCompletedCount() + 1);

                            missionService.updateMission(mission);

                            if ((!previousTrip.getIsAllowMultipleEntries() && !trip.getIsAllowMultipleEntries())
                                || (!previousTrip.getIsAllowMultipleEntries() && trip.getIsAllowMultipleEntries())
                                || (previousTrip.getIsAllowMultipleEntries() && trip.getIsAllowMultipleEntries())) {

                                if (previousTrip.getParkingPermissionIdPortEntry() != ServerConstants.DEFAULT_LONG) {
                                    // remove the port entry parking permission
                                    anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_ENTRY, ServerConstants.DEFAULT_LONG,
                                        previousTrip, null, null, new Date());
                                }

                                if (previousTrip.getParkingPermissionIdPortExit() != ServerConstants.DEFAULT_LONG) {
                                    // remove the port entry parking permission
                                    anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_EXIT, ServerConstants.DEFAULT_LONG,
                                        previousTrip, null, null, new Date());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = { PADException.class, PADValidationException.class, Exception.class })
    public PortAccess processWhitelistedVehiclePortEntry(String vehicleRegNumber, long parkingPermissionId, Integer portOperatorId, Long gateId, long operatorId, long entryLaneId,
        Date dateEvent, String selectedZone) throws PADException, PADValidationException {

        PortAccess portAccess = null;
        PortOperatorTransactionType portOperatorTransactionType = null;

        try {
            if (portOperatorId == null)
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                    "processWhitelistedVehiclePortEntry#portOperatorId is null");

            if (gateId == null)
                throw new PADException(ServerResponseConstants.MISSING_GATE_ID_CODE, ServerResponseConstants.MISSING_GATE_ID_TEXT,
                    "processWhitelistedVehiclePortEntry#gateId is null");

            if (StringUtils.isBlank(vehicleRegNumber))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                    "processWhitelistedVehiclePortEntry#vehicleRegNumber is blank");

            Lane lane = laneService.getLaneByLaneId(entryLaneId);

            // TODO remove check 'entryLaneId > 0'. This check was added to avoid exception while there's no way to know the right laneId (event triggered manually)
            if (lane == null && entryLaneId > ServerConstants.ZERO_INT)
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "processWhitelistedVehiclePortEntry#lane is null");

            if (getEnteredPortAccessByVehicleRegistration(vehicleRegNumber) != null)
                throw new PADValidationException(ServerResponseConstants.VEHICLE_ALREADY_ENTERED_PORT_AREA_CODE, ServerResponseConstants.VEHICLE_ALREADY_ENTERED_PORT_AREA_TEXT,
                    "processWhitelistedVehiclePortEntry#Vehicle has already entered the port");

            portOperatorTransactionType = systemService.getPortOperatorTransactionTypeEntity(portOperatorId, ServerConstants.TRANSACTION_TYPE_WHITELIST);

            Mission mission = new Mission();
            mission.setCode(SecurityUtil.generateUniqueCode());
            mission.setStatus(ServerConstants.MISSION_STATUS_TRIPS_BOOKED);
            mission.setAccountId(ServerConstants.DEFAULT_LONG);
            mission.setPortOperatorId(portOperatorId);
            mission.setIndependentPortOperatorId(ServerConstants.DEFAULT_LONG);
            mission.setPortOperatorGateId(gateId);
            mission.setTransactionType(ServerConstants.TRANSACTION_TYPE_WHITELIST);
            mission.setReferenceNumber(ServerConstants.DEFAULT_STRING);
            mission.setTripsCompletedCount(0);
            mission.setTripsBookedCount(1);
            mission.setOperatorId(operatorId);
            mission.setIsDirectToPort(portOperatorTransactionType.getIsDirectToPort());
            mission.setIsAllowMultipleEntries(portOperatorTransactionType.getIsAllowMultipleEntries());
            mission.setDateMissionStart(new Date());

            Calendar calendarDateMissionEnd = Calendar.getInstance();
            calendarDateMissionEnd.setTime(new Date());
            calendarDateMissionEnd.set(Calendar.SECOND, 59);
            calendarDateMissionEnd.set(Calendar.MINUTE, 59);
            calendarDateMissionEnd.set(Calendar.HOUR_OF_DAY, 23);

            mission.setDateMissionEnd(calendarDateMissionEnd.getTime());
            mission.setDateCreated(new Date());
            mission.setDateEdited(mission.getDateCreated());

            missionService.saveMission(mission);

            Trip trip = new Trip();
            trip.setCode(SecurityUtil.generateUniqueCode());
            trip.setType(ServerConstants.TRIP_TYPE_BOOKED);
            trip.setStatus(ServerConstants.TRIP_STATUS_ENTERED_PORT);
            trip.setAccountId(ServerConstants.DEFAULT_LONG);
            trip.setMission(mission);
            trip.setVehicleId(ServerConstants.DEFAULT_LONG);
            trip.setDriverId(ServerConstants.DEFAULT_LONG);
            trip.setPortOperatorId(mission.getPortOperatorId());
            trip.setIndependentPortOperatorId(trip.getMission().getIndependentPortOperatorId());
            trip.setTransactionType(mission.getTransactionType());
            trip.setPortOperatorGateId(mission.getPortOperatorGateId());
            trip.setParkingPermissionId(parkingPermissionId);
            trip.setParkingPermissionIdParkingEntryFirst(ServerConstants.DEFAULT_LONG);
            trip.setParkingPermissionIdParkingEntry(ServerConstants.DEFAULT_LONG);
            trip.setParkingPermissionIdParkingExit(ServerConstants.DEFAULT_LONG);
            trip.setParkingPermissionIdPortEntry(parkingPermissionId);
            trip.setParkingPermissionIdPortExit(ServerConstants.DEFAULT_LONG);
            trip.setReferenceNumber(ServerConstants.DEFAULT_STRING);
            trip.setContainerId(ServerConstants.DEFAULT_STRING);
            trip.setContainerType(ServerConstants.DEFAULT_STRING);
            trip.setVehicleRegistration(vehicleRegNumber);
            trip.setVehicleRegistrationCountryISO("SN");
            trip.setDriverMsisdn(ServerConstants.DEFAULT_STRING);
            trip.setDriverLanguageId(ServerConstants.DEFAULT_LONG);
            trip.setCompanyName(ServerConstants.DEFAULT_STRING);
            trip.setOperatorIdCreated(operatorId);
            trip.setOperatorId(operatorId);
            trip.setLaneSessionId(ServerConstants.DEFAULT_LONG);
            trip.setFeePaid(false);
            trip.setCurrency(ServerConstants.CURRENCY_CFA_FRANC);
            trip.setFeeAmount(BigDecimal.ZERO);
            trip.setOperatorFeeAmount(BigDecimal.ZERO);
            trip.setDateSlotRequested(dateEvent);
            trip.setDateSlotApproved(dateEvent);
            trip.setParkingEntryCount(0);
            trip.setParkingExitCount(0);
            trip.setPortEntryCount(1);
            trip.setPortExitCount(0);
            trip.setIsDirectToPort(portOperatorTransactionType.getIsDirectToPort());
            trip.setIsAllowMultipleEntries(portOperatorTransactionType.getIsAllowMultipleEntries());
            trip.setReasonDeny(ServerConstants.DEFAULT_STRING);
            trip.setDateEntryPort(dateEvent);
            trip.setDateCreated(new Date());
            trip.setDateEdited(trip.getDateCreated());

            missionService.saveMissionTrip(trip);

            portAccess = new PortAccess();
            portAccess.setCode(SecurityUtil.generateUniqueCode());
            portAccess.setStatus(ServerConstants.PORT_ACCESS_STATUS_ENTRY);
            portAccess.setParkingId(ServerConstants.DEFAULT_LONG);
            portAccess.setTripId(trip.getId());
            portAccess.setMissionId(trip.getMission() == null ? ServerConstants.DEFAULT_LONG : trip.getMission().getId());
            portAccess.setVehicleId(trip.getVehicleId());
            portAccess.setDriverId(trip.getDriverId());
            portAccess.setPortOperatorId(trip.getPortOperatorId());
            portAccess.setPortOperatorGateId(trip.getPortOperatorGateId());
            portAccess.setVehicleRegistration(trip.getVehicleRegistration());
            portAccess.setDriverMsisdn(trip.getDriverMsisdn());
            portAccess.setReasonDeny(ServerConstants.DEFAULT_STRING);
            portAccess.setDateDeny(null);
            portAccess.setOperatorIdEntry(operatorId);
            portAccess.setOperatorGate(selectedZone);
            portAccess.setOperatorId(operatorId);
            portAccess.setEntryLaneId(entryLaneId == ServerConstants.ZERO_INT ? ServerConstants.DEFAULT_LONG : entryLaneId);
            portAccess.setEntryLaneNumber(lane == null ? ServerConstants.DEFAULT_INT : lane.getLaneNumber()); // TODO Remove null check after passing right laneId
            portAccess.setExitLaneId(ServerConstants.DEFAULT_LONG);
            portAccess.setExitLaneNumber(ServerConstants.DEFAULT_INT);
            portAccess.setDateEntry(dateEvent);
            portAccess.setDateExit(null);
            portAccess.setDateCreated(new Date());
            portAccess.setDateEdited(new Date());

            savePortAccess(portAccess);

            // since this is port whitelist, we are not going to disable port entry permission after vehicle enters the port or create a new permission for port exit

            if (operatorId != ServerConstants.DEFAULT_LONG) {
                activityLogService.saveActivityLogPortAccess(ServerConstants.ACTIVITY_LOG_PORT_WHITELIST_ENTRY, operatorId, portAccess.getId());
            }

        } catch (PADException | PADValidationException pade) {
            throw pade;

        } catch (Exception e) {
            logger.error("processWhitelistedVehiclePortEntry###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT,
                "processWhitelistedVehiclePortEntry#Exception#" + e.getMessage());
        }

        return portAccess;
    }

    @Override
    @Transactional(rollbackFor = { PADException.class, PADValidationException.class, Exception.class })
    public PortAccess processUrgentTripPortEntry(String tripCode, String vehicleRegNumber, long parkingPermissionId, long operatorId, long entryLaneId, Date dateEvent,
        String selectedZone) throws PADException, PADValidationException {

        PortAccess portAccess = null;
        Date dateToday = new Date();

        try {
            if (StringUtils.isBlank(vehicleRegNumber))
                throw new PADValidationException(ServerResponseConstants.VEHICLE_NOT_AUTHORIZED_PORT_ENTRY_CODE, ServerResponseConstants.VEHICLE_NOT_AUTHORIZED_PORT_ENTRY_TEXT,
                    "processUrgentTripPortEntry#vehicleRegNumber is blank");

            Trip trip = tripService.getTripByCode(tripCode);

            if (trip == null)
                throw new PADValidationException(ServerResponseConstants.VEHICLE_NOT_AUTHORIZED_PORT_ENTRY_CODE, ServerResponseConstants.VEHICLE_NOT_AUTHORIZED_PORT_ENTRY_TEXT,
                    "processUrgentTripPortEntry#trip is null");

            if (!trip.getMission().getDateMissionStart().before(dateToday) && !trip.getMission().getDateMissionEnd().after(dateToday))
                throw new PADValidationException(ServerResponseConstants.VEHICLE_NOT_AUTHORIZED_PORT_ENTRY_CODE, ServerResponseConstants.VEHICLE_NOT_AUTHORIZED_PORT_ENTRY_TEXT,
                    "processUrgentTripPortEntry#Mission date start/end is invalid");

            Lane lane = laneService.getLaneByLaneId(entryLaneId);

            // TODO remove check 'entryLaneId > 0'. This check was added to avoid exception while there's no way to know the right laneId (event triggered manually)
            if (lane == null && entryLaneId > ServerConstants.ZERO_INT)
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "processUrgentTripPortEntry#lane is null");

            Parking parking = parkingService.getExitedParkingByVehicleRegistration(vehicleRegNumber);

            if (parking != null) {

                parking.setStatus(ServerConstants.PARKING_STATUS_ENTERED_PORT);
                parking.setDateEdited(new Date());

                parkingService.updateParking(parking);
            }

            expireOldPortAccessAndTrip(trip);

            if (trip.getStatus() == ServerConstants.TRIP_STATUS_APPROVED || trip.getStatus() == ServerConstants.TRIP_STATUS_IN_TRANSIT) {
                // the very first port entry by vehicle on trip on urgent mission

                portAccess = new PortAccess();
                portAccess.setCode(SecurityUtil.generateUniqueCode());
                portAccess.setStatus(ServerConstants.PORT_ACCESS_STATUS_ENTRY);
                portAccess.setParkingId(ServerConstants.DEFAULT_LONG);
                portAccess.setTripId(trip.getId());
                portAccess.setMissionId(trip.getMission().getId());
                portAccess.setVehicleId(trip.getVehicleId());
                portAccess.setDriverId(trip.getDriverId());
                portAccess.setPortOperatorId(trip.getPortOperatorId());
                portAccess.setPortOperatorGateId(trip.getPortOperatorGateId());
                portAccess.setVehicleRegistration(trip.getVehicleRegistration());
                portAccess.setDriverMsisdn(trip.getDriverMsisdn());
                portAccess.setReasonDeny(ServerConstants.DEFAULT_STRING);
                portAccess.setDateDeny(null);
                portAccess.setOperatorIdEntry(operatorId);
                portAccess.setOperatorGate(selectedZone);
                portAccess.setOperatorId(operatorId);
                portAccess.setEntryLaneId(entryLaneId == ServerConstants.ZERO_INT ? ServerConstants.DEFAULT_LONG : entryLaneId);
                portAccess.setEntryLaneNumber(lane == null ? ServerConstants.DEFAULT_INT : lane.getLaneNumber()); // TODO Remove null check after passing right laneId
                portAccess.setExitLaneId(ServerConstants.DEFAULT_LONG);
                portAccess.setExitLaneNumber(ServerConstants.DEFAULT_INT);
                portAccess.setDateEntry(dateEvent);
                portAccess.setDateExit(null);
                portAccess.setDateCreated(new Date());
                portAccess.setDateEdited(new Date());

                savePortAccess(portAccess);

                if (!trip.isFeePaid()) {
                    statementService.chargeParkingFee(trip, operatorId);
                }

                trip.setStatus(ServerConstants.TRIP_STATUS_ENTERED_PORT);
                trip.setPortEntryCount(trip.getPortEntryCount() + 1);
                trip.setDateEntryPort(dateEvent);

                tripService.updateTrip(trip);

            } else {
                // subsequent port entry by same vehicle on trip for same urgent mission

                LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());
                LocalDate tripDatePaid = null;

                if (trip.getStatus() == ServerConstants.TRIP_STATUS_IN_FLIGHT) {

                    Trip lastCompletedTrip = tripService.getTheLatestCompletedTripByVehicleRegNumber(trip.getVehicleRegistration());

                    tripDatePaid = lastCompletedTrip.getDateFeePaid().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                } else {
                    tripDatePaid = trip.getDateFeePaid().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }

                boolean isAccountChargeForReentry = true;

                if ((trip.getPortOperatorId() == ServerConstants.PORT_OPERATOR_TM_SOUTH || trip.getPortOperatorId() == ServerConstants.PORT_OPERATOR_DAKAR_TERMINAL)
                    && currentDate.getMonthValue() == tripDatePaid.getMonthValue() && currentDate.getDayOfMonth() == tripDatePaid.getDayOfMonth()) {
                    isAccountChargeForReentry = false;
                }

                portAccess = new PortAccess();

                if (trip.getStatus() == ServerConstants.TRIP_STATUS_IN_FLIGHT) {
                    trip.setStatus(ServerConstants.TRIP_STATUS_ENTERED_PORT);
                    trip.setDateSlotRequested(dateEvent);
                    trip.setDateSlotApproved(dateEvent);
                    trip.setPortEntryCount(1);
                    trip.setDateEntryPort(dateEvent);
                    trip.setDateEdited(new Date());

                    if (isAccountChargeForReentry) {
                        statementService.chargeParkingFee(trip, operatorId);

                    } else {
                        trip.setFeePaid(true);
                        trip.setFeeAmount(BigDecimal.ZERO);
                        trip.setDateFeePaid(new Date());
                    }

                    tripService.updateTrip(trip);

                    portAccess.setTripId(trip.getId());

                } else {

                    Trip newTrip = new Trip();
                    newTrip.setCode(SecurityUtil.generateUniqueCode());
                    newTrip.setType(ServerConstants.TRIP_TYPE_BOOKED);
                    newTrip.setStatus(ServerConstants.TRIP_STATUS_ENTERED_PORT);
                    newTrip.setAccountId(trip.getAccountId());
                    newTrip.setMission(trip.getMission());
                    newTrip.setVehicleId(trip.getVehicleId());
                    newTrip.setDriverId(trip.getDriverId());
                    newTrip.setPortOperatorId(trip.getMission().getPortOperatorId());
                    newTrip.setIndependentPortOperatorId(trip.getMission().getIndependentPortOperatorId());
                    newTrip.setTransactionType(trip.getMission().getTransactionType());
                    newTrip.setPortOperatorGateId(trip.getMission().getPortOperatorGateId());
                    newTrip.setParkingPermissionId(trip.getParkingPermissionId());
                    newTrip.setParkingPermissionIdParkingEntryFirst(ServerConstants.DEFAULT_LONG);
                    newTrip.setParkingPermissionIdParkingEntry(ServerConstants.DEFAULT_LONG);
                    newTrip.setParkingPermissionIdParkingExit(ServerConstants.DEFAULT_LONG);
                    newTrip.setParkingPermissionIdPortEntry(trip.getParkingPermissionIdPortEntry());
                    newTrip.setParkingPermissionIdPortExit(ServerConstants.DEFAULT_LONG);
                    newTrip.setReferenceNumber(trip.getReferenceNumber());
                    newTrip.setVehicleRegistration(vehicleRegNumber);
                    newTrip.setVehicleRegistrationCountryISO(trip.getVehicleRegistrationCountryISO());
                    newTrip.setContainerId(trip.getContainerId() == null ? ServerConstants.DEFAULT_STRING : trip.getContainerId());
                    newTrip.setContainerType(trip.getContainerType() == null ? ServerConstants.DEFAULT_STRING : trip.getContainerType());
                    newTrip.setDriverMsisdn(trip.getDriverMsisdn());
                    newTrip.setDriverLanguageId(trip.getDriverLanguageId());
                    newTrip.setCompanyName(trip.getCompanyName() == null ? ServerConstants.DEFAULT_STRING : trip.getCompanyName());
                    newTrip.setOperatorIdCreated(operatorId);
                    newTrip.setOperatorId(operatorId);
                    newTrip.setLaneSessionId(ServerConstants.DEFAULT_LONG);
                    newTrip.setFeePaid(false);
                    newTrip.setCurrency(ServerConstants.CURRENCY_CFA_FRANC);
                    newTrip.setFeeAmount(systemService.getTripFeeAmount(trip.getPortOperatorId(), trip.getMission().getTransactionType(), trip.getVehicleRegistrationCountryISO()));
                    newTrip.setOperatorFeeAmount(systemService.getOperatorTripFeeAmount(trip.getPortOperatorId(), trip.getMission().getTransactionType()));
                    newTrip.setDateSlotRequested(dateEvent);
                    newTrip.setDateSlotApproved(dateEvent);
                    newTrip.setParkingEntryCount(0);
                    newTrip.setParkingExitCount(0);
                    newTrip.setPortEntryCount(1);
                    newTrip.setPortExitCount(0);
                    newTrip.setIsDirectToPort(trip.getIsDirectToPort());
                    newTrip.setIsAllowMultipleEntries(trip.getIsAllowMultipleEntries());
                    newTrip.setReasonDeny(ServerConstants.DEFAULT_STRING);
                    newTrip.setDateEntryPort(dateEvent);
                    newTrip.setDateCreated(new Date());
                    newTrip.setDateEdited(newTrip.getDateCreated());

                    missionService.saveMissionTrip(newTrip);

                    if (isAccountChargeForReentry) {
                        statementService.chargeParkingFee(newTrip, operatorId);

                    } else {
                        newTrip.setFeePaid(true);
                        newTrip.setFeeAmount(BigDecimal.ZERO);
                        newTrip.setDateFeePaid(new Date());
                    }

                    tripService.updateTrip(newTrip);

                    portAccess.setTripId(newTrip.getId());

                }

                portAccess.setCode(SecurityUtil.generateUniqueCode());
                portAccess.setStatus(ServerConstants.PORT_ACCESS_STATUS_ENTRY);
                portAccess.setParkingId(ServerConstants.DEFAULT_LONG);
                portAccess.setMissionId(trip.getMission().getId());
                portAccess.setVehicleId(trip.getVehicleId());
                portAccess.setDriverId(trip.getDriverId());
                portAccess.setPortOperatorId(trip.getPortOperatorId());
                portAccess.setPortOperatorGateId(trip.getPortOperatorGateId());
                portAccess.setVehicleRegistration(trip.getVehicleRegistration());
                portAccess.setDriverMsisdn(trip.getDriverMsisdn());
                portAccess.setReasonDeny(ServerConstants.DEFAULT_STRING);
                portAccess.setDateDeny(null);
                portAccess.setOperatorIdEntry(operatorId);
                portAccess.setOperatorGate(selectedZone);
                portAccess.setOperatorId(operatorId);
                portAccess.setEntryLaneId(entryLaneId);
                portAccess.setEntryLaneNumber(lane == null ? ServerConstants.DEFAULT_INT : lane.getLaneNumber()); // TODO Remove null check after passing right laneId
                portAccess.setExitLaneId(ServerConstants.DEFAULT_LONG);
                portAccess.setExitLaneNumber(ServerConstants.DEFAULT_INT);
                portAccess.setDateEntry(dateEvent);
                portAccess.setDateExit(null);
                portAccess.setDateCreated(new Date());
                portAccess.setDateEdited(new Date());

                savePortAccess(portAccess);

                if (operatorId != ServerConstants.DEFAULT_LONG) {
                    activityLogService.saveActivityLogPortAccess(ServerConstants.ACTIVITY_LOG_PORT_ENTRY, operatorId, portAccess.getId());
                }

                // for urgent trips, we are not going to disable port entry permission after vehicle enters the port
            }

        } catch (PADValidationException pade) {
            throw pade;

        } catch (Exception e) {
            logger.error("processUrgentTripPortEntry###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "processUrgentTripPortEntry#Exception#" + e.getMessage());
        }

        return portAccess;
    }

    @Override
    public PortAccessJson findEnteredVehicleByRegNumber(String vehicleRegistration) throws PADException, PADValidationException {

        PortAccessJson portAccessJson = null;

        try {
            PortAccess portAccess = getEnteredPortAccessByVehicleRegistration(vehicleRegistration);

            if (portAccess == null)
                throw new PADValidationException(ServerResponseConstants.VEHICLE_NOT_FOUND_PROCEED_EXIT_CODE, ServerResponseConstants.VEHICLE_NOT_FOUND_PROCEED_EXIT_TEXT,
                    "findEnteredVehicleByRegNumber#portAccess is null");

            Trip trip = tripService.getTripById(portAccess.getTripId());

            portAccessJson = new PortAccessJson();
            portAccessJson.setCode(portAccess.getCode());

            portAccessJson.setStatus(portAccess.getStatus());
            portAccessJson.setVehicleRegistration(portAccess.getVehicleRegistration());

            try {
                portAccessJson.setDatePortEntryString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, portAccess.getDateEntry()));
            } catch (ParseException e) {
                portAccessJson.setDatePortEntryString("");
            }

            portAccessJson.setDriverMobile(portAccess.getDriverMsisdn());
            portAccessJson.setIsAdHoc((trip != null && trip.getType() == ServerConstants.TRIP_TYPE_ADHOC) ? true : false);

            Mission mission = missionService.getMissionById(portAccess.getMissionId());
            if (mission != null) {
                portAccessJson.setTransactionType(mission.getTransactionType());
                portAccessJson.setReferenceNumber(mission.getReferenceNumber());
                portAccessJson.setPortOperator(mission.getPortOperatorId());

                long independentPortOperatorId = mission.getIndependentPortOperatorId();

                IndependentPortOperator independentPortOperator = independentPortOperatorId == ServerConstants.DEFAULT_LONG ? null
                    : systemService.getIndependentPortOperatorById(independentPortOperatorId);

                portAccessJson.setIndependentPortOperatorName(independentPortOperator == null ? "" : independentPortOperator.getName());

                if (mission.getAccountId() != ServerConstants.DEFAULT_LONG) {
                    Account account = accountService.getAccountById(mission.getAccountId());

                    if (account != null) {
                        portAccessJson.setAccountNumber(account.getNumber());
                        portAccessJson.setAccountType(account.getType());
                        portAccessJson.setAccountName(account.getFirstName() + " " + account.getLastName());
                        portAccessJson.setCompanyName(account.getCompanyName());
                    }
                }
            }

            if (trip != null) {
                portAccessJson.setTripCode(trip.getCode());
                portAccessJson.setContainerId(trip.getContainerId());

                try {
                    portAccessJson.setDateSlotString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, trip.getDateSlotApproved()));
                } catch (ParseException e) {
                    portAccessJson.setDateSlotString("");
                }
            }

            Driver driver = driverService.getDriverById(portAccess.getDriverId());
            if (driver != null) {
                portAccessJson.setDriverName(driver.getFirstName() + " " + driver.getLastName());
                portAccessJson.setDriverLicenceNumber(driver.getLicenceNumber());
            }

        } catch (PADValidationException pade) {
            throw pade;

        } catch (Exception e) {
            logger.error("findEnteredVehicleByRegNumber###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "findEnteredVehicleByRegNumber#Exception#" + e.getMessage());
        }

        return portAccessJson;
    }

    @Override
    @Transactional(rollbackFor = { PADException.class, PADValidationException.class, Exception.class })
    public void processVehiclePortExit(String portAccessCode, String vehicleRegistration, long operatorId, long exitLaneId, Date dateEvent)
        throws PADException, PADValidationException {

        try {
            PortAccess portAccess = null;

            Lane lane = laneService.getLaneByLaneId(exitLaneId);

            // TODO remove check 'entryLaneId > 0'. This check was added to avoid exception while there's no way to know the right laneId (event triggered manually)
            if (lane == null && exitLaneId > ServerConstants.ZERO_INT)
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "processVehiclePortExit#lane is null");

            if (StringUtils.isNotBlank(portAccessCode) && StringUtils.isBlank(vehicleRegistration)) {
                // ANPR port exit event
                portAccess = getPortAccessByCode(portAccessCode);

                if (portAccess == null)
                    throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                        "processVehiclePortExit#portAccess is null");

                if (portAccess.getStatus() == ServerConstants.PORT_ACCESS_STATUS_EXIT)
                    throw new PADValidationException(ServerResponseConstants.VEHICLE_ALREADY_EXITED_PORT_AREA_CODE, ServerResponseConstants.VEHICLE_ALREADY_EXITED_PORT_AREA_TEXT,
                        "processVehiclePortExit#Vehicle has already exited the port");

                Trip trip = tripService.getTripById(portAccess.getTripId());

                if (trip == null)
                    throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                        "processVehiclePortExit#trip is null");

                portAccess.setStatus(ServerConstants.PORT_ACCESS_STATUS_EXIT);
                portAccess.setOperatorId(operatorId);
                portAccess.setExitLaneId(exitLaneId == ServerConstants.ZERO_INT ? ServerConstants.DEFAULT_LONG : exitLaneId);
                portAccess.setExitLaneNumber(lane == null ? ServerConstants.DEFAULT_INT : lane.getLaneNumber()); // TODO Remove null check after passing right laneId
                portAccess.setDateExit(dateEvent);
                portAccess.setDateEdited(new Date());

                updatePortAccess(portAccess);

                if (trip.getStatus() == ServerConstants.TRIP_STATUS_ENTERED_PORT) {

                    trip.setStatus(ServerConstants.TRIP_STATUS_COMPLETED);

                    Mission mission = trip.getMission();
                    mission.setTripsCompletedCount(mission.getTripsCompletedCount() + 1);

                    missionService.updateMission(mission);

                    if (trip.getIsAllowMultipleEntries() && trip.getIsDirectToPort()) {

                        Trip newTrip = new Trip();
                        newTrip.setCode(SecurityUtil.generateUniqueCode());
                        newTrip.setType(ServerConstants.TRIP_TYPE_BOOKED);
                        newTrip.setStatus(ServerConstants.TRIP_STATUS_IN_FLIGHT);
                        newTrip.setAccountId(trip.getAccountId());
                        newTrip.setMission(trip.getMission());
                        newTrip.setVehicleId(trip.getVehicleId());
                        newTrip.setDriverId(trip.getDriverId());
                        newTrip.setPortOperatorId(trip.getMission().getPortOperatorId());
                        newTrip.setIndependentPortOperatorId(trip.getMission().getIndependentPortOperatorId());
                        newTrip.setTransactionType(trip.getMission().getTransactionType());
                        newTrip.setPortOperatorGateId(trip.getMission().getPortOperatorGateId());
                        newTrip.setParkingPermissionId(trip.getParkingPermissionIdParkingEntry());
                        newTrip.setParkingPermissionIdParkingEntryFirst(trip.getParkingPermissionIdParkingEntryFirst());
                        newTrip.setParkingPermissionIdParkingEntry(trip.getParkingPermissionIdParkingEntry());
                        newTrip.setParkingPermissionIdParkingExit(trip.getParkingPermissionIdParkingExit());
                        newTrip.setParkingPermissionIdPortEntry(trip.getParkingPermissionIdPortEntry());
                        newTrip.setParkingPermissionIdPortExit(trip.getParkingPermissionIdPortExit());
                        newTrip.setReferenceNumber(trip.getReferenceNumber());
                        newTrip.setVehicleRegistration(trip.getVehicleRegistration());
                        newTrip.setVehicleRegistrationCountryISO(trip.getVehicleRegistrationCountryISO());
                        newTrip.setContainerId(trip.getContainerId() == null ? ServerConstants.DEFAULT_STRING : trip.getContainerId());
                        newTrip.setContainerType(trip.getContainerType() == null ? ServerConstants.DEFAULT_STRING : trip.getContainerType());
                        newTrip.setDriverMsisdn(trip.getDriverMsisdn());
                        newTrip.setDriverLanguageId(trip.getDriverLanguageId());
                        newTrip.setCompanyName(trip.getCompanyName() == null ? ServerConstants.DEFAULT_STRING : trip.getCompanyName());
                        newTrip.setOperatorIdCreated(operatorId);
                        newTrip.setOperatorId(operatorId);
                        newTrip.setLaneSessionId(ServerConstants.DEFAULT_LONG);
                        newTrip.setFeePaid(false);
                        newTrip.setDateFeePaid(null);
                        newTrip.setCurrency(ServerConstants.CURRENCY_CFA_FRANC);
                        newTrip.setFeeAmount(
                            systemService.getTripFeeAmount(trip.getPortOperatorId(), trip.getMission().getTransactionType(), trip.getVehicleRegistrationCountryISO()));
                        newTrip.setOperatorFeeAmount(systemService.getOperatorTripFeeAmount(trip.getPortOperatorId(), trip.getMission().getTransactionType()));
                        newTrip.setDateSlotRequested(dateEvent);
                        newTrip.setDateSlotApproved(dateEvent);
                        newTrip.setParkingEntryCount(0);
                        newTrip.setParkingExitCount(0);
                        newTrip.setPortEntryCount(0);
                        newTrip.setPortExitCount(0);
                        newTrip.setIsDirectToPort(trip.getIsDirectToPort());
                        newTrip.setIsAllowMultipleEntries(trip.getIsAllowMultipleEntries());
                        newTrip.setReasonDeny(ServerConstants.DEFAULT_STRING);
                        newTrip.setDateCreated(new Date());
                        newTrip.setDateEdited(newTrip.getDateCreated());

                        missionService.saveMissionTrip(newTrip);
                    }

                }
                trip.setPortExitCount(trip.getPortExitCount() + 1);
                trip.setDateExitPort(dateEvent);

                tripService.updateTrip(trip);

                if ((trip.getIsDirectToPort() && !trip.getIsAllowMultipleEntries()) || !trip.getIsDirectToPort()) {
                    // remove the port entry parking permission
                    anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_ENTRY, ServerConstants.DEFAULT_LONG, trip, null, null,
                        new Date());

                    // remove the port exit parking permission
                    anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PORT_EXIT, ServerConstants.DEFAULT_LONG, trip, null, null,
                        new Date(System.currentTimeMillis() + 5l * 1000l));
                }

            } else if (StringUtils.isBlank(portAccessCode) && StringUtils.isNotBlank(vehicleRegistration)) {
                // MANUAL vehicle search by port exit operator
                portAccess = new PortAccess();
                portAccess.setCode(SecurityUtil.generateUniqueCode());
                portAccess.setStatus(ServerConstants.PORT_ACCESS_STATUS_EXIT);
                portAccess.setParkingId(ServerConstants.DEFAULT_LONG);
                portAccess.setTripId(ServerConstants.DEFAULT_LONG);
                portAccess.setMissionId(ServerConstants.DEFAULT_LONG);
                portAccess.setVehicleId(ServerConstants.DEFAULT_LONG);
                portAccess.setDriverId(ServerConstants.DEFAULT_LONG);
                portAccess.setPortOperatorId(ServerConstants.DEFAULT_LONG);
                portAccess.setPortOperatorGateId(ServerConstants.DEFAULT_LONG);
                portAccess.setVehicleRegistration(vehicleRegistration);
                portAccess.setDriverMsisdn(ServerConstants.DEFAULT_STRING);
                portAccess.setReasonDeny(ServerConstants.DEFAULT_STRING);
                portAccess.setDateDeny(null);
                portAccess.setOperatorIdEntry(ServerConstants.DEFAULT_LONG);
                portAccess.setOperatorGate(ServerConstants.DEFAULT_STRING);
                portAccess.setOperatorId(operatorId);
                portAccess.setEntryLaneId(ServerConstants.DEFAULT_LONG);
                portAccess.setEntryLaneNumber(ServerConstants.DEFAULT_INT);
                portAccess.setExitLaneId(exitLaneId);
                portAccess.setExitLaneNumber(lane == null ? ServerConstants.DEFAULT_INT : lane.getLaneNumber()); // TODO Remove null check after passing right laneId
                portAccess.setDateEntry(null);
                portAccess.setDateExit(dateEvent);
                portAccess.setDateCreated(new Date());
                portAccess.setDateEdited(portAccess.getDateCreated());

                savePortAccess(portAccess);
            }

            if (operatorId != ServerConstants.DEFAULT_LONG) {
                activityLogService.saveActivityLogPortAccess(ServerConstants.ACTIVITY_LOG_PORT_EXIT, operatorId, portAccess.getId());
            }

        } catch (PADException | PADValidationException pade) {
            throw pade;

        } catch (Exception e) {
            logger.error("processVehiclePortExit###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "processVehiclePortExit#Exception#" + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PortAccess> getExpiredPortEntries(int minutesSinceDateEntry) {

        String[] paramNames = { "statusEntry", "minutesSinceDateEntry" };
        Object[] paramValues = { ServerConstants.PORT_ACCESS_STATUS_ENTRY, new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(minutesSinceDateEntry)) };

        List<PortAccess> portAccessList = (List<PortAccess>) hibernateTemplate
            .findByNamedParam("FROM PortAccess WHERE status = :statusEntry AND dateEntry < :minutesSinceDateEntry", paramNames, paramValues);

        return portAccessList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PortAccess getLastPortAccessByRegNumber(String vehicleRegistration) {
        PortAccess portAccess = null;

        List<PortAccess> portAccessList = (List<PortAccess>) hibernateTemplate
            .findByNamedParam("FROM PortAccess WHERE vehicleRegistration = :vehicleRegistration ORDER BY dateEntry DESC", "vehicleRegistration", vehicleRegistration);
        if (portAccessList != null && !portAccessList.isEmpty()) {
            portAccess = portAccessList.get(0);
        }

        return portAccess;
    }

    @Override
    public boolean isPortEntryAllowedInZone(String zoneName, Long gateId) {
        boolean isPortEntryAllowed = true;

        if (!zoneName.isBlank() && gateId != null) {
            PreparedJDBCQuery query = new PreparedJDBCQuery();

            query.append("SELECT COUNT(zonesGates.id)");
            query.append(" FROM pad.port_entry_zones_port_operator_gates zonesGates");
            query.append(" INNER JOIN port_entry_zones portEntryZones ON zonesGates.port_entry_zone_id = portEntryZones.id");
            query.append(" WHERE zonesGates.port_operator_gate_id = ? AND portEntryZones.name = ?");

            query.addQueryParameter(gateId);
            query.addQueryParameter(zoneName);

            long count = jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Long.class);

            isPortEntryAllowed = count > 0;
        }
        return isPortEntryAllowed;
    }
}
