package com.pad.server.base.services.sms;

import java.util.HashMap;

import com.pad.server.base.entities.Sms;
import com.pad.server.base.exceptions.PADException;

public interface SmsService {

    public void sendBulkSms(Sms sms) throws PADException;

    public void scheduleSmsById(Sms sms, long templateId, HashMap<String, Object> params) throws PADException;

    public void scheduleSmsByType(Sms sms, long templateType, HashMap<String, Object> params) throws PADException;

    public void updateScheduledSms(Sms sms);

    public void deleteScheduledSms(long smsId);

    public void updateSms(Sms sms);

    public boolean isParkingExitSmsEligibleToSend(long tripId);

    public String getParkingExitSMSMessage(long languageId);
}
