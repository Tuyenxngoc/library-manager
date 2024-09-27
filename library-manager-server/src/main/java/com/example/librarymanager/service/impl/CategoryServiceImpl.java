package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.SortByDataConstant;
import com.example.librarymanager.constant.SuccessMessage;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.pagination.PagingMeta;
import com.example.librarymanager.domain.dto.request.CategoryRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetCategoryResponseDto;
import com.example.librarymanager.domain.entity.Category;
import com.example.librarymanager.domain.entity.CategoryGroup;
import com.example.librarymanager.domain.mapper.CategoryMapper;
import com.example.librarymanager.domain.specification.AuthorSpecification;
import com.example.librarymanager.exception.BadRequestException;
import com.example.librarymanager.exception.ConflictException;
import com.example.librarymanager.exception.NotFoundException;
import com.example.librarymanager.repository.CategoryGroupRepository;
import com.example.librarymanager.repository.CategoryRepository;
import com.example.librarymanager.service.CategoryService;
import com.example.librarymanager.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final MessageSource messageSource;

    private final CategoryGroupRepository categoryGroupRepository;

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Category.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public PaginationResponseDto<GetCategoryResponseDto> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.CATEGORY);

        Page<Category> page = categoryRepository.findAll(
                AuthorSpecification.filterCategories(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable);

        List<GetCategoryResponseDto> items = page.getContent().stream()
                .map(GetCategoryResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.CATEGORY, page);

        PaginationResponseDto<GetCategoryResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public CommonResponseDto save(CategoryRequestDto requestDto) {
        CategoryGroup categoryGroup = categoryGroupRepository.findById(requestDto.getParentId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.CategoryGroup.ERR_NOT_FOUND_ID, requestDto.getParentId()));

        if (categoryRepository.existsByCategoryName(requestDto.getCategoryName())) {
            throw new ConflictException(ErrorMessage.Category.ERR_DUPLICATE_NAME);
        }

        if (categoryRepository.existsByCategoryCode(requestDto.getCategoryCode())) {
            throw new ConflictException(ErrorMessage.Category.ERR_DUPLICATE_CODE);
        }

        Category category = categoryMapper.toCategory(requestDto);
        category.setCategoryGroup(categoryGroup);

        categoryRepository.save(category);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, category);
    }

    @Override
    public CommonResponseDto delete(Long id) {
        Category category = findById(id);

        if (!category.getBookDefinitions().isEmpty()) {
            throw new BadRequestException(ErrorMessage.Category.ERR_HAS_LINKED_BOOKS);
        }

        categoryRepository.delete(category);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, true);
    }

    @Override
    public CommonResponseDto update(Long id, CategoryRequestDto requestDto) {
        Category category = findById(id);

        if (!Objects.equals(category.getCategoryName(), requestDto.getCategoryName())
                && categoryRepository.existsByCategoryName(requestDto.getCategoryName())) {
            throw new ConflictException(ErrorMessage.Category.ERR_DUPLICATE_NAME);
        }

        if (!Objects.equals(category.getCategoryCode(), requestDto.getCategoryCode())
                && categoryRepository.existsByCategoryCode(requestDto.getCategoryCode())) {
            throw new ConflictException(ErrorMessage.Category.ERR_DUPLICATE_CODE);
        }

        category.setCategoryName(requestDto.getCategoryName());
        category.setCategoryCode(requestDto.getCategoryCode());

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, category);
    }

    @Override
    public CommonResponseDto toggleActiveStatus(Long id) {
        Category category = findById(id);

        category.setActiveFlag(!category.getActiveFlag());

        categoryRepository.save(category);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, category.getActiveFlag());
    }
}