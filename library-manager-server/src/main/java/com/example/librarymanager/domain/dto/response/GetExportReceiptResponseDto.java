package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.domain.entity.ExportReceipt;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GetExportReceiptResponseDto {

    private final long id;

    private final String receiptNumber;

    private final LocalDate exportDate;

    private final String exportReason;

    public GetExportReceiptResponseDto(ExportReceipt exportReceipt) {
        this.id = exportReceipt.getId();
        this.receiptNumber = exportReceipt.getReceiptNumber();
        this.exportDate = exportReceipt.getExportDate();
        this.exportReason = exportReceipt.getExportReason();
    }
}
