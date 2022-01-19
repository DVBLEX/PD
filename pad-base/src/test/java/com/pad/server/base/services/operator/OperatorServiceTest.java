package com.pad.server.base.services.operator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.configuration.AppTestConfig;
import com.pad.server.base.entities.Operator;

@ActiveProfiles("test")
@Transactional("transactionManager")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppTestConfig.class })
@TestPropertySource(properties = { "system.environment=DEV", "system.shortname=PAD" })
public class OperatorServiceTest {

    private static final long TEST_OPERATOR_ID = 1;

    @Autowired
    @InjectMocks
    private OperatorService   operatorService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUnlockOperator() {
        Operator operator = operatorService.getOperatorById(TEST_OPERATOR_ID);
        operatorService.unlockOperator(operator);

        operator = operatorService.getOperatorById(TEST_OPERATOR_ID);
        assertFalse("Operator should not be locked", operator.getIsLocked());
        assertEquals("0 login failure count expected", 0, operator.getLoginFailureCount());
    }
}
