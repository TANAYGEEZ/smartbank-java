# SmartBank - Banking Management System

## Overview

SmartBank is a console-based Banking Management System built in Java as a university course project for **Programming in Java**. It demonstrates core Object-Oriented Programming (OOP) concepts through a practical, real-world banking application.

The system allows users to manage bank accounts, perform transactions (deposit, withdraw, transfer), and generate reports - all through an intuitive command-line interface with persistent local file storage.

## Features

### Module 1: Account Management
- Create bank accounts (Savings or Current)
- View detailed account information
- Update account holder details
- Delete accounts
- List all accounts

### Module 2: Banking Transactions
- Deposit money into accounts
- Withdraw money with balance validation
- Transfer money between accounts
- Check account balance

### Module 3: Transaction & Reporting
- View transaction history for any account
- Generate comprehensive account summaries
- Track deposits and withdrawals
- View all system transactions

### Validation & Error Handling
- Prevents duplicate account numbers
- Rejects negative or zero transaction amounts
- Prevents withdrawal when balance is insufficient
- Maintains minimum balance for savings accounts
- Handles invalid menu and numeric inputs
- Displays meaningful error messages via custom exceptions

## Technologies Used

| Technology | Version | Purpose |
|---|---|---|
| Java | 17+ | Core programming language |
| Maven | 3.8+ | Build automation and project management |
| File I/O | - | Local data persistence |
| Collections | - | In-memory data management |

## Requirements

- **Java Development Kit (JDK)** 17 or later
- **Apache Maven** 3.8 or later
- **Operating System**: Windows, macOS, or Linux

## Installation & Setup

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd SmartBank
   ```

2. **Verify Java installation**:
   ```bash
   java -version
   ```
   Ensure it shows Java 17 or later.

3. **Verify Maven installation**:
   ```bash
   mvn -version
   ```

## How to Compile

```bash
mvn clean compile
```

This compiles all Java source files and places the compiled classes in the `target/` directory.

## How to Run

```bash
mvn exec:java
```

Or, after building:

```bash
mvn clean package -q
java -jar target/smartbank-1.0-SNAPSHOT.jar
```

## How to Test

Run the built-in test suite (51 tests):

```bash
mvn clean compile
javac -d target/test-classes -cp target/classes src/test/java/com/smartbank/SmartBankTest.java
java -cp "target/classes;target/test-classes" com.smartbank.SmartBankTest
```

On macOS/Linux, use `:` instead of `;` in the classpath:

```bash
java -cp "target/classes:target/test-classes" com.smartbank.SmartBankTest
```

This runs comprehensive tests covering:
- Input validation
- Account CRUD operations
- Transaction processing
- Exception handling
- Polymorphism verification

## Project Structure

```
SmartBank/
├── pom.xml                          # Maven configuration
├── README.md                        # Project documentation
├── statement.md                     # Problem statement
├── .gitignore                       # Git ignore rules
├── docs/                            # Documentation
│   ├── SystemArchitecture.md
│   ├── Workflow.md
│   ├── UseCaseDiagram.md
│   ├── ClassDiagram.md
│   └── SequenceDiagram.md
├── data/                            # Runtime data (auto-created)
│   ├── accounts.dat
│   └── transactions.dat
└── src/
    ├── main/java/com/smartbank/
    │   ├── Main.java                # Entry point
    │   ├── model/                   # Data models
    │   │   ├── Account.java         # Abstract base class
    │   │   ├── SavingsAccount.java  # Savings account
    │   │   ├── CurrentAccount.java  # Current account
    │   │   ├── Transaction.java     # Transaction record
    │   │   ├── AccountType.java     # Account type enum
    │   │   └── TransactionType.java # Transaction type enum
    │   ├── interfaces/              # Interfaces
    │   │   ├── TransactionOperations.java
    │   │   └── Reportable.java
    │   ├── exception/               # Custom exceptions
    │   │   ├── AccountNotFoundException.java
    │   │   ├── InsufficientBalanceException.java
    │   │   ├── InvalidAmountException.java
    │   │   └── DuplicateAccountException.java
    │   ├── repository/              # Data access layer
    │   │   ├── AccountRepository.java
    │   │   └── TransactionRepository.java
    │   ├── service/                 # Business logic layer
    │   │   ├── AccountService.java
    │   │   ├── TransactionService.java
    │   │   └── ReportService.java
    │   ├── ui/                      # User interface
    │   │   └── ConsoleUI.java
    │   └── util/                    # Utilities
    │       ├── Constants.java
    │       ├── FileUtil.java
    │       └── InputValidator.java
    └── test/java/com/smartbank/
        └── SmartBankTest.java        # Test suite
```

## OOP Concepts Demonstrated

| Concept | Where Used |
|---|---|
| **Classes and Objects** | All model, service, repository classes |
| **Encapsulation** | Private fields with getters/setters in Account, Transaction |
| **Inheritance** | SavingsAccount and CurrentAccount extend Account |
| **Abstraction** | Account is an abstract class with abstract methods |
| **Interfaces** | TransactionOperations, Reportable |
| **Polymorphism** | getAccountDetails() behaves differently for Savings vs Current |
| **Method Overriding** | getAccountType(), getAccountDetails(), getExtraFieldValue() |
| **Exception Handling** | Custom exceptions: AccountNotFound, InsufficientBalance, etc. |
| **Collections** | ArrayList, List, Stream API for data management |
| **File Handling** | FileUtil, AccountRepository, TransactionRepository |
| **Packages** | model, service, repository, exception, interfaces, ui, util |
| **Enums** | AccountType, TransactionType |
| **Static Methods** | Account.fromCsv(), factory methods |
| **Pattern Matching** | instanceof with pattern variables in TransactionService |

## Sample Usage

```
+============================================+
|     SMARTBANK - Banking Management System  |
|            Welcome to SmartBank!           |
+============================================+

============================================
              SMARTBANK SYSTEM
============================================
  1.  Create Account
  2.  View Account
  ...
  0.  Exit
============================================
  Enter your choice: 1

--- Create New Account ---
  Enter holder name: John Doe
  Account Type:
    1. Savings Account
    2. Current Account
  Select type (1-2): 1
  Enter email: john@example.com
  Enter phone (10-15 digits): 1234567890
  Enter initial deposit amount: 5000
  Enter interest rate (press Enter for default 3.5%): 

  [+] Account created successfully!
  Account Number: ACC1000
```

## Future Enhancements

- User authentication and login system
- Automated interest calculation
- Loan management module
- Multi-currency support
- GUI using JavaFX
- Database integration (MySQL/PostgreSQL)
- PDF report generation
- Email notifications
- Account statement export (CSV/PDF)
- Role-based access control (Admin/Teller/Customer)
