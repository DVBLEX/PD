package com.pad.server.base.tasks;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.PreparedJDBCQuery;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.services.email.EmailService;

@Component
@Transactional
public class AccountBalancesTaskExecutor implements Runnable {

    private static final Logger logger = Logger.getLogger(AccountBalancesTaskExecutor.class);

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private EmailService        emailService;

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void run() {

        try {
            logger.info("run#");

            final Date dateToday = new Date();

            PreparedJDBCQuery query = new PreparedJDBCQuery();

            query.append(" INSERT INTO pad.account_balances (account_id, payment_terms_type, amount_balance, amount_overdraft_limit, amount_hold, date_created)");
            query.append(
                " SELECT id, payment_terms_type, amount_balance, amount_overdraft_limit, amount_hold, ? FROM pad.accounts WHERE is_deleted = ? AND status != ? ORDER BY id ASC");

            query.addQueryParameter(dateToday);
            query.addQueryParameter(false);
            query.addQueryParameter(ServerConstants.ACCOUNT_STATUS_PENDING_FOR_ACTIVATION);

            jdbcTemplate.update(query.getQueryString(), query.getQueryParameters());

        } catch (DataAccessException dae) {
            logger.error("run###DataAccessException: ", dae);

            emailService.sendSystemEmail("AccountBalancesTaskExecutor DataAccessException", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "AccountBalancesTaskExecutor#run###DataAccessException:<br />" + dae.getMessage());

        } catch (Exception e) {
            logger.error("run###Exception: ", e);

            emailService.sendSystemEmail("AccountBalancesTaskExecutor Exception", EmailService.EMAIL_TYPE_EXCEPTION, null, null,
                "AccountBalancesTaskExecutor#run###Exception:<br />" + e.getMessage());
        }
    }
}
