package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.SortByDataConstant;
import com.example.librarymanager.constant.SuccessMessage;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.pagination.PagingMeta;
import com.example.librarymanager.domain.dto.request.BookSetRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetBookSetResponseDto;
import com.example.librarymanager.domain.entity.BookSet;
import com.example.librarymanager.domain.mapper.BookSetMapper;
import com.example.librarymanager.domain.specification.EntitySpecification;
import com.example.librarymanager.exception.BadRequestException;
import com.example.librarymanager.exception.ConflictException;
import com.example.librarymanager.exception.NotFoundException;
import com.example.librarymanager.repository.BookSetRepository;
import com.example.librarymanager.service.BookSetService;
import com.example.librarymanager.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookSetServiceImpl implements BookSetService {
    private final BookSetRepository bookSetRepository;

    private final MessageSource messageSource;

    private final BookSetMapper bookSetMapper;

    @Override
    public CommonResponseDto save(BookSetRequestDto requestDto) {
        if (bookSetRepository.existsByName(requestDto.getName())) {
            throw new ConflictException(ErrorMessage.BookSet.ERR_DUPLICATE_NAME);
        }

        BookSet bookSet = bookSetMapper.toBookSet(requestDto);

        bookSet.setActiveFlag(true);
        bookSetRepository.save(bookSet);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, new GetBookSetResponseDto(bookSet));
    }

    @Override
    public CommonResponseDto update(Long id, BookSetRequestDto requestDto) {
        BookSet bookSet = findById(id);

        if (!Objects.equals(bookSet.getName(), requestDto.getName()) && bookSetRepository.existsByName(requestDto.getName())) {
            throw new ConflictException(ErrorMessage.BookSet.ERR_DUPLICATE_NAME);
        }

        bookSet.setName(requestDto.getName());

        bookSetRepository.save(bookSet);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, new GetBookSetResponseDto(bookSet));
    }

    @Override
    public CommonResponseDto delete(Long id) {
        BookSet bookSet = findById(id);

        if (!bookSet.getBookDefinitions().isEmpty()) {
            throw new BadRequestException(ErrorMessage.BookSet.ERR_HAS_LINKED_BOOKS);
        }

        bookSetRepository.delete(bookSet);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public PaginationResponseDto<GetBookSetResponseDto> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.BOOK_SET);

        Page<BookSet> page = bookSetRepository.findAll(
                EntitySpecification.filterBookSets(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable);

        List<GetBookSetResponseDto> items = page.getContent().stream()
                .map(GetBookSetResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.BOOK_SET, page);

        PaginationResponseDto<GetBookSetResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public BookSet findById(Long id) {
        return bookSetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.BookSet.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public CommonResponseDto toggleActiveStatus(Long id) {
        BookSet bookSet = findById(id);

        bookSet.setActiveFlag(!bookSet.getActiveFlag());

        bookSetRepository.save(bookSet);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, bookSet.getActiveFlag());
    }
}
