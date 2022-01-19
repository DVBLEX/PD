package com.pad.server.base.services.receipt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pad.server.base.common.PreparedJDBCQuery;
import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.ActivityLog;
import com.pad.server.base.entities.Lane;
import com.pad.server.base.entities.Payment;
import com.pad.server.base.entities.Receipt;
import com.pad.server.base.entities.ReceiptItemPDF;
import com.pad.server.base.entities.ReceiptNumber;
import com.pad.server.base.entities.ReceiptPDF;
import com.pad.server.base.entities.Sms;
import com.pad.server.base.entities.SystemParameter;
import com.pad.server.base.entities.Trip;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;
import com.pad.server.base.jsonentities.api.ReceiptJson;
import com.pad.server.base.jsonentities.api.printer.PrinterItemsJson;
import com.pad.server.base.jsonentities.api.printer.PrinterRequestJson;
import com.pad.server.base.jsonentities.api.printer.PrinterResponseJson;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.lane.LaneService;
import com.pad.server.base.services.payment.PaymentService;
import com.pad.server.base.services.session.SessionService;
import com.pad.server.base.services.sms.SmsService;
import com.pad.server.base.services.system.SystemService;
import com.pad.server.base.services.trip.TripService;

@Service
@Transactional
public class ReceiptServiceImpl implements ReceiptService {

    private static final Logger logger = Logger.getLogger(ReceiptServiceImpl.class);

    @Autowired
    private HibernateTemplate   hibernateTemplate;

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private SessionFactory      sessionFactory;

    @Autowired
    private ActivityLogService  activityLogService;

    @Autowired
    private LaneService         laneService;

    @Autowired
    private PaymentService      paymentService;

    @Autowired
    private SessionService      sessionService;

    @Autowired
    private SmsService          smsService;

    @Autowired
    private SystemService       systemService;

    @Autowired
    private TripService         tripService;

    @Value("${receipt.file.main.directory}")
    private String              receiptFileMainDirectory;

    @Value("${system.url.local}")
    private String              systemUrlLocal;

    @Value("${system.url.images}")
    private String              systemUrlImages;

    @Value("${system.url}")
    private String              systemUrl;

    @Value("${printer.api.url.local}")
    private String              printerApiUrlLocal;

    @Value("${printer.api.client.id}")
    private String              printerApiClientId;

    @Value("${printer.api.secret}")
    private String              printerApiSecret;

    @Override
    public long getReceiptCount(Long accountId) throws Exception {

        PreparedJDBCQuery query = getQuery(PreparedJDBCQuery.QUERY_TYPE_COUNT, accountId);

        return jdbcTemplate.queryForObject(query.getQueryString(), query.getQueryParameters(), Long.class);
    }

    @Override
    public List<Receipt> getReceiptList(Long accountId, String sortColumn, boolean sortAsc, int startLimit, int endLimit) throws SQLException {
        final List<Receipt> receiptList = new ArrayList<>();

        PreparedJDBCQuery query = getQuery(PreparedJDBCQuery.QUERY_TYPE_SELECT, accountId);

        query.setSortParameters(sortColumn, sortAsc, "receipt", ServerConstants.DEFAULT_SORTING_FIELD, "DESC");
        query.append(" LIMIT ").append(startLimit).append(", ").append(endLimit);

        jdbcTemplate.query(query.getQueryString(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Receipt receipt = new Receipt();
                receipt.setNumber(rs.getString("number"));
                receipt.setAccountId(rs.getLong("account_id"));
                receipt.setPaymentId(rs.getLong("payment_id"));
                receipt.setFirstName(rs.getString("first_name"));
                receipt.setLastName(rs.getString("last_name"));
                receipt.setMsisdn(rs.getString("msisdn"));
                receipt.setTotalAmount(rs.getBigDecimal("total_amount"));
                receipt.setCurrency(rs.getString("currency"));
                receipt.setPaymentOption(rs.getInt("payment_option"));
                receipt.setPath(rs.getString("path"));
                receipt.setOperatorId(rs.getLong("operator_id"));
                receipt.setDateCreated(rs.getTimestamp("date_created"));
                receipt.setDateEdited(rs.getTimestamp("date_edited"));
                receipt.setPaymentOptionString(ServerUtil.getPaymentOptionDescriptionById(receipt.getPaymentOption(), rs.getInt("language_id")));

                receiptList.add(receipt);
            }
        }, query.getQueryParameters());

