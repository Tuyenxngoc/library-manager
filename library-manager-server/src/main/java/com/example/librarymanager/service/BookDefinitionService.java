package com.example.librarymanager.service;

import com.example.librarymanager.domain.entity.BookDefinition;

import java.util.List;

public interface BookDefinitionService {
    BookDefinition findById(Long id);

    List<BookDefinition> findAll();

    BookDefinition save(BookDefinition bookDefinition);

    void delete(Long id);
}