package com.example.librarymanager.repository;

import com.example.librarymanager.domain.entity.BookDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDefinitionRepository extends JpaRepository<BookDefinition, Long>, JpaSpecificationExecutor<BookDefinition> {
}
