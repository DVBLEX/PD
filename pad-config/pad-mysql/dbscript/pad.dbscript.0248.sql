USE pad;

CREATE TABLE `pad`.`receipt_unique_urls` (
  `receipt_id` int(11) NOT NULL AUTO_INCREMENT,
  `unique_url` varchar(10) NOT NULL,
  PRIMARY KEY (`receipt_id`,`unique_url`),
  UNIQUE KEY `unique_url_UNIQUE` (`unique_url`),
  UNIQUE KEY `receipt_id_UNIQUE` (`receipt_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


ALTER TABLE `pad`.`receipt` 
CHANGE COLUMN `code` `number` VARCHAR(16) NOT NULL ;

ALTER TABLE `pad`.`receipt` 
ADD COLUMN `unique_url` VARCHAR(16) NOT NULL AFTER `path`;
