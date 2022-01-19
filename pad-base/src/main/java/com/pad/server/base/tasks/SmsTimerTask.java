package com.pad.server.base.tasks;

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

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.entities.Sms;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.sms.SmsService;
import com.pad.server.base.services.system.SystemService;

@Component
public class SmsTimerTask {

    private static final Logger logger            = Logger.getLogger(SmsTimerTask.class);

    private long                dateLastRunMillis = (System.currentTimeMillis() - ServerConstants.NINE_MINUTES_MILLIS);

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private EmailService        emailService;

    @Autowired
    private SmsService          smsService;

    @Autowired
    private SystemService       systemService;

    @Autowired
    private TaskExecutor        smsTaskExecutor;

    @Scheduled(fixedDelay = 1000, initialDelay = 10000)
    public void run() {

        try {
            // logger.info("run#");
            if ((System.currentTimeMillis() - dateLastRunMillis) >= ServerConstants.NINE_MINUTES_MILLIS) {
                dateLastRunMillis = System.currentTimeMillis();
                systemService.updateSystemTimerTaskDateLastRun(ServerConstants.SYSTEM_TIMER_TASK_SMS_ID, new Date(dateLastRunMillis));
            }

            jdbcTemplate.query(
                "SELECT ss.id, ss.type, ss.config_id, ss.language_id, ss.account_id, ss.trip_id, ss.template_id, ss.msisdn, ss.source_addr, ss.message, ss.date_scheduled, ss.retry_count FROM sms_scheduler ss WHERE ss.date_scheduled <= now() AND ss.is_processed = "
                    + ServerConstants.PROCESS_NOTPROCESSED + " ORDER BY ss.priority, ss.id LIMIT 3",
                new RowCallbackHandler() {

                    @Override
                    public void processRow(ResultSet rs) throws SQLException {

                        if (ServerConstants.SCHEDULER_ID == rs.getLong("id")) {
                            jdbcTemplate.update("UPDATE sms_scheduler SET date_scheduled = now(), retry_count = retry_count + 1 WHERE id = ?", rs.getLong("id"));
                            return;
                        }

                        jdbcTemplate.update("UPDATE sms_scheduler SET is_processed = " + ServerConstants.PROCESS_PROGRESS + " WHERE id = ?", rs.getLong("id"));

                        try {
                            Sms sms = new Sms();
                            sms.setId(rs.getLong("id"));
                            sms.setType(rs.getLong("type"));
                            sms.setConfigId(rs.getLong("config_id"));
                            sms.setLanguageId(rs.getLong("language_id"));
                            sms.setAccountId(rs.getLong("account_id"));
                            sms.setTripId(rs.getLong("trip_id"));
                            sms.setTemplateId(rs.getLong("template_id"));
                            sms.setMsisdn(rs.getString("msisdn"));
                            sms.setSourceAddr(rs.getString("source_addr"));
                            sms.setMessage(rs.getString("message"));
                            sms.setDateScheduled(rs.getTimestamp("date_scheduled"));
                            sms.setRetryCount(rs.getInt("retry_count"));
                            sms.setIsProcessed(ServerConstants.PROCESS_PROGRESS);

                            smsTaskExecutor.execute(new SmsTaskExecutor(sms, smsService));

                        } catch (TaskRejectedException tre) {
                            logger.error("run#smsId=" + rs.getLong("id") + "###TaskRejectedException: ", tre);

                            jdbcTemplate.update("UPDATE sms_scheduler SET is_processed = " + ServerConstants.PROCESS_NOTPROCESSED + " WHERE id = ?", rs.getLong("id"));
                        }
                    }
                });
        } catch (Exception e) {
            logger.error("run###Exception: ", e);
            emailService.sendSystemEmail("Sms TimerTask Error", EmailService.EMAIL_TYPE_EXCEPTION, null, null, "SmsTimerTask#run###Exception:<br />" + e.getMessage());
        }
    }
}
