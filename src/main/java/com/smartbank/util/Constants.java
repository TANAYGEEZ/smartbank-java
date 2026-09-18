package com.smartbank.util;

/**
 * Application constants.
 * Centralizes all constant values used throughout the application.
 */
public final class Constants {

    private Constants() {
        // Prevent instantiation
    }

    public static final String DATA_DIR = "data";
    public static final String ACCOUNTS_FILE = "data/accounts.dat";
    public static final String TRANSACTIONS_FILE = "data/transactions.dat";
    public static final String DELIMITER = "\\|";
    public static final String SEPARATOR = "|";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String ACCOUNT_PREFIX = "ACC";
    public static final String TRANSACTION_PREFIX = "TXN";
    public static final int ACCOUNT_NUMBER_LENGTH = 4;
}
