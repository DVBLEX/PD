package com.pad.server.base.services.operator;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.pad.server.base.entities.Operator;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.OperatorJson;

public interface OperatorService {

    public Operator getOperator(String username);

    public Operator getOperatorById(long id);

    public Operator getOperatorByCode(String code);

    public Operator getOperatorByAccountId(long accountId);
    
    public List<Operator> getOperatorsByAccountId(long accountId);

    public String getValidOperatorUsername(String username);

    public List<OperatorJson> getSystemOperatorList(String firstName, String lastName, Integer role, long accountId, boolean isLimit, String sortColumn, boolean sortAsc,
        int startLimit, int endLimit) throws SQLException;

    public Long getSystemOperatorCount(String firstName, String lastName, Integer role, long accountId);

    public void updateOperator(Operator operator);

    public void saveOperator(Operator operator);

    public void deleteOperator(Operator operator);

    public void sendPasswdForgotEmail(String email, long languageId) throws PADException, UnsupportedEncodingException;

    public void sendPasswdForgotSms(String msisdn, long languageId) throws PADException;

    public void sendPasswdSetUpEmail(String email, long languageId, Operator operator) throws PADException, UnsupportedEncodingException;

    public void sendPasswdSetUpSms(String msisdn, long languageId, Operator operator) throws PADException, UnsupportedEncodingException;

    public Operator addOperator(long accountId, OperatorJson operatorJson, String msisdn, long languageId, long operatorId) throws PADException, PADValidationException;

    public Map<String, String> getKioskOperatorNamesMap();

    public void unlockOperator(Operator operator);
}
