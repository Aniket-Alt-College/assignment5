// DatabaseUtil.java
package com.servletapp.util;

import com.servletapp.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.InputStream;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * Utility class for database operations
 * Demonstrates various approaches to database connectivity in a servlet environment
 */
public class DatabaseUtil {

    // JDBC URL, username and password
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/servletapp";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "password";

    // Connection pool name (for JNDI lookup)
    private static final String DATASOURCE_JNDI = "java:comp/env/jdbc/servletapp";

    /**
     * Get a database connection using direct JDBC
     * @return A database connection
     * @throws SQLException If a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    /**
     * Get a database connection using properties file
     * @return A database connection
     * @throws Exception If a database access error occurs
     */
    public static Connection getConnectionFromProperties() throws Exception {
        Properties props = new Properties();

        // Load database properties from file
        try (InputStream input = DatabaseUtil.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new Exception("Unable to find database.properties");
            }
            props.load(input);
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Get a database connection from a connection pool using JNDI
     * @return A pooled database connection
     * @throws Exception If a database access error occurs
     */
    public static Connection getConnectionFromPool() throws Exception {
        Context initContext = new InitialContext();
        DataSource ds = (DataSource) initContext.lookup(DATASOURCE_JNDI);
        return ds.getConnection();
    }

    /**
     * Close database resources safely
     * @param conn The Connection to close
     * @param stmt The Statement to close
     * @param rs The ResultSet to close
     */
    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the database with sample data
     * @throws SQLException If a database access error occurs
     */
    public static void initializeDatabase() throws SQLException {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();

            // Check if users table exists, if not create it
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, "users", null);

            if (!tables.next()) {
                // Create users table
                String createTableSQL = "CREATE TABLE users (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "username VARCHAR(50) NOT NULL UNIQUE, " +
                        "password VARCHAR(100) NOT NULL, " +
                        "fullname VARCHAR(100), " +
                        "email VARCHAR(100), " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
                stmt.executeUpdate(createTableSQL);

                // Add sample users
                String insertUser1 = "INSERT INTO users (username, password, fullname, email) " +
                        "VALUES ('admin', 'admin123', 'Administrator', 'admin@example.com')";

                String insertUser2 = "INSERT INTO users (username, password, fullname, email) " +
                        "VALUES ('user', 'user123', 'Regular User', 'user@example.com')";

                stmt.executeUpdate(insertUser1);
                stmt.executeUpdate(insertUser2);
            }

            tables.close();
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    /**
     * Authenticate a user
     * @param username The username
     * @param password The password
     * @return true if authentication successful, false otherwise
     */
    public static boolean authenticateUser(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean authenticated = false;

        try {
            conn = getConnection();
            String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            rs = stmt.executeQuery();
            if (rs.next()) {
                authenticated = (rs.getInt(1) > 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }

        return authenticated;
    }

    /**
     * Get user by username
     * @param username The username to look up
     * @return User object if found, null otherwise
     */
    public static User getUserByUsername(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = getConnection();
            String sql = "SELECT id, username, password, fullname, email FROM users WHERE username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }

        return user;
    }

    /**
     * Get all users
     * @return List of all users
     */
    public static List<User> getAllUsers() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT id, username, password, fullname, email FROM users";

            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));

                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }

        return users;
    }

    /**
     * Add a new user
     * @param user The user to add
     * @return true if successful, false otherwise
     */
    public static boolean addUser(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = getConnection();
            String sql = "INSERT INTO users (username, password, fullname, email) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getEmail());

            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, null);
        }

        return success;
    }

    /**
     * Update an existing user
     * @param user The user to update
     * @return true if successful, false otherwise
     */
    public static boolean updateUser(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = getConnection();
            String sql = "UPDATE users SET password = ?, fullname = ?, email = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getFullName());
            stmt.setString(3, user.getEmail());
            stmt.setInt(4, user.getId());

            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, null);
        }

        return success