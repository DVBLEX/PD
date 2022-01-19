package com.pad.server.base.services.invoice;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.common.PreparedJDBCQuery;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.Email;
import com.pad.server.base.entities.Invoice;
import com.pad.server.base.entities.InvoiceNumber;
import com.pad.server.base.entities.PDF;
import com.pad.server.base.entities.PDF.Type;
import com.pad.server.base.entities.PDFItem;
import com.pad.server.base.entities.Sms;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.AccountInvoiceJson;
import com.pad.server.base.jsonentities.api.InvoiceJson;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.pdf.PdfService;
import com.pad.server.base.services.sms.SmsService;
import com.pad.server.base.services.system.SystemService;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private static final Logger logger = Logger.getLogger(InvoiceServiceImpl.class);

    @Autowired
    private HibernateTemplate   hibernateTemplate;

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private SessionFactory      sessionFactory;

    @Autowired
    private AccountService      accountService;

    @Autowired
    private EmailService        emailService;

    @Autowired
    private SmsService          smsService;

    @Autowired
    private SystemService       systemService;

    @Autowired
    private PdfService          pdfService;

    @Value("${invoice.file.main.directory}")
    private String              invoiceFileMainDirectory;

    @Value("${statement.file.main.directory}")
    private String              statementFileMainDirectory;

    @Value("${system.url.local}")
    private String              systemUrlLocal;

    @Value("${system.url.images}")
    private String              systemUrlImages;

    @Override
    public void saveInvoice(Invoice invoice) {

        hibernateTemplate.save(invoice);
    }

    @Override
    public void updateInvoice(Invoice invoice) {

        hibernateTemplate.update(invoice);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Invoice getInvoiceByCode(String code) {

        Invoice invoice = null;

        List<Invoice> invoiceList = (List<Invoice>) hibernateTemplate.findByNamedParam("FROM Invoice WHERE code = :code", "code", code);

        if (invoiceList != null && !invoiceList.isEmpty()) {
            invoice = invoiceList.get(0);
        }

        return invoice;
    }

    @Override
    public List<AccountInvoiceJson> getAccountInvoicesList(AccountInvoiceJson accountInvoiceJson) throws PADException {
        PreparedJDBCQuery query = getAccountInvoicesQuery(PreparedJDBCQuery.QUERY_TYPE_SELECT, accountInvoiceJson);

        query.setSortParameters(accountInvoiceJson.getSortColumn(), accountInvoiceJson.getSortAsc(), "i", ServerConstants.DEFAULT_SORTING_FIELD, "DESC");
        query.setLimitParameters(accountInvoiceJson.getCurrentPage(), accountInvoiceJson.getPageCount());

        List<AccountInvoiceJson> accountInvoiceJsonList = new ArrayList<>();

        jdbcTemplate.query(query.getQueryString(), rs -> {
            AccountInvoiceJson invoiceJson = new AccountInvoiceJson();
            invoiceJson.setDateCreated(rs.getTimestamp("date_created"));
            try {
                invoiceJson.setDateCreatedString(ServerUtil.formatDate(ServerConstants.dateFormatddMMyyyy, rs.getTimestamp("date_created")));
            } catch (ParseException e) {
                invoiceJson.setDateCreatedString("");
            }
            invoiceJson.setType(rs.getInt("type"));
            invoiceJson.setCode(rs.getString("code"));
            invoiceJson.setReference(rs.getString("reference"));
            invoiceJson.setAmount(rs.getBigDecimal("amount"));
            if (rs.getTimestamp("date_due") != null) {
                invoiceJson.setDateDue(new Date(rs.getTimestamp("date_due").getTime()));
            }
            try {
                if (invoiceJson.getDateDue() != null) {
                    invoiceJson.setDateDueString(ServerUtil.formatDate(ServerConstants.dateFormatddMMyyyy, invoiceJson.getDateDue()));
                } else {
                    invoiceJson.setDateDueString("");
                }
            } catch (ParseException e) {
                invoiceJson.setDateDueString("");
            }
            invoiceJson.setPath(rs.getString("path"));
            accountInvoiceJsonList.add(invoiceJson);
        }, query.getQueryParameters());
        return accountInvoiceJsonList;
    }

    private PreparedJDBCQuery getAccountInvoicesQuery(int queryType, AccountInvoiceJson accountInvoiceJson) throws PADException {

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        if (queryType == PreparedJDBCQuery.QUERY_TYPE_COUNT) {
            query.append("SELECT COUNT(i.id)");
            query.append(" FROM pad.invoice i ");
        } else if (queryType == PreparedJDBCQuery.QUERY_TYPE_SELECT) {
            query.append("SELECT i.date_created, i.type, i.reference, i.code, i.total_amount AS amount, i.date_due, i.path ");
            query.append("FROM pad.invoice i ");
        }
        query.append("WHERE i.account_id = ? AND i.date_created >= ?");

        Account account = accountService.getAccountByCode(accountInvoiceJson.getAccountCode());
        query.setAccountParameters(account, accountInvoiceJson.getDateFromString());
        return query;
    }

    @Override
    public long getInvoiceCount(Long accountId, Integer type, Long accountNumber) {

        PreparedJDBCQuery query = getQuery(PreparedJDBCQuery.QUERY_TYPE_COUNT, accountId, type, accountNumber);

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Long.class);
    }

    private PreparedJDBCQuery getQuery(int queryType, Long accountId, Integer type, Long accountNumber) {

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        if (queryType == PreparedJDBCQuery.QUERY_TYPE_COUNT) {
            query.append(" SELECT COUNT(invoices.id)");
            query.append(" FROM pad.invoice invoices ");
        } else if (queryType == PreparedJDBCQuery.QUERY_TYPE_SELECT) {
            query
                .append(" SELECT invoices.*, account.number, account.type as account_type, account.first_name, account.last_name, account.company_name FROM pad.invoice invoices ");
        }

        query.append(" INNER JOIN pad.accounts account ON invoices.account_id = account.id ");
        query.append(" WHERE (1=1) ");

        if (accountId != null) {
            query.append(" AND invoices.account_id = ?");
            query.addQueryParameter(accountId);
        }

        if (type != null && type > ServerConstants.ZERO_INT) {
            query.append(" AND invoices.type = ?");
            query.addQueryParameter(type);
        }

        if (accountNumber != null) {
            query.append(" AND account.number = ?");
            query.addQueryParameter(accountNumber);
        }

        return query;
    }

    @Override
    public List<InvoiceJson> getInvoiceList(Long accountId, Integer type, InvoiceJson invoiceJson) throws SQLException {
        final String sortColumn = invoiceJson.getSortColumn();
        final boolean sortAsc = invoiceJson.getSortAsc();
        final List<InvoiceJson> invoiceList = new ArrayList<>();

        PreparedJDBCQuery query = getQuery(PreparedJDBCQuery.QUERY_TYPE_SELECT, accountId, type, invoiceJson.getAccountNumber());

        query.setSortParameters(sortColumn, sortAsc, "invoices", "id", "DESC");
        query.setLimitParameters(invoiceJson.getCurrentPage(), invoiceJson.getPageCount());

        jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                InvoiceJson invoice = new InvoiceJson();
                invoice.setCode(rs.getString("code"));
                invoice.setReference(rs.getString("reference"));
                invoice.setType(rs.getInt("type"));
                invoice.setAccountNumber(rs.getLong("number"));
                invoice.setTotalAmount(rs.getBigDecimal("total_amount"));
                invoice.setCurrency(rs.getString("currency"));
                try {
                    if (rs.getTimestamp("date_due") != null) {
                        invoice.setDateDueString(ServerUtil.formatDate(ServerConstants.dateFormatddMMyyyy, rs.getTimestamp("date_due")));
                    } else {
                        invoice.setDateDueString("");
                    }
                } catch (ParseException e) {
                    invoice.setDateDueString("");
                }
                invoice.setIsPaid(rs.getBoolean("is_paid"));
                try {
                    if (rs.getTimestamp("date_payment") != null) {
                        invoice.setDatePaymentString(ServerUtil.formatDate(ServerConstants.dateFormatddMMyyyy, rs.getTimestamp("date_payment")));
                    } else {
                        invoice.setDatePaymentString("");
                    }
                } catch (ParseException e) {
                    invoice.setDatePaymentString("");
                }
                invoice.setTypePayment(rs.getInt("type_payment"));
                invoice.setPath(rs.getString("path"));
                invoice.setOperatorId(rs.getLong("operator_id"));
                try {
                    invoice.setDateCreatedString(ServerUtil.formatDate(ServerConstants.dateFormatddMMyyyy, rs.getTimestamp("date_created")));
                } catch (ParseException e) {
                    invoice.setDateCreatedString("");
                }

                if (rs.getInt("account_type") == ServerConstants.ACCOUNT_TYPE_COMPANY) {
                    invoice.setCompanyName(rs.getString("company_name"));
                } else {
                    invoice.setCompanyName(rs.getString("first_name") + " " + rs.getString("last_name"));
                }

                invoiceList.add(invoice);
            }
        }, query.getQueryParameters());

        return invoiceList;
    }

    @Override
    public void generateInvoicesStatements(LocalDateTime dateFrom, LocalDateTime dateTo) throws DataAccessException, Exception {

        int counter = 0;
        List<Account> accounts = accountService.getActiveAccounts().stream().filter(a -> a.getPaymentTermsType() != ServerConstants.DEFAULT_INT).collect(Collectors.toList());

        for (Account account : accounts) {

            PDF pdf = createPDF(account, dateFrom, dateTo);

            if (pdf != null && pdf.getTotalAmount().intValue() > ServerConstants.ZERO_INT) {

                logger.info("Generating invoice/statement for " + dateFrom.getMonth().toString() + " " + dateFrom.getYear() + " for account:" + account.getId());

                Invoice invoice = new Invoice();
                invoice.setCode(ServerConstants.DEFAULT_STRING);
                invoice.setType(pdf.getType().getId());
                invoice.setReference(generateReference(dateFrom, account.getNumber(), ++counter, pdf.getType()));
                invoice.setAccountId(account.getId());
                invoice.setTotalAmount(pdf.getTotalAmount());
                invoice.setCurrency(account.getCurrency());
                invoice.setDateDue(pdf.getDatePaymentDue());
                invoice.setIsPaid(Boolean.FALSE);
                invoice.setDatePayment(null);
                invoice.setTypePayment(ServerConstants.DEFAULT_INT);
                invoice.setPath(ServerConstants.DEFAULT_STRING);
                invoice.setOperatorId(ServerConstants.DEFAULT_LONG);
                invoice.setDateCreated(new Date());
                invoice.setDateEdited(new Date());

                saveInvoice(invoice);

                invoice.setCode("AGS_" + getNumberForInvoice(invoice.getId()));

                String path = pdf.getType().equals(PDF.Type.INVOICE) ? invoiceFileMainDirectory : statementFileMainDirectory;
                invoice.setPath(path + account.getId() + "/" + invoice.getCode() + ".pdf");

                updateInvoice(invoice);

                pdf.setReference(invoice.getReference());

                File pdfFile = pdfService.generatePdf(pdf, invoice.getPath());

                HashMap<String, Object> params = new HashMap<>();

                if (account.getLanguageId() == ServerConstants.LANGUAGE_EN_ID) {
                    params.put("invoiceType", pdf.getType().equals(PDF.Type.INVOICE) ? "Invoice" : "Statement");
                    params.put("invoiceDate", dateFrom.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase() + " " + dateFrom.getYear());
                } else {
                    params.put("invoiceType", pdf.getType().equals(PDF.Type.INVOICE) ? "Facture" : "Relevé de Compte");
                    params.put("invoiceDate", dateFrom.getMonth().getDisplayName(TextStyle.SHORT, Locale.FRENCH).toUpperCase() + " " + dateFrom.getYear());
                }

                int hourSchedule = systemService.getSystemParameter().getInvoiceStatementScheduleHour();
                Date dateSchedule = Date.from(LocalDate.now().plusDays(1).atTime(hourSchedule, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant());

                if (StringUtils.isNotBlank(account.getEmailListInvoiceStatement())) {
                    Email email = initEmail(account, dateSchedule, pdfFile);
                    params.put("accountName", account.getCompanyName());
                    sendEmail(email, params);
                } else {
                    params.put("accountName", account.getFirstName());
                    sendSms(account, dateSchedule, params);
                }
            }
        }
    }

    private Email initEmail(Account account, Date dateSchedule, File pdfFile) {
        Email email = new Email();
        email.setEmailTo(account.getEmailListInvoiceStatement());
        email.setLanguageId(account.getLanguageId());
        email.setAccountId(account.getId());
        email.setAttachmentPath(pdfFile.getAbsolutePath());
        email.setDateScheduled(dateSchedule);
        email.setMissionId(ServerConstants.DEFAULT_LONG);
        email.setTripId(ServerConstants.DEFAULT_LONG);
        return email;
    }

    private void sendEmail(Email email, HashMap<String, Object> params) {
        try {
            emailService.scheduleEmailByType(email, ServerConstants.EMAIL_INVOICE_NOTIFICATION_TYPE, params);
        } catch (PADException pade) {
            // email could not be sent due to a network problem, continue with the execution
            logger.error("generateInvoices#scheduleInvoiceEmail###PADException: ", pade);
        } catch (Exception e) {
            logger.error("generateInvoices#scheduleInvoiceEmail###Exception: ", e);
        }
    }

    private Sms initSms(Account account, Date dateSchedule) {
        Sms scheduleSms = new Sms();
        scheduleSms.setLanguageId(account.getLanguageId());
        scheduleSms.setAccountId(account.getId());
        scheduleSms.setMissionId(ServerConstants.DEFAULT_LONG);
        scheduleSms.setTripId(ServerConstants.DEFAULT_LONG);
        scheduleSms.setMsisdn(account.getMsisdn());
        scheduleSms.setDateScheduled(dateSchedule);
        return scheduleSms;
    }

    private void sendSms(Account account, Date dateSchedule, HashMap<String, Object> params) {
        Sms scheduleSms = initSms(account, dateSchedule);
        try {
            smsService.scheduleSmsById(scheduleSms, ServerConstants.SMS_FR_NEW_INVOICE_TEMPLATE_ID, params);
        } catch (PADException pade) {
            // sms could not be sent due to a network problem, continue with the execution
            logger.error("generateInvoices#scheduleSmsById###PADException: ", pade);
        } catch (Exception e) {
            logger.error("generateInvoices#scheduleSmsById###Exception: ", e);
        }
    }

    private String generateReference(LocalDateTime dateFrom, long accountNumber, int counter, Type pdfType) {

        StringBuffer reference = new StringBuffer();

        reference.append(pdfType.equals(PDF.Type.INVOICE) ? "FAC" : "REL");
        reference.append("/");
        reference.append(accountNumber);
        reference.append("/");
        reference.append(dateFrom.getYear());
        reference.append("/");
        reference.append(String.format("%02d", dateFrom.getMonth().getValue()));
        reference.append("/");
        reference.append(String.format("%04d", counter));

        return reference.toString();
    }

    private PDF createPDF(Account account, LocalDateTime dateFrom, LocalDateTime dateTo) throws Exception {

        List<PDFItem> items = new ArrayList<>();
        Date startDate = Date.from(dateFrom.atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(dateTo.atZone(ZoneId.systemDefault()).toInstant());

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        query.append(" SELECT port_operator_id, transaction_type, vehicle_registration_country_iso, ");
        query.append("  SUM(count_trips) AS quantity, SUM(transporter_total_amount_fee) AS total ");
        query.append(" FROM pad.transporter_trips_statistics ");
        query.append(" WHERE account_id = ? AND date_fee_paid >= ? AND date_fee_paid < ? ");
        query.append(" GROUP BY port_operator_id, transaction_type, vehicle_registration_country_iso ");

        query.addQueryParameter(account.getId());
        query.addQueryParameter(startDate);
        query.addQueryParameter(endDate);

        jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {

                String portOperatorName = systemService.getPortOperatorNameById(rs.getLong("port_operator_id"));
                String transactionType = ServerUtil.getTransactionTypeName(rs.getInt("transaction_type"), ServerConstants.LANGUAGE_FR_ID);
                String vehicleRegCountryISO = rs.getString("vehicle_registration_country_iso");
                String description = "Voyage pour opérateur portuaire: " + portOperatorName + "; Type: " + transactionType + ";";
                String quantity = rs.getString("quantity");
                BigDecimal total = rs.getBigDecimal("total");
                String currency = ServerUtil.getSymbolByCurrencyCode(account.getCurrency());

                items.add(new PDFItem(description, vehicleRegCountryISO, quantity, total, currency));
            }
        }, query.getQueryParameters());

        String companyName = account.getNumber() + " - ";
        companyName += account.getType() == ServerConstants.ACCOUNT_TYPE_COMPANY ? account.getCompanyName() : account.getFirstName() + " " + account.getLastName();

        Date dateDue = Date.from(LocalDateTime.now().plusDays(8).atZone(ZoneId.systemDefault()).toInstant());

        Date datePeriodEnd = Date.from(dateTo.minusDays(1).atZone(ZoneId.systemDefault()).toInstant());

        BigDecimal accountBalance = accountService.getOpeningBalance(account.getCode(), endDate);

        if (items.size() > 0) {
            if (account.getPaymentTermsType() == ServerConstants.ACCOUNT_PAYMENT_TERMS_TYPE_POSTPAY)
                return new PDF(companyName, dateDue, ServerUtil.getSymbolByCurrencyCode(account.getCurrency()), startDate, datePeriodEnd, accountBalance, endDate, items);
            else
                return new PDF(companyName, ServerUtil.getSymbolByCurrencyCode(account.getCurrency()), startDate, datePeriodEnd, accountBalance, endDate, items);
        } else
            return null;

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private String getNumberForInvoice(long invoiceId) throws Exception {

        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("FROM InvoiceNumber WHERE invoiceId=:id").setParameter("id", invoiceId).setMaxResults(1);

        List<InvoiceNumber> results = query.list();

        if (results.isEmpty())
            throw new Exception("getNumberForInvoice#invoiceId: " + invoiceId + ". InvoiceNumber is not found by invoiceId...");

        InvoiceNumber invoiceNumber = results.get(0);

        return invoiceNumber.getNumber();
    }

    @Override
    public Long getAccountInvoicesCount(AccountInvoiceJson accountInvoiceJson) throws PADException {
        PreparedJDBCQuery query = getAccountInvoicesQuery(PreparedJDBCQuery.QUERY_TYPE_COUNT, accountInvoiceJson);

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Long.class);
    }
}
