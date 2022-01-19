USE pad;

ALTER TABLE `pad`.`port_operator_transaction_types` 
ADD COLUMN `port_operator_gate_id` INT(11) NOT NULL AFTER `transaction_type`;

ALTER TABLE `pad`.`port_operator_gates` 
DROP COLUMN `port_operator_id`;

DELETE FROM pad.port_operator_gates WHERE id > 0;
ALTER TABLE pad.port_operator_gates AUTO_INCREMENT = 1001;

INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `gate_number_short`, `anpr_zone_id`, `anpr_zone_name`, `date_created`) VALUES ('1001', 'Môle 1', 'ML1', '10002', 'mole2zone', '2020-04-09 15:55:12');
INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `gate_number_short`, `anpr_zone_id`, `anpr_zone_name`, `date_created`) VALUES ('1002', 'Môle 2', 'ML2', '10002', 'mole2zone', '2020-04-09 15:55:12');
INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `gate_number_short`, `anpr_zone_id`, `anpr_zone_name`, `date_created`) VALUES ('1003', 'Môle 3', 'ML3', '10002', 'mole2zone', '2020-04-09 15:55:12');
INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `gate_number_short`, `anpr_zone_id`, `anpr_zone_name`, `date_created`) VALUES ('1004', 'Môle 4', 'ML4', '10002', 'mole2zone', '2020-04-09 15:55:12');
INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `gate_number_short`, `anpr_zone_id`, `anpr_zone_name`, `date_created`) VALUES ('1005', 'Môle 8', 'ML8', '10002', 'mole2zone', '2020-04-09 15:55:12');
INSERT INTO `pad`.`port_operator_gates` (`id`, `gate_number`, `gate_number_short`, `anpr_zone_id`, `anpr_zone_name`, `date_created`) VALUES ('1006', 'Môle 9', 'ML9', '10002', 'mole2zone', '2020-04-09 15:55:12');

-- fix mapping for existing gate ids to the new ids after updating the current config for gates table
UPDATE `pad`.`port_operator_transaction_types` SET `port_operator_gate_id` = '1004' WHERE (`id` = '1001');
UPDATE `pad`.`port_operator_transaction_types` SET `port_operator_gate_id` = '1005' WHERE (`id` = '1002');
UPDATE `pad`.`port_operator_transaction_types` SET `port_operator_gate_id` = '1004' WHERE (`id` = '1003');
UPDATE `pad`.`port_operator_transaction_types` SET `port_operator_gate_id` = '1005' WHERE (`id` = '1006');
UPDATE `pad`.`port_operator_transaction_types` SET `port_operator_gate_id` = '1005' WHERE (`id` = '1007');
UPDATE `pad`.`port_operator_transaction_types` SET `port_operator_gate_id` = '1005' WHERE (`id` = '1009');
UPDATE `pad`.`port_operator_transaction_types` SET `port_operator_gate_id` = '1003' WHERE (`id` = '1011');
UPDATE `pad`.`port_operator_transaction_types` SET `port_operator_gate_id` = '1003' WHERE (`id` = '1012');
UPDATE `pad`.`port_operator_transaction_types` SET `port_operator_gate_id` = '1005' WHERE (`id` = '1017');
UPDATE `pad`.`port_operator_transaction_types` SET `port_operator_gate_id` = '1005' WHERE (`id` = '1022');
UPDATE `pad`.`port_operator_transaction_types` SET `port_operator_gate_id` = '1005' WHERE (`id` = '1027');
UPDATE `pad`.`port_operator_transaction_types` SET `port_operator_gate_id` = '1005' WHERE (`id` = '1032');
UPDATE `pad`.`port_operator_transaction_types` SET `port_operator_gate_id` = '1003' WHERE (`id` = '1036');
UPDATE `pad`.`port_operator_transaction_types` SET `port_operator_gate_id` = '1004' WHERE (`id` = '1037');
UPDATE `pad`.`port_operator_transaction_types` SET `port_operator_gate_id` = '1003' WHERE (`id` = '1043');
UPDATE `pad`.`port_operator_transaction_types` SET `port_operator_gate_id` = '1004' WHERE (`id` = '1044');

