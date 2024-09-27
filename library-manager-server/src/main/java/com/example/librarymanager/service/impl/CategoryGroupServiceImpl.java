package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.SortByDataConstant;
import com.example.librarymanager.constant.SuccessMessage;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.pagination.PagingMeta;
import com.example.librarymanager.domain.dto.request.CategoryGroupRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.entity.CategoryGroup;
import com.example.librarymanager.domain.mapper.CategoryGroupMapper;
import com.example.librarymanager.exception.BadRequestException;
import com.example.librarymanager.exception.ConflictException;
import com.example.librarymanager.exception.NotFoundException;
import com.example.librarymanager.repository.CategoryGroupRepository;
import com.example.librarymanager.service.CategoryGroupService;
import com.example.librarymanager.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryGroupServiceImpl implements CategoryGroupService {

    private final CategoryGroupRepository categoryGroupRepository;

    private final CategoryGroupMapper categoryGroupMapper;

    private final MessageSource messageSource;

    @Override
    public CommonResponseDto save(CategoryGroupRequestDto requestDto) {
        if (categoryGroupRepository.existsByGroupName(requestDto.getGroupName())) {
            throw new ConflictException(ErrorMessage.CategoryGroup.ERR_DUPLICATE_GROUP_NAME);
        }

        CategoryGroup categoryGroup = categoryGroupMapper.toCategoryGroup(requestDto);

        categoryGroupRepository.save(categoryGroup);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, categoryGroup);
    }

    @Override
    public CommonResponseDto update(Long id, CategoryGroupRequestDto requestDto) {
        if (categoryGroupRepository.existsByGroupName(requestDto.getGroupName())) {
            throw new ConflictException(ErrorMessage.CategoryGroup.ERR_DUPLICATE_GROUP_NAME);
        }

        CategoryGroup categoryGroup = findById(id);

        categoryGroup.setGroupName(requestDto.getGroupName());

        categoryGroupRepository.save(categoryGroup);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, categoryGroup);
    }

    @Override
    public CommonResponseDto delete(Long id) {
        CategoryGroup categoryGroup = findById(id);

        if (!categoryGroup.getCategories().isEmpty()) {
            throw new BadRequestException(ErrorMessage.CategoryGroup.ERR_HAS_LINKED_CATEGORIES);
        }

        categoryGroupRepository.delete(categoryGroup);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, true);
    }

    @Override
    public PaginationResponseDto<CategoryGroup> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.CATEGORY);

        Page<CategoryGroup> page = categoryGroupRepository.findAll(pageable);

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.AUTHOR, page);

        PaginationResponseDto<CategoryGroup> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public CategoryGroup findById(Long id) {
        return categoryGroupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.CategoryGroup.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public CommonResponseDto toggleActiveStatus(Long id) {
        CategoryGroup categoryGroup = findById(id);

        categoryGroup.setActiveFlag(!categoryGroup.getActiveFlag());

        categoryGroupRepository.save(categoryGroup);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, categoryGroup.getActiveFlag());
    }

}