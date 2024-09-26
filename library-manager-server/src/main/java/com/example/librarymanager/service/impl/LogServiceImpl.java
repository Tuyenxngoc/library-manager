package com.example.librarymanager.service.impl;

import com.example.librarymanager.domain.entity.Log;
import com.example.librarymanager.repository.LogRepository;
import com.example.librarymanager.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Override
    public Log findById(Long id) {
        return logRepository.findById(id).orElse(null);
    }

    @Override
    public List<Log> findAll() {
        return logRepository.findAll();
    }

    @Override
    public Log save(Log log) {
        return logRepository.save(log);
    }

    @Override
    public void delete(Long id) {
        logRepository.deleteById(id);
    }
}