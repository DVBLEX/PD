package com.pad.server.base.services.receipt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

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
import com.pad.server.base.entities.ReceiptItemPDF;
import com.pad.server.base.entities.ReceiptPDF;

public class GenerateReceiptPDF {

    public static void main(String[] args) {

        List<ReceiptItemPDF> items = new ArrayList<>();
        items.add(new ReceiptItemPDF("Voyage pour opérateur portuaire:TVS; Type: Exportation;", "1", new BigDecimal(410.50), new BigDecimal(410.50), "CFA"));

        ReceiptPDF receiptPDF = new ReceiptPDF("UIDGBKIXDZ", new Date(), "Cash", new BigDecimal(410.50), new BigDecimal(410.50), new BigDecimal(0), "CFA", items);

        generateReceipt(receiptPDF);

    }

    private static void generateReceipt(ReceiptPDF receiptPDF) {
        try {
            Document document = new Document();

            File file = new File("/var/log/tc/pad/receipt/1001/PAYMENT_RECEIPT_UIDGBKIXDZ.pdf");
            file.getParentFile().mkdirs();
            file.createNewFile();

            PdfWriter.getInstance(document, new FileOutputStream(file));

            document.addTitle("Hello World example");
            document.addAuthor("Bruno Lowagie");
            document.addSubject("This example shows how to add metadata");
            document.addKeywords("Metadata, iText, PDF");
            document.addCreator("My program using iText");

            document.open();

            addLogo(document);

            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            addReceiptHeader(document);

            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            addReceiptCode(document, receiptPDF.getNumber());

            addReceiptDetails(document, receiptPDF);

            document.add(Chunk.NEWLINE);

            addReceiptItems(document, receiptPDF);

            document.close();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

    }

    private static void addLogo(Document document) throws MalformedURLException, IOException, DocumentException {
        Image img = Image.getInstance("C:\\temp\\ags_logo.png");
        img.setAlignment(Element.ALIGN_CENTER);
        img.scalePercent(20);

        document.add(img);
    }

    private static void addReceiptHeader(Document document) throws DocumentException {
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

    private static void addReceiptCode(Document document, String invoiceCode) throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);

        Paragraph para = new Paragraph(new Chunk("Numéro de reçu: " + invoiceCode, font));

        document.add(para);
    }

    private static void addReceiptDetails(Document document, ReceiptPDF receiptPDF) throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

        document.add(new Paragraph(new Chunk("Date de reçu: " + receiptPDF.getDateReceiptString(), font)));
    }

    private static void addReceiptItems(Document document, ReceiptPDF receiptPDF) throws DocumentException {

        PdfPTable table = new PdfPTable(new float[] { 50, 11, 13, 13 });
        table.setWidthPercentage(95);

        addTableHeader(table);

        addTableRows(table, receiptPDF.getItems());

        addTableTotals(table, receiptPDF);

        addTableInfo(table, receiptPDF);

        document.add(table);
    }

    private static void addTableHeader(PdfPTable table) {
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

    private static void addTableRows(PdfPTable table, List<ReceiptItemPDF> items) {
        for (ReceiptItemPDF item : items) {
            table.addCell(getItemCell(item.getDescription(), Element.ALIGN_LEFT));
            table.addCell(getItemCell(item.getQuantity(), Element.ALIGN_RIGHT));
            table.addCell(getItemCell(item.getUnitaryPriceString(), Element.ALIGN_RIGHT));
            table.addCell(getItemCell(item.getTotalString(), Element.ALIGN_RIGHT));
        }
    }

    private static PdfPCell getItemCell(String value, int alignment) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);

        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(alignment);
        cell.setPhrase(new Phrase(value, font));

        return cell;

    }

    private static void addTableTotals(PdfPTable table, ReceiptPDF receiptPDF) {

        addTotal(table, "Total HT:", receiptPDF.getTotalHT(), 0);

        addTotal(table, "TVA 18%:", receiptPDF.getTotalTVA(), 0);

        addTotal(table, "Total TTC:", receiptPDF.getTotalTTC(), 1);

    }

    private static void addTableInfo(PdfPTable table, ReceiptPDF receiptPDF) {

        addInfo(table, receiptPDF.getTypePaymentLabel() + ":", receiptPDF.getTypePaymentAmount(), 1);

        addInfo(table, "Paid Amount:", receiptPDF.getPaidAmount(), 0);

        addInfo(table, "Change:", receiptPDF.getChangeAmount(), 0);

    }

    private static PdfPCell getTotalCell(String value, int alignment, int border) {

        PdfPCell totalCell = new PdfPCell();
        totalCell.setBorder(border);
        totalCell.setVerticalAlignment(alignment);
        totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalCell.setPhrase(new Phrase(value, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK)));

        return totalCell;

    }

    private static PdfPCell getInfoCell(String value, int alignment, int border) {

        PdfPCell infoCell = new PdfPCell();
        infoCell.setBorder(border);
        infoCell.setVerticalAlignment(alignment);
        infoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        infoCell.setPhrase(new Phrase(value, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK)));

        return infoCell;

    }

    private static void addTotal(PdfPTable table, String totalLabel, String totalAmount, int border) {

        PdfPCell total = new PdfPCell();
        total.setColspan(2);
        total.setBorder(border);
        table.addCell(total);
        table.addCell(getTotalCell(totalLabel, Element.ALIGN_LEFT, border));
        table.addCell(getTotalCell(totalAmount, Element.ALIGN_MIDDLE, border));
    }

    private static void addInfo(PdfPTable table, String infoLabel, String infoAmount, int border) {

        PdfPCell info = new PdfPCell();
        info.setColspan(2);
        info.setBorder(border);
        table.addCell(info);
        table.addCell(getInfoCell(infoLabel, Element.ALIGN_LEFT, border));
        table.addCell(getInfoCell(infoAmount, Element.ALIGN_MIDDLE, border));
    }

}
