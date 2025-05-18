package com.servletapp.servlets;

import com.servletapp.util.DatabaseUtil;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private com.servletapp.servlets.DatabaseUtil dbUtil;
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize database utility
        dbUtil = new DatabaseUtil();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if the user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedInUser") != null) {
            // User is already logged in, redirect to welcome page
            response.sendRedirect("welcome");
            return;
        }
        
        // Check if there's an error message
        String error = request.getParameter("error");
        if (error != null) {
            request.setAttribute("errorMessage", "Invalid username or password. Please try again.");
        }
        
        // Forward to login.html page
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login.html");
        dispatcher.forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get form parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Simple validation
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            
            // Invalid input, redirect back to login page with error
            response.sendRedirect("login?error=true");
            return;
        }
        
        // Authenticate user (in a real application, this would use a database)
        boolean isValid = authenticateUser(username, password);
        
        if (isValid) {
            // Set user in session
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", username);
            
            // Forward to welcome servlet
            RequestDispatcher dispatcher = request.getRequestDispatcher("/welcome");
            request.setAttribute("welcomeMessage", "Welcome, " + username + "! You have successfully logged in.");
            dispatcher.forward(request, response);
        } else {
            // Authentication failed, redirect back to login page with error
            response.sendRedirect("login?error=true");
        }
    }
    
    private boolean authenticateUser(String username, String password) {
        // Simple authentication logic for demo purposes
        // In an advanced implementation, this would use DatabaseUtil to query the database
        
        // For demonstration, hardcoded check
        if (username.equals("admin") && password.equals("password123")) {
            return true;
        }
        
        // If we're using the database (advanced implementation)
        // return dbUtil.validateUser(username, password);
        
        return false;
    }
}