package com.example.librarymanager.constant;

public enum SortByDataConstant implements SortByInterface {

    USER {
        @Override
        public String getSortBy(String sortBy) {
            return "createdDate";
        }
    },

    AUTHOR {
        @Override
        public String getSortBy(String sortBy) {
            return switch (sortBy) {
                case "code" -> "code";
                case "fullName" -> "fullName";
                case "penName" -> "penName";
                case "gender" -> "gender";
                case "dateOfBirth" -> "dateOfBirth";
                case "address" -> "address";
                default -> "createdDate";
            };
        }
    },

    CATEGORY_GROUP {
        @Override
        public String getSortBy(String sortBy) {
            return switch (sortBy) {
                case "groupName" -> "groupName";
                case "activeFlag" -> "activeFlag";
                default -> "createdDate";
            };
        }
    },

    CATEGORY {
        @Override
        public String getSortBy(String sortBy) {
            return switch (sortBy) {
                case "categoryName" -> "categoryName";
                case "categoryCode" -> "categoryCode";
                case "activeFlag" -> "activeFlag";
                default -> "createdDate";
            };
        }
    },

    BOOK_SET {
        @Override
        public String getSortBy(String sortBy) {
            return switch (sortBy) {
                case "name" -> "name";
                case "activeFlag" -> "activeFlag";
                default -> "createdDate";
            };
        }
    },

    PUBLISHER {
        @Override
        public String getSortBy(String sortBy) {
            return switch (sortBy) {
                case "code" -> "code";
                case "name" -> "name";
                case "address" -> "address";
                case "city" -> "city";
                case "notes" -> "notes";
                case "activeFlag" -> "activeFlag";
                default -> "createdDate";
            };
        }
    },

    BOOK_DEFINITION {
        @Override
        public String getSortBy(String sortBy) {
            return switch (sortBy) {
                case "title" -> "title";
                case "bookCode" -> "bookCode";
                case "activeFlag" -> "activeFlag";
                default -> "createdDate";
            };
        }
    },

    CLASSIFICATION_SYMBOL {
        @Override
        public String getSortBy(String sortBy) {
            return switch (sortBy) {
                case "name" -> "name";
                case "code" -> "code";
                case "level" -> "level";
                case "activeFlag" -> "activeFlag";
                default -> "createdDate";
            };
        }
    },

}
