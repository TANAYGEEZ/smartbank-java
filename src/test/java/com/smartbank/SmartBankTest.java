package com.smartbank;

import com.smartbank.exception.*;
import com.smartbank.model.*;
import com.smartbank.repository.AccountRepository;
import com.smartbank.repository.TransactionRepository;
import com.smartbank.service.AccountService;
import com.smartbank.service.ReportService;
import com.smartbank.service.TransactionService;
import com.smartbank.util.InputValidator;

import java.io.File;
import java.util.List;

/**
 * Test class for SmartBank application.
 * Performs comprehensive validation of all modules.
 * Can be run from command line without JUnit.
 */
public class SmartBankTest {

    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;

    private static AccountRepository accountRepository;
    private static TransactionRepository transactionRepository;
    private static AccountService accountService;
    private static TransactionService transactionService;
    private static ReportService reportService;

    public static void main(String[] args) {
        System.out.println();
        System.out.println("+============================================+");
        System.out.println("|       SMARTBANK TEST SUITE                 |");
        System.out.println("+============================================+");
        System.out.println();

        // Clean up test data
        cleanTestData();

        // Initialize
        accountRepository = new AccountRepository();
        transactionRepository = new TransactionRepository();
        accountService = new AccountService(accountRepository);
        transactionService = new TransactionService(accountRepository, transactionRepository);
        reportService = new ReportService(accountRepository, transactionRepository);

        // Run tests
        testInputValidation();
        testAccountCreation();
        testDuplicateAccountPrevention();
        testViewAccount();
        testUpdateAccount();
        testDeposit();
        testWithdraw();
        testInsufficientBalance();
        testInvalidAmount();
        testTransfer();
        testTransactionHistory();
        testAccountSummary();
        testListAllAccounts();
        testDeleteAccount();
        testAccountNotFound();
        testPolymorphism();

        // Print summary
        printSummary();

        // Clean up
        cleanTestData();
    }

    // ==================== TEST METHODS ====================

    private static void testInputValidation() {
        printSection("Input Validation Tests");

        assertTest("Valid account number ACC1000",
                InputValidator.isValidAccountNumber("ACC1000"));
        assertTest("Invalid account number 'ABC'",
                !InputValidator.isValidAccountNumber("ABC"));
        assertTest("Invalid account number null",
                !InputValidator.isValidAccountNumber(null));
        assertTest("Valid email",
                InputValidator.isValidEmail("test@example.com"));
        assertTest("Invalid email",
                !InputValidator.isValidEmail("invalid"));
        assertTest("Valid phone",
                InputValidator.isValidPhone("1234567890"));
        assertTest("Invalid phone",
                !InputValidator.isValidPhone("123"));
        assertTest("Valid name",
                InputValidator.isValidName("John Doe"));
        assertTest("Invalid name (empty)",
                !InputValidator.isValidName(""));
        assertTest("Valid amount",
                InputValidator.isValidAmount(100));
        assertTest("Invalid amount (zero)",
                !InputValidator.isValidAmount(0));
        assertTest("Invalid amount (negative)",
                !InputValidator.isValidAmount(-50));
    }

    private static void testAccountCreation() {
        printSection("Account Creation Tests");

        try {
            Account savings = accountService.createAccount(
                    "John Doe", AccountType.SAVINGS, "john@test.com", "1234567890", 1000.0, 0);
            assertTest("Create savings account", savings != null);
            assertTest("Savings account number generated", savings.getAccountNumber().startsWith("ACC"));
            assertTest("Savings account type", savings.getAccountType() == AccountType.SAVINGS);
            assertTest("Savings account balance", savings.getBalance() == 1000.0);
            assertTest("Savings account is SavingsAccount instance", savings instanceof SavingsAccount);
        } catch (Exception e) {
            assertTest("Create savings account - EXCEPTION: " + e.getMessage(), false);
        }

        try {
            Account current = accountService.createAccount(
                    "Jane Smith", AccountType.CURRENT, "jane@test.com", "0987654321", 2000.0, 0);
            assertTest("Create current account", current != null);
            assertTest("Current account type", current.getAccountType() == AccountType.CURRENT);
            assertTest("Current account is CurrentAccount instance", current instanceof CurrentAccount);
        } catch (Exception e) {
            assertTest("Create current account - EXCEPTION: " + e.getMessage(), false);
        }
    }

