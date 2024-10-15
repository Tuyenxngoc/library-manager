package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.constant.PenaltyForm;
import com.example.librarymanager.domain.dto.common.DateAuditingDto;
import com.example.librarymanager.domain.entity.ReaderViolation;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GetReaderViolationResponseDto extends DateAuditingDto {
    private final long id;

    private final String violationDetails;

    private final PenaltyForm penaltyForm;

    private final String otherPenaltyForm;

    private final LocalDate penaltyDate;

    private final LocalDate endDate;

    private final double fineAmount;

    private final String notes;

    private final long readerId;

    private final String cardNumber;

    private final String fullName;

    public GetReaderViolationResponseDto(ReaderViolation violation) {
        this.createdDate = violation.getCreatedDate();
        this.lastModifiedDate = violation.getLastModifiedDate();
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
