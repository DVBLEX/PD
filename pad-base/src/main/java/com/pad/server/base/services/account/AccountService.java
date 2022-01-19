package com.pad.server.base.services.account;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pad.server.base.entities.Account;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.AccountJson;

public interface AccountService {

    public long getAccountsCount(AccountJson accountJson);

    public List<AccountJson> getAccountsList(AccountJson accountJson) throws SQLException;

    public Account getAccountById(Long id);

    public Account getAccountByCode(String code);

    public void updateAccount(Account account);

    public long getAccountsByStatusCount(int status);

    public Map<String, String> getAccountNamesMap(long languageId);

    public List<Account> getActiveAccounts();

    public Account getAccountByCompanyShortName(String companyShortName);

    public long getAccountNumberForAccount(long accountId) throws Exception;

    public BigDecimal getOpeningBalance(String code, Date dateCreated) throws PADException;

    public void notifyAccountWhenLowBalance(Account account, BigDecimal newAmountBalance);
}
