package com.servletapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database utility class for managing database connections and operations
 */
public class DatabaseUtil {

    private static final Logger LOGGER = Logger.getLogger(DatabaseUtil.class.getName());

    // Database connection properties
    private static String DB_URL = "jdbc:mysql://localhost:3306/servletapp";
    private static String DB_USER = "root";
    private static String DB_PASSWORD = "password"; // Replace with your actual MySQL password

    // Connection pool could be implemented here in a production environment

    static {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            LOGGER.info("MySQL JDBC Driver Registered!");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found", e);
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    /**
     * Get a database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection failure", e);
            throw e;
        }
    }

    /**
     * Close database resources safely
     * @param connection Database connection
     * @param statement SQL statement
     * @param resultSet Result set
     */
    public static void closeResources(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error closing ResultSet", e);
        }

        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error closing Statement", e);
        }

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error closing Connection", e);
        }
    }

    /**
     * Initialize the database with required tables
     * @throws SQLException if initialization fails
     */
    public static void initializeDatabase() throws SQLException {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();

            // Create users table if it doesn't exist
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL UNIQUE," +
                    "password VARCHAR(100) NOT NULL," +
                    "email VARCHAR(100)," +
                    "full_name VARCHAR(100)," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            stmt.executeUpdate(createUsersTable);

            // Check if default admin user exists, if not create one
            String checkAdmin = "SELECT COUNT(*) FROM users WHERE username = 'admin'";
            ResultSet rs = stmt.executeQuery(checkAdmin);
            if (rs.next() && rs.getInt(1) == 0) {
                String createAdmin = "INSERT INTO users (username, password, email, full_name) " +
                        "VALUES ('admin', 'password', 'admin@example.com', 'Administrator')";
                stmt.executeUpdate(createAdmin);
                LOGGER.info("Default admin user created");
            }

            LOGGER.info("Database initialized successfully");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database initialization failed", e);
            throw e;
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    /**
     * Configure database connection parameters
     * @param url Database URL
     * @param user Database username
     * @param password Database password
     */
    public static void configureDatabaseConnection(String url, String user, String password) {
        DB_URL = url;
        DB_USER = user;
        DB_PASSWORD = password;
        LOGGER.info("Database connection parameters updated");
    }
}