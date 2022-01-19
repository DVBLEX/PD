package com.pad.server.web.restcontrollers;

import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.jsonentities.api.AccountStatementJson;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.statement.StatementService;
import com.pad.server.web.configuration.AppTestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pad.server.web.configuration.TestData.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppTestConfig.class })
public class AccountRESTControllerTest {

    private static final String        ACCOUNT_CODE = "33dec95c8788f4805a823fd93ec9bbc4008a60a67cafd045ec04dfdc4d1b53ee";
    private static final long          ACCOUNT_ID   = 1L;
    private static final long          OPERATOR_ID  = 2L;
    @Mock
    private AccountService             accountService;

    @Mock
    private ActivityLogService         activityLogService;

    @Mock
    private OperatorService            operatorService;

    @Mock
    private StatementService           statementService;

    @InjectMocks
    private AccountRESTController      accountRESTController;

    private MockMvc                    mockMvc;
    private List<AccountStatementJson> statementJsonList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountRESTController).build();
        statementJsonList = createStatementsList();
    }

    private List<AccountStatementJson> createStatementsList() {
        List<AccountStatementJson> statementJsonList = new ArrayList<>();
        AccountStatementJson accountStatementJson = new AccountStatementJson();
        statementJsonList.add(accountStatementJson);
        return statementJsonList;
    }

    @Test
    //    @Ignore
    public void getStatements() throws Exception {
        when(statementService.getAccountStatementsList(any(AccountStatementJson.class))).thenReturn(statementJsonList);
        when(accountService.getOpeningBalance(anyString(), any(Date.class))).thenReturn(null);

        mockMvc.perform(post("/account/statement/list")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(ACCOUNT_STATEMENTS_REQUEST_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("responseText", is("Success.")))
                .andExpect(jsonPath("dataList", hasSize(1)));

        verify(statementService, times(1)).getAccountStatementsList(any(AccountStatementJson.class));
        //        verify(accountService, times(1)).getOpeningBalance(eq("33dec95c8788f4805a823fd93ec9bbc4008a60a67cafd045ec04dfdc4d1b53ee"), any());
    }

    @Test
    //    @Ignore
    public void getStatementsWithOpeningBalance() throws Exception {
        when(statementService.getAccountStatementsList(any(AccountStatementJson.class))).thenReturn(new ArrayList<>());
        when(accountService.getOpeningBalance(anyString(), any(Date.class))).thenReturn(BigDecimal.valueOf(1000));

        mockMvc.perform(post("/account/statement/list")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(ACCOUNT_STATEMENTS_REQUEST_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("responseText", is("Success.")));
//                .andExpect(jsonPath("dataList", hasSize(1)))
//                .andExpect(jsonPath("dataList[0].balance", is(1000)));

        verify(statementService, times(1)).getAccountStatementsList(any(AccountStatementJson.class));
        //        verify(accountService, times(1)).getOpeningBalance(eq("33dec95c8788f4805a823fd93ec9bbc4008a60a67cafd045ec04dfdc4d1b53ee"), any());
    }

    @Test
    public void updateLowAccountBalanceWarn() throws Exception {
        when(accountService.getAccountByCode(eq(ACCOUNT_CODE))).thenReturn(getAccountStub());
        when(operatorService.getOperatorByAccountId(ACCOUNT_ID)).thenReturn(getOperatorStub());

        mockMvc.perform(post("/account/update/lowAccountBalanceWarn")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(ACCOUNT_LOW_AMOUNT_BALANCE_WARN_REQUEST_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("responseText", is("Success.")));

        ArgumentCaptor<Account> argumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountService, times(1)).updateAccount(argumentCaptor.capture());
        assertTrue(argumentCaptor.getValue().getIsSendLowAccountBalanceWarn());
        assertEquals(BigDecimal.valueOf(6000), argumentCaptor.getValue().getAmountLowAccountBalanceWarn());
    }

    @Test
    public void updateEmptyLowAccountBalanceWarn() throws Exception {
        when(accountService.getAccountByCode(eq(ACCOUNT_CODE))).thenReturn(getAccountStub());
        when(operatorService.getOperatorByAccountId(ACCOUNT_ID)).thenReturn(getOperatorStub());

        mockMvc.perform(post("/account/update/lowAccountBalanceWarn")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(ACCOUNT_EMPTY_LOW_AMOUNT_BALANCE_WARN_TRUE_REQUEST_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("responseText", is("Success.")));

        ArgumentCaptor<Account> argumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountService, times(1)).updateAccount(argumentCaptor.capture());
        assertTrue(argumentCaptor.getValue().getIsSendLowAccountBalanceWarn());
        assertEquals(BigDecimal.valueOf(5000), argumentCaptor.getValue().getAmountLowAccountBalanceWarn());
    }

    @Test
    public void updateLowAccountBalanceWarnWhenFalse() throws Exception {
        when(accountService.getAccountByCode(eq(ACCOUNT_CODE))).thenReturn(getAccountStub());
        when(operatorService.getOperatorByAccountId(ACCOUNT_ID)).thenReturn(getOperatorStub());

        mockMvc.perform(post("/account/update/lowAccountBalanceWarn")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(ACCOUNT_IS_LOW_AMOUNT_BALANCE_WARN_FALSE_REQUEST_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("responseText", is("Success.")));

        ArgumentCaptor<Account> argumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountService, times(1)).updateAccount(argumentCaptor.capture());
        assertFalse(argumentCaptor.getValue().getIsSendLowAccountBalanceWarn());
        assertEquals(BigDecimal.valueOf(5000), argumentCaptor.getValue().getAmountLowAccountBalanceWarn());
    }

    @Test(expected = NestedServletException.class)
    public void updateNegativeLowAccountBalanceWarn() throws Exception {
        when(accountService.getAccountByCode(eq(ACCOUNT_CODE))).thenReturn(getAccountStub());

        mockMvc.perform(post("/account/update/lowAccountBalanceWarn")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(ACCOUNT_NEGATIVE_LOW_AMOUNT_BALANCE_WARN_REQUEST_JSON));

        verify(accountService, never()).updateAccount(any(Account.class));
    }

    @Test(expected = NestedServletException.class)
    public void updateLowAccountBalanceWarnWithEmptyJSON() throws Exception {
        mockMvc.perform(post("/account/update/lowAccountBalanceWarn")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(EMPTY_JSON));

        verify(accountService, never()).updateAccount(any(Account.class));
    }

    @Test(expected = NestedServletException.class)
    public void updateLowAccountBalanceWarnWithInvalidCode() throws Exception {
        when(accountService.getAccountByCode(eq(ACCOUNT_CODE))).thenReturn(null);

        mockMvc.perform(post("/account/update/lowAccountBalanceWarn")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(ACCOUNT_LOW_AMOUNT_BALANCE_WARN_REQUEST_JSON));

        verify(accountService, never()).updateAccount(any(Account.class));
    }

    private Account getAccountStub() {
        Account account = new Account();
        account.setId(ACCOUNT_ID);
        account.setAmountLowAccountBalanceWarn(BigDecimal.valueOf(5000));
        return account;
    }

    private Operator getOperatorStub() {
        Operator operator = new Operator();
        operator.setId(OPERATOR_ID);
        return operator;
    }
}
