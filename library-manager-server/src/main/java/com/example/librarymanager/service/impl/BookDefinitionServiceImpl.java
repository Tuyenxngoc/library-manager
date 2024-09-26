package com.example.librarymanager.service.impl;

import com.example.librarymanager.domain.entity.BookDefinition;
import com.example.librarymanager.repository.BookDefinitionRepository;
import com.example.librarymanager.service.BookDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookDefinitionServiceImpl implements BookDefinitionService {

    @Autowired
    private BookDefinitionRepository bookDefinitionRepository;

    @Override
    public BookDefinition findById(Long id) {
        return bookDefinitionRepository.findById(id).orElse(null);
    }

    @Override
    public List<BookDefinition> findAll() {
        return bookDefinitionRepository.findAll();
    }

    @Override
    public BookDefinition save(BookDefinition bookDefinition) {
        return bookDefinitionRepository.save(bookDefinition);
    }

    @Override
    public void delete(Long id) {
        bookDefinitionRepository.deleteById(id);
    }
}