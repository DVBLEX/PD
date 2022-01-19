package com.pad.server.base.services.counter.vehicle;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.*;
import com.pad.server.base.entities.Lane;
import com.pad.server.base.entities.NameValuePair;
import com.pad.server.base.entities.VehicleCounter;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.counter.VehicleCounterEventJson;
import com.pad.server.base.jsonentities.api.counter.VehicleCounterJson;
import com.pad.server.base.jsonentities.api.counter.VehicleCounterRequestJson;
import com.pad.server.base.services.lane.LaneService;
import com.pad.server.base.services.session.SessionService;

@Service
@Transactional
public class VehicleCounterServiceImpl implements VehicleCounterService {

    private static final Logger logger = Logger.getLogger(VehicleCounterServiceImpl.class);

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private SessionFactory      sessionFactory;

    @Autowired
    private SessionService      sessionService;

    @Autowired
    private LaneService         laneService;

    @Override
    public long getCount(VehicleCounterJson vehicleCounterJson) {

        PreparedJDBCQuery query = getQuery(PreparedJDBCQuery.QUERY_TYPE_COUNT, vehicleCounterJson);

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Long.class);
    }

    private PreparedJDBCQuery getQuery(int queryType, VehicleCounterJson vehicleCounterJson) {

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        if (queryType == PreparedJDBCQuery.QUERY_TYPE_COUNT) {

            query.append(" SELECT COUNT(vc.id) ");
            query.append(" FROM pad.vehicle_counter vc ");

        } else if (queryType == PreparedJDBCQuery.QUERY_TYPE_SELECT) {

            query.append(" SELECT vc.*, o.first_name, o.last_name, o.username ");
            query.append(" FROM pad.vehicle_counter vc ");
            query.append(" LEFT JOIN pad.sessions s ON s.id = vc.session_id ");
            query.append(" LEFT JOIN pad.operators o ON o.id = s.kiosk_operator_id ");
        }

        query.append(" WHERE (1=1) ");

        if (vehicleCounterJson.getDateCountStart() != null) {
            query.append(" AND vc.date_count >= ?");
            query.addQueryParameter(vehicleCounterJson.getDateCountStart());
        }

        if (vehicleCounterJson.getDateCountEnd() != null) {
            query.append(" AND vc.date_count <= ?");
            query.addQueryParameter(vehicleCounterJson.getDateCountEnd());
        }

        if (StringUtils.isNotBlank(vehicleCounterJson.getDeviceId())) {
            query.append(" AND vc.device_id = ?");
            query.addQueryParameter(vehicleCounterJson.getDeviceId());
        }

        if (vehicleCounterJson.getLaneNumber() > ServerConstants.ZERO_INT) {
            query.append(" AND vc.lane_number = ?");
            query.addQueryParameter(vehicleCounterJson.getLaneNumber());
        }

        if (StringUtils.isNotBlank(vehicleCounterJson.getSessionIdString())) {
            query.append(" AND vc.session_id = ?");
            query.addQueryParameter(vehicleCounterJson.getSessionIdString());
        }

        if (StringUtils.isNotBlank(vehicleCounterJson.getType())) {
            if (vehicleCounterJson.getType().contains(VehicleCounterRequestJson.TYPE_AUTOMATIC) && vehicleCounterJson.getType().contains(VehicleCounterRequestJson.TYPE_MANUAL)) {
                query.append(" AND vc.type IN (?,?)");
                query.addQueryParameter(VehicleCounterRequestJson.TYPE_AUTOMATIC);
                query.addQueryParameter(VehicleCounterRequestJson.TYPE_MANUAL);
            } else {
                query.append(" AND vc.type = ?");
                query.addQueryParameter(vehicleCounterJson.getType());
            }
        }

        if (vehicleCounterJson.getIsShowDefaultDates()) {
            query.append(" AND DATE(vc.date_count) = ? ");
        } else {
            query.append(" AND DATE(vc.date_count) != ? ");
        }

        try {
            query.addQueryParameter(ServerUtil.parseDate(ServerConstants.dateFormatddMMyyyy, ServerConstants.VEHICLE_COUNTER_DEFAULT_DATE));
        } catch (ParseException e) {
            logger.error(e);
        }

        return query;
    }

    @Override
    public List<VehicleCounterJson> getList(VehicleCounterJson vehicleCounterJson) {

        final List<VehicleCounterJson> vehicleCounterList = new ArrayList<>();
        try {

            PreparedJDBCQuery query = getQuery(PreparedJDBCQuery.QUERY_TYPE_SELECT, vehicleCounterJson);

            if (vehicleCounterJson.getPageCount() != ServerConstants.DEFAULT_INT) {
                query.setSortParameters(vehicleCounterJson.getSortColumn(), vehicleCounterJson.getSortAsc(), "vc", ServerConstants.DEFAULT_SORTING_FIELD, "DESC");
                query.setLimitParameters(vehicleCounterJson.getCurrentPage(), vehicleCounterJson.getPageCount());
            }

            jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    VehicleCounterJson json = new VehicleCounterJson();
                    json.setDeviceId(rs.getString("vc.device_id"));
                    json.setDeviceName(rs.getString("vc.device_name"));
                    json.setLaneNumber(rs.getInt("vc.lane_number"));
                    json.setSessionId(rs.getLong("vc.session_id"));
                    json.setType(rs.getString("vc.type"));
                    json.setDateCount(rs.getTimestamp("vc.date_count"));
                    json.setDateCreated(rs.getTimestamp("vc.date_created"));

                    if (rs.getString("o.first_name") != null) {
                        json.setOperatorName(rs.getString("o.first_name") + " " + rs.getString("o.last_name"));
                    }

                    if (rs.getString("o.username") != null) {
                        json.setOperatorUsername(rs.getString("o.username"));
                    }

                    vehicleCounterList.add(json);
                }
            }, query.getQueryParameters());

        } catch (Exception e) {
            logger.error("getList###Exception: ", e);
        }

        return vehicleCounterList;
    }

    @Override
    public void log(VehicleCounterRequestJson vehicleCounterRequestJson) throws PADException {

        try {

            Lane lane = laneService.getLaneByDeviceId(vehicleCounterRequestJson.getDeviceId());

            if (lane == null)
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "DeviceId not found.");

            SecurityUtil.validateApiRemoteAddr(lane.getAllowedHosts());

            Session session = null;
            Transaction tx = null;
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            for (VehicleCounterEventJson eventJson : vehicleCounterRequestJson.getEvents()) {

                if (StringUtils.isBlank(eventJson.getType()))
                    throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "Event type is misssin.");

                if (!eventJson.getType().equals(VehicleCounterRequestJson.TYPE_AUTOMATIC) && !eventJson.getType().equals(VehicleCounterRequestJson.TYPE_MANUAL)
                    && !eventJson.getType().equals(VehicleCounterRequestJson.TYPE_UNKNOWN) && !eventJson.getType().equals(VehicleCounterRequestJson.TYPE_HEARTBEAT))
                    throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "Event type is invalid.");

                if (StringUtils.isBlank(eventJson.getDateCount()))
                    throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "Event dateCount is missing.");

                Date dateCount = null;
                try {
                    dateCount = ServerUtil.parseDate(ServerConstants.dateFormatyyyyMMddHHmmss, eventJson.getDateCount());
                } catch (Exception e) {
                    throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "Event dateCount is invalid.");
                }

                long sessionId = ServerConstants.DEFAULT_INT;
                if (lane.getZoneId() == ServerConstants.ZONE_ID_PARKING) {
                    sessionId = sessionService.getSessionIdByLaneNumberAndDateStartEnd(lane.getLaneNumber(), dateCount);
                }

                VehicleCounter vehicleCounter = new VehicleCounter();
                vehicleCounter.setDeviceId(vehicleCounterRequestJson.getDeviceId());
                vehicleCounter.setDeviceName(lane.getDeviceName());
                vehicleCounter.setLaneId(lane.getLaneId());
                vehicleCounter.setLaneNumber(lane.getLaneNumber());
                vehicleCounter.setZoneId(lane.getZoneId());
                vehicleCounter.setSessionId(sessionId);
                vehicleCounter.setType(eventJson.getType());
                vehicleCounter.setDateCount(dateCount);
                vehicleCounter.setDateCreated(new Date());

                session.save(vehicleCounter);

                session.flush();
                session.clear();
            }

            tx.commit();

            lane.setDateLastRequest(new Date());
            laneService.updateLane(lane);

        } catch (PADException pade) {
            throw pade;
        } catch (Exception e) {
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "log");
        }

    }

    @Override
    public List<NameValuePair> getSessionOperatorsList() {

        List<NameValuePair> list = new ArrayList<>();

        StringBuffer query = new StringBuffer();
        query.append(" SELECT s.id, o.first_name, o.last_name, o.username FROM sessions s ");
        query.append(" INNER JOIN operators o ON o.id = s.kiosk_operator_id ");
        query.append(" WHERE s.id IN (SELECT distinct session_id FROM pad.vehicle_counter) ");
        query.append(" GROUP BY s.kiosk_operator_id ");

        jdbcTemplate.query(query.toString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                String name = rs.getString("s.id");
                String value = rs.getString("o.first_name") + " " + rs.getString("o.last_name") + " - " + rs.getString("o.username");

                list.add(new NameValuePair(name, value));
            }
        });

        return list;
    }

    @Override
    public void exportVehicleCounter(List<VehicleCounterJson> vehicleCounterJsonList, OutputStream os) throws IOException {

        SXSSFWorkbook wb = new SXSSFWorkbook(1000);

        Font font = wb.createFont();
        font.setBold(true);

        CellStyle cellStyleBold = wb.createCellStyle();
        cellStyleBold.setFont(font);

        CellStyle cellStyleAmount = wb.createCellStyle();
        cellStyleAmount.setDataFormat((short) 2);

        Sheet sheet = wb.createSheet("Vehicle Counter Data");
        sheet.createFreezePane(0, 1);

        int row = 0;
        int column = 0;

        Row headerRow = sheet.createRow(row++);

        headerRow.createCell(column).setCellValue("Date Count");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Device");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Device Name");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Lane Number");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Session");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Type");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        headerRow.createCell(column).setCellValue("Date Created");
        headerRow.getCell(column++).setCellStyle(cellStyleBold);

        Row dataRow = null;
        for (VehicleCounterJson vehicleCounterJson : vehicleCounterJsonList) {

            column = 0;

            dataRow = sheet.createRow(row++);

            dataRow.createCell(column++).setCellValue(vehicleCounterJson.getDateCountString());
            dataRow.createCell(column++).setCellValue(vehicleCounterJson.getDeviceId());
            dataRow.createCell(column++).setCellValue(vehicleCounterJson.getDeviceName());
            dataRow.createCell(column++).setCellValue(vehicleCounterJson.getLaneNumber());
            dataRow.createCell(column++).setCellValue(vehicleCounterJson.getSessionDisplay());
            dataRow.createCell(column++).setCellValue(vehicleCounterJson.getType().equalsIgnoreCase("M") ? "Manual" : "Automatic");
            dataRow.createCell(column++).setCellValue(vehicleCounterJson.getDateCreatedString());
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

}
