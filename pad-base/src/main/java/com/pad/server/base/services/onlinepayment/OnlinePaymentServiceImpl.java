package com.pad.server.base.services.onlinepayment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.SecurityUtil;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.OnlinePayment;
import com.pad.server.base.entities.OnlinePaymentParameter;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADOnlinePaymentException;
import com.pad.server.base.jsonentities.api.OnlinePaymentParameterJson;
import com.pad.server.base.jsonentities.api.PaymentJson;
import com.pad.server.base.services.onlinepaymentprocessors.cotizel.CotizelService;

@Service
@Transactional
public class OnlinePaymentServiceImpl implements OnlinePaymentService {

    private static final Logger               logger                     = Logger.getLogger(OnlinePaymentServiceImpl.class);

    @Autowired
    private CotizelService                    cotizelService;

    @Autowired
    private HibernateTemplate                 hibernateTemplate;

    @Autowired
    private SessionFactory                    sessionFactory;

    private List<OnlinePaymentParameter>      onlinePaymentParameterList = new CopyOnWriteArrayList<>();

    private Map<Long, OnlinePaymentParameter> onlinePaymentParameterMap  = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {

        onlinePaymentParameterList = (List<OnlinePaymentParameter>) hibernateTemplate.find("FROM OnlinePaymentParameter");
        if (onlinePaymentParameterList != null && !onlinePaymentParameterList.isEmpty()) {
            for (OnlinePaymentParameter onlinePaymentParameter : onlinePaymentParameterList) {
                logger.info("init###" + onlinePaymentParameter.toString());
                onlinePaymentParameterMap.put(onlinePaymentParameter.getMnoId(), onlinePaymentParameter);
            }
        }
    }

    @Override
    public Map<Long, OnlinePaymentParameter> getOnlinePaymentParameterMap() {
        return onlinePaymentParameterMap;
    }

    @Override
    public OnlinePaymentParameter getOnlinePaymentParameterFromMap(long mnoId) {
        return onlinePaymentParameterMap.get(mnoId);
    }

    @Override
    public void updateOnlinePaymentParameter(OnlinePaymentParameterJson onlinePaymentParameterJson) throws PADException {
        try {

            OnlinePaymentParameter onlinePaymentParameter = onlinePaymentParameterMap.get(onlinePaymentParameterJson.getMnoId());

            onlinePaymentParameter.setIsActive(onlinePaymentParameterJson.getIsActive());
            onlinePaymentParameter.setIsPrintReceipt(onlinePaymentParameterJson.getIsPrintReceipt());
            onlinePaymentParameter.setDateEdited(new Date());

            hibernateTemplate.update(onlinePaymentParameter);

            onlinePaymentParameterMap.put(onlinePaymentParameter.getMnoId(), onlinePaymentParameter);

            onlinePaymentParameterList = new ArrayList<>(onlinePaymentParameterMap.values());

        } catch (Exception e) {
            logger.error("updateOnlinePaymentParameter###Exception: ", e);
            throw new PADException(ServerResponseConstants.DATABASE_FAILURE_CODE, ServerResponseConstants.DATABASE_FAILURE_TEXT, "");
        }
    }

    @Override
    public void saveOnlinePayment(OnlinePayment onlinePayment) throws PADOnlinePaymentException {
        try {
            hibernateTemplate.save(onlinePayment);
        } catch (Exception e) {
            logger.error("saveOnlinePayment###Exception: ", e);
            throw new PADOnlinePaymentException(ServerResponseConstants.DATABASE_FAILURE_CODE, ServerResponseConstants.DATABASE_FAILURE_TEXT, "");
        }
    }

    @Override
    public void updateOnlinePayment(OnlinePayment onlinePayment) throws PADOnlinePaymentException {
        try {
            hibernateTemplate.update(onlinePayment);
        } catch (Exception e) {
            logger.error("updateOnlinePayment###Exception: ", e);
            throw new PADOnlinePaymentException(ServerResponseConstants.DATABASE_FAILURE_CODE, ServerResponseConstants.DATABASE_FAILURE_TEXT, "");
        }
    }

