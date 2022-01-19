USE pad;

ALTER TABLE `pad`.`system_parameters` 
ADD COLUMN `drop_off_empty_triangle_mission_start_time` VARCHAR(5) NOT NULL AFTER `drop_off_empty_night_mission_end_time`,
ADD COLUMN `drop_off_empty_triangle_mission_end_time` VARCHAR(5) NOT NULL AFTER `drop_off_empty_triangle_mission_start_time`;

UPDATE pad.system_parameters SET drop_off_empty_triangle_mission_start_time = drop_off_empty_night_mission_start_time, drop_off_empty_triangle_mission_end_time = drop_off_empty_night_mission_end_time WHERE id = 1;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1727', '1', 'KEY_SCREEN_DROP_OFF_EMPTY_TRIANGLE_START_TIME_LABEL', 'D.O.E.T Start Time');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1728', '2', 'KEY_SCREEN_DROP_OFF_EMPTY_TRIANGLE_START_TIME_LABEL', 'D.V.T Heure de Début');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1729', '1', 'KEY_SCREEN_DROP_OFF_EMPTY_TRIANGLE_END_TIME_LABEL', 'D.O.E.T End Time');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1730', '2', 'KEY_SCREEN_DROP_OFF_EMPTY_TRIANGLE_END_TIME_LABEL', 'D.V.T Heure de Fin');
