package com.pad.server.base.services.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.Anchor;
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
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.pad.server.base.entities.PDF;
import com.pad.server.base.entities.PDFItem;

@Service
@Transactional
public class PdfServiceImpl implements PdfService {

    // private static final Logger logger = Logger.getLogger(PdfServiceImpl.class);

    @Value("${system.url.local}")
    private String systemUrlLocal;

    @Value("${system.url.images}")
    private String systemUrlImages;

    private Font   font            = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, new BaseColor(73, 80, 87));
    private Font   fontClient      = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(73, 80, 87));
    private Font   fontBold        = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
    private Font   fontInvoiceCode = new Font(Font.FontFamily.HELVETICA, 20, Font.NORMAL, new BaseColor(175, 156, 101));
    private Font   fontHeaderBold  = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, new BaseColor(73, 80, 87));

    @Override
    public File generatePdf(PDF pdf, String fileName) throws Exception {

        File file = new File(fileName);
        file.getParentFile().mkdirs();
        file.createNewFile();

        Document document = new Document();
        document.setMargins(36, 36, 10, 15);

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

        if (pdf.getType().equals(PDF.Type.INVOICE)) {
            writer.setPageEvent(new HeaderFooterInvoicePageEvent());
        } else {
            writer.setPageEvent(new HeaderFooterPageEvent());
        }

        addMetadata(document, pdf);

        document.open();

        PdfPTable header = new PdfPTable(2);
        header.setWidths(new int[] { 9, 24 });
        header.setTotalWidth(527);
        header.setLockedWidth(true);
        header.getDefaultCell().setBorder(Rectangle.BOTTOM);
        header.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

        PdfPCell cellImage = new PdfPCell();
        cellImage.setImage(Image.getInstance(systemUrlLocal + systemUrlImages + "ags_logo_big.jpg"));
        cellImage.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellImage.setBorder(Rectangle.BOTTOM);
        cellImage.setBorderColor(BaseColor.LIGHT_GRAY);
        cellImage.setPaddingBottom(5);
        header.addCell(cellImage);

        PdfPCell text = new PdfPCell();
        text.setBorder(Rectangle.BOTTOM);
        text.setBorderColor(BaseColor.LIGHT_GRAY);
        text.addElement(new Phrase("AGS", fontHeaderBold));
        text.addElement(new Phrase("Aménagement Gestion Services SARL", font));
        text.addElement(new Phrase("Rocade Fann Bel Air", font));
        text.addElement(new Phrase("Dakar 20941", font));
        text.addElement(new Phrase("Sénégal", font));
        text.setPaddingBottom(5);
        header.addCell(text);

        document.add(header);

        // CLIENT
        document.add(new Paragraph(new Chunk("BREAK LINE", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE))));

        if (!pdf.getType().equals(PDF.Type.RECEIPT)) {
            Paragraph client = new Paragraph(new Chunk(pdf.getClient(), fontClient));
            client.setLeading(0, 2);
            client.setAlignment(Element.ALIGN_RIGHT);
            document.add(client);
        }

        // CODE
        Paragraph invoiceCode = new Paragraph(new Chunk(pdf.getType().getLabel() + " " + pdf.getReference(), fontInvoiceCode));
        invoiceCode.setAlignment(Element.ALIGN_LEFT);
        document.add(invoiceCode);

        document.add(new Paragraph("BREAK LINE", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE)));

        PdfPTable table;

        if (pdf.getType().equals(PDF.Type.INVOICE)) {

            table = new PdfPTable(new float[] { 33, 33, 33 });
            table.setWidthPercentage(100);

            Stream.of("Date de la facture:", "Date d'échéance:", "Période:").forEach(columnTitle -> {
                PdfPCell title = new PdfPCell();
                title.setVerticalAlignment(Element.ALIGN_MIDDLE);
                title.setHorizontalAlignment(Element.ALIGN_LEFT);
                title.setBorder(0);
                title.setPhrase(new Phrase(columnTitle, fontBold));

                table.addCell(title);
            });

            Stream.of(pdf.getDateString(), pdf.getDatePaymentDueString(), pdf.getDatePeriodString()).forEach(column -> {
                PdfPCell cell = new PdfPCell();
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.BOTTOM);
                cell.setBorderColor(BaseColor.LIGHT_GRAY);
                cell.setPhrase(new Phrase(column, font));

                table.addCell(cell);
            });
        } else if (pdf.getType().equals(PDF.Type.STATEMENT)) {

            table = new PdfPTable(new float[] { 50, 50 });
            table.setWidthPercentage(100);

            Stream.of("Date du relevé de compte:", "Période:").forEach(columnTitle -> {
                PdfPCell title = new PdfPCell();
                title.setVerticalAlignment(Element.ALIGN_MIDDLE);
                title.setHorizontalAlignment(Element.ALIGN_LEFT);
                title.setBorder(0);
                title.setPhrase(new Phrase(columnTitle, fontBold));

                table.addCell(title);
            });

            Stream.of(pdf.getDateString(), pdf.getDatePeriodString()).forEach(column -> {
                PdfPCell cell = new PdfPCell();
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.BOTTOM);
                cell.setBorderColor(BaseColor.LIGHT_GRAY);
                cell.setPhrase(new Phrase(column, font));

                table.addCell(cell);
            });
        } else {

            table = new PdfPTable(new float[] { 50, 50 });
            table.setWidthPercentage(100);

            Stream.of("Date de reçu:", StringUtils.isNotBlank(pdf.getVehicleRegNumber()) ? "Véhicule:" : "").forEach(columnTitle -> {
                PdfPCell title = new PdfPCell();
                title.setVerticalAlignment(Element.ALIGN_MIDDLE);
                title.setHorizontalAlignment(Element.ALIGN_LEFT);
                title.setBorder(0);
                title.setPhrase(new Phrase(columnTitle, fontBold));

                table.addCell(title);
            });

            Stream.of(pdf.getDateTimeString(), StringUtils.isNotBlank(pdf.getVehicleRegNumber()) ? pdf.getVehicleRegNumber() : "").forEach(column -> {
                PdfPCell cell = new PdfPCell();
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.BOTTOM);
                cell.setBorderColor(BaseColor.LIGHT_GRAY);
                cell.setPhrase(new Phrase(column, font));

                table.addCell(cell);
            });

        }

        document.add(table);

        document.add(new Paragraph(new Chunk("BREAK LINE", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE))));

        addItems(document, pdf);

        document.add(Chunk.NEWLINE);

        if (pdf.getType().equals(PDF.Type.INVOICE)) {
            document.add(new Paragraph(new Chunk("Merci d'utiliser la communication suivante pour votre paiement: " + pdf.getReference(), font)));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph(new Chunk("Moyen de payement : chèque, virement bancaire, espèces", font)));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph(new Chunk("Banque : CBAO : SN012 01307 036191694101 90", font)));
            document.add(Chunk.NEWLINE);
        }

        if (!pdf.getType().equals(PDF.Type.RECEIPT)) {
            if (StringUtils.isNotBlank(pdf.getAccountBalance())) {
                document.add(new Paragraph(new Chunk("Le solde du compte au " + pdf.getDateAccountBalanceString() + " : " + pdf.getAccountBalance(), font)));
            }
        }

        document.close();

        return file;
    }

    private void addMetadata(Document document, PDF pdf) {
        document.addTitle("AGS PAD " + pdf.getType().getLabel());
        document.addAuthor("AGS PAD System");
        document.addSubject("Monthly AGS PAD " + pdf.getType().getLabel());
        document.addKeywords("Metadata, iText, PDF");
        document.addCreator("AGS PAD System");
    }

    private void addItems(Document document, PDF pdf) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[] { 50, 6, 14, 14, 16 });
        table.setWidthPercentage(100);

        addTableHeader(table);

        addTableRows(table, pdf.getItems());

        addTableTotals(table, pdf);

        document.add(table);
    }

    private void addTableHeader(PdfPTable table) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);

        PdfPCell headerDesc = new PdfPCell();
        headerDesc.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerDesc.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerDesc.setBorder(0);
        headerDesc.setPhrase(new Phrase("Description", font));
        table.addCell(headerDesc);

        PdfPCell headerCountry = new PdfPCell();
        headerCountry.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerCountry.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCountry.setBorder(0);
        headerCountry.setPhrase(new Phrase("Pays", font));
        table.addCell(headerCountry);

        PdfPCell headerQuant = new PdfPCell();
        headerQuant.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerQuant.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerQuant.setBorder(0);
        headerQuant.setPhrase(new Phrase("Quantité", font));
        table.addCell(headerQuant);

        PdfPCell headerTax = new PdfPCell();
        headerTax.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerTax.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerTax.setBorder(0);
        headerTax.setPhrase(new Phrase("Taxes", font));
        table.addCell(headerTax);

        PdfPCell headerMontant = new PdfPCell();
        headerMontant.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerMontant.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerMontant.setBorder(0);
        headerMontant.setPhrase(new Phrase("Montant", font));
        table.addCell(headerMontant);
    }

    private void addTableRows(PdfPTable table, List<PDFItem> items) {
        int index = 0;
        for (PDFItem item : items) {
            table.addCell(getItemCell(item.getDescription(), Element.ALIGN_LEFT, index));
            table.addCell(getItemCell(item.getVehicleRegCountryISO(), Element.ALIGN_CENTER, index));
            table.addCell(getItemCell(item.getQuantity(), Element.ALIGN_RIGHT, index));
            table.addCell(getItemCell("TVA 18% (vente)", Element.ALIGN_RIGHT, index));
            table.addCell(getItemCell(item.getTotalString(), Element.ALIGN_RIGHT, index));

            index++;
        }
    }

    private PdfPCell getItemCell(String value, int alignment, int index) {
        Font font = new Font(Font.FontFamily.HELVETICA, 10);
        font.setColor(new BaseColor(73, 80, 87));

        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(0);
        cell.setPhrase(new Phrase(value, font));

        if (index % 2 == 0) {
            cell.setBackgroundColor(new BaseColor(247, 244, 246));
        }

        return cell;
    }

    private void addTableTotals(PdfPTable table, PDF pdf) {

        Font font = new Font(Font.FontFamily.HELVETICA, 10);
        font.setColor(new BaseColor(73, 80, 87));

        Font bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);

        PdfPCell cellNoBorder = new PdfPCell();
        cellNoBorder.setBorder(0);

        PdfPCell cellBorder = new PdfPCell();
        cellBorder.setBorder(Rectangle.TOP);
        cellBorder.setBorderColor(BaseColor.LIGHT_GRAY);

        table.addCell(cellNoBorder);
        table.addCell(cellNoBorder);
        table.addCell(getTotalCell("Sous-total", Element.ALIGN_LEFT, 1, bold));
        table.addCell(cellBorder);
        table.addCell(getTotalCell(pdf.getTotalHT(), Element.ALIGN_MIDDLE, 1, font));

        table.addCell(cellNoBorder);
        table.addCell(cellNoBorder);
        table.addCell(getTotalCell("TVA 18%", Element.ALIGN_LEFT, 0, font));
        table.addCell(cellNoBorder);
        table.addCell(getTotalCell(pdf.getTotalTVA(), Element.ALIGN_MIDDLE, 0, font));

        table.addCell(cellNoBorder);
        table.addCell(cellNoBorder);
        table.addCell(getTotalCell("Total", Element.ALIGN_LEFT, 1, bold));
        table.addCell(cellBorder);
        table.addCell(getTotalCell(pdf.getTotalTTC(), Element.ALIGN_MIDDLE, 1, font));

        if (pdf.getType().equals(PDF.Type.RECEIPT)) {
            table.addCell(cellNoBorder);
            table.addCell(cellNoBorder);
            table.addCell(getTotalCell(pdf.getTypePaymentLabel(), Element.ALIGN_LEFT, 1, font));
            table.addCell(cellBorder);
            table.addCell(getTotalCell(pdf.getTypePaymentAmount(), Element.ALIGN_MIDDLE, 1, font));

            table.addCell(cellNoBorder);
            table.addCell(cellNoBorder);
            table.addCell(getTotalCell("Montant Payé", Element.ALIGN_LEFT, 0, font));
            table.addCell(cellNoBorder);
            table.addCell(getTotalCell(pdf.getPaidAmount(), Element.ALIGN_MIDDLE, 0, font));

            table.addCell(cellNoBorder);
            table.addCell(cellNoBorder);
            table.addCell(getTotalCell("Changement", Element.ALIGN_LEFT, 0, font));
            table.addCell(cellNoBorder);
            table.addCell(getTotalCell(pdf.getChangeAmount(), Element.ALIGN_MIDDLE, 0, font));
        }

    }

    private PdfPCell getTotalCell(String value, int alignment, int border, Font font) {

        PdfPCell totalCell = new PdfPCell();
        if (border == 1) {
            totalCell.setBorder(Rectangle.TOP);
            totalCell.setBorderColor(BaseColor.LIGHT_GRAY);
        } else {
            totalCell.setBorder(border);
        }

        totalCell.setVerticalAlignment(alignment);
        totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalCell.setPhrase(new Phrase(value, font));

        return totalCell;

    }

    public class HeaderFooterInvoicePageEvent extends PdfPageEventHelper {

        private PdfTemplate t;
        private Image       total;

        private Font        footerFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, new BaseColor(73, 80, 87));

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            t = writer.getDirectContent().createTemplate(30, 16);
            try {
                total = Image.getInstance(t);
                total.setRole(PdfName.ARTIFACT);
            } catch (Exception de) {
                de.printStackTrace();
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            addFooter(writer);
        }

        private void addFooter(PdfWriter writer) {
            PdfPTable footer = new PdfPTable(3);

            try {
                // set defaults
                footer.setWidths(new int[] { 33, 33, 33 });
                footer.setTotalWidth(527);
                footer.setLockedWidth(true);

                PdfPCell cellNINEA = new PdfPCell();
                cellNINEA.setBorder(Rectangle.TOP);
                cellNINEA.setBorderColor(BaseColor.LIGHT_GRAY);
                cellNINEA.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellNINEA.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellNINEA.setPhrase(new Phrase("NINEA: 0070277342Y2", footerFont));
                footer.addCell(cellNINEA);

                PdfPCell cellRCCM = new PdfPCell();
                cellRCCM.setBorder(Rectangle.TOP);
                cellRCCM.setBorderColor(BaseColor.LIGHT_GRAY);
                cellRCCM.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellRCCM.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellRCCM.setPhrase(new Phrase("RCCM: SN DKR-2018-B-25972", footerFont));
                footer.addCell(cellRCCM);

                PdfPCell cellCBAO = new PdfPCell();
                cellCBAO.setBorder(Rectangle.TOP);
                cellCBAO.setBorderColor(BaseColor.LIGHT_GRAY);
                cellCBAO.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellCBAO.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellCBAO.setPhrase(new Phrase("CBAO: SN012 01307 036191694101 90", footerFont));
                footer.addCell(cellCBAO);

                PdfPCell cellWeb = new PdfPCell();
                cellWeb.setBorder(Rectangle.BOTTOM);
                cellWeb.setBorderColor(BaseColor.LIGHT_GRAY);
                cellWeb.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellWeb.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellWeb.setPaddingBottom(3);

                Phrase web = new Phrase();
                web.setFont(footerFont);
                Chunk chunk = new Chunk("Web: http://www.agsparking.com");
                chunk.setAnchor("http://www.agsparking.com");
                web.add(chunk);
                cellWeb.setPhrase(web);
                footer.addCell(cellWeb);

                PdfPCell cellEmail = new PdfPCell();
                cellEmail.setBorder(Rectangle.BOTTOM);
                cellEmail.setBorderColor(BaseColor.LIGHT_GRAY);
                cellEmail.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellEmail.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellEmail.setPaddingBottom(3);
                Phrase email = new Phrase("Email: info@agsparking.com", footerFont);
                Anchor anchor = new Anchor();
                anchor.setReference("mailto:info@agsparking.com");
                email.add(anchor);
                cellEmail.setPhrase(email);
                footer.addCell(cellEmail);

                PdfPCell cellPhone = new PdfPCell();
                cellPhone.setBorder(Rectangle.BOTTOM);
                cellPhone.setBorderColor(BaseColor.LIGHT_GRAY);
                cellPhone.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellPhone.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellPhone.setPaddingBottom(3);
                cellPhone.setPhrase(new Phrase("Tel: +221 33 832 61 61", footerFont));
                footer.addCell(cellPhone);

                PdfPCell cellPage = new PdfPCell();
                cellPage.setBorder(0);
                cellPage.setColspan(3);
                cellPage.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellPage.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cellPage.setPhrase(new Phrase("Page: 1 sur 1", font));
                footer.addCell(cellPage);

                // write page
                PdfContentByte canvas = writer.getDirectContent();
                canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
                footer.writeSelectedRows(0, -1, 34, 50, canvas);
                canvas.endMarkedContentSequence();
            } catch (Exception de) {
                de.printStackTrace();
            }
        }

    }

    public class HeaderFooterPageEvent extends PdfPageEventHelper {

        private PdfTemplate t;
        private Image       total;

        private Font        footerFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, new BaseColor(73, 80, 87));

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            t = writer.getDirectContent().createTemplate(30, 16);
            try {
                total = Image.getInstance(t);
                total.setRole(PdfName.ARTIFACT);
            } catch (Exception de) {
                de.printStackTrace();
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            addFooter(writer);
        }

        private void addFooter(PdfWriter writer) {
            PdfPTable footer = new PdfPTable(3);

            try {
                // set defaults
                footer.setWidths(new int[] { 33, 33, 33 });
                footer.setTotalWidth(527);
                footer.setLockedWidth(true);

                PdfPCell cellNINEA = new PdfPCell();
                cellNINEA.setBorder(Rectangle.TOP);
                cellNINEA.setBorderColor(BaseColor.LIGHT_GRAY);
                cellNINEA.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellNINEA.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellNINEA.setPhrase(new Phrase("NINEA: 0070277342Y2", footerFont));
                footer.addCell(cellNINEA);

                PdfPCell cellRCCM = new PdfPCell();
                cellRCCM.setBorder(Rectangle.TOP);
                cellRCCM.setBorderColor(BaseColor.LIGHT_GRAY);
                cellRCCM.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellRCCM.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellRCCM.setPhrase(new Phrase("RCCM: SN DKR-2018-B-25972", footerFont));
                footer.addCell(cellRCCM);

                PdfPCell cellCBAO = new PdfPCell();
                cellCBAO.setBorder(Rectangle.TOP);
                cellCBAO.setBorderColor(BaseColor.LIGHT_GRAY);
                cellCBAO.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellCBAO.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellCBAO.setPhrase(new Phrase("", footerFont));
                footer.addCell(cellCBAO);

                PdfPCell cellWeb = new PdfPCell();
                cellWeb.setBorder(Rectangle.BOTTOM);
                cellWeb.setBorderColor(BaseColor.LIGHT_GRAY);
                cellWeb.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellWeb.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellWeb.setPaddingBottom(3);

                Phrase web = new Phrase();
                web.setFont(footerFont);
                Chunk chunk = new Chunk("Web: http://www.agsparking.com");
                chunk.setAnchor("http://www.agsparking.com");
                web.add(chunk);
                cellWeb.setPhrase(web);
                footer.addCell(cellWeb);

                PdfPCell cellEmail = new PdfPCell();
                cellEmail.setBorder(Rectangle.BOTTOM);
                cellEmail.setBorderColor(BaseColor.LIGHT_GRAY);
                cellEmail.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellEmail.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellEmail.setPaddingBottom(3);
                Phrase email = new Phrase("Email: info@agsparking.com", footerFont);
                Anchor anchor = new Anchor();
                anchor.setReference("mailto:info@agsparking.com");
                email.add(anchor);
                cellEmail.setPhrase(email);
                footer.addCell(cellEmail);

                PdfPCell cellPhone = new PdfPCell();
                cellPhone.setBorder(Rectangle.BOTTOM);
                cellPhone.setBorderColor(BaseColor.LIGHT_GRAY);
                cellPhone.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellPhone.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellPhone.setPaddingBottom(3);
                cellPhone.setPhrase(new Phrase("Tel: +221 33 832 61 61", footerFont));
                footer.addCell(cellPhone);

                PdfPCell cellPage = new PdfPCell();
                cellPage.setBorder(0);
                cellPage.setColspan(3);
                cellPage.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellPage.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cellPage.setPhrase(new Phrase("Page: 1 sur 1", font));
                footer.addCell(cellPage);

                // write page
                PdfContentByte canvas = writer.getDirectContent();
                canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
                footer.writeSelectedRows(0, -1, 34, 50, canvas);
                canvas.endMarkedContentSequence();
            } catch (Exception de) {
                de.printStackTrace();
            }
        }

    }

}
