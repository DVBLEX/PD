USE pad;

INSERT INTO `pad`.`system_timer_tasks` (`id`, `name`, `date_last_run`, `type`, `period`, `application`) VALUES ('103', 'missionExpiryTaskExecutor', '2019-04-08 09:29:34', 'cron', '0 0/2 4-7 * * ?', 'pad');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('653', '1', 'KEY_SCREEN_EXPIRED_LABEL', 'Expired');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('654', '2', 'KEY_SCREEN_EXPIRED_LABEL', 'Expiré');
