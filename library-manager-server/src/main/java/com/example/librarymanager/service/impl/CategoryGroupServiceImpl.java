package com.example.librarymanager.service.impl;

import com.example.librarymanager.domain.entity.CategoryGroup;
import com.example.librarymanager.repository.CategoryGroupRepository;
import com.example.librarymanager.service.CategoryGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryGroupServiceImpl implements CategoryGroupService {

    @Autowired
    private CategoryGroupRepository categoryGroupRepository;

    @Override
    public CategoryGroup findById(Long id) {
        return categoryGroupRepository.findById(id).orElse(null);
    }

    @Override
    public List<CategoryGroup> findAll() {
        return categoryGroupRepository.findAll();
    }

    @Override
    public CategoryGroup save(CategoryGroup categoryGroup) {
        return categoryGroupRepository.save(categoryGroup);
    }

    @Override
    public void delete(Long id) {
        categoryGroupRepository.deleteById(id);
    }
}