USE pad;

UPDATE `pad`.`port_operator_transaction_types` SET `is_allowed_for_virtual_kiosk_op` = '0' WHERE (`id` = '1005');
UPDATE `pad`.`port_operator_transaction_types` SET `is_allowed_for_virtual_kiosk_op` = '0' WHERE (`id` = '1048');
