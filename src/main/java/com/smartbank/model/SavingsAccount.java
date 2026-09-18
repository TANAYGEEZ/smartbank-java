package com.smartbank.model;

/**
 * Savings Account - extends Account.
 * Demonstrates: Inheritance, Method Overriding, Polymorphism.
 * Has interest rate and minimum balance requirement.
 */
public class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;
    public static final double MIN_BALANCE = 500.0;
    public static final double DEFAULT_INTEREST_RATE = 3.5;

    private double interestRate;

    public SavingsAccount() {
        super();
        this.interestRate = DEFAULT_INTEREST_RATE;
    }

    public SavingsAccount(String accountNumber, String holderName, double balance,
                          String email, String phone) {
        super(accountNumber, holderName, balance, email, phone);
        this.interestRate = DEFAULT_INTEREST_RATE;
    }

    public SavingsAccount(String accountNumber, String holderName, double balance,
                          String email, String phone, double interestRate) {
        super(accountNumber, holderName, balance, email, phone);
        this.interestRate = interestRate;
    }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    @Override
    public AccountType getAccountType() {
        return AccountType.SAVINGS;
    }

    @Override
    public String getAccountDetails() {
        return toString() + String.format(
            "%nInterest Rate  : %.2f%%%nMin Balance    : %.2f",
            interestRate, MIN_BALANCE);
    }

    @Override
    public String getExtraFieldValue() {
        return String.valueOf(interestRate);
    }
}
