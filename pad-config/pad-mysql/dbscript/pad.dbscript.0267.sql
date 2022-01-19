USE pad;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1289', '1', 'KEY_SCREEN_ABORT_MESSAGE', 'Abort');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1290', '2', 'KEY_SCREEN_ABORT_MESSAGE', 'Avorter');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1291', '1', 'KEY_RESPONSE_1157', 'Unexpected trip status');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1292', '2', 'KEY_RESPONSE_1157', 'Statut de voyage inattendu');

UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_TRIP_CANCEL_ALERT_MESSAGE', `translate_value` = 'Are you sure you want to cancel this trip?' WHERE (`id` = '657');
UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_TRIP_CANCEL_ALERT_MESSAGE', `translate_value` = 'Êtes-vous sûr de vouloir annuler ce voyage?' WHERE (`id` = '658');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1293', '1', 'KEY_SCREEN_TRIP_ABORT_ALERT_MESSAGE', 'Are you sure you want to abort this trip?');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1294', '2', 'KEY_SCREEN_TRIP_ABORT_ALERT_MESSAGE', 'Êtes-vous sûr de vouloir avorter ce voyage?');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1295', '1', 'KEY_SCREEN_CANCELLED_LABEL', 'CANCELLED');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1296', '2', 'KEY_SCREEN_CANCELLED_LABEL', 'ANNULÉ');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1297', '1', 'KEY_SCREEN_ABORTED_LABEL', 'ABORTED');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1298', '2', 'KEY_SCREEN_ABORTED_LABEL', 'AVORTÉ');
