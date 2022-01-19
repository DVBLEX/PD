USE pad;
INSERT INTO `pad`.`system_timer_tasks` (`id`, `name`, `date_last_run`, `type`, `period`, `application`) VALUES ('108', 'anprParkingPermissionTimerTask', '2019-09-16 16:04:14', 'continuous', '300000', 'pad');
UPDATE `pad`.`system_timer_tasks` SET `name` = 'anprParkingPermissionTaskExecutor' WHERE (`id` = '108');

ALTER TABLE `pad`.`anpr_parameters` 
ADD COLUMN `parking_permission_hours_in_future` INT(11) NOT NULL AFTER `anpr_zone_id_mole8`;
UPDATE `pad`.`anpr_parameters` SET `parking_permission_hours_in_future` = '4' WHERE (`id` = '1');

ALTER TABLE `pad`.`anpr_parameters` 
ADD COLUMN `parking_permission_hours_prior_slot_date` INT(11) NOT NULL AFTER `parking_permission_hours_in_future`,
ADD COLUMN `parking_permission_hours_after_slot_date` INT(11) NOT NULL AFTER `parking_permission_hours_prior_slot_date`,
ADD COLUMN `parking_permission_hours_after_exit_date` INT(11) NOT NULL AFTER `parking_permission_hours_after_slot_date`;

UPDATE `pad`.`anpr_parameters` SET `parking_permission_hours_prior_slot_date` = '2', `parking_permission_hours_after_slot_date` = '2', `parking_permission_hours_after_exit_date` = '2' WHERE (`id` = '1');

ALTER TABLE `pad`.`trips` 
ADD COLUMN `parking_permission_id` INT(11) NOT NULL AFTER `port_operator_gate_id`;

UPDATE pad.trips SET parking_permission_id = -1 WHERE id > 0;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1127', '1', 'KEY_SCREEN_PARKING_PERMISSION_HOURS_INFUTURE_LABEL', 'Parking permission hours in future');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1128', '2', 'KEY_SCREEN_PARKING_PERMISSION_HOURS_INFUTURE_LABEL', 'Permis de stationnement à l\'avenir');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1129', '1', 'KEY_SCREEN_PARKING_PERMISSION_HOURS_PRIOR_SLOTDATE_LABEL', 'Parking permission hours prior slot date');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1130', '2', 'KEY_SCREEN_PARKING_PERMISSION_HOURS_PRIOR_SLOTDATE_LABEL', 'Heures de permission de stationnement avant la date');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1131', '1', 'KEY_SCREEN_PARKING_PERMISSION_HOURS_AFTER_SLOTDATE_LABEL', 'Parking permission hours after slot date');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1132', '2', 'KEY_SCREEN_PARKING_PERMISSION_HOURS_AFTER_SLOTDATE_LABEL', 'Heures de permission de stationnement après la date du créneau');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1133', '1', 'KEY_SCREEN_PARKING_PERMISSION_HOURS_AFTER_EXITDATE_LABEL', 'Parking permission hours after exit date');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1134', '2', 'KEY_SCREEN_PARKING_PERMISSION_HOURS_AFTER_EXITDATE_LABEL', 'Heures de permission de stationnement après la date de sortie');

