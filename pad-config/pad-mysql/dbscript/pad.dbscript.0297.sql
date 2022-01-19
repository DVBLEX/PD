USE pad;

ALTER TABLE `pad`.`port_operator_transaction_types` 
ADD COLUMN `port_transit_duration_minutes` INT(11) NOT NULL AFTER `is_auto_release_parking`;

UPDATE `pad`.`port_operator_transaction_types` SET `port_transit_duration_minutes` = '10' WHERE (`id` = '1003');
