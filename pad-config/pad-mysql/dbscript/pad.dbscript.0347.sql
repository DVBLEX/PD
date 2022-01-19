USE pad;

ALTER TABLE `pad`.`port_operator_transaction_types` 
ADD COLUMN `mission_cancel_system_after_minutes` INT(11) NOT NULL AFTER `port_transit_duration_minutes`;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1541', '1', 'KEY_SCREEN_CANCELLED_BY_SYSTEM_LABEL', 'Cancelled - System');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1542', '2', 'KEY_SCREEN_CANCELLED_BY_SYSTEM_LABEL', 'Annulé - Système');

UPDATE pad.port_operator_transaction_types SET mission_cancel_system_after_minutes = 10080 where is_allow_multiple_entries = 0 and id > 0;
