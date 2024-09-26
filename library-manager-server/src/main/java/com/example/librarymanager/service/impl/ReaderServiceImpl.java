package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.CardType;
import com.example.librarymanager.domain.entity.Reader;
import com.example.librarymanager.repository.ReaderRepository;
import com.example.librarymanager.service.ReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void initReaders() {
        if (readerRepository.count() == 0) {
            Reader reader = new Reader();
            reader.setCardType(CardType.STUDENT);
            reader.setFullName("John Doe");
            reader.setDateOfBirth(LocalDate.now());
            reader.setCardNumber("1234567890");
            reader.setPassword(passwordEncoder.encode("123"));
            reader.setCreatedBy("Tuyen Ngoc");
            reader.setLastModifiedBy("Tuyen Ngoc");

            readerRepository.save(reader);
        }
    }

}
