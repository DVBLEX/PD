package com.pad.server.base.tasks;

import java.util.Date;

import org.apache.log4j.Logger;

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.entities.Sms;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.services.sms.SmsService;

public class SmsTaskExecutor implements Runnable {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(SmsTaskExecutor.class);

    private SmsService          smsService;

    private Sms                 sms;

    public SmsTaskExecutor(Sms sms, SmsService smsService) {
        this.sms = sms;
        this.smsService = smsService;
    }

    @Override
    public void run() {

        try {
            // logger.info("run#smsId=" + sms.getId());

            smsService.sendBulkSms(sms);

            sms.setIsProcessed(ServerConstants.PROCESS_PROCESSED);
            smsService.updateSms(sms);
            smsService.deleteScheduledSms(sms.getId());

        } catch (PADException pade) {

            sms.setResponseCode(pade.getResponseCode());
            sms.setResponseText(pade.getResponseText());

            if (pade.getResponseCode() == 3105 || pade.getResponseCode() == 3106) {

                sms.setIsProcessed(ServerConstants.PROCESS_PROCESSED);
                smsService.updateSms(sms);
                smsService.deleteScheduledSms(sms.getId());
            } else {

                sms.setRetryCount(sms.getRetryCount() + 1);
                if (sms.getRetryCount() == 1) {
                    sms.setIsProcessed(ServerConstants.PROCESS_NOTPROCESSED);
                    sms.setDateScheduled(new Date(sms.getDateScheduled().getTime() + 5l * 1000l));
                    smsService.updateScheduledSms(sms);
                } else if (sms.getRetryCount() < 4) {
                    sms.setIsProcessed(ServerConstants.PROCESS_NOTPROCESSED);
                    sms.setDateScheduled(new Date(sms.getDateScheduled().getTime() + 30l * 1000l));
                    smsService.updateScheduledSms(sms);
                } else {
                    smsService.updateScheduledSms(sms);
                    // smsService.updateSms(sms);
                    // smsService.deleteScheduledSms(sms.getId());
                }
            }
        }
    }
}
