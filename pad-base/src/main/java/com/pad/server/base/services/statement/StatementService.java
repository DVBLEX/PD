package com.pad.server.base.services.statement;

import com.pad.server.base.entities.Statement;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.AccountStatementJson;

import java.util.List;

public interface StatementService {

    public void saveStatement(Statement statement);

    public void updateStatement(Statement statement);

    public void chargeParkingFee(Trip trip, long loggedOperatorId);

    public long getAccountStatementsCount(AccountStatementJson accountStatementJson) throws PADException;

    public List<AccountStatementJson> getAccountStatementsList(AccountStatementJson accountStatementJson) throws PADException;
}
