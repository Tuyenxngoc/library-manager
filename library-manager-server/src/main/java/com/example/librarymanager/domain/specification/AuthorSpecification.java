package com.example.librarymanager.domain.specification;


import com.example.librarymanager.domain.entity.Author;
import com.example.librarymanager.domain.entity.Author_;
import com.example.librarymanager.domain.entity.Category;
import com.example.librarymanager.domain.entity.Category_;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class AuthorSpecification {

    public static Specification<Author> filterAuthors(String keyword, String searchBy) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case Author_.CODE ->
                            predicate = builder.and(predicate, builder.like(root.get(Author_.code), "%" + keyword + "%"));

                    case Author_.FULL_NAME ->
                            predicate = builder.and(predicate, builder.like(root.get(Author_.fullName), "%" + keyword + "%"));
                }
            }
            return predicate;
        };
    }

    public static Specification<Category> filterCategories(String keyword, String searchBy) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case Category_.CATEGORY_NAME ->
                            predicate = builder.and(predicate, builder.like(root.get(Category_.categoryName), "%" + keyword + "%"));
                }
            }
            return predicate;
        };
    }

}
