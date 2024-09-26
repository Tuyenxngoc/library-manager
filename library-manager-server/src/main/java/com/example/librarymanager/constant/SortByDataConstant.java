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
            return "createdDate";
        }
    }

}
