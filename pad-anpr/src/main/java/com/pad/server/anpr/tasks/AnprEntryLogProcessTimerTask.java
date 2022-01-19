package com.pad.server.anpr.tasks;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pad.server.anpr.common.ServerConstants;
import com.pad.server.anpr.services.anpr.AnprService;
import com.pad.server.base.entities.AnprEntryScheduler;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.system.SystemService;

@Component
public class AnprEntryLogProcessTimerTask {

    private static final Logger logger            = Logger.getLogger(AnprEntryLogProcessTimerTask.class);

    private long                dateLastRunMillis = (System.currentTimeMillis() - ServerConstants.NINE_MINUTES_MILLIS);

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private AnprService         anprService;

    @Autowired
    private EmailService        emailService;

    @Autowired
    private SystemService       systemService;

    @Scheduled(fixedDelay = 20000, initialDelay = 15000)
    public void run() {

        try {
            // logger.info("run#");

            if ((System.currentTimeMillis() - dateLastRunMillis) >= ServerConstants.NINE_MINUTES_MILLIS) {
                dateLastRunMillis = System.currentTimeMillis();
                systemService.updateSystemTimerTaskDateLastRun(ServerConstants.SYSTEM_TIMER_TASK_ANPR_ENTRY_LOG_PROCESS_ID, new Date(dateLastRunMillis));
            }

            List<AnprEntryScheduler> anprEntrySchedulerEntityList = new ArrayList<>();

            jdbcTemplate.query(
                "SELECT id, retry_count, trip_id, mission_id, date_scheduled, zone_id_from, zone_id_to, lane_id, plate_number, recognized_plate_number, parking_permission_id, timestamp, entry_event_type_id"
                    + " FROM anpr_entry_scheduler WHERE date_scheduled <= now() AND is_processed = " + ServerConstants.PROCESS_NOTPROCESSED + " AND entry_event_type_id IN ("
                    + ServerConstants.ENTRY_EVENT_TYPE_ID_APPROVED + "," + ServerConstants.ENTRY_EVENT_TYPE_ID_NO_PERMISSION + ","
                    + ServerConstants.ENTRY_EVENT_TYPE_ID_NO_PERMISSION_FOR_ZONE + ") ORDER BY id ASC LIMIT 100",
                new RowCallbackHandler() {

                    @Override
                    public void processRow(ResultSet rs) throws SQLException {

                        jdbcTemplate.update("UPDATE anpr_entry_scheduler SET is_processed = " + ServerConstants.PROCESS_PROGRESS + " WHERE id = ?", rs.getLong("id"));

                        try {
                            AnprEntryScheduler anprEntryScheduler = new AnprEntryScheduler();

                            anprEntryScheduler.setId(rs.getLong("id"));
                            anprEntryScheduler.setRetryCount(rs.getInt("retry_count"));
                            anprEntryScheduler.setIsProcessed(ServerConstants.PROCESS_PROGRESS);
                            anprEntryScheduler.setTripId(rs.getLong("trip_id"));
                            anprEntryScheduler.setMissionId(rs.getLong("mission_id"));
                            anprEntryScheduler.setDateScheduled(rs.getTimestamp("date_scheduled"));
                            anprEntryScheduler.setZoneIdFrom(rs.getLong("zone_id_from"));
                            anprEntryScheduler.setZoneIdTo(rs.getLong("zone_id_to"));
                            anprEntryScheduler.setLaneId(rs.getLong("lane_id"));
                            anprEntryScheduler.setPlateNumber(rs.getString("plate_number"));
                            anprEntryScheduler.setRecognizedPlateNumber(rs.getString("recognized_plate_number"));
                            anprEntryScheduler.setParkingPermissionId(rs.getLong("parking_permission_id"));
                            anprEntryScheduler.setTimestamp(rs.getString("timestamp"));
                            anprEntryScheduler.setEntryEventTypeId(rs.getLong("entry_event_type_id"));

                            anprEntrySchedulerEntityList.add(anprEntryScheduler);

                            anprService.processAnprEventScheduler(anprEntryScheduler);

                        } catch (Exception e) {
                            logger.error("run#AnprId=" + rs.getLong("id") + "###Exception: ", e);

                            jdbcTemplate.update("UPDATE anpr_entry_scheduler SET is_processed = " + ServerConstants.PROCESS_NOTPROCESSED + " WHERE id = ?", rs.getLong("id"));
                        }
                    }
                });

            logger.info("run#anprEntrySchedulerEntityList size=" + anprEntrySchedulerEntityList.size());

        } catch (Exception e) {
            logger.error("run###Exception: ", e);
            emailService.sendSystemEmail("AnprEntryLogProcess TimerTask Error", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "AnprEntryLogProcessTimerTask#run###Exception:<br />" + e.getMessage());
        }
    }
}
