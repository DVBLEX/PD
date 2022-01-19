USE pad;

ALTER TABLE `pad`.`trips` 
ADD COLUMN `port_operator_id` INT(11) NOT NULL AFTER `driver_id`,
ADD COLUMN `port_operator_gate_id` INT(11) NOT NULL AFTER `port_operator_id`;

ALTER TABLE `pad`.`parking` 
ADD COLUMN `port_operator_id` INT(11) NOT NULL AFTER `driver_id`,
ADD COLUMN `port_operator_gate_id` INT(11) NOT NULL AFTER `port_operator_id`;

ALTER TABLE `pad`.`port_access` 
ADD COLUMN `port_operator_id` INT(11) NOT NULL AFTER `driver_id`,
ADD COLUMN `port_operator_gate_id` INT(11) NOT NULL AFTER `port_operator_id`;

UPDATE pad.trips t
INNER JOIN pad.missions m ON m.id = t.mission_id
SET t.port_operator_id = m.port_operator_id, t.port_operator_gate_id = m.port_operator_gate_id;

UPDATE pad.parking p
INNER JOIN pad.missions m ON m.id = p.mission_id
SET p.port_operator_id = m.port_operator_id, p.port_operator_gate_id = m.port_operator_gate_id;

UPDATE pad.port_access p
INNER JOIN pad.missions m ON m.id = p.mission_id
SET p.port_operator_id = m.port_operator_id, p.port_operator_gate_id = m.port_operator_gate_id;