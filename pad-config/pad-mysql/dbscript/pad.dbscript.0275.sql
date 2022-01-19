USE pad;

UPDATE pad.booking_slot_limits SET booking_limit = 8 where port_operator_id = 5 and id > 0;
UPDATE pad.booking_slot_limits SET booking_limit = 0 where port_operator_id = 5 and day_of_week_id IN (1,7) and id > 0;
UPDATE pad.booking_slot_limits SET booking_limit = 4 where port_operator_id = 6 and id > 0;
UPDATE pad.booking_slot_limits SET booking_limit = 0 where port_operator_id = 6 and day_of_week_id = 1 and id > 0;
UPDATE pad.booking_slot_limits SET booking_limit = 4 where port_operator_id = 4 and id > 0;
UPDATE pad.booking_slot_limits SET booking_limit = 0 where port_operator_id = 4 and day_of_week_id = 1 and id > 0;
