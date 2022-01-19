USE pad;

-- ALTER TABLE `pad`.`booking_slot_limits`
-- ADD COLUMN `transaction_type` INT(11) NOT NULL AFTER `port_operator_id`;

-- ALTER TABLE `pad`.`booking_slot_counts`
-- ADD COLUMN `transaction_type` INT(11) NOT NULL AFTER `port_operator_id`;

-- ALTER TABLE `pad`.`booking_slot_limits` RENAME TO `pad`.`booking_slot_limits_default`;

-- ALTER TABLE `pad`.`booking_slot_limits_default` COMMENT = 'captures the booking limits per port operator per transaction type per day of week per hour slot';

CREATE TABLE `booking_slot_limits` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`port_operator_id` int(11) NOT NULL,
`transaction_type` int(11) NOT NULL,
`date_slot`DATE NOT NULL,
`day_of_week_id` int(11) NOT NULL,
`day_of_week_name` varchar(12) NOT NULL,
`hour_slot_from` int(11) NOT NULL,
`hour_slot_to` int(11) NOT NULL,
`booking_limit` int(11) NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='captures the booking limits per port operator per transaction type per day of week per hour slot';

DELETE FROM pad.booking_slot_limits_default WHERE id > 0;

ALTER TABLE pad.booking_slot_limits_default AUTO_INCREMENT=0;
