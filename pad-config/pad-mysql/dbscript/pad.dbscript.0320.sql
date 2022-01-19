USE pad;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1469', '1', 'KEY_SCREEN_PORT_EXIT_EXPIRED_LABEL', 'Port Exit - Expired');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1470', '2', 'KEY_SCREEN_PORT_EXIT_EXPIRED_LABEL', 'Sortie de Port - Expirée');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1471', '1', 'KEY_SCREEN_COMPLETED_BY_SYSTEM_LABEL', 'Completed - System');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1472', '2', 'KEY_SCREEN_COMPLETED_BY_SYSTEM_LABEL', 'Terminé - Système');

INSERT INTO `pad`.`system_timer_tasks` (`id`, `name`, `date_last_run`, `type`, `period`, `application`) VALUES ('111', 'tripAnprExpiryTimerTask', '2020-06-12 16:12:39', 'continuous', '300000', 'pad');
