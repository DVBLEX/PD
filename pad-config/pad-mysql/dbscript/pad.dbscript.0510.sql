USE pad;

ALTER TABLE `pad`.`port_operator_transaction_types`
    ADD COLUMN `is_trip_cancel_system` TINYINT(1) NOT NULL AFTER `mission_cancel_system_after_minutes`,
    ADD COLUMN `trip_cancel_system_after_minutes` INT NOT NULL AFTER `is_trip_cancel_system`;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1867', '1', 'KEY_SCREEN_TRIP_CANCEL_SYSTEM_AFTER_MINUTES_LABEL', 'Trip Cancel (minutes)');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1868', '2', 'KEY_SCREEN_TRIP_CANCEL_SYSTEM_AFTER_MINUTES_LABEL', 'Annulation de voyage (minutes)');
