package com.pad.server.api.services.trip;

import com.pad.server.base.entities.Operator;
import com.pad.server.base.entities.PortOperatorTripsApi;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.TripApiJson;
import com.pad.server.base.jsonentities.api.TripApiResponseJson;

public interface TripService {

    public TripApiResponseJson addTripApi(TripApiJson tripApiJson, Operator operator) throws PADException, PADValidationException, Exception;

    public TripApiResponseJson updateTripApi(TripApiJson tripApiJson, Operator operator) throws PADException, Exception;

    public TripApiResponseJson cancelTripApi(TripApiJson tripApiJson, Operator operator) throws PADException, Exception;

    public PortOperatorTripsApi saveRequest(TripApiJson tripApiJson, String type);

    public void saveResponse(long portOperatorTripsApiId, int responseCode, String responseText, String tripCode);
}
