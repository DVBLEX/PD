package com.pad.server.base.services.invoice;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.pad.server.base.entities.Invoice;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.AccountInvoiceJson;
import com.pad.server.base.jsonentities.api.InvoiceJson;

public interface InvoiceService {

    public long getInvoiceCount(Long accountId, Integer type, Long accountNumber) throws Exception;

    public List<InvoiceJson> getInvoiceList(Long accountId, Integer type, InvoiceJson invoiceJson) throws SQLException;

    public Invoice getInvoiceByCode(String code);

    public List<AccountInvoiceJson> getAccountInvoicesList(AccountInvoiceJson accountInvoiceJson) throws PADException;

    public void generateInvoicesStatements(LocalDateTime dateFrom, LocalDateTime dateTo) throws DataAccessException, Exception;

    public void saveInvoice(Invoice invoice);

    public void updateInvoice(Invoice invoice);

    public Long getAccountInvoicesCount(AccountInvoiceJson accountInvoiceJson) throws PADException;
}
