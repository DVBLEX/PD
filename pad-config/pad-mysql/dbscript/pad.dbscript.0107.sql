USE pad;

ALTER TABLE `pad`.`parking`
ADD COLUMN `is_eligible_port_entry` TINYINT(1) NOT NULL COMMENT 'this flag will be set once truck exits parking after being authorised to exit by parking office operator' AFTER `status`;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('743', '1', 'KEY_SCREEN_EXITED_PARKING_LABEL', 'Exited Parking');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('744', '2', 'KEY_SCREEN_EXITED_PARKING_LABEL', 'Parking fermé');
