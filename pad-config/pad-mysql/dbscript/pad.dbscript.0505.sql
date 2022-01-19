USE pad;

-- CREATE TABLE pad.parking_statistics_20210426
-- SELECT * FROM pad.parking_statistics;

-- CREATE TABLE pad.port_statistics_20210426
-- SELECT * FROM pad.port_statistics;

-- DELETE FROM pad.parking_statistics;

-- DELETE FROM pad.port_statistics;

-- ALTER TABLE `pad`.`port_statistics` 
-- DROP COLUMN `port_operator_gate_number`,
-- DROP COLUMN `port_operator_gate_id`,
-- DROP INDEX `port_operator_gate_id_ik` ;
-- ;


-- ALTER TABLE `pad`.`parking_statistics` 
-- DROP COLUMN `port_operator_gate_number`,
-- DROP COLUMN `port_operator_gate_id`,
-- DROP INDEX `port_operator_gate_id_ik` ;
-- ;



-- INSERT INTO pad.port_statistics
-- (date, port_operator_id, port_operator_name, transaction_type, count_entry, amount_total_trip_fee)
-- SELECT DATE(pa.date_entry), m.port_operator_id, po.name, m.transaction_type, COUNT(1), SUM(t.amount_fee)
-- FROM port_access pa
-- LEFT JOIN missions m ON pa.mission_id = m.id
-- LEFT JOIN trips t ON pa.trip_id = t.id
-- LEFT JOIN port_operators po ON m.port_operator_id = po.id
-- WHERE DATE(pa.date_entry) < DATE(NOW())
-- GROUP BY DATE(pa.date_entry), m.port_operator_id, m.transaction_type
-- ORDER BY pa.date_entry, m.port_operator_id, m.transaction_type;



-- INSERT INTO pad.parking_statistics
-- (date, port_operator_id, port_operator_name, transaction_type, count_entry, amount_total_trip_fee)
-- SELECT DATE(p.date_entry), m.port_operator_id, po.name, m.transaction_type, COUNT(1) AS countEntry, SUM(t.amount_fee) AS tripFeeTotalAmount
-- FROM parking p
-- LEFT JOIN missions m ON p.mission_id = m.id
-- LEFT JOIN trips t ON p.trip_id = t.id
-- LEFT JOIN port_operators po ON m.port_operator_id = po.id
-- WHERE p.type = 1 AND DATE(p.date_entry) < DATE(NOW())
-- GROUP BY DATE(p.date_entry), m.port_operator_id, m.transaction_type
-- ORDER BY p.date_entry, m.port_operator_id, m.transaction_type;



UPDATE `pad`.`language_keys` SET `translate_value` = 'Port Entry Counts' WHERE (`id` = '1069');
UPDATE `pad`.`language_keys` SET `translate_value` = 'Nombre d\'entrées de port' WHERE (`id` = '1070');

