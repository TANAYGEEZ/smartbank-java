package com.smartbank.exception;

/**
 * Custom exception thrown when an account is not found.
 * Demonstrates: Custom Exception Handling.
 */
public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
