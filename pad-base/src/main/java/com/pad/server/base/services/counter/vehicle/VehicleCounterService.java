package com.pad.server.base.services.counter.vehicle;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.pad.server.base.entities.NameValuePair;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.counter.VehicleCounterJson;
import com.pad.server.base.jsonentities.api.counter.VehicleCounterRequestJson;

public interface VehicleCounterService {

    public long getCount(VehicleCounterJson vehicleCounterJson);

    public List<VehicleCounterJson> getList(VehicleCounterJson vehicleCounterJson);

    public void log(VehicleCounterRequestJson vehicleCounterRequestJson) throws PADException;

    public List<NameValuePair> getSessionOperatorsList();

    public void exportVehicleCounter(List<VehicleCounterJson> parkingList, OutputStream os) throws IOException;

}
