package com.pad.server.base.services.lane;

import java.util.List;

import com.pad.server.base.entities.Lane;
import com.pad.server.base.jsonentities.api.LaneJson;

public interface LaneService {

    public long getCount(LaneJson laneJson);

    public List<LaneJson> getList(LaneJson laneJson);

    public Lane getLaneByDeviceId(String deviceId);

    public Lane getLaneByLaneId(long laneId);

    public Lane getLaneById(long id);

    public void updateLane(Lane lane);

    public void saveLane(Lane lane);

    public List<LaneJson> getParkingEntryLanes();

}
