package com.smartbank.util;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for file operations.
 * Demonstrates: File Handling, Exception Handling.
 */
public final class FileUtil {

    private FileUtil() {
        // Prevent instantiation
    }

    /**
     * Ensures the specified directory exists, creates it if not.
     */
    public static void ensureDirectoryExists(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Ensures the specified file exists, creates it and parent dirs if not.
     */
    public static void ensureFileExists(String filePath) {
        try {
            File file = new File(filePath);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error creating file: " + filePath + " - " + e.getMessage());
        }
    }

    /**
     * Reads all lines from a file.
     */
    public static List<String> readAllLines(String filePath) {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return lines;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
        }
        return lines;
    }

    /**
     * Writes all lines to a file, replacing existing content.
     */
    public static void writeAllLines(String filePath, List<String> lines) {
        ensureFileExists(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing file: " + filePath + " - " + e.getMessage());
        }
    }

    /**
     * Appends a single line to a file.
     */
    public static void appendLine(String filePath, String line) {
        ensureFileExists(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error appending to file: " + filePath + " - " + e.getMessage());
        }
    }

    /**
     * Checks if a file exists.
     */
    public static boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }
}
