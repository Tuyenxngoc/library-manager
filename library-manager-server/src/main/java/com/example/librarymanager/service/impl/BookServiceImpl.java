package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.SortByDataConstant;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.pagination.PagingMeta;
import com.example.librarymanager.domain.dto.response.book.BookResponseDto;
import com.example.librarymanager.domain.entity.Book;
import com.example.librarymanager.domain.specification.EntitySpecification;
import com.example.librarymanager.exception.NotFoundException;
import com.example.librarymanager.repository.BookRepository;
import com.example.librarymanager.service.BookService;
import com.example.librarymanager.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private Book getEntity(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Book.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public PaginationResponseDto<BookResponseDto> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.BOOK);

        Page<Book> page = bookRepository.findAll(
                EntitySpecification.filterBooks(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable);

        List<BookResponseDto> items = page.getContent().stream()
                .map(BookResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.BOOK, page);

        PaginationResponseDto<BookResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public List<BookResponseDto> findByIds(Set<Long> ids) {
        return bookRepository.findBooksByIds(ids);
    }

    @Override
    public BookResponseDto findById(Long id) {
        return new BookResponseDto(getEntity(id));
    }
}
