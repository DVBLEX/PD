package com.pad.server.base.tasks;

import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.ServerConstants;

@Component
@Transactional
public class AccountNumberGeneratorTimerTask implements Runnable {

    private static final Logger logger         = Logger.getLogger(AccountNumberGeneratorTimerTask.class);

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    private final long          NUMBER_INSERTS = 700000;

    @Override
    // @Scheduled(cron = "0 43 10 * * *")
    public void run() {
        generateAccountNumbers();
    }

    private void generateAccountNumbers() {

        logger.info("AccountNumberGeneratorTimerTask#Start");

        long numberInsertCount = 0;

        while (numberInsertCount <= NUMBER_INSERTS) {

            try {

                long number = -1;
                boolean numberExists = true;
                int exitCount = 0;

                while (numberExists) {

                    number = new Random().longs(ServerConstants.ACCOUNT_NUMBER_MIN, ServerConstants.ACCOUNT_NUMBER_MAX).findFirst().getAsLong();

                    try {

                        numberExists = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM `pad`.`account_numbers` WHERE `account_numbers`.`number` = '" + number + "'",
                            Long.class) > 0;

                    } catch (Exception e) {
                        logger.error("AccountNumberGeneratorTimerTask: Account Number Generation error. Exception: " + e.getMessage());
                        numberExists = true;
                        exitCount++;
                        if (exitCount > 5)
                            throw e;
                    }
                }

                long nullValCount = jdbcTemplate.queryForObject("SELECT COUNT(account_id) FROM `pad`.`account_numbers` WHERE `account_numbers`.`number` IS NULL LIMIT 1",
                    Long.class);

                if (nullValCount > 0) {

                    jdbcTemplate.update("UPDATE `pad`.`account_numbers` SET `account_numbers`.`number` = '" + number + "' WHERE `account_numbers`.`number` IS NULL LIMIT 1");

                } else {

                    jdbcTemplate.update("INSERT INTO `pad`.`account_numbers` (`number`) VALUES ('" + number + "')");
                }

                numberInsertCount++;

            } catch (Exception e) {
                logger.error("AccountNumberGeneratorTimerTask#generateAccountNumbers##Exception: " + e.getMessage());
            }

        }

        logger.info("AccountNumberGeneratorTimerTask#End");

    }

}
