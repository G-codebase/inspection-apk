package com.example.myapss.utils;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean isValidEmail(String email) {
        return email != null && emailPattern.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpperCase = password.matches(".*[A-Z].*");
        boolean hasLowerCase = password.matches(".*[a-z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};:'\",.<>?/].*");

        return hasUpperCase && hasLowerCase && hasNumber && hasSpecial;
    }

    public static int getPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return -1;
        }

        int score = 0;

        if (password.length() >= 8) score++;
        if (password.length() >= 12) score++;
        if (password.matches(".*[a-z].*")) score++;
        if (password.matches(".*[A-Z].*")) score++;
        if (password.matches(".*[0-9].*")) score++;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};:'\",.<>?/].*")) score++;

        return Math.min(score - 1, 4);
    }

    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.replaceAll("[^0-9]", "").length() >= 10;
    }

    public static boolean isNumeric(String text) {
        return text != null && text.matches("[0-9]+");
    }
}