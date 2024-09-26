package com.example.librarymanager.service;

import com.example.librarymanager.domain.entity.LibraryVisit;

import java.util.List;

public interface LibraryVisitService {
    LibraryVisit findById(Long id);

    List<LibraryVisit> findAll();

    LibraryVisit save(LibraryVisit libraryVisit);

    void delete(Long id);
}