USE pad;

ALTER TABLE `pad`.`operators`
CHANGE COLUMN `count_passwd_forgot_email` `count_passwd_forgot_requests` INT(11) NOT NULL ;

ALTER TABLE `pad`.`operators`
CHANGE COLUMN `date_last_passwd_forgot_email` `date_last_passwd_forgot_request` DATETIME(3) NULL DEFAULT NULL ;

INSERT INTO `pad`.`sms_templates` (`id`, `type`, `name`, `language_id`, `account_id`, `config_id`, `source_addr`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('7', '4', 'Password Forgot SMS - EN', '1', '-1', '1', 'PAD', 'Please open the link to set a new password. ${resetPasswordLink}', ' ', '10', '-1', '2019-04-10 10:18:20', '2019-04-10 10:18:20');
INSERT INTO `pad`.`sms_templates` (`id`, `type`, `name`, `language_id`, `account_id`, `config_id`, `source_addr`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('8', '4', 'Password Forgot SMS - FR', '2', '-1', '1', 'PAD', 'Veuillez ouvrir le lien pour définir un nouveau mot de passe. ${resetPasswordLink}', ' ', '10', '-1', '2019-04-10 10:18:20', '2019-04-10 10:18:20');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('691', '1', 'KEY_SCREEN_PASSWORD_FORGOT_SMS_SENT_MESSAGE', 'If you have entered the correct details, you will receive SMS with instructions on how to reset your password');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('692', '2', 'KEY_SCREEN_PASSWORD_FORGOT_SMS_SENT_MESSAGE', 'Si vous avez entré les informations correctes, vous recevrez SMS avec les instructions pour réinitialiser votre mot de passe');
