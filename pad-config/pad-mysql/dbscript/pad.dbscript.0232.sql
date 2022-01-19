USE pad;

ALTER TABLE `pad`.`port_operator_gates` 
ADD COLUMN `gate_number_short` VARCHAR(16) NOT NULL AFTER `gate_number`;

ALTER TABLE `pad`.`port_operators` 
ADD COLUMN `name_short` VARCHAR(16) NOT NULL AFTER `name`;

UPDATE `pad`.`port_operators` SET `name_short`='DPW' WHERE `id`='1';
UPDATE `pad`.`port_operators` SET `name_short`='TVS' WHERE `id`='2';
UPDATE `pad`.`port_operators` SET `name_short`='DAKAR' WHERE `id`='3';
UPDATE `pad`.`port_operators` SET `name_short`='VIVO' WHERE `id`='4';
UPDATE `pad`.`port_operators` SET `name_short`='SEN' WHERE `id`='5';
UPDATE `pad`.`port_operators` SET `name_short`='ORYX' WHERE `id`='6';
UPDATE `pad`.`port_operators` SET `name_short`='ERES' WHERE `id`='7';
UPDATE `pad`.`port_operators` SET `name_short`='TOUS' WHERE `id`='8';
UPDATE `pad`.`port_operators` SET `name_short`='TOTAL' WHERE `id`='9';

UPDATE `pad`.`port_operator_gates` SET `gate_number_short`='ML5' WHERE `id`='1001';
UPDATE `pad`.`port_operator_gates` SET `gate_number_short`='ML8' WHERE `id`='1002';
UPDATE `pad`.`port_operator_gates` SET `gate_number_short`='CT' WHERE `id`='1003';
UPDATE `pad`.`port_operator_gates` SET `gate_number_short`='ML2' WHERE `id`='1004';
UPDATE `pad`.`port_operator_gates` SET `gate_number_short`='ML9' WHERE `id`='1005';
UPDATE `pad`.`port_operator_gates` SET `gate_number_short`='JN' WHERE `id`='1006';
UPDATE `pad`.`port_operator_gates` SET `gate_number_short`='ML9' WHERE `id`='1007';
UPDATE `pad`.`port_operator_gates` SET `gate_number_short`='JN' WHERE `id`='1008';
UPDATE `pad`.`port_operator_gates` SET `gate_number_short`='ML9' WHERE `id`='1009';
UPDATE `pad`.`port_operator_gates` SET `gate_number_short`='JN' WHERE `id`='1010';
UPDATE `pad`.`port_operator_gates` SET `gate_number_short`='ML9' WHERE `id`='1011';
UPDATE `pad`.`port_operator_gates` SET `gate_number_short`='JN' WHERE `id`='1012';
UPDATE `pad`.`port_operator_gates` SET `gate_number_short`='ML1' WHERE `id`='1013';
UPDATE `pad`.`port_operator_gates` SET `gate_number_short`='ML3' WHERE `id`='1014';
UPDATE `pad`.`port_operator_gates` SET `gate_number_short`='ML4' WHERE `id`='1015';