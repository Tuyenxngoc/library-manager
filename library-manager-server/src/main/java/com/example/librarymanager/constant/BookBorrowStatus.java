package com.example.librarymanager.constant;

public enum BookBorrowStatus {
    NOT_RETURNED("Chưa trả"),
    LOST("Báo mất"),
    RETURNED("Đã trả"),
    OVERDUE("Quá hạn");

    private final String name;

    BookBorrowStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
