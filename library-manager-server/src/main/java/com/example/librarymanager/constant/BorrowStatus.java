package com.example.librarymanager.constant;

public enum BorrowStatus {
    BORROWED("Đang mượn"),
    RETURNED("Đã trả"),
    OVERDUE("Quá hạn");

    private final String name;

    BorrowStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
