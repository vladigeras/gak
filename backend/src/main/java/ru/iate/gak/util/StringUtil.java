package ru.iate.gak.util;

public class StringUtil {
    public static boolean isStringNullOrEmptyTrim(String input) {
        return input == null || input.trim().isEmpty();
    }
}