    private static void testDuplicateAccountPrevention() {
        printSection("Duplicate Account Prevention");

        // Accounts already created in previous test, trying internal duplicate check
        assertTest("Account ACC1000 exists", accountRepository.exists("ACC1000"));
    }

    private static void testViewAccount() {
        printSection("View Account Tests");

        try {
            Account account = accountService.getAccount("ACC1000");
            assertTest("View account returns correct account", account != null);
            assertTest("Account holder name", "John Doe".equals(account.getHolderName()));
            assertTest("Account details not null", account.getAccountDetails() != null);
        } catch (AccountNotFoundException e) {
            assertTest("View account - EXCEPTION: " + e.getMessage(), false);
        }
    }

    private static void testUpdateAccount() {
        printSection("Update Account Tests");

        try {
            accountService.updateAccount("ACC1000", "John Updated", "john.updated@test.com", null);
            Account updated = accountService.getAccount("ACC1000");
            assertTest("Update account name", "John Updated".equals(updated.getHolderName()));
            assertTest("Update account email", "john.updated@test.com".equals(updated.getEmail()));
            assertTest("Phone unchanged", "1234567890".equals(updated.getPhone()));
        } catch (AccountNotFoundException e) {
            assertTest("Update account - EXCEPTION: " + e.getMessage(), false);
        }
    }

    private static void testDeposit() {
        printSection("Deposit Tests");

        try {
            transactionService.deposit("ACC1000", 500.0);
            double balance = transactionService.getBalance("ACC1000");
            assertTest("Deposit increases balance", balance == 1500.0);
        } catch (Exception e) {
            assertTest("Deposit - EXCEPTION: " + e.getMessage(), false);
        }
    }

    private static void testWithdraw() {
        printSection("Withdrawal Tests");

        try {
            transactionService.withdraw("ACC1000", 200.0);
            double balance = transactionService.getBalance("ACC1000");
            assertTest("Withdraw decreases balance", balance == 1300.0);
        } catch (Exception e) {
            assertTest("Withdraw - EXCEPTION: " + e.getMessage(), false);
        }
    }

    private static void testInsufficientBalance() {
        printSection("Insufficient Balance Tests");

        try {
            // ACC1000 has 1300, savings min is 500, so max withdrawal is 800
            transactionService.withdraw("ACC1000", 900.0);
            assertTest("Should have thrown InsufficientBalanceException", false);
        } catch (InsufficientBalanceException e) {
            assertTest("InsufficientBalanceException thrown correctly", true);
        } catch (Exception e) {
            assertTest("Wrong exception type: " + e.getClass().getSimpleName(), false);
        }
    }

    private static void testInvalidAmount() {
        printSection("Invalid Amount Tests");

        try {
            transactionService.deposit("ACC1000", -100);
            assertTest("Should have thrown InvalidAmountException", false);
        } catch (InvalidAmountException e) {
            assertTest("InvalidAmountException for negative deposit", true);
        } catch (Exception e) {
            assertTest("Wrong exception: " + e.getClass().getSimpleName(), false);
        }

        try {
            transactionService.withdraw("ACC1000", 0);
            assertTest("Should have thrown InvalidAmountException", false);
        } catch (InvalidAmountException e) {
            assertTest("InvalidAmountException for zero withdrawal", true);
        } catch (Exception e) {
            assertTest("Wrong exception: " + e.getClass().getSimpleName(), false);
        }
    }

    private static void testTransfer() {
        printSection("Transfer Tests");

        try {
            double fromBefore = transactionService.getBalance("ACC1000");
            double toBefore = transactionService.getBalance("ACC1001");
            transactionService.transfer("ACC1000", "ACC1001", 100.0);
            double fromAfter = transactionService.getBalance("ACC1000");
            double toAfter = transactionService.getBalance("ACC1001");

            assertTest("Transfer debits source", fromAfter == fromBefore - 100.0);
            assertTest("Transfer credits destination", toAfter == toBefore + 100.0);
        } catch (Exception e) {
            assertTest("Transfer - EXCEPTION: " + e.getMessage(), false);
        }
    }

