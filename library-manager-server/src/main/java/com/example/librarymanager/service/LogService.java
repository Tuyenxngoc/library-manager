package com.example.librarymanager.service;

import com.example.librarymanager.domain.entity.Log;

import java.util.List;

public interface LogService {
    Log findById(Long id);

    List<Log> findAll();

    Log save(Log log);

    void delete(Long id);
}