package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.domain.dto.request.BookRequestDto;
import com.example.librarymanager.domain.entity.Book;
import com.example.librarymanager.domain.entity.ImportReceipt;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@AllArgsConstructor
public class GetImportReceiptResponseDto {

    private final LocalDateTime createdDate;

    private final LocalDateTime lastModifiedDate;

    private final String createdBy;

    private final String lastModifiedBy;

    private final Long id;

    private final String receiptNumber;  // Số phiếu nhập

    private final LocalDate importDate;  // Ngày nhập

    private final String generalRecordNumber;  // Số vào sổ tổng quát

    private final String fundingSource;  // Nguồn cấp

    private final String importReason;  // Lý do

    private Set<BookRequestDto> books = new HashSet<>(); // Danh sách sách

    public GetImportReceiptResponseDto(ImportReceipt importReceipt) {
        this.createdDate = importReceipt.getCreatedDate();
        this.lastModifiedDate = importReceipt.getLastModifiedDate();
        this.createdBy = importReceipt.getCreatedBy();
        this.lastModifiedBy = importReceipt.getLastModifiedBy();

        this.id = importReceipt.getId();
        this.receiptNumber = importReceipt.getReceiptNumber();
        this.importDate = importReceipt.getImportDate();
        this.generalRecordNumber = importReceipt.getGeneralRecordNumber();
        this.fundingSource = importReceipt.getFundingSource();
        this.importReason = importReceipt.getImportReason();

        Map<Long, Integer> bookCountMap = new HashMap<>();

        List<Book> books = importReceipt.getBook();
        for (Book book : books) {
            Long bookDefinitionId = book.getBookDefinition().getId();
            if (bookCountMap.containsKey(bookDefinitionId)) {
                bookCountMap.put(bookDefinitionId, bookCountMap.get(bookDefinitionId) + 1);
            } else {
                bookCountMap.put(bookDefinitionId, 1);
            }
        }

        for (Map.Entry<Long, Integer> entry : bookCountMap.entrySet()) {
            Long id = entry.getKey();
            Integer quantity = entry.getValue();

            this.books.add(new BookRequestDto(id, quantity));
        }
    }
}
