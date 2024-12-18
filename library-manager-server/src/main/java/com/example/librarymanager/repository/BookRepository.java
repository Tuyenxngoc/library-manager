package com.example.librarymanager.repository;

import com.example.librarymanager.domain.dto.response.book.BookResponseDto;
import com.example.librarymanager.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    long countByBookDefinitionId(Long id);

    Optional<Book> findByBookCode(String code);

    @Query("SELECT new com.example.librarymanager.domain.dto.response.book.BookResponseDto(b) " +
            "FROM Book b " +
            "WHERE b.id IN :ids")
    List<BookResponseDto> findBooksByIds(Set<Long> ids);
}
