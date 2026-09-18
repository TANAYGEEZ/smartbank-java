package com.smartbank.util;

/**
 * Utility class for validating user inputs.
 * Demonstrates: Encapsulation, validation logic.
 */
public final class InputValidator {

    private InputValidator() {
        // Prevent instantiation
    }

    /**
     * Validates account number format (e.g., ACC1001).
     */
    public static boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }
        return accountNumber.matches("ACC\\d{4,}");
    }

    /**
     * Validates that an amount is positive.
     */
    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }

    /**
     * Validates email format (basic check).
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    /**
     * Validates phone number (digits only, 10-15 chars).
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return phone.matches("\\d{10,15}");
    }

    /**
     * Validates that a name is not empty and contains only letters and spaces.
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.matches("[A-Za-z ]{2,50}");
    }

    /**
     * Sanitizes input by removing pipe characters (used as delimiter).
     */
    public static String sanitize(String input) {
        if (input == null) return "";
        return input.replace("|", "").trim();
    }
}
