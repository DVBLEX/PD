USE pad;

SELECT t.id FROM pad.port_access pa LEFT JOIN pad.trips t ON pa.trip_id = t.id WHERE pa.status = 4 AND t.status = 6 AND t.is_direct_to_port=1 AND t.is_allow_multiple_entries=1 AND t.port_entry_count > 0;

-- updates the old trips for already expired port sessions. the code was fixed to handle the condition for multiple entries/direct to port and subsequent entry is after 60 min
UPDATE pad.port_access pa, pad.trips t SET t.status = 15, t.port_exit_count = t.port_exit_count + 1, t.date_exit_port = pa.date_exit
WHERE pa.trip_id = t.id AND pa.status = 4 AND t.status = 6 AND t.is_direct_to_port=1 AND t.is_allow_multiple_entries=1 AND t.port_entry_count > 0;
