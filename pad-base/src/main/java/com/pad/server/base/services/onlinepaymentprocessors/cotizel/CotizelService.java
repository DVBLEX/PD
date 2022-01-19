package com.pad.server.base.services.onlinepaymentprocessors.cotizel;

import com.pad.server.base.entities.OnlinePayment;
import com.pad.server.base.exceptions.PADOnlinePaymentException;

public interface CotizelService {

    public void processOnlinePayment(OnlinePayment onlinePayment) throws PADOnlinePaymentException;

}
