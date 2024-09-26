package com.example.librarymanager.service;

import com.example.librarymanager.domain.entity.Category;

import java.util.List;

public interface CategoryService {
    Category findById(Long id);

    List<Category> findAll();

    Category save(Category category);

    void delete(Long id);
}