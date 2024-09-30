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
import java.util.Set;
import java.util.stream.Collectors;

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

    private final ClassificationSymbolRepository classificationSymbolRepository;

    private void checkImageIsValid(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new BadRequestException(ErrorMessage.INVALID_FILE_TYPE);
            }
        }
    }

    @Override
    public CommonResponseDto save(BookDefinitionRequestDto requestDto, MultipartFile file) {
        //Kiểm tra file tải lên có phải định dạng ảnh không
        checkImageIsValid(file);

        //Map dữ liệu
        BookDefinition bookDefinition = bookDefinitionMapper.toBookDefinition(requestDto);

        //Lưu danh mục
        Category category = categoryRepository.findByIdAndActiveFlagIsTrue(requestDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Category.ERR_NOT_FOUND_ID, requestDto.getCategoryId()));
        bookDefinition.setCategory(category);

        //Lưu vào bộ sách
        if (requestDto.getBookSetId() != null) {
            BookSet bookSet = bookSetRepository.findByIdAndActiveFlagIsTrue(requestDto.getBookSetId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.BookSet.ERR_NOT_FOUND_ID, requestDto.getBookSetId()));
            bookDefinition.setBookSet(bookSet);
        }

        //Lưu nhà xuất bản
        if (requestDto.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findByIdAndActiveFlagIsTrue(requestDto.getPublisherId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.Publisher.ERR_NOT_FOUND_ID, requestDto.getPublisherId()));
            bookDefinition.setPublisher(publisher);
        }

        //Lưu danh mục phân loại
        if (requestDto.getClassificationSymbolId() != null) {
            ClassificationSymbol classificationSymbol = classificationSymbolRepository.findByIdAndActiveFlagIsTrue(requestDto.getClassificationSymbolId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.ClassificationSymbol.ERR_NOT_FOUND_ID, requestDto.getClassificationSymbolId()));
            bookDefinition.setClassificationSymbol(classificationSymbol);
        }

        //Lưu danh sách tác giả
        if (requestDto.getAuthorIds() != null) {
            requestDto.getAuthorIds().forEach(authorId -> {
                Author author = authorRepository.findByIdAndActiveFlagIsTrue(authorId)
                        .orElseThrow(() -> new NotFoundException(ErrorMessage.Author.ERR_NOT_FOUND_ID, authorId));

                BookAuthor bookAuthor = new BookAuthor();
                bookAuthor.setAuthor(author);
                bookAuthor.setBookDefinition(bookDefinition);

                bookDefinition.getBookAuthors().add(bookAuthor);
            });
        }

        // Xử lý upload ảnh, ưu tiên file upload, rồi đến image url
        if (file != null && !file.isEmpty()) {
            String newImageUrl = uploadFileUtil.uploadFile(file);
            bookDefinition.setImageUrl(newImageUrl);
        }else if(requestDto.getImageUrl() != null){
            String newImageUrl = uploadFileUtil.copyImageFromUrl(requestDto.getImageUrl());
            bookDefinition.setImageUrl(newImageUrl);
        }

        bookDefinition.setActiveFlag(true);
        bookDefinitionRepository.save(bookDefinition);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto update(Long id, BookDefinitionRequestDto requestDto, MultipartFile file) {
        //Kiểm tra file tải lên có phải định dạng ảnh không
        checkImageIsValid(file);

        // Tìm bookDefinition dựa trên id
        BookDefinition bookDefinition = bookDefinitionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.BookDefinition.ERR_NOT_FOUND_ID, id));

        // Cập nhật danh mục
        Category category = categoryRepository.findByIdAndActiveFlagIsTrue(requestDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Category.ERR_NOT_FOUND_ID, requestDto.getCategoryId()));
        bookDefinition.setCategory(category);

        // Cập nhật bộ sách
        if (requestDto.getBookSetId() != null) {
            BookSet bookSet = bookSetRepository.findByIdAndActiveFlagIsTrue(requestDto.getBookSetId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.BookSet.ERR_NOT_FOUND_ID, requestDto.getBookSetId()));
            bookDefinition.setBookSet(bookSet);
        } else {
            bookDefinition.setBookSet(null);
        }

        // Cập nhật nhà xuất bản
        if (requestDto.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findByIdAndActiveFlagIsTrue(requestDto.getPublisherId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.Publisher.ERR_NOT_FOUND_ID, requestDto.getPublisherId()));
            bookDefinition.setPublisher(publisher);
        } else {
            bookDefinition.setPublisher(null);
        }

        // Cập nhật biểu tượng phân loại
        if (requestDto.getClassificationSymbolId() != null) {
            ClassificationSymbol classificationSymbol = classificationSymbolRepository.findByIdAndActiveFlagIsTrue(requestDto.getClassificationSymbolId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.ClassificationSymbol.ERR_NOT_FOUND_ID, requestDto.getClassificationSymbolId()));
            bookDefinition.setClassificationSymbol(classificationSymbol);
        } else {
            bookDefinition.setClassificationSymbol(null);
        }

        // Cập nhật danh sách tác giả
        List<Long> newAuthorIds = requestDto.getAuthorIds(); // Mảng tác giả mới

        // Xóa các tác giả không còn trong danh sách mới
        if (newAuthorIds == null || newAuthorIds.isEmpty()) {
            bookAuthorRepository.deleteAllByBookDefinitionId(bookDefinition.getId()); // Xóa các bản ghi trong cơ sở dữ liệu
            bookDefinition.getBookAuthors().clear(); // Xóa các bản ghi trong bộ nhớ
        } else {
            // Tập hợp ID tác giả hiện tại
            Set<Long> currentAuthorIds = bookDefinition.getBookAuthors().stream()
                    .map(bookAuthor -> bookAuthor.getAuthor().getId())
                    .collect(Collectors.toSet());

            // Xóa các tác giả không có trong danh sách mới
            for (Long currentAuthorId : currentAuthorIds) {
                if (!newAuthorIds.contains(currentAuthorId)) {
                    // Tìm BookAuthor để xóa
                    BookAuthor bookAuthorToRemove = bookDefinition.getBookAuthors().stream()
                            .filter(bookAuthor -> bookAuthor.getAuthor().getId().equals(currentAuthorId))
                            .findFirst()
                            .orElse(null);
                    if (bookAuthorToRemove != null) {
                        bookDefinition.getBookAuthors().remove(bookAuthorToRemove);
                        bookAuthorRepository.delete(bookAuthorToRemove); // Xóa trong cơ sở dữ liệu
                    }
                }
            }

            // Thêm các tác giả mới
            for (Long authorId : newAuthorIds) {
                if (!currentAuthorIds.contains(authorId)) { // Nếu tác giả chưa tồn tại
                    Author author = authorRepository.findByIdAndActiveFlagIsTrue(authorId)
                            .orElseThrow(() -> new NotFoundException(ErrorMessage.Author.ERR_NOT_FOUND_ID, authorId));

                    BookAuthor bookAuthor = new BookAuthor();
                    bookAuthor.setAuthor(author);
                    bookAuthor.setBookDefinition(bookDefinition);
                    bookDefinition.getBookAuthors().add(bookAuthor);
                }
            }
        }

        // Cập nhật các thông tin khác
        bookDefinition.setTitle(requestDto.getTitle());
        bookDefinition.setPublishingYear(requestDto.getPublishingYear());
        bookDefinition.setPrice(requestDto.getPrice());
        bookDefinition.setEdition(requestDto.getEdition());
        bookDefinition.setReferencePrice(requestDto.getReferencePrice());
        bookDefinition.setPublicationPlace(requestDto.getPublicationPlace());
        bookDefinition.setBookCode(requestDto.getBookCode());
        bookDefinition.setPageCount(requestDto.getPageCount());
        bookDefinition.setBookSize(requestDto.getBookSize());
        bookDefinition.setParallelTitle(requestDto.getParallelTitle());
        bookDefinition.setSummary(requestDto.getSummary());
        bookDefinition.setSubtitle(requestDto.getSubtitle());
        bookDefinition.setAdditionalMaterial(requestDto.getAdditionalMaterial());
        bookDefinition.setKeywords(requestDto.getKeywords());
        bookDefinition.setIsbn(requestDto.getIsbn());
        bookDefinition.setLanguage(requestDto.getLanguage());
        bookDefinition.setSeries(requestDto.getSeries());
        bookDefinition.setAdditionalInfo(requestDto.getAdditionalInfo());

        // Xử lý upload ảnh, ưu tiên file upload, rồi đến image url
        if (file != null && !file.isEmpty()) {
            String newImageUrl = uploadFileUtil.uploadFile(file);

            //Xóa ảnh cũ
            uploadFileUtil.destroyFileWithUrl(bookDefinition.getImageUrl());

            bookDefinition.setImageUrl(newImageUrl);
        }

        // Lưu đối tượng bookDefinition đã cập nhật
        bookDefinitionRepository.save(bookDefinition);

        // Trả về kết quả sau khi cập nhật thành công
        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }


    @Override
    public CommonResponseDto delete(Long id) {
        BookDefinition bookDefinition = findEntityById(id);

        if (!bookDefinition.getBooks().isEmpty()) {
            throw new BadRequestException(ErrorMessage.BookDefinition.ERR_HAS_LINKED_BOOKS);
        }

        uploadFileUtil.destroyFileWithUrl(bookDefinition.getImageUrl());

        bookDefinitionRepository.delete(bookDefinition);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public PaginationResponseDto<GetBookDefinitionResponseDto> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.BOOK_DEFINITION);

        Page<BookDefinition> page = bookDefinitionRepository.findAll(
                EntitySpecification.filterBookDefinitions(requestDto.getKeyword(), requestDto.getSearchBy(), requestDto.getActiveFlag()),
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