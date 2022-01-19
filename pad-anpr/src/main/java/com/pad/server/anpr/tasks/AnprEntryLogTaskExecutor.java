package com.pad.server.anpr.tasks;

import java.util.Date;

import org.apache.log4j.Logger;

import com.pad.server.anpr.common.ServerConstants;
import com.pad.server.anpr.services.anpr.AnprService;
import com.pad.server.base.entities.Anpr;
import com.pad.server.base.exceptions.PADException;

public class AnprEntryLogTaskExecutor implements Runnable {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(AnprEntryLogTaskExecutor.class);

    private AnprService         anprService;

    private Anpr                anpr;

    public AnprEntryLogTaskExecutor(Anpr anpr, AnprService anprService) {
        this.anpr = anpr;
        this.anprService = anprService;
    }

    @Override
    public void run() {

        try {
            switch (anpr.getRequestType()) {
                case ServerConstants.REQUEST_TYPE_ANPR_API_GET_ENTRY_LOG:
                    anprService.saveAnprEventLog(anpr);

                    anpr.setIsProcessed(ServerConstants.PROCESS_PROCESSED);
                    anprService.updateAnpr(anpr);
                    anprService.deleteScheduledAnpr(anpr.getId());

                    anprService.scheduleAnprEventLog();

                    break;

                default:
                    throw new PADException();
            }

        } catch (PADException pade) {

            anpr.setRetryCount(anpr.getRetryCount() + 1);
            anpr.setResponseCode(pade.getResponseCode());
            anpr.setResponseText(pade.getResponseText());

            if (anpr.getRetryCount() == 1) {
                anpr.setIsProcessed(ServerConstants.PROCESS_NOTPROCESSED);
                anpr.setDateScheduled(new Date(anpr.getDateScheduled().getTime() + 10l * 1000l));
                anprService.updateScheduledAnpr(anpr);
            } else {
                anpr.setIsProcessed(ServerConstants.PROCESS_NOTPROCESSED);
                anpr.setDateScheduled(new Date(anpr.getDateScheduled().getTime() + 60l * 1000l));
                anprService.updateScheduledAnpr(anpr);
            }

        }
    }
}
