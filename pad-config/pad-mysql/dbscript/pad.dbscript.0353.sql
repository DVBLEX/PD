USE pad;

CREATE TABLE `port_operator_alerts` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`code` varchar(64) NOT NULL,
`port_operator_id` int(11) NOT NULL,
`transaction_type` int(11) NOT NULL,
`working_capacity` int(11) NOT NULL,
`description` varchar(256) NOT NULL,
`date_alert` datetime NOT NULL,
`date_resolution_estimate` datetime NOT NULL,
`date_created` datetime NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8;

ALTER TABLE `pad`.`port_operator_alerts` 
DROP COLUMN `date_created`;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1549', '1', 'KEY_SCREEN_ISSUE_REPORT_LABEL', 'Issue Report');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1550', '2', 'KEY_SCREEN_ISSUE_REPORT_LABEL', 'Rapport de Problème');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1551', '1', 'KEY_SCREEN_ISSUE_DESCRIPTION_LABEL', 'Issue Description');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1552', '2', 'KEY_SCREEN_ISSUE_DESCRIPTION_LABEL', 'Description du Problème');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1553', '1', 'KEY_SCREEN_CURRENT_WORKING_CAPACITY_LABEL', 'Current Working Capacity');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1554', '2', 'KEY_SCREEN_CURRENT_WORKING_CAPACITY_LABEL', 'Capacité de Travail Actuelle');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1555', '1', 'KEY_SCREEN_EST_RESOLUTION_DATE_LABEL', 'Estimated Resolution Date');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1556', '2', 'KEY_SCREEN_EST_RESOLUTION_DATE_LABEL', 'Date de Résolution Estimée');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1557', '1', 'KEY_SCREEN_EST_RESOLUTION_TIME_LABEL', 'Estimated Resolution Time');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1558', '2', 'KEY_SCREEN_EST_RESOLUTION_TIME_LABEL', 'Temps de Résolution Estimé');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1559', '1', 'KEY_SCREEN_ENTER_ISSUE_DESCRIPTION_MESSAGE', 'Please enter an Issue Description');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1560', '2', 'KEY_SCREEN_ENTER_ISSUE_DESCRIPTION_MESSAGE', 'Veuillez Saisir une Description du Problème');INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1561', '1', 'KEY_SCREEN_ENTER_CURRENT_WORKING_CAPACITY_MESSAGE', 'Please enter the Current Working Capacity');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1562', '2', 'KEY_SCREEN_ENTER_CURRENT_WORKING_CAPACITY_MESSAGE', 'Veuillez Saisir la Capacité de Travail Actuelle');UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_SELECT_CURRENT_WORKING_CAPACITY_MESSAGE', `translate_value` = 'Please select the Current Working Capacity' WHERE (`id` = '1561');
-- UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_SELECT_CURRENT_WORKING_CAPACITY_MESSAGE', `translate_value` = 'Veuillez Sélectionner la Capacité de Travail Actuelle' WHERE (`id` = '1562');INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1563', '1', 'KEY_SCREEN_SELECT_EST_RESOLUTION_DATE_MESSAGE', 'Please select the Estimated Resolution Date');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1564', '2', 'KEY_SCREEN_SELECT_EST_RESOLUTION_DATE_MESSAGE', 'Veuillez Sélectionner la Date de Résolution Estimée');INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1565', '1', 'KEY_SCREEN_SELECT_EST_RESOLUTION_TIME_MESSAGE', 'Please select the Estimated Resolution Time');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1566', '2', 'KEY_SCREEN_SELECT_EST_RESOLUTION_TIME_MESSAGE', 'Veuillez Sélectionner le Temps de Résolution Estimé');INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1567', '1', 'KEY_SCREEN_ISSUE_REPORTED_MESSAGE', 'The issue was reported successfully and AGS Office will be notified');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1568', '2', 'KEY_SCREEN_ISSUE_REPORTED_MESSAGE', 'Le problème a été signalé avec succès et le bureau AGS en sera informé');
