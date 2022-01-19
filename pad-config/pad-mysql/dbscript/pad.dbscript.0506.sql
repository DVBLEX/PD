USE pad;

UPDATE `pad`.`port_operator_transaction_types` SET `translate_key` = 'KEY_SCREEN_DROP_OFF_EXPORT_TRIANGLE_LABEL', `translate_key_short` = 'KEY_SCREEN_DROP_OFF_EXPORT_TRIANGLE_SHORT_LABEL' WHERE (`id` = '1045');
UPDATE `pad`.`port_operator_transaction_types` SET `translate_key` = 'KEY_SCREEN_PICK_UP_IMPORT_DIRECT_LABEL', `translate_key_short` = 'KEY_SCREEN_PICK_UP_IMPORT_DIRECT_SHORT_LABEL' WHERE (`id` = '1046');

UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_DROP_OFF_EXPORT_TRIANGLE_LABEL', `translate_value` = 'Drop off Export - Triangle' WHERE (`id` = '1615');
UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_DROP_OFF_EXPORT_TRIANGLE_LABEL', `translate_value` = 'Exportation - Triangle' WHERE (`id` = '1616');
UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_PICK_UP_IMPORT_DIRECT_LABEL', `translate_value` = 'Pick up Import - Direct' WHERE (`id` = '1617');
UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_PICK_UP_IMPORT_DIRECT_LABEL', `translate_value` = 'Importation - Direct' WHERE (`id` = '1618');
UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_DROP_OFF_EXPORT_TRIANGLE_SHORT_LABEL', `translate_value` = 'Exp-Tri' WHERE (`id` = '1619');
UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_DROP_OFF_EXPORT_TRIANGLE_SHORT_LABEL', `translate_value` = 'Exp-Tri' WHERE (`id` = '1620');
UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_PICK_UP_IMPORT_DIRECT_SHORT_LABEL', `translate_value` = 'Imp-Dir' WHERE (`id` = '1621');
UPDATE `pad`.`language_keys` SET `translate_key` = 'KEY_SCREEN_PICK_UP_IMPORT_DIRECT_SHORT_LABEL', `translate_value` = 'Imp-Dir' WHERE (`id` = '1622');
