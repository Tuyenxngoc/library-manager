package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.Gender;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    @Value("${data.authors.csv}")
    private String authorsCsvPath;

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    private final MessageSource messageSource;

    @Override
    @Transactional
    public void initAuthorsFromCsv(String username) {
        if (authorRepository.count() > 0) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(authorsCsvPath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length < 11) continue;

                // Tạo một tác giả mới
                Author author = new Author();
                author.setFullName(values[1]);
                author.setCode(values[2]);
                author.setPenName(values[3].isEmpty() ? null : values[3]);
                author.setGender(Gender.valueOf(values[4]));
                author.setDateOfBirth(LocalDate.parse(values[5]));
                if (!values[6].isEmpty()) {
                    author.setDateOfDeath(LocalDate.parse(values[6]));
                }
                author.setTitle(values[7]);
                author.setResidence(values[8]);
                author.setAddress(values[9].isEmpty() ? null : values[9]);
                author.setNotes(values[10].isEmpty() ? null : values[10]);
                author.setActiveFlag(true);
                author.setCreatedBy(username);
                author.setLastModifiedBy(username);

                // Kiểm tra xem tác giả đã tồn tại chưa
                if (!authorRepository.existsByCode(author.getCode())) {
                    authorRepository.save(author);
                }
            }
        } catch (IOException e) {
            log.error("Error while saving author: {}", e.getMessage(), e);
        }
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
    public PaginationResponseDto<Author> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.AUTHOR);

        Page<Author> page = authorRepository.findAll(
                EntitySpecification.filterAuthors(requestDto.getKeyword(), requestDto.getSearchBy(), requestDto.getActiveFlag()),
                pageable);

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.AUTHOR, page);

        PaginationResponseDto<Author> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public Author findById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Author.ERR_NOT_FOUND_ID));
    }

    @Override
    public CommonResponseDto toggleActiveStatus(Long id) {
        Author author = findById(id);

        author.setActiveFlag(!author.getActiveFlag());

        authorRepository.save(author);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, author.getActiveFlag());
    }

}