USE pad;

ALTER TABLE `pad`.`payments` 
ADD COLUMN `first_name` VARCHAR(64) NOT NULL AFTER `notes`,
ADD COLUMN `last_name` VARCHAR(64)  NOT NULL AFTER `first_name`,
ADD COLUMN `msisdn` VARCHAR(16) NOT NULL AFTER `last_name`;

UPDATE pad.payments p 
LEFT JOIN pad.receipt r ON p.id = r.payment_id
LEFT JOIN pad.online_payments op ON p.id = op.payment_id
SET p.first_name = if(r.first_name is not null, r.first_name, if(op.first_name is not null, op.first_name, '')), 
    p.last_name = if(r.last_name is not null, r.last_name, if(op.last_name is not null, op.last_name, '')), 
    p.msisdn = if(r.msisdn is not null, r.msisdn, if(op.msisdn is not null, op.msisdn, ''));

