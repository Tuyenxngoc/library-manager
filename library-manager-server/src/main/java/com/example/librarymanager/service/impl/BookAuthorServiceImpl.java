package com.example.librarymanager.service.impl;

import com.example.librarymanager.domain.entity.BookAuthor;
import com.example.librarymanager.repository.BookAuthorRepository;
import com.example.librarymanager.service.BookAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookAuthorServiceImpl implements BookAuthorService {

    @Autowired
    private BookAuthorRepository bookAuthorRepository;

    @Override
    public BookAuthor findById(Long id) {
        return bookAuthorRepository.findById(id).orElse(null);
    }

    @Override
    public List<BookAuthor> findAll() {
        return bookAuthorRepository.findAll();
    }

    @Override
    public BookAuthor save(BookAuthor bookAuthor) {
        return bookAuthorRepository.save(bookAuthor);
    }

    @Override
    public void delete(Long id) {
        bookAuthorRepository.deleteById(id);
    }
}