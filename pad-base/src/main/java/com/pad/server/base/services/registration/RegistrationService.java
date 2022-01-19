package com.pad.server.base.services.registration;

import java.sql.SQLException;

import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.EmailCodeRequest;
import com.pad.server.base.entities.SMSCodeRequest;
import com.pad.server.base.exceptions.PADException;

public interface RegistrationService {

    public void sendRegistrationCodeEmail(String emailTo, String firstName, String lastName, long languageId) throws PADException;

    public void sendRegistrationCodeSMS(String msisdnTo, long languageId) throws PADException;

    public boolean verifyRegistrationCodeEmail(String email, String code);

    public boolean verifyRegistrationCodeSMS(final String msisdn, final String code);

    public boolean verifyRegistrationCodeSMSForPasswordReset(final String msisdn, final String code);

    public void setEmailToVerified(String email, String code, String token);

    public void setMsisdnToVerified(String msisdn, String code, String token);

    public boolean isEmailVerifiedWithinHours(String email, String token, int hours);

    public boolean isMsisdnVerifiedWithinHours(String msisdn, String token, int hours);

    public boolean isCountEmailCodeSentUnderLimit(String email, int codeSentLimit);

    public boolean isCountSmsCodeSentUnderLimit(String msisdn, int codeSentLimit);

    public boolean isCountEmailVerifiedUnderLimit(String email, int verifiedLimit);

    public boolean isCountMsidnVerifiedUnderLimit(String msisdn, int verifiedLimit);

    public EmailCodeRequest getEmailCodeRequest(String email) throws SQLException;

    public SMSCodeRequest getSmsCodeRequest(String msisdn) throws SQLException;

    public void processRegistration(String email, String msisdn, String firstName, String lastName, String password, Account account) throws PADException;

    public void deleteEmailCodeRequest(String email) throws SQLException;

    public void deleteSmsCodeRequest(String msisdn) throws SQLException;

}
