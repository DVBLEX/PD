package com.pad.server.base.services.account;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.PreparedJDBCQuery;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.AccountBalance;
import com.pad.server.base.entities.AccountNumber;
import com.pad.server.base.entities.Email;
import com.pad.server.base.entities.Sms;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.AccountJson;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.sms.SmsService;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = Logger.getLogger(AccountServiceImpl.class);

    @Autowired
    private HibernateTemplate   hibernateTemplate;

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private SessionFactory      sessionFactory;

    @Autowired
    private EmailService        emailService;

    @Autowired
    private SmsService          smsService;

    @Override
    public long getAccountsCount(AccountJson accountJson) {

        PreparedJDBCQuery query = getAccountsQuery(PreparedJDBCQuery.QUERY_TYPE_COUNT, accountJson);

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Long.class);
    }

    @Override
    public List<AccountJson> getAccountsList(AccountJson accountJson) throws SQLException {

        final List<AccountJson> accountsList = new ArrayList<>();

        PreparedJDBCQuery query = getAccountsQuery(PreparedJDBCQuery.QUERY_TYPE_SELECT, accountJson);

        query.setSortParameters(accountJson.getSortColumn(), accountJson.getSortAsc(), "accounts", "status", null);
        query.setLimitParameters(accountJson.getCurrentPage(), accountJson.getPageCount());

        jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {

                AccountJson account = new AccountJson();
                account.setCode(rs.getString("accounts.code"));
                account.setNumber(rs.getLong("accounts.number"));
                account.setType(rs.getInt("accounts.type"));
                account.setStatus(rs.getInt("accounts.status"));
                account.setFirstName(rs.getString("accounts.first_name"));
                account.setLastName(rs.getString("accounts.last_name"));
                account.setCompanyName(rs.getString("accounts.company_name"));
                account.setCompanyRegistration(rs.getString("accounts.company_registration"));
                account.setPaymentTermsType(rs.getInt("accounts.payment_terms_type"));
                account.setCompanyTelephone(rs.getString("accounts.company_telephone"));
                account.setRegistrationCountryISO(rs.getString("accounts.registration_country_iso"));
                account.setAddress1(rs.getString("accounts.address_1"));
                account.setAddress2(rs.getString("accounts.address_2"));
                account.setAddress3(rs.getString("accounts.address_3"));
                account.setAddress4(rs.getString("accounts.address_4"));
                account.setPostCode(rs.getString("accounts.post_code"));
                account.setMsisdn(rs.getString("accounts.msisdn"));
                account.setNationalityCountryISO(rs.getString("accounts.nationality_country_iso"));
                account.setBalanceAmount(rs.getBigDecimal("accounts.amount_balance"));
                account.setAmountOverdraftLimit(rs.getBigDecimal("accounts.amount_overdraft_limit"));
                account.setAmountHold(rs.getBigDecimal("accounts.amount_hold"));
                account.setDenialReason(rs.getString("accounts.denial_reason"));
                account.setCurrency(rs.getString("currency"));
                account.setSpecialTaxStatus(rs.getBoolean("accounts.special_tax_status"));
                account.setIsDeleted(rs.getBoolean("is_deleted"));
                account.setIsActive(account.getStatus() == ServerConstants.ACCOUNT_STATUS_ACTIVE);
                account.setDateCreated(rs.getTimestamp("accounts.date_created"));
                try {
                    account.setDateCreatedString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("accounts.date_created")));
                } catch (ParseException e) {
                    account.setDateCreatedString("");
                }

                if (account.getType() == ServerConstants.ACCOUNT_TYPE_COMPANY) {
                    account.setAccountName(account.getCompanyName());
                } else {
                    account.setAccountName(account.getFirstName() + " " + account.getLastName());
                }

                accountsList.add(account);
            }
        }, query.getQueryParameters());

        return accountsList;
    }

    @Override
    public Account getAccountById(Long id) {

        Account account = null;
        account = hibernateTemplate.get(Account.class, id);

        return account;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Account getAccountByCode(String code) {

        Account account = null;

        List<Account> accountList = (List<Account>) hibernateTemplate.findByNamedParam("FROM Account WHERE code = :code", "code", code);

        if (accountList != null && !accountList.isEmpty()) {
            account = accountList.get(0);
        }

        return account;
    }

    @Override
    public synchronized void updateAccount(Account account) {

        hibernateTemplate.update(account);
    }

    @Override
    public long getAccountsByStatusCount(int status) {

        PreparedJDBCQuery query = getAccountsByStatusQuery(PreparedJDBCQuery.QUERY_TYPE_COUNT, status);

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Long.class);
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    public Map<String, String> getAccountNamesMap(long languageId) {

        Map<String, String> accountNamesMap = new ConcurrentHashMap<>();

        Session currentSession = sessionFactory.getCurrentSession();

        List<Account> accountNamesList = new ArrayList<>();

        accountNamesList = currentSession.createQuery("FROM Account a where a.isDeleted=:is_deleted and a.status=:status").setParameter("is_deleted", false)
            .setParameter("status", ServerConstants.ACCOUNT_STATUS_ACTIVE).list();

        if (accountNamesList != null && !accountNamesList.isEmpty()) {

            for (Account account : accountNamesList) {

                if (account.getType() == ServerConstants.ACCOUNT_TYPE_COMPANY) {
                    accountNamesMap.put(account.getCode(),
                        account.getCompanyName() + " - " + (languageId == ServerConstants.LANGUAGE_EN_ID ? "Company" : "Entreprise") + " - " + account.getNumber());

                } else if (account.getType() == ServerConstants.ACCOUNT_TYPE_INDIVIDUAL) {
                    accountNamesMap.put(account.getCode(), account.getFirstName() + " " + account.getLastName() + " - "
                        + (languageId == ServerConstants.LANGUAGE_EN_ID ? "Individual" : "Individuel") + " - " + account.getNumber());
                }
            }
        }

        return accountNamesMap;
    }

    private PreparedJDBCQuery getAccountsByStatusQuery(int queryType, int status) {

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        if (queryType == PreparedJDBCQuery.QUERY_TYPE_COUNT) {

            query.append("SELECT COUNT(accounts.id)");
            query.append(" FROM pad.accounts accounts  ");

        } else if (queryType == PreparedJDBCQuery.QUERY_TYPE_SELECT) {

            query.append("SELECT * FROM pad.accounts accounts ");
        }

        query.append(" WHERE accounts.status = ? ");
        query.addQueryParameter(status);

        return query;
    }

    private PreparedJDBCQuery getAccountsQuery(int queryType, AccountJson accountJson) {

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        if (queryType == PreparedJDBCQuery.QUERY_TYPE_COUNT) {

            query.append("SELECT COUNT(accounts.id)");
            query.append(" FROM pad.accounts accounts  ");

        } else if (queryType == PreparedJDBCQuery.QUERY_TYPE_SELECT) {

            query.append("SELECT * FROM pad.accounts accounts ");
        }

        query.append(" WHERE (1=1) ");

        if (accountJson.getNumber() != null && accountJson.getNumber() >= ServerConstants.ACCOUNT_NUMBER_MIN && accountJson.getNumber() <= ServerConstants.ACCOUNT_NUMBER_MAX) {
            query.append(" AND accounts.number = ?");
            query.addQueryParameter(accountJson.getNumber());
        }

        if (StringUtils.isNotBlank(accountJson.getAccountName())) {
            query.append(" AND ((accounts.type = 1 AND accounts.company_name LIKE ?) OR (accounts.type = 2 AND CONCAT(accounts.first_name, ' ', accounts.last_name) LIKE ?))");
            query.addQueryParameter("%" + accountJson.getAccountName() + "%");
            query.addQueryParameter("%" + accountJson.getAccountName() + "%");
        }

        if (accountJson.getStatus() != null) {
            query.append(" AND accounts.status = ?");
            query.addQueryParameter(accountJson.getStatus());
        }

        if (StringUtils.isNotBlank(accountJson.getCompanyName())) {
            query.append(" AND accounts.company_name LIKE ?");
            query.addQueryParameter("%" + accountJson.getCompanyName() + "%");
        }
        if (StringUtils.isNotBlank(accountJson.getMsisdn())) {
            query.append(" AND accounts.msisdn LIKE ?");
            query.addQueryParameter("%" + accountJson.getMsisdn() + "%");
        }

        return query;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Account> getActiveAccounts() {

        Session currentSession = sessionFactory.getCurrentSession();

        List<Account> accountList = new ArrayList<>();

        accountList = currentSession.createQuery("FROM Account a where a.isDeleted=:is_deleted and a.status=:status ORDER BY a.paymentTermsType DESC")
            .setParameter("is_deleted", false).setParameter("status", ServerConstants.ACCOUNT_STATUS_ACTIVE).list();

        return accountList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Account getAccountByCompanyShortName(String companyShortName) {

        Account account = null;

        List<Account> accountList = (List<Account>) hibernateTemplate.findByNamedParam("FROM Account WHERE companyShortName = :companyShortName", "companyShortName",
            companyShortName);

        if (accountList != null && !accountList.isEmpty()) {
            account = accountList.get(0);
        }

        return account;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public long getAccountNumberForAccount(long accountId) throws Exception {

        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("FROM AccountNumber WHERE accountId=:id").setParameter("id", accountId).setMaxResults(1);

        List<AccountNumber> results = query.list();

        if (results.isEmpty())
            throw new Exception("getAccountNumberForAccount#accountId: " + accountId + ". AccountNumber is not found by accountId...");

        AccountNumber accountNumber = results.get(0);

        return accountNumber.getNumber();
    }

    @Override
    public BigDecimal getOpeningBalance(String code, Date dateCreated) throws PADException {

        Session currentSession = sessionFactory.getCurrentSession();
        Account account = this.getAccountByCode(code);
        if (account == null)
            throw new PADException(ServerResponseConstants.ACCOUNT_NOT_FOUND_CODE, ServerResponseConstants.ACCOUNT_NOT_FOUND_TEXT, "Account was not found by code");

        Query<AccountBalance> query = currentSession.createQuery("FROM AccountBalance WHERE accountId=:id AND dateCreated=:dateCreated", AccountBalance.class)
            .setParameter("id", account.getId()).setParameter("dateCreated", dateCreated).setMaxResults(1);

        List<AccountBalance> results = query.list();
        return results.isEmpty() ? null : results.get(0).getAmountBalance();
    }

    @Override
    public void notifyAccountWhenLowBalance(Account account, BigDecimal newAmountBalance) {
        if (account.getIsSendLowAccountBalanceWarn() && newAmountBalance.compareTo(account.getAmountLowAccountBalanceWarn()) <= 0) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("accountBalance", newAmountBalance);
            try {
                sendLowAccountBalanceWarning(account, params);
            } catch (PADException pade) {
                // notification could not be sent due to a network problem, continue with the execution
                logger.error("notifyAccountWhenLowBalance#sendLowAccountBalanceWarning###PADException: ", pade);
            } catch (Exception e) {
                logger.error("notifyAccountWhenLowBalance#sendLowAccountBalanceWarning###Exception: ", e);
            }
        }
    }

    private void sendLowAccountBalanceWarning(Account account, HashMap<String, Object> params) throws PADException {
        if (StringUtils.isNotBlank(account.getEmailListInvoiceStatement())) {
            Email email = initEmail(account);
            params.put("accountName", account.getType() == ServerConstants.ACCOUNT_TYPE_COMPANY ? account.getCompanyName() : account.getFirstName());
            emailService.scheduleEmailByType(email, ServerConstants.EMAIL_LOW_ACCOUNT_BALANCE_WARNING_TYPE, params);
        } else {

            String msisdn = ServerConstants.DEFAULT_STRING;

            if (StringUtils.isNotBlank(account.getMsisdn())) {
                msisdn = account.getMsisdn();
            } else if (ServerUtil.isMsisdn(account.getCompanyTelephone())) {
                msisdn = account.getCompanyTelephone();
            }

            if (StringUtils.isNotBlank(msisdn)) {
                Sms sms = initSms(account, msisdn);
                params.put("accountName", account.getFirstName());
                smsService.scheduleSmsByType(sms, ServerConstants.SMS_LOW_ACCOUNT_BALANCE_WARNING_TYPE, params);
            }
        }
    }

    private Email initEmail(Account account) {
        Email email = new Email();
        email.setEmailTo(account.getEmailListInvoiceStatement());
        email.setLanguageId(account.getLanguageId());
        email.setAccountId(account.getId());
        email.setAttachmentPath(ServerConstants.DEFAULT_STRING);
        email.setDateScheduled(new Date());
        email.setMissionId(ServerConstants.DEFAULT_LONG);
        email.setTripId(ServerConstants.DEFAULT_LONG);
        return email;
    }

    private Sms initSms(Account account, String msisdn) {
        Sms scheduleSms = new Sms();
        scheduleSms.setLanguageId(account.getLanguageId());
        scheduleSms.setAccountId(account.getId());
        scheduleSms.setMissionId(ServerConstants.DEFAULT_LONG);
        scheduleSms.setTripId(ServerConstants.DEFAULT_LONG);
        scheduleSms.setMsisdn(msisdn);
        scheduleSms.setDateScheduled(new Date());
        return scheduleSms;
    }
}
