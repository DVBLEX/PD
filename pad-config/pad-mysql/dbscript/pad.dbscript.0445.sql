USE pad;

ALTER TABLE `pad`.`email_log` 
ADD COLUMN `attachment_path` VARCHAR(256) NOT NULL AFTER `channel`;

ALTER TABLE `pad`.`email_scheduler` 
ADD COLUMN `attachment_path` VARCHAR(256) NOT NULL AFTER `channel`;


ALTER TABLE `pad`.`invoice` 
CHANGE COLUMN `date_due` `date_due` DATE NULL ;

ALTER TABLE `pad`.`system_parameters` 
ADD COLUMN `invoice_statement_schedule_hour` INT NOT NULL AFTER `printer_conn_request_timeout`;

UPDATE `pad`.`system_parameters` SET `invoice_statement_schedule_hour` = '9' WHERE (`id` = '1');
