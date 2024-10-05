package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.SortByDataConstant;
import com.example.librarymanager.constant.SuccessMessage;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.pagination.PagingMeta;
import com.example.librarymanager.domain.dto.request.ExportReceiptRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetExportReceiptResponseDto;
import com.example.librarymanager.domain.entity.ExportReceipt;
import com.example.librarymanager.domain.mapper.ExportReceiptMapper;
import com.example.librarymanager.domain.specification.EntitySpecification;
import com.example.librarymanager.exception.ConflictException;
import com.example.librarymanager.exception.NotFoundException;
import com.example.librarymanager.repository.ExportReceiptRepository;
import com.example.librarymanager.service.ExportReceiptService;
import com.example.librarymanager.service.LogService;
import com.example.librarymanager.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportReceiptServiceImpl implements ExportReceiptService {

    private static final String TAG = "Xuất sách";

    private final ExportReceiptRepository exportReceiptRepository;

    private final ExportReceiptMapper exportReceiptMapper;

    private final MessageSource messageSource;

    private final LogService logService;

    private ExportReceipt getEntity(Long id) {
        return exportReceiptRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ImportReceipt.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public CommonResponseDto save(ExportReceiptRequestDto requestDto, String userId) {
        if (exportReceiptRepository.existsByReceiptNumber(requestDto.getReceiptNumber())) {
            throw new ConflictException(ErrorMessage.ExportReceipt.ERR_DUPLICATE_NUMBER, requestDto.getReceiptNumber());
        }
        ExportReceipt exportReceipt = exportReceiptMapper.toExportReceipt(requestDto);

        logService.createLog(TAG, "Thêm", "Tạo phiếu xuất mới: " + exportReceipt.getReceiptNumber(), userId);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto update(Long id, ExportReceiptRequestDto requestDto, String userId) {
        return null;
    }

    @Override
    public PaginationResponseDto<GetExportReceiptResponseDto> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.EXPORT_RECEIPT);

        Page<ExportReceipt> page = exportReceiptRepository.findAll(
                EntitySpecification.filterExportReceipts(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable);

        List<GetExportReceiptResponseDto> items = page.getContent().stream()
                .map(GetExportReceiptResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.EXPORT_RECEIPT, page);

        PaginationResponseDto<GetExportReceiptResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public GetExportReceiptResponseDto findById(Long id) {
        ExportReceipt exportReceipt = getEntity(id);
        return new GetExportReceiptResponseDto(exportReceipt);
    }
}