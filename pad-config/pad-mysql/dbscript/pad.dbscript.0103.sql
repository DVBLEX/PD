USE pad;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('719', '1', 'KEY_SCREEN_EXIT_DUE_LABEL', 'Exit Due');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('720', '2', 'KEY_SCREEN_EXIT_DUE_LABEL', 'Sortie due');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('721', '1', 'KEY_SCREEN_EXIT_PARKING_DRIVER_NOTIFIED_LABEL', 'Driver Notified');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('722', '2', 'KEY_SCREEN_EXIT_PARKING_DRIVER_NOTIFIED_LABEL', 'Conducteur notifié');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('723', '1', 'KEY_SCREEN_EXIT_DUE_VEHICLE_LABEL', 'Parking Exit Due For Vehicle');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('724', '2', 'KEY_SCREEN_EXIT_DUE_VEHICLE_LABEL', 'Sortie de stationnement due pour le véhicule');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('725', '1', 'KEY_SCREEN_EXIT_SMS_CONTENT_LABEL', 'Exit SMS Content');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('726', '2', 'KEY_SCREEN_EXIT_SMS_CONTENT_LABEL', 'Quitter le contenu SMS');

UPDATE `pad`.`language_keys` SET `translate_key`='KEY_SCREEN_EXIT_VEHICLE_LABEL', `translate_value`='Parking Exit For Vehicle' WHERE `id`='723';
UPDATE `pad`.`language_keys` SET `translate_key`='KEY_SCREEN_EXIT_VEHICLE_LABEL', `translate_value`='Sortie de stationnement pour véhicule' WHERE `id`='724';

ALTER TABLE `pad`.`payments`
DROP COLUMN `status`;

ALTER TABLE `pad`.`payments`
DROP COLUMN `date_paid`,
DROP COLUMN `is_paid`,
DROP COLUMN `amount_payment`,
ADD COLUMN `amount_credit` DECIMAL(7,0) NOT NULL AFTER `currency`,
ADD COLUMN `amount_debit` DECIMAL(7,0) NOT NULL AFTER `amount_credit`,
ADD COLUMN `amount_running_balance` DECIMAL(7,0) NOT NULL AFTER `amount_debit`;
