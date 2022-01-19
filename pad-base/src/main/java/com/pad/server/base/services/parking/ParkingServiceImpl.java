package com.pad.server.base.services.parking;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.type.TimestampType;
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
import com.pad.server.base.entities.AnprEntryScheduler;
import com.pad.server.base.entities.AnprParameter;
import com.pad.server.base.entities.Driver;
import com.pad.server.base.entities.IndependentPortOperator;
import com.pad.server.base.entities.Lane;
import com.pad.server.base.entities.Mission;
import com.pad.server.base.entities.Parking;
import com.pad.server.base.entities.PortAccessWhitelist;
import com.pad.server.base.entities.Sms;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.entities.Vehicle;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ParkingJson;
import com.pad.server.base.jsonentities.api.PortAccessWhitelistJson;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.anpr.AnprBaseService;
import com.pad.server.base.services.driver.DriverService;
import com.pad.server.base.services.lane.LaneService;
import com.pad.server.base.services.mission.MissionService;
import com.pad.server.base.services.portaccess.PortAccessService;
import com.pad.server.base.services.session.SessionService;
import com.pad.server.base.services.sms.SmsService;
import com.pad.server.base.services.statement.StatementService;
import com.pad.server.base.services.system.SystemService;
import com.pad.server.base.services.trip.TripService;
import com.pad.server.base.services.vehicle.VehicleService;

@Service
@Transactional
public class ParkingServiceImpl implements ParkingService {

    private static final Logger logger = Logger.getLogger(ParkingServiceImpl.class);

    @Autowired
    private HibernateTemplate   hibernateTemplate;

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private SessionFactory      sessionFactory;

    @Autowired
    private AccountService      accountService;

    @Autowired
    private AnprBaseService     anprBaseService;

    @Autowired
    private ActivityLogService  activityLogService;

    @Autowired
    private DriverService       driverService;

    @Autowired
    private LaneService         laneService;

    @Autowired
    private MissionService      missionService;

    @Autowired
    private PortAccessService   portAccessService;

    @Autowired
    private SessionService      sessionService;

    @Autowired
    private SystemService       systemService;

    @Autowired
    private SmsService          smsService;

    @Autowired
    private StatementService    statementService;

    @Autowired
    private TripService         tripService;

    @Autowired
    private VehicleService      vehicleService;

