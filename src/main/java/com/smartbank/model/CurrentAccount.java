package com.smartbank.model;

/**
 * Current Account - extends Account.
 * Demonstrates: Inheritance, Method Overriding, Polymorphism.
 * Has overdraft limit feature.
 */
public class CurrentAccount extends Account {
    private static final long serialVersionUID = 1L;
    public static final double DEFAULT_OVERDRAFT_LIMIT = 10000.0;

    private double overdraftLimit;

    public CurrentAccount() {
        super();
        this.overdraftLimit = DEFAULT_OVERDRAFT_LIMIT;
    }

    public CurrentAccount(String accountNumber, String holderName, double balance,
                          String email, String phone) {
        super(accountNumber, holderName, balance, email, phone);
        this.overdraftLimit = DEFAULT_OVERDRAFT_LIMIT;
    }

    public CurrentAccount(String accountNumber, String holderName, double balance,
                          String email, String phone, double overdraftLimit) {
        super(accountNumber, holderName, balance, email, phone);
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() { return overdraftLimit; }
    public void setOverdraftLimit(double overdraftLimit) { this.overdraftLimit = overdraftLimit; }

    @Override
    public AccountType getAccountType() {
        return AccountType.CURRENT;
    }

    @Override
    public String getAccountDetails() {
        return toString() + String.format(
            "%nOverdraft Limit: %.2f", overdraftLimit);
    }

    @Override
    public String getExtraFieldValue() {
        return String.valueOf(overdraftLimit);
    }
}
