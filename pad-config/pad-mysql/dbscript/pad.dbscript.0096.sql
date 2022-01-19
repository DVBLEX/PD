USE pad;

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('655', '1', 'KEY_SCREEN_ENTERED_PARKING_LABEL', 'Entered Parking');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('656', '2', 'KEY_SCREEN_ENTERED_PARKING_LABEL', 'Parking entré');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('657', '1', 'KEY_SCREEN_TRIP_DELETE_ALERT_MESSAGE', 'Are you sure you want to delete this trip?');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('658', '2', 'KEY_SCREEN_TRIP_DELETE_ALERT_MESSAGE', 'Êtes-vous sûr de vouloir supprimer ce voyage?');

CREATE TABLE pad.`sms_code_requests` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msisdn` varchar(64) NOT NULL,
  `code` varchar(5) NOT NULL,
  `token` varchar(64) DEFAULT NULL,
  `count_code_sent` int(11) NOT NULL,
  `count_verified` int(11) NOT NULL,
  `date_code_sent` datetime NOT NULL,
  `date_verified` datetime DEFAULT NULL,
  `date_created` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `msisdn_UNIQUE` (`msisdn`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO `pad`.`sms_templates` (`id`, `type`, `name`, `language_id`, `account_id`, `config_id`, `source_addr`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('3', '2', 'SMS Verification Code - EN', '1', '-1', '1', 'PAD', 'Your Port Access Dakar (PAD) verification code is: ${verificationCode}', ' ', '10', '-1', '2019-04-10 10:18:20', '2019-04-10 10:18:20');
INSERT INTO `pad`.`sms_templates` (`id`, `type`, `name`, `language_id`, `account_id`, `config_id`, `source_addr`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('4', '2', 'SMS Verification Code - FR', '2', '-1', '1', 'PAD', 'Votre code de vérification de Port Access Dakar (PAD) est le suivant: ${verificationCode}', ' ', '10', '-1', '2019-04-10 10:18:20', '2019-04-10 10:18:20');

UPDATE `pad`.`sms_templates` SET `message`='Your Port Access Dakar (PAD) verification code is: ${verificationCode}. This verification code will be active for the next ${smsCodeValidityPeriod} minutes.' WHERE `id`='3';
UPDATE `pad`.`sms_templates` SET `message`='Votre code de vérification de Port Access Dakar (PAD) est le suivant: ${verificationCode}. Ce code de vérification sera actif pendant les ${smsCodeValidityPeriod} prochaines minutes.' WHERE `id`='4';

ALTER TABLE `pad`.`system_parameters`
ADD COLUMN `reg_sms_code_send_limit` INT(11) NOT NULL AFTER `reg_email_verification_valid_hours`,
ADD COLUMN `reg_sms_verification_limit` INT(11) NOT NULL AFTER `reg_sms_code_send_limit`,
ADD COLUMN `reg_sms_code_valid_minutes` INT(11) NOT NULL AFTER `reg_sms_verification_limit`,
ADD COLUMN `reg_sms_verification_valid_hours` INT(11) NOT NULL AFTER `reg_sms_code_valid_minutes`;

UPDATE `pad`.`system_parameters` SET `reg_sms_code_send_limit`='10', `reg_sms_verification_limit`='3', `reg_sms_code_valid_minutes`='30', `reg_sms_verification_valid_hours`='4' WHERE `id`='1';

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('659', '1', 'KEY_RESPONSE_1163', 'Mobile number is already registered');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('660', '2', 'KEY_RESPONSE_1163', 'Le numéro de mobile est déjà enregistré');

UPDATE `pad`.`language_keys` SET `translate_value`='Account Type' WHERE `id`='349';
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('661', '1', 'KEY_SCREEN_LOGIN_MOBILE_NUMBER_VERIFICATION_LABEL', 'Login - Mobile Number Verification');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('662', '2', 'KEY_SCREEN_LOGIN_MOBILE_NUMBER_VERIFICATION_LABEL', 'Login - Vérification du numéro de portable');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('663', '1', 'KEY_SCREEN_MOBILE_NUMBER_CONFIRM_LABEL', 'Confirm Mobile Number');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('664', '2', 'KEY_SCREEN_MOBILE_NUMBER_CONFIRM_LABEL', 'Confirmer le numéro de portable');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('665', '1', 'KEY_SCREEN_VERIFICATION_CODE_SMS_SENT_MESSAGE', 'The verification code was sent via SMS to ');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('666', '2', 'KEY_SCREEN_VERIFICATION_CODE_SMS_SENT_MESSAGE', 'Le code de vérification a été envoyé par SMS à');

UPDATE `pad`.`language_keys` SET `translate_value`='Didn\'t receive the verification code' WHERE `id`='335';
UPDATE `pad`.`language_keys` SET `translate_value`='N\'a pas reçu le code de vérification' WHERE `id`='336';

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('667', '1', 'KEY_SCREEN_TO_RESEND_CHANGE_MSISDN_LABEL', 'to resend or change the mobile number');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('668', '2', 'KEY_SCREEN_TO_RESEND_CHANGE_MSISDN_LABEL', 'renvoyer ou changer le numéro de mobile');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('669', '1', 'KEY_SCREEN_VERIFY_MSISDN_LABEL', 'Verify Mobile Number');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('670', '2', 'KEY_SCREEN_VERIFY_MSISDN_LABEL', 'Vérifier le numéro de portable');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('671', '1', 'KEY_SCREEN_THE_MOBILE_NUMBER_LABEL', 'The Mobile Number');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('672', '2', 'KEY_SCREEN_THE_MOBILE_NUMBER_LABEL', 'Le numéro de portable');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('673', '1', 'KEY_SCREEN_ENTER_VALID_CONFIRM_MOBILE_NUMBER_MESSAGE', 'Please enter valid Confirm Mobile Number');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('674', '2', 'KEY_SCREEN_ENTER_VALID_CONFIRM_MOBILE_NUMBER_MESSAGE', 'S\'il vous plaît entrer valide Confirmer le numéro de téléphone');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('675', '1', 'KEY_SCREEN_CONFIRM_MOBILE_NUMBER_NOT_MATCH_MESSAGE', 'Confirm Mobile Number does not match Mobile Number');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('676', '2', 'KEY_SCREEN_CONFIRM_MOBILE_NUMBER_NOT_MATCH_MESSAGE', 'Confirmer que le numéro de mobile ne correspond pas au numéro de mobile');

INSERT INTO `pad`.`sms_templates` (`id`, `type`, `name`, `language_id`, `account_id`, `config_id`, `source_addr`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('5', '3', 'Registration Completed - EN', '1', '-1', '1', 'PAD', 'Hello ${operatorName}. We are delighted to confirm your registration with Port Access Dakar! Your account will be activated in the next 24 hours.', ' ', '10', '-1', '2019-04-10 10:18:20', '2019-04-10 10:18:20');
INSERT INTO `pad`.`sms_templates` (`id`, `type`, `name`, `language_id`, `account_id`, `config_id`, `source_addr`, `message`, `variables`, `priority`, `operator_id`, `date_created`, `date_edited`) VALUES ('6', '3', 'Registration Completed - EN', '2', '-1', '1', 'PAD', 'Hello ${operatorName}. We are delighted to confirm your registration with Port Access Dakar! Your account will be activated in the next 24 hours.', ' ', '10', '-1', '2019-04-10 10:18:20', '2019-04-10 10:18:20');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('677', '1', 'KEY_SCREEN_ADDRESS_LINE_LABEL', 'Address Line');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('678', '2', 'KEY_SCREEN_ADDRESS_LINE_LABEL', 'Ligne d\'adresse');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('679', '1', 'KEY_SCREEN_ENTER_ADDRESS_MESSAGE', 'Please enter Address Line');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('680', '2', 'KEY_SCREEN_ENTER_ADDRESS_MESSAGE', 'S\'il vous plaît entrer la ligne d\'adresse');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('681', '1', 'KEY_SCREEN_ADDRESS_LABEL', 'Address');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('682', '2', 'KEY_SCREEN_ADDRESS_LABEL', 'Adresse');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('683', '1', 'KEY_SCREEN_PLEASE_LOGIN_MOBILE_PASSWORD_MESSAGE', 'Please Login with your mobile number and password to continue');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('684', '2', 'KEY_SCREEN_PLEASE_LOGIN_MOBILE_PASSWORD_MESSAGE', 'Veuillez vous connecter avec votre numéro de téléphone mobile et votre mot de passe pour continuer');


