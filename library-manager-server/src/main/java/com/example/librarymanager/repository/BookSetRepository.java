package com.example.librarymanager.repository;

import com.example.librarymanager.domain.entity.BookSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookSetRepository extends JpaRepository<BookSet, Long>, JpaSpecificationExecutor<BookSet> {
    boolean existsByName(String name);
}