package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.SortByDataConstant;
import com.example.librarymanager.constant.SuccessMessage;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.pagination.PagingMeta;
import com.example.librarymanager.domain.dto.request.AuthorRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.entity.Author;
import com.example.librarymanager.domain.mapper.AuthorMapper;
import com.example.librarymanager.domain.specification.EntitySpecification;
import com.example.librarymanager.exception.ConflictException;
import com.example.librarymanager.exception.NotFoundException;
import com.example.librarymanager.repository.AuthorRepository;
import com.example.librarymanager.service.AuthorService;
import com.example.librarymanager.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    private final MessageSource messageSource;

    @Override
    public Author findById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Author.ERR_NOT_FOUND_ID));
    }

    @Override
    public PaginationResponseDto<Author> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.AUTHOR);

        Page<Author> page = authorRepository.findAll(
                EntitySpecification.filterAuthors(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable);

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.AUTHOR, page);

        PaginationResponseDto<Author> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public CommonResponseDto save(AuthorRequestDto requestDto) {
        Author author = authorMapper.toAuthor(requestDto);

        if (authorRepository.existsByCode(author.getCode())) {
            throw new ConflictException(ErrorMessage.Author.ERR_DUPLICATE_CODE);
        }

        author.setActiveFlag(true);
        authorRepository.save(author);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, author);
    }

    @Override
    public CommonResponseDto delete(Long id) {
        Author author = findById(id);

        if (!author.getBookAuthors().isEmpty()) {
            throw new ConflictException(ErrorMessage.Author.ERR_HAS_LINKED_BOOKS);
        }

        authorRepository.delete(author);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto update(Long id, AuthorRequestDto requestDto) {
        Author author = findById(id);

        if (!Objects.equals(author.getCode(), requestDto.getCode())
                && authorRepository.existsByCode(requestDto.getCode())) {
            throw new ConflictException(ErrorMessage.Author.ERR_DUPLICATE_CODE);
        }

        author.setFullName(requestDto.getFullName());
        author.setCode(requestDto.getCode());
        author.setPenName(requestDto.getPenName());
        author.setGender(requestDto.getGender());
        author.setDateOfBirth(requestDto.getDateOfBirth());
        author.setDateOfDeath(requestDto.getDateOfDeath());
        author.setTitle(requestDto.getTitle());
        author.setResidence(requestDto.getResidence());
        author.setAddress(requestDto.getAddress());
        author.setNotes(requestDto.getNotes());

        authorRepository.save(author);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, author);
    }
}