package com.example.librarymanager.service.impl;

import com.example.librarymanager.domain.entity.LibraryVisit;
import com.example.librarymanager.repository.LibraryVisitRepository;
import com.example.librarymanager.service.LibraryVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryVisitServiceImpl implements LibraryVisitService {

    @Autowired
    private LibraryVisitRepository libraryVisitRepository;

    @Override
    public LibraryVisit findById(Long id) {
        return libraryVisitRepository.findById(id).orElse(null);
    }

    @Override
    public List<LibraryVisit> findAll() {
        return libraryVisitRepository.findAll();
    }

    @Override
    public LibraryVisit save(LibraryVisit libraryVisit) {
        return libraryVisitRepository.save(libraryVisit);
    }

    @Override
    public void delete(Long id) {
        libraryVisitRepository.deleteById(id);
    }
}