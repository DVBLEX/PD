USE pad;

UPDATE pad.trips SET status = 10 where status = 11; -- reset status to "pending approval" from "approved by port operator"
UPDATE pad.trips SET status = 10 where status = 12; -- reset status to "pending approval" from "denied by port operator"
