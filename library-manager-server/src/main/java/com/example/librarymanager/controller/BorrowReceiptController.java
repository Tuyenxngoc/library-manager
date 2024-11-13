package com.example.librarymanager.controller;

import com.example.librarymanager.annotation.CurrentUser;
import com.example.librarymanager.annotation.RestApiV1;
import com.example.librarymanager.base.VsResponseUtil;
import com.example.librarymanager.constant.UrlConstant;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.request.BorrowReceiptRequestDto;
import com.example.librarymanager.security.CustomUserDetails;
import com.example.librarymanager.service.BorrowReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Borrow Receipt")
public class BorrowReceiptController {

    BorrowReceiptService borrowReceiptService;

    @Operation(summary = "API Create Borrow Receipt")
    @PreAuthorize("hasRole('ROLE_MANAGE_BORROW_RECEIPT')")
    @PostMapping(UrlConstant.BorrowReceipt.CREATE)
    public ResponseEntity<?> createBorrowReceipt(
            @Valid @RequestBody BorrowReceiptRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(HttpStatus.CREATED, borrowReceiptService.save(requestDto, userDetails.getUserId()));
    }

    @Operation(summary = "API Update Borrow Receipt")
    @PreAuthorize("hasRole('ROLE_MANAGE_BORROW_RECEIPT')")
    @PutMapping(UrlConstant.BorrowReceipt.UPDATE)
    public ResponseEntity<?> updateBorrowReceipt(
            @PathVariable Long id,
            @Valid @RequestBody BorrowReceiptRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(borrowReceiptService.update(id, requestDto, userDetails.getUserId()));
    }

    @Operation(summary = "API Delete Borrow Receipt")
    @PreAuthorize("hasRole('ROLE_MANAGE_BORROW_RECEIPT')")
    @DeleteMapping(UrlConstant.BorrowReceipt.DELETE)
    public ResponseEntity<?> deleteBorrowReceipt(
            @PathVariable Long id,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(borrowReceiptService.delete(id, userDetails.getUserId()));
    }

    @Operation(summary = "API Get All Borrow Receipts")
    @PreAuthorize("hasRole('ROLE_MANAGE_BORROW_RECEIPT')")
    @GetMapping(UrlConstant.BorrowReceipt.GET_ALL)
    public ResponseEntity<?> getAllBorrowReceipts(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(borrowReceiptService.findAll(requestDto));
    }

    @Operation(summary = "API Get Borrow Receipt By Id")
    @PreAuthorize("hasRole('ROLE_MANAGE_BORROW_RECEIPT')")
    @GetMapping(UrlConstant.BorrowReceipt.GET_BY_ID)
    public ResponseEntity<?> getBorrowReceiptById(@PathVariable Long id) {
        return VsResponseUtil.success(borrowReceiptService.findById(id));
    }

    @Operation(summary = "API Get Borrow Receipt By Cart Id")
    @PreAuthorize("hasRole('ROLE_MANAGE_BORROW_RECEIPT')")
    @GetMapping(UrlConstant.BorrowReceipt.GET_BY_CART_ID)
    public ResponseEntity<?> getBorrowReceiptByCartId(@PathVariable Long id) {
        return VsResponseUtil.success(borrowReceiptService.findByCartId(id));
    }

    @Operation(summary = "API Get Borrow Receipts by Reader")
    @PreAuthorize("hasRole('ROLE_READER')")
    @GetMapping(UrlConstant.BorrowReceipt.GET_BY_READER)
    public ResponseEntity<?> getBorrowReceiptsByReader(
            @CurrentUser CustomUserDetails userDetails,
            @ParameterObject PaginationFullRequestDto requestDto
    ) {
        return VsResponseUtil.success(borrowReceiptService.findByCardNumber(userDetails.getCardNumber(), requestDto));
    }

}
