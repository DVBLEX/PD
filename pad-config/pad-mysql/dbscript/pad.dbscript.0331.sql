USE pad;

ALTER TABLE `pad`.`trips` 
ADD COLUMN `is_direct_to_port` TINYINT(1) NOT NULL AFTER `port_exit_count`,
ADD COLUMN `is_allow_multiple_entries` TINYINT(1) NOT NULL AFTER `is_direct_to_port`;

UPDATE pad.trips t
INNER JOIN pad.missions m ON t.mission_id = m.id
SET t.is_direct_to_port = IFNULL( (SELECT 
    IF(is_direct_to_port IS NULL OR is_direct_to_port = '', 0, is_direct_to_port) 
    FROM pad.port_operator_transaction_types pot WHERE pot.port_operator_id = m.port_operator_id AND pot.transaction_type = m.transaction_type), 0)
WHERE t.id > 0;

UPDATE pad.trips t
INNER JOIN pad.missions m ON t.mission_id = m.id
SET t.is_allow_multiple_entries = IFNULL( (SELECT 
    IF(is_allow_multiple_entries IS NULL OR is_allow_multiple_entries = '', 0, is_allow_multiple_entries) 
    FROM pad.port_operator_transaction_types pot WHERE pot.port_operator_id = m.port_operator_id AND pot.transaction_type = m.transaction_type), 0)
WHERE t.id > 0;

ALTER TABLE `pad`.`missions` 
ADD COLUMN `is_direct_to_port` TINYINT(1) NOT NULL AFTER `operator_id`,
ADD COLUMN `is_allow_multiple_entries` TINYINT(1) NOT NULL AFTER `is_direct_to_port`;

UPDATE pad.missions m
LEFT JOIN pad.port_operator_transaction_types p ON m.port_operator_id = p.port_operator_id AND m.transaction_type = p.transaction_type
SET m.is_direct_to_port = p.is_direct_to_port
WHERE p.is_direct_to_port = 1;

UPDATE pad.missions m
LEFT JOIN pad.port_operator_transaction_types p ON m.port_operator_id = p.port_operator_id AND m.transaction_type = p.transaction_type
SET m.is_allow_multiple_entries = p.is_allow_multiple_entries
WHERE p.is_allow_multiple_entries = 1;

-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1495', '1', 'KEY_SCREEN_DIRECT_TO_PORT_LABEL', 'Direct to Port?');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1496', '2', 'KEY_SCREEN_DIRECT_TO_PORT_LABEL', 'Direct au Port?');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1497', '1', 'KEY_SCREEN_MULTIPLE_ENTRIES_LABEL', 'Multiple Entries?');
-- INSERT INTO `pad`.`language_keys` (`id`, `language_id`, `translate_key`, `translate_value`) VALUES ('1498', '2', 'KEY_SCREEN_MULTIPLE_ENTRIES_LABEL', 'Plusieurs Entrées?');
