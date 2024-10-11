package com.example.librarymanager.service.impl;

import com.example.librarymanager.domain.dto.request.CreateReaderCardsRequestDto;
import com.example.librarymanager.domain.entity.Reader;
import com.example.librarymanager.service.PdfService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    private static final String FONT_PATH = "src/main/resources/fonts/NotoSans-Regular.ttf";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public byte[] createPdf(CreateReaderCardsRequestDto requestDto, List<Reader> readers) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 10, 10, 10, 10);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            // Font settings
            BaseFont baseFont = BaseFont.createFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font headerFont = new Font(baseFont, 14, Font.BOLD);
            Font normalFont = new Font(baseFont, 12);
            Font smallFont = new Font(baseFont, 10);

            // Number of cards per page (8 cards: 4 rows, 2 columns)
            int cardsPerPage = 8;
            int totalReaders = readers.size();
            int totalPages = (int) Math.ceil((double) totalReaders / cardsPerPage);

            for (int pageIndex = 0; pageIndex < totalPages; pageIndex++) {
                PdfPTable mainTable = new PdfPTable(2);
                mainTable.setWidthPercentage(100);
                mainTable.setSpacingBefore(10f);
                mainTable.setSpacingAfter(10f);

                // Add 8 cards (or less if it's the last page)
                int lop = Math.min((pageIndex + 1) * cardsPerPage, totalReaders);
                for (int i = pageIndex * cardsPerPage; i < lop; i++) {
                    Reader reader = readers.get(i);
                    PdfPTable cardContainer = new PdfPTable(1);
                    cardContainer.setWidthPercentage(100);

                    PdfPCell headerCell = new PdfPCell();
                    headerCell.addElement(createParagraph(requestDto.getManagementUnit(), normalFont, Element.ALIGN_CENTER));
                    headerCell.addElement(createParagraph(requestDto.getSchoolName(), headerFont, Element.ALIGN_CENTER));
                    headerCell.setBorder(Rectangle.NO_BORDER);
                    cardContainer.addCell(headerCell);

                    PdfPTable cardContentTable = new PdfPTable(2);
                    cardContentTable.setWidthPercentage(100);
                    cardContentTable.setWidths(new int[]{1, 3});

                    PdfPTable avatarTable = createAvatarTable(writer, reader);
                    PdfPCell avatarCell = new PdfPCell(avatarTable);
                    avatarCell.setPadding(5);
                    avatarCell.setBorder(Rectangle.NO_BORDER);
                    cardContentTable.addCell(avatarCell);

                    PdfPCell infoCell = createInfoCell(baseFont, requestDto, reader);
                    cardContentTable.addCell(infoCell);

                    PdfPCell cardCell = new PdfPCell(cardContentTable);
                    cardCell.setPadding(5);
                    cardCell.setBorder(Rectangle.NO_BORDER);
                    cardContainer.addCell(cardCell);

                    mainTable.addCell(cardContainer);
                }

                if (lop % 2 == 1) {
                    mainTable.completeRow();
                }

                document.add(mainTable);

                // Add new page except for the last page
                if (pageIndex < totalPages - 1) {
                    document.newPage();
                }
            }

            document.close();
        } catch (DocumentException | IOException e) {
            log.error("Error creating PDF: {}", e.getMessage(), e);
        }

        return outputStream.toByteArray();
    }

    private PdfPTable createAvatarTable(PdfWriter writer, Reader reader) throws IOException, DocumentException {
        PdfPTable avatarTable = new PdfPTable(1);
        avatarTable.setWidthPercentage(100);

        PdfPCell avatarImageCell = new PdfPCell();
        avatarImageCell.setFixedHeight(90);
        String avatarUrl = reader.getAvatar();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Image avatarImage = Image.getInstance(avatarUrl);
            avatarImage.scaleToFit(75, 75);
            avatarImage.setAlignment(Element.ALIGN_CENTER);
            avatarImageCell.addElement(avatarImage);
            avatarImageCell.setBorder(Rectangle.NO_BORDER);
        } else {
            avatarImageCell.setBorder(Rectangle.BOX);
        }
        avatarTable.addCell(avatarImageCell);

        PdfPCell barcodeCell = new PdfPCell();
        barcodeCell.setPaddingTop(10);
        barcodeCell.setBorder(Rectangle.NO_BORDER);

        Barcode128 barcode = new Barcode128();
        barcode.setCode(reader.getCardNumber());
        Image barcodeImage = barcode.createImageWithBarcode(writer.getDirectContent(), null, null);
        barcodeImage.setAlignment(Element.ALIGN_CENTER);
        barcodeImage.scalePercent(80);
        barcodeCell.addElement(barcodeImage);

        avatarTable.addCell(barcodeCell);

        return avatarTable;
    }

    private PdfPCell createInfoCell(BaseFont baseFont, CreateReaderCardsRequestDto requestDto, Reader reader) {
        PdfPCell infoCell = new PdfPCell();
        infoCell.setBorder(Rectangle.NO_BORDER);

        Font boldFont = new Font(baseFont, 16, Font.BOLD);
        Font normalFont = new Font(baseFont, 12);

        infoCell.addElement(createParagraph("THẺ THƯ VIỆN", boldFont, Element.ALIGN_CENTER));
        infoCell.addElement(new Paragraph(String.format("Họ và tên: %s", reader.getFullName()), normalFont));
        infoCell.addElement(new Paragraph("Lớp học: 6/1", normalFont));
        infoCell.addElement(new Paragraph(String.format("Ngày sinh: %s", reader.getDateOfBirth().format(formatter)), normalFont));
        infoCell.addElement(new Paragraph(String.format("Ngày hết hạn: %s", reader.getExpiryDate() != null ? reader.getExpiryDate().format(formatter) : "Không có"), normalFont));
        infoCell.addElement(createParagraph("Ban giám hiệu", normalFont, Element.ALIGN_CENTER));
        infoCell.addElement(createParagraph(requestDto.getPrincipalName().toUpperCase(), normalFont, Element.ALIGN_CENTER));

        return infoCell;
    }

    private Paragraph createParagraph(String text, Font font, int alignment) {
        Paragraph paragraph = new Paragraph(text, font);
        paragraph.setAlignment(alignment);
        return paragraph;
    }
}
