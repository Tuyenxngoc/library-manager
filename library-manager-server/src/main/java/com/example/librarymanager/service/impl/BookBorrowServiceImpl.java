package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.*;
import com.example.librarymanager.domain.dto.common.CommonResponseDto;
import com.example.librarymanager.domain.dto.filter.TimeFilter;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.pagination.PagingMeta;
import com.example.librarymanager.domain.dto.request.BookReturnRequestDto;
import com.example.librarymanager.domain.dto.response.bookborrow.BookBorrowResponseDto;
import com.example.librarymanager.domain.entity.Book;
import com.example.librarymanager.domain.entity.BookBorrow;
import com.example.librarymanager.domain.entity.BorrowReceipt;
import com.example.librarymanager.domain.specification.EntitySpecification;
import com.example.librarymanager.exception.ConflictException;
import com.example.librarymanager.exception.NotFoundException;
import com.example.librarymanager.repository.BookBorrowRepository;
import com.example.librarymanager.repository.BookRepository;
import com.example.librarymanager.repository.BorrowReceiptRepository;
import com.example.librarymanager.service.BookBorrowService;
import com.example.librarymanager.service.BorrowReceiptService;
import com.example.librarymanager.service.LogService;
import com.example.librarymanager.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookBorrowServiceImpl implements BookBorrowService {

    private static final String TAG = "Quản lý phiếu mượn";

    private final MessageSource messageSource;

    private final LogService logService;

    private final BookRepository bookRepository;

    private final BookBorrowRepository bookBorrowRepository;

    private final BorrowReceiptRepository borrowReceiptRepository;

    private final BorrowReceiptService borrowReceiptService;

    private BookBorrow getEntity(Long id) {
        return bookBorrowRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.BookBorrow.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public PaginationResponseDto<BookBorrowResponseDto> findAll(PaginationFullRequestDto requestDto, TimeFilter timeFilter, List<BookBorrowStatus> status) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.BOOK_BORROW);

        Specification<BookBorrow> spec =
                EntitySpecification.filterBookBorrows(status)
                        .and(EntitySpecification.filterBookBorrows(timeFilter))
                        .and(EntitySpecification.filterBookBorrows(requestDto.getKeyword(), requestDto.getSearchBy()));
        Page<BookBorrow> page = bookBorrowRepository.findAll(spec, pageable);

        List<BookBorrowResponseDto> items = page.getContent().stream()
                .map(BookBorrowResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.BOOK_BORROW, page);

        PaginationResponseDto<BookBorrowResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    @Transactional
    public CommonResponseDto returnBooksByIds(List<BookReturnRequestDto> requestDtos, String userId) {
        //Lấy ra danh sách sách dựa vào id
        Map<BookBorrow, BookStatus> bookBorrowsWithStatus = requestDtos.stream()
                .collect(Collectors.toMap(
                        requestDto -> {
                            BookBorrow bookBorrow = getEntity(requestDto.getBookBorrowId());
                            if (BookBorrowStatus.RETURNED.equals(bookBorrow.getStatus())) {
                                throw new ConflictException(
                                        ErrorMessage.BookBorrow.ERR_ALREADY_MARKED_AS_RETURNED,
                                        requestDto.getBookBorrowId()
                                );
                            }
                            return bookBorrow;
                        },
                        BookReturnRequestDto::getBookStatus
                ));

        // Khởi tạo danh sách để cập nhật
        List<Book> updatedBooks = new ArrayList<>();
        List<BookBorrow> updatedBookBorrows = new ArrayList<>();
        Set<BorrowReceipt> updatedBorrowReceipts = new HashSet<>();
        List<String> logMessages = new ArrayList<>();

        // Xử lý trạng thái sách và sách mượn
        bookBorrowsWithStatus.forEach((bookBorrow, newStatus) -> {
            // Cập nhật trạng thái sách mượn
            bookBorrow.setReturnDate(LocalDate.now());
            bookBorrow.setStatus(BookBorrowStatus.RETURNED);
            updatedBookBorrows.add(bookBorrow);

            // Đánh dấu sách đã trả
            Book book = bookBorrow.getBook();
            book.setBookCondition(BookCondition.AVAILABLE);
            if (newStatus != null) {
                book.setBookStatus(newStatus);
            }
            updatedBooks.add(book);

            //Thêm phiếu mượn vào danh sách
            BorrowReceipt borrowReceipt = bookBorrow.getBorrowReceipt();
            updatedBorrowReceipts.add(borrowReceipt);

            // Thêm log
            if (newStatus != null) {
                logMessages.add("{mã: " + book.getBookCode() + ", trạng thái: " + newStatus.getName() + "}");
            } else {
                logMessages.add("{mã: " + book.getBookCode() + "}");
            }
        });

        // Cập nhật trạng thái phiếu mượn
        updatedBorrowReceipts.forEach(borrowReceiptService::updateBorrowStatus); //todo

        bookRepository.saveAll(updatedBooks);
        bookBorrowRepository.saveAll(updatedBookBorrows);
        borrowReceiptRepository.saveAll(updatedBorrowReceipts);

        String logMessage = "Trả sách: [" + String.join(", ", logMessages) + "]";
        logService.createLog(TAG, EventConstants.EDIT, logMessage, userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    @Transactional
    public CommonResponseDto reportLostBooksByIds(Set<Long> ids, String userId) {
        //Lấy ra danh sách sách dựa vào id
        Set<BookBorrow> bookBorrows = ids.stream()
                .map(id -> {
                    BookBorrow bookBorrow = getEntity(id);
                    if (BookBorrowStatus.LOST.equals(bookBorrow.getStatus())) {
                        throw new ConflictException(ErrorMessage.BookBorrow.ERR_ALREADY_MARKED_AS_LOST, id);
                    }
                    return bookBorrow;
                })
                .collect(Collectors.toSet());

        // Khởi tạo danh sách để cập nhật
        List<Book> updatedBooks = new ArrayList<>();
        List<BookBorrow> updatedBookBorrows = new ArrayList<>();
        Set<BorrowReceipt> updatedBorrowReceipts = new HashSet<>();
        List<String> lostBookCodes = new ArrayList<>();

        //Cập nhật trạng thái sách và phiếu mượn
        bookBorrows.forEach(bookBorrow -> {
            //Đánh dấu sách mượn mất
            bookBorrow.setStatus(BookBorrowStatus.LOST);
            updatedBookBorrows.add(bookBorrow);

            //Đánh dấu sách bị mất
            Book book = bookBorrow.getBook();
            book.setBookCondition(BookCondition.LOST);
            updatedBooks.add(book);

            //Thêm phiếu mượn vào danh sách
            BorrowReceipt borrowReceipt = bookBorrow.getBorrowReceipt();
            updatedBorrowReceipts.add(borrowReceipt);

            // Ghi mã sách mất
            lostBookCodes.add(book.getBookCode());
        });

        // Cập nhật trạng thái phiếu mượn
        updatedBorrowReceipts.forEach(borrowReceiptService::updateBorrowStatus); //todo

        bookRepository.saveAll(updatedBooks);
        bookBorrowRepository.saveAll(updatedBookBorrows);
        borrowReceiptRepository.saveAll(updatedBorrowReceipts);

        String logMessage = "Báo mất sách với mã: " + String.join(", ", lostBookCodes);
        logService.createLog(TAG, EventConstants.EDIT, logMessage, userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }
}
