package com.pad.server.base.tasks;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.ServerConstants;

@Component
@Transactional
public class ReceiptInvoiceNumberGeneratorTimerTask implements Runnable {

    private static final Logger logger         = Logger.getLogger(ReceiptInvoiceNumberGeneratorTimerTask.class);

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    private final long          NUMBER_INSERTS = 1000000;

    @Override
    // @Scheduled(cron = "0 08 12 * * *")
    public void run() {

        generateReceiptNumbers();
        generateInvoiceNumbers();

    }

    private void generateReceiptNumbers() {
        long numberInsertCount = 0;

        while (numberInsertCount <= NUMBER_INSERTS) {

            try {

                String number = null;
                boolean numberExists = true;
                int exitCount = 0;

                while (numberExists) {

                    number = RandomStringUtils.randomAlphanumeric(ServerConstants.SIZE_RECEIPT_UNIQUE_URL);

                    try {

                        numberExists = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM `pad`.`receipt_numbers` WHERE `receipt_numbers`.`number` = '" + number + "'",
                            Long.class) > 0;

                    } catch (Exception e) {
                        logger.error("ReceiptInvoiceNumberGeneratorTimerTask: ReceiptNumber Generation error. Exception: " + e.getMessage());
                        numberExists = true;
                        exitCount++;
                        if (exitCount > 5)
                            throw e;
                    }
                }

                long nullValCount = jdbcTemplate.queryForObject("SELECT COUNT(receipt_id) FROM `pad`.`receipt_numbers` WHERE `receipt_numbers`.`number` IS NULL LIMIT 1",
                    Long.class);

                if (nullValCount > 0) {

                    jdbcTemplate.update("UPDATE `pad`.`receipt_numbers` SET `receipt_numbers`.`number` = '" + number + "' WHERE `receipt_numbers`.`number` IS NULL LIMIT 1");

                } else {

                    jdbcTemplate.update("INSERT INTO `pad`.`receipt_numbers` (`number`) VALUES ('" + number + "')");
                }

                numberInsertCount++;

            } catch (Exception e) {
                logger.error("ReceiptInvoiceNumberGeneratorTimerTask#generateReceiptNumbers##Exception: " + e.getMessage());
            }

        }

    }

    private void generateInvoiceNumbers() {
        long numberInsertCount = 0;

        while (numberInsertCount <= NUMBER_INSERTS) {

            try {

                String number = null;
                boolean numberExists = true;
                int exitCount = 0;

                while (numberExists) {

                    number = RandomStringUtils.randomAlphanumeric(ServerConstants.SIZE_RECEIPT_UNIQUE_URL);

                    try {

                        numberExists = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM `pad`.`invoice_numbers` WHERE `invoice_numbers`.`number` = '" + number + "'",
                            Long.class) > 0;

                    } catch (Exception e) {
                        logger.error("ReceiptInvoiceNumberGeneratorTimerTask: InvoiceNumber Generation error. Exception: " + e.getMessage());
                        numberExists = true;
                        exitCount++;
                        if (exitCount > 5)
                            throw e;
                    }
                }

                long nullValCount = jdbcTemplate.queryForObject("SELECT COUNT(invoice_id) FROM `pad`.`invoice_numbers` WHERE `invoice_numbers`.`number` IS NULL LIMIT 1",
                    Long.class);

                if (nullValCount > 0) {

                    jdbcTemplate.update("UPDATE `pad`.`invoice_numbers` SET `invoice_numbers`.`number` = '" + number + "' WHERE `invoice_numbers`.`number` IS NULL LIMIT 1");

                } else {

                    jdbcTemplate.update("INSERT INTO `pad`.`invoice_numbers` (`number`) VALUES ('" + number + "')");
                }

                numberInsertCount++;

            } catch (Exception e) {
                logger.error("ReceiptInvoiceNumberGeneratorTimerTask#generateInvoiceNumbers##Exception: " + e.getMessage());
            }

        }

    }
}
