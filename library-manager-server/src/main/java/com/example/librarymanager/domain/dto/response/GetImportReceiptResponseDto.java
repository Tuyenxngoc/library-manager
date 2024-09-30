package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.domain.entity.ImportReceipt;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    }
}
