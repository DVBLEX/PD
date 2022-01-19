USE pad;

UPDATE pad.online_payments SET response_code = 1600 WHERE status_aggregator = 'SUCCESSFUL';
UPDATE pad.online_payments SET response_code = 1560 WHERE status_aggregator = 'FAILED';