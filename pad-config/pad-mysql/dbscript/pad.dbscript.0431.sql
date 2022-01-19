USE pad;

UPDATE `pad`.`port_operator_transaction_types` SET `is_allowed_for_virtual_kiosk_op` = '0' WHERE (`id` = '1002');
UPDATE `pad`.`port_operator_transaction_types` SET `is_allowed_for_virtual_kiosk_op` = '0' WHERE (`id` = '1045');
UPDATE `pad`.`port_operator_transaction_types` SET `is_allowed_for_virtual_kiosk_op` = '0' WHERE (`id` = '1046');
UPDATE `pad`.`port_operator_transaction_types` SET `is_allowed_for_virtual_kiosk_op` = '0' WHERE (`id` = '1009');
UPDATE `pad`.`port_operator_transaction_types` SET `is_allowed_for_virtual_kiosk_op` = '1' WHERE (`id` = '1012');
