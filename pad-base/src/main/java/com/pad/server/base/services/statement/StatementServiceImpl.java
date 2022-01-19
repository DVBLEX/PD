package com.pad.server.base.services.statement;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.PreparedJDBCQuery;
import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.Statement;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.AccountStatementJson;
import com.pad.server.base.services.account.AccountService;

@Service
@Transactional
public class StatementServiceImpl implements StatementService {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Autowired
    private JdbcTemplate      jdbcTemplate;

    @Autowired
    private AccountService    accountService;

    @Override
    public void saveStatement(Statement statement) {

        hibernateTemplate.save(statement);
    }

    @Override
    public void updateStatement(Statement statement) {

        hibernateTemplate.update(statement);
    }

    @Override
    @Transactional(rollbackFor = { Exception.class })
    public void chargeParkingFee(Trip trip, long loggedOperatorId) {

        Account account = accountService.getAccountById(trip.getAccountId());

        if (account != null) {

            BigDecimal accountBalanceAmount = account.getBalanceAmount();
            BigDecimal tripFeeAmount = trip.getFeeAmount();

            accountBalanceAmount = accountBalanceAmount.subtract(tripFeeAmount);

            createStatement(trip, loggedOperatorId, account.getId(), accountBalanceAmount, tripFeeAmount);

            accountService.notifyAccountWhenLowBalance(account, accountBalanceAmount);

            account.setBalanceAmount(accountBalanceAmount);
            account.setDateEdited(new Date());

            if (trip.getType() == ServerConstants.TRIP_TYPE_BOOKED && trip.getParkingPermissionId() > 0l
                && (trip.getStatus() == ServerConstants.TRIP_STATUS_APPROVED || trip.getStatus() == ServerConstants.TRIP_STATUS_IN_TRANSIT)) {

                // Amount in hold is increased when:
                // 1. trip is pre-booked and approved and it has access to enter the parking / port area via ANPR (ANPR permission was already created)
                // 2. trip allowed multiple entries to the parking area, so at the point of the authorized port entry, create another approved trip that will be used during
                // re-entry to the parking area

                // Amount in hold is decreased when:
                // 1. at the point of parking / port entry when trip fee is charged & when the trip status was approved (authorized to enter the car park or the port directly) OR
                // in transit (authorized to enter the port after parking exit authorization).
                // If trip exited prematurely and entered parking after 1 hour then trip fee will be charged but amount hold will not be decreased
                // 2. trip eligible to go direct to port & enter it multiple times is aborted by a transporter and the trip had ANPR entry permission
                // 3. trip eligible to enter the parking area multiple times is cancelled by a transporter and the trip had ANPR entry permission
                // 4. trip eligible to go direct to port once is cancelled by a transporter and the trip had ANPR entry permission
                // 5. trip's mission had expired and the trip had status approved & pre-booked and it had ANPR entry permission
                // 6. when a direct to port trip goes through the car park first and remains in transit until the expiry job picks it up to mark it 'in transit expired'

                BigDecimal accountAmountHold = account.getAmountHold();
                accountAmountHold = accountAmountHold.subtract(tripFeeAmount);

                account.setAmountHold(accountAmountHold);
            }

            accountService.updateAccount(account);

            trip.setFeePaid(true);
            trip.setDateFeePaid(new Date());
            trip.setDateEdited(new Date());
        }
    }

    private void createStatement(Trip trip, long loggedOperatorId, long accountId, BigDecimal accountBalanceAmount, BigDecimal tripFeeAmount) {
        Statement statement = new Statement();
        statement.setCode(SecurityUtil.generateUniqueCode());
        statement.setType(ServerConstants.PAYMENT_TYPE_TRIP_FEE);
        statement.setPaymentId(ServerConstants.DEFAULT_LONG);
        statement.setAccountId(accountId);
        statement.setMissionId(trip.getMission().getId());
        statement.setTripId(trip.getId());
        statement.setOperatorId(loggedOperatorId);
        statement.setCurrency(ServerConstants.CURRENCY_CFA_FRANC);
        statement.setAmountCredit(BigDecimal.ZERO);
        statement.setAmountDebit(tripFeeAmount);
        statement.setAmountRunningBalance(accountBalanceAmount);
        statement.setNotes(ServerConstants.DEFAULT_STRING);
        statement.setDateCreated(new Date());
        statement.setDateEdited(statement.getDateCreated());
        saveStatement(statement);
    }

