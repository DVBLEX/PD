USE pad;

CREATE TABLE `pad`.`invoice` (
  `id` INT(11) NOT NULL,
  `code` VARCHAR(16) NOT NULL,
  `account_id` INT(11) NOT NULL,
  `total_amount` DECIMAL(7,0) NOT NULL,
  `path` VARCHAR(128) NOT NULL,
  `date_created` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1135', '1', 'KEY_NAVBAR_INVOICE', 'Invoices');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1136', '2', 'KEY_NAVBAR_INVOICE', 'Factures');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1137', '1', 'KEY_SCREEN_INVOICE_CODE_LABEL', 'Code');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1138', '2', 'KEY_SCREEN_INVOICE_CODE_LABEL', 'Code');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1139', '1', 'KEY_SCREEN_INVOICE_TOTAL_LABEL', 'Total');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1140', '2', 'KEY_SCREEN_INVOICE_TOTAL_LABEL', 'Total');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1141', '1', 'KEY_SCREEN_IS_PAID_LABEL', 'Is Paid?');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1142', '2', 'KEY_SCREEN_IS_PAID_LABEL', 'Est payé?');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1143', '1', 'KEY_SCREEN_DOWNLOAD_LABEL', 'Download');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1144', '2', 'KEY_SCREEN_DOWNLOAD_LABEL', 'Télécharger');


ALTER TABLE `pad`.`invoice` 
ADD COLUMN `date_due` DATETIME NOT NULL AFTER `total_amount`,
ADD COLUMN `is_paid` TINYINT(1) NOT NULL DEFAULT 0 AFTER `date_due`,
ADD COLUMN `date_payment` DATETIME NULL AFTER `is_paid`;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1145', '1', 'KEY_SCREEN_DATE_DUE_LABEL', 'Due Date');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1146', '2', 'KEY_SCREEN_DATE_DUE_LABEL', 'Date déchéance');

ALTER TABLE `pad`.`invoice` 
CHANGE COLUMN `date_due` `date_due` DATE NOT NULL ;

ALTER TABLE `pad`.`invoice` 
ADD COLUMN `currency` VARCHAR(3) NOT NULL AFTER `total_amount`;

