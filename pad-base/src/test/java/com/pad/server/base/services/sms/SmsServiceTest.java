package com.pad.server.base.services.sms;

import static junit.framework.TestCase.assertEquals;

import java.util.HashMap;

import javax.sql.DataSource;

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

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.configuration.AppTestConfig;
import com.pad.server.base.entities.Sms;
import com.pad.server.base.exceptions.PADException;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppTestConfig.class })
@TestPropertySource(properties = { "system.environment=DEV", "system.shortname=PAD" })
public class SmsServiceTest {

    private HashMap<String, Object> params;
    @Autowired
    @InjectMocks
    private SmsService              smsService;
    private JdbcTemplate            jdbcTemplate;
    private Sms                     scheduledSms;
    private static final String     CORRECT_MSISDN     = "221338222222";
    private static final long       CORRECT_ACCOUNT_ID = 1L;
    private static final long       CORRECT_MISSION_ID = 2L;

    @Autowired
    void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        params = new HashMap<>();
        scheduledSms = generateScheduledSms();
    }

    @Test
    @Sql(statements = { "TRUNCATE TABLE sms_log", "TRUNCATE TABLE sms_scheduler" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void scheduleSmsByType() throws PADException {
        smsService.scheduleSmsByType(scheduledSms, ServerConstants.SMS_MISSION_ADDED_NOTIFICATION_TEMPLATE_TYPE, params);
        assertEquals("Number of rows in the [sms_log] table.", 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "sms_log"));
        assertEquals("Number of rows in the [sms_scheduler] table.", 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "sms_scheduler"));
        jdbcTemplate.query("SELECT msisdn, language_id, account_id, mission_id, trip_id FROM sms_log", rs -> {
            assertEquals("msisdn value in [sms_log] table", CORRECT_MSISDN, rs.getString("msisdn"));
            assertEquals("language_id value in [sms_log] table", ServerConstants.LANGUAGE_EN_ID, rs.getLong("language_id"));
            assertEquals("account_id value in [sms_log] table", CORRECT_ACCOUNT_ID, rs.getLong("account_id"));
            assertEquals("mission_id value in [sms_log] table", CORRECT_MISSION_ID, rs.getLong("mission_id"));
            assertEquals("trip_id value in [sms_log] table", ServerConstants.DEFAULT_LONG, rs.getLong("trip_id"));
        });
        jdbcTemplate.query("SELECT msisdn, language_id, account_id, mission_id, trip_id FROM sms_scheduler", rs -> {
            assertEquals("msisdn value in [sms_scheduler] table", CORRECT_MSISDN, rs.getString("msisdn"));
            assertEquals("language_id value in [sms_scheduler] table", ServerConstants.LANGUAGE_EN_ID, rs.getLong("language_id"));
            assertEquals("account_id value in [sms_scheduler] table", CORRECT_ACCOUNT_ID, rs.getLong("account_id"));
            assertEquals("mission_id value in [sms_scheduler] table", CORRECT_MISSION_ID, rs.getLong("mission_id"));
            assertEquals("trip_id value in [sms_scheduler] table", ServerConstants.DEFAULT_LONG, rs.getLong("trip_id"));
        });
    }

    private Sms generateScheduledSms() {
        Sms scheduledSms = new Sms();
        scheduledSms.setMsisdn(CORRECT_MSISDN);
        scheduledSms.setLanguageId(ServerConstants.LANGUAGE_EN_ID);
        scheduledSms.setAccountId(CORRECT_ACCOUNT_ID);
        scheduledSms.setMissionId(CORRECT_MISSION_ID);
        scheduledSms.setTripId(ServerConstants.DEFAULT_LONG);
        return scheduledSms;
    }
}
