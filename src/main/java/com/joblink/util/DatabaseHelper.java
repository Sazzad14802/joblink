package com.joblink.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:joblink.db";
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
                // Enable foreign keys for SQLite
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON");
                }
                initializeTables();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Database connection error: " + e.getMessage());
        }
        return connection;
    }

    private static void initializeTables() {
        String createUsersTable = """
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        email TEXT UNIQUE NOT NULL,
                        password TEXT NOT NULL,
                        account_type TEXT NOT NULL DEFAULT 'SEEKER' CHECK(account_type IN ('SEEKER', 'EMPLOYER', 'ADMIN'))
                    )
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUsersTable);
            initializeAdminAccount(stmt);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initializeAdminAccount(Statement stmt) {
        try {
            // Check if admin account already exists
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE email = 'admin'");
            if (rs.next() && rs.getInt(1) == 0) {
                // Create predefined admin account
                // Email: admin
                // Password: admin
                stmt.execute(
                        "INSERT INTO users (name, email, password, account_type) VALUES ('Administrator', 'admin', 'admin', 'ADMIN')");
                System.out.println("Initialized predefined admin account (email: admin, password: admin)");
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Admin initialization note: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
