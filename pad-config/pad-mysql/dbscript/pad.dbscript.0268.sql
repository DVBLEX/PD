USE pad;

-- ALTER TABLE `pad`.`system_parameters` 
-- ADD COLUMN `receipt_lock_count_failed` INT(11) NOT NULL AFTER `receipt_link_expire_days`,
-- ADD COLUMN `receipt_lock_period` INT(11) NOT NULL AFTER `receipt_lock_count_failed`;

-- ALTER TABLE `pad`.`receipt` 
-- ADD COLUMN `lock_count_failed` INT(11) NOT NULL AFTER `operator_id`,
-- ADD COLUMN `date_lock` DATETIME NULL AFTER `lock_count_failed`;

-- UPDATE `pad`.`system_parameters` SET `receipt_lock_count_failed` = '5', `receipt_lock_period` = '2' WHERE (`id` = '1');

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1299', '1', 'KEY_RESPONSE_1197', 'Receipt download is locked due too many wrong attempts');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1300', '2', 'KEY_RESPONSE_1197', 'Le téléchargement du reçu est verrouillé en raison d\'un trop grand nombre de tentatives erronées');