    private static void testTransactionHistory() {
        printSection("Transaction History Tests");

        try {
            List<Transaction> history = transactionService.getTransactionHistory("ACC1000");
            assertTest("Transaction history not empty", !history.isEmpty());
            assertTest("Has deposit transaction",
                    history.stream().anyMatch(t -> t.getType() == TransactionType.DEPOSIT));
            assertTest("Has withdrawal transaction",
                    history.stream().anyMatch(t -> t.getType() == TransactionType.WITHDRAWAL));
            assertTest("Has transfer transaction",
                    history.stream().anyMatch(t -> t.getType() == TransactionType.TRANSFER_OUT));
        } catch (AccountNotFoundException e) {
            assertTest("Transaction history - EXCEPTION: " + e.getMessage(), false);
        }
    }

    private static void testAccountSummary() {
        printSection("Account Summary Tests");

        try {
            String summary = reportService.generateAccountSummary("ACC1000");
            assertTest("Summary generated", summary != null && !summary.isEmpty());
            assertTest("Summary contains account info", summary.contains("ACC1000"));
        } catch (AccountNotFoundException e) {
            assertTest("Account summary - EXCEPTION: " + e.getMessage(), false);
        }
    }

    private static void testListAllAccounts() {
        printSection("List All Accounts Tests");

        List<Account> accounts = accountService.getAllAccounts();
        assertTest("Accounts list not empty", !accounts.isEmpty());
        assertTest("At least 2 accounts", accounts.size() >= 2);
    }

    private static void testDeleteAccount() {
        printSection("Delete Account Tests");

        try {
            // Create a temporary account to delete
            Account temp = accountService.createAccount(
                    "Temp User", AccountType.SAVINGS, "temp@test.com", "5555555555", 1000.0, 0);
            String tempAccNum = temp.getAccountNumber();
            assertTest("Temp account created", accountRepository.exists(tempAccNum));

            accountService.deleteAccount(tempAccNum);
            assertTest("Account deleted successfully", !accountRepository.exists(tempAccNum));
        } catch (Exception e) {
            assertTest("Delete account - EXCEPTION: " + e.getMessage(), false);
        }
    }

    private static void testAccountNotFound() {
        printSection("Account Not Found Tests");

        try {
            accountService.getAccount("ACC9999");
            assertTest("Should have thrown AccountNotFoundException", false);
        } catch (AccountNotFoundException e) {
            assertTest("AccountNotFoundException thrown correctly", true);
        }
    }

    private static void testPolymorphism() {
        printSection("Polymorphism Tests");

        try {
            Account savings = accountService.getAccount("ACC1000");
            Account current = accountService.getAccount("ACC1001");

            assertTest("Savings is instance of Account", savings instanceof Account);
            assertTest("Current is instance of Account", current instanceof Account);
            assertTest("Savings type is SAVINGS", savings.getAccountType() == AccountType.SAVINGS);
            assertTest("Current type is CURRENT", current.getAccountType() == AccountType.CURRENT);

            // Polymorphic method calls
            String savingsDetails = savings.getAccountDetails();
            String currentDetails = current.getAccountDetails();
            assertTest("Savings details contain interest", savingsDetails.contains("Interest"));
            assertTest("Current details contain overdraft", currentDetails.contains("Overdraft"));
        } catch (AccountNotFoundException e) {
            assertTest("Polymorphism test - EXCEPTION: " + e.getMessage(), false);
        }
    }

    // ==================== HELPER METHODS ====================

    private static void assertTest(String testName, boolean condition) {
        totalTests++;
        if (condition) {
            passedTests++;
            System.out.println("    [PASS] " + testName);
        } else {
            failedTests++;
            System.out.println("    [FAIL] " + testName);
        }
    }

    private static void printSection(String name) {
        System.out.println();
        System.out.println("  >> " + name);
    }

    private static void printSummary() {
        System.out.println();
        System.out.println("============================================");
        System.out.println("              TEST RESULTS                  ");
        System.out.println("============================================");
        System.out.printf("  Total Tests  : %d%n", totalTests);
        System.out.printf("  Passed       : %d%n", passedTests);
        System.out.printf("  Failed       : %d%n", failedTests);
        System.out.printf("  Success Rate : %.1f%%%n",
                totalTests > 0 ? (passedTests * 100.0 / totalTests) : 0);
        System.out.println("============================================");

        if (failedTests == 0) {
            System.out.println("  ALL TESTS PASSED!");
        } else {
            System.out.println("  SOME TESTS FAILED. Review output above.");
        }
        System.out.println();
    }

    private static void cleanTestData() {
        // Delete test data files
        new File("data/accounts.dat").delete();
        new File("data/transactions.dat").delete();
    }
}
