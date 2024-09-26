package com.example.librarymanager.service;

import com.example.librarymanager.domain.entity.BookBorrow;

import java.util.List;

public interface BookBorrowService {
    BookBorrow findById(Long id);

    List<BookBorrow> findAll();

    BookBorrow save(BookBorrow bookBorrow);

    void delete(Long id);
}