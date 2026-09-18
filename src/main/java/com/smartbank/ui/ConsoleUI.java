package com.smartbank.ui;

import com.smartbank.exception.*;
import com.smartbank.model.*;
import com.smartbank.repository.AccountRepository;
import com.smartbank.repository.TransactionRepository;
import com.smartbank.service.AccountService;
import com.smartbank.service.ReportService;
import com.smartbank.service.TransactionService;
import com.smartbank.util.InputValidator;

import java.util.List;
import java.util.Scanner;

/**
 * Console-based User Interface for SmartBank.
 * Handles all user interaction through the command line.
 * Demonstrates: Encapsulation, Exception Handling, Collections.
 */
public class ConsoleUI {

    private final Scanner scanner;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final ReportService reportService;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        AccountRepository accountRepository = new AccountRepository();
        TransactionRepository transactionRepository = new TransactionRepository();
        this.accountService = new AccountService(accountRepository);
        this.transactionService = new TransactionService(accountRepository, transactionRepository);
        this.reportService = new ReportService(accountRepository, transactionRepository);
    }

    /**
     * Starts the main application loop.
     */
    public void start() {
        printWelcome();
        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = readIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> handleCreateAccount();
                case 2 -> handleViewAccount();
                case 3 -> handleUpdateAccount();
                case 4 -> handleDeleteAccount();
                case 5 -> handleListAllAccounts();
                case 6 -> handleDeposit();
                case 7 -> handleWithdraw();
                case 8 -> handleTransfer();
                case 9 -> handleCheckBalance();
                case 10 -> handleTransactionHistory();
                case 11 -> handleAccountSummary();
                case 0 -> {
                    running = false;
                    printGoodbye();
                }
                default -> System.out.println("\n  [!] Invalid option. Please enter 0-11.");
            }
        }
        scanner.close();
    }

    // ==================== MENU DISPLAY ====================

    private void printWelcome() {
        System.out.println();
        System.out.println("+============================================+");
        System.out.println("|                                            |");
        System.out.println("|     SMARTBANK - Banking Management System  |");
        System.out.println("|            Welcome to SmartBank!           |");
        System.out.println("|                                            |");
        System.out.println("+============================================+");
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("============================================");
        System.out.println("              SMARTBANK SYSTEM              ");
        System.out.println("============================================");
        System.out.println("  1.  Create Account");
        System.out.println("  2.  View Account");
        System.out.println("  3.  Update Account");
        System.out.println("  4.  Delete Account");
        System.out.println("  5.  List All Accounts");
        System.out.println("  6.  Deposit Money");
        System.out.println("  7.  Withdraw Money");
        System.out.println("  8.  Transfer Money");
        System.out.println("  9.  Check Balance");
        System.out.println("  10. Transaction History");
        System.out.println("  11. Account Summary");
        System.out.println("  0.  Exit");
        System.out.println("============================================");
    }

    private void printGoodbye() {
        System.out.println();
        System.out.println("+============================================+");
        System.out.println("|   Thank you for using SmartBank!           |");
        System.out.println("|   Your data has been saved.                |");
        System.out.println("+============================================+");
        System.out.println();
    }

    private void printSectionHeader(String title) {
        System.out.println();
        System.out.println("--- " + title + " ---");
    }

    // ==================== ACCOUNT MANAGEMENT ====================

    private void handleCreateAccount() {
        printSectionHeader("Create New Account");

        String name = readStringInput("Enter holder name: ");
        if (!InputValidator.isValidName(name)) {
            System.out.println("  [!] Invalid name. Use only letters and spaces (2-50 chars).");
            return;
        }

        System.out.println("  Account Type:");
        System.out.println("    1. Savings Account");
        System.out.println("    2. Current Account");
        int typeChoice = readIntInput("  Select type (1-2): ");
        AccountType type;
        if (typeChoice == 1) {
            type = AccountType.SAVINGS;
        } else if (typeChoice == 2) {
            type = AccountType.CURRENT;
        } else {
            System.out.println("  [!] Invalid account type.");
            return;
        }

        String email = readStringInput("Enter email: ");
        if (!InputValidator.isValidEmail(email)) {
            System.out.println("  [!] Invalid email format.");
            return;
        }

        String phone = readStringInput("Enter phone (10-15 digits): ");
        if (!InputValidator.isValidPhone(phone)) {
            System.out.println("  [!] Invalid phone number. Must be 10-15 digits.");
            return;
        }

        double initialDeposit = readDoubleInput("Enter initial deposit amount: ");
        if (initialDeposit < 0) {
            System.out.println("  [!] Initial deposit cannot be negative.");
            return;
        }

        double extraParam = 0;
        if (type == AccountType.SAVINGS) {
            String input = readStringInput("Enter interest rate (press Enter for default " + SavingsAccount.DEFAULT_INTEREST_RATE + "%): ");
            if (!input.trim().isEmpty()) {
                try {
                    extraParam = Double.parseDouble(input);
                } catch (NumberFormatException e) {
                    System.out.println("  [!] Invalid rate. Using default.");
                }
            }
        } else {
            String input = readStringInput("Enter overdraft limit (press Enter for default " + CurrentAccount.DEFAULT_OVERDRAFT_LIMIT + "): ");
            if (!input.trim().isEmpty()) {
                try {
                    extraParam = Double.parseDouble(input);
                } catch (NumberFormatException e) {
                    System.out.println("  [!] Invalid limit. Using default.");
                }
            }
        }

        try {
            name = InputValidator.sanitize(name);
            Account account = accountService.createAccount(name, type, email, phone, initialDeposit, extraParam);
            System.out.println();
            System.out.println("  [+] Account created successfully!");
            System.out.println("  Account Number: " + account.getAccountNumber());
            System.out.println();
            System.out.println(account.getAccountDetails());
        } catch (DuplicateAccountException | InvalidAmountException e) {
            System.out.println("  [!] Error: " + e.getMessage());
        }
    }

    private void handleViewAccount() {
        printSectionHeader("View Account Details");
        String accNum = readStringInput("Enter account number (e.g., ACC1000): ");

        try {
            Account account = accountService.getAccount(accNum.toUpperCase());
            System.out.println();
            // Polymorphism - getAccountDetails() behaves differently for each type
            System.out.println(account.getAccountDetails());
        } catch (AccountNotFoundException e) {
            System.out.println("  [!] Error: " + e.getMessage());
        }
    }

    private void handleUpdateAccount() {
        printSectionHeader("Update Account");
        String accNum = readStringInput("Enter account number: ");

        try {
            Account account = accountService.getAccount(accNum.toUpperCase());
            System.out.println("  Current details:");
            System.out.println(account.getAccountDetails());
            System.out.println();
            System.out.println("  (Press Enter to keep current value)");

            String newName = readStringInput("  New holder name [" + account.getHolderName() + "]: ");
            String newEmail = readStringInput("  New email [" + account.getEmail() + "]: ");
            String newPhone = readStringInput("  New phone [" + account.getPhone() + "]: ");

            // Validate only if provided
            if (!newName.isEmpty() && !InputValidator.isValidName(newName)) {
                System.out.println("  [!] Invalid name format.");
                return;
            }
            if (!newEmail.isEmpty() && !InputValidator.isValidEmail(newEmail)) {
                System.out.println("  [!] Invalid email format.");
                return;
            }
            if (!newPhone.isEmpty() && !InputValidator.isValidPhone(newPhone)) {
                System.out.println("  [!] Invalid phone format.");
                return;
            }

            newName = newName.isEmpty() ? null : InputValidator.sanitize(newName);
            newEmail = newEmail.isEmpty() ? null : newEmail;
            newPhone = newPhone.isEmpty() ? null : newPhone;

            accountService.updateAccount(accNum.toUpperCase(), newName, newEmail, newPhone);
            System.out.println("  [+] Account updated successfully!");
        } catch (AccountNotFoundException e) {
            System.out.println("  [!] Error: " + e.getMessage());
        }
    }

    private void handleDeleteAccount() {
        printSectionHeader("Delete Account");
        String accNum = readStringInput("Enter account number: ");

        try {
            Account account = accountService.getAccount(accNum.toUpperCase());
            System.out.println("  Account: " + account.getHolderName() + " (" + accNum.toUpperCase() + ")");
            System.out.println("  Balance: " + String.format("%.2f", account.getBalance()));
            String confirm = readStringInput("  Are you sure you want to delete? (yes/no): ");

            if (confirm.equalsIgnoreCase("yes")) {
                accountService.deleteAccount(accNum.toUpperCase());
                System.out.println("  [+] Account deleted successfully!");
            } else {
                System.out.println("  [-] Deletion cancelled.");
            }
        } catch (AccountNotFoundException e) {
            System.out.println("  [!] Error: " + e.getMessage());
        }
    }

    private void handleListAllAccounts() {
        printSectionHeader("All Accounts");
        List<Account> accounts = accountService.getAllAccounts();

        if (accounts.isEmpty()) {
            System.out.println("  No accounts found.");
            return;
        }

        System.out.printf("  %-10s | %-20s | %-15s | %15s%n",
                "Acc No.", "Holder Name", "Type", "Balance");
        System.out.println("  " + "-".repeat(68));

        for (Account account : accounts) {
            System.out.printf("  %-10s | %-20s | %-15s | %15.2f%n",
                    account.getAccountNumber(),
                    account.getHolderName(),
                    account.getAccountType().getDisplayName(),
                    account.getBalance());
        }
        System.out.println("  " + "-".repeat(68));
        System.out.println("  Total accounts: " + accounts.size());
    }

    // ==================== TRANSACTIONS ====================

    private void handleDeposit() {
        printSectionHeader("Deposit Money");
        String accNum = readStringInput("Enter account number: ");
        double amount = readDoubleInput("Enter deposit amount: ");

        try {
            transactionService.deposit(accNum.toUpperCase(), amount);
            double newBalance = transactionService.getBalance(accNum.toUpperCase());
            System.out.printf("  [+] Successfully deposited %.2f%n", amount);
            System.out.printf("  New balance: %.2f%n", newBalance);
        } catch (AccountNotFoundException | InvalidAmountException e) {
            System.out.println("  [!] Error: " + e.getMessage());
        }
    }

    private void handleWithdraw() {
        printSectionHeader("Withdraw Money");
        String accNum = readStringInput("Enter account number: ");
        double amount = readDoubleInput("Enter withdrawal amount: ");

        try {
            transactionService.withdraw(accNum.toUpperCase(), amount);
            double newBalance = transactionService.getBalance(accNum.toUpperCase());
            System.out.printf("  [+] Successfully withdrew %.2f%n", amount);
            System.out.printf("  New balance: %.2f%n", newBalance);
        } catch (AccountNotFoundException | InvalidAmountException | InsufficientBalanceException e) {
            System.out.println("  [!] Error: " + e.getMessage());
        }
    }

    private void handleTransfer() {
        printSectionHeader("Transfer Money");
        String fromAcc = readStringInput("Enter source account number: ");
        String toAcc = readStringInput("Enter destination account number: ");
        double amount = readDoubleInput("Enter transfer amount: ");

        try {
            transactionService.transfer(fromAcc.toUpperCase(), toAcc.toUpperCase(), amount);
            System.out.printf("  [+] Successfully transferred %.2f from %s to %s%n",
                    amount, fromAcc.toUpperCase(), toAcc.toUpperCase());
            System.out.printf("  Source balance: %.2f%n",
                    transactionService.getBalance(fromAcc.toUpperCase()));
            System.out.printf("  Destination balance: %.2f%n",
                    transactionService.getBalance(toAcc.toUpperCase()));
        } catch (AccountNotFoundException | InvalidAmountException | InsufficientBalanceException e) {
            System.out.println("  [!] Error: " + e.getMessage());
        }
    }

    private void handleCheckBalance() {
        printSectionHeader("Check Balance");
        String accNum = readStringInput("Enter account number: ");

        try {
            double balance = transactionService.getBalance(accNum.toUpperCase());
            System.out.printf("  Account %s balance: %.2f%n", accNum.toUpperCase(), balance);
        } catch (AccountNotFoundException e) {
            System.out.println("  [!] Error: " + e.getMessage());
        }
    }

    // ==================== REPORTS ====================

    private void handleTransactionHistory() {
        printSectionHeader("Transaction History");
        String accNum = readStringInput("Enter account number: ");

        try {
            List<Transaction> transactions = transactionService.getTransactionHistory(accNum.toUpperCase());

            if (transactions.isEmpty()) {
                System.out.println("  No transactions found for account " + accNum.toUpperCase());
                return;
            }

            System.out.printf("  %-12s | %-10s | %-15s | %10s | %10s | %-19s | %s%n",
                    "Txn ID", "Account", "Type", "Amount", "Balance", "Date", "Description");
            System.out.println("  " + "-".repeat(100));

            for (Transaction t : transactions) {
                System.out.println("  " + t);
            }
            System.out.println("  " + "-".repeat(100));
            System.out.println("  Total transactions: " + transactions.size());
        } catch (AccountNotFoundException e) {
            System.out.println("  [!] Error: " + e.getMessage());
        }
    }

    private void handleAccountSummary() {
        printSectionHeader("Account Summary");
        String accNum = readStringInput("Enter account number: ");

        try {
            String summary = reportService.generateAccountSummary(accNum.toUpperCase());
            System.out.println(summary);
        } catch (AccountNotFoundException e) {
            System.out.println("  [!] Error: " + e.getMessage());
        }
    }

    // ==================== INPUT HELPERS ====================

    private String readStringInput(String prompt) {
        System.out.print("  " + prompt);
        return scanner.nextLine().trim();
    }

    private int readIntInput(String prompt) {
        System.out.print("  " + prompt);
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid input. Please enter a valid number.");
            return -1;
        }
    }

    private double readDoubleInput(String prompt) {
        System.out.print("  " + prompt);
        try {
            String input = scanner.nextLine().trim();
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid input. Please enter a valid number.");
            return -1;
        }
    }
}
