USE pad;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1697', '1', 'KEY_SCREEN_ACCOUNT_NUMBER_LABEL', 'Account Number');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1698', '2', 'KEY_SCREEN_ACCOUNT_NUMBER_LABEL', 'Numéro de compte');

ALTER TABLE `pad`.`accounts` 
ADD COLUMN `number` INT NOT NULL AFTER `code`;

UPDATE pad.accounts a
INNER JOIN pad.account_numbers an ON a.id = an.account_id
SET a.number = an.number;