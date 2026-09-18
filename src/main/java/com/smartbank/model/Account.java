package com.smartbank.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract base class for all bank accounts.
 * Demonstrates: Abstraction, Encapsulation, Inheritance (base class),
 * Serializable interface, and method overriding.
 */
public abstract class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String accountNumber;
    private String holderName;
    private double balance;
    private String email;
    private String phone;
    private LocalDateTime createdDate;

    public Account() {
        this.createdDate = LocalDateTime.now();
    }

    public Account(String accountNumber, String holderName, double balance,
                   String email, String phone) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
        this.email = email;
        this.phone = phone;
        this.createdDate = LocalDateTime.now();
    }

    // Abstract methods - subclasses must implement
    public abstract AccountType getAccountType();
    public abstract String getAccountDetails();
    public abstract String getExtraFieldValue();

    // Getters and Setters (Encapsulation)
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    /**
     * Converts the account to a pipe-delimited string for file storage.
     */
    public String toCsv() {
        return String.join("|",
            accountNumber,
            holderName,
            String.valueOf(balance),
            getAccountType().name(),
            email,
            phone,
            createdDate.format(FORMATTER),
            getExtraFieldValue()
        );
    }

    /**
     * Factory method to create an Account from a pipe-delimited string.
     * Demonstrates polymorphism - returns either SavingsAccount or CurrentAccount.
     */
    public static Account fromCsv(String csvLine) {
        String[] parts = csvLine.split("\\|", -1);
        if (parts.length < 8) return null;

        String accNum = parts[0].trim();
        String name = parts[1].trim();
        double bal = Double.parseDouble(parts[2].trim());
        String type = parts[3].trim();
        String eml = parts[4].trim();
        String phn = parts[5].trim();
        LocalDateTime created = LocalDateTime.parse(parts[6].trim(), FORMATTER);
        String extra = parts[7].trim();

        Account account;
        if (type.equals(AccountType.SAVINGS.name())) {
            SavingsAccount sa = new SavingsAccount(accNum, name, bal, eml, phn);
            sa.setInterestRate(Double.parseDouble(extra));
            account = sa;
        } else {
            CurrentAccount ca = new CurrentAccount(accNum, name, bal, eml, phn);
            ca.setOverdraftLimit(Double.parseDouble(extra));
            account = ca;
        }
        account.setCreatedDate(created);
        return account;
    }

    @Override
    public String toString() {
        return String.format(
            "Account Number : %s%nHolder Name    : %s%nBalance        : %.2f%nAccount Type   : %s%nEmail          : %s%nPhone          : %s%nCreated Date   : %s",
            accountNumber, holderName, balance, getAccountType().getDisplayName(),
            email, phone, createdDate.format(FORMATTER)
        );
    }
}
