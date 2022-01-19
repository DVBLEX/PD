package com.pad.server.base.services.payment;

import java.util.List;

import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.Payment;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADOnlinePaymentException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.AccountPaymentJson;
import com.pad.server.base.jsonentities.api.PaymentJson;

public interface PaymentService {

    public void savePayment(Payment payment);

    public void updatePayment(Payment payment);

    public String saveTransporterPayment(PaymentJson paymentJson, long operatorId) throws PADException, PADValidationException, PADOnlinePaymentException;

    public String accountTopup(PaymentJson paymentJson, long operatorId) throws PADException, PADValidationException, PADOnlinePaymentException;

    public Payment getPaymentByCode(String paymentCode);

    public Payment getPaymentById(long paymentId);

    public List<PaymentJson> getAccountPayments(long accountId);

    public Long getAccountPaymentsCount(AccountPaymentJson accountPaymentJson) throws PADException;

    public List<AccountPaymentJson> getAccountPaymentsList(AccountPaymentJson accountPaymentJson) throws PADException;

    public void sendTransactionNotifyEmail(Account account, Payment payment) throws PADException;
}
