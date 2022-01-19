package com.pad.server.base.services.mission;

import java.util.Date;
import java.util.List;

import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.Mission;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.MissionJson;

public interface MissionService {

    public long getMissionCount(Account account, MissionJson missionJson, Date dateMissionStart, Date dateMissionEnd) throws Exception;

    public List<MissionJson> getMissionList(Account account, MissionJson missionJson, Date dateMissionStart, Date dateMissionEnd);

    public void saveMission(Mission mission);

    public void updateMission(Mission mission);

    public Mission getMissionById(Long id);

    public Mission getMissionByCode(String code);

    public Mission getMissionByReferenceNumber(String referenceNumber);

    public Mission getActiveMissionByReferenceNumberAndAccountId(String referenceNumber, long accountId, int portOperatorId);

    public List<String> getMissionReferenceNumbersByAccountIdAndPortOperator(long accountId, int portOperatorId);

    public Mission getMissionByContainerId(String containerId);

    public void saveMissionTrip(Trip trip);

    public Mission getMissionByPortOperatorIdAndTransactionTypeAndReferenceNumber(int portOperatorId, int transactionType, String referenceNumber);

    public Mission getMissionByPortOperatorIdAndReferenceNumberAndAccountId(int portOperatorId, String referenceNumber, long accountId);

    public void createMission(MissionJson missionJson, long loggedOperatorId, boolean isApi) throws PADValidationException, PADException;

    public List<Mission> getExpiredMissions(int portOperatorId, int transactionType, Date dateMissionExpiry);

    public void cancelMission(MissionJson missionJson, long loggedOperatorId) throws PADValidationException, PADException;
}
