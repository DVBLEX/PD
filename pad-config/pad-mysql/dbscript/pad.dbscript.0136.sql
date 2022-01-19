USE pad;

ALTER TABLE `pad`.`trips_port_operator` 
ADD UNIQUE INDEX `port_operator_id_UNIQUE` (`port_operator_id` ASC),
ADD UNIQUE INDEX `reference_number_UNIQUE` (`reference_number` ASC);

CREATE TABLE `pad`.`port_access_whitelist` (
  `id` INT(11) NOT NULL,
  `port_operator_id` INT(11) NOT NULL,
  `date_from` DATETIME NOT NULL,
  `date_to` DATETIME NOT NULL,
  `vehicle_registration` VARCHAR(32) NOT NULL,
  `operator_id` INT(11) NOT NULL,
  `date_created` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('941', '1', 'KEY_NAVBAR_NIGHT_RUN', 'Night Run');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('942', '2', 'KEY_NAVBAR_NIGHT_RUN', 'Course de nuit');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('955', '1', 'KEY_SCREEN_DATE_TO_2_LABEL', 'Date To');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('956', '2', 'KEY_SCREEN_DATE_TO_2_LABEL', 'Date à');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('957', '1', 'KEY_SCREEN_NO_DATA_RETURNED_MESSAGE', 'No data returned');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('958', '2', 'KEY_SCREEN_NO_DATA_RETURNED_MESSAGE', 'Aucune donnée retournée');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('959', '1', 'KEY_SCREEN_CREATE_NIGHT_RUN_LABEL', 'Create Night Run');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('960', '2', 'KEY_SCREEN_CREATE_NIGHT_RUN_LABEL', 'Créer une course de nuit');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('963', '1', 'KEY_SCREEN_TIME_FROM_LABEL', 'Time From');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('964', '2', 'KEY_SCREEN_TIME_FROM_LABEL', 'Le temps de');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('965', '1', 'KEY_SCREEN_TIME_TO_LABEL', 'Time To');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('966', '2', 'KEY_SCREEN_TIME_TO_LABEL', 'Temps de');

