package com.example.librarymanager.repository;

import com.example.librarymanager.domain.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long>, JpaSpecificationExecutor<Reader> {
    Optional<Reader> findByCardNumber(String cardNumber);

    boolean existsByCardNumber(String cardNumber);
}
