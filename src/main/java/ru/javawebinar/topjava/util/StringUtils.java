package ru.javawebinar.topjava.util;

public final class StringUtils {

    public static boolean isNullOrEmpty(String s) {
        if (s == null || s.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isNullOrWhiteSpace(String s) {
        if (s == null || s.trim().isEmpty()) {
            return true;
        }
        return false;
    }
}
