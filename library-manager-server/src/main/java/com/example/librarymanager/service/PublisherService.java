package com.example.librarymanager.service;

import com.example.librarymanager.domain.entity.Publisher;

import java.util.List;

public interface PublisherService {
    Publisher findById(Long id);

    List<Publisher> findAll();

    Publisher save(Publisher publisher);

    void delete(Long id);
}