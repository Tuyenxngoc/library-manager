package com.example.librarymanager.service;

import com.example.librarymanager.domain.entity.Author;

import java.util.List;

public interface AuthorService {
    Author findById(Long id);

    List<Author> findAll();

    Author save(Author author);

    void delete(Long id);
}