UPDATE pad.missions SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 1 AND transaction_type = 1) WHERE port_operator_id = 1 AND transaction_type = 1;
UPDATE pad.missions SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 1 AND transaction_type = 2) WHERE port_operator_id = 1 AND transaction_type = 2;
UPDATE pad.missions SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 1 AND transaction_type = 3) WHERE port_operator_id = 1 AND transaction_type = 3;
UPDATE pad.missions SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 1 AND transaction_type = 4) WHERE port_operator_id = 1 AND transaction_type = 4;
UPDATE pad.missions SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 2 AND transaction_type = 1) WHERE port_operator_id = 2 AND transaction_type = 1;

UPDATE pad.missions SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types 
WHERE port_operator_id = 2 AND transaction_type = 3) WHERE port_operator_id = 2 AND transaction_type = 3;

UPDATE pad.missions SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types 
WHERE port_operator_id = 2 AND transaction_type = 6) WHERE port_operator_id = 2 AND transaction_type = 6;
UPDATE pad.missions SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types 
WHERE port_operator_id = 3 AND transaction_type = 1) WHERE port_operator_id = 3 AND transaction_type = 1;

UPDATE pad.missions SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types 
WHERE port_operator_id = 3 AND transaction_type = 3) WHERE port_operator_id = 3 AND transaction_type = 3;
UPDATE pad.missions SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types 
WHERE port_operator_id = 4 AND transaction_type = 3) WHERE port_operator_id = 4 AND transaction_type = 3;
UPDATE pad.missions SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types 
WHERE port_operator_id = 5 AND transaction_type = 3) WHERE port_operator_id = 5 AND transaction_type = 3;

UPDATE pad.missions SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types 
WHERE port_operator_id = 6 AND transaction_type = 3) WHERE port_operator_id = 6 AND transaction_type = 3;

UPDATE pad.missions SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types 
WHERE port_operator_id = 7 AND transaction_type = 3) WHERE port_operator_id = 7 AND transaction_type = 3;
UPDATE pad.missions SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types 
WHERE port_operator_id = 8 AND transaction_type = 1) WHERE port_operator_id = 8 AND transaction_type = 1;

UPDATE pad.missions SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types 
WHERE port_operator_id = 8 AND transaction_type = 3) WHERE port_operator_id = 8 AND transaction_type = 3;
UPDATE pad.missions SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types 
WHERE port_operator_id = 10 AND transaction_type = 1) WHERE port_operator_id = 10 AND transaction_type = 1;

UPDATE pad.missions SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types 
WHERE port_operator_id = 10 AND transaction_type = 3) WHERE port_operator_id = 10 AND transaction_type = 3;
UPDATE pad.parking p
INNER JOIN pad.missions m ON p.mission_id = m.id
SET p.port_operator_gate_id = m.port_operator_gate_id WHERE p.id > 0;

UPDATE pad.parking_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 1 AND transaction_type = 1) 
WHERE port_operator_id = 1 AND transaction_type = 1 AND id > 0;

UPDATE pad.parking_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 1 AND transaction_type = 2) 
WHERE port_operator_id = 1 AND transaction_type = 2 AND id > 0;

UPDATE pad.parking_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 1 AND transaction_type = 3) 
WHERE port_operator_id = 1 AND transaction_type = 3 AND id > 0;

UPDATE pad.parking_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 1 AND transaction_type = 4) 
WHERE port_operator_id = 1 AND transaction_type = 4 AND id > 0;

UPDATE pad.parking_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 2 AND transaction_type = 1) 
WHERE port_operator_id = 2 AND transaction_type = 1 AND id > 0;

UPDATE pad.parking_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 2 AND transaction_type = 3) 
WHERE port_operator_id = 2 AND transaction_type = 3 AND id > 0;

UPDATE pad.parking_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 2 AND transaction_type = 6) 
WHERE port_operator_id = 2 AND transaction_type = 6 AND id > 0;

UPDATE pad.parking_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 3 AND transaction_type = 1) 
WHERE port_operator_id = 3 AND transaction_type = 1 AND id > 0;

UPDATE pad.parking_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 3 AND transaction_type = 3) 
WHERE port_operator_id = 3 AND transaction_type = 3 AND id > 0;

UPDATE pad.parking_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 4 AND transaction_type = 3) 
WHERE port_operator_id = 4 AND transaction_type = 3 AND id > 0;

