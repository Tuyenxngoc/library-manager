package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.constant.PenaltyForm;
import com.example.librarymanager.domain.entity.ReaderViolation;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GetReaderViolationResponseDto {
    private final Long id; // ID của vi phạm

    private final String violationDetails; // Nội dung vi phạm

    private final PenaltyForm penaltyForm; // Hình thức phạt

    private final String otherPenaltyForm; // Hình thức phạt khác (nếu có)

    private final LocalDate penaltyDate; // Ngày phạt

    private final LocalDate endDate; // Ngày kết thúc

    private final Double fineAmount; // Số tiền phạt

    private final String notes; // Ghi chú

    private final long readerId;

    private final String cardNumber; // ID của bạn đọc liên kết

    private final String fullName; // Họ tên của bạn đọc liên kết

    public GetReaderViolationResponseDto(ReaderViolation violation) {
        this.id = violation.getId();
        this.violationDetails = violation.getViolationDetails();
        this.penaltyForm = violation.getPenaltyForm();
        this.otherPenaltyForm = violation.getOtherPenaltyForm();
        this.penaltyDate = violation.getPenaltyDate();
        this.endDate = violation.getEndDate();
        this.fineAmount = violation.getFineAmount();
        this.notes = violation.getNotes();
        this.readerId = violation.getReader().getId();
        this.cardNumber = violation.getReader().getCardNumber();
        this.fullName = violation.getReader().getFullName();
    }
}
