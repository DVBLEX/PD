USE pad;

ALTER TABLE `pad`.`operators`
    ADD COLUMN `independent_port_operator_id` INT NOT NULL AFTER `port_operator_id`;

UPDATE `pad`.`operators` SET `independent_port_operator_id` = -1;