    @Override
    public String processOnlinePayment(Trip trip, PaymentJson paymentJson, BigDecimal amountPayment, long paymentId) throws PADOnlinePaymentException {

        OnlinePayment onlinePayment;

        try {

            onlinePayment = new OnlinePayment();
            onlinePayment.setCode(SecurityUtil.generateUniqueCode());
            onlinePayment.setClientId(ServerConstants.CLIENT_ID_AGS);
            onlinePayment.setAggregatorId(ServerConstants.AGGREGATOR_ID_COTIZEL);
            onlinePayment.setAccountId(trip.getAccountId());
            onlinePayment.setMissionId(trip.getMission().getId());
            onlinePayment.setTripId(trip.getId());
            onlinePayment.setDriverId(trip.getDriverId());
            onlinePayment.setPaymentId(paymentId);
            onlinePayment.setPaymentOption(paymentJson.getPaymentOption());
            onlinePayment.setFirstName(paymentJson.getFirstName());
            onlinePayment.setLastName(paymentJson.getLastName());
            onlinePayment.setMnoId(paymentJson.getMnoId());
            onlinePayment.setMsisdn(ServerUtil.getValidNumber(paymentJson.getMsisdn(), "processOnlinePayment"));
            onlinePayment.setCurrencyCode(ServerConstants.CURRENCY_CFA_FRANC);
            onlinePayment.setAmount(amountPayment);
            onlinePayment.setAmountAggregator(new BigDecimal(ServerConstants.ZERO_INT));
            onlinePayment.setFeeAggregator(new BigDecimal(ServerConstants.ZERO_INT));
            onlinePayment.setReferenceAggregator(ServerConstants.DEFAULT_STRING);
            onlinePayment.setTransactionAggregator(ServerConstants.DEFAULT_STRING);
            onlinePayment.setDateRequest(new Date());
            onlinePayment.setStatusAggregator(ServerConstants.DEFAULT_STRING);
            onlinePayment.setErrorAggregator(ServerConstants.DEFAULT_STRING);
            onlinePayment.setClientIdAggregator(ServerConstants.DEFAULT_STRING);
            onlinePayment.setGuIdAggregator(ServerConstants.DEFAULT_STRING);
            onlinePayment.setResponseCode(ServerConstants.DEFAULT_INT);
            onlinePayment.setRequestHash(ServerConstants.DEFAULT_STRING);
            onlinePayment.setResponseHash(ServerConstants.DEFAULT_STRING);
            onlinePayment.setResponseCallbackHash(ServerConstants.DEFAULT_STRING);

            saveOnlinePayment(onlinePayment);

            cotizelService.processOnlinePayment(onlinePayment);

        } catch (PADOnlinePaymentException pade) {
            throw pade;
        } catch (Exception e) {
            throw new PADOnlinePaymentException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "processOnlinePayment");
        }

