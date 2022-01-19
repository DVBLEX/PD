package com.pad.server.web.restcontrollers;

import static com.pad.server.web.configuration.TestData.EMPTY_JSON;
import static com.pad.server.web.configuration.TestData.SESSION_JSON;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.entities.Session;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.session.SessionService;
import com.pad.server.web.configuration.AppTestConfig;
import org.springframework.web.util.NestedServletException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppTestConfig.class })
public class SessionRESTControllerTest {

    private static final String   SESSION_CODE = "55252ad8cad3ac7ea064c492d55ac2e7c97316dd93b0caf5c8fc0ca541b2bff0";
    private static final long     SESSION_ID   = 1L;
    private static final long     OPERATOR_ID  = 2L;

    @Mock
    private ActivityLogService    activityLogService;

    @Mock
    private OperatorService       operatorService;

    @Mock
    private SessionService        sessionService;

    @InjectMocks
    private SessionRESTController sessionRESTController;

    private MockMvc               mockMvc;

    @Before
    public void setUp() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        // Mockito.whens() for your authorization object
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(sessionRESTController).build();
    }

    @Test
    public void endAssignedSession() throws Exception {
        Session sessionStub = getSessionStub();
        sessionStub.setStatus(ServerConstants.SESSION_STATUS_ASSIGNED);

        when(sessionService.getSessionByCode(SESSION_CODE)).thenReturn(sessionStub);
        when(operatorService.getOperator(any())).thenReturn(getOperatorStub());

        mockMvc.perform(post("/session/end")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(SESSION_JSON))
            .andExpect(status().isOk());

        ArgumentCaptor<Session> argumentCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionService, times(1)).updateSession(argumentCaptor.capture());
        assertEquals(ServerConstants.SESSION_STATUS_VALIDATED, argumentCaptor.getValue().getStatus());
    }

    @Test
    public void endStartSession() throws Exception {
        Session sessionStub = getSessionStub();
        sessionStub.setStatus(ServerConstants.SESSION_STATUS_START);

        when(sessionService.getSessionByCode(SESSION_CODE)).thenReturn(sessionStub);
        when(operatorService.getOperator(any())).thenReturn(getOperatorStub());

        mockMvc.perform(post("/session/end")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(SESSION_JSON))
            .andExpect(status().isOk());

        ArgumentCaptor<Session> argumentCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionService, times(1)).updateSession(argumentCaptor.capture());
        assertEquals(ServerConstants.SESSION_STATUS_END, argumentCaptor.getValue().getStatus());
    }

    @Test(expected = NestedServletException.class)
    public void endSessionWithBlankCode() throws Exception {
        Session sessionStub = getSessionStub();
        sessionStub.setStatus(ServerConstants.SESSION_STATUS_START);

        mockMvc.perform(post("/session/end")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(EMPTY_JSON))
            .andExpect(status().isOk());
    }

    @Test(expected = NestedServletException.class)
    public void endSessionWhenNotFound() throws Exception {
        when(sessionService.getSessionByCode(SESSION_CODE)).thenReturn(null);

        mockMvc.perform(post("/session/end")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(SESSION_JSON))
            .andExpect(status().isOk());

        verify(sessionService, never()).updateSession(any());
    }

    @Test(expected = NestedServletException.class)
    public void endSessionWithUnexpectedStatus() throws Exception {
        Session sessionStub = getSessionStub();
        sessionStub.setStatus(ServerConstants.SESSION_STATUS_END);

        when(sessionService.getSessionByCode(SESSION_CODE)).thenReturn(sessionStub);

        mockMvc.perform(post("/session/end")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(SESSION_JSON))
            .andExpect(status().isOk());

        verify(sessionService, never()).updateSession(any());
    }

    private Operator getOperatorStub() {
        Operator operator = new Operator();
        operator.setId(OPERATOR_ID);
        return operator;
    }

    private Session getSessionStub() {
        Session session = new Session();
        session.setId(SESSION_ID);
        session.setCode(SESSION_JSON);
        return session;
    }
}
