package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.SortByDataConstant;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.pagination.PagingMeta;
import com.example.librarymanager.domain.dto.request.BookRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetBookResponseDto;
import com.example.librarymanager.domain.entity.Book;
import com.example.librarymanager.domain.specification.EntitySpecification;
import com.example.librarymanager.repository.BookRepository;
import com.example.librarymanager.service.BookService;
import com.example.librarymanager.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public CommonResponseDto update(Long id, BookRequestDto requestDto, String userId) {
        return null;
    }

    @Override
    public PaginationResponseDto<GetBookResponseDto> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.BOOK);

        Page<Book> page = bookRepository.findAll(
                EntitySpecification.filterBooks(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable);

        List<GetBookResponseDto> items = page.getContent().stream()
                .map(GetBookResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.BOOK, page);

        PaginationResponseDto<GetBookResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public GetBookResponseDto findById(Long id) {
        return null;
    }
}
