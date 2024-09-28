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
}
