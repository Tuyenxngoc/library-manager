package com.example.librarymanager.util;

public class MaskingUtils {

    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email; // Không che dấu nếu email không hợp lệ
        }
        int atIndex = email.indexOf("@");
        if (atIndex <= 2) {
            return email; // Không che dấu nếu phần trước ký tự @ quá ngắn
        }
        String localPart = email.substring(0, atIndex);
        String domainPart = email.substring(atIndex);
        return localPart.charAt(0) + "*****" + localPart.charAt(localPart.length() - 1) + domainPart;
    }

    public static String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 7) {
            return phoneNumber; // Không che dấu nếu số quá ngắn
        }
        return phoneNumber.substring(0, 2) + "*****" + phoneNumber.substring(phoneNumber.length() - 3);
    }

}
