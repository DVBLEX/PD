USE pad;

-- ALTER TABLE `pad`.`invoice` 
-- ADD COLUMN `type` INT NOT NULL AFTER `code`;

-- UPDATE pad.invoice SET type = 2 where id > 0;


-- ALTER TABLE `pad`.`invoice` 
-- DROP INDEX `code_uk` ;
-- ;

ALTER TABLE `pad`.`invoice` 
ADD INDEX `code_ik` (`code` ASC);
;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1713', '1', 'KEY_SCREEN_STATEMENT_CODE_LABEL', 'Statement Code');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1714', '2', 'KEY_SCREEN_STATEMENT_CODE_LABEL', 'Code de relevé');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1715', '1', 'KEY_SCREEN_PERIOD_START_LABEL', 'Period Start');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1716', '2', 'KEY_SCREEN_PERIOD_START_LABEL', 'Début de la période');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1717', '1', 'KEY_SCREEN_PERIOD_END_LABEL', 'Period End');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1718', '2', 'KEY_SCREEN_PERIOD_END_LABEL', 'Fin de période');

