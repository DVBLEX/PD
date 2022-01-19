USE pad;

UPDATE `pad`.`independent_port_operators` SET `is_active` = '0' WHERE (`id` = '6');

UPDATE pad.accounts SET amount_overdraft_limit = 0 where payment_terms_type = 1 AND id > 0;
