package com.smartbank.exception;

/**
 * Custom exception thrown when trying to create an account with a duplicate number.
 * Demonstrates: Custom Exception Handling.
 */
public class DuplicateAccountException extends Exception {
    public DuplicateAccountException(String message) {
        super(message);
    }
}
