package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.domain.entity.ExportReceipt;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GetExportReceiptResponseDto {

    private final long id;

    private final String receiptNumber;  // Số phiếu xuất

    private final LocalDate exportDate;  // Ngày xuất (Ngày lập phiếu)

    private final String exportReason;  // Lý do xuất (không bắt buộc)

    public GetExportReceiptResponseDto(ExportReceipt exportReceipt) {
        this.id = exportReceipt.getId();
        this.receiptNumber = exportReceipt.getReceiptNumber();
        this.exportDate = exportReceipt.getExportDate();
        this.exportReason = exportReceipt.getExportReason();
    }
}
