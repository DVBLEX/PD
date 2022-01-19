USE pad;

ALTER TABLE `pad`.`receipt` 
CHANGE COLUMN `type_payment` `payment_option` INT NOT NULL ;
