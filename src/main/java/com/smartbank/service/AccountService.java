package com.smartbank.service;

import com.smartbank.exception.AccountNotFoundException;
import com.smartbank.exception.DuplicateAccountException;
import com.smartbank.exception.InvalidAmountException;
import com.smartbank.model.*;
import com.smartbank.repository.AccountRepository;

import java.util.List;

/**
 * Service class for account management operations.
 * Demonstrates: Service Layer pattern, Exception Handling, Polymorphism.
 */
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Creates a new bank account.
     * Uses polymorphism to create either SavingsAccount or CurrentAccount.
     */
    public Account createAccount(String holderName, AccountType type, String email,
                                  String phone, double initialDeposit, double extraParam)
            throws DuplicateAccountException, InvalidAmountException {

        if (initialDeposit < 0) {
            throw new InvalidAmountException("Initial deposit cannot be negative.");
        }

        if (type == AccountType.SAVINGS && initialDeposit < SavingsAccount.MIN_BALANCE) {
            throw new InvalidAmountException(
                "Savings account requires a minimum initial deposit of " + SavingsAccount.MIN_BALANCE);
        }

        String accountNumber = accountRepository.generateAccountNumber();

        // Polymorphism - creating different account types
        Account account;
        if (type == AccountType.SAVINGS) {
            account = new SavingsAccount(accountNumber, holderName, initialDeposit,
                    email, phone, extraParam > 0 ? extraParam : SavingsAccount.DEFAULT_INTEREST_RATE);
        } else {
            account = new CurrentAccount(accountNumber, holderName, initialDeposit,
                    email, phone, extraParam > 0 ? extraParam : CurrentAccount.DEFAULT_OVERDRAFT_LIMIT);
        }

        accountRepository.save(account);
        return account;
    }

    /**
     * Retrieves an account by account number.
     */
    public Account getAccount(String accountNumber) throws AccountNotFoundException {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    /**
     * Returns all accounts.
     */
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Updates account holder details.
     */
    public void updateAccount(String accountNumber, String newName, String newEmail,
                               String newPhone) throws AccountNotFoundException {
        Account account = accountRepository.findByAccountNumber(accountNumber);

        if (newName != null && !newName.trim().isEmpty()) {
            account.setHolderName(newName);
        }
        if (newEmail != null && !newEmail.trim().isEmpty()) {
            account.setEmail(newEmail);
        }
        if (newPhone != null && !newPhone.trim().isEmpty()) {
            account.setPhone(newPhone);
        }

        accountRepository.update(account);
    }

    /**
     * Deletes an account.
     */
    public void deleteAccount(String accountNumber) throws AccountNotFoundException {
        accountRepository.delete(accountNumber);
    }
}
