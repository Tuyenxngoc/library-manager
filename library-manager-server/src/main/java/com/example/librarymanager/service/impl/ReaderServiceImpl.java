package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.CommonConstant;
import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.SortByDataConstant;
import com.example.librarymanager.constant.SuccessMessage;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.pagination.PagingMeta;
import com.example.librarymanager.domain.dto.request.CreateReaderCardsRequestDto;
import com.example.librarymanager.domain.dto.request.ReaderRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetReaderResponseDto;
import com.example.librarymanager.domain.entity.Reader;
import com.example.librarymanager.domain.mapper.ReaderMappper;
import com.example.librarymanager.domain.specification.EntitySpecification;
import com.example.librarymanager.exception.BadRequestException;
import com.example.librarymanager.exception.ConflictException;
import com.example.librarymanager.exception.NotFoundException;
import com.example.librarymanager.repository.ReaderRepository;
import com.example.librarymanager.service.LogService;
import com.example.librarymanager.service.PdfService;
import com.example.librarymanager.service.ReaderService;
import com.example.librarymanager.util.PaginationUtil;
import com.example.librarymanager.util.UploadFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {

    private static final String TAG = "Quản lý bạn đọc";

    private final ReaderRepository readerRepository;

    private final PasswordEncoder passwordEncoder;

    private final ReaderMappper readerMappper;

    private final LogService logService;

    private final UploadFileUtil uploadFileUtil;

    private final MessageSource messageSource;

    private final PdfService pdfService;

    private Reader getEntity(Long id) {
        return readerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Reader.ERR_NOT_FOUND_ID, id));
    }

    private Reader getEntity(String cardNumber) {
        return readerRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Reader.ERR_NOT_FOUND_CARD_NUMBER, cardNumber));
    }

    public void validatePassword(String password) {
        if (password.length() > 30) {
            throw new BadRequestException(ErrorMessage.INVALID_TEXT_LENGTH);
        } else if (!password.matches(CommonConstant.REGEXP_PASSWORD)) {
            throw new BadRequestException(ErrorMessage.INVALID_FORMAT_PASSWORD);
        }
    }

    @Override
    public void initReadersFromCsv(String readersCsvPath) {
    }

    @Override
    public CommonResponseDto save(ReaderRequestDto requestDto, MultipartFile image, String userId) {
        //Kiểm tra mật khẩu
        String password = requestDto.getPassword();
        if (password == null || password.isEmpty()) {
            throw new BadRequestException(ErrorMessage.INVALID_NOT_BLANK_FIELD);
        } else {
            validatePassword(password);
        }

        //Kiểm tra file tải lên có phải định dạng ảnh không
        uploadFileUtil.checkImageIsValid(image);

        if (readerRepository.existsByCardNumber(requestDto.getCardNumber())) {
            throw new ConflictException(ErrorMessage.Reader.ERR_DUPLICATE_CARD_NUMBER, requestDto.getCardNumber());
        }

        Reader reader = readerMappper.toReader(requestDto);
        reader.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        if (image != null && !image.isEmpty()) {
            String newImageUrl = uploadFileUtil.uploadFile(image);
            reader.setAvatar(newImageUrl);
        }

        readerRepository.save(reader);

        logService.createLog(TAG, "Thêm", "Thêm thẻ bạn đọc mới: " + reader.getCardNumber(), userId);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, new GetReaderResponseDto(reader));
    }

    @Override
    public CommonResponseDto update(Long id, ReaderRequestDto requestDto, MultipartFile image, String userId) {
        //Kiểm tra file tải lên có phải định dạng ảnh không
        uploadFileUtil.checkImageIsValid(image);

        Reader reader = getEntity(id);

        //Nếu mật khẩu khác null thì cập nhật lại mật khẩu
        String password = requestDto.getPassword();
        if (password != null && !password.isEmpty()) {
            validatePassword(password);
            reader.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        }

        if (!Objects.equals(reader.getCardNumber(), requestDto.getCardNumber()) &&
                readerRepository.existsByCardNumber(requestDto.getCardNumber())) {
            throw new ConflictException(ErrorMessage.Reader.ERR_DUPLICATE_CARD_NUMBER, requestDto.getCardNumber());
        }

        if (image != null && !image.isEmpty()) {
            String newImageUrl = uploadFileUtil.uploadFile(image);

            uploadFileUtil.destroyFileWithUrl(reader.getAvatar());

            reader.setAvatar(newImageUrl);
        }

        reader.setCardType(requestDto.getCardType());
        reader.setFullName(requestDto.getFullName());
        reader.setDateOfBirth(requestDto.getDateOfBirth());
        reader.setGender(requestDto.getGender());
        reader.setAddress(requestDto.getAddress());
        reader.setPhoneNumber(requestDto.getPhoneNumber());
        reader.setCardNumber(requestDto.getCardNumber());
        reader.setExpiryDate(requestDto.getExpiryDate());
        reader.setStatus(requestDto.getStatus());

        readerRepository.save(reader);

        logService.createLog(TAG, "Sửa", "Sửa thẻ bạn đọc: " + reader.getCardNumber(), userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, new GetReaderResponseDto(reader));
    }

    @Override
    public CommonResponseDto delete(Long id, String userId) {
        Reader reader = getEntity(id);

        if (!reader.getBorrowReceipts().isEmpty()) {
            throw new BadRequestException(ErrorMessage.Reader.ERR_READER_HAS_DATA);
        }

        if (!reader.getLibraryVisits().isEmpty()) {
            throw new BadRequestException(ErrorMessage.Reader.ERR_READER_HAS_DATA);
        }

        uploadFileUtil.destroyFileWithUrl(reader.getAvatar());

        readerRepository.delete(reader);

        logService.createLog(TAG, "Xóa", "Xóa thẻ bạn đọc: " + reader.getCardNumber(), userId);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public PaginationResponseDto<GetReaderResponseDto> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.READER);

        Page<Reader> page = readerRepository.findAll(
                EntitySpecification.filterReaders(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable);

        List<GetReaderResponseDto> items = page.getContent().stream()
                .map(GetReaderResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.READER, page);

        PaginationResponseDto<GetReaderResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public GetReaderResponseDto findById(Long id) {
        Reader reader = getEntity(id);
        return new GetReaderResponseDto(reader);
    }

    @Override
    public GetReaderResponseDto findByCardNumber(String cardNumber) {
        Reader reader = getEntity(cardNumber);
        return new GetReaderResponseDto(reader);
    }

    @Override
    public byte[] generateReaderCards(CreateReaderCardsRequestDto requestDto) {
        List<Reader> readers = readerRepository.findAllByIdIn(requestDto.getReaderIds());
        if (readers.isEmpty()) {
            throw new BadRequestException(ErrorMessage.Reader.ERR_NOT_FOUND_ID, 1);
        }
        return pdfService.createPdf(requestDto, readers);
    }

}
