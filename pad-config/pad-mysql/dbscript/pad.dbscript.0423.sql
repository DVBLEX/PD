USE pad;

UPDATE pad.missions SET status = 2 where status = 1 and account_id != -1 and count_trips_booked > 0 and id > 0;
