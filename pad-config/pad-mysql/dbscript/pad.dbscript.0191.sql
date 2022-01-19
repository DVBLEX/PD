USE pad;

ALTER TABLE `pad`.`port_operator_gates` 
ADD COLUMN `anpr_zone_id` INT(11) NOT NULL AFTER `gate_number`;

ALTER TABLE `pad`.`port_operator_gates` 
ADD COLUMN `anpr_zone_name` VARCHAR(64) NOT NULL AFTER `anpr_zone_id`;

UPDATE `pad`.`port_operator_gates` SET `anpr_zone_id` = '10005', `anpr_zone_name` = 'mole2zone' WHERE (`id` = '1003');
UPDATE `pad`.`port_operator_gates` SET `anpr_zone_id` = '10005' WHERE (`id` = '1006');
UPDATE `pad`.`port_operator_gates` SET `anpr_zone_id` = '10005' WHERE (`id` = '1008');
UPDATE `pad`.`port_operator_gates` SET `anpr_zone_id` = '10005' WHERE (`id` = '1010');
UPDATE `pad`.`port_operator_gates` SET `anpr_zone_id` = '10005' WHERE (`id` = '1012');
UPDATE `pad`.`port_operator_gates` SET `anpr_zone_id` = '10005' WHERE (`id` = '1017');
UPDATE `pad`.`port_operator_gates` SET `anpr_zone_id` = '10005' WHERE (`id` = '1013');
UPDATE `pad`.`port_operator_gates` SET `anpr_zone_id` = '10005' WHERE (`id` = '1004');
UPDATE `pad`.`port_operator_gates` SET `anpr_zone_id` = '10005' WHERE (`id` = '1014');
UPDATE `pad`.`port_operator_gates` SET `anpr_zone_id` = '10005' WHERE (`id` = '1015');
UPDATE `pad`.`port_operator_gates` SET `anpr_zone_id` = '10005' WHERE (`id` = '1001');
UPDATE `pad`.`port_operator_gates` SET `anpr_zone_id` = '10005' WHERE (`id` = '1002');
UPDATE `pad`.`port_operator_gates` SET `anpr_zone_id` = '10005' WHERE (`id` = '1005');
UPDATE `pad`.`port_operator_gates` SET `anpr_zone_id` = '10005' WHERE (`id` = '1007');
UPDATE `pad`.`port_operator_gates` SET `anpr_zone_id` = '10005' WHERE (`id` = '1009');
UPDATE `pad`.`port_operator_gates` SET `anpr_zone_id` = '10005' WHERE (`id` = '1011');
UPDATE `pad`.`port_operator_gates` SET `anpr_zone_id` = '10005' WHERE (`id` = '1016');
UPDATE pad.port_operator_gates SET anpr_zone_name = 'mole2zone' WHERE id > 0;
