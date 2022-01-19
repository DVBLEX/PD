USE pad;

ALTER TABLE `pad`.`port_access`
    ADD COLUMN `operator_id_entry` INT NOT NULL AFTER `exit_lane_number`;

UPDATE `pad`.`port_access` SET `operator_id_entry` = -1;

ALTER TABLE `pad`.`port_access`
    ADD INDEX `operator_id_entry_ik` (`operator_id_entry` ASC);
;
