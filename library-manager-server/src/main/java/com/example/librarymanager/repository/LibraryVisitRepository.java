package com.example.librarymanager.repository;

import com.example.librarymanager.domain.entity.LibraryVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryVisitRepository extends JpaRepository<LibraryVisit, Long> {
}
