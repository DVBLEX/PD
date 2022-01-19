USE pad;

UPDATE `pad`.`independent_port_operators` SET `name_short` = 'ACC' WHERE (`id` = '1');
UPDATE `pad`.`independent_port_operators` SET `name_short` = 'CST' WHERE (`id` = '2');
UPDATE `pad`.`independent_port_operators` SET `name_short` = 'BOL' WHERE (`id` = '3');
UPDATE `pad`.`independent_port_operators` SET `name_short` = 'DMS' WHERE (`id` = '4');
UPDATE `pad`.`independent_port_operators` SET `name_short` = 'GMT' WHERE (`id` = '5');
UPDATE `pad`.`independent_port_operators` SET `name_short` = 'IST' WHERE (`id` = '6');
UPDATE `pad`.`independent_port_operators` SET `name_short` = 'INT' WHERE (`id` = '7');
UPDATE `pad`.`independent_port_operators` SET `name_short` = 'ICS' WHERE (`id` = '8');
UPDATE `pad`.`independent_port_operators` SET `name_short` = 'ITS' WHERE (`id` = '9');
UPDATE `pad`.`independent_port_operators` SET `name_short` = 'MAR' WHERE (`id` = '10');
UPDATE `pad`.`independent_port_operators` SET `name_short` = 'MLT' WHERE (`id` = '11');
UPDATE `pad`.`independent_port_operators` SET `name_short` = 'SAF' WHERE (`id` = '12');
UPDATE `pad`.`independent_port_operators` SET `name_short` = 'SIM' WHERE (`id` = '13');
UPDATE `pad`.`independent_port_operators` SET `name_short` = 'SNT' WHERE (`id` = '14');
UPDATE `pad`.`independent_port_operators` SET `name_short` = 'SNA' WHERE (`id` = '15');
UPDATE `pad`.`independent_port_operators` SET `name_short` = 'SOM' WHERE (`id` = '16');
UPDATE `pad`.`independent_port_operators` SET `name_short` = 'TIM' WHERE (`id` = '17');
UPDATE `pad`.`independent_port_operators` SET `name_short` = 'TCR' WHERE (`id` = '18');
UPDATE `pad`.`independent_port_operators` SET `name_short` = 'TEX' WHERE (`id` = '19');
UPDATE `pad`.`independent_port_operators` SET `name_short` = 'RLO' WHERE (`id` = '20');

UPDATE `pad`.`language_keys` SET `translate_value` = 'Stevedore' WHERE (`id` = '1709');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Stevedore' WHERE (`id` = '1710');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Please enter a valid Stevedore' WHERE (`id` = '1711');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Veuillez entrer un Stevedore valide' WHERE (`id` = '1712');

ALTER TABLE `pad`.`independent_port_operators` 
ADD COLUMN `is_active` TINYINT(1) NOT NULL AFTER `name_short`;

UPDATE pad.independent_port_operators SET is_active = 1 WHERE id > 0;

ALTER TABLE `pad`.`trips`
ADD COLUMN `independent_port_operator_id` INT(11) NOT NULL AFTER `port_operator_id`;

UPDATE pad.trips SET independent_port_operator_id = -1 WHERE id > 0;

ALTER TABLE `pad`.`trips`
ADD INDEX `independent_port_operator_id` (`independent_port_operator_id` ASC);
