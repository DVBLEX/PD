USE pad;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1103', '1', 'KEY_SCREEN_ENTERED_PARKING_TODAY_LABEL', 'Entered Parking Today');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1104', '2', 'KEY_SCREEN_ENTERED_PARKING_TODAY_LABEL', 'Parking Entré Aujourd\'hui');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1105', '1', 'KEY_SCREEN_ENTERED_PARKING_THIS_MONTH_LABEL', 'Entered Parking This Month');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1106', '2', 'KEY_SCREEN_ENTERED_PARKING_THIS_MONTH_LABEL', 'Parking Entré ce Mois-ci');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1107', '1', 'KEY_SCREEN_ENTERED_PARKING_THIS_YEAR_LABEL', 'Entered Parking This Year');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1108', '2', 'KEY_SCREEN_ENTERED_PARKING_THIS_YEAR_LABEL', 'Parking Entré Cette Année');

CREATE TABLE pad.`port_statistics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `port_operator_id` int(11) NOT NULL,
  `port_operator_gate_id` int(11) NOT NULL,
  `port_operator_name` varchar(64) NOT NULL,
  `port_operator_gate_number` varchar(64) NOT NULL,
  `transaction_type` int(11) NOT NULL,
  `count_entry` int(11) NOT NULL,
  `amount_total_trip_fee` decimal(7,0) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8;

UPDATE `pad`.`language_keys` SET `translate_key`='KEY_SCREEN_PORT_ENTRY_OPERATOR_COUNTS_LABEL', `translate_value`='Port Entry Counts - Port Operator' WHERE `id`='1069';
UPDATE `pad`.`language_keys` SET `translate_key`='KEY_SCREEN_PORT_ENTRY_OPERATOR_COUNTS_LABEL', `translate_value`='Nombre d\'entrées de port - Opérateur de port' WHERE `id`='1070';

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1109', '1', 'KEY_SCREEN_PORT_ENTRY_GATE_COUNTS_LABEL', 'Port Entry Counts - Gate Number');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1110', '2', 'KEY_SCREEN_PORT_ENTRY_GATE_COUNTS_LABEL', 'Nombre d\'entrées de port - Numéro de porte');