        return onlinePayment.getCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public OnlinePayment getOnlinePaymentByAggregatorReferenceAndGuIdAggregatorReference(String aggregatorReference, String guIDAggregatorReference) {

        OnlinePayment onlinePayment = null;

        Session currentSession = sessionFactory.getCurrentSession();

        List<OnlinePayment> onlinePaymentList = currentSession
            .createQuery("FROM OnlinePayment op where op.referenceAggregator=:aggregator_reference and op.guIdAggregator=:gu_id_aggregator")
            .setParameter("aggregator_reference", aggregatorReference).setParameter("gu_id_aggregator", guIDAggregatorReference).list();

        if (onlinePaymentList != null && !onlinePaymentList.isEmpty()) {
            onlinePayment = onlinePaymentList.get(0);
        }

        return onlinePayment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public OnlinePayment getOnlinePaymentByCode(String code) {

        OnlinePayment onlinePayment = null;

        Session currentSession = sessionFactory.getCurrentSession();

        List<OnlinePayment> onlinePaymentList = currentSession.createQuery("FROM OnlinePayment op where op.code=:code").setParameter("code", code).list();

        if (onlinePaymentList != null && !onlinePaymentList.isEmpty()) {
            onlinePayment = onlinePaymentList.get(0);
        }

        return onlinePayment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public OnlinePayment getOnlinePaymentById(long id) {

        OnlinePayment onlinePayment = null;

        Session currentSession = sessionFactory.getCurrentSession();

        List<OnlinePayment> onlinePaymentList = currentSession.createQuery("FROM OnlinePayment op where op.id=:id").setParameter("id", id).list();

        if (onlinePaymentList != null && !onlinePaymentList.isEmpty()) {
            onlinePayment = onlinePaymentList.get(0);
        }

        return onlinePayment;
    }

    @Override
    public boolean isOnlinePaymentSuccessful(OnlinePayment onlinePayment) {

        return onlinePayment != null && onlinePayment.getStatusAggregator().equalsIgnoreCase(ServerConstants.COTIZEL_CALLBACK_STATUS_SUCCESS)
            && onlinePayment.getDatePaymentAggregator() != null && StringUtils.isNotBlank(onlinePayment.getTransactionAggregator());
    }

    @Override
    public boolean isOnlinePaymentFailure(OnlinePayment onlinePayment) {

        return onlinePayment != null && onlinePayment.getStatusAggregator().equalsIgnoreCase(ServerConstants.COTIZEL_CALLBACK_STATUS_FAILURE);
    }

    @Override
    public Map<Integer, Boolean> getMobilePaymentOptionsMap() {

        Map<Integer, Boolean> mobilePaymentOptionsMap = new ConcurrentHashMap<>();

        mobilePaymentOptionsMap.put(ServerConstants.PAYMENT_OPTION_ORANGE_MONEY, getOnlinePaymentParameterFromMap(ServerConstants.MNO_ID_ORANGE_MONEY).getIsActive());
        mobilePaymentOptionsMap.put(ServerConstants.PAYMENT_OPTION_WARI, getOnlinePaymentParameterFromMap(ServerConstants.MNO_ID_WARI).getIsActive());
        mobilePaymentOptionsMap.put(ServerConstants.PAYMENT_OPTION_FREE_MONEY, getOnlinePaymentParameterFromMap(ServerConstants.MNO_ID_FREE_MONEY).getIsActive());
        mobilePaymentOptionsMap.put(ServerConstants.PAYMENT_OPTION_E_MONEY, getOnlinePaymentParameterFromMap(ServerConstants.MNO_ID_E_MONEY).getIsActive());
        mobilePaymentOptionsMap.put(ServerConstants.PAYMENT_OPTION_ECOBANK, getOnlinePaymentParameterFromMap(ServerConstants.MNO_ID_ECO_BANK).getIsActive());

        return mobilePaymentOptionsMap;
    }

    @Override
    public List<OnlinePaymentParameterJson> getOnlinePaymentParameterJsonList() {

        List<OnlinePaymentParameterJson> onlinePaymentParameterJsonList = new ArrayList<>();

        for (OnlinePaymentParameter onlinePaymentParameter : this.onlinePaymentParameterList) {

            OnlinePaymentParameterJson onlinePaymentParameterJson = new OnlinePaymentParameterJson();

            BeanUtils.copyProperties(onlinePaymentParameter, onlinePaymentParameterJson);
            onlinePaymentParameterJson.setMnoName(ServerUtil.getMnoNameByMnoId(onlinePaymentParameter.getMnoId()));

            onlinePaymentParameterJsonList.add(onlinePaymentParameterJson);
        }

        return onlinePaymentParameterJsonList;
    }
}
