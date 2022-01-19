USE pad;

ALTER TABLE `accounts`
ADD COLUMN `is_deduct_credit_registered_trucks` INT NOT NULL DEFAULT 0 AFTER `language_id`;


