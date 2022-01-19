USE pad;

CREATE TABLE pad.`parking_statistics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `port_operator_id` int(11) NOT NULL,
  `port_operator_gate_id` int(11) NOT NULL,
  `count_entry` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8;

ALTER TABLE `pad`.`parking_statistics`
ADD COLUMN `transaction_type` INT(11) NOT NULL AFTER `port_operator_gate_id`,
ADD COLUMN `amount_total_trip_fee` DECIMAL(7,0) NOT NULL AFTER `count_entry`;

INSERT INTO `pad`.`system_timer_tasks` (`id`, `name`, `date_last_run`, `type`, `period`, `application`) VALUES ('104', 'dailyStatisticsTaskExecutor', '2019-08-15 15:37:06', 'cron', '0 2 0 * * *', 'pad');

CREATE TABLE pad.`port_operator_transaction_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `port_operator_id` int(11) NOT NULL,
  `transaction_type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8;

INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('1', '1');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('1', '2');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('1', '3');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('1', '4');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('1', '7');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('2', '1');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('2', '3');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('2', '5');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('2', '6');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('2', '7');

INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('3', '1');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('3', '3');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('3', '5');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('3', '6');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('3', '7');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('4', '1');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('4', '3');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('4', '5');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('4', '6');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('4', '7');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('5', '1');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('5', '3');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('5', '5');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('5', '6');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('5', '7');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('6', '1');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('6', '3');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('6', '5');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('6', '6');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('6', '7');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('7', '1');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('7', '3');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('7', '5');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('7', '6');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('7', '7');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('8', '1');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('8', '3');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('8', '5');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('8', '6');
INSERT INTO `pad`.`port_operator_transaction_types` (`port_operator_id`, `transaction_type`) VALUES ('8', '7');

ALTER TABLE `pad`.`parking_statistics`
ADD COLUMN `port_operator_name` VARCHAR(64) NOT NULL AFTER `port_operator_gate_id`,
ADD COLUMN `port_operator_gate_number` VARCHAR(64) NOT NULL AFTER `port_operator_name`;

UPDATE pad.sms_templates SET source_addr = 'AGS' WHERE id > 0;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1067', '1', 'KEY_REPORT_TYPE_LABEL', 'Report Type');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1068', '2', 'KEY_REPORT_TYPE_LABEL', 'Type de Rapport');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1069', '1', 'KEY_SCREEN_PORT_ENTRY_COUNTS_LABEL', 'Port Entry Counts');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1070', '2', 'KEY_SCREEN_PORT_ENTRY_COUNTS_LABEL', 'Nombre d\'entrées au port');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1071', '1', 'KEY_MONTH_LABEL', 'Month');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1072', '2', 'KEY_MONTH_LABEL', 'Mois');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1073', '1', 'KEY_SCREEN_JANUARY_LABEL', 'January');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1074', '2', 'KEY_SCREEN_JANUARY_LABEL', 'Janvier');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1075', '1', 'KEY_SCREEN_FEBRUARY_LABEL', 'February');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1076', '2', 'KEY_SCREEN_FEBRUARY_LABEL', 'Février');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1077', '1', 'KEY_SCREEN_MARCH_LABEL', 'March');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1078', '2', 'KEY_SCREEN_MARCH_LABEL', 'Mars');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1079', '1', 'KEY_SCREEN_APRIL_LABEL', 'April');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1080', '2', 'KEY_SCREEN_APRIL_LABEL', 'Avril');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1081', '1', 'KEY_SCREEN_MAY_LABEL', 'May');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1082', '2', 'KEY_SCREEN_MAY_LABEL', 'Mai');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1083', '1', 'KEY_SCREEN_JUNE_LABEL', 'June');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1084', '2', 'KEY_SCREEN_JUNE_LABEL', 'Juin');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1085', '1', 'KEY_SCREEN_JULY_LABEL', 'July');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1086', '2', 'KEY_SCREEN_JULY_LABEL', 'Juillet');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1087', '1', 'KEY_SCREEN_AUGUST_LABEL', 'August');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1088', '2', 'KEY_SCREEN_AUGUST_LABEL', 'Août');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1089', '1', 'KEY_SCREEN_SEPTEMBER_LABEL', 'September');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1090', '2', 'KEY_SCREEN_SEPTEMBER_LABEL', 'Septembre');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1091', '1', 'KEY_SCREEN_OCTOBER_LABEL', 'October');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1092', '2', 'KEY_SCREEN_OCTOBER_LABEL', 'Octobre');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1093', '1', 'KEY_SCREEN_NOVEMBER_LABEL', 'November');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1094', '2', 'KEY_SCREEN_NOVEMBER_LABEL', 'Novembre');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1095', '1', 'KEY_SCREEN_DECEMBER_LABEL', 'December');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1096', '2', 'KEY_SCREEN_DECEMBER_LABEL', 'Décembre');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1097', '1', 'KEY_SCREEN_YEAR_LABEL', 'Year');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1098', '2', 'KEY_SCREEN_YEAR_LABEL', 'Année');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1099', '1', 'KEY_SCREEN_GRAND_TOTAL_LABEL', 'Grand Total');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1100', '2', 'KEY_SCREEN_GRAND_TOTAL_LABEL', 'Somme Finale');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1101', '1', 'KEY_SCREEN_NO_REPORT_DATA_AVAILABLE_MESSAGE', 'No report data available for the selected month and year');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1102', '2', 'KEY_SCREEN_NO_REPORT_DATA_AVAILABLE_MESSAGE', 'Aucune donnée de rapport disponible pour le mois et l\'année sélectionnés');
