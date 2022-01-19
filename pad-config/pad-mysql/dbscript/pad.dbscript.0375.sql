USE pad;

CREATE TABLE pad.`account_balances` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`account_id` int(11) NOT NULL,
`payment_terms_type` int(11) NOT NULL COMMENT 'prepay or postpay',
`amount_balance` decimal(7,0) NOT NULL,
`amount_overdraft_limit` decimal(7,0) NOT NULL,
`amount_hold` decimal(7,0) NOT NULL,
`date_created` datetime NOT NULL,
PRIMARY KEY (`id`),
KEY `account_id_ik` (`account_id`),
KEY `payment_terms_type_ik` (`payment_terms_type`),
KEY `amount_balance_ik` (`amount_balance`),
KEY `amount_overdraft_limit_ik` (`amount_overdraft_limit`),
KEY `amount_hold_ik` (`amount_hold`),
KEY `date_created_ik` (`date_created`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;
