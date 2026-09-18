package com.smartbank.interfaces;

import com.smartbank.exception.AccountNotFoundException;
import com.smartbank.model.Transaction;
import java.util.List;

/**
 * Interface for reporting operations.
 * Demonstrates: Interface usage, Abstraction.
 */
public interface Reportable {

    /**
     * Generate a comprehensive summary for an account.
     */
    String generateAccountSummary(String accountNumber) throws AccountNotFoundException;

    /**
     * Get deposit history for an account.
     */
    List<Transaction> getDepositHistory(String accountNumber) throws AccountNotFoundException;

    /**
     * Get withdrawal history for an account.
     */
    List<Transaction> getWithdrawalHistory(String accountNumber) throws AccountNotFoundException;

    /**
     * Get all transactions across all accounts.
     */
    List<Transaction> getAllTransactions();
}
