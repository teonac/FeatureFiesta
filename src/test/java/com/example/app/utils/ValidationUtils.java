package com.example.app.utils;

public class ValidationUtils {
    public static boolean is11ProefRespected(String bsn) {
        if (bsn == null || !bsn.matches("\\d{9}")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < bsn.length(); i++) {
            int digit = Character.getNumericValue(bsn.charAt(i));
            int weight = (i == bsn.length() - 1) ? -1 : (9 - i);
            sum += digit * weight;
        }

        return sum % 11 == 0;
    }

    public static boolean containsLetters(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        return input.matches(".*[a-zA-Z]+.*");
    }

    public static boolean isValidUUID(String uuidAsString) {
        if (uuidAsString == null) {
            return false;
        }
        String sanitizedUUID = uuidAsString.replace("-", "");
        return sanitizedUUID.matches("[0-9a-fA-F]{32}");
    }

    public static boolean checkNull(String value) {
        return value == null || value.equals("null") || value.equals("");
    }

}
