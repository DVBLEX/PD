USE pad;

ALTER TABLE `pad`.`port_operator_transaction_types` 
ADD COLUMN `is_allowed_for_parking_and_kiosk_op` TINYINT(1) NOT NULL AFTER `translate_key`;

UPDATE pad.port_operator_transaction_types SET is_allowed_for_parking_and_kiosk_op = 1 WHERE transaction_type NOT IN (5,6) AND id > 0;
