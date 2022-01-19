package com.pad.server.base.services.session;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.pad.server.base.entities.Session;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.SessionJson;

public interface SessionService {

    public void saveSession(Session session);

    public void updateSession(Session session);

    public long getSessionCount(SessionJson sessionJson) throws PADException;

    public List<SessionJson> getSessionList(SessionJson sessionJson, String sortColumn, boolean sortAsc, int startLimit, int endLimit) throws PADException;

    public void addSession(long kioskOperatorId, int type, long laneId, int laneNumber, BigDecimal amountStart, String cashBagNumber, long operatorId)
        throws PADException, PADValidationException;

    public Session getLastSessionByKioskOperatorId(long kioskOperatorId);

    public Session getSessionById(long sessionId);

    public Session getSessionByCode(String code);

    public long getSessionIdByLaneNumberAndDateStartEnd(int laneNumber, Date date);

    public void performKioskSessionValidationChecks(Session kioskSession) throws PADException;
}
