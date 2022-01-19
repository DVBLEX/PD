USE pad;

ALTER TABLE `pad`.`port_operator_transaction_types` 
ADD COLUMN `is_auto_release_parking` TINYINT(1) NOT NULL AFTER `is_allowed_for_parking_and_kiosk_op`;

UPDATE pad.port_operator_transaction_types SET is_auto_release_parking = 0 where id > 0;
INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('36', 'Auto Release Vehicles at Parking', '2020-03-09 17:41:14');

INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('37', 'Auto Release Vehicles at Parking', '2020-03-09 17:41:14');

UPDATE `pad`.`activity_log_description` SET `name` = 'Abort Trip' WHERE (`id` = '36');

ALTER TABLE `pad`.`activity_log` 
ADD COLUMN `is_auto_release_parking` TINYINT(1) NOT NULL AFTER `reference_number`;
