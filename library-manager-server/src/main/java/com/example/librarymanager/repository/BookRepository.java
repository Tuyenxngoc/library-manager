package com.example.librarymanager.repository;

import com.example.librarymanager.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    long countByBookDefinitionId(Long id);

    Optional<Book> findByBookCode(String code);
}
