# SmartBank - Problem Statement

## 1. Problem Statement

Managing banking operations manually is prone to errors, time-consuming, and lacks proper record-keeping. There is a need for a simple, reliable, and efficient banking management system that can handle core banking operations such as account management, transactions, and reporting without requiring complex infrastructure.

The SmartBank Banking Management System aims to solve this problem by providing a console-based application that manages bank accounts, processes transactions, and generates reports - all through a simple command-line interface with local file-based data persistence.

## 2. Project Scope

The project covers the following functional areas:

### In Scope
- **Account Management**: Create, view, update, delete, and list bank accounts (Savings and Current)
- **Banking Transactions**: Deposit, withdraw, and transfer money between accounts
- **Reporting**: Transaction history, account summaries, deposit/withdrawal reports
- **Data Persistence**: Local file-based storage that persists data across application restarts
- **Input Validation**: Comprehensive validation of all user inputs
- **Error Handling**: Meaningful error messages using custom exceptions

### Out of Scope
- Graphical User Interface (GUI)
- Network/Internet connectivity
- External database systems
- User authentication/login system
- Interest calculation automation
- Multi-currency support

## 3. Target Users

- **University Students**: Learning Java OOP concepts through a practical project
- **Instructors**: Evaluating student understanding of core Java programming concepts
- **Beginners**: Understanding how to build a layered Java application

## 4. High-Level Features

| Module | Features |
|--------|----------|
| Account Management | Create, View, Update, Delete, List accounts |
| Banking Transactions | Deposit, Withdraw, Transfer, Check Balance |
| Reporting | Transaction History, Account Summary, Deposit/Withdrawal reports |
| Data Storage | File-based persistence using pipe-delimited data files |
| Validation | Input validation, duplicate prevention, balance checks |
| Error Handling | Custom exceptions with meaningful messages |

## 5. Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Core programming language |
| Maven | Build and dependency management |
| File I/O | Data persistence |
| Collections Framework | In-memory data management |
