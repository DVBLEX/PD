USE pad;

ALTER TABLE `pad`.`sessions` 
ADD COLUMN `lane_id` INT NOT NULL AFTER `operator_id`;

ALTER TABLE `pad`.`online_payment_parameters` 
ADD COLUMN `is_print_receipt` TINYINT(1) NOT NULL AFTER `default_conn_request_timeout`;

ALTER TABLE `pad`.`online_payments` 
ADD COLUMN `payment_id` INT NOT NULL AFTER `driver_id`;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1499', '1', 'KEY_SCREEN_IS_PRINT_RECEIPT_LABEL', 'Is Print Receipt');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1500', '2', 'KEY_SCREEN_IS_PRINT_RECEIPT_LABEL', 'Est le reçu d\'impression');
