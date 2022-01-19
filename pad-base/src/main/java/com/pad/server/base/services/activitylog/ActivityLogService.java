package com.pad.server.base.services.activitylog;

import com.pad.server.base.entities.ActivityLog;

public interface ActivityLogService {

    public void saveActivityLog(ActivityLog activityLog);

    public void saveActivityLog(long activityId, long operatorId);

    public void saveActivityLog(long activityId, long operatorId, long accountId);

    public void saveActivityLogMission(long activityId, long operatorId, long missionId);

    public void saveActivityLogTrip(long activityId, long operatorId, long tripId);

    public void saveActivityLogParking(long activityId, long operatorId, long parkingId);

    public void saveActivityLogPortAccess(long activityId, long operatorId, long portAccessId);

    public void saveActivityLogPortAccessWhitelist(long activityId, long operatorId, long portAccessWhitelistId);

    public void saveActivityLogStatement(long activityId, long operatorId, long statementId);

    public void saveActivityLogSession(long activityId, long operatorId, long sessionId);

    public void saveActivityLogOperator(long activityId, long operatorId, long newUpdatedOperatorId);

    public void saveActivityLogTrip(long activityId, long operatorId, String regNumber, long portOperatorId, int transactionType, String referenceNumber);

    public void saveActivityLogParkingAutoRelease(long activityId, long operatorId, long portOperatorId, int transactionType, boolean isAutoReleaseOn);

    public void saveActivityLogParkingManualRelease(long activityId, long operatorId, long portOperatorId, int transactionType, int vehicleReleaseCount);

    public void saveActivityLogWithObjectJson(long activityId, long operatorId, long accountId, String jsonObj);
}
