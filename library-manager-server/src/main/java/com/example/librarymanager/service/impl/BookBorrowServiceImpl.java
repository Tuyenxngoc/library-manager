package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.*;
import com.example.librarymanager.domain.dto.common.CommonResponseDto;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.pagination.PagingMeta;
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
import com.example.librarymanager.service.LogService;
import com.example.librarymanager.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
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

    @Override
    public PaginationResponseDto<BookBorrowResponseDto> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.BOOK_BORROW);

        Specification<BookBorrow> spec = EntitySpecification.filterBookBorrows().and(EntitySpecification.filterBookBorrows(requestDto.getKeyword(), requestDto.getSearchBy()));
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
    public CommonResponseDto returnBooksByIds(Set<Long> ids, String userId) {
        Set<BookBorrow> bookBorrows = ids.stream().map(this::getBookBorrow).collect(Collectors.toSet());
        bookBorrows.forEach(bookBorrow -> {
            //Cập nhật trạng thái của sách
            Book book = bookBorrow.getBook();
            book.setBookCondition(BookCondition.AVAILABLE);
            bookRepository.save(book);

            //Cập nhật trạng thái của bookBorrow
            bookBorrow.setReturned(true);

            //Cập nhật trạng thái của phiếu mượn
            BorrowReceipt borrowReceipt = bookBorrow.getBorrowReceipt();
            if (allBooksReturned(borrowReceipt)) {
                borrowReceipt.setReturnDate(LocalDate.now());
                borrowReceipt.setStatus(BorrowStatus.RETURNED);  // Nếu tất cả sách đã trả
            } else if (someBooksReturned(borrowReceipt)) {
                borrowReceipt.setStatus(BorrowStatus.PARTIALLY_RETURNED);  // Nếu chỉ một phần sách đã trả
            } else if (isOverdue(borrowReceipt)) {
                borrowReceipt.setStatus(BorrowStatus.OVERDUE);  // Nếu quá hạn
            }

            borrowReceiptRepository.save(borrowReceipt);
            bookBorrowRepository.save(bookBorrow);

            logService.createLog(TAG, EventConstants.EDIT, "Trả sách về thư viện mã: " + bookBorrow.getBook().getBookCode(), userId);
        });

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    private boolean allBooksReturned(BorrowReceipt borrowReceipt) {
        return borrowReceipt.getBookBorrows().stream().allMatch(BookBorrow::isReturned);
    }

    private boolean someBooksReturned(BorrowReceipt borrowReceipt) {
        return borrowReceipt.getBookBorrows().stream().anyMatch(BookBorrow::isReturned);
    }

    private boolean isOverdue(BorrowReceipt borrowReceipt) {
        return borrowReceipt.getDueDate().isBefore(LocalDate.now()) && !allBooksReturned(borrowReceipt);
    }

    private BookBorrow getBookBorrow(Long id) {
        BookBorrow bookBorrow = bookBorrowRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.BookBorrow.ERR_NOT_FOUND_ID, id));
        if (bookBorrow.isReturned()) {
            throw new ConflictException(ErrorMessage.BookBorrow.ERR_RETURNED_BOOK_CANNOT_BE_RETURNED, id);
        }
        return bookBorrow;
    }
}
