package com.pad.server.anpr.tasks;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pad.server.anpr.common.ServerConstants;
import com.pad.server.anpr.services.anpr.AnprService;
import com.pad.server.base.entities.Anpr;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.system.SystemService;

@Component
public class AnprTimerTask {

    private static final Logger logger            = Logger.getLogger(AnprTimerTask.class);

    private long                dateLastRunMillis = (System.currentTimeMillis() - ServerConstants.NINE_MINUTES_MILLIS);

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private AnprService         anprService;

    @Autowired
    private EmailService        emailService;

    @Autowired
    private SystemService       systemService;

    @Autowired
    private TaskExecutor        anprTaskExecutor;

    @Scheduled(fixedDelay = 5000, initialDelay = 10000)
    public void run() {

        try {
            // logger.info("run#");

            if ((System.currentTimeMillis() - dateLastRunMillis) >= ServerConstants.NINE_MINUTES_MILLIS) {
                dateLastRunMillis = System.currentTimeMillis();
                systemService.updateSystemTimerTaskDateLastRun(ServerConstants.SYSTEM_TIMER_TASK_ANPR_ID, new Date(dateLastRunMillis));
            }

            jdbcTemplate.query("SELECT anprScheduler.* FROM anpr_scheduler anprScheduler WHERE anprScheduler.request_type != " + ServerConstants.REQUEST_TYPE_ANPR_API_GET_ENTRY_LOG
                + " AND anprScheduler.date_scheduled <= now() AND anprScheduler.is_processed = " + ServerConstants.PROCESS_NOTPROCESSED
                + " ORDER BY anprScheduler.priority, anprScheduler.id LIMIT 5", new RowCallbackHandler() {

                    @Override
                    public void processRow(ResultSet rs) throws SQLException {

                        jdbcTemplate.update("UPDATE anpr_scheduler SET is_processed = " + ServerConstants.PROCESS_PROGRESS + " WHERE id = ?", rs.getLong("id"));

                        try {
                            Anpr anpr = new Anpr();

                            anpr.setId(rs.getLong("id"));
                            anpr.setRequestType(rs.getInt("request_type"));
                            anpr.setZoneId(rs.getLong("zone_id"));
                            anpr.setMissionId(rs.getLong("mission_id"));
                            anpr.setTripId(rs.getLong("trip_id"));
                            anpr.setPortAccessWhitelistId(rs.getLong("port_access_whitelist_id"));
                            anpr.setVehicleRegistration(rs.getString("vehicle_registration"));
                            anpr.setParkingPermissionId(rs.getLong("parking_permission_id"));
                            anpr.setDateValidFrom(rs.getTimestamp("date_valid_from"));
                            anpr.setDateValidTo(rs.getTimestamp("date_valid_to"));
                            anpr.setDateScheduled(rs.getTimestamp("date_scheduled"));
                            anpr.setRetryCount(rs.getInt("retry_count"));
                            anpr.setIsProcessed(ServerConstants.PROCESS_PROGRESS);

                            anprTaskExecutor.execute(new AnprTaskExecutor(anpr, anprService, emailService));

                        } catch (TaskRejectedException tre) {
                            logger.error("run#AnprId=" + rs.getLong("id") + "###TaskRejectedException: ", tre);

                            jdbcTemplate.update("UPDATE anpr_scheduler SET is_processed = " + ServerConstants.PROCESS_NOTPROCESSED + " WHERE id = ?", rs.getLong("id"));
                        }
                    }
                });
        } catch (Exception e) {
            logger.error("run###Exception: ", e);
            emailService.sendSystemEmail("Anpr TimerTask Error", EmailService.EMAIL_TYPE_EXCEPTION, null, null, "AnprTimerTask#run###Exception:<br />" + e.getMessage());
        }
    }
}
