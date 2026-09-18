package com.smartbank.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a banking transaction.
 * Demonstrates: Encapsulation, Serializable.
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String transactionId;
    private String accountNumber;
    private TransactionType type;
    private double amount;
    private double balanceAfter;
    private LocalDateTime timestamp;
    private String description;

    public Transaction() {
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(String transactionId, String accountNumber, TransactionType type,
                       double amount, double balanceAfter, String description) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
        this.description = description;
    }

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(double balanceAfter) { this.balanceAfter = balanceAfter; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    /**
     * Converts the transaction to a pipe-delimited string for file storage.
     */
    public String toCsv() {
        return String.join("|",
            transactionId,
            accountNumber,
            type.name(),
            String.valueOf(amount),
            String.valueOf(balanceAfter),
            timestamp.format(FORMATTER),
            description
        );
    }

    /**
     * Factory method to create a Transaction from a pipe-delimited string.
     */
    public static Transaction fromCsv(String csvLine) {
        String[] parts = csvLine.split("\\|", -1);
        if (parts.length < 7) return null;

        Transaction t = new Transaction();
        t.setTransactionId(parts[0].trim());
        t.setAccountNumber(parts[1].trim());
        t.setType(TransactionType.valueOf(parts[2].trim()));
        t.setAmount(Double.parseDouble(parts[3].trim()));
        t.setBalanceAfter(Double.parseDouble(parts[4].trim()));
        t.setTimestamp(LocalDateTime.parse(parts[5].trim(), FORMATTER));
        t.setDescription(parts[6].trim());
        return t;
    }

    @Override
    public String toString() {
        return String.format(
            "%-12s | %-10s | %-15s | %10.2f | %10.2f | %s | %s",
            transactionId, accountNumber, type.getDisplayName(),
            amount, balanceAfter, timestamp.format(FORMATTER), description
        );
    }
}
