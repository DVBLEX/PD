package com.pad.server.api.services.api;

import com.pad.server.base.entities.Operator;
import com.pad.server.base.exceptions.PADException;

public interface ApiService {

    public Operator validateApiRequest(String authHeader, String remoteAddr) throws PADException, Exception;

}
