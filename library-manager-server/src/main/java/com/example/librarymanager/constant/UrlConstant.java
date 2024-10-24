package com.example.librarymanager.constant;

public class UrlConstant {

    public static final String ADMIN_URL = "/admin";

    public static class Auth {
        private static final String PRE_FIX = "/auth";

        public static final String LOGIN = PRE_FIX + "/login";
        public static final String LOGOUT = PRE_FIX + "/logout";
        public static final String FORGET_PASSWORD = PRE_FIX + "/forget-password";
        public static final String CHANGE_PASSWORD = PRE_FIX + "/change-password";
        public static final String REFRESH_TOKEN = PRE_FIX + "/refresh-token";

        public static final String ADMIN_LOGIN = ADMIN_URL + LOGIN;
    }

    public static class User {
        private static final String PRE_FIX = "/users";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";

        public static final String GET_CURRENT_USER = PRE_FIX + "/current";
    }

    public static class Role {
        private static final String PRE_FIX = "/roles";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
    }

    public static class Author {
        private static final String PRE_FIX = "/authors";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
        public static final String TOGGLE_ACTIVE = PRE_FIX + "/{id}/toggle-active";
    }

    public static class Category {
        private static final String PRE_FIX = "/categories";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
        public static final String TOGGLE_ACTIVE = PRE_FIX + "/{id}/toggle-active";
    }

    public static class CategoryGroup {
        private static final String PRE_FIX = "/category-groups";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
        public static final String TOGGLE_ACTIVE = PRE_FIX + "/{id}/toggle-active";
        public static final String GET_TREE = PRE_FIX + "/tree";
    }

    public static class BookSet {
        private static final String PRE_FIX = "/book-sets";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
        public static final String TOGGLE_ACTIVE = PRE_FIX + "/{id}/toggle-active";
    }

    public static class Publisher {
        private static final String PRE_FIX = "/publishers";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
        public static final String TOGGLE_ACTIVE = PRE_FIX + "/{id}/toggle-active";
    }

    public static class BookDefinition {
        private static final String PRE_FIX = "/book-definitions";

        public static final String CREATE = ADMIN_URL + PRE_FIX;
        public static final String GET_ALL = ADMIN_URL + PRE_FIX;
        public static final String GET_BY_ID = ADMIN_URL + PRE_FIX + "/{id}";
        public static final String UPDATE = ADMIN_URL + PRE_FIX + "/{id}";
        public static final String DELETE = ADMIN_URL + PRE_FIX + "/{id}";
        public static final String TOGGLE_ACTIVE = ADMIN_URL + PRE_FIX + "/{id}/toggle-active";
        public static final String GET_BOOKS = ADMIN_URL + PRE_FIX + "/books";

        public static final String GET_BOOKS_FOR_USER = PRE_FIX + "/books";
    }

    public static class ClassificationSymbol {
        private static final String PRE_FIX = "/classification-symbols";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
        public static final String TOGGLE_ACTIVE = PRE_FIX + "/{id}/toggle-active";
    }

    public static class Log {
        private static final String PRE_FIX = "/logs";

        public static final String GET_ALL = PRE_FIX;
    }

    public static class ImportReceipt {
        private static final String PRE_FIX = "/import-receipts";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
    }

    public static class ExportReceipt {
        private static final String PRE_FIX = "/export-receipts";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
    }

    public static class Book {
        private static final String PRE_FIX = "/books";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
    }

    public static class NewsArticle {
        private static final String PRE_FIX = "/news-articles";

        public static final String CREATE = ADMIN_URL + PRE_FIX;
        public static final String GET_ALL = ADMIN_URL + PRE_FIX;
        public static final String GET_BY_ID = ADMIN_URL + PRE_FIX + "/{id}";
        public static final String UPDATE = ADMIN_URL + PRE_FIX + "/{id}";
        public static final String DELETE = ADMIN_URL + PRE_FIX + "/{id}";
        public static final String TOGGLE_ACTIVE = ADMIN_URL + PRE_FIX + "/{id}/toggle-active";

        public static final String GET_ALL_FOR_USER = PRE_FIX;
        public static final String GET_BY_ID_FOR_USER = PRE_FIX + "/{id}";

    }

    public static class UserGroup {
        private static final String PRE_FIX = "/user-groups";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
        public static final String TOGGLE_ACTIVE = PRE_FIX + "/{id}/toggle-active";
    }

    public static class Reader {
        private static final String PRE_FIX = "/readers";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String GET_BY_CARD_NUMBER = PRE_FIX + "/card-number/{cardNumber}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
        public static final String PRINT_CARDS = PRE_FIX + "/print-cards";
    }

    public static class LibraryVisit {
        private static final String PRE_FIX = "/library-visits";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
        public static final String CLOSE = PRE_FIX + "/close";
    }

    public static class ReaderViolation {
        private static final String PRE_FIX = "/reader-violations";

        public static final String CREATE = PRE_FIX;
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
    }

    public static class Stats {
        private static final String PRE_FIX = "/stats";

        public static final String GET_LIBRARY_STATISTICS = PRE_FIX + "/library";
    }
}
