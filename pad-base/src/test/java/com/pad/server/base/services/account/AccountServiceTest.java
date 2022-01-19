package com.pad.server.base.services.account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.configuration.AppTestConfig;
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.Email;
import com.pad.server.base.entities.Sms;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.sms.SmsService;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppTestConfig.class })
@TestPropertySource(properties = { "system.environment=DEV", "system.shortname=PAD" })
public class AccountServiceTest {

    private static final BigDecimal AMOUNT_LOW_ACCOUNT_BALANCE_WARN = new BigDecimal(5000);
    private static final BigDecimal AMOUNT_LOW_ACCOUNT_BALANCE      = new BigDecimal(3000);
    private static final BigDecimal AMOUNT_NORMAL_ACCOUNT_BALANCE   = new BigDecimal(6000);
    @Mock
    private SmsService              smsService;
    @Mock
    private EmailService            emailService;
    @Autowired
    @InjectMocks
    private AccountService          accountService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLowAccountBalanceWarnFlagOff() throws PADException {
        Account accountStub = getAccountStub();
        accountStub.setIsSendLowAccountBalanceWarn(false);
        accountService.notifyAccountWhenLowBalance(accountStub, AMOUNT_LOW_ACCOUNT_BALANCE);
        verify(smsService, never()).scheduleSmsByType(any(Sms.class), anyLong(), any());
        verify(emailService, never()).scheduleEmailByType(any(Email.class), anyLong(), any());
    }

    @Test
    public void testLowAccountBalanceWarnFlagOnBalanceAbove() throws PADException {
        Account accountStub = getAccountStub();
        accountStub.setIsSendLowAccountBalanceWarn(true);
        accountService.notifyAccountWhenLowBalance(accountStub, AMOUNT_NORMAL_ACCOUNT_BALANCE);
        verify(smsService, never()).scheduleSmsByType(any(Sms.class), anyLong(), any());
        verify(emailService, never()).scheduleEmailByType(any(Email.class), anyLong(), any());
    }

    @Test
    public void testLowAccountBalanceSms() throws PADException {
        Account accountStub = getAccountStub();
        accountStub.setIsSendLowAccountBalanceWarn(true);
        accountService.notifyAccountWhenLowBalance(accountStub, AMOUNT_LOW_ACCOUNT_BALANCE);
        verify(smsService, times(1)).scheduleSmsByType(any(Sms.class), eq(ServerConstants.SMS_LOW_ACCOUNT_BALANCE_WARNING_TYPE), any());
        verify(emailService, never()).scheduleEmailByType(any(Email.class), anyLong(), any());
    }

    @Test
    public void testLowAccountBalanceEmail() throws PADException {
        Account accountStub = getAccountStub();
        accountStub.setIsSendLowAccountBalanceWarn(true);
        accountStub.setEmailListInvoiceStatement("test@test.com");
        accountService.notifyAccountWhenLowBalance(accountStub, AMOUNT_LOW_ACCOUNT_BALANCE);
        verify(emailService, times(1)).scheduleEmailByType(any(Email.class), eq(ServerConstants.EMAIL_LOW_ACCOUNT_BALANCE_WARNING_TYPE), any());
        verify(smsService, never()).scheduleSmsByType(any(Sms.class), anyLong(), any());
    }

    private Account getAccountStub() {
        Account account = new Account();
        account.setId(1L);
        account.setType(ServerConstants.ACCOUNT_TYPE_COMPANY);
        account.setLanguageId(ServerConstants.LANGUAGE_EN_ID);
        account.setFirstName("John");
        account.setCompanyName("Test Company");
        account.setMsisdn("221338222222");
        account.setAmountLowAccountBalanceWarn(AMOUNT_LOW_ACCOUNT_BALANCE_WARN);
        return account;
    }
}
