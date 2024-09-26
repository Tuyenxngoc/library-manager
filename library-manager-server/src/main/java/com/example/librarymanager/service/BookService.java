package com.example.librarymanager.service;

import com.example.librarymanager.domain.entity.Book;

import java.util.List;

public interface BookService {
    Book findById(Long id);

    List<Book> findAll();

    Book save(Book book);

    void delete(Long id);
}