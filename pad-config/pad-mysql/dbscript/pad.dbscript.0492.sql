USE pad;

ALTER TABLE `pad`.`port_access`
    ADD COLUMN `operator_gate` VARCHAR(32) NOT NULL AFTER `operator_id_entry`;

INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('57', 'Select Port Entry Zone', '2021-03-08 12:00:00');
