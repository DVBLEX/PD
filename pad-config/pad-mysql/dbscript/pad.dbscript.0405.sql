USE pad;

ALTER TABLE `pad`.`accounts`
ADD COLUMN `is_trip_approved_email` TINYINT(1) NOT NULL AFTER `language_id`;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1701', '1', 'KEY_SCREEN_RECEIVE_TRIP_APPROVED_EMAIL_LABEL', 'Receive Trip Approved Email');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1702', '2', 'KEY_SCREEN_RECEIVE_TRIP_APPROVED_EMAIL_LABEL', 'Recevoir un e-mail d\'approbation de voyage');
