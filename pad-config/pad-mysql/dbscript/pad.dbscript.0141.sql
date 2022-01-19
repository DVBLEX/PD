USE pad;

ALTER TABLE `pad`.`port_access_whitelist` 
ADD COLUMN `code` VARCHAR(64) NOT NULL AFTER `id`;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('985', '1', 'KEY_SCREEN_WHITELIST_DELETED_SUCCESS_MESSAGE', 'Night Run successfully deleted');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('986', '2', 'KEY_SCREEN_WHITELIST_DELETED_SUCCESS_MESSAGE', 'Night Run supprimé avec succès');
