USE pad;

CREATE TABLE `receipt` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(16) NOT NULL,
  `account_id` int(11) NOT NULL,
  `type_payment` int(2) NOT NULL,
  `total_amount` decimal(7,0) NOT NULL,
  `currency` varchar(3) NOT NULL,
  `path` varchar(128) NOT NULL,
  `operator_id` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_edited` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;