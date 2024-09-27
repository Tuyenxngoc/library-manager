package com.example.librarymanager.repository;

import com.example.librarymanager.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    boolean existsByCategoryName(String categoryName);

    boolean existsByCategoryCode(String categoryCode);
}