    @Override
    public long getAccountStatementsCount(AccountStatementJson accountStatementJson) throws PADException {
        PreparedJDBCQuery query = getAccountStatementsQuery(PreparedJDBCQuery.QUERY_TYPE_COUNT, accountStatementJson);

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Long.class);
    }

    @Override
    public List<AccountStatementJson> getAccountStatementsList(AccountStatementJson accountStatementJson) throws PADException {

        PreparedJDBCQuery query = getAccountStatementsQuery(PreparedJDBCQuery.QUERY_TYPE_SELECT, accountStatementJson);

        query.setSortParameters(accountStatementJson.getSortColumn(), accountStatementJson.getSortAsc(), "s", ServerConstants.DEFAULT_SORTING_FIELD, " DESC");
        query.setLimitParameters(accountStatementJson.getCurrentPage(), accountStatementJson.getPageCount());

        List<AccountStatementJson> statementList = new ArrayList<>();
        jdbcTemplate.query(query.getQueryString(), rs -> {
            AccountStatementJson statementJson = new AccountStatementJson();

            statementJson.setDateCreated(rs.getTimestamp("date_created"));
            try {
                statementJson.setDateCreatedString(ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmm, rs.getTimestamp("date_created")));
            } catch (ParseException e) {
                statementJson.setDateCreatedString("");
            }
            statementJson.setType(rs.getInt("type"));
            statementJson.setPortOperatorId(rs.getInt("port_operator_id"));
            statementJson.setIndependentPortOperatorId(rs.getInt("independent_port_operator_id"));
            statementJson.setTransactionType(rs.getString("transaction_type"));
            statementJson.setReferenceNumber(rs.getString("reference_number"));
            statementJson.setRegistration(rs.getString("vehicle_registration"));
            statementJson.setCountry(rs.getString("vehicle_registration_country_iso"));
            statementJson.setCredit(rs.getBigDecimal("amount_credit"));
            statementJson.setDebit(rs.getBigDecimal("amount_debit"));
            statementJson.setBalance(rs.getBigDecimal("amount_running_balance"));
            statementJson.setOpeningBalance(false);

            statementList.add(statementJson);
        }, query.getQueryParameters());

        return statementList;
    }

    private PreparedJDBCQuery getAccountStatementsQuery(int queryType, AccountStatementJson accountStatementJson) throws PADException {

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        if (queryType == PreparedJDBCQuery.QUERY_TYPE_COUNT) {
            query.append("SELECT COUNT(s.id)");
            query.append(" FROM pad.statements s ");
        } else if (queryType == PreparedJDBCQuery.QUERY_TYPE_SELECT) {
            query.append("SELECT s.date_created, s.type, s.amount_credit, s.amount_debit, s.amount_running_balance, ");
            query.append("t.port_operator_id, t.independent_port_operator_id, t.transaction_type, t.reference_number, ");
            query.append("t.vehicle_registration, t.vehicle_registration_country_iso ");
            query.append("FROM pad.statements s  ");
            query.append("LEFT JOIN pad.trips t ON t.id = s.trip_id ");
        }
        query.append("WHERE s.account_id = ? AND s.date_created >= ? AND s.date_created < ?");

        Account account = accountService.getAccountByCode(accountStatementJson.getAccountCode());
        query.setAccountParameters(account, accountStatementJson.getDateFromString(), accountStatementJson.getDateToString());

        return query;
    }
}
