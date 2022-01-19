USE pad;

UPDATE pad.port_operator_transaction_types SET port_transit_duration_minutes = 15 where id > 0;
UPDATE `pad`.`language_keys` SET `translate_value` = 'Langue du chauffeur' WHERE (`id` = '1328');
