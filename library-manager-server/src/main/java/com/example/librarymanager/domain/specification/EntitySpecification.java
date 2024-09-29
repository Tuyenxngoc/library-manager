package com.example.librarymanager.domain.specification;

import com.example.librarymanager.domain.entity.*;
import com.example.librarymanager.util.SpecificationsUtil;
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

    public static Specification<BookDefinition> filterBookDefinitions(String keyword, String searchBy) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case BookDefinition_.BOOK_CODE ->
                            predicate = builder.and(predicate, builder.like(root.get(BookDefinition_.bookCode), "%" + keyword + "%"));

                    case BookDefinition_.TITLE ->
                            predicate = builder.and(predicate, builder.like(root.get(BookDefinition_.TITLE), "%" + keyword + "%"));
                }
            }
            return predicate;
        };
    }

    public static Specification<ClassificationSymbol> filterClassificationSymbols(String keyword, String searchBy) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case ClassificationSymbol_.CODE ->
                            predicate = builder.and(predicate, builder.like(root.get(ClassificationSymbol_.code), "%" + keyword + "%"));

                    case ClassificationSymbol_.NAME ->
                            predicate = builder.and(predicate, builder.like(root.get(ClassificationSymbol_.name), "%" + keyword + "%"));

                    case ClassificationSymbol_.LEVEL ->
                            predicate = builder.and(predicate, builder.equal(root.get(ClassificationSymbol_.level),
                                    SpecificationsUtil.castToRequiredType(root.get(ClassificationSymbol_.level).getJavaType(), keyword)));
                }
            }
            return predicate;
        };
    }
}
