package com.smartbank.interfaces;

import com.smartbank.exception.AccountNotFoundException;
import com.smartbank.exception.InsufficientBalanceException;
import com.smartbank.exception.InvalidAmountException;

/**
 * Interface defining banking transaction operations.
 * Demonstrates: Interface usage, Abstraction.
 */
public interface TransactionOperations {

    /**
     * Deposit money into an account.
     */
    void deposit(String accountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException;

    /**
     * Withdraw money from an account.
     */
    void withdraw(String accountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException, InsufficientBalanceException;

    /**
     * Transfer money between two accounts.
     */
    void transfer(String fromAccountNumber, String toAccountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException, InsufficientBalanceException;

    /**
     * Get the current balance of an account.
     */
    double getBalance(String accountNumber) throws AccountNotFoundException;
}
