USE pad;

UPDATE `pad`.`independent_port_operators` SET `is_active` = '1' WHERE (`id` = '6');

ALTER TABLE `pad`.`port_operators` 
ADD COLUMN `is_active` TINYINT(1) NOT NULL AFTER `name_short`;

UPDATE pad.port_operators set is_active = 1 WHERE id > 0;

UPDATE `pad`.`port_operators` SET `is_active` = '0' WHERE (`id` = '10');

update pad.operators set port_operator_id = 99, independent_port_operator_id = 6 where port_operator_id = 10;
