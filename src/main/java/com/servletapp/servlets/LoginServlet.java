package com.servletapp.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.servletapp.util.DatabaseUtil;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        try {
            // Optional: Get database connection parameters from ServletConfig
            if (getServletConfig().getInitParameter("dbUrl") != null) {
                String dbUrl = getServletConfig().getInitParameter("dbUrl");
                String dbUser = getServletConfig().getInitParameter("dbUser");
                String dbPassword = getServletConfig().getInitParameter("dbPassword");

                DatabaseUtil.configureDatabaseConnection(dbUrl, dbUser, dbPassword);
            }

            // Initialize database
            // Comment out this line if you want to handle database setup separately
            // DatabaseUtil.initializeDatabase();

        } catch (Exception e) {
            throw new ServletException("Failed to initialize database", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // For demo purposes - hardcoded admin login
        // This lets you test without a database connection
        if ("admin".equals(username) && "password".equals(password)) {
            // Create a session for this user
            HttpSession session = request.getSession();
            session.setAttribute("user", username);

            // Redirect to welcome page after successful login
            response.sendRedirect(request.getContextPath() + "/welcome");
            return;
        }

        // Database authentication - only attempt if hardcoded login fails
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();

            // Query to validate user credentials
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // Valid credentials, create session
                HttpSession session = request.getSession();
                session.setAttribute("user", username);
                session.setAttribute("fullName", rs.getString("full_name"));

                // Redirect to welcome page after successful login
                response.sendRedirect(request.getContextPath() + "/welcome");
            } else {
                // Invalid credentials, redirect back to login with error
                response.sendRedirect(request.getContextPath() + "/login.html?error=invalid");
            }

        } catch (SQLException e) {
            // Log the error and redirect to error page
            getServletContext().log("Database error during login", e);
            response.sendRedirect(request.getContextPath() + "/error.html");
        } finally {
            DatabaseUtil.closeResources(conn, pstmt, rs);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect GET requests to the login page
        response.sendRedirect(request.getContextPath() + "/login.html");
    }
}