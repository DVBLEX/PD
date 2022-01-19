USE pad;

ALTER TABLE `pad`.`email_templates`
    DROP COLUMN `account_id`,
    DROP INDEX `account_id_ik` ;
;
