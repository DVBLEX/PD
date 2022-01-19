USE pad;

DELETE FROM `pad`.`port_operators` WHERE (`id` = '9');
DELETE FROM pad.port_operator_gates WHERE port_operator_id = 9 AND id > 0;
DELETE FROM pad.port_operator_transaction_types WHERE port_operator_id = 9 AND id > 0;
DELETE FROM pad.port_operator_trip_fee WHERE port_operator_id = 9 AND id > 0;
UPDATE pad.activity_log SET port_operator_id = 5 WHERE port_operator_id = 9 AND id > 0;
UPDATE pad.booking_slot_counts SET port_operator_id = 5 WHERE port_operator_id = 9 AND id > 0;
DELETE FROM pad.booking_slot_limits WHERE port_operator_id = 9 and id > 0;
UPDATE pad.missions SET port_operator_id = 5 WHERE port_operator_id = 9 AND id > 0;
UPDATE pad.operators SET port_operator_id = 5 WHERE port_operator_id = 9 AND id > 0;
UPDATE pad.parking SET port_operator_id = 5 WHERE port_operator_id = 9 AND id > 0;
UPDATE pad.port_access SET port_operator_id = 5 WHERE port_operator_id = 9 AND id > 0;
UPDATE pad.port_access_whitelist SET port_operator_id = 5 WHERE port_operator_id = 9 AND id > 0;
UPDATE pad.transporter_trips_statistics SET port_operator_id = 5 WHERE port_operator_id = 9 AND id > 0;
UPDATE pad.trips SET port_operator_id = 5 WHERE port_operator_id = 9 AND id > 0;

DELETE FROM pad.port_statistics WHERE port_operator_id = 9 AND id > 0;

UPDATE pad.parking_statistics SET port_operator_gate_id = 1007 WHERE port_operator_gate_id = 1016 AND id > 0;
UPDATE pad.parking_statistics SET port_operator_gate_id = 1008 WHERE port_operator_gate_id = 1017 AND id > 0;

UPDATE pad.parking_statistics AS ps1,
(SELECT date, port_operator_gate_id, transaction_type, count_entry FROM pad.parking_statistics WHERE port_operator_id = 9 AND count_entry > 0) AS ps2
SET ps1.count_entry = ps1.count_entry + ps2.count_entry
WHERE ps1.port_operator_id = 5 AND ps1.date = ps2.date AND ps1.port_operator_gate_id = ps2.port_operator_gate_id AND ps1.transaction_type = ps2.transaction_type AND ps1.id > 0;

DELETE FROM pad.parking_statistics WHERE port_operator_id = 9 AND id > 0;
