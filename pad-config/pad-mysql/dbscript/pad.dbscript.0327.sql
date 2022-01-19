USE pad;

ALTER TABLE `pad`.`port_operator_transaction_types` 
ADD COLUMN `is_allow_multiple_entries` TINYINT(1) NOT NULL AFTER `is_direct_to_port`;

UPDATE `pad`.`port_operator_transaction_types` SET `is_allow_multiple_entries` = '1' WHERE (`id` = '1005');
UPDATE `pad`.`port_operator_transaction_types` SET `is_allow_multiple_entries` = '1' WHERE (`id` = '1009');
UPDATE `pad`.`port_operator_transaction_types` SET `is_allow_multiple_entries` = '1' WHERE (`id` = '1007');

ALTER TABLE `pad`.`port_operator_transaction_types` 
DROP COLUMN `is_allow_multiple_parking_entries`,
DROP COLUMN `is_allow_multiple_port_entries`;
