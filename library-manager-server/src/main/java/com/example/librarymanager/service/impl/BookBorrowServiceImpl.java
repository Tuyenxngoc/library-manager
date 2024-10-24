package com.example.librarymanager.service.impl;

import com.example.librarymanager.repository.BookBorrowRepository;
import com.example.librarymanager.service.BookBorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookBorrowServiceImpl implements BookBorrowService {

    private final BookBorrowRepository bookBorrowRepository;

}
