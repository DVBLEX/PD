package com.pad.server.base.tasks;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.system.SystemService;

@Component
public class SystemStatusCheckTimerTask {

    private static final Logger logger  = Logger.getLogger(SystemStatusCheckTimerTask.class);

    private boolean             isEmail = false;

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private JdbcTemplate        jdbcTemplateRepl;

    @Autowired
    private EmailService        emailService;

    @Autowired
    private SystemService       systemService;

    @Value("${system.environment}")
    private String              systemEnvironment;

    @Autowired
    private TaskExecutor        systemStatusCheckTaskExecutor;

    @Scheduled(fixedDelay = 360000, initialDelay = 30000)
    public void run() {

        try {
            // logger.info("run#");
            systemService.updateSystemTimerTaskDateLastRun(ServerConstants.SYSTEM_TIMER_TASK_SYSTEM_STATUS_CHECK_ID, new Date());

            isEmail = false;

            final StringBuilder content = new StringBuilder("<table><tr><td>System Time</td><td>");
            content.append(new Date());
            content.append("</td></tr></table>");
            content.append("<br /><table><tr><th>ID</th><th>Check</th><th>QueryResult</th><th>CheckResult</th></tr>");

            jdbcTemplate.query("SELECT * FROM system_checks WHERE is_active = ? AND (query_time_from <= TIME(now()) AND query_time_to >= TIME(now()) OR comparison_result = ?)",
                new RowCallbackHandler() {

                    @Override
                    public void processRow(ResultSet rs) throws SQLException {

                        logger.info("run#start#id=" + rs.getLong("id") + ", name=" + rs.getString("name"));

                        final Data data = new Data();

                        switch (rs.getInt("type")) {

                            case ServerConstants.SYSTEM_CHECK_TYPE_QUERY:

                                data.startTime = System.currentTimeMillis();

                                try {
                                    data.queryResult = jdbcTemplate.queryForObject(rs.getString("query"), Integer.class);
                                } catch (Exception e) {
                                    data.queryResult = ServerConstants.DEFAULT_INT;
                                }

                                data.finishTime = System.currentTimeMillis();

                                break;

                            case ServerConstants.SYSTEM_CHECK_TYPE_QUERY_REPLICATION:

                                data.startTime = System.currentTimeMillis();

                                jdbcTemplateRepl.query("SHOW SLAVE STATUS", new RowCallbackHandler() {

                                    @Override
                                    public void processRow(ResultSet rs) throws SQLException {

                                        data.finishTime = System.currentTimeMillis();

                                        BigDecimal secondsBehindMaster = rs.getBigDecimal("Seconds_Behind_Master");
                                        if (secondsBehindMaster == null) {
                                            data.queryResult = ServerConstants.DEFAULT_INT;
                                        } else if (secondsBehindMaster.compareTo(new BigDecimal(Integer.MAX_VALUE)) == -1) {
                                            data.queryResult = secondsBehindMaster.intValue();
                                        } else {
                                            data.queryResult = Integer.MAX_VALUE;
                                        }
                                    }
                                });

                                break;

                            case ServerConstants.SYSTEM_CHECK_TYPE_HTTP_GET:

                                try {
                                    systemStatusCheckTaskExecutor
                                        .execute(new SystemStatusCheckTaskExecutor(jdbcTemplate, rs.getLong("id"), rs.getString("name"), rs.getString("query"),
                                            rs.getString("query_params"), rs.getString("config_params"), rs.getInt("comparison_operator"), rs.getInt("comparison_value"), data));

                                } catch (TaskRejectedException tre) {
                                    logger.error("run#id=" + rs.getLong("id") + ", name=" + rs.getString("name") + "###TaskRejectedException: ", tre);
                                }

                                break;

                            default:
                                break;
                        }

                        if (rs.getInt("type") != ServerConstants.SYSTEM_CHECK_TYPE_HTTP_GET) {

                            if (data.queryResult == ServerConstants.DEFAULT_INT) {
                                data.comparisonResult = true;
                            } else {
                                switch (rs.getInt("comparison_operator")) {
                                    case ServerConstants.COMP_OP_EQUAL_TO:
                                        data.comparisonResult = (data.queryResult == rs.getInt("comparison_value"));
                                        break;

                                    case ServerConstants.COMP_OP_LESS_THAN:
                                        data.comparisonResult = (data.queryResult < rs.getInt("comparison_value"));
                                        break;

                                    case ServerConstants.COMP_OP_GREATER_THAN:
                                        data.comparisonResult = (data.queryResult > rs.getInt("comparison_value"));
                                        break;

                                    case ServerConstants.COMP_OP_LESS_THAN_EQUAL_TO:
                                        data.comparisonResult = (data.queryResult <= rs.getInt("comparison_value"));
                                        break;

                                    case ServerConstants.COMP_OP_GREATER_THAN_EQUAL_TO:
                                        data.comparisonResult = (data.queryResult >= rs.getInt("comparison_value"));
                                        break;

                                    case ServerConstants.COMP_OP_NOT_EQUAL_TO:
                                        data.comparisonResult = (data.queryResult != rs.getInt("comparison_value"));
                                        break;
                                }
                            }

                            if (data.finishTime > 0) {
                                data.queryExecutionTime = data.finishTime - data.startTime;
                            }

                            logger.info("run#finish#id=" + rs.getLong("id") + ", name=" + rs.getString("name") + ", queryResult=" + data.queryResult + ", comparisonResult="
                                + data.comparisonResult + ", queryExecutionTime=" + data.queryExecutionTime);

                            jdbcTemplate.update("UPDATE system_checks SET query_result = ?, query_execution_time = ?, comparison_result = ?, date_edited = ? WHERE id = ?",
                                data.queryResult, data.queryExecutionTime, data.comparisonResult, new Date(), rs.getLong("id"));

                            if (data.comparisonResult) {
                                isEmail = true;
                                content.append("<tr style=\"color: red;\"><td>" + rs.getLong("id") + "</td><td>" + rs.getString("name") + "</td><td align=\"center\">"
                                    + data.queryResult + "</td><td>" + data.comparisonResult + "</td></tr>");
                            } else {
                                content.append("<tr style=\"color: green;\"><td>" + rs.getLong("id") + "</td><td>" + rs.getString("name") + "</td><td align=\"center\">"
                                    + data.queryResult + "</td><td>" + data.comparisonResult + "</td></tr>");
                            }
                        }
                    }
                }, true, true);

            content.append("</table>");

            logger.info("run#Status: " + content.toString());
            if (ServerConstants.SYSTEM_ENVIRONMENT_PROD.equals(systemEnvironment) && isEmail) {
                emailService.sendSystemEmail("Error - Status " + ServerUtil.formatDate(ServerConstants.EXPORT_yyyyMMddHHmmss, new Date()), EmailService.EMAIL_TYPE_FAILURE,
                    content.toString(), null, null);
            }

        } catch (Exception e) {
            logger.error("run###Exception: ", e);
            emailService.sendSystemEmail("System StatusCheck TimerTask Error", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "SystemStatusCheckTimerTask#run###Exception:<br />" + e.getMessage());
        }
    }

    class Data {

        long    startTime          = 0;
        long    finishTime         = 0;
        long    queryExecutionTime = 0;
        int     queryResult        = ServerConstants.DEFAULT_INT;
        boolean comparisonResult   = false;
    }
}
