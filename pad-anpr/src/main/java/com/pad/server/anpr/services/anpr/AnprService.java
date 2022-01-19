package com.pad.server.anpr.services.anpr;

import java.text.ParseException;

import com.pad.server.base.entities.Anpr;
import com.pad.server.base.entities.AnprEntryScheduler;
import com.pad.server.base.entities.AnprScheduler;
import com.pad.server.base.exceptions.PADException;

public interface AnprService {

    public void resetAnprSchedulerProcessing(AnprScheduler anprScheduler);

    public void resetAnprEntrySchedulerProcessing(AnprEntryScheduler anprEntryScheduler);

    public void scheduleAnprEventLog() throws PADException;

    public void saveAnprEventLog(Anpr anpr) throws PADException;

    public void createWhiteList(Anpr anpr) throws PADException, ParseException;

    public void deleteWhiteList(Anpr anpr) throws PADException;

    public void updateWhiteListStatus(Anpr anpr) throws PADException;

    public void updateScheduledAnpr(Anpr anpr);

    public void updateScheduledAnprEntryLog(AnprEntryScheduler anprEntryScheduler);

    public void deleteScheduledAnpr(long anprId);

    public void deleteAnprEntryScheduler(long anprEntrySchedulerId);

    public void updateAnpr(Anpr anpr);

    public void updateAnprEntryLog(AnprEntryScheduler anprEntryScheduler);

    public void processAnprEventScheduler(AnprEntryScheduler anprEntryScheduler);
}
