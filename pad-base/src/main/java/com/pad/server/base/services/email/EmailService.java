package com.pad.server.base.services.email;

import java.util.HashMap;
import java.util.List;

import com.pad.server.base.entities.Email;
import com.pad.server.base.entities.NameValuePair;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.TripApiJson;

public interface EmailService {

    public static final String EMAIL_TYPE_BLANK     = "";
    public static final String EMAIL_TYPE_ALERT     = "ALERT";
    public static final String EMAIL_TYPE_SUCCESS   = "SUCCESS";
    public static final String EMAIL_TYPE_FAILURE   = "FAILURE";
    public static final String EMAIL_TYPE_EXCEPTION = "EXCEPTION";

    public void sendSystemEmail(String subject, String emailType, String headerContent, List<NameValuePair> nameValuePairs, String footerContent);

    public String getContent(String emailType, String headerContent, List<NameValuePair> nameValuePairs, String footerContent);

    public void sendEmail(Email email) throws PADException;

    public void updateScheduledEmail(Email email);

    public void deleteScheduledEmail(long emailId);

    public void updateEmail(Email email);

    public boolean getIsLive();

    public void scheduleEmailByType(Email email, long templateType, HashMap<String, Object> params) throws PADException;

    public void sendTransporterShortNameErrorEmailNotification(TripApiJson tripApiJson, int transactionType, long languageId, long portOpeartorId, String emailTo,
        long templateType) throws PADException;

}
