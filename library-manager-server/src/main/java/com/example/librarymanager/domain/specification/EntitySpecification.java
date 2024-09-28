package com.example.librarymanager.domain.specification;


import com.example.librarymanager.domain.entity.*;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class EntitySpecification {

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

    public static Specification<CategoryGroup> filterCategoryGroups(String keyword, String searchBy) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case CategoryGroup_.GROUP_NAME ->
                            predicate = builder.and(predicate, builder.like(root.get(CategoryGroup_.groupName), "%" + keyword + "%"));
                }
            }
            return predicate;
        };
    }

    public static Specification<BookSet> filterBookSets(String keyword, String searchBy) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case BookSet_.NAME ->
                            predicate = builder.and(predicate, builder.like(root.get(BookSet_.name), "%" + keyword + "%"));
                }
            }
            return predicate;
        };
    }

    public static Specification<Publisher> filterPublishers(String keyword, String searchBy) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case Publisher_.CODE ->
                            predicate = builder.and(predicate, builder.like(root.get(Publisher_.code), "%" + keyword + "%"));

                    case Publisher_.NAME ->
                            predicate = builder.and(predicate, builder.like(root.get(Publisher_.name), "%" + keyword + "%"));

                    case Publisher_.ADDRESS ->
                            predicate = builder.and(predicate, builder.like(root.get(Publisher_.address), "%" + keyword + "%"));

                    case Publisher_.CITY ->
                            predicate = builder.and(predicate, builder.like(root.get(Publisher_.city), "%" + keyword + "%"));
                }
            }
            return predicate;
        };
    }
}