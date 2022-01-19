USE pad;

UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_DROP_OFF_EMPTY_DAY_LABEL', `translate_value` = 'Drop off Empty - Day' WHERE (`id` = '479');
UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_DROP_OFF_EMPTY_DAY_LABEL', `translate_value` = 'Déposer Vide - Jour' WHERE (`id` = '480');
UPDATE `pad`.`port_operator_transaction_types` SET `translate_key` = 'KEY_SCREEN_DROP_OFF_EMPTY_DAY_LABEL' WHERE (`id` = '1002');

INSERT INTO `pad`.`port_operator_transaction_types` (`id`, `port_operator_id`, `transaction_type`, `port_operator_gate_id`, `translate_key`, `translate_key_short`, `is_allowed_for_parking_and_kiosk_op`, `is_auto_release_parking`, `is_direct_to_port`, `is_allow_multiple_port_entries`, `port_transit_duration_minutes`) VALUES ('1005', '1', '8', '1005', 'KEY_SCREEN_DROP_OFF_EMPTY_NIGHT_LABEL', 'KEY_SCREEN_DROP_OFF_EMPTY_SHORT_LABEL', '0', '0', '1', '1', '0');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1445', '1', 'KEY_SCREEN_DROP_OFF_EMPTY_NIGHT_LABEL', 'Drop off Empty - Night');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1446', '2', 'KEY_SCREEN_DROP_OFF_EMPTY_NIGHT_LABEL', 'Déposer Vide - Nuit');

ALTER TABLE `pad`.`system_parameters` 
ADD COLUMN `drop_off_empty_night_mission_start_time` VARCHAR(5) NOT NULL AFTER `auto_release_exit_capacity_percentage`,
ADD COLUMN `drop_off_empty_night_mission_end_time` VARCHAR(5) NOT NULL AFTER `drop_off_empty_night_mission_start_time`;

UPDATE `pad`.`system_parameters` SET `drop_off_empty_night_mission_start_time` = '19:00', `drop_off_empty_night_mission_end_time` = '07:00' WHERE (`id` = '1');
