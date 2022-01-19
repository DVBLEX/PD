USE pad;

CREATE TABLE pad.`port_operator_gates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gate_id` int(11) NOT NULL,
  `port_operator_id` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8;

INSERT INTO `pad`.`port_operator_gates` (`gate_id`, `port_operator_id`, `date_created`) VALUES ('5', '2', '2019-07-30 15:34:00');
INSERT INTO `pad`.`port_operator_gates` (`gate_id`, `port_operator_id`, `date_created`) VALUES ('8', '2', '2019-07-30 15:34:00');

ALTER TABLE `pad`.`port_operator_gates`
CHANGE COLUMN `gate_id` `gate_number` VARCHAR(16) NOT NULL ;

INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `port_operator_id`, `date_created`) VALUES ('1003', 'Môle 5', '1', '2019-07-30 15:34:00');

UPDATE `pad`.`port_operator_gates` SET `gate_number`='Môle 8' WHERE `id`='1002';
UPDATE `pad`.`port_operator_gates` SET `gate_number`='Môle 5' WHERE `id`='1001';

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1023', '1', 'KEY_SCREEN_PORT_OPERATOR_GATE_LABEL', 'Gate');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1024', '2', 'KEY_SCREEN_PORT_OPERATOR_GATE_LABEL', 'Porte');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1025', '1', 'KEY_SCREEN_SELECT_PORT_OPERATOR_GATE_MESSAGE', 'Please select Gate');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1026', '2', 'KEY_SCREEN_SELECT_PORT_OPERATOR_GATE_MESSAGE', 'S\'il vous plaît sélectionnez la porte');

ALTER TABLE `pad`.`missions`
ADD COLUMN `port_operator_gate_id` INT(11) NOT NULL AFTER `port_operator_id`;

ALTER TABLE `pad`.`port_access_whitelist`
ADD COLUMN `port_operator_gate_id` INT(11) NOT NULL AFTER `port_operator_id`;
