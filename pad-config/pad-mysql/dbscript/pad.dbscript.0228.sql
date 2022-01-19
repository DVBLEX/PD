USE pad;

DELETE FROM pad.anpr_entry_scheduler WHERE is_processed = 3 AND id > 0;
