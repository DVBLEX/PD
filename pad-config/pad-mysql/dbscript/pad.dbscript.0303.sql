USE pad;

ALTER TABLE `pad`.`system_parameters` 
ADD COLUMN `auto_release_exit_capacity_percentage` INT(11) NOT NULL AFTER `ags_operations_email`;

UPDATE `pad`.`system_parameters` SET `auto_release_exit_capacity_percentage` = '150' WHERE (`id` = '1');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1443', '1', 'KEY_SCREEN_AUTORELEASE_EXIT_CAPACITY_LABEL', 'Auto Release Exit Capacity');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1444', '2', 'KEY_SCREEN_AUTORELEASE_EXIT_CAPACITY_LABEL', 'Capacité de Sortie de Libération Automatique');