        return receiptList;
    }

    private PreparedJDBCQuery getQuery(int queryType, Long accountId) {

        PreparedJDBCQuery query = new PreparedJDBCQuery();

        if (queryType == PreparedJDBCQuery.QUERY_TYPE_COUNT) {

            query.append(" SELECT COUNT(receipt.id)");
            query.append(" FROM pad.receipt receipt ");

        } else if (queryType == PreparedJDBCQuery.QUERY_TYPE_SELECT) {

            query.append(" SELECT receipt.*, account.language_id  FROM pad.receipt receipt ");
            query.append(" INNER JOIN pad.accounts account ON receipt.account_id = account.id ");
        }

        query.append(" WHERE (1=1) ");

        if (accountId != null) {
            query.append(" AND receipt.account_id = ?");
            query.addQueryParameter(accountId);
        }

        return query;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Receipt getReceiptByNumber(String number) {
        Receipt receipt = null;

        List<Receipt> receiptList = (List<Receipt>) hibernateTemplate.findByNamedParam("FROM Receipt WHERE number = :number", "number", number);

        if (receiptList != null && !receiptList.isEmpty()) {
            receipt = receiptList.get(0);
        }

        return receipt;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Receipt getReceiptByUniqueUrl(String uniqueUrl) {
        Receipt receipt = null;

        List<Receipt> receiptList = (List<Receipt>) hibernateTemplate.findByNamedParam("FROM Receipt WHERE uniqueUrl = :uniqueUrl", "uniqueUrl", uniqueUrl);

        if (receiptList != null && !receiptList.isEmpty()) {
            receipt = receiptList.get(0);
        }

        return receipt;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Receipt getReceiptByPaymentId(long paymentId) {
        Receipt receipt = null;

        List<Receipt> receiptList = (List<Receipt>) hibernateTemplate.findByNamedParam("FROM Receipt WHERE paymentId = :paymentId", "paymentId", paymentId);

        if (receiptList != null && !receiptList.isEmpty()) {
            receipt = receiptList.get(0);
        }

        return receipt;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createReceipt(Account account, long paymentId, String firstName, String lastName, String msisdn, Integer paymentOption, String typePaymentString,
        String itemDescription, int itemQuantity, BigDecimal unitaryPrice, String currency, BigDecimal typePaymentAmount, BigDecimal paidAmount, BigDecimal changeAmount,
        String vehicleRegNumber, String vehicleRegCountryISO, long operatorId) {

        try {

            LocalDateTime localDateTime = LocalDateTime.now();
            Date dateNow = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

            Receipt receipt = new Receipt();
            receipt.setNumber(ServerConstants.DEFAULT_STRING);
            receipt.setAccountId(account == null ? ServerConstants.DEFAULT_LONG : account.getId());
            receipt.setPaymentId(paymentId);
            receipt.setFirstName(firstName);
            receipt.setLastName(lastName);
            receipt.setMsisdn(msisdn);
            receipt.setTotalAmount(new BigDecimal(ServerConstants.DEFAULT_LONG));
            receipt.setCurrency(currency);
            receipt.setPaymentOption(paymentOption);
            receipt.setPath(ServerConstants.DEFAULT_STRING);
            receipt.setUniqueUrl(ServerConstants.DEFAULT_STRING);
            receipt.setOperatorId(operatorId);
            receipt.setLockCountFailed(ServerConstants.ZERO_INT);
            receipt.setDateCreated(dateNow);
            receipt.setDateEdited(dateNow);

            saveReceipt(receipt);

            receipt.setUniqueUrl(getUniqueURLForReceipt(receipt.getId()));

            try {
                receipt.setNumber(ServerUtil.formatDate(ServerConstants.dateFormatyyyyMMdd, dateNow) + receipt.getUniqueUrl());
            } catch (ParseException e) {
                receipt.setNumber("");
            }

            receipt.setPath(receiptFileMainDirectory + receipt.getAccountId() + "/" + receipt.getNumber() + ".pdf");

            /*
             * List<PDFItem> items = new ArrayList<>();
             * items.add(new PDFItem(itemDescription, vehicleRegCountryISO, String.valueOf(itemQuantity), unitaryPrice.multiply(new BigDecimal(itemQuantity)),
             * ServerUtil.getSymbolByCurrencyCode(currency)));
             * PDF pdf = new PDF(typePaymentString, typePaymentAmount, paidAmount, changeAmount, ServerUtil.getSymbolByCurrencyCode(currency), vehicleRegNumber, items);
             * pdf.setReference(generateReference(localDateTime));
             * pdfService.generatePdf(pdf, receipt.getPath());
             */

            File file = new File(receipt.getPath());
            file.getParentFile().mkdirs();
            file.createNewFile();

            generateReceiptPdf(receipt, file, typePaymentString, itemDescription, itemQuantity, unitaryPrice, ServerUtil.getSymbolByCurrencyCode(currency), typePaymentAmount,
                paidAmount, changeAmount, vehicleRegNumber);

            updateReceipt(receipt);

            HashMap<String, Object> params = new HashMap<>();

            params.put("accountName", firstName);
            params.put("receiptLink", systemUrl + "receipt.htm?c=" + receipt.getUniqueUrl());

            Sms scheduledSms = new Sms();
            scheduledSms.setLanguageId(account == null ? ServerConstants.LANGUAGE_FR_ID : account.getLanguageId());
            scheduledSms.setAccountId(account == null ? ServerConstants.DEFAULT_LONG : account.getId());
            scheduledSms.setMissionId(ServerConstants.DEFAULT_LONG);
            scheduledSms.setTripId(ServerConstants.DEFAULT_LONG);
            scheduledSms.setMsisdn(msisdn);

            smsService.scheduleSmsByType(scheduledSms, ServerConstants.SMS_RECEIPT_NOTIFICATION_TYPE, params);

            // For now use just SMS
            /*
             * Operator transporter = operatorService.getOperatorByAccountId(account.getId());
             * if (account.getType() == ServerConstants.ACCOUNT_TYPE_COMPANY) {
             * //email transporter
             * Email scheduledEmail = new Email();
             * scheduledEmail.setEmailTo(transporter.getUsername());
             * scheduledEmail.setLanguageId(account.getLanguageId());
             * scheduledEmail.setAccountId(ServerConstants.DEFAULT_LONG);
             * emailService.scheduleEmailByType(scheduledEmail, ServerConstants.EMAIL_RECEIPT_NOTIFICATION_TYPE, params);
             * } else {
             * // send sms to individual transporter
             * Sms scheduledSms = new Sms();
             * scheduledSms.setLanguageId(account.getLanguageId());
             * scheduledSms.setAccountId(ServerConstants.DEFAULT_LONG);
             * scheduledSms.setMsisdn(transporter.getUsername());
             * smsService.scheduleSmsByType(scheduledSms, ServerConstants.SMS_RECEIPT_NOTIFICATION_TYPE, params);
             * }
             */

        } catch (Exception e) {
            logger.error("createReceipt###Exception: ", e);
        }
    }

    /*
     * private String generateReference(LocalDateTime localDateTime) {
     * StringBuffer reference = new StringBuffer();
     * reference.append("REC");
     * reference.append("/");
     * reference.append(localDateTime.getYear());
     * reference.append("/");
     * reference.append(String.format("%02d", localDateTime.getMonth().getValue()));
     * reference.append("/");
     * reference.append(String.format("%02d", localDateTime.getDayOfMonth()));
     * reference.append("/");
     * reference.append(String.format("%02d", localDateTime.getHour()));
     * reference.append(String.format("%02d", localDateTime.getMinute()));
     * reference.append(String.format("%02d", localDateTime.getSecond()));
     * return reference.toString();
     * }
     */

    @Override
    public void saveReceipt(Receipt receipt) {
        hibernateTemplate.save(receipt);
    }

    @Override
    public void updateReceipt(Receipt receipt) {

        hibernateTemplate.update(receipt);
    }

    private void generateReceiptPdf(Receipt receipt, File file, String typePaymentString, String itemDescription, int itemQuantity, BigDecimal unitaryPrice, String currency,
        BigDecimal typePaymentAmount, BigDecimal paidAmount, BigDecimal changeAmount, String vehicleRegNumber) throws Exception {

        List<ReceiptItemPDF> items = new ArrayList<>();
        items.add(new ReceiptItemPDF(itemDescription, String.valueOf(itemQuantity), unitaryPrice, unitaryPrice.multiply(new BigDecimal(itemQuantity)), currency));

        ReceiptPDF receiptPDF = new ReceiptPDF(receipt.getNumber(), new Date(), typePaymentString, typePaymentAmount, paidAmount, changeAmount, currency, items);

        receipt.setTotalAmount(receiptPDF.getTotalAmount());

        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream(file));

        addMetadata(document);

        document.open();

        addLogo(document);

        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        addReceiptHeader(document);

        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        addReceiptCode(document, receiptPDF.getNumber());

        addReceiptDetails(document, receiptPDF, vehicleRegNumber);

        document.add(Chunk.NEWLINE);

        addReceiptItems(document, receiptPDF);

        document.close();

    }

    private void addMetadata(Document document) {
        document.addTitle("AGS PAD Receipt");
        document.addAuthor("AGS PAD System");
        document.addSubject("AGS PAD Payment Receipt");
        document.addKeywords("Metadata, iText, PDF");
        document.addCreator("AGS PAD System");
    }

    private void addLogo(Document document) throws MalformedURLException, IOException, DocumentException {
        Image img = Image.getInstance(systemUrlLocal + systemUrlImages + "ags_logo_big.jpg");
        img.setAlignment(Element.ALIGN_CENTER);
        img.scalePercent(100);

        document.add(img);
    }

    private void addReceiptHeader(Document document) throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);

        Paragraph paragraph1 = new Paragraph(new Chunk("AMENAGEMENT GESTION SERVICE SARL", font));
        paragraph1.setAlignment(Element.ALIGN_CENTER);

        Paragraph paragraph2 = new Paragraph(new Chunk("Rocade Fann Bel Air, face CFMPL", font));
        paragraph2.setAlignment(Element.ALIGN_CENTER);

        Paragraph paragraph3 = new Paragraph(new Chunk("RCCM : SN DKR-2018-B-25972", font));
        paragraph3.setAlignment(Element.ALIGN_CENTER);

        Paragraph paragraph4 = new Paragraph(new Chunk("NINEA / 0070277342Y2", font));
        paragraph4.setAlignment(Element.ALIGN_CENTER);

        Paragraph paragraph5 = new Paragraph(new Chunk("DAKAR-SENEGAL", font));
        paragraph5.setAlignment(Element.ALIGN_CENTER);

        document.add(paragraph1);
        document.add(paragraph2);
        document.add(paragraph3);
        document.add(paragraph4);
        document.add(paragraph5);
    }

    private void addReceiptCode(Document document, String invoiceCode) throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);

        Paragraph para = new Paragraph(new Chunk("Numéro de reçu: " + invoiceCode, font));

        document.add(para);
    }

    private void addReceiptDetails(Document document, ReceiptPDF receiptPDF, String vehicleRegNumber) throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

        document.add(new Paragraph(new Chunk("Date de reçu: " + receiptPDF.getDateReceiptString(), font)));

        if (StringUtils.isNotBlank(vehicleRegNumber)) {
            document.add(new Paragraph(new Chunk("Véhicule: " + vehicleRegNumber, font)));
        }
    }

    private void addReceiptItems(Document document, ReceiptPDF receiptPDF) throws DocumentException {

        PdfPTable table = new PdfPTable(new float[] { 50, 11, 13, 13 });
        table.setWidthPercentage(95);

        addTableHeader(table);

        addTableRows(table, receiptPDF.getItems());

        addTableTotals(table, receiptPDF);

        addTableInfo(table, receiptPDF);

        document.add(table);
    }

    private void addTableHeader(PdfPTable table) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);

        Stream.of("Désignation de la prestation", "Quantité", "Prix Unit HT", "Prix Total HT").forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(new BaseColor(220, 220, 220));
            header.setBorderWidth(1);
            header.setVerticalAlignment(Element.ALIGN_MIDDLE);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setPhrase(new Phrase(columnTitle, font));

            table.addCell(header);
        });
    }

    private void addTableRows(PdfPTable table, List<ReceiptItemPDF> items) {
        for (ReceiptItemPDF item : items) {
            table.addCell(getItemCell(item.getDescription(), Element.ALIGN_LEFT));
            table.addCell(getItemCell(item.getQuantity(), Element.ALIGN_RIGHT));
            table.addCell(getItemCell(item.getUnitaryPriceString(), Element.ALIGN_RIGHT));
            table.addCell(getItemCell(item.getTotalString(), Element.ALIGN_RIGHT));
        }
    }

    private PdfPCell getItemCell(String value, int alignment) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);

        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(alignment);
        cell.setPhrase(new Phrase(value, font));

        return cell;

    }

    private void addTableTotals(PdfPTable table, ReceiptPDF receiptPDF) {

        addTotal(table, "Total HT:", receiptPDF.getTotalHT(), 0);

        addTotal(table, "TVA 18%:", receiptPDF.getTotalTVA(), 0);

        addTotal(table, "Total TTC:", receiptPDF.getTotalTTC(), 1);

    }

    private void addTableInfo(PdfPTable table, ReceiptPDF receiptPDF) {

        addInfo(table, receiptPDF.getTypePaymentLabel() + ":", receiptPDF.getTypePaymentAmount(), 1);

        addInfo(table, "Montant Payé:", receiptPDF.getPaidAmount(), 0);

        addInfo(table, "Changement:", receiptPDF.getChangeAmount(), 0);

    }

    private PdfPCell getTotalCell(String value, int alignment, int border) {

        PdfPCell totalCell = new PdfPCell();
        totalCell.setBorder(border);
        totalCell.setVerticalAlignment(alignment);
        totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalCell.setPhrase(new Phrase(value, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK)));

        return totalCell;

    }

    private PdfPCell getInfoCell(String value, int alignment, int border) {

        PdfPCell infoCell = new PdfPCell();
        infoCell.setBorder(border);
        infoCell.setVerticalAlignment(alignment);
        infoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        infoCell.setPhrase(new Phrase(value, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK)));

        return infoCell;

    }

    private void addTotal(PdfPTable table, String totalLabel, String totalAmount, int border) {

        PdfPCell total = new PdfPCell();
        total.setColspan(2);
        total.setBorder(border);
        table.addCell(total);
        table.addCell(getTotalCell(totalLabel, Element.ALIGN_LEFT, border));
        table.addCell(getTotalCell(totalAmount, Element.ALIGN_MIDDLE, border));
    }

    private void addInfo(PdfPTable table, String infoLabel, String infoAmount, int border) {

        PdfPCell info = new PdfPCell();
        info.setColspan(2);
        info.setBorder(border);
        table.addCell(info);
        table.addCell(getInfoCell(infoLabel, Element.ALIGN_LEFT, border));
        table.addCell(getInfoCell(infoAmount, Element.ALIGN_MIDDLE, border));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private String getUniqueURLForReceipt(long receiptId) throws Exception {

        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("FROM ReceiptNumber WHERE receiptId=:id").setParameter("id", receiptId).setMaxResults(1);

        List<ReceiptNumber> results = query.list();

        if (results.isEmpty())
            throw new Exception("getUniqueURLForReceipt#receiptId: " + receiptId + ". ReceiptNumber is not found by receiptId...");

        ReceiptNumber receiptNumber = results.get(0);

        return receiptNumber.getNumber();
    }

    @Override
    public void printReceipt(ReceiptJson receiptJson, long loggedOperatorId) {
        try {

            Payment payment = paymentService.getPaymentByCode(receiptJson.getPaymentCode());

            if (payment == null)
                throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                    "paymentCode is invalid");

            com.pad.server.base.entities.Session session = sessionService.getLastSessionByKioskOperatorId(loggedOperatorId);

            if (session == null)
                throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "session is null");

            Lane lane = laneService.getLaneByLaneId(session.getLaneId());

            if (lane == null)
                throw new PADValidationException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "laneId is invalid");

            Trip trip = tripService.getTripById(payment.getTripId());

            PrinterRequestJson printerRequestJson = new PrinterRequestJson();
            printerRequestJson.setApiClientId(printerApiClientId);
            printerRequestJson.setApiSecret(printerApiSecret);
            printerRequestJson.setIp(lane.getPrinterIp());
            printerRequestJson.setVrn(trip == null ? ServerConstants.DEFAULT_STRING : trip.getVehicleRegistration());

            String itemDescription = payment.getAccountId() == ServerConstants.DEFAULT_LONG ? "Paiement des frais de voyage" : "Recharge de compte";
            List<PrinterItemsJson> items = new ArrayList<>();
            items.add(new PrinterItemsJson(payment.getAmountDue().toString(), itemDescription));

            printerRequestJson.setItems(items);

            BigDecimal tax = payment.getAmountDue().multiply(new BigDecimal(18)).divide(new BigDecimal(100));

            printerRequestJson.setTotal(payment.getAmountDue().toString());
            printerRequestJson.setMoney_received(payment.getAmountPayment().toString());
            printerRequestJson.setChange_given(payment.getAmountChangeDue().toString());
            printerRequestJson.setHT(payment.getAmountDue().subtract(tax).toString());
            printerRequestJson.setTVA(tax.toString());
            printerRequestJson.setTTC(payment.getAmountDue().toString());
            printerRequestJson.setReceipt_id(getReceiptByPaymentId(payment.getId()).getNumber());
            printerRequestJson.setDate(ServerUtil.formatDate(ServerConstants.dateFormatyyyyMMdd_ISO, new Date()));
            printerRequestJson.setTime(ServerUtil.formatDate(ServerConstants.timeFormatHHmss, new Date()));

            PrinterResponseJson printerResponseJson = printReceipt(printerRequestJson);

            logger.info(printerResponseJson);

            activityLogService.saveActivityLog(new ActivityLog(ServerConstants.ACTIVITY_LOG_RECEIPT_PRINT, loggedOperatorId, new ObjectMapper().writeValueAsString(receiptJson)));
        } catch (Exception e) {
            logger.error("printReceipt###Exception: ", e);
        }

    }

    private PrinterResponseJson printReceipt(PrinterRequestJson printerRequestJson) throws PADException {

        PrinterResponseJson printerResponseJson = null;

        try {

            String responseJson = callPrintReceiptServlet(printerApiUrlLocal, printerRequestJson, systemService.getSystemParameter());

            logger.info("printReceipt###" + printerApiUrlLocal + "#Response: " + responseJson);

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            printerResponseJson = mapper.readValue(responseJson, PrinterResponseJson.class);

        } catch (PADException pade) {
            throw pade;

        } catch (Exception e) {
            logger.error("printReceipt###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "printReceipt");
        }

        return printerResponseJson;
    }

    private String callPrintReceiptServlet(String url, PrinterRequestJson printerRequestJson, SystemParameter systemParameter) throws PADException {

        String responseSource = "callPrintReceiptServlet#";

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        RequestConfig requestConfig = null;
        String responseText = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(printerRequestJson)));

            httpClient = HttpClients.createDefault();

            requestConfig = RequestConfig.custom().setSocketTimeout(systemParameter.getPrinterSocketTimeout()).setConnectTimeout(systemParameter.getPrinterConnectTimeout())
                .setConnectionRequestTimeout(systemParameter.getPrinterConnRequestTimeout()).build();

            httpPost.setConfig(requestConfig);
            httpPost.setHeader("content-type", "application/json");

            httpResponse = httpClient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                HttpEntity httpResponseEntity = httpResponse.getEntity();
                if (httpResponseEntity != null) {
                    ByteArrayOutputStream httpResponseByteArrayOutputStream = new ByteArrayOutputStream();
                    httpResponseEntity.writeTo(httpResponseByteArrayOutputStream);
                    responseText = httpResponseByteArrayOutputStream.toString();
                }
            } else {

                logger.info(responseSource + "#Response: [StatusCode=" + httpResponse.getStatusLine().getStatusCode() + ", ReasonPhrase="
                    + httpResponse.getStatusLine().getReasonPhrase() + "]");
                throw new PADException(ServerResponseConstants.EXTERNAL_API_CONNECTION_FAILURE_CODE, responseSource + "Response: [StatusCode="
                    + httpResponse.getStatusLine().getStatusCode() + ", ReasonPhrase=" + httpResponse.getStatusLine().getReasonPhrase() + "]", responseSource);
            }
        } catch (PADException pade) {
            throw pade;
        } catch (Exception e) {
            logger.error(responseSource + "###Exception: ", e);
            throw new PADException(ServerResponseConstants.EXTERNAL_API_CONNECTION_FAILURE_CODE, e.getClass() + "###" + e.getMessage(), responseSource + "##Exception");
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }

                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (Exception e) {
                logger.error(responseSource + "###finally#Exception: ", e);
            }
        }

        return responseText;
    }

}
