USE pad;

ALTER TABLE `pad`.`port_operator_transaction_types` 
ADD COLUMN `translate_key` VARCHAR(128) NOT NULL AFTER `transaction_type`;

UPDATE pad.port_operator_transaction_types SET translate_key = 'KEY_SCREEN_DROP_OFF_EXPORT_LABEL' WHERE id > 0 AND transaction_type = 1;
UPDATE pad.port_operator_transaction_types SET translate_key = 'KEY_SCREEN_DROP_OFF_EMPTY_LABEL' WHERE id > 0 AND transaction_type = 2;
UPDATE pad.port_operator_transaction_types SET translate_key = 'KEY_SCREEN_PICK_UP_IMPORT_LABEL' WHERE id > 0 AND transaction_type = 3;
UPDATE pad.port_operator_transaction_types SET translate_key = 'KEY_SCREEN_PICK_UP_EMPTY_LABEL' WHERE id > 0 AND transaction_type = 4;
UPDATE pad.port_operator_transaction_types SET translate_key = 'KEY_SCREEN_URGENT_DROP_OFF_EXPORT_LABEL' WHERE id > 0 AND transaction_type = 5;
UPDATE pad.port_operator_transaction_types SET translate_key = 'KEY_SCREEN_URGENT_PICK_UP_IMPORT_LABEL' WHERE id > 0 AND transaction_type = 6;

DELETE FROM pad.port_operator_transaction_types WHERE id > 0 AND transaction_type = 7;
DELETE FROM pad.port_operator_transaction_types WHERE id > 0 AND port_operator_id != 2 AND transaction_type IN (5,6);
