USE pad;

ALTER TABLE `pad`.`port_operator_transaction_types` 
ADD COLUMN `is_allow_multiple_parking_entries` TINYINT(1) NOT NULL AFTER `is_allow_multiple_port_entries`;

UPDATE `pad`.`port_operator_transaction_types` SET `is_allow_multiple_parking_entries` = '1' WHERE (`id` = '1007');
