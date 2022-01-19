package com.pad.server.web.services.padanpr;

import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.AnprParameterJson;
import com.pad.server.base.jsonentities.api.PortOperatorTransactionTypeJson;
import com.pad.server.base.jsonentities.api.SystemParamJson;

public interface PadAnprService {

    public void updateAnprParameterPadanpr(AnprParameterJson anprParameterJson) throws PADException;

    public void updateTransactionTypeFlagPadanpr(PortOperatorTransactionTypeJson portOperatorTransactionTypeJson) throws PADException;

    public void updateSystemParameterPadanpr(SystemParamJson systemParamJson) throws PADException;
}
