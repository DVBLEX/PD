USE pad;

ALTER TABLE `pad`.`port_operator_transaction_types` 
ADD COLUMN `is_direct_to_port` TINYINT(1) NOT NULL AFTER `is_auto_release_parking`;

UPDATE `pad`.`port_operator_transaction_types` SET `is_direct_to_port` = '1' WHERE (`id` = '1001');
UPDATE `pad`.`port_operator_transaction_types` SET `is_direct_to_port` = '1' WHERE (`id` = '1006');
UPDATE `pad`.`port_operator_transaction_types` SET `is_direct_to_port` = '1' WHERE (`id` = '1011');
UPDATE `pad`.`port_operator_transaction_types` SET `is_direct_to_port` = '1' WHERE (`id` = '1036');
UPDATE `pad`.`port_operator_transaction_types` SET `is_direct_to_port` = '1' WHERE (`id` = '1043');
UPDATE `pad`.`port_operator_transaction_types` SET `is_direct_to_port` = '1' WHERE (`id` = '1009');

ALTER TABLE `pad`.`port_operator_transaction_types` 
ADD COLUMN `is_allow_multiple_port_entries` TINYINT(1) NOT NULL AFTER `is_direct_to_port`;

UPDATE `pad`.`port_operator_transaction_types` SET `is_allow_multiple_port_entries` = '1' WHERE (`id` = '1009');