UPDATE pad.parking_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 5 AND transaction_type = 3) 
WHERE port_operator_id = 5 AND transaction_type = 3 AND id > 0;

UPDATE pad.parking_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 6 AND transaction_type = 3) 
WHERE port_operator_id = 6 AND transaction_type = 3 AND id > 0;

UPDATE pad.parking_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 7 AND transaction_type = 3) 
WHERE port_operator_id = 7 AND transaction_type = 3 AND id > 0;

UPDATE pad.parking_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 8 AND transaction_type = 1) 
WHERE port_operator_id = 8 AND transaction_type = 1 AND id > 0;

UPDATE pad.parking_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 8 AND transaction_type = 3) 
WHERE port_operator_id = 8 AND transaction_type = 3 AND id > 0;

UPDATE pad.parking_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 10 AND transaction_type = 1) 
WHERE port_operator_id = 10 AND transaction_type = 1 AND id > 0;

UPDATE pad.parking_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 10 AND transaction_type = 3) 
WHERE port_operator_id = 10 AND transaction_type = 3 AND id > 0;

UPDATE pad.port_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 1 AND transaction_type = 1) 
WHERE port_operator_id = 1 AND transaction_type = 1 AND id > 0;

UPDATE pad.port_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 1 AND transaction_type = 2) 
WHERE port_operator_id = 1 AND transaction_type = 2 AND id > 0;

UPDATE pad.port_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 1 AND transaction_type = 3) 
WHERE port_operator_id = 1 AND transaction_type = 3 AND id > 0;

UPDATE pad.port_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 1 AND transaction_type = 4) 
WHERE port_operator_id = 1 AND transaction_type = 4 AND id > 0;

UPDATE pad.port_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 2 AND transaction_type = 1) 
WHERE port_operator_id = 2 AND transaction_type = 1 AND id > 0;

UPDATE pad.port_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 2 AND transaction_type = 3) 
WHERE port_operator_id = 2 AND transaction_type = 3 AND id > 0;

UPDATE pad.port_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 2 AND transaction_type = 6) 
WHERE port_operator_id = 2 AND transaction_type = 6 AND id > 0;

UPDATE pad.port_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 3 AND transaction_type = 1) 
WHERE port_operator_id = 3 AND transaction_type = 1 AND id > 0;

UPDATE pad.port_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 3 AND transaction_type = 3) 
WHERE port_operator_id = 3 AND transaction_type = 3 AND id > 0;

UPDATE pad.port_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 4 AND transaction_type = 3) 
WHERE port_operator_id = 4 AND transaction_type = 3 AND id > 0;

UPDATE pad.port_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 5 AND transaction_type = 3) 
WHERE port_operator_id = 5 AND transaction_type = 3 AND id > 0;

UPDATE pad.port_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 6 AND transaction_type = 3) 
WHERE port_operator_id = 6 AND transaction_type = 3 AND id > 0;

UPDATE pad.port_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 7 AND transaction_type = 3) 
WHERE port_operator_id = 7 AND transaction_type = 3 AND id > 0;

UPDATE pad.port_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 8 AND transaction_type = 1) 
WHERE port_operator_id = 8 AND transaction_type = 1 AND id > 0;

UPDATE pad.port_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 8 AND transaction_type = 3) 
WHERE port_operator_id = 8 AND transaction_type = 3 AND id > 0;

UPDATE pad.port_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 10 AND transaction_type = 1) 
WHERE port_operator_id = 10 AND transaction_type = 1 AND id > 0;

UPDATE pad.port_statistics SET port_operator_gate_id = (SELECT port_operator_gate_id FROM pad.port_operator_transaction_types WHERE port_operator_id = 10 AND transaction_type = 3) 
WHERE port_operator_id = 10 AND transaction_type = 3 AND id > 0;

UPDATE pad.port_statistics p
INNER JOIN pad.port_operator_gates pog ON p.port_operator_gate_id = pog.id
SET p.port_operator_gate_number = pog.gate_number WHERE p.id > 0;

UPDATE pad.trips t
INNER JOIN pad.missions m ON t.mission_id = m.id
SET t.port_operator_gate_id = m.port_operator_gate_id WHERE t.id > 0;

UPDATE pad.port_access p
INNER JOIN pad.missions m ON p.mission_id = m.id
SET p.port_operator_gate_id = m.port_operator_gate_id WHERE p.id > 0;
