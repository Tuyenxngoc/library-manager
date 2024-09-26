package com.example.librarymanager.repository;

import com.example.librarymanager.domain.entity.BookBorrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookBorrowRepository extends JpaRepository<BookBorrow, Long> {
}
