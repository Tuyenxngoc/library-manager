package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.SortByDataConstant;
import com.example.librarymanager.constant.SuccessMessage;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.pagination.PagingMeta;
import com.example.librarymanager.domain.dto.request.PublisherRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.entity.Publisher;
import com.example.librarymanager.domain.mapper.PublisherMapper;
import com.example.librarymanager.domain.specification.EntitySpecification;
import com.example.librarymanager.exception.BadRequestException;
import com.example.librarymanager.exception.ConflictException;
import com.example.librarymanager.exception.NotFoundException;
import com.example.librarymanager.repository.PublisherRepository;
import com.example.librarymanager.service.PublisherService;
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
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;

    private final MessageSource messageSource;

    private final PublisherMapper publisherMapper;

    @Override
    public CommonResponseDto save(PublisherRequestDto requestDto) {
        if (publisherRepository.existsByCode(requestDto.getCode())) {
            throw new ConflictException(ErrorMessage.Publisher.ERR_DUPLICATE_CODE);
        }

        Publisher publisher = publisherMapper.toPublisher(requestDto);

        publisherRepository.save(publisher);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, publisher);
    }

    @Override
    public CommonResponseDto update(Long id, PublisherRequestDto requestDto) {
        Publisher publisher = findById(id);

        if (!Objects.equals(publisher.getCode(), requestDto.getCode()) && publisherRepository.existsByCode(requestDto.getCode())) {
            throw new ConflictException(ErrorMessage.Publisher.ERR_DUPLICATE_CODE);
        }

        publisher.setCode(requestDto.getCode());
        publisher.setName(requestDto.getName());
        publisher.setAddress(requestDto.getAddress());
        publisher.setCity(requestDto.getCity());
        publisher.setNotes(requestDto.getNotes());

        publisherRepository.save(publisher);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, publisher);
    }

    @Override
    public CommonResponseDto delete(Long id) {
        Publisher publisher = findById(id);

        if (!publisher.getBookDefinitions().isEmpty()) {
            throw new BadRequestException(ErrorMessage.Publisher.ERR_HAS_LINKED_BOOKS);
        }

        publisherRepository.delete(publisher);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public PaginationResponseDto<Publisher> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.PUBLISHER);

        Page<Publisher> page = publisherRepository.findAll(
                EntitySpecification.filterPublishers(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable);

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.PUBLISHER, page);

        PaginationResponseDto<Publisher> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public Publisher findById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Publisher.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public CommonResponseDto toggleActiveStatus(Long id) {
        Publisher publisher = findById(id);

        publisher.setActiveFlag(!publisher.getActiveFlag());

        publisherRepository.save(publisher);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, publisher.getActiveFlag());
    }
}