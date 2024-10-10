package com.example.librarymanager.service.impl;

import com.example.librarymanager.service.PdfService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    public static final String NOTOSAN = "src/main/resources/fonts/NotoSans-Regular.ttf";

    @Override
    public byte[] createPdf(String content) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 10, 10, 10, 10); // Set page size to A4 with margins

        try {
            PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            // Font settings
            BaseFont unicodeFont = BaseFont.createFont(NOTOSAN, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font headerFont = new Font(unicodeFont, 14, Font.BOLD);
            Font normalFont = new Font(unicodeFont, 12);
            Font smallFont = new Font(unicodeFont, 10);

            // Table layout for 8 cards (4 rows, 2 columns)
            PdfPTable outerTable = new PdfPTable(2); // 2 columns
            outerTable.setWidthPercentage(100);
            outerTable.setSpacingBefore(10f);
            outerTable.setSpacingAfter(10f);

            // Create 8 library cards
            for (int i = 0; i < 8; i++) {
                // Inner table for a single card
                PdfPTable cardTable = new PdfPTable(2); // 2 columns
                cardTable.setWidthPercentage(100);
                cardTable.setWidths(new int[]{1, 2}); // Column width ratio

                // Image placeholder (for now, it's blank)
                PdfPCell imageCell = new PdfPCell();
                imageCell.setBorder(Rectangle.BOX);
                imageCell.setFixedHeight(100); // Adjust as per your need
                cardTable.addCell(imageCell);

                // Information for the card
                PdfPCell infoCell = new PdfPCell();
                infoCell.setBorder(Rectangle.BOX);
                infoCell.addElement(new Paragraph("TÊN ĐƠN VỊ CHỦ QUẢN 1", smallFont));
                infoCell.addElement(new Paragraph("Trường THCS Nguyễn Huệ", headerFont));
                infoCell.addElement(new Paragraph("THẺ THƯ VIỆN", new Font(unicodeFont, 16, Font.BOLD)));
                infoCell.addElement(new Paragraph("Họ và tên: Nguyễn Trần Phúc An", normalFont));
                infoCell.addElement(new Paragraph("Lớp học: 6/1", normalFont));
                infoCell.addElement(new Paragraph("Ngày sinh: 05/11/2010", normalFont));
                infoCell.addElement(new Paragraph("Năm học: 2021 - 2022", normalFont));
                infoCell.addElement(new Paragraph("Ban giám hiệu", normalFont));
                cardTable.addCell(infoCell);

                // Add card table to outer table
                PdfPCell cardCell = new PdfPCell(cardTable);
                cardCell.setPadding(10);
                cardCell.setBorder(Rectangle.NO_BORDER); // No border between cards
                outerTable.addCell(cardCell);

//                // Barcode for one of the cards (optional)
//                Barcode128 barcode = new Barcode128();
//                barcode.setCode("2102729238");
//                Image barcodeImage = barcode.createImageWithBarcode(writer.getDirectContent(), null, null);
//                barcodeImage.setAlignment(Element.ALIGN_LEFT);
//                barcodeImage.scalePercent(100);
//                document.add(barcodeImage);
//
//                // Signature (optional)
//                Paragraph signature = new Paragraph("THÂN ĐỨC HÙNG", normalFont);
//                signature.setAlignment(Element.ALIGN_RIGHT);
//                document.add(signature);
            }

            // Add outer table to document
            document.add(outerTable);

            document.close();
        } catch (DocumentException | IOException e) {
            log.error(e.getMessage(), e);
        }

        return byteArrayOutputStream.toByteArray();
    }
}
