package com.pad.server.base.services.receipt;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.Receipt;
import com.pad.server.base.jsonentities.api.ReceiptJson;

public interface ReceiptService {

    public long getReceiptCount(Long accountId) throws Exception;

    public List<Receipt> getReceiptList(Long accountId, String sortColumn, boolean sortAsc, int startLimit, int endLimit) throws SQLException;

    public Receipt getReceiptByNumber(String number);

    public Receipt getReceiptByPaymentId(long paymentId);

    public Receipt getReceiptByUniqueUrl(String uniqueUrl);

    public void createReceipt(Account account, long paymentId, String firstName, String lastName, String msisdn, Integer paymentOption, String typePaymentString,
        String itemDescription, int itemQuantity, BigDecimal unitaryPrice, String currency, BigDecimal typePaymentAmount, BigDecimal paidAmount, BigDecimal changeAmount,
        String vehicleRegNumber, String vehicleRegCountryISO, long operatorId);

    public void saveReceipt(Receipt receipt);

    public void updateReceipt(Receipt receipt);

    public void printReceipt(ReceiptJson receiptJson, long loggedOperatorId);

}
