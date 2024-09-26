package com.example.librarymanager.service;

import com.example.librarymanager.domain.entity.CategoryGroup;

import java.util.List;

public interface CategoryGroupService {
    CategoryGroup findById(Long id);

    List<CategoryGroup> findAll();

    CategoryGroup save(CategoryGroup categoryGroup);

    void delete(Long id);
}