package com.smartbank.repository;

import com.smartbank.exception.AccountNotFoundException;
import com.smartbank.exception.DuplicateAccountException;
import com.smartbank.model.Account;
import com.smartbank.util.Constants;
import com.smartbank.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository class for Account persistence using file storage.
 * Demonstrates: File Handling, Collections, Exception Handling.
 */
public class AccountRepository {

    public AccountRepository() {
        FileUtil.ensureDirectoryExists(Constants.DATA_DIR);
        FileUtil.ensureFileExists(Constants.ACCOUNTS_FILE);
    }

    /**
     * Saves a new account to the file.
     * @throws DuplicateAccountException if account number already exists
     */
    public void save(Account account) throws DuplicateAccountException {
        if (exists(account.getAccountNumber())) {
            throw new DuplicateAccountException(
                "Account with number " + account.getAccountNumber() + " already exists.");
        }
        FileUtil.appendLine(Constants.ACCOUNTS_FILE, account.toCsv());
    }

    /**
     * Finds an account by its account number.
     * @throws AccountNotFoundException if account is not found
     */
    public Account findByAccountNumber(String accountNumber) throws AccountNotFoundException {
        List<Account> accounts = loadAllAccounts();
        return accounts.stream()
                .filter(a -> a.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException(
                    "Account not found: " + accountNumber));
    }

    /**
     * Returns all accounts.
     */
    public List<Account> findAll() {
        return loadAllAccounts();
    }

    /**
     * Updates an existing account.
     * @throws AccountNotFoundException if account is not found
     */
    public void update(Account updatedAccount) throws AccountNotFoundException {
        List<Account> accounts = loadAllAccounts();
        boolean found = false;
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountNumber().equals(updatedAccount.getAccountNumber())) {
                accounts.set(i, updatedAccount);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new AccountNotFoundException(
                "Account not found: " + updatedAccount.getAccountNumber());
        }
        saveAllAccounts(accounts);
    }

    /**
     * Deletes an account by account number.
     * @throws AccountNotFoundException if account is not found
     */
    public void delete(String accountNumber) throws AccountNotFoundException {
        List<Account> accounts = loadAllAccounts();
        boolean removed = accounts.removeIf(a -> a.getAccountNumber().equals(accountNumber));
        if (!removed) {
            throw new AccountNotFoundException(
                "Account not found: " + accountNumber);
        }
        saveAllAccounts(accounts);
    }

    /**
     * Checks if an account exists.
     */
    public boolean exists(String accountNumber) {
        return loadAllAccounts().stream()
                .anyMatch(a -> a.getAccountNumber().equals(accountNumber));
    }

    /**
     * Generates the next available account number.
     */
    public String generateAccountNumber() {
        List<Account> accounts = loadAllAccounts();
        int maxNum = 1000;
        for (Account account : accounts) {
            String numStr = account.getAccountNumber().replace(Constants.ACCOUNT_PREFIX, "");
            try {
                int num = Integer.parseInt(numStr);
                if (num >= maxNum) {
                    maxNum = num + 1;
                }
            } catch (NumberFormatException e) {
                // Skip invalid entries
            }
        }
        return Constants.ACCOUNT_PREFIX + String.format("%04d", maxNum);
    }

    /**
     * Loads all accounts from the data file.
     */
    private List<Account> loadAllAccounts() {
        List<String> lines = FileUtil.readAllLines(Constants.ACCOUNTS_FILE);
        List<Account> accounts = new ArrayList<>();
        for (String line : lines) {
            try {
                Account account = Account.fromCsv(line);
                if (account != null) {
                    accounts.add(account);
                }
            } catch (Exception e) {
                System.err.println("Warning: Skipping invalid account record: " + e.getMessage());
            }
        }
        return accounts;
    }

    /**
     * Saves all accounts to the data file (overwrites).
     */
    private void saveAllAccounts(List<Account> accounts) {
        List<String> lines = accounts.stream()
                .map(Account::toCsv)
                .collect(Collectors.toList());
        FileUtil.writeAllLines(Constants.ACCOUNTS_FILE, lines);
    }
}
