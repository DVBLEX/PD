package com.pad.server.base.services.statistics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.PreparedJDBCQuery;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.ParkingStatistics;
import com.pad.server.base.entities.PortOperator;
import com.pad.server.base.entities.PortStatistics;
import com.pad.server.base.jsonentities.api.StatsJson;
import com.pad.server.base.services.system.SystemService;

@Service
@Transactional
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Autowired
    private JdbcTemplate      jdbcTemplate;

    @Autowired
    private SystemService     systemService;

    @Override
    public void saveParkingStatistics(ParkingStatistics parkingStatistics) {

        hibernateTemplate.save(parkingStatistics);
    }

    @Override
    public void savePortStatistics(PortStatistics portStatistics) {

        hibernateTemplate.save(portStatistics);
    }

    @Override
    public void updateParkingStatistics(ParkingStatistics parkingStatistics) {

        hibernateTemplate.update(parkingStatistics);
    }

    @Override
    public void updatePortStatistics(PortStatistics portStatistics) {

        hibernateTemplate.update(portStatistics);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ParkingStatistics getParkingStatsByPortOperatorAndTransactionType(Date dateYesterday, long portOperatorId, int transactionType) {

        ParkingStatistics parkingStatistics = null;

        String[] paramNames = { "dateYesterday", "portOperatorId", "transactionType" };
        Object[] paramValues = { dateYesterday, portOperatorId, transactionType };

        List<ParkingStatistics> parkingStatsList = (List<ParkingStatistics>) hibernateTemplate.findByNamedParam(
            "FROM ParkingStatistics WHERE date = :dateYesterday AND portOperatorId = :portOperatorId AND transactionType =:transactionType", paramNames, paramValues);

        if (parkingStatsList != null && !parkingStatsList.isEmpty()) {
            parkingStatistics = parkingStatsList.get(0);
        }

        return parkingStatistics;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PortStatistics getPortStatsByPortOperatorAndTransactionType(Date dateYesterday, long portOperatorId, int transactionType) {

        PortStatistics portStatistics = null;

        String[] paramNames = { "dateYesterday", "portOperatorId", "transactionType" };
        Object[] paramValues = { dateYesterday, portOperatorId, transactionType };

        List<PortStatistics> portStatsList = (List<PortStatistics>) hibernateTemplate.findByNamedParam(
            "FROM PortStatistics WHERE date = :dateYesterday AND portOperatorId = :portOperatorId AND transactionType =:transactionType", paramNames, paramValues);

        if (portStatsList != null && !portStatsList.isEmpty()) {
            portStatistics = portStatsList.get(0);
        }

        return portStatistics;
    }

    @Override
    public List<StatsJson> getParkingStatsByDateRange(Date dateFrom, Date dateTo) {

        List<StatsJson> statsJsonList = new ArrayList<>();

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        query.append("SELECT");
        query.append(" ps.date,");

        for (int i = 0; i < systemService.getPortOperators().size(); i++) {

            if (i > 0) {
                query.append(",");
            }
            query.append(" SUM(CASE WHEN ps.port_operator_id = '" + systemService.getPortOperators().get(i).getId() + "' THEN ps.count_entry ELSE 0 END) AS '"
                + systemService.getPortOperators().get(i).getNameShort() + "_CNT" + "'");
        }

        query.append(" FROM parking_statistics ps");
        query.append(" WHERE ps.date >= ? AND ps.date < ?");
        query.append(" GROUP BY ps.date");

        query.addQueryParameter(dateFrom);
        query.addQueryParameter(dateTo);

        jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {

                Map<String, Integer> operatorCountsMap = new ConcurrentHashMap<>();

                int dailyGrandTotal = 0;

                StatsJson statsJson = new StatsJson();

                try {
                    statsJson.setDateEntryString(ServerUtil.formatDate(ServerConstants.dateFormatddMMyyyy, rs.getTimestamp("ps.date")));
                } catch (ParseException e) {
                    statsJson.setDateEntryString("");
                }

                for (int i = 0; i < systemService.getPortOperators().size(); i++) {

                    operatorCountsMap.put(systemService.getPortOperators().get(i).getNameShort() + "_CNT",
                        rs.getInt(systemService.getPortOperators().get(i).getNameShort() + "_CNT"));

                    dailyGrandTotal += rs.getInt(systemService.getPortOperators().get(i).getNameShort() + "_CNT");
                }

                operatorCountsMap.put("TOTAL_CNT", dailyGrandTotal);

                statsJson.setOperatorCountsMap(operatorCountsMap);

                statsJsonList.add(statsJson);
            }
        }, query.getQueryParameters());

        return statsJsonList;
    }

    @Override
    public List<StatsJson> getPortStatsByDateRange(Date dateFrom, Date dateTo) {

        List<StatsJson> statsJsonList = new ArrayList<>();

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        query.append("SELECT");
        query.append(" ps.date,");

        for (int i = 0; i < systemService.getPortOperators().size(); i++) {

            if (i > 0) {
                query.append(",");
            }
            query.append(" SUM(CASE WHEN ps.port_operator_id = '" + systemService.getPortOperators().get(i).getId() + "' THEN ps.count_entry ELSE 0 END) AS '"
                + systemService.getPortOperators().get(i).getNameShort() + "_CNT" + "'");
        }

        query.append(" FROM port_statistics ps");
        query.append(" WHERE ps.date >= ? AND ps.date < ?");
        query.append(" GROUP BY ps.date");

        query.addQueryParameter(dateFrom);
        query.addQueryParameter(dateTo);

        jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {

                Map<String, Integer> operatorCountsMap = new ConcurrentHashMap<>();
                new ConcurrentHashMap<>();

                int dailyGrandTotal = 0;

                StatsJson statsJson = new StatsJson();
                try {
                    statsJson.setDateEntryString(ServerUtil.formatDate(ServerConstants.dateFormatddMMyyyy, rs.getTimestamp("ps.date")));
                } catch (ParseException e) {
                    statsJson.setDateEntryString("");
                }

                for (int i = 0; i < systemService.getPortOperators().size(); i++) {

                    operatorCountsMap.put(systemService.getPortOperators().get(i).getNameShort() + "_CNT",
                        rs.getInt(systemService.getPortOperators().get(i).getNameShort() + "_CNT"));

                    dailyGrandTotal += rs.getInt(systemService.getPortOperators().get(i).getNameShort() + "_CNT");
                }

                operatorCountsMap.put("TOTAL_CNT", dailyGrandTotal);
                operatorCountsMap.put("VIRTUAL_CNT", getVirtualCountByDate(rs.getDate("ps.date")));

                statsJson.setOperatorCountsMap(operatorCountsMap);

                statsJsonList.add(statsJson);
            }
        }, query.getQueryParameters());

        return statsJsonList;
    }

    private int getVirtualCountByDate(Date dateFrom) {

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        query.append("SELECT COUNT(1) FROM pad.port_access pa ");
        query.append("INNER JOIN pad.trips t ON t.id = pa.trip_id ");
        query.append("WHERE DATE(pa.date_entry) >= ? AND DATE(pa.date_entry) < ? AND t.is_direct_to_port = 1 ");

        query.addQueryParameter(dateFrom);

        Calendar c = Calendar.getInstance();
        c.setTime(dateFrom);
        c.add(Calendar.DATE, 1);
        query.addQueryParameter(c.getTime());

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Integer.class);
    }

    @Override
    public Map<String, List<Integer>> getStatisticsMapForLineChart(int reportType, List<StatsJson> statsJsonList, Date dateFrom) {

        var calendar = Calendar.getInstance();
        calendar.setTime(dateFrom);

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        Map<PortOperator, List<Integer>> portOperatorListMap = new LinkedHashMap<>();

        if (reportType == ServerConstants.REPORT_TYPE_PARKING_ENTRY_COUNTS || reportType == ServerConstants.REPORT_TYPE_PORT_ENTRY_OPERATOR_COUNTS) {
            for (PortOperator portOperator : systemService.getPortOperators()) {
                List<Integer> countsList = new ArrayList<>();
                for (var d = 1; d <= daysInMonth; d++) {
                    countsList.add(0);
                }
                portOperatorListMap.put(portOperator, countsList);
            }
        }

        for (StatsJson statsJson : statsJsonList) {
            var day = Integer.parseInt(statsJson.getDateEntryString().substring(0, 2));
            portOperatorListMap.forEach((key, value) -> value.set(day - 1, statsJson.getOperatorCountsMap().get(key.getNameShort() + "_CNT")));
        }

        return portOperatorListMap.entrySet().stream()
            .collect(Collectors.toMap(e -> e.getKey().getName().trim(), Map.Entry::getValue,(e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    public StatsJson getStatsTotalsByCurrentMonthAndYear(StatsJson statsJson) {

        final Calendar calendar = Calendar.getInstance();
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        query.append("SELECT q1.count AS parkingMonthTotal, q2.count AS parkingYearTotal, q3.count AS parkingDayTotal, q4.count AS inTheParking, q5.count AS inTransit, ");
        query.append("       q6.count AS portMonthTotal, q7.count AS portYearTotal, q8.count AS portDayTotal, q9.count AS enteredPortLastHour ");
        query.append(" FROM");

        // Parking
        query.append(
            " (SELECT (CASE WHEN ps.count_entry IS NOT NULL THEN SUM(ps.count_entry) ELSE 0 END) AS count FROM parking_statistics ps WHERE MONTH(ps.date) = ? AND YEAR(ps.date) = ?) AS q1,");
        query.append(" (SELECT (CASE WHEN ps.count_entry IS NOT NULL THEN SUM(ps.count_entry) ELSE 0 END) AS count FROM parking_statistics ps WHERE YEAR(ps.date) = ?) AS q2,");
        query.append(" (SELECT COUNT(1) AS count FROM parking p WHERE p.type = ? AND DATE(p.date_entry) = CURDATE()) AS q3,");
        query.append(" (SELECT COUNT(1) AS count FROM parking p WHERE p.type = ? AND (p.status = ? OR p.status = ?)) AS q4,");
        query.append(" (SELECT COUNT(1) AS count FROM parking p WHERE p.status = ? AND p.is_eligible_port_entry = 1) AS q5,");

        query.addQueryParameter(month + 1);
        query.addQueryParameter(year);
        query.addQueryParameter(year);
        query.addQueryParameter(ServerConstants.PARKING_TYPE_PARKING);
        query.addQueryParameter(ServerConstants.PARKING_TYPE_PARKING);
        query.addQueryParameter(ServerConstants.PARKING_STATUS_ENTRY);
        query.addQueryParameter(ServerConstants.PARKING_STATUS_REMINDER_EXIT_DUE);
        query.addQueryParameter(ServerConstants.PARKING_STATUS_EXIT);

        // Port
        query.append(
            " (SELECT (CASE WHEN pts.count_entry IS NOT NULL THEN SUM(pts.count_entry) ELSE 0 END) AS count FROM port_statistics pts WHERE MONTH(pts.date) = ? AND YEAR(pts.date) = ?) AS q6,");
        query.append(" (SELECT (CASE WHEN pts.count_entry IS NOT NULL THEN SUM(pts.count_entry) ELSE 0 END) AS count FROM port_statistics pts WHERE YEAR(pts.date) = ?) AS q7,");
        query.append(" (SELECT COUNT(1) AS count FROM port_access pa WHERE DATE(pa.date_entry) = CURDATE()) AS q8,");
        query.append(" (SELECT COUNT(1) AS count FROM port_access pa WHERE pa.date_entry >= ?) AS q9");

        query.addQueryParameter(month + 1);
        query.addQueryParameter(year);
        query.addQueryParameter(year);
        calendar.add(Calendar.HOUR, -1);
        query.addQueryParameter(calendar.getTime());

        jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {

                // Parking
                statsJson.setMonthTotalCountParkingEntry(rs.getInt("parkingMonthTotal") + rs.getInt("parkingDayTotal"));
                statsJson.setYearTotalCountParkingEntry(rs.getInt("parkingYearTotal") + rs.getInt("parkingDayTotal"));
                statsJson.setDayTotalCountParkingEntry(rs.getInt("parkingDayTotal"));
                statsJson.setCountInTheParking(rs.getInt("inTheParking"));
                statsJson.setCountInTransit(rs.getInt("inTransit"));

                // Port
                statsJson.setMonthTotalCountPortEntry(rs.getInt("portMonthTotal") + rs.getInt("portDayTotal"));
                statsJson.setYearTotalCountPortEntry(rs.getInt("portYearTotal") + rs.getInt("portDayTotal"));
                statsJson.setDayTotalCountPortEntry(rs.getInt("portDayTotal"));
                statsJson.setCountEnteredPortLastHour(rs.getInt("enteredPortLastHour"));

            }
        }, query.getQueryParameters());

        return statsJson;
    }
}
