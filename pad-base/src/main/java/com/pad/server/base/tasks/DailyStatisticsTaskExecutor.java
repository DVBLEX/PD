package com.pad.server.base.tasks;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.PreparedJDBCQuery;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.AnprEntryLog;
import com.pad.server.base.entities.ParkingStatistics;
import com.pad.server.base.entities.PortOperator;
import com.pad.server.base.entities.PortOperatorTransactionType;
import com.pad.server.base.entities.PortStatistics;
import com.pad.server.base.services.anpr.AnprBaseService;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.invoice.InvoiceService;
import com.pad.server.base.services.statistics.StatisticsService;
import com.pad.server.base.services.system.SystemService;

@Component
@Transactional
public class DailyStatisticsTaskExecutor implements Runnable {

    private static final Logger logger = Logger.getLogger(DailyStatisticsTaskExecutor.class);

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private AnprBaseService     anprBaseService;

    @Autowired
    private EmailService        emailService;

    @Autowired
    private InvoiceService      invoiceService;

    @Autowired
    private StatisticsService   statisticsService;

    @Autowired
    private SystemService       systemService;

    @Override
    @Scheduled(cron = "0 0 3 * * *")
    public void run() {

        try {
            systemService.updateSystemTimerTaskDateLastRun(ServerConstants.SYSTEM_TIMER_TASK_DAILY_STATISTICS_ID, new Date());

            LocalDate todayLocalDate = LocalDate.now();

            final Date dateToday = Date.from(todayLocalDate.atTime(0, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
            final Date dateYesterday = Date.from(todayLocalDate.minusDays(1).atTime(0, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant());

            logger.info("run# [dateYesterday=" + dateYesterday + ", dateToday=" + dateToday + "]");

            runParkingAndPortStatistics(dateToday, dateYesterday);

            runTransporterTripsStatistics(dateToday, dateYesterday);

            runAnprEntryLog(dateToday, dateYesterday);

            if (todayLocalDate.getDayOfMonth() == ServerConstants.INVOICE_GENERATION_DAY_MONTH) {

                final LocalDateTime firstDayLastMonth = todayLocalDate.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).atTime(0, 0, 0, 0);
                final LocalDateTime firstDayThisMonth = todayLocalDate.atTime(0, 0, 0, 0);

                logger.info("run##Invoices/Statements #[firstDayLastMonth=" + firstDayLastMonth + ", firstDayThisMonth= " + firstDayThisMonth + "]");

                runInvoicesStatements(firstDayLastMonth, firstDayThisMonth);

            }

        } catch (DataAccessException dae) {
            logger.error("run###DataAccessException: ", dae);

            emailService.sendSystemEmail("DailyStatisticsTask DataAccessException", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "DailyStatisticsTask#run###DataAccessException:<br />" + dae.getMessage());

        } catch (Exception e) {
            logger.error("run###Exception: ", e);

            emailService.sendSystemEmail("DailyStatisticsTask Exception", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "DailyStatisticsTask#run###Exception:<br />" + e.getMessage());
        }
    }

    private void runInvoicesStatements(LocalDateTime dateFrom, LocalDateTime dateTo) {

        try {

            invoiceService.generateInvoicesStatements(dateFrom, dateTo);

        } catch (DataAccessException dae) {
            logger.error("run#Invoices###DataAccessException: ", dae);

            emailService.sendSystemEmail("DailyStatisticsTask DataAccessException - Invoice", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "DailyStatisticsTask#runInvoices###DataAccessException:<br />" + dae.getMessage());

        } catch (Exception e) {
            logger.error("run#Invoices###Exception: ", e);

            emailService.sendSystemEmail("DailyStatisticsTask Exception - Invoice", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "DailyStatisticsTask#runInvoices###Exception:<br />" + e.getMessage());
        }

    }

    private void runTransporterTripsStatistics(final Date dateToday, final Date dateYesterday) {

        logger.info("run##TransporterTripsStatistics#");

        try {

            PreparedJDBCQuery query = new PreparedJDBCQuery();

            query.append(" INSERT INTO pad.transporter_trips_statistics");
            query.append(" ( ");
            query.append("  date_fee_paid, account_id, port_operator_id, ");
            query.append("  transaction_type, vehicle_registration_country_iso, count_trips, ");
            query.append("  transporter_total_amount_fee, operator_total_amount_fee, date_created ");
            query.append(" )");
            query.append(" SELECT ");
            query.append("  DATE(t.date_fee_paid), t.account_id, t.port_operator_id, ");
            query.append("  t.transaction_type, t.vehicle_registration_country_iso, COUNT(1), ");
            query.append("  sum(t.amount_fee), sum(t.operator_amount_fee), ? ");
            query.append(" FROM pad.trips t ");
            query.append(" WHERE t.date_fee_paid IS NOT NULL ");
            query.append("  AND t.date_fee_paid < ? ");
            query.append("  AND t.date_fee_paid >= ? ");
            query.append(" GROUP BY DATE(t.date_fee_paid), t.account_id, t.port_operator_id, t.transaction_type, t.vehicle_registration_country_iso ");

            query.addQueryParameter(new Date());
            query.addQueryParameter(dateToday);
            query.addQueryParameter(dateYesterday);

            int count = jdbcTemplate.update(query.getQueryString(), query.getQueryParameters());

            logger.info("run##TransporterTripsStatistics#Count= " + count);

        } catch (DataAccessException dae) {
            logger.error("run#TransporterTripsStatistics###DataAccessException: ", dae);

            emailService.sendSystemEmail("DailyStatisticsTask DataAccessException -  Transporter Trips Statistics", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "DailyStatisticsTask#runTransporterTripsStatistics###DataAccessException:<br />" + dae.getMessage());

        } catch (Exception e) {
            logger.error("run#TransporterTripsStatistics###Exception: ", e);

            emailService.sendSystemEmail("DailyStatisticsTask Exception - Transporter Trips Statistics", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "DailyStatisticsTask#runTransporterTripsStatistics###Exception:<br />" + e.getMessage());
        }

    }

    private void runParkingAndPortStatistics(final Date dateToday, final Date dateYesterday) {

        logger.info("run##ParkingAndPortStatistics#");

        try {

            for (PortOperator portOperator : systemService.getPortOperators()) {

                for (PortOperatorTransactionType transactionType : portOperator.getPortOperatorTransactionTypesList()) {

                    ParkingStatistics parkingStatistics = new ParkingStatistics();
                    parkingStatistics.setDate(dateYesterday);
                    parkingStatistics.setPortOperatorId(portOperator.getId());
                    parkingStatistics.setPortOperatorName(portOperator.getName());
                    parkingStatistics.setTransactionType(transactionType.getTransactionType());
                    parkingStatistics.setCountEntry(0);
                    parkingStatistics.setTotalTripFeeAmount(BigDecimal.ZERO);

                    statisticsService.saveParkingStatistics(parkingStatistics);

                    PortStatistics portStatistics = new PortStatistics();
                    portStatistics.setDate(dateYesterday);
                    portStatistics.setPortOperatorId(portOperator.getId());
                    portStatistics.setPortOperatorName(portOperator.getName());
                    portStatistics.setTransactionType(transactionType.getTransactionType());
                    portStatistics.setCountEntry(0);
                    portStatistics.setTotalTripFeeAmount(BigDecimal.ZERO);

                    statisticsService.savePortStatistics(portStatistics);
                }
            }

            PreparedJDBCQuery parkingEntryStatsQuery = new PreparedJDBCQuery();

            parkingEntryStatsQuery.append("SELECT m.port_operator_id, m.transaction_type, COUNT(1) AS countEntry, SUM(t.amount_fee) AS tripFeeTotalAmount");
            parkingEntryStatsQuery.append(" FROM parking p");
            parkingEntryStatsQuery.append(" LEFT JOIN missions m ON p.mission_id = m.id");
            parkingEntryStatsQuery.append(" LEFT JOIN trips t ON p.trip_id = t.id");
            parkingEntryStatsQuery.append(" WHERE p.type = ? AND p.date_entry >= ? AND p.date_entry < ?");
            parkingEntryStatsQuery.append(" GROUP BY m.port_operator_id, m.transaction_type");

            parkingEntryStatsQuery.addQueryParameter(ServerConstants.PARKING_TYPE_PARKING);
            parkingEntryStatsQuery.addQueryParameter(dateYesterday);
            parkingEntryStatsQuery.addQueryParameter(dateToday);

            jdbcTemplate.query(parkingEntryStatsQuery.getQueryString(), new RowCallbackHandler() {

                @Override
                public void processRow(ResultSet rs) throws SQLException {

                    ParkingStatistics parkingStatistics = statisticsService.getParkingStatsByPortOperatorAndTransactionType(dateYesterday, rs.getLong("m.port_operator_id"),
                        rs.getInt("m.transaction_type"));

                    if (parkingStatistics != null) {

                        parkingStatistics.setCountEntry(rs.getInt("countEntry"));
                        parkingStatistics.setTotalTripFeeAmount(rs.getBigDecimal("tripFeeTotalAmount"));

                        statisticsService.updateParkingStatistics(parkingStatistics);
                    }
                }
            }, parkingEntryStatsQuery.getQueryParameters());

            PreparedJDBCQuery portEntryStatsQuery = new PreparedJDBCQuery();

            portEntryStatsQuery.append("SELECT m.port_operator_id, m.transaction_type, COUNT(1) AS countEntry, SUM(t.amount_fee) AS tripFeeTotalAmount");
            portEntryStatsQuery.append(" FROM port_access pa");
            portEntryStatsQuery.append(" LEFT JOIN missions m ON pa.mission_id = m.id");
            portEntryStatsQuery.append(" LEFT JOIN trips t ON pa.trip_id = t.id");
            portEntryStatsQuery.append(" WHERE pa.date_entry >= ? AND pa.date_entry < ?");
            portEntryStatsQuery.append(" GROUP BY m.port_operator_id, m.transaction_type");

            portEntryStatsQuery.addQueryParameter(dateYesterday);
            portEntryStatsQuery.addQueryParameter(dateToday);

            jdbcTemplate.query(portEntryStatsQuery.getQueryString(), new RowCallbackHandler() {

                @Override
                public void processRow(ResultSet rs) throws SQLException {

                    PortStatistics portStatistics = statisticsService.getPortStatsByPortOperatorAndTransactionType(dateYesterday, rs.getLong("m.port_operator_id"),
                        rs.getInt("m.transaction_type"));

                    if (portStatistics != null) {

                        portStatistics.setCountEntry(rs.getInt("countEntry"));
                        portStatistics.setTotalTripFeeAmount(rs.getBigDecimal("tripFeeTotalAmount"));

                        statisticsService.updatePortStatistics(portStatistics);
                    }
                }
            }, portEntryStatsQuery.getQueryParameters());

        } catch (DataAccessException dae) {
            logger.error("run#ParkingAndPortStatistics###DataAccessException: ", dae);

            emailService.sendSystemEmail("DailyStatisticsTask DataAccessException -  Parking And Port Statistics", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "DailyStatisticsTask#runParkingAndPortStatistics###DataAccessException:<br />" + dae.getMessage());

        } catch (Exception e) {
            logger.error("run#ParkingAndPortStatistics###Exception: ", e);

            emailService.sendSystemEmail("DailyStatisticsTask Exception - Parking And Port Statistics", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "DailyStatisticsTask#runParkingAndPortStatistics###Exception:<br />" + e.getMessage());
        }
    }

    private void runAnprEntryLog(final Date dateToday, final Date dateYesterday) {
        logger.info("run##AnprEntryLog#");

        try {

            List<AnprEntryLog> anprEntryLogList = anprBaseService.getUnsuccessfulEventsFromAnprEntryLog(dateYesterday, dateToday);

            if (anprEntryLogList != null && anprEntryLogList.size() > 0) {

                final StringBuilder content = new StringBuilder(
                    "<table border=\"1px\"><tr><td><b>Log ID</b></td><td><b>Event Type</b></td><td><b>Vehicle Reg</b></td><td><b>Event Datetime</b></td><td><b>PAD response</b></td></tr>");

                for (AnprEntryLog anprEntryLog : anprEntryLogList) {

                    content.append("<tr>");

                    long eventEpochTimeMillis = Long.parseLong(anprEntryLog.getTimestamp().substring(6, 19));
                    final LocalDateTime eventLocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(eventEpochTimeMillis), ZoneId.systemDefault());
                    final Date dateEvent = Date.from(eventLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

                    content.append("<td>" + anprEntryLog.getId() + "</td>");
                    content.append("<td>" + anprEntryLog.getEntryEventTypeId() + "</td>");
                    content.append("<td>" + anprEntryLog.getPlateNumber() + "</td>");

                    try {
                        content.append("<td>" + ServerUtil.formatDate(ServerConstants.dateFormatyyyyMMddHHmmss, dateEvent) + "</td>");
                    } catch (ParseException e) {
                        content.append("<td></td>");
                    }

                    content.append("<td>" + anprEntryLog.getResponseText() + "</td>");
                    content.append("</tr>");
                }

                content.append("</table>");

                emailService.sendSystemEmail("DailyStatisticsTask ANPR Entry Log - PAD Exceptions", EmailService.EMAIL_TYPE_ALERT, null, null, content.toString());
            }
        } catch (DataAccessException dae) {
            logger.error("run#AnprEntryLog###DataAccessException: ", dae);

            emailService.sendSystemEmail("DailyStatisticsTask DataAccessException -  AnprEntryLog", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "DailyStatisticsTask#runAnprEntryLog###DataAccessException:<br />" + dae.getMessage());

        } catch (Exception e) {
            logger.error("run#AnprEntryLog###Exception: ", e);

            emailService.sendSystemEmail("DailyStatisticsTask Exception - AnprEntryLog", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "DailyStatisticsTask#runAnprEntryLog###Exception:<br />" + e.getMessage());
        }
    }

}
