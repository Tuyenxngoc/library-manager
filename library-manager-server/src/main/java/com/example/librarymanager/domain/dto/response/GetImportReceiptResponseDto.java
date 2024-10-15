package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.domain.dto.common.DateAuditingDto;
import com.example.librarymanager.domain.dto.request.BookRequestDto;
import com.example.librarymanager.domain.entity.Book;
import com.example.librarymanager.domain.entity.ImportReceipt;
import lombok.Getter;

import java.time.LocalDate;
import java.util.*;

@Getter
public class GetImportReceiptResponseDto extends DateAuditingDto {

    private final Long id;

    private final String receiptNumber;

    private final LocalDate importDate;

    private final String generalRecordNumber;

    private final String fundingSource;

    private final String importReason;

    private final Set<BookRequestDto> books = new HashSet<>();

    public GetImportReceiptResponseDto(ImportReceipt importReceipt) {
        this.createdDate = importReceipt.getCreatedDate();
        this.lastModifiedDate = importReceipt.getLastModifiedDate();
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
