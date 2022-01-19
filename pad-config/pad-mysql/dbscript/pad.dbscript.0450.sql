USE pad;

-- ALTER TABLE `pad`.`transporter_trips_statistics` 
-- DROP INDEX `date_created_ik` ;
-- ;

UPDATE `pad`.`system_parameters` SET `invoice_statement_schedule_hour` = '11' WHERE (`id` = '1');