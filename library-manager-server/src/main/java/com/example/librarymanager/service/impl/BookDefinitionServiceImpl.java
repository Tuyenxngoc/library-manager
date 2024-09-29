package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.SortByDataConstant;
import com.example.librarymanager.constant.SuccessMessage;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.pagination.PagingMeta;
import com.example.librarymanager.domain.dto.request.BookDefinitionRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetBookDefinitionResponseDto;
import com.example.librarymanager.domain.entity.*;
import com.example.librarymanager.domain.mapper.BookDefinitionMapper;
import com.example.librarymanager.domain.specification.EntitySpecification;
import com.example.librarymanager.exception.BadRequestException;
import com.example.librarymanager.exception.NotFoundException;
import com.example.librarymanager.repository.*;
import com.example.librarymanager.service.BookDefinitionService;
import com.example.librarymanager.util.PaginationUtil;
import com.example.librarymanager.util.UploadFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    private final ClassificationSymbolRepository classificationSymbolRepository;

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

        //Lưu danh mục phân loại
        if(requestDto.getClassificationSymbolId() != null){
            ClassificationSymbol classificationSymbol = classificationSymbolRepository.findById(requestDto.getClassificationSymbolId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.ClassificationSymbol.ERR_NOT_FOUND_ID, requestDto.getClassificationSymbolId()));
            bookDefinition.setClassificationSymbol(classificationSymbol);
        }

        //Lưu danh sách tác giả
        if (requestDto.getAuthorIds() != null) {
            requestDto.getAuthorIds().forEach(authorId -> {
                Author author = authorRepository.findById(authorId)
                        .orElseThrow(() -> new NotFoundException(ErrorMessage.Author.ERR_NOT_FOUND_ID, authorId));

                BookAuthor bookAuthor = new BookAuthor();
                bookAuthor.setAuthor(author);
                bookAuthor.setBookDefinition(bookDefinition);

                bookDefinition.getBookAuthors().add(bookAuthor);
            });
        }

        bookDefinition.setActiveFlag(true);
        bookDefinitionRepository.save(bookDefinition);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto update(Long id, BookDefinitionRequestDto requestDto, MultipartFile image) {
        return null;
    }

    @Override
    public CommonResponseDto delete(Long id) {
        BookDefinition bookDefinition = findEntityById(id);

        if (!bookDefinition.getBooks().isEmpty()) {
            throw new BadRequestException(ErrorMessage.BookDefinition.ERR_HAS_LINKED_BOOKS);
        }

        bookDefinitionRepository.delete(bookDefinition);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public PaginationResponseDto<GetBookDefinitionResponseDto> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.BOOK_DEFINITION);

        Page<BookDefinition> page = bookDefinitionRepository.findAll(
                EntitySpecification.filterBookDefinitions(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable);

        List<GetBookDefinitionResponseDto> items = page.getContent().stream()
                .map(GetBookDefinitionResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.BOOK_DEFINITION, page);

        PaginationResponseDto<GetBookDefinitionResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public GetBookDefinitionResponseDto findById(Long id) {
        BookDefinition bookDefinition = findEntityById(id);

        return new GetBookDefinitionResponseDto(bookDefinition);
    }

    private BookDefinition findEntityById(Long id) {
        return bookDefinitionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.BookDefinition.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public CommonResponseDto toggleActiveStatus(Long id) {
        BookDefinition bookDefinition = findEntityById(id);

        bookDefinition.setActiveFlag(!bookDefinition.getActiveFlag());

        bookDefinitionRepository.save(bookDefinition);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, bookDefinition.getActiveFlag());
    }

}