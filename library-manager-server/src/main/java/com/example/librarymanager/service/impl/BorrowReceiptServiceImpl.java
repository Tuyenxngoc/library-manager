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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    private BorrowReceipt getEntity(Long id) {
        return borrowReceiptRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.BorrowReceipt.ERR_RECEIPT_NOT_FOUND, id));
    }

    @Override
    public CommonResponseDto save(BorrowReceiptRequestDto requestDto, String userId) {
        //Kiểm tra số phiếu mượn
        if (borrowReceiptRepository.existsByReceiptNumber(requestDto.getReceiptNumber())) {
            throw new ConflictException(ErrorMessage.BorrowReceipt.ERR_DUPLICATE_RECEIPT_NUMBER);
        }

        //Lấy ra bạn đọc và kiểm tra thông tin
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

        BorrowReceipt borrowReceipt = borrowReceiptMapper.toBorrowReceipt(requestDto);
        borrowReceipt.setReader(reader);
        borrowReceipt.setBookBorrows(new ArrayList<>());

        //Lấy ra sách và kiểm tra thông tin
        for (BookBorrowRequestDto dto : requestDto.getBooks()) {
            Book book = bookRepository.findByBookCode(dto.getBookCode())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.Book.ERR_NOT_FOUND_CODE, dto.getBookCode()));

            for (BookBorrow bookBorrow : book.getBookBorrows()) {
                if (!bookBorrow.isReturned()) {
                    throw new ConflictException(ErrorMessage.Book.ERR_BOOK_ALREADY_BORROWED, dto.getBookCode());
                }
            }

            BookBorrow bookBorrow = new BookBorrow();
            bookBorrow.setBook(book);
            bookBorrow.setBorrowReceipt(borrowReceipt);
            bookBorrow.setDueDate(dto.getDueDate());
            borrowReceipt.getBookBorrows().add(bookBorrow);
        }

        borrowReceiptRepository.save(borrowReceipt);

        logService.createLog(TAG, EventConstants.ADD, "Tạo phiếu mượn mới: " + borrowReceipt.getReceiptNumber(), userId);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto update(Long id, BorrowReceiptRequestDto requestDto, String userId) {
        return null;
    }

    @Override
    public CommonResponseDto delete(Long id, String userId) {
        return null;
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