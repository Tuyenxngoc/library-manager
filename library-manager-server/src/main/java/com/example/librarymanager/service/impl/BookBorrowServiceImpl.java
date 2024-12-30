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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
    public CommonResponseDto returnBooksByIds(List<BookReturnRequestDto> requestDtos, String userId) {
        //Lấy ra danh sách sách dựa vào id
        Map<BookBorrow, BookStatus> bookBorrowsWithStatus = requestDtos.stream()
                .collect(Collectors.toMap(
                        requestDto -> {
                            BookBorrow bookBorrow = getEntity(requestDto.getBookBorrowId());
                            if (BookBorrowStatus.RETURNED.equals(bookBorrow.getStatus())) {
                                throw new ConflictException(
                                        ErrorMessage.BookBorrow.ERR_ALREADY_MARKED_AS_RETURNED,
                                        requestDto
                                );
                            }
                            return bookBorrow;
                        },
                        BookReturnRequestDto::getBookStatus
                ));

        //Cập nhật trạng thái sách
        bookBorrowsWithStatus.forEach((bookBorrow, newStatus) -> {
            //Đánh dấu sách đang rảnh
            Book book = bookBorrow.getBook();
            book.setBookCondition(BookCondition.AVAILABLE);
            if (newStatus != null) {
                book.setBookStatus(newStatus);
            }

            //Đánh dấu sách đã trả
            bookBorrow.setReturnDate(LocalDate.now());
            bookBorrow.setStatus(BookBorrowStatus.RETURNED);

            //Cập nhật trạng thái phiếu mượn
            BorrowReceipt borrowReceipt = bookBorrow.getBorrowReceipt();
            borrowReceiptService.updateBorrowStatus(borrowReceipt);

            bookRepository.save(book);
            bookBorrowRepository.save(bookBorrow);
            borrowReceiptRepository.save(borrowReceipt);

            if (newStatus != null) {
                logService.createLog(TAG, EventConstants.EDIT, "Trả sách về thư viện mã: " + bookBorrow.getBook().getBookCode() + ", trạng thái: " + newStatus.getName(), userId);
            } else {
                logService.createLog(TAG, EventConstants.EDIT, "Trả sách về thư viện mã: " + bookBorrow.getBook().getBookCode(), userId);
            }
        });

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
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

        //Cập nhật trạng thái sách
        bookBorrows.forEach(bookBorrow -> {
            //Đánh dấu sách bị mất
            Book book = bookBorrow.getBook();
            book.setBookCondition(BookCondition.LOST);

            //Đánh dấu sách mất
            bookBorrow.setStatus(BookBorrowStatus.LOST);

            //Cập nhật trạng thái phiếu mượn
            BorrowReceipt borrowReceipt = bookBorrow.getBorrowReceipt();
            borrowReceiptService.updateBorrowStatus(borrowReceipt);

            bookRepository.save(book);
            bookBorrowRepository.save(bookBorrow);
            borrowReceiptRepository.save(borrowReceipt);

            logService.createLog(TAG, EventConstants.EDIT, "Báo mất sách mã: " + bookBorrow.getBook().getBookCode(), userId);
        });

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }
}
