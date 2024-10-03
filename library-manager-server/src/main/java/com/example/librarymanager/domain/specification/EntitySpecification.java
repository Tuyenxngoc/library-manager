package com.example.librarymanager.domain.specification;

import com.example.librarymanager.domain.dto.filter.LogFilter;
import com.example.librarymanager.domain.entity.*;
import com.example.librarymanager.util.SpecificationsUtil;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class EntitySpecification {

    public static Specification<Author> filterAuthors(String keyword, String searchBy, Boolean activeFlag) {
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

            if (activeFlag != null) {
                predicate = builder.and(predicate, builder.equal(root.get(Author_.activeFlag), activeFlag));
            }

            return predicate;
        };
    }

    public static Specification<Category> filterCategories(String keyword, String searchBy, Boolean activeFlag) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case Category_.CATEGORY_NAME ->
                            predicate = builder.and(predicate, builder.like(root.get(Category_.categoryName), "%" + keyword + "%"));
                }
            }

            if (activeFlag != null) {
                predicate = builder.and(predicate, builder.equal(root.get(Category_.activeFlag), activeFlag));
            }

            return predicate;
        };
    }

    public static Specification<CategoryGroup> filterCategoryGroups(String keyword, String searchBy, Boolean activeFlag) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case CategoryGroup_.GROUP_NAME ->
                            predicate = builder.and(predicate, builder.like(root.get(CategoryGroup_.groupName), "%" + keyword + "%"));
                }
            }

            if (activeFlag != null) {
                predicate = builder.and(predicate, builder.equal(root.get(CategoryGroup_.activeFlag), activeFlag));
            }

            return predicate;
        };
    }

    public static Specification<BookSet> filterBookSets(String keyword, String searchBy, Boolean activeFlag) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case BookSet_.NAME ->
                            predicate = builder.and(predicate, builder.like(root.get(BookSet_.name), "%" + keyword + "%"));
                }
            }

            if (activeFlag != null) {
                predicate = builder.and(predicate, builder.equal(root.get(BookSet_.activeFlag), activeFlag));
            }

            return predicate;
        };
    }

    public static Specification<Publisher> filterPublishers(String keyword, String searchBy, Boolean activeFlag) {
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

            if (activeFlag != null) {
                predicate = builder.and(predicate, builder.equal(root.get(Publisher_.activeFlag), activeFlag));
            }

            return predicate;
        };
    }

    public static Specification<BookDefinition> filterBookDefinitions(String keyword, String searchBy, Boolean activeFlag) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case BookDefinition_.TITLE ->
                            predicate = builder.and(predicate, builder.like(root.get(BookDefinition_.title), "%" + keyword + "%"));

                    case BookDefinition_.BOOK_CODE ->
                            predicate = builder.and(predicate, builder.like(root.get(BookDefinition_.bookCode), "%" + keyword + "%"));
                }
            }

            if (activeFlag != null) {
                predicate = builder.and(predicate, builder.equal(root.get(BookDefinition_.activeFlag), activeFlag));
            }

            return predicate;
        };
    }

    public static Specification<ClassificationSymbol> filterClassificationSymbols(String keyword, String searchBy, Boolean activeFlag) {
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

    public static Specification<Log> filterLogs(String keyword, String searchBy, LogFilter logFilter) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case Log_.USER -> {
                        Join<Log, User> userJoin = root.join(Log_.user);
                        predicate = builder.and(predicate, builder.like(userJoin.get(User_.username), "%" + keyword + "%"));
                    }

                    case Log_.FEATURE ->
                            predicate = builder.and(predicate, builder.like(root.get(Log_.feature), "%" + keyword + "%"));

                    case Log_.EVENT ->
                            predicate = builder.and(predicate, builder.like(root.get(Log_.event), "%" + keyword + "%"));

                    case Log_.CONTENT ->
                            predicate = builder.and(predicate, builder.like(root.get(Log_.content), "%" + keyword + "%"));
                }
            }

            if (logFilter != null) {
                if (logFilter.getStartDate() != null) {
                    predicate = builder.and(predicate, builder.greaterThanOrEqualTo(root.get(Log_.timestamp), logFilter.getStartDate().atStartOfDay()));
                }

                if (logFilter.getEndDate() != null) {
                    predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get(Log_.timestamp), logFilter.getEndDate().atTime(23, 59, 59)));
                }
            }

            return predicate;
        };
    }

    public static Specification<ImportReceipt> filterImportReceipts(String keyword, String searchBy) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case ImportReceipt_.ID ->
                            predicate = builder.and(predicate, builder.equal(root.get(ImportReceipt_.ID),
                                    SpecificationsUtil.castToRequiredType(root.get(ImportReceipt_.id).getJavaType(), keyword)));

                    case ImportReceipt_.RECEIPT_NUMBER ->
                            predicate = builder.and(predicate, builder.like(root.get(ImportReceipt_.receiptNumber), "%" + keyword + "%"));

                    case ImportReceipt_.FUNDING_SOURCE ->
                            predicate = builder.and(predicate, builder.like(root.get(ImportReceipt_.fundingSource), "%" + keyword + "%"));

                    case ImportReceipt_.IMPORT_REASON ->
                            predicate = builder.and(predicate, builder.like(root.get(ImportReceipt_.importReason), "%" + keyword + "%"));

                    case ImportReceipt_.GENERAL_RECORD_NUMBER ->
                            predicate = builder.and(predicate, builder.like(root.get(ImportReceipt_.generalRecordNumber), "%" + keyword + "%"));
                }
            }
            return predicate;
        };
    }

    public static Specification<Book> filterBooks(String keyword, String searchBy) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case Book_.ID -> predicate = builder.and(predicate, builder.equal(root.get(Book_.ID),
                            SpecificationsUtil.castToRequiredType(root.get(Book_.id).getJavaType(), keyword)));

                }
            }
            return predicate;
        };
    }

    public static Specification<NewsArticle> filterNewsArticles(String keyword, String searchBy, Boolean activeFlag) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case NewsArticle_.TITLE ->
                            predicate = builder.and(predicate, builder.like(root.get(NewsArticle_.title), "%" + keyword + "%"));

                    case NewsArticle_.DESCRIPTION ->
                            predicate = builder.and(predicate, builder.like(root.get(NewsArticle_.description), "%" + keyword + "%"));
                }
            }

            if (activeFlag != null) {
                predicate = builder.and(predicate, builder.equal(root.get(NewsArticle_.activeFlag), activeFlag));
            }

            return predicate;
        };
    }

    public static Specification<UserGroup> filterUserGroups(String keyword, String searchBy, Boolean activeFlag) {
        return (root, query, builder) -> {
            query.distinct(true);

            Predicate predicate = builder.conjunction();

            if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(searchBy)) {
                switch (searchBy) {
                    case UserGroup_.CODE ->
                            predicate = builder.and(predicate, builder.like(root.get(UserGroup_.code), "%" + keyword + "%"));

                    case UserGroup_.NAME ->
                            predicate = builder.and(predicate, builder.like(root.get(UserGroup_.name), "%" + keyword + "%"));
                }
            }

            if (activeFlag != null) {
                predicate = builder.and(predicate, builder.equal(root.get(UserGroup_.activeFlag), activeFlag));
            }

            return predicate;
        };
    }
}
