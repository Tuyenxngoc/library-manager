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
    }

}
