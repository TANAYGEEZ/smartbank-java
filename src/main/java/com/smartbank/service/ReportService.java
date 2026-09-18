package com.smartbank.service;

import com.smartbank.exception.AccountNotFoundException;
import com.smartbank.interfaces.Reportable;
import com.smartbank.model.Account;
import com.smartbank.model.Transaction;
import com.smartbank.model.TransactionType;
import com.smartbank.repository.AccountRepository;
import com.smartbank.repository.TransactionRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for generating reports.
 * Implements Reportable interface.
 * Demonstrates: Interface implementation, Streams, Collections.
 */
public class ReportService implements Reportable {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public ReportService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Generates a comprehensive account summary.
     */
    @Override
    public String generateAccountSummary(String accountNumber) throws AccountNotFoundException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        List<Transaction> transactions = transactionRepository.findByAccountNumber(accountNumber);

        double totalDeposits = transactions.stream()
                .filter(t -> t.getType() == TransactionType.DEPOSIT || t.getType() == TransactionType.TRANSFER_IN)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalWithdrawals = transactions.stream()
                .filter(t -> t.getType() == TransactionType.WITHDRAWAL || t.getType() == TransactionType.TRANSFER_OUT)
                .mapToDouble(Transaction::getAmount)
                .sum();

        long totalTransactions = transactions.size();

        StringBuilder sb = new StringBuilder();
        sb.append("\n+==============================================+\n");
        sb.append("|          ACCOUNT SUMMARY REPORT              |\n");
        sb.append("+==============================================+\n");
        sb.append(String.format("| %-44s |%n", ""));
        sb.append(account.getAccountDetails()).append("\n");
        sb.append(String.format("%n--- Transaction Summary ---%n"));
        sb.append(String.format("Total Transactions : %d%n", totalTransactions));
        sb.append(String.format("Total Deposits     : %.2f%n", totalDeposits));
        sb.append(String.format("Total Withdrawals  : %.2f%n", totalWithdrawals));
        sb.append(String.format("Net Flow           : %.2f%n", totalDeposits - totalWithdrawals));
        sb.append(String.format("Current Balance    : %.2f%n", account.getBalance()));
        sb.append("+==============================================+");

        return sb.toString();
    }

    /**
     * Gets deposit history for an account.
     */
    @Override
    public List<Transaction> getDepositHistory(String accountNumber)
            throws AccountNotFoundException {
        // Verify account exists
        accountRepository.findByAccountNumber(accountNumber);
        return transactionRepository.findByAccountNumber(accountNumber).stream()
                .filter(t -> t.getType() == TransactionType.DEPOSIT || t.getType() == TransactionType.TRANSFER_IN)
                .collect(Collectors.toList());
    }

    /**
     * Gets withdrawal history for an account.
     */
    @Override
    public List<Transaction> getWithdrawalHistory(String accountNumber)
            throws AccountNotFoundException {
        // Verify account exists
        accountRepository.findByAccountNumber(accountNumber);
        return transactionRepository.findByAccountNumber(accountNumber).stream()
                .filter(t -> t.getType() == TransactionType.WITHDRAWAL || t.getType() == TransactionType.TRANSFER_OUT)
                .collect(Collectors.toList());
    }

    /**
     * Gets all transactions across all accounts.
     */
    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
