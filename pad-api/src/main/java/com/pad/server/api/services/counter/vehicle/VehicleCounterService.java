package com.pad.server.api.services.counter.vehicle;

import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.counter.VehicleCounterRequestJson;

public interface VehicleCounterService {

    public void log(VehicleCounterRequestJson vehicleCounterRequestJson) throws PADException;

}
