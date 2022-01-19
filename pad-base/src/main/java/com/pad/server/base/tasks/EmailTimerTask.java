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
import com.pad.server.base.entities.Email;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.system.SystemService;

@Component
public class EmailTimerTask {

    private static final Logger logger            = Logger.getLogger(EmailTimerTask.class);

    private long                dateLastRunMillis = (System.currentTimeMillis() - ServerConstants.NINE_MINUTES_MILLIS);

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private AccountService      accountService;

    @Autowired
    private EmailService        emailService;

    @Autowired
    private SystemService       systemService;

    @Autowired
    private TaskExecutor        emailTaskExecutor;

    @Scheduled(fixedDelay = 1000, initialDelay = 10000)
    public void run() {

        try {
            // logger.info("run#");
            if ((System.currentTimeMillis() - dateLastRunMillis) >= ServerConstants.NINE_MINUTES_MILLIS) {
                dateLastRunMillis = System.currentTimeMillis();
                systemService.updateSystemTimerTaskDateLastRun(ServerConstants.SYSTEM_TIMER_TASK_EMAIL_ID, new Date(dateLastRunMillis));
            }

            jdbcTemplate.query(
                "SELECT es.id, es.type, es.config_id, es.language_id, es.account_id, es.mission_id, es.template_id, es.email_to, es.email_bcc, es.subject, es.message, es.attachment_path, es.date_scheduled, es.retry_count FROM email_scheduler es WHERE  es.date_scheduled <= now() AND es.is_processed = "
                    + ServerConstants.PROCESS_NOTPROCESSED + " ORDER BY es.priority, es.id LIMIT 3",
                new RowCallbackHandler() {

                    @Override
                    public void processRow(ResultSet rs) throws SQLException {

                        if (ServerConstants.SCHEDULER_ID == rs.getLong("id")) {
                            jdbcTemplate.update("UPDATE email_scheduler SET date_scheduled = now(), retry_count = retry_count + 1 WHERE id = ?", rs.getLong("id"));
                            return;
                        }

                        jdbcTemplate.update("UPDATE email_scheduler SET is_processed = " + ServerConstants.PROCESS_PROGRESS + " WHERE id = ?", rs.getLong("id"));

                        try {
                            Email email = new Email();

                            email.setId(rs.getLong("id"));
                            email.setType(rs.getLong("type"));
                            email.setConfigId(rs.getLong("config_id"));
                            email.setLanguageId(rs.getLong("language_id"));
                            email.setAccountId(rs.getLong("account_id"));
                            email.setMissionId(rs.getLong("mission_id"));
                            email.setTemplateId(rs.getLong("template_id"));
                            email.setEmailTo(rs.getString("email_to"));
                            email.setEmailBcc(rs.getString("email_bcc"));
                            email.setSubject(rs.getString("subject"));
                            email.setMessage(rs.getString("message"));
                            email.setAttachmentPath(rs.getString("attachment_path"));
                            email.setDateScheduled(rs.getTimestamp("date_scheduled"));
                            email.setRetryCount(rs.getInt("retry_count"));
                            email.setIsProcessed(ServerConstants.PROCESS_PROGRESS);

                            emailTaskExecutor.execute(new EmailTaskExecutor(email, emailService, accountService));

                        } catch (TaskRejectedException tre) {
                            logger.error("run#EmailId=" + rs.getLong("id") + "###TaskRejectedException: ", tre);

                            jdbcTemplate.update("UPDATE email_scheduler SET is_processed = " + ServerConstants.PROCESS_NOTPROCESSED + " WHERE id = ?", rs.getLong("id"));
                        }
                    }
                });
        } catch (Exception e) {
            logger.error("run###Exception: ", e);
            emailService.sendSystemEmail("Email TimerTask Error", EmailService.EMAIL_TYPE_EXCEPTION, null, null, "EmailTimerTask#run###Exception:<br />" + e.getMessage());
        }
    }
}
