USE pad;

UPDATE pad.trips SET parking_permission_id_parking_entry = -1 WHERE parking_permission_id_parking_entry = 0 AND id > 0;
UPDATE pad.trips SET parking_permission_id_parking_exit = -1 WHERE parking_permission_id_parking_exit = 0 AND id > 0;
UPDATE pad.trips SET parking_permission_id_port_entry = -1 WHERE parking_permission_id_port_entry = 0 AND id > 0;
UPDATE pad.trips SET parking_permission_id_port_exit = -1 WHERE parking_permission_id_port_exit = 0 AND id > 0;
