package com.smartbank.exception;

/**
 * Custom exception thrown when account balance is insufficient for a transaction.
 * Demonstrates: Custom Exception Handling.
 */
public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
