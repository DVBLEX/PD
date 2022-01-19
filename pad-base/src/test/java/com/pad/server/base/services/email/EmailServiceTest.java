package com.pad.server.base.services.email;

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.configuration.AppTestConfig;
import com.pad.server.base.entities.Email;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.TripApiJson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppTestConfig.class })
@TestPropertySource(properties = { "system.environment=DEV", "system.shortname=PAD" })
public class EmailServiceTest {

    private static final String     EMAIL_TO = "test@test.com";
    private HashMap<String, Object> params;
    @Autowired
    @InjectMocks
    private EmailService            emailService;
    private TripApiJson             tripApiJson;
    private JdbcTemplate            jdbcTemplate;
    private Email                   scheduledEmail;

    @Autowired
    void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        tripApiJson = new TripApiJson();
        params = new HashMap<>();
        scheduledEmail = generateScheduledEmail();
    }

    @Test
    @Sql(statements = { "TRUNCATE TABLE email_log", "TRUNCATE TABLE email_scheduler" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void scheduleEmailByType() throws PADException {
        scheduledEmail.setMissionId(1);
        scheduledEmail.setTripId(ServerConstants.DEFAULT_LONG);
        emailService.scheduleEmailByType(scheduledEmail, ServerConstants.EMAIL_MISSION_ADDED_NOTIFICATION_TEMPLATE_TYPE, params);
        assertEquals("Number of rows in the [email_log] table.", 1, countRowsInTable("email_log"));
        assertEquals("Number of rows in the [email_scheduler] table.", 1, countRowsInTable("email_scheduler"));
        jdbcTemplate.query("SELECT trip_id FROM email_log", rs -> {
            assertEquals("trip_id value in [email_log] table", ServerConstants.DEFAULT_LONG, rs.getLong("trip_id"));
        });

        jdbcTemplate.query("SELECT mission_id, trip_id FROM email_scheduler", rs -> {
            assertEquals("trip_id value in [email_log] table", ServerConstants.DEFAULT_LONG, rs.getLong("trip_id"));
        });
    }

    @Test
    @Sql(statements = { "TRUNCATE TABLE email_log", "TRUNCATE TABLE email_scheduler" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void sendTransporterShortNameErrorEmailNotification() throws PADException {
        emailService.sendTransporterShortNameErrorEmailNotification(tripApiJson, ServerConstants.TRANSACTION_TYPE_PICK_UP_IMPORT, ServerConstants.LANGUAGE_EN_ID,
            ServerConstants.PORT_OPERATOR_DPWORLD, EMAIL_TO, ServerConstants.EMAIL_TRANSPORTER_SHORT_NAME_MAPPING_ERROR_TEMPLATE_TYPE);
        assertEquals("Number of rows in the [email_log] table.", 1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate,"email_log"));
        assertEquals("Number of rows in the [email_scheduler] table.", 1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate,"email_scheduler"));
        jdbcTemplate.query("SELECT trip_id FROM email_log", rs -> {
            assertEquals("trip_id value in [email_log] table", ServerConstants.DEFAULT_LONG, rs.getLong("trip_id"));
        });

        jdbcTemplate.query("SELECT trip_id FROM email_scheduler", rs -> {
            assertEquals("trip_id value in [email_log] table", ServerConstants.DEFAULT_LONG, rs.getLong("trip_id"));
        });
    }

    private int countRowsInTable(String tableName) {
        return JdbcTestUtils.countRowsInTable(this.jdbcTemplate, tableName);
    }

    private Email generateScheduledEmail() {
        Email scheduledEmail = new Email();
        scheduledEmail.setEmailTo(EMAIL_TO);
        scheduledEmail.setLanguageId(ServerConstants.LANGUAGE_EN_ID);
        scheduledEmail.setAccountId(1);
        scheduledEmail.setMissionId(1);
        scheduledEmail.setAttachmentPath(ServerConstants.DEFAULT_STRING);
        return scheduledEmail;
    }

}
