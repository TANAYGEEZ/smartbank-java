package com.smartbank.service;

import com.smartbank.exception.AccountNotFoundException;
import com.smartbank.exception.InsufficientBalanceException;
import com.smartbank.exception.InvalidAmountException;
import com.smartbank.interfaces.TransactionOperations;
import com.smartbank.model.*;
import com.smartbank.repository.AccountRepository;
import com.smartbank.repository.TransactionRepository;

import java.util.List;

/**
 * Service class for banking transactions.
 * Implements TransactionOperations interface.
 * Demonstrates: Interface implementation, Polymorphism, Exception Handling.
 */
public class TransactionService implements TransactionOperations {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository,
                               TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Deposits money into an account.
     */
    @Override
    public void deposit(String accountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than zero.");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber);
        account.setBalance(account.getBalance() + amount);
        accountRepository.update(account);

        // Record transaction
        String txnId = transactionRepository.generateTransactionId();
        Transaction transaction = new Transaction(
                txnId, accountNumber, TransactionType.DEPOSIT,
                amount, account.getBalance(),
                "Deposit of " + String.format("%.2f", amount)
        );
        transactionRepository.save(transaction);
    }

    /**
     * Withdraws money from an account.
     */
    @Override
    public void withdraw(String accountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException, InsufficientBalanceException {

        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero.");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber);

        // Check balance based on account type (Polymorphism)
        double availableBalance = account.getBalance();
        if (account instanceof CurrentAccount currentAccount) {
            availableBalance += currentAccount.getOverdraftLimit();
        }

        if (amount > availableBalance) {
            throw new InsufficientBalanceException(
                String.format("Insufficient balance. Available: %.2f, Requested: %.2f",
                    availableBalance, amount));
        }

        // For savings accounts, check minimum balance
        if (account instanceof SavingsAccount) {
            double newBalance = account.getBalance() - amount;
            if (newBalance < SavingsAccount.MIN_BALANCE) {
                throw new InsufficientBalanceException(
                    String.format("Cannot withdraw. Savings account must maintain minimum balance of %.2f. " +
                        "Current balance: %.2f, Max withdrawal: %.2f",
                        SavingsAccount.MIN_BALANCE, account.getBalance(),
                        account.getBalance() - SavingsAccount.MIN_BALANCE));
            }
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.update(account);

        // Record transaction
        String txnId = transactionRepository.generateTransactionId();
        Transaction transaction = new Transaction(
                txnId, accountNumber, TransactionType.WITHDRAWAL,
                amount, account.getBalance(),
                "Withdrawal of " + String.format("%.2f", amount)
        );
        transactionRepository.save(transaction);
    }

    /**
     * Transfers money between two accounts.
     */
    @Override
    public void transfer(String fromAccountNumber, String toAccountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException, InsufficientBalanceException {

        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be greater than zero.");
        }

        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new InvalidAmountException("Cannot transfer to the same account.");
        }

        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber);
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber);

        // Check balance
        double availableBalance = fromAccount.getBalance();
        if (fromAccount instanceof CurrentAccount currentAccount) {
            availableBalance += currentAccount.getOverdraftLimit();
        }

        if (amount > availableBalance) {
            throw new InsufficientBalanceException(
                String.format("Insufficient balance for transfer. Available: %.2f, Requested: %.2f",
                    availableBalance, amount));
        }

        // For savings accounts, check minimum balance
        if (fromAccount instanceof SavingsAccount) {
            double newBalance = fromAccount.getBalance() - amount;
            if (newBalance < SavingsAccount.MIN_BALANCE) {
                throw new InsufficientBalanceException(
                    String.format("Cannot transfer. Savings account must maintain minimum balance of %.2f.",
                        SavingsAccount.MIN_BALANCE));
            }
        }

        // Perform transfer
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.update(fromAccount);
        accountRepository.update(toAccount);

        // Record transactions for both accounts
        String txnId1 = transactionRepository.generateTransactionId();
        Transaction txnOut = new Transaction(
                txnId1, fromAccountNumber, TransactionType.TRANSFER_OUT,
                amount, fromAccount.getBalance(),
                "Transfer to " + toAccountNumber
        );
        transactionRepository.save(txnOut);

        String txnId2 = transactionRepository.generateTransactionId();
        Transaction txnIn = new Transaction(
                txnId2, toAccountNumber, TransactionType.TRANSFER_IN,
                amount, toAccount.getBalance(),
                "Transfer from " + fromAccountNumber
        );
        transactionRepository.save(txnIn);
    }

    /**
     * Gets the current balance of an account.
     */
    @Override
    public double getBalance(String accountNumber) throws AccountNotFoundException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        return account.getBalance();
    }

    /**
     * Gets all transactions for an account.
     */
    public List<Transaction> getTransactionHistory(String accountNumber)
            throws AccountNotFoundException {
        // Verify account exists
        accountRepository.findByAccountNumber(accountNumber);
        return transactionRepository.findByAccountNumber(accountNumber);
    }
}
