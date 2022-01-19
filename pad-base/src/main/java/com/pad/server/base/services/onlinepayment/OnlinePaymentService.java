package com.pad.server.base.services.onlinepayment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pad.server.base.entities.OnlinePayment;
import com.pad.server.base.entities.OnlinePaymentParameter;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADOnlinePaymentException;
import com.pad.server.base.jsonentities.api.OnlinePaymentParameterJson;
import com.pad.server.base.jsonentities.api.PaymentJson;

public interface OnlinePaymentService {

    public Map<Long, OnlinePaymentParameter> getOnlinePaymentParameterMap();

    public OnlinePaymentParameter getOnlinePaymentParameterFromMap(long mnoId);

    public void saveOnlinePayment(OnlinePayment onlinePayment) throws PADOnlinePaymentException;

    public void updateOnlinePayment(OnlinePayment onlinePayment) throws PADOnlinePaymentException;

    public String processOnlinePayment(Trip trip, PaymentJson paymentJson, BigDecimal amountPayment, long paymentId) throws PADOnlinePaymentException;

    public OnlinePayment getOnlinePaymentByAggregatorReferenceAndGuIdAggregatorReference(String aggregatorReference, String guIDAggregatorReference);

    public OnlinePayment getOnlinePaymentByCode(String code);

    public OnlinePayment getOnlinePaymentById(long id);

    public boolean isOnlinePaymentSuccessful(OnlinePayment onlinePayment);

    public boolean isOnlinePaymentFailure(OnlinePayment onlinePayment);

    public Map<Integer, Boolean> getMobilePaymentOptionsMap();

    public List<OnlinePaymentParameterJson> getOnlinePaymentParameterJsonList();

    public void updateOnlinePaymentParameter(OnlinePaymentParameterJson onlinePaymentParameterJson) throws PADException;
}
