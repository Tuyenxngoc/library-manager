package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.EventConstants;
import com.example.librarymanager.constant.SortByDataConstant;
import com.example.librarymanager.constant.SuccessMessage;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.pagination.PagingMeta;
import com.example.librarymanager.domain.dto.request.BookBorrowRequestDto;
import com.example.librarymanager.domain.dto.request.BorrowReceiptRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetBorrowReceiptDetailResponseDto;
import com.example.librarymanager.domain.dto.response.GetBorrowReceiptResponseDto;
import com.example.librarymanager.domain.entity.Book;
import com.example.librarymanager.domain.entity.BookBorrow;
import com.example.librarymanager.domain.entity.BorrowReceipt;
import com.example.librarymanager.domain.entity.Reader;
import com.example.librarymanager.domain.mapper.BorrowReceiptMapper;
import com.example.librarymanager.domain.specification.EntitySpecification;
import com.example.librarymanager.exception.ConflictException;
import com.example.librarymanager.exception.ForbiddenException;
import com.example.librarymanager.exception.NotFoundException;
import com.example.librarymanager.repository.BookBorrowRepository;
import com.example.librarymanager.repository.BookRepository;
import com.example.librarymanager.repository.BorrowReceiptRepository;
import com.example.librarymanager.repository.ReaderRepository;
import com.example.librarymanager.service.BorrowReceiptService;
import com.example.librarymanager.service.LogService;
import com.example.librarymanager.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowReceiptServiceImpl implements BorrowReceiptService {
    private static final String TAG = "Quản lý phiếu mượn";

    private final LogService logService;

    private final MessageSource messageSource;

    private final BorrowReceiptRepository borrowReceiptRepository;

    private final BorrowReceiptMapper borrowReceiptMapper;

    private final ReaderRepository readerRepository;

    private final BookRepository bookRepository;

    private final BookBorrowRepository bookBorrowRepository;

    private BorrowReceipt getEntity(Long id) {
        return borrowReceiptRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.BorrowReceipt.ERR_RECEIPT_NOT_FOUND, id));
    }

    private void getReader(BorrowReceipt borrowReceipt, BorrowReceiptRequestDto requestDto) {
        Reader reader = readerRepository.findById(requestDto.getReaderId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Reader.ERR_NOT_FOUND_ID, requestDto.getReaderId()));
        switch (reader.getStatus()) {
            case INACTIVE -> throw new ForbiddenException(ErrorMessage.Reader.ERR_READER_INACTIVE);
            case SUSPENDED -> throw new ForbiddenException(ErrorMessage.Reader.ERR_READER_SUSPENDED);
            case REVOKED -> throw new ForbiddenException(ErrorMessage.Reader.ERR_READER_REVOKED);
        }
        if (reader.getExpiryDate().isBefore(LocalDate.now())) {
            throw new ForbiddenException(ErrorMessage.Reader.ERR_READER_EXPIRED);
        }

        borrowReceipt.setReader(reader);
    }

    private void getBook(BorrowReceipt borrowReceipt, BookBorrowRequestDto requestDto) {
        Book book = bookRepository.findByBookCode(requestDto.getBookCode())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Book.ERR_NOT_FOUND_CODE, requestDto.getBookCode()));

        for (BookBorrow bookBorrow : book.getBookBorrows()) {
            if (!bookBorrow.isReturned()) {
                throw new ConflictException(ErrorMessage.Book.ERR_BOOK_ALREADY_BORROWED, requestDto.getBookCode());
            }
        }

        BookBorrow bookBorrow = new BookBorrow();
        bookBorrow.setBook(book);
        bookBorrow.setBorrowReceipt(borrowReceipt);
        bookBorrow.setDueDate(requestDto.getDueDate());
        borrowReceipt.getBookBorrows().add(bookBorrow);
    }

    @Override
    public CommonResponseDto save(BorrowReceiptRequestDto requestDto, String userId) {
        //Kiểm tra số phiếu mượn
        if (borrowReceiptRepository.existsByReceiptNumber(requestDto.getReceiptNumber())) {
            throw new ConflictException(ErrorMessage.BorrowReceipt.ERR_DUPLICATE_RECEIPT_NUMBER);
        }

        BorrowReceipt borrowReceipt = borrowReceiptMapper.toBorrowReceipt(requestDto);

        //Lấy ra bạn đọc và kiểm tra thông tin
        getReader(borrowReceipt, requestDto);
        borrowReceipt.setBookBorrows(new ArrayList<>());

        //Lấy ra sách và kiểm tra thông tin
        for (BookBorrowRequestDto dto : requestDto.getBooks()) {
            getBook(borrowReceipt, dto);
        }

        borrowReceiptRepository.save(borrowReceipt);

        logService.createLog(TAG, EventConstants.ADD, "Tạo phiếu mượn mới mã: " + borrowReceipt.getReceiptNumber(), userId);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    @Transactional
    public CommonResponseDto update(Long id, BorrowReceiptRequestDto requestDto, String userId) {
        BorrowReceipt borrowReceipt = getEntity(id);

        if (!Objects.equals(borrowReceipt.getReceiptNumber(), requestDto.getReceiptNumber())
                && borrowReceiptRepository.existsByReceiptNumber(requestDto.getReceiptNumber())) {
            throw new ConflictException(ErrorMessage.BorrowReceipt.ERR_DUPLICATE_RECEIPT_NUMBER);
        }

        //Nếu bạn đọc thay đổi thì cập nhật thông tin
        if (!borrowReceipt.getReader().getId().equals(requestDto.getReaderId())) {
            //Lấy ra bạn đọc và kiểm tra thông tin
            getReader(borrowReceipt, requestDto);
        }

        // Lấy danh sách sách hiện tại trong phiếu mượn
        Set<BookBorrowRequestDto> currentBookCodes = borrowReceipt.getBookBorrows().stream()
                .map(BookBorrowRequestDto::new)
                .collect(Collectors.toSet());

        //Lấy danh sách mới
        Set<BookBorrowRequestDto> newBookCodes = requestDto.getBooks();

        // Tìm sách cần thêm mới
        Set<BookBorrowRequestDto> bookCodesToAdd = new HashSet<>(newBookCodes);
        bookCodesToAdd.removeAll(currentBookCodes);

        // Tìm sách cần xóa
        Set<BookBorrowRequestDto> bookCodesToRemove = new HashSet<>(currentBookCodes);
        bookCodesToRemove.removeAll(newBookCodes);

        // Thêm sách mới vào phiếu xuất
        if (!bookCodesToAdd.isEmpty()) {
            for (BookBorrowRequestDto bookId : bookCodesToAdd) {
                getBook(borrowReceipt, bookId);
            }
        }

        // Xóa sách không còn trong danh sách
        if (!bookCodesToRemove.isEmpty()) {
            Set<BookBorrow> booksToRemove = borrowReceipt.getBookBorrows().stream()
                    .filter(book -> bookCodesToRemove.contains(new BookBorrowRequestDto(book.getBook().getBookCode())))
                    .collect(Collectors.toSet());

            bookBorrowRepository.deleteAll(booksToRemove);

            borrowReceipt.getBookBorrows().removeAll(booksToRemove);
        }

        borrowReceipt.setReceiptNumber(requestDto.getReceiptNumber());
        borrowReceipt.setCreatedDate(requestDto.getCreatedDate());
        borrowReceipt.setNote(requestDto.getNote());

        borrowReceiptRepository.save(borrowReceipt);

        logService.createLog(TAG, EventConstants.EDIT, "Cập nhật phiếu mượn mã: " + borrowReceipt.getReceiptNumber(), userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto delete(Long id, String userId) {
        BorrowReceipt borrowReceipt = getEntity(id);

        borrowReceiptRepository.delete(borrowReceipt);

        logService.createLog(TAG, EventConstants.DELETE, "Xóa phiếu mượn mã: " + borrowReceipt.getReceiptNumber(), userId);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public PaginationResponseDto<GetBorrowReceiptResponseDto> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.BORROW_RECEIPT);

        Page<BorrowReceipt> page = borrowReceiptRepository.findAll(
                EntitySpecification.filterBorrowReceipts(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable);

        List<GetBorrowReceiptResponseDto> items = page.getContent().stream()
                .map(GetBorrowReceiptResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.BORROW_RECEIPT, page);

        PaginationResponseDto<GetBorrowReceiptResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public GetBorrowReceiptDetailResponseDto findById(Long id) {
        BorrowReceipt borrowReceipt = getEntity(id);
        return new GetBorrowReceiptDetailResponseDto(borrowReceipt);
    }
}