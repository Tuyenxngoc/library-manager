package com.example.librarymanager.controller;

import com.example.librarymanager.annotation.CurrentUser;
import com.example.librarymanager.annotation.RestApiV1;
import com.example.librarymanager.base.VsResponseUtil;
import com.example.librarymanager.constant.UrlConstant;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.request.BookRequestDto;
import com.example.librarymanager.security.CustomUserDetails;
import com.example.librarymanager.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Book")
public class BookController {

    BookService bookService;

    @Operation(summary = "API Update Book")
    @PreAuthorize("hasRole('ROLE_MANAGE_BOOK')")
    @PutMapping(UrlConstant.Book.UPDATE)
    public ResponseEntity<?> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(bookService.update(id, requestDto, userDetails.getUserId()));
    }

    @Operation(summary = "API Get All Books")
    @PreAuthorize("hasAnyRole('ROLE_MANAGE_BOOK', 'ROLE_MANAGE_EXPORT_RECEIPT')")
    @GetMapping(UrlConstant.Book.GET_ALL)
    public ResponseEntity<?> getAllBooks(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(bookService.findAll(requestDto));
    }

    @Operation(summary = "API Get Book By Id")
    @PreAuthorize("hasRole('ROLE_MANAGE_BOOK')")
    @GetMapping(UrlConstant.Book.GET_BY_ID)
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        return VsResponseUtil.success(bookService.findById(id));
    }

    @Operation(summary = "API Get Book PDF")
    @PreAuthorize("hasRole('ROLE_MANAGE_BOOK')")
    @PostMapping(UrlConstant.Book.BOOK_PDF)
    public ResponseEntity<byte[]> getBookPdf(@RequestBody List<Long> bookIds) {
        byte[] pdfContent = bookService.getBooksPdfContent(bookIds);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=book.pdf")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(pdfContent);
    }

    @Operation(summary = "API Get Book Label Type 1 PDF")
    @PreAuthorize("hasRole('ROLE_MANAGE_BOOK')")
    @PostMapping(UrlConstant.Book.BOOK_LABEL_TYPE_1_PDF)
    public ResponseEntity<byte[]> getBookLabelType1Pdf(@RequestBody List<Long> bookIds) {
        byte[] pdfContent = bookService.getBooksLabelType1PdfContent(bookIds);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=book_label_type1.pdf")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(pdfContent);
    }

    @Operation(summary = "API Get Book Label Type 2 PDF")
    @PreAuthorize("hasRole('ROLE_MANAGE_BOOK')")
    @PostMapping(UrlConstant.Book.BOOK_LABEL_TYPE_2_PDF)
    public ResponseEntity<byte[]> getBookLabelType2Pdf(@RequestBody List<Long> bookIds) {
        byte[] pdfContent = bookService.getBooksLabelType2PdfContent(bookIds);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=book_label_type2.pdf")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(pdfContent);
    }

    @Operation(summary = "API Get Book List PDF")
    @PreAuthorize("hasRole('ROLE_MANAGE_BOOK')")
    @GetMapping(UrlConstant.Book.GET_BOOK_LIST_PDF)
    public ResponseEntity<byte[]> getBookListPdf() {
        byte[] pdfContent = bookService.generateBookListPdf();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=book_list.pdf")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(pdfContent);
    }
}
