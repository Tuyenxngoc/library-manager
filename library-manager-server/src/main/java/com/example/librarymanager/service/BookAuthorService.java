package com.example.librarymanager.service;

import com.example.librarymanager.domain.entity.BookAuthor;

import java.util.List;

public interface BookAuthorService {
    BookAuthor findById(Long id);

    List<BookAuthor> findAll();

    BookAuthor save(BookAuthor bookAuthor);

    void delete(Long id);
}