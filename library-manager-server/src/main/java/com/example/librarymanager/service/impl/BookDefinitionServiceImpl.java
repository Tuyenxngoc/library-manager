package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.SuccessMessage;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.BookDefinitionRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.entity.*;
import com.example.librarymanager.domain.mapper.BookDefinitionMapper;
import com.example.librarymanager.exception.BadRequestException;
import com.example.librarymanager.exception.NotFoundException;
import com.example.librarymanager.repository.*;
import com.example.librarymanager.service.BookDefinitionService;
import com.example.librarymanager.util.UploadFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookDefinitionServiceImpl implements BookDefinitionService {

    private final UploadFileUtil uploadFileUtil;

    private final BookDefinitionRepository bookDefinitionRepository;

    private final BookDefinitionMapper bookDefinitionMapper;

    private final MessageSource messageSource;

    private final CategoryRepository categoryRepository;

    private final BookSetRepository bookSetRepository;

    private final PublisherRepository publisherRepository;

    private final AuthorRepository authorRepository;

    private final BookAuthorRepository bookAuthorRepository;

    @Override
    public CommonResponseDto save(BookDefinitionRequestDto requestDto, MultipartFile file) {
        BookDefinition bookDefinition = bookDefinitionMapper.toBookDefinition(requestDto);

        //Upload ảnh
        if (file != null && !file.isEmpty()) {
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new BadRequestException(ErrorMessage.INVALID_FILE_TYPE);
            }

            String newUrl = uploadFileUtil.uploadFile(file);
            bookDefinition.setImageUrl(newUrl);
        }

        //Lưu danh mục
        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Category.ERR_NOT_FOUND_ID, requestDto.getCategoryId()));
        bookDefinition.setCategory(category);

        //Lưu vào bộ sách
        if (requestDto.getBookSetId() != null) {
            BookSet bookSet = bookSetRepository.findById(requestDto.getBookSetId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.BookSet.ERR_NOT_FOUND_ID, requestDto.getBookSetId()));
            bookDefinition.setBookSet(bookSet);
        }

        //Lưu nhà xuất bản
        if (requestDto.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(requestDto.getPublisherId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.Publisher.ERR_NOT_FOUND_ID, requestDto.getPublisherId()));
            bookDefinition.setPublisher(publisher);
        }

        //Lưu danh sách tác giả
        if (requestDto.getAuthorIds() != null) {
            List<Author> authorList = new ArrayList<>();
            requestDto.getAuthorIds().forEach(authorId -> {
                Author author = authorRepository.findById(authorId)
                        .orElseThrow(() -> new NotFoundException(ErrorMessage.Author.ERR_NOT_FOUND_ID, authorId));
                authorList.add(author);
            });

            for (Author author : authorList) {
                BookAuthor bookAuthor = new BookAuthor();
                bookAuthor.setAuthor(author);
                bookAuthor.setBookDefinition(bookDefinition);

                bookAuthorRepository.save(bookAuthor);
            }
        }

        bookDefinitionRepository.save(bookDefinition);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, bookDefinition);
    }

    @Override
    public CommonResponseDto update(Long id, BookDefinitionRequestDto requestDto) {
        return null;
    }

    @Override
    public CommonResponseDto delete(Long id) {
        return null;
    }

    @Override
    public PaginationResponseDto<BookDefinition> findAll(PaginationFullRequestDto requestDto) {
        return null;
    }

    @Override
    public BookDefinition findById(Long id) {
        return null;
    }

    @Override
    public CommonResponseDto toggleActiveStatus(Long id) {
        return null;
    }

    private long parseSize(String size) {
        try {
            size = size.toUpperCase();
            long parseLong = Long.parseLong(size.substring(0, size.length() - 2));
            if (size.endsWith("KB")) {
                return parseLong * 1024;
            } else if (size.endsWith("MB")) {
                return parseLong * 1024 * 1024;
            } else if (size.endsWith("GB")) {
                return parseLong * 1024 * 1024 * 1024;
            } else {
                return parseLong;
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            return 2 * 1024 * 1024;
        }
    }

}