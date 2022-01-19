USE pad;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1655', '1', 'KEY_SCREEN_CANCEL_ADHOC_TRIP_LABEL', 'Cancel ADHOC trip');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1656', '2', 'KEY_SCREEN_CANCEL_ADHOC_TRIP_LABEL', 'Annuler le voyage ADHOC');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1657', '1', 'KEY_SCREEN_ADHOC_TRIP_CANCELLED_SUCCESSFULLY_MESSAGE', 'ADHOC trip has been cancelled successfully');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1658', '2', 'KEY_SCREEN_ADHOC_TRIP_CANCELLED_SUCCESSFULLY_MESSAGE', 'Le voyage ADHOC a été annulé avec succès');
-- UPDATE `pad`.`language_keys` SET `translate_value` = 'Ad hoc trip was successfully cancelled' WHERE (`id` = '1657');
-- UPDATE `pad`.`language_keys` SET `translate_value` = 'Le voyage ad hoc a été annulé avec succès' WHERE (`id` = '1658');
-- UPDATE `pad`.`language_keys` SET `translate_value` = 'Cancel Ad hoc trip' WHERE (`id` = '1655');
-- UPDATE `pad`.`language_keys` SET `translate_value` = 'Annuler le voyage Ad hoc' WHERE (`id` = '1656');
-- UPDATE `pad`.`language_keys` SET `translate_value` = 'Le voyage Ad hoc a été annulé avec succès' WHERE (`id` = '1658');

INSERT INTO `pad`.`activity_log_description` (`id`, `name`, `date_created`) VALUES ('49', 'Trip ADHOC Cancel', '2020-09-24 12:00:00');