    @Override
    public long getParkingCount(Integer portOperatorId, Integer transactionType, String vehicleRegistration, Integer type, Integer status, Boolean isInTransit, Date dateEntryFrom,
        Date dateEntryTo) {

        PreparedJDBCQuery query = getParkingQuery(PreparedJDBCQuery.QUERY_TYPE_COUNT, portOperatorId, transactionType, vehicleRegistration, type, status, isInTransit,
            dateEntryFrom, dateEntryTo);

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Long.class);
    }

    private PreparedJDBCQuery getParkingQuery(int queryType, Integer portOperatorId, Integer transactionType, String vehicleRegistration, Integer type, Integer status,
        Boolean isInTransit, Date dateEntryFrom, Date dateEntryTo) {

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        if (queryType == PreparedJDBCQuery.QUERY_TYPE_COUNT) {

            query.append("SELECT COUNT(parking.id)");
            query.append(" FROM pad.parking parking  ");

        } else if (queryType == PreparedJDBCQuery.QUERY_TYPE_SELECT) {

            query.append(
                "SELECT parking.code, parking.type, parking.status, parking.is_eligible_port_entry, parking.vehicle_registration, parking.vehicle_color, parking.driver_msisdn, "
                    + "parking.date_entry, parking.date_exit, parking.date_sms_exit, parking.entry_lane_id, parking.entry_lane_number, mission.reference_number, mission.transaction_type, trip.code, "
                    + "mission.code, mission.independent_port_operator_id, vehicle.code, vehicle.make, driver.code, driver.first_name, driver.last_name, trip.date_slot_approved, parking.port_operator_id, "
                    + "parking.port_operator_gate_id, parking.vehicle_state FROM pad.parking parking ");
        }

        query.append(" LEFT JOIN pad.missions mission on parking.mission_id = mission.id");
        query.append(" LEFT JOIN pad.trips trip on parking.trip_id = trip.id");
        query.append(" LEFT JOIN pad.drivers driver on parking.driver_id = driver.id");
        query.append(" LEFT JOIN pad.vehicles vehicle on parking.vehicle_id = vehicle.id");
        query.append(" WHERE (1=1)");

        if (status == null) {
            query.append(" AND parking.status IN (?,?) ");
            query.addQueryParameter(ServerConstants.PARKING_STATUS_ENTRY);
            query.addQueryParameter(ServerConstants.PARKING_STATUS_REMINDER_EXIT_DUE);

        } else if (status != ServerConstants.PARKING_STATUS_ALL) {
            query.append(" AND parking.status = ? ");
            query.addQueryParameter(status);
        }

        if (type == null || type == ServerConstants.PARKING_TYPE_ALL) {
            query.append(" AND parking.type IN (?,?)");
            query.addQueryParameter(ServerConstants.PARKING_TYPE_PARKING);
            query.addQueryParameter(ServerConstants.PARKING_TYPE_EXIT_ONLY);

        } else {
            query.append(" AND parking.type = ?");
            query.addQueryParameter(type);
        }

        if (isInTransit != null && isInTransit) {
            query.append(" AND parking.is_eligible_port_entry = ?");
            query.addQueryParameter(isInTransit);
        }

        if (StringUtils.isNotBlank(vehicleRegistration)) {
            query.append(" AND parking.vehicle_registration LIKE ?");
            query.addQueryParameter("%" + vehicleRegistration + "%");
        }

        if (dateEntryFrom != null) {
            query.append(" AND parking.date_entry >= ?");
            query.addQueryParameter(dateEntryFrom);
        }

        if (dateEntryTo != null) {
            query.append(" AND parking.date_entry <= ?");
            query.addQueryParameter(dateEntryTo);
        }

        if (portOperatorId != null) {
            query.append(" AND parking.port_operator_id = ?");
            query.addQueryParameter(portOperatorId);
        }

        if (transactionType != null && transactionType != ServerConstants.DEFAULT_INT) {
            query.append(" AND mission.transaction_type = ?");
            query.addQueryParameter(transactionType);
        }

        return query;
    }

    @Override
    public List<ParkingJson> getParkingList(Integer portOperatorId, Integer transactionType, String vehicleRegistration, Integer type, Integer status, Boolean isInTransit,
        Date dateEntryFrom, Date dateEntryTo, String sortColumn, boolean sortAsc, int startLimit, int endLimit) throws SQLException {

        final List<ParkingJson> parkingList = new ArrayList<>();

        PreparedJDBCQuery query = getParkingQuery(PreparedJDBCQuery.QUERY_TYPE_SELECT, portOperatorId, transactionType, vehicleRegistration, type, status, isInTransit,
            dateEntryFrom, dateEntryTo);

        if (endLimit != ServerConstants.DEFAULT_INT) {
            String tableAlias; // this field is on "trips" table (alias = trip)
            if ("date_slot_approved".equals(sortColumn)) {
                tableAlias = "trip";
            } else if ("reference_number".equals(sortColumn) || "transaction_type".equals(sortColumn)) {
                tableAlias = "mission";
            } else {
                tableAlias = "parking";
            }
            if ("driver.first_name, driver.last_name".equals(sortColumn)) {
                query.append(" ORDER BY ").append(sortColumn).append(" ").append("DESC");
            } else {
                query.setSortParameters(sortColumn, sortAsc, tableAlias, "id", "DESC");
            }
            query.append(" LIMIT ").append(startLimit).append(", ").append(endLimit);
        }

        jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                ParkingJson parkingJson = new ParkingJson();
                parkingJson.setCode(rs.getString("parking.code"));
                parkingJson.setType(rs.getInt("parking.type"));
                parkingJson.setStatus(rs.getInt("parking.status"));
                parkingJson.setVehicleState(rs.getInt("parking.vehicle_state"));
                parkingJson.setIsInTransit(rs.getBoolean("parking.is_eligible_port_entry"));

                if (parkingJson.getType() == ServerConstants.PARKING_TYPE_PARKING) {
                    parkingJson.setReferenceNumber(rs.getString("mission.reference_number"));
                    parkingJson.setTripCode(rs.getString("trip.code"));
                    parkingJson.setMissionCode(rs.getString("mission.code"));
                    parkingJson.setTransactionType(rs.getInt("mission.transaction_type"));
                    parkingJson.setVehicleCode(rs.getString("vehicle.code"));
                    parkingJson.setVehicleMaker(rs.getString("vehicle.make"));
                    parkingJson.setDriverCode(rs.getString("driver.code"));
                    parkingJson.setDriverName(StringUtils.isNotBlank(rs.getString("driver.first_name")) ? rs.getString("driver.first_name") + " " + rs.getString("driver.last_name")
                        : ServerConstants.DEFAULT_STRING);

                    try {
                        parkingJson.setDateSlotString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("trip.date_slot_approved")));
                    } catch (ParseException e) {
                        parkingJson.setDateSlotString("");
                    }

                    parkingJson.setPortOperator(rs.getInt("parking.port_operator_id"));
                    parkingJson.setIndependentOperatorCode(systemService.getIndependentPortOperatorCodeById(rs.getLong("mission.independent_port_operator_id")));
                } else {
                    parkingJson.setDateSlotString(""); // need to have empty string except of null to sort jsons later
                }

                parkingJson.setVehicleRegistration(rs.getString("parking.vehicle_registration"));
                parkingJson.setVehicleColor(rs.getString("parking.vehicle_color"));
                parkingJson.setDriverMobile(rs.getString("parking.driver_msisdn"));
                parkingJson.setEntryLaneId(rs.getInt("parking.entry_lane_id"));
                parkingJson.setEntryLaneNumber(rs.getInt("parking.entry_lane_number"));

                try {
                    parkingJson.setDateEntryString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("parking.date_entry")));
                } catch (ParseException e) {
                    parkingJson.setDateEntryString("");
                }

                try {
                    parkingJson.setDateExitString(
                        rs.getTimestamp("parking.date_exit") != null ? ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("parking.date_exit"))
                            : ServerConstants.DEFAULT_STRING);
                } catch (ParseException e) {
                    parkingJson.setDateExitString(ServerConstants.DEFAULT_STRING);
                }

                try {
                    parkingJson.setDateExitSmsString(rs.getTimestamp("parking.date_sms_exit") != null
                        ? ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("parking.date_sms_exit"))
                        : ServerConstants.DEFAULT_STRING);
                } catch (ParseException e) {
                    parkingJson.setDateExitSmsString(ServerConstants.DEFAULT_STRING);
                }

                parkingList.add(parkingJson);
            }
        }, query.getQueryParameters());

        // next IF should be removed if we want to show slot date for records with type=1
        if ("date_slot_approved".equals(sortColumn)) { // resort data because we don't have parsed date_slot from DB in our list for records with type=1
            if (sortAsc) {
                parkingList.sort((pj1, pj2) -> pj1.getDateSlotString().compareTo(pj2.getDateSlotString()));
            } else {
                parkingList.sort((pj1, pj2) -> pj2.getDateSlotString().compareTo(pj1.getDateSlotString()));
            }
        }

        return parkingList;
    }

    @Override
    public List<Parking> getActiveParkingList(Integer portOperatorId, Integer transactionType, boolean isOrderByDateSmsExit) throws SQLException {

        final List<Parking> parkingList = new ArrayList<>();

        PreparedJDBCQuery query = new PreparedJDBCQuery();
        query.append("SELECT parking.*, mission.transaction_type, mission.reference_number, trip.type, trip.date_slot_approved ");
        query.append(" FROM pad.parking parking ");
        query.append(" INNER JOIN pad.trips trip on parking.trip_id = trip.id");
        query.append(" INNER JOIN pad.missions mission on parking.mission_id = mission.id");
        query.append(" WHERE parking.status IN (?,?) AND parking.type = ? AND trip.status = ? ");

        query.addQueryParameter(ServerConstants.PARKING_STATUS_ENTRY);
        query.addQueryParameter(ServerConstants.PARKING_STATUS_REMINDER_EXIT_DUE);
        query.addQueryParameter(ServerConstants.PARKING_TYPE_PARKING);
        query.addQueryParameter(ServerConstants.TRIP_STATUS_ENTERED_PARKING);

        if (portOperatorId != null && portOperatorId != ServerConstants.DEFAULT_INT) {
            query.append(" AND parking.port_operator_id = ? ");
            query.addQueryParameter(portOperatorId);
        }

        if (transactionType != null && transactionType != ServerConstants.DEFAULT_INT) {
            query.append(" AND mission.transaction_type = ? ");
            query.addQueryParameter(transactionType);
        }

        if (isOrderByDateSmsExit) {
            query.append(" ORDER BY parking.date_sms_exit");

        } else {
            query.append(" ORDER BY trip.date_slot_approved, trip.type, parking.date_entry");
        }

        jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Parking parking = new Parking();
                parking.setId(rs.getLong("parking.id"));
                parking.setCode(rs.getString("parking.code"));
                parking.setType(rs.getInt("parking.type"));
                parking.setStatus(rs.getInt("parking.status"));
                parking.setIsEligiblePortEntry(rs.getBoolean("parking.is_eligible_port_entry"));
                parking.setTripId(rs.getLong("parking.trip_id"));
                parking.setMissionId(rs.getLong("parking.mission_id"));
                parking.setPortAccessWhitelistId(rs.getLong("parking.port_access_whitelist_id"));
                parking.setVehicleId(rs.getLong("parking.vehicle_id"));
                parking.setDriverId(rs.getLong("parking.driver_id"));
                parking.setPortOperatorId(rs.getLong("parking.port_operator_id"));
                parking.setPortOperatorGateId(rs.getLong("parking.port_operator_gate_id"));
                parking.setVehicleState(rs.getInt("vehicle_state"));
                parking.setVehicleRegistration(rs.getString("parking.vehicle_registration"));
                parking.setVehicleColor(rs.getString("parking.vehicle_color"));
                parking.setDriverMsisdn(rs.getString("parking.driver_msisdn"));
                parking.setDriverLanguageId(rs.getLong("parking.driver_language_id"));
                parking.setOperatorId(rs.getLong("parking.operator_id"));
                parking.setEntryLaneId(rs.getLong("parking.entry_lane_id"));
                parking.setEntryLaneNumber(rs.getInt("parking.entry_lane_number"));
                parking.setExitLaneId(rs.getLong("parking.exit_lane_id"));
                parking.setExitLaneNumber(rs.getInt("parking.exit_lane_number"));
                parking.setDateEntry(rs.getTimestamp("parking.date_entry"));
                parking.setDateExit(rs.getTimestamp("parking.date_exit"));
                parking.setDateSmsExit(rs.getTimestamp("parking.date_sms_exit"));
                parking.setDateCreated(rs.getTimestamp("parking.date_created"));
                parking.setDateEdited(rs.getTimestamp("parking.date_edited"));
                parking.setTransactionType(rs.getInt("mission.transaction_type"));
                parking.setMissionReferenceNumber(rs.getString("mission.reference_number"));
                parking.setDateTripSlotApproved(rs.getTimestamp("trip.date_slot_approved"));

                parkingList.add(parking);
            }
        }, query.getQueryParameters());

        return parkingList;
    }

    @Override
    public void exportParkingList(List<ParkingJson> parkingList, OutputStream os) throws IOException {

        SXSSFWorkbook wb = new SXSSFWorkbook(1000);

        Font font = wb.createFont();
        font.setBold(true);

        CellStyle cellStyleBold = wb.createCellStyle();
        cellStyleBold.setFont(font);

        CellStyle cellStyleAmount = wb.createCellStyle();
        cellStyleAmount.setDataFormat((short) 2);

        Sheet sheet = wb.createSheet("Parking Data");
        sheet.createFreezePane(0, 1);

        int row = 0;
        int column = 0;

        Row headerRow = sheet.createRow(row++);

        headerRow.createCell(column).setCellValue("Operator");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Reference Number");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Transaction Type");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Entry Lane Number");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Vehicle");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Driver Name");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Mobile Number");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Status");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Slot Date");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Entry Date");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Release Date");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Exit Date");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        Row dataRow = null;
        for (ParkingJson parkingJson : parkingList) {

            column = 0;

            dataRow = sheet.createRow(row++);

            if (parkingJson.getType() == ServerConstants.PARKING_TYPE_PARKING) {
                dataRow.createCell(column++).setCellValue(systemService.getPortOperatorNameById(parkingJson.getPortOperator()) + " "
                    + systemService.getIndependentPortOperatorNameByCode(parkingJson.getIndependentOperatorCode()));
                dataRow.createCell(column++).setCellValue(parkingJson.getReferenceNumber());
                dataRow.createCell(column++).setCellValue(ServerUtil.getTransactionTypeName(parkingJson.getTransactionType(), ServerConstants.LANGUAGE_EN_ID));
                dataRow.createCell(column++).setCellValue(parkingJson.getEntryLaneId());
                dataRow.createCell(column++).setCellValue(parkingJson.getVehicleRegistration());
                dataRow.createCell(column++).setCellValue(parkingJson.getDriverName());
                dataRow.createCell(column++).setCellValue(parkingJson.getDriverMobile());
                dataRow.createCell(column++).setCellValue(ServerUtil.getParkingStatusNameByStatusId(parkingJson.getStatus()));
                dataRow.createCell(column++).setCellValue(parkingJson.getDateSlotString());
                dataRow.createCell(column++).setCellValue(parkingJson.getDateEntryString());
                dataRow.createCell(column++).setCellValue(parkingJson.getDateExitSmsString());
                dataRow.createCell(column++).setCellValue(parkingJson.getDateExitString());

            } else {
                dataRow.createCell(column++).setCellValue("");
                dataRow.createCell(column++).setCellValue("");
                dataRow.createCell(column++).setCellValue("");
                dataRow.createCell(column++).setCellValue(parkingJson.getEntryLaneId());
                dataRow.createCell(column++).setCellValue(parkingJson.getVehicleRegistration());
                dataRow.createCell(column++).setCellValue("");
                dataRow.createCell(column++).setCellValue(parkingJson.getDriverMobile());
                dataRow.createCell(column++).setCellValue(ServerUtil.getParkingStatusNameByStatusId(parkingJson.getStatus()));
                dataRow.createCell(column++).setCellValue("");
                dataRow.createCell(column++).setCellValue(parkingJson.getDateEntryString());
                dataRow.createCell(column++).setCellValue(parkingJson.getDateExitSmsString());
                dataRow.createCell(column++).setCellValue(parkingJson.getDateExitString());
            }
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

    @SuppressWarnings("unchecked")
    @Override
    public List<Parking> getInTransitParkingSessionForMoreThanXMinutes(int minutes) {

        List<Parking> parkingList = new ArrayList<>();

        Session currentSession = sessionFactory.getCurrentSession();

        parkingList = currentSession.createQuery("FROM Parking p where p.status=:status and p.isEligiblePortEntry = 1 and p.dateExit<=:date_exited_to ORDER BY p.dateEdited")
            .setParameter("status", ServerConstants.PARKING_STATUS_EXIT)
            .setParameter("date_exited_to", new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(minutes)), TimestampType.INSTANCE).list();

        return parkingList;
    }

    @Override
    public long getVehicleExitOnlyCount() {

        StringBuffer query = new StringBuffer();
        List<Object> queryParameters = new ArrayList<>();

        query.append("SELECT COUNT(parking.id)");
        query.append(" FROM pad.parking parking  ");
        query.append(" WHERE parking.type = ? AND parking.status IN (?,?) ");

        queryParameters.add(ServerConstants.PARKING_TYPE_EXIT_ONLY);
        queryParameters.add(ServerConstants.PARKING_STATUS_ENTRY);
        queryParameters.add(ServerConstants.PARKING_STATUS_REMINDER_EXIT_DUE);

        return jdbcTemplate.queryForObject(query.toString(), queryParameters.toArray(), Long.class);
    }

    @Override
    public long getVehicleInTransitCount() {

        StringBuffer query = new StringBuffer();
        List<Object> queryParameters = new ArrayList<>();

        query.append("SELECT COUNT(parking.id)");
        query.append(" FROM pad.parking parking  ");
        query.append(" WHERE parking.type = ? AND parking.status = ? AND parking.is_eligible_port_entry = ? ");

        queryParameters.add(ServerConstants.PARKING_TYPE_PARKING);
        queryParameters.add(ServerConstants.PARKING_STATUS_EXIT);
        queryParameters.add(Boolean.TRUE);

        return jdbcTemplate.queryForObject(query.toString(), queryParameters.toArray(), Long.class);
    }

    @Override
    public long getParkingCountByPortOperator(int portOpertaor) {

        StringBuffer query = new StringBuffer();
        List<Object> queryParameters = new ArrayList<>();

        query.append("SELECT COUNT(parking.id)");
        query.append(" FROM pad.parking parking  ");
        query.append(" INNER JOIN pad.missions mission on parking.mission_id = mission.id");
        query.append(" WHERE parking.status IN (?,?)");
        query.append(" AND mission.port_operator_id = ? ");

        queryParameters.add(ServerConstants.PARKING_STATUS_ENTRY);
        queryParameters.add(ServerConstants.PARKING_STATUS_REMINDER_EXIT_DUE);
        queryParameters.add(portOpertaor);

        return jdbcTemplate.queryForObject(query.toString(), queryParameters.toArray(), Long.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Parking getParkingByVehicleRegistrationAndStatus(String vehicleRegistration, int parkingStatus) {

        Parking parking = null;

        String[] paramNames = { "vehicleRegistration", "status" };
        Object[] paramValues = { vehicleRegistration, parkingStatus };

        List<Parking> parkingList = (List<Parking>) hibernateTemplate.findByNamedParam("FROM Parking WHERE vehicleRegistration = :vehicleRegistration AND status = :status",
            paramNames, paramValues);

        if (parkingList != null && !parkingList.isEmpty()) {
            parking = parkingList.get(0);
        }

        return parking;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Parking getEnteredParkingByVehicleRegistration(String vehicleRegistration) {

        Parking parking = null;

        String[] paramNames = { "vehicleRegistration", "statusEntry", "statusReminderExitDue" };
        Object[] paramValues = { vehicleRegistration, ServerConstants.PARKING_STATUS_ENTRY, ServerConstants.PARKING_STATUS_REMINDER_EXIT_DUE };

        List<Parking> parkingList = (List<Parking>) hibernateTemplate.findByNamedParam(
            "FROM Parking WHERE vehicleRegistration = :vehicleRegistration AND (status = :statusEntry OR status =:statusReminderExitDue)", paramNames, paramValues);

        if (parkingList != null && !parkingList.isEmpty()) {
            parking = parkingList.get(0);
        }

        return parking;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Parking getExitedParkingByVehicleRegistration(String vehicleRegistration) {

        Parking parking = null;

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, systemService.getSystemParameter().getInTransitValidityMinutes() * -1);

        String[] paramNames = { "vehicleRegistration", "status", "isEligiblePortEntry", "dateExited" };
        Object[] paramValues = { vehicleRegistration, ServerConstants.PARKING_STATUS_EXIT, true, cal.getTime() };

        String hql = "FROM Parking WHERE vehicleRegistration = :vehicleRegistration AND status = :status AND isEligiblePortEntry = :isEligiblePortEntry AND dateExit > :dateExited ORDER BY id DESC";

        List<Parking> parkingList = (List<Parking>) hibernateTemplate.findByNamedParam(hql, paramNames, paramValues);

        // Ignore for trips which have already ended
        if (parkingList != null && !parkingList.isEmpty()) {
        	
        	for(Parking p: parkingList) {
        		
	            Trip trip = tripService.getTripById(p.getTripId());
	
	            if (trip.getStatus() != ServerConstants.TRIP_STATUS_COMPLETED && trip.getStatus() != ServerConstants.TRIP_STATUS_COMPLETED_SYSTEM
	                && trip.getStatus() != ServerConstants.TRIP_STATUS_CANCELLED_SYSTEM && trip.getStatus() != ServerConstants.TRIP_STATUS_PORT_EXIT_EXPIRED
	                && trip.getStatus() != ServerConstants.TRIP_STATUS_DENIED_PORT_ENTRY && trip.getStatus() != ServerConstants.TRIP_STATUS_ABORTED 
	                && trip.getStatus() != ServerConstants.TRIP_STATUS_CANCELLED && trip.getStatus() != ServerConstants.TRIP_STATUS_CANCELLED_BY_KIOSK_OPERATOR) {
	                parking = p;
	                break;
	            }
        	}
        }

        return parking;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Parking getParkingSessionByTripId(long tripId) {

        Parking parking = null;

        String[] paramNames = { "tripId" };
        Object[] paramValues = { tripId };

        String hql = "FROM Parking WHERE tripId = :tripId ORDER BY id DESC";

        List<Parking> parkingList = (List<Parking>) hibernateTemplate.findByNamedParam(hql, paramNames, paramValues);

        if (parkingList != null && !parkingList.isEmpty()) {
            parking = parkingList.get(0);
        }

        return parking;
    }

    @Override
    public void saveParking(Parking parking) {

        hibernateTemplate.save(parking);
    }

    @Override
    public void updateParking(Parking parking) {

        parking.setDateEdited(new Date());

        hibernateTemplate.update(parking);
    }

    @Override
    public Parking getParkingById(long id) {

        Parking parking = null;

        parking = hibernateTemplate.get(Parking.class, id);

        return parking;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Parking getParkingByCode(String code) {

        Parking parking = null;

        List<Parking> parkingList = (List<Parking>) hibernateTemplate.findByNamedParam("FROM Parking WHERE code = :code", "code", code);

        if (parkingList != null && !parkingList.isEmpty()) {
            parking = parkingList.get(0);
        }

        return parking;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getEnteredVehicleRegistrationList() {

        List<String> vehicleRegistrationList = new ArrayList<>();

        String[] paramNames = { "statusEntry", "statusReminderExitDue" };
        Object[] paramValues = { ServerConstants.PARKING_STATUS_ENTRY, ServerConstants.PARKING_STATUS_REMINDER_EXIT_DUE };

        List<Parking> parkingList = (List<Parking>) hibernateTemplate.findByNamedParam("FROM Parking WHERE status = :statusEntry OR status =:statusReminderExitDue", paramNames,
            paramValues);

        if (parkingList != null && !parkingList.isEmpty()) {
            vehicleRegistrationList = parkingList.stream().map(result -> result.getVehicleRegistration()).distinct().collect(Collectors.toList());
        }

        return vehicleRegistrationList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getExitedVehicleRegistrationList() {

        List<String> vehicleRegistrationList = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, systemService.getSystemParameter().getInTransitValidityMinutes() * -1);

        String[] paramNames = { "type", "status", "isEligiblePortEntry", "dateExited" };
        Object[] paramValues = { ServerConstants.PARKING_TYPE_PARKING, ServerConstants.PARKING_STATUS_EXIT, true, cal.getTime() };

        List<Parking> parkingList = (List<Parking>) hibernateTemplate.findByNamedParam(
            "FROM Parking WHERE type = :type AND status = :status AND isEligiblePortEntry = :isEligiblePortEntry AND dateExit > :dateExited", paramNames, paramValues);

        // Remove reg numbers of trips which have already ended
        Trip trip = null;
        for (Parking parking : parkingList) {
            trip = tripService.getTripById(parking.getTripId());
            if (trip == null || trip.getStatus() == ServerConstants.TRIP_STATUS_COMPLETED || trip.getStatus() == ServerConstants.TRIP_STATUS_COMPLETED_SYSTEM
                || trip.getStatus() == ServerConstants.TRIP_STATUS_PORT_EXIT_EXPIRED || trip.getStatus() == ServerConstants.TRIP_STATUS_DENIED_PORT_ENTRY) {
                vehicleRegistrationList.remove(parking.getVehicleRegistration());
            }
        }

        if (parkingList != null && !parkingList.isEmpty()) {
            vehicleRegistrationList = parkingList.stream().map(result -> result.getVehicleRegistration()).distinct().collect(Collectors.toList());
        }

        return vehicleRegistrationList;
    }

    @Override
    @Transactional(rollbackFor = PADException.class)
    public void sendExitParkingSms(Trip trip, Parking parking, int addSecondsSchedule) throws PADException {

        parking.setStatus(ServerConstants.PARKING_STATUS_REMINDER_EXIT_DUE);
        parking.setDateSmsExit(new Date());

        int rows = jdbcTemplate.update("UPDATE parking SET status = ?, date_sms_exit = ? WHERE id = ? AND status = ?", parking.getStatus(), parking.getDateSmsExit(),
            parking.getId(), ServerConstants.PARKING_STATUS_ENTRY);

        if (rows == 1) {

            final Mission tripMission = trip.getMission();
            Sms scheduleSms = new Sms();
            scheduleSms.setLanguageId(parking.getDriverLanguageId());
            scheduleSms.setAccountId(ServerConstants.DEFAULT_LONG);
            scheduleSms.setMissionId(tripMission.getId());
            scheduleSms.setTripId(parking.getTripId());
            scheduleSms.setMsisdn(parking.getDriverMsisdn());

            if (addSecondsSchedule > 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, addSecondsSchedule);

                scheduleSms.setDateScheduled(calendar.getTime());
            }

            int smsId = -1;
            HashMap<String, Object> params = new HashMap<>();
            params.put("referenceNumber", tripMission.getReferenceNumber());

            if (scheduleSms.getLanguageId() == ServerConstants.LANGUAGE_EN_ID) {
                smsId = ServerConstants.SMS_EN_EXIT_PARKING_TEMPLATE_ID;
                params.put("referenceLabel", "BAD / Booking Export");

            } else if (scheduleSms.getLanguageId() == ServerConstants.LANGUAGE_BM_ID) {
                smsId = ServerConstants.SMS_BM_EXIT_PARKING_TEMPLATE_ID;
                // TODO add translation
                params.put("referenceLabel", "BAD / Réservation Export");

            } else if (scheduleSms.getLanguageId() == ServerConstants.LANGUAGE_WO_ID) {
                smsId = ServerConstants.SMS_WO_EXIT_PARKING_TEMPLATE_ID;
                // TODO add translation
                params.put("referenceLabel", "BAD / Réservation Export");

            } else {
                smsId = ServerConstants.SMS_FR_EXIT_PARKING_TEMPLATE_ID;
                params.put("referenceLabel", "BAD / Réservation Export");
            }

            smsService.scheduleSmsById(scheduleSms, smsId, params);

            AnprParameter anprParameter = anprBaseService.getAnprParameter();

            try {
                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_EXIT, anprParameter.getAnprZoneIdAgsparking(), trip, null,
                    null, new Date());

            } catch (Exception e) {
                logger.error("sendExitParkingSms###Exception: ", e);
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "sendExitParkingSms#");
            }
        }
    }

    @Override
    public ParkingJson getParkingSessionByVehicleReg(String vehicleRegistration) throws PADException {

        ParkingJson parkingJson = null;

        try {
            Parking parking = getEnteredParkingByVehicleRegistration(ServerUtil.formatVehicleRegNumber(vehicleRegistration));

            if (parking != null) {

                Trip trip = tripService.getTripById(parking.getTripId());

                parkingJson = new ParkingJson();
                parkingJson.setCode(parking.getCode());
                parkingJson.setType(parking.getType());
                parkingJson.setStatus(parking.getStatus());
                parkingJson.setVehicleRegistration(parking.getVehicleRegistration());

                try {
                    parkingJson.setDateEntryString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, parking.getDateEntry()));
                } catch (ParseException e) {
                    parkingJson.setDateEntryString("");
                }

                parkingJson.setDriverMobile(parking.getDriverMsisdn());
                parkingJson.setIsAdHoc((trip != null && trip.getType() == ServerConstants.TRIP_TYPE_ADHOC) ? true : false);

                Mission mission = missionService.getMissionById(parking.getMissionId());
                if (mission != null) {
                    parkingJson.setMissionCode(mission.getCode());
                    parkingJson.setPortOperator(mission.getPortOperatorId());

                    long independentPortOperatorId = mission.getIndependentPortOperatorId();

                    IndependentPortOperator independentPortOperator = independentPortOperatorId == ServerConstants.DEFAULT_LONG ? null
                        : systemService.getIndependentPortOperatorById(independentPortOperatorId);

                    parkingJson.setIndependentPortOperatorName(independentPortOperator == null ? "" : independentPortOperator.getName());
                    parkingJson.setTransactionType(mission.getTransactionType());
                    parkingJson.setReferenceNumber(mission.getReferenceNumber());
                    parkingJson.setPortOperator(mission.getPortOperatorId());

                    if (mission.getAccountId() != ServerConstants.DEFAULT_LONG) {
                        Account account = accountService.getAccountById(mission.getAccountId());

                        if (account != null) {
                            parkingJson.setAccountNumber(account.getNumber());
                            parkingJson.setAccountType(account.getType());
                            parkingJson.setAccountName(account.getFirstName() + " " + account.getLastName());
                            parkingJson.setCompanyName(account.getCompanyName());
                        }
                    }
                }

                if (trip != null) {
                    parkingJson.setTripCode(trip.getCode());
                    parkingJson.setContainerId(trip.getContainerId());

                    try {
                        parkingJson.setDateSlotString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, trip.getDateSlotApproved()));
                    } catch (ParseException e) {
                        parkingJson.setDateSlotString("");
                    }

                    if (parking.getType() == ServerConstants.PARKING_TYPE_PARKING) {
                        // NORMAL PARKING SESSION

                        parkingJson.setGateId(parking.getPortOperatorGateId());

                        if (parking.getStatus() == ServerConstants.PARKING_STATUS_REMINDER_EXIT_DUE) {
                            // Message: Vehicle found. To confirm exit, please tap on Proceed button. Advise the driver they are authorised to proceed to the port entry right away.
                            parkingJson.setParkingExitMessageId(ServerConstants.PARKING_EXIT_MESSAGE_ID_AUTH_EXIT);

                        } else if (parking.getStatus() == ServerConstants.PARKING_STATUS_ENTRY) {
                            // vehicle exited the parking area prematurely before office operator's authorisation
                            parkingJson.setParkingExitMessageId(ServerConstants.PARKING_EXIT_MESSAGE_ID_PREMATURE_EXIT);
                        }

                    } else {
                        // EXIT ONLY SESSION
                        if (parking.getStatus() == ServerConstants.PARKING_STATUS_REMINDER_EXIT_DUE) {
                            // Message: Vehicle found. To confirm exit, please tap on Proceed button. Advise the driver they are authorised to proceed to the port entry right away.
                            parkingJson.setParkingExitMessageId(ServerConstants.PARKING_EXIT_MESSAGE_ID_AUTH_EXIT);
                        } else {
                            parkingJson.setParkingExitMessageId(ServerConstants.PARKING_EXIT_MESSAGE_ID_EXIT_ONLY);
                        }
                    }

                } else {
                    // EXIT ONLY SESSION - No trip (Port Whitelist)
                    if (parking.getType() == ServerConstants.PARKING_TYPE_EXIT_ONLY && parking.getStatus() == ServerConstants.PARKING_STATUS_REMINDER_EXIT_DUE
                        && parking.getPortAccessWhitelistId() != ServerConstants.DEFAULT_LONG) {
                        // Message: Vehicle found. To confirm exit, please tap on Proceed button. Advise the driver they are authorised to proceed to the port entry right away.
                        parkingJson.setParkingExitMessageId(ServerConstants.PARKING_EXIT_MESSAGE_ID_AUTH_EXIT);
                    }
                }

                Driver driver = driverService.getDriverById(parking.getDriverId());
                if (driver != null) {
                    parkingJson.setDriverName(driver.getFirstName() + " " + driver.getLastName());
                    parkingJson.setDriverLicenceNumber(driver.getLicenceNumber());
                }
            }

        } catch (Exception e) {
            logger.error("getParkingSessionByVehicleReg###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "getParkingSessionByVehicleReg#Exception#" + e.getMessage());
        }

        return parkingJson;
    }

    @Override
    @Transactional(rollbackFor = { PADException.class, PADValidationException.class, Exception.class })
    public AnprEntryScheduler processVehicleExit(AnprEntryScheduler anprEntryScheduler, String parkingCode, long parkingPermissionId, String vehicleRegistration, long operatorId,
        long exitLaneId, Date dateEvent) throws PADException {

        try {

            Lane lane = laneService.getLaneByLaneId(exitLaneId);

            // TODO remove check 'entryLaneId > 0'. This check was added to avoid exception while there's no way to know the right laneId (event triggered manually)
            if (lane == null && exitLaneId > ServerConstants.ZERO_INT)
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "processVehicleExit#Lane is null");

            if (StringUtils.isBlank(parkingCode)) {
                // no vehicle with that reg found in the existing parking sessions. record the exit by creating a parking session with status "exit"

                Parking parking = new Parking();
                parking.setCode(SecurityUtil.generateUniqueCode());
                parking.setType(ServerConstants.PARKING_TYPE_PARKING);
                parking.setStatus(ServerConstants.PARKING_STATUS_EXIT);
                parking.setIsEligiblePortEntry(false);
                parking.setTripId(ServerConstants.DEFAULT_LONG);
                parking.setMissionId(ServerConstants.DEFAULT_LONG);
                parking.setPortAccessWhitelistId(ServerConstants.DEFAULT_LONG);
                parking.setVehicleId(ServerConstants.DEFAULT_LONG);
                parking.setDriverId(ServerConstants.DEFAULT_LONG);
                parking.setVehicleState(ServerConstants.VEHICLE_PARKING_STATE_NORMAL);
                parking.setVehicleRegistration(ServerUtil.formatVehicleRegNumber(vehicleRegistration));
                parking.setVehicleColor(ServerConstants.DEFAULT_STRING);
                parking.setDriverMsisdn(StringUtils.EMPTY);
                parking.setOperatorId(operatorId);
                parking.setEntryLaneId(ServerConstants.DEFAULT_LONG);
                parking.setEntryLaneNumber(ServerConstants.DEFAULT_INT);
                parking.setExitLaneId(exitLaneId == ServerConstants.ZERO_INT ? ServerConstants.DEFAULT_LONG : exitLaneId);
                parking.setExitLaneNumber(lane == null ? ServerConstants.DEFAULT_INT : lane.getLaneNumber()); // TODO Remove null check after passing right laneId
                parking.setDateEntry(null);
                parking.setDateExit(dateEvent);
                parking.setDateCreated(new Date());
                parking.setDateEdited(parking.getDateCreated());

                saveParking(parking);

                if (operatorId != ServerConstants.DEFAULT_LONG) {
                    activityLogService.saveActivityLogParking(ServerConstants.ACTIVITY_LOG_PARKING_EXIT_ONLY, operatorId, parking.getId());
                }

            } else {
                Parking parking = getParkingByCode(parkingCode);

                if (parking == null)
                    throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "processVehicleExit#parking is null");

                AnprParameter anprParameter = anprBaseService.getAnprParameter();

                if (parking.getType() == ServerConstants.PARKING_TYPE_PARKING) {
                    // NORMAL parking session
                    Trip trip = tripService.getTripById(parking.getTripId());
                    if (trip == null)
                        throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "processVehicleExit#trip is null");

                    if (trip.getStatus() != ServerConstants.TRIP_STATUS_ENTERED_PARKING)
                        throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT,
                            "processVehicleExit#Trip status is not entered parking");

                    switch (parking.getStatus()) {

                        case ServerConstants.PARKING_STATUS_REMINDER_EXIT_DUE:

                            parking.setIsEligiblePortEntry(true);

                            trip.setStatus(ServerConstants.TRIP_STATUS_IN_TRANSIT);

                            if (trip.getIsDirectToPort()) {
                                // trip on direct to port mission ended up at parking area. remove the parking exit permission
                                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_EXIT, ServerConstants.DEFAULT_LONG, trip, null,
                                    null, new Date());

                            } else if (!trip.getIsDirectToPort() && trip.getIsAllowMultipleEntries()) {
                                // remove the parking exit permission
                                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_EXIT, ServerConstants.DEFAULT_LONG, trip, null,
                                    null, new Date());

                                if (trip.getType() == ServerConstants.TRIP_TYPE_BOOKED) {
                                    // re-enable the original parking entry permission
                                    anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PARKING_ENTRY_STATUS_ENABLED,
                                        ServerConstants.DEFAULT_LONG, trip, null, null, new Date());
                                }

                                // create port entry permission
                                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY,
                                    systemService.getPortOperatorAnprZoneIdById(trip.getPortOperatorGateId()), trip, null, dateEvent,
                                    new Date(System.currentTimeMillis() + 10l * 1000l));

                            } else {
                                // remove the parking entry permission
                                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_ENTRY, ServerConstants.DEFAULT_LONG, trip,
                                    null, null, new Date());

                                // remove the parking exit permission
                                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_EXIT, ServerConstants.DEFAULT_LONG, trip, null,
                                    null, new Date(System.currentTimeMillis() + 5l * 1000l));

                                // add the vehicle reg number to port entry whitelist on ANPR system. Based on port operator gate id determine which zone parking permission request
                                // will be scheduled for
                                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PORT_ENTRY,
                                    systemService.getPortOperatorAnprZoneIdById(trip.getPortOperatorGateId()), trip, null, dateEvent,
                                    new Date(System.currentTimeMillis() + 10l * 1000l));
                            }
                            break;

                        case ServerConstants.PARKING_STATUS_ENTRY:

                            // vehicle exited the parking area prematurely before authorisation
                            parking.setIsEligiblePortEntry(false);

                            trip.setStatus(ServerConstants.TRIP_STATUS_EXITED_PARKING_PREMATURELY);

                            if (!trip.getIsDirectToPort() && trip.getIsAllowMultipleEntries() && trip.getType() == ServerConstants.TRIP_TYPE_BOOKED) {
                                // re-enable the original parking entry permission
                                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PARKING_ENTRY_STATUS_ENABLED,
                                    ServerConstants.DEFAULT_LONG, trip, null, null, new Date());

                            } else {
                                // remove parking permission for vehicle at parking area
                                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_ENTRY, ServerConstants.DEFAULT_LONG, trip,
                                    null, null, new Date());

                                // add the vehicle to parking entry whitelist for the next hour to allow driver to come back to parking as they are not allowed enter the port
                                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_REENTRY_AFTER_PREM_EXIT,
                                    anprParameter.getAnprZoneIdAgsparking(), trip, null, null, new Date());
                            }
                            break;

                        default:
                            break;
                    }

                    trip.setParkingExitCount(trip.getParkingExitCount() + 1);
                    trip.setDateExitParking(dateEvent);
                    trip.setDateEdited(new Date());

                    Session session = sessionFactory.openSession();
                    Transaction tx = session.beginTransaction();

                    session.update(trip);
                    tx.commit();

                    if (session != null) {
                        session.close();
                    }

                    if (anprEntryScheduler != null) {
                        anprEntryScheduler.setTripId(trip.getId());
                        anprEntryScheduler.setMissionId(trip.getMission().getId());
                    }

                } else {
                    // EXIT ONLY parking session
                    parking.setIsEligiblePortEntry(false);

                    Trip trip = null;
                    PortAccessWhitelist portAccessWhitelist = null;

                    if (parking.getTripId() != ServerConstants.DEFAULT_LONG) {
                        trip = tripService.getTripById(parking.getTripId());

                    } else if (parking.getPortAccessWhitelistId() != ServerConstants.DEFAULT_LONG) {
                        portAccessWhitelist = portAccessService.getPortAccessWhitelistById(parking.getPortAccessWhitelistId());
                    }

                    if (trip != null && trip.getParkingPermissionIdParkingExit() != ServerConstants.DEFAULT_LONG) {
                        // remove the parking exit permission
                        anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_EXIT, ServerConstants.DEFAULT_LONG, trip, null, null,
                            new Date(System.currentTimeMillis() + 5l * 1000l));

                    } else if (parkingPermissionId != ServerConstants.DEFAULT_LONG) {
                        Mission missionTmp = new Mission();
                        missionTmp.setId(parking.getMissionId());

                        Trip tripTmp = new Trip();
                        tripTmp.setId(parking.getTripId());
                        tripTmp.setMission(missionTmp);
                        tripTmp.setVehicleRegistration(parking.getVehicleRegistration());
                        tripTmp.setParkingPermissionIdParkingExit(parkingPermissionId);

                        // remove the parking exit permission
                        anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_DELETE_PARKINGPERMISSIONS_PARKING_EXIT, ServerConstants.DEFAULT_LONG, tripTmp, null,
                            null, new Date(System.currentTimeMillis() + 5l * 1000l));
                    }

                    // for a direct to port trip, re-enable port entry parking permission, if any
                    if (trip != null) {
                        if (trip.getStatus() != ServerConstants.TRIP_STATUS_CANCELLED_BY_KIOSK_OPERATOR) {

                            trip.setStatus(ServerConstants.TRIP_STATUS_IN_TRANSIT);
                            trip.setParkingExitCount(trip.getParkingExitCount() + 1);
                            trip.setDateExitParking(dateEvent);

                            tripService.updateTrip(trip);

                            if (trip.getParkingPermissionIdPortEntry() != ServerConstants.DEFAULT_LONG) {
                                parking.setIsEligiblePortEntry(true);

                                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_ENTRY_STATUS_ENABLED,
                                    ServerConstants.DEFAULT_LONG, trip, null, null, new Date(System.currentTimeMillis() + 5l * 1000l));
                            }
                        }

                    } else if (portAccessWhitelist != null && portAccessWhitelist.getParkingPermissionId() != ServerConstants.DEFAULT_LONG) {

                        parking.setIsEligiblePortEntry(true);

                        Mission missionTmp = new Mission();
                        missionTmp.setId(parking.getMissionId());

                        Trip tripTmp = new Trip();
                        tripTmp.setId(parking.getTripId());
                        tripTmp.setMission(missionTmp);
                        tripTmp.setVehicleRegistration(parking.getVehicleRegistration());
                        tripTmp.setParkingPermissionIdPortEntry(portAccessWhitelist.getParkingPermissionId());

                        anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PORT_ENTRY_STATUS_ENABLED, ServerConstants.DEFAULT_LONG,
                            tripTmp, null, null, new Date(System.currentTimeMillis() + 5l * 1000l));
                    }
                }

                parking.setStatus(ServerConstants.PARKING_STATUS_EXIT);
                parking.setExitLaneId(exitLaneId == ServerConstants.ZERO_INT ? ServerConstants.DEFAULT_LONG : exitLaneId);
                parking.setExitLaneNumber(lane == null ? ServerConstants.DEFAULT_INT : lane.getLaneNumber()); // TODO Remove null check after passing right laneId
                parking.setDateExit(dateEvent);
                parking.setDateEdited(new Date());

                if (parking.getVehicleState() == ServerConstants.VEHICLE_PARKING_STATE_BROKEN_DOWN || parking.getVehicleState() == ServerConstants.VEHICLE_PARKING_STATE_CLAMPED) {
                    parking.setVehicleState(ServerConstants.VEHICLE_PARKING_STATE_NORMAL);
                }

                Session session = sessionFactory.openSession();
                Transaction tx = session.beginTransaction();

                session.update(parking);
                tx.commit();

                if (session != null) {
                    session.close();
                }

                if (operatorId != ServerConstants.DEFAULT_LONG) {
                    activityLogService.saveActivityLogParking(ServerConstants.ACTIVITY_LOG_PARKING_EXIT, operatorId, parking.getId());
                }
            }

        } catch (PADException pade) {
            throw pade;

        } catch (Exception e) {
            logger.error("processVehicleExit###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "processVehicleExit#Exception#" + e.getMessage());
        }

        return anprEntryScheduler;
    }

    @Override
    public List<ParkingJson> findExitedVehicleByRegNumber(String vehicleRegistration) throws PADException, PADValidationException {

        ParkingJson parkingJson = new ParkingJson();
        parkingJson.setVehicleRegistration(vehicleRegistration);

        List<ParkingJson> parkingJsonList = new ArrayList<>();

        try {
            Parking parking = getExitedParkingByVehicleRegistration(vehicleRegistration);

            if (parking == null) {
                // no parking session found so search the direct to port eligible trips

                PortAccessWhitelistJson portAccessWhitelistJson = portAccessService.getVehicleWhitelisted(vehicleRegistration);

                if (portAccessWhitelistJson != null) {

                    parkingJson = new ParkingJson();
                    parkingJson.setPortOperator(portAccessWhitelistJson.getPortOperatorId());
                    parkingJson.setGateId(portAccessWhitelistJson.getGateId());
                    parkingJson.setGateNumber(systemService.getPortOperatorGateNumberById(parkingJson.getGateId()));
                    parkingJson.setGateNumberShort(systemService.getPortOperatorGateNumberShortById(parkingJson.getGateId()));
                    parkingJson.setTransactionType(ServerConstants.TRANSACTION_TYPE_WHITELIST);
                    parkingJson.setIsWhitelisted(Boolean.TRUE);

                    parkingJsonList.add(parkingJson);

                } else {

                    Trip authorisedDirectToPortTrip = tripService.getTheLatestAuthorisedDirectToPortTripByVehicleRegNumber(vehicleRegistration);

                    if (authorisedDirectToPortTrip != null) {

                        parkingJson = new ParkingJson();
                        parkingJson.setCode("");
                        parkingJson.setVehicleRegistration(vehicleRegistration);
                        parkingJson.setDriverMobile(authorisedDirectToPortTrip.getDriverMsisdn());
                        parkingJson.setIsAdHoc((authorisedDirectToPortTrip != null && authorisedDirectToPortTrip.getType() == ServerConstants.TRIP_TYPE_ADHOC) ? true : false);
                        parkingJson.setMissionCode(authorisedDirectToPortTrip.getMission().getCode());
                        parkingJson.setTransactionType(authorisedDirectToPortTrip.getMission().getTransactionType());
                        parkingJson.setReferenceNumber(authorisedDirectToPortTrip.getMission().getReferenceNumber());
                        parkingJson.setContainerId(authorisedDirectToPortTrip.getContainerId());

                        long independentPortOperatorId = authorisedDirectToPortTrip.getMission().getIndependentPortOperatorId();

                        IndependentPortOperator independentPortOperator = independentPortOperatorId == ServerConstants.DEFAULT_LONG ? null
                            : systemService.getIndependentPortOperatorById(independentPortOperatorId);

                        parkingJson.setIndependentPortOperatorName(independentPortOperator == null ? "" : independentPortOperator.getName());
                        parkingJson.setPortOperator(authorisedDirectToPortTrip.getMission().getPortOperatorId());
                        parkingJson.setGateId(authorisedDirectToPortTrip.getMission().getPortOperatorGateId());
                        parkingJson.setGateNumber(systemService.getPortOperatorGateNumberById(authorisedDirectToPortTrip.getMission().getPortOperatorGateId()));

                        if (authorisedDirectToPortTrip.getMission().getAccountId() != ServerConstants.DEFAULT_LONG) {
                            Account account = accountService.getAccountById(authorisedDirectToPortTrip.getMission().getAccountId());

                            if (account != null) {
                                parkingJson.setAccountNumber(account.getNumber());
                                parkingJson.setAccountType(account.getType());
                                parkingJson.setAccountName(account.getFirstName() + " " + account.getLastName());
                                parkingJson.setCompanyName(account.getCompanyName());
                            }
                        }

                        parkingJson.setTripCode(authorisedDirectToPortTrip.getCode());

                        try {
                            parkingJson.setDateSlotString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, authorisedDirectToPortTrip.getDateSlotApproved()));
                        } catch (ParseException e) {
                            parkingJson.setDateSlotString("");
                        }

                        Driver driver = driverService.getDriverById(authorisedDirectToPortTrip.getDriverId());
                        if (driver != null) {
                            parkingJson.setDriverName(driver.getFirstName() + " " + driver.getLastName());
                            parkingJson.setDriverLicenceNumber(driver.getLicenceNumber());
                        }

                        parkingJsonList.add(parkingJson);

                    } else
                        throw new PADValidationException(ServerResponseConstants.VEHICLE_NOT_AUTHORIZED_PORT_ENTRY_CODE,
                            ServerResponseConstants.VEHICLE_NOT_AUTHORIZED_PORT_ENTRY_TEXT, "");
                }

            } else {
                Trip trip = tripService.getTripById(parking.getTripId());

                parkingJson = new ParkingJson();
                parkingJson.setCode(parking.getCode());
                parkingJson.setStatus(parking.getStatus());
                parkingJson.setVehicleRegistration(parking.getVehicleRegistration());

                try {
                    parkingJson.setDateEntryString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, parking.getDateEntry()));
                } catch (ParseException e) {
                    parkingJson.setDateEntryString("");
                }

                try {
                    parkingJson.setDateExitString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, parking.getDateExit()));
                } catch (ParseException e) {
                    parkingJson.setDateExitString("");
                }

                parkingJson.setDriverMobile(parking.getDriverMsisdn());
                parkingJson.setIsAdHoc((trip != null && trip.getType() == ServerConstants.TRIP_TYPE_ADHOC) ? true : false);

                Mission mission = missionService.getMissionById(trip == null ? parking.getMissionId() : trip.getMission().getId());
                if (mission != null) {
                    parkingJson.setMissionCode(mission.getCode());
                    parkingJson.setTransactionType(mission.getTransactionType());
                    parkingJson.setReferenceNumber(mission.getReferenceNumber());

                    long independentPortOperatorId = mission.getIndependentPortOperatorId();

                    IndependentPortOperator independentPortOperator = independentPortOperatorId == ServerConstants.DEFAULT_LONG ? null
                        : systemService.getIndependentPortOperatorById(independentPortOperatorId);

                    parkingJson.setIndependentPortOperatorName(independentPortOperator == null ? "" : independentPortOperator.getName());
                    parkingJson.setPortOperator(mission.getPortOperatorId());
                    parkingJson.setGateNumber(systemService.getPortOperatorGateNumberById(mission.getPortOperatorGateId()));

                    if (mission.getAccountId() != ServerConstants.DEFAULT_LONG) {
                        Account account = accountService.getAccountById(mission.getAccountId());

                        if (account != null) {
                            parkingJson.setAccountNumber(account.getNumber());
                            parkingJson.setAccountType(account.getType());
                            parkingJson.setAccountName(account.getFirstName() + " " + account.getLastName());
                            parkingJson.setCompanyName(account.getCompanyName());
                        }
                    }
                }

                if (trip != null) {
                    parkingJson.setTripCode(trip.getCode());
                    parkingJson.setContainerId(trip.getContainerId());

                    try {
                        parkingJson.setDateSlotString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, trip.getDateSlotApproved()));
                    } catch (ParseException e) {
                        parkingJson.setDateSlotString("");
                    }
                }

                Driver driver = driverService.getDriverById(trip == null ? parking.getDriverId() : trip.getDriverId());
                if (driver != null) {
                    parkingJson.setDriverName(driver.getFirstName() + " " + driver.getLastName());
                    parkingJson.setDriverLicenceNumber(driver.getLicenceNumber());
                }

                parkingJsonList.add(parkingJson);
            }

        } catch (PADValidationException pade) {
            throw pade;

        } catch (Exception e) {
            logger.error("findExitedVehicleByRegNumber###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "findExitedVehicleByRegNumber#");
        }

        return parkingJsonList;
    }

    @Override
    @Transactional(rollbackFor = { PADException.class, PADValidationException.class, Exception.class })
    public String processVehicleEntry(String tripCode, long operatorId, long entryLaneId, Date dateEvent) throws PADException, PADValidationException {

        Vehicle vehicle = null;
        Trip trip = null;
        com.pad.server.base.entities.Session session = null;

        try {
            trip = tripService.getTripByCode(tripCode);

            if (trip == null)
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                    "processVehicleEntry#Trip is null");

            if (trip.getMission() == null)
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "processVehicleEntry#Mission is null");

            if (trip.getStatus() != ServerConstants.TRIP_STATUS_APPROVED && trip.getStatus() != ServerConstants.TRIP_STATUS_EXITED_PARKING_PREMATURELY)
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                    "processVehicleEntry#Trip status different than APPROVED or EXITED PREMATURELY");

            Lane lane = laneService.getLaneByLaneId(entryLaneId);

            // TODO remove check 'entryLaneId > 0'. This check was added to avoid exception while there's no way to know the right laneId (event triggered manually)
            if (lane == null && entryLaneId > ServerConstants.ZERO_INT)
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "processVehicleEntry#Lane is null");

            // check if vehicle already entered the parking area
            if (getParkingByVehicleRegistrationAndStatus(trip.getVehicleRegistration(), ServerConstants.PARKING_STATUS_ENTRY) != null)
                throw new PADValidationException(ServerResponseConstants.VEHICLE_ALREADY_ENTERED_PARKING_AREA_CODE,
                    ServerResponseConstants.VEHICLE_ALREADY_ENTERED_PARKING_AREA_TEXT, "processVehicleEntry#Vehicle had already entered the parking area");

            if (trip.getVehicleId() != ServerConstants.DEFAULT_LONG) {
                vehicle = vehicleService.getVehicleById(trip.getVehicleId());
            }

            Parking parking = new Parking();
            parking.setCode(SecurityUtil.generateUniqueCode());
            parking.setType(ServerConstants.PARKING_TYPE_PARKING);
            parking.setStatus(ServerConstants.PARKING_STATUS_ENTRY);
            parking.setIsEligiblePortEntry(false);
            parking.setTripId(trip.getId());
            parking.setMissionId(trip.getMission().getId());
            parking.setPortAccessWhitelistId(ServerConstants.DEFAULT_LONG);
            parking.setVehicleId(trip.getVehicleId());
            parking.setDriverId(trip.getDriverId());
            parking.setPortOperatorId(trip.getPortOperatorId());
            parking.setPortOperatorGateId(trip.getPortOperatorGateId());
            parking.setVehicleState(ServerConstants.VEHICLE_PARKING_STATE_NORMAL);
            parking.setVehicleRegistration(trip.getVehicleRegistration());

            if (vehicle == null) {
                parking.setVehicleColor(ServerConstants.DEFAULT_STRING);
            } else {
                parking.setVehicleColor(StringUtils.isBlank(vehicle.getColor()) ? "" : vehicle.getColor());
            }

            parking.setDriverMsisdn(trip.getDriverMsisdn());
            parking.setDriverLanguageId(trip.getDriverLanguageId());
            parking.setOperatorId(operatorId);
            parking.setEntryLaneId(entryLaneId == ServerConstants.ZERO_INT ? ServerConstants.DEFAULT_LONG : entryLaneId);
            parking.setEntryLaneNumber(lane == null ? ServerConstants.DEFAULT_INT : lane.getLaneNumber()); // TODO Remove null check after passing right laneId
            parking.setExitLaneId(ServerConstants.DEFAULT_LONG);
            parking.setExitLaneNumber(ServerConstants.DEFAULT_INT);
            parking.setDateEntry(dateEvent);
            parking.setDateCreated(new Date());
            parking.setDateEdited(parking.getDateCreated());

            if (operatorId != ServerConstants.DEFAULT_LONG) {
                session = sessionService.getLastSessionByKioskOperatorId(operatorId);
            }

            if (trip.getIsDirectToPort() && trip.getAccountId() != ServerConstants.DEFAULT_LONG) {

                AnprParameter anprParameter = anprBaseService.getAnprParameter();

                // authorise vehicle to leave parking right away
                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_CREATE_PARKINGPERMISSIONS_PARKING_EXIT, anprParameter.getAnprZoneIdAgsparking(), trip, null,
                    null, new Date());

                // the trip fee is charged at port entry in case of trips on urgent mission or direct to port
                // vehicle can go to parking exit directly. office operator doesn't need to authorise it to leave
                parking.setStatus(ServerConstants.PARKING_STATUS_REMINDER_EXIT_DUE);

            } else {
                // check if parking entry fee needs to be charged. in case vehicle exited prematurely the car park within last hour, let the truck in without a fee
                if (trip.getStatus() == ServerConstants.TRIP_STATUS_APPROVED && trip.getAccountId() != ServerConstants.DEFAULT_LONG) {
                    statementService.chargeParkingFee(trip, operatorId);

                    if (session != null) {
                        session.setAccountDeductTransactionCount(session.getAccountDeductTransactionCount() + 1);
                        session.setAccountDeductTransactionTotalAmount(session.getAccountDeductTransactionTotalAmount().add(trip.getFeeAmount()));
                        sessionService.updateSession(session);
                    }

                } else if (trip.getStatus() == ServerConstants.TRIP_STATUS_EXITED_PARKING_PREMATURELY && trip.getAccountId() != ServerConstants.DEFAULT_LONG) {

                    Parking existingParkingSessionForTrip = getParkingSessionByTripId(trip.getId());

                    if (existingParkingSessionForTrip != null) {

                        // check whether date event is < exit date+1hour. that means truck came back to parking entry within 1 hour of parking exit. in that case don't charge the
                        // entry fee otherwise charge

                        final Calendar calendarValidTo = Calendar.getInstance();
                        calendarValidTo.setTime(existingParkingSessionForTrip.getDateExit());
                        calendarValidTo.add(Calendar.HOUR_OF_DAY, 1);

                        if (dateEvent.compareTo(calendarValidTo.getTime()) > 0) {

                            statementService.chargeParkingFee(trip, operatorId);

                            if (session != null) {
                                session.setAccountDeductTransactionCount(session.getAccountDeductTransactionCount() + 1);
                                session.setAccountDeductTransactionTotalAmount(session.getAccountDeductTransactionTotalAmount().add(trip.getFeeAmount()));
                                sessionService.updateSession(session);
                            }
                        }

                    } else {
                        statementService.chargeParkingFee(trip, operatorId);

                        if (session != null) {
                            session.setAccountDeductTransactionCount(session.getAccountDeductTransactionCount() + 1);
                            session.setAccountDeductTransactionTotalAmount(session.getAccountDeductTransactionTotalAmount().add(trip.getFeeAmount()));
                            sessionService.updateSession(session);
                        }
                    }
                }
            }

            saveParking(parking);

            trip.setStatus(ServerConstants.TRIP_STATUS_ENTERED_PARKING);
            trip.setParkingEntryCount(trip.getParkingEntryCount() + 1);

            if (session != null && trip.getLaneSessionId() == ServerConstants.DEFAULT_LONG) {
                trip.setLaneSessionId(session.getId());
            }

            if (trip.getDateEntryParking() == null) {
                trip.setDateEntryParking(dateEvent);
            }

            tripService.updateTrip(trip);

            if (!trip.getIsDirectToPort()) {

                // update parking permission status to disabled for vehicle at parking area
                anprBaseService.scheduleAnpr(ServerConstants.REQUEST_TYPE_ANPR_API_UPDATE_PARKINGPERMISSIONS_PARKING_ENTRY_STATUS_DISABLED, ServerConstants.DEFAULT_LONG, trip,
                    null, null, new Date());
            }

            if (operatorId != ServerConstants.DEFAULT_LONG) {
                activityLogService.saveActivityLogParking(ServerConstants.ACTIVITY_LOG_PARKING_ENTRY, operatorId, parking.getId());
            }

        } catch (PADException | PADValidationException pade) {
            throw pade;

        } catch (Exception e) {
            logger.error("processVehicleEntry###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "processVehicleEntry#Exception#" + e.getMessage());
        }

        return trip.getCode();
    }

    @Override
    public int getVehicleReleasedCountInLastXMinutes(Long portOperatorId, Integer transactionType, Date dateTodayMinusXMinutes) throws SQLException {

        PreparedJDBCQuery query = new PreparedJDBCQuery();
        query.append("SELECT COUNT(parking.id) ");
        query.append(" FROM pad.parking parking ");
        query.append(" INNER JOIN pad.trips trip on parking.trip_id = trip.id");
        query.append(" INNER JOIN pad.missions mission on parking.mission_id = mission.id");
        query.append(" WHERE parking.status IN (?,?) AND parking.type = ? AND parking.date_sms_exit > ? AND trip.status IN (?,?) ");

        query.addQueryParameter(ServerConstants.PARKING_STATUS_REMINDER_EXIT_DUE);
        query.addQueryParameter(ServerConstants.PARKING_STATUS_EXIT);
        query.addQueryParameter(ServerConstants.PARKING_TYPE_PARKING);
        query.addQueryParameter(dateTodayMinusXMinutes);
        query.addQueryParameter(ServerConstants.TRIP_STATUS_ENTERED_PARKING);
        query.addQueryParameter(ServerConstants.TRIP_STATUS_IN_TRANSIT);

        if (portOperatorId != null && portOperatorId != ServerConstants.DEFAULT_LONG) {
            query.append(" AND parking.port_operator_id = ? ");
            query.addQueryParameter(portOperatorId);
        }

        if (transactionType != null && transactionType != ServerConstants.DEFAULT_INT) {
            query.append(" AND mission.transaction_type = ? ");
            query.addQueryParameter(transactionType);
        }

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Integer.class);
    }
}
