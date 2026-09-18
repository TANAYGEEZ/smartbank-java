package com.smartbank.model;

/**
 * Enum representing the types of bank accounts.
 * Demonstrates the use of Java enums with fields and methods.
 */
public enum AccountType {
    SAVINGS("Savings Account"),
    CURRENT("Current Account");

    private final String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
