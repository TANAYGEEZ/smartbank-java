package com.smartbank.repository;

import com.smartbank.model.Transaction;
import com.smartbank.util.Constants;
import com.smartbank.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository class for Transaction persistence using file storage.
 * Demonstrates: File Handling, Collections.
 */
public class TransactionRepository {

    public TransactionRepository() {
        FileUtil.ensureDirectoryExists(Constants.DATA_DIR);
        FileUtil.ensureFileExists(Constants.TRANSACTIONS_FILE);
    }

    /**
     * Saves a new transaction to the file.
     */
    public void save(Transaction transaction) {
        FileUtil.appendLine(Constants.TRANSACTIONS_FILE, transaction.toCsv());
    }

    /**
     * Finds all transactions for a given account number.
     */
    public List<Transaction> findByAccountNumber(String accountNumber) {
        return loadAllTransactions().stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .collect(Collectors.toList());
    }

    /**
     * Returns all transactions.
     */
    public List<Transaction> findAll() {
        return loadAllTransactions();
    }

    /**
     * Generates the next available transaction ID.
     */
    public String generateTransactionId() {
        List<Transaction> transactions = loadAllTransactions();
        int maxNum = 1000;
        for (Transaction t : transactions) {
            String numStr = t.getTransactionId().replace(Constants.TRANSACTION_PREFIX, "");
            try {
                int num = Integer.parseInt(numStr);
                if (num >= maxNum) {
                    maxNum = num + 1;
                }
            } catch (NumberFormatException e) {
                // Skip invalid entries
            }
        }
        return Constants.TRANSACTION_PREFIX + String.format("%04d", maxNum);
    }

    /**
     * Loads all transactions from the data file.
     */
    private List<Transaction> loadAllTransactions() {
        List<String> lines = FileUtil.readAllLines(Constants.TRANSACTIONS_FILE);
        List<Transaction> transactions = new ArrayList<>();
        for (String line : lines) {
            try {
                Transaction t = Transaction.fromCsv(line);
                if (t != null) {
                    transactions.add(t);
                }
            } catch (Exception e) {
                System.err.println("Warning: Skipping invalid transaction record: " + e.getMessage());
            }
        }
        return transactions;
    }
}
