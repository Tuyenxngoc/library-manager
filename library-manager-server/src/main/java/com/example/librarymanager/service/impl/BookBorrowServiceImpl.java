package com.example.librarymanager.service.impl;

import com.example.librarymanager.domain.entity.BookBorrow;
import com.example.librarymanager.repository.BookBorrowRepository;
import com.example.librarymanager.service.BookBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookBorrowServiceImpl implements BookBorrowService {

    @Autowired
    private BookBorrowRepository bookBorrowRepository;

    @Override
    public BookBorrow findById(Long id) {
        return bookBorrowRepository.findById(id).orElse(null);
    }

    @Override
    public List<BookBorrow> findAll() {
        return bookBorrowRepository.findAll();
    }

    @Override
    public BookBorrow save(BookBorrow bookBorrow) {
        return bookBorrowRepository.save(bookBorrow);
    }

    @Override
    public void delete(Long id) {
        bookBorrowRepository.deleteById(id);
    }
}
