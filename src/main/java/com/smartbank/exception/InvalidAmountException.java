package com.smartbank.exception;

/**
 * Custom exception thrown when a transaction amount is invalid (negative or zero).
 * Demonstrates: Custom Exception Handling.
 */
public class InvalidAmountException extends Exception {
    public InvalidAmountException(String message) {
        super(message);
    }
}
