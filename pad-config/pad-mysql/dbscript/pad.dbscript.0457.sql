USE pad;

update accounts
    inner join operators
    on operators.account_id = accounts.id

set accounts.email_list_invoice_statement=operators.username

where operators.username REGEXP '^[_A-Za-z0-9\\-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$'
  and operators.account_id != -1
  and accounts.type = 1;
