USE pad;

ALTER TABLE `pad`.`system_parameters`
    ADD COLUMN `is_port_entry_filtering` TINYINT(1) NOT NULL AFTER `is_booking_limit_check_enabled`;

UPDATE `pad`.`system_parameters` SET `is_port_entry_filtering` = '1' WHERE (`id` = '1');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1863', '1', 'KEY_SCREEN_PORT_ENTRY_FILTERING_LABEL', 'Port Entry Filtering');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1864', '2', 'KEY_SCREEN_PORT_ENTRY_FILTERING_LABEL', 'Filtrage d\'entrée de port');
