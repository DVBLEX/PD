package com.pad.server.web.restcontrollers;

import static com.pad.server.web.configuration.TestData.TRANSACTION_TYPE_FLAGS_JSON;
import static com.pad.server.web.configuration.TestData.TRANSACTION_TYPE_FLAGS_WITHOUT_GATE_JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.pad.server.base.entities.Operator;
import com.pad.server.base.entities.PortOperator;
import com.pad.server.base.entities.PortOperatorGate;
import com.pad.server.base.entities.PortOperatorTransactionType;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.system.SystemService;
import com.pad.server.base.services.trip.TripService;
import com.pad.server.web.configuration.AppTestConfig;
import com.pad.server.web.services.padanpr.PadAnprService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppTestConfig.class })
public class SystemRESTControllerTest {

    private static final long    OPERATOR_ID           = 1L;
    private static final long    PORT_OPERATOR_GATE_ID = 1004L;
    @InjectMocks
    private SystemRESTController systemRESTController;
    @Mock
    private ActivityLogService   activityLogService;
    @Mock
    private OperatorService      operatorService;
    @Mock
    private PadAnprService       padAnprService;
    @Mock
    private SystemService        systemService;
    @Mock
    private TripService          tripService;
    private MockMvc              mockMvc;

    @Before
    public void setUp() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(systemRESTController).build();

        Mockito.when(operatorService.getOperator(any())).thenReturn(getOperatorStub());
        Mockito.when(systemService.getPortOperatorFromMap(anyLong())).thenReturn(getPortOperatorStub());
        Mockito.when(systemService.getPortOperatorGateById(PORT_OPERATOR_GATE_ID)).thenReturn(getPortOperatorGateStub());
    }

    @Test
    public void testUpdateTransactionTypeFlags() throws Exception {
        mockMvc.perform(post("/system/update/transaction/type/flags")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(TRANSACTION_TYPE_FLAGS_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("responseText", is("Success.")));
        ArgumentCaptor<PortOperatorTransactionType> argumentCaptor = ArgumentCaptor.forClass(PortOperatorTransactionType.class);
        Mockito.verify(tripService, Mockito.times(1)).updateExistingMissionTripsFlags(argumentCaptor.capture());
        assertEquals(PORT_OPERATOR_GATE_ID, argumentCaptor.getValue().getPortOperatorGate().getId());
    }

    @Test
    public void testUpdateTransactionTypeFlagsWithoutPortOperatorGate() throws Exception {
        mockMvc.perform(post("/system/update/transaction/type/flags")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(TRANSACTION_TYPE_FLAGS_WITHOUT_GATE_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("responseText", is("Success.")));
        ArgumentCaptor<PortOperatorTransactionType> argumentCaptor = ArgumentCaptor.forClass(PortOperatorTransactionType.class);
        Mockito.verify(tripService, Mockito.times(1)).updateExistingMissionTripsFlags(argumentCaptor.capture());
        Mockito.verify(systemService, Mockito.never()).getPortOperatorGateById(anyLong());
        assertNull(argumentCaptor.getValue().getPortOperatorGate());
    }

    private PortOperatorGate getPortOperatorGateStub() {
        PortOperatorGate portOperatorGate = new PortOperatorGate();
        portOperatorGate.setId(PORT_OPERATOR_GATE_ID);
        return portOperatorGate;
    }

    private PortOperator getPortOperatorStub() {
        PortOperator portOperator = new PortOperator();
        portOperator.setPortOperatorTransactionTypesList(Collections.singletonList(getPortOperatorTransactionTypeStub()));
        return portOperator;
    }

    private PortOperatorTransactionType getPortOperatorTransactionTypeStub() {
        PortOperatorTransactionType portOperatorTransactionType = new PortOperatorTransactionType();
        portOperatorTransactionType.setTransactionType(1);
        return portOperatorTransactionType;
    }

    private Operator getOperatorStub() {
        Operator operator = new Operator();
        operator.setId(OPERATOR_ID);
        return operator;
    }
}
