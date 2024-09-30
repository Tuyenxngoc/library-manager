package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.SortByDataConstant;
import com.example.librarymanager.constant.SuccessMessage;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.pagination.PagingMeta;
import com.example.librarymanager.domain.dto.request.BookRequestDto;
import com.example.librarymanager.domain.dto.request.ImportReceiptRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetImportReceiptResponseDto;
import com.example.librarymanager.domain.entity.Book;
import com.example.librarymanager.domain.entity.BookDefinition;
import com.example.librarymanager.domain.entity.ImportReceipt;
import com.example.librarymanager.domain.mapper.ImportReceiptMapper;
import com.example.librarymanager.domain.specification.EntitySpecification;
import com.example.librarymanager.exception.ConflictException;
import com.example.librarymanager.exception.NotFoundException;
import com.example.librarymanager.repository.BookDefinitionRepository;
import com.example.librarymanager.repository.ImportReceiptRepository;
import com.example.librarymanager.service.ImportReceiptService;
import com.example.librarymanager.service.LogService;
import com.example.librarymanager.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImportReceiptServiceImpl implements ImportReceiptService {

    private static final String TAG = "Nhập sách";

    private final ImportReceiptRepository importReceiptRepository;

    private final BookDefinitionRepository bookDefinitionRepository;

    private final ImportReceiptMapper importReceiptMapper;

    private final MessageSource messageSource;

    private final LogService logService;

    @Override
    public CommonResponseDto save(ImportReceiptRequestDto requestDto, String userId) {
        if (importReceiptRepository.existsByReceiptNumber(requestDto.getReceiptNumber())) {
            throw new ConflictException(ErrorMessage.ImportReceipt.ERR_DUPLICATE_NUMBER, requestDto.getReceiptNumber());
        }

        ImportReceipt importReceipt = importReceiptMapper.toImportReceipt(requestDto);
        importReceipt.setBook(new ArrayList<>());

        Map<Integer, BookDefinition> bookDefinitionMap = new HashMap<>();
        for (BookRequestDto bookRequestDto : requestDto.getBookRequestDtos()) {
            BookDefinition bookDefinition = bookDefinitionRepository.findByIdAndActiveFlagIsTrue(bookRequestDto.getBookDefinitionId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.BookDefinition.ERR_NOT_FOUND_ID, bookRequestDto.getBookDefinitionId()));

            bookDefinitionMap.put(bookRequestDto.getQuantity(), bookDefinition);
        }

        for (Map.Entry<Integer, BookDefinition> entry : bookDefinitionMap.entrySet()) {
            Integer quantity = entry.getKey();
            BookDefinition bookDefinition = entry.getValue();

            for (int i = 0; i < quantity; i++) {
                long count = bookDefinitionRepository.countByBookCode(bookDefinition.getBookCode());
                String bookCode = String.format("%s.%05d", bookDefinition.getBookCode(), (int) (Math.random() * 1000));

                //Tạo sách dựa theo biên mục
                Book book = new Book();
                book.setBookCode(bookCode);
                book.setBookDefinition(bookDefinition);
                book.setImportReceipt(importReceipt);
                book.setActiveFlag(true);

                importReceipt.getBook().add(book);
            }
        }

        importReceiptRepository.save(importReceipt);

        logService.createLog(TAG, "Thêm", "Tạo phiếu nhập mới: " + importReceipt.getReceiptNumber(), userId);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, new GetImportReceiptResponseDto(importReceipt));
    }

    @Override
    public CommonResponseDto update(Long id, ImportReceiptRequestDto requestDto, String userId) {
        return null;
    }

    @Override
    public CommonResponseDto delete(Long id, String userId) {
        return null;
    }

    @Override
    public PaginationResponseDto<GetImportReceiptResponseDto> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.BOOK_SET);

        Page<ImportReceipt> page = importReceiptRepository.findAll(
                EntitySpecification.filterImportReceipts(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable);

        List<GetImportReceiptResponseDto> items = page.getContent().stream()
                .map(GetImportReceiptResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.BOOK_SET, page);

        PaginationResponseDto<GetImportReceiptResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public GetImportReceiptResponseDto findById(Long id) {
        ImportReceipt importReceipt = importReceiptRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ImportReceipt.ERR_NOT_FOUND_ID, id));
        return new GetImportReceiptResponseDto(importReceipt);
    }
}