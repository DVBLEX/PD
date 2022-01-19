package com.pad.server.base.tasks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.Email;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.email.EmailService;

public class EmailTaskExecutor implements Runnable {

    private static final Logger logger = Logger.getLogger(EmailTaskExecutor.class);

    private EmailService        emailService;

    private Email               email;

    private AccountService      accountService;

    public EmailTaskExecutor(Email email, EmailService emailService, AccountService accountService) {
        this.email = email;
        this.emailService = emailService;
        this.accountService = accountService;
    }

    @Override
    public void run() {

        try {
            email.setEmailTo(email.getEmailTo().replaceAll(" ", ""));
            if (emailService.getIsLive() || email.getAccountId() == ServerConstants.DEFAULT_LONG) {
                emailService.sendEmail(email);
            } else {
                Account account = accountService.getAccountById(email.getAccountId());

                String[] emailsTo = email.getEmailTo().split(",");
                List<String> emailsSend = new ArrayList<>();
                List<String> emailsNotSend = new ArrayList<>();

                for (String emailTo : emailsTo) {
                    if (account != null) {
                        emailsSend.add(emailTo);
                    } else {
                        emailsNotSend.add(emailTo);
                    }
                }

                if (!emailsSend.isEmpty()) {
                    email.setEmailTo(String.join(",", emailsSend));
                    emailService.sendEmail(email);
                } else if (!emailsNotSend.isEmpty()) {
                    email.setDateProcessed(new Date());
                    email.setResponseCode(ServerResponseConstants.SUCCESS_TEST_CODE);
                    email.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
                }

            }

            email.setIsProcessed(ServerConstants.PROCESS_PROCESSED);
            emailService.updateEmail(email);
            emailService.deleteScheduledEmail(email.getId());

        } catch (PADException rbse) {

            email.setRetryCount(email.getRetryCount() + 1);
            email.setResponseCode(rbse.getResponseCode());
            email.setResponseText(rbse.getResponseText());

            if (ServerResponseConstants.INVALID_EMAIL_CODE == email.getResponseCode()) {
                emailService.updateEmail(email);
                emailService.deleteScheduledEmail(email.getId());
            } else {
                if (email.getRetryCount() == 1) {
                    email.setIsProcessed(ServerConstants.PROCESS_NOTPROCESSED);
                    email.setDateScheduled(new Date(email.getDateScheduled().getTime() + 60l * 1000l));
                    emailService.updateScheduledEmail(email);
                } else if (email.getRetryCount() < 7) {
                    email.setIsProcessed(ServerConstants.PROCESS_NOTPROCESSED);
                    email.setDateScheduled(new Date(email.getDateScheduled().getTime() + 10l * 60l * 1000l));
                    emailService.updateScheduledEmail(email);
                } else {
                    emailService.updateScheduledEmail(email);
                    // emailService.updateEmail(email);
                    // emailService.deleteScheduledEmail(email.getId());
                }
            }

        } catch (Exception e) {

            logger.error("EmailTaskExecutor##Exception: ", e);
        }
    }
}
