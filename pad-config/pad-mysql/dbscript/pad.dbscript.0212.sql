USE pad;

UPDATE `pad`.`language_keys` SET `translate_value` = 'A message for password set up has been sent to' WHERE (`id` = '799');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Un message pour la configuration du mot de passe a été envoyé à' WHERE (`id` = '800');

INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1195', '1', 'KEY_SCREEN_USER_ACCOUNTS_LABEL', 'User Accounts');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1196', '2', 'KEY_SCREEN_USER_ACCOUNTS_LABEL', 'Comptes Utilisateur');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1197', '1', 'KEY_SCREEN_ADD_USER_ACCOUNT_LABEL', 'Add User Account');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1198', '2', 'KEY_SCREEN_ADD_USER_ACCOUNT_LABEL', 'Ajouter un compte utilisateur');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1199', '1', 'KEY_SCREEN_NO_USER_ACCOUNT_RETURNED_MESSAGE', 'No User Accounts returned');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1200', '2', 'KEY_SCREEN_NO_USER_ACCOUNT_RETURNED_MESSAGE', 'Aucun compte d\'utilisateur retourné');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1201', '1', 'KEY_SCREEN_UPDATE_USER_ACCOUNT_LABEL', 'Update User Account');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1202', '2', 'KEY_SCREEN_UPDATE_USER_ACCOUNT_LABEL', 'Mettre à jour le compte d\'utilisateur');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1203', '1', 'KEY_SCREEN_USER_ACCOUNT_UPDATED_SUCCESS_MESSAGE', 'User Account details updated successfully');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1204', '2', 'KEY_SCREEN_USER_ACCOUNT_UPDATED_SUCCESS_MESSAGE', 'Les détails du compte d\'utilisateur ont été mis à jour avec succès');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1205', '1', 'KEY_SCREEN_USER_ACCOUNT_ADDED_SUCCESS_MESSAGE', 'User Account added successfully');
INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1206', '2', 'KEY_SCREEN_USER_ACCOUNT_ADDED_SUCCESS_MESSAGE', 'Compte utilisateur ajouté avec succès');
