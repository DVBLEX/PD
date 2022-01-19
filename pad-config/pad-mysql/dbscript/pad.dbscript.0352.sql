USE pad;

UPDATE pad.trips SET status = 14, port_exit_count = port_exit_count + 1, date_exit_port = date_add(date_entry_port,interval 1 hour) WHERE status = 6 AND date_entry_port <= date_sub(now(),interval 1 hour);
