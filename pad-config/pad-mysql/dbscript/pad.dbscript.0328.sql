USE pad;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1487', '1', 'KEY_SCREEN_PRINT_RECEIPT_LABEL', 'Print Receipt');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1488', '2', 'KEY_SCREEN_PRINT_RECEIPT_LABEL', 'Imprimer le reçu');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1489', '1', 'KEY_SCREEN_PRINT_RECEIPT_DISABLE_MESSAGE', 'You\'ll be able to print again in the next seconds');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1490', '2', 'KEY_SCREEN_PRINT_RECEIPT_DISABLE_MESSAGE', 'Vous pourrez imprimer à nouveau dans les prochaines secondes');

INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('40', 'Receipt Print', '2020-06-30 12:00:00');

ALTER TABLE `pad`.`activity_log` 
ADD COLUMN `object_json` VARCHAR(8192) NOT NULL AFTER `vehicle_release_count`;

ALTER TABLE `pad`.`lanes` 
ADD COLUMN `printer_ip` VARCHAR(64) NOT NULL AFTER `is_active`;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1491', '1', 'KEY_SCREEN_PRINTER_IP_LABEL', 'Printer IP Address');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1492', '2', 'KEY_SCREEN_PRINTER_IP_LABEL', 'Adresse IP de l\'imprimante');

ALTER TABLE `pad`.`system_parameters` 
ADD COLUMN `printer_socket_timeout` INT NOT NULL AFTER `drop_off_empty_night_mission_end_time`,
ADD COLUMN `printer_connect_timeout` INT NOT NULL AFTER `printer_socket_timeout`,
ADD COLUMN `printer_conn_request_timeout` INT NOT NULL AFTER `printer_connect_timeout`;

UPDATE `pad`.`system_parameters` SET `printer_socket_timeout` = '90000', `printer_connect_timeout` = '90000', `printer_conn_request_timeout` = '90000' WHERE (`id` = '1');


ALTER TABLE `pad`.`payments` 
ADD COLUMN `code` VARCHAR(64) NOT NULL AFTER `id`;
