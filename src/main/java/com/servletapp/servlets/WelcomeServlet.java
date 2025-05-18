package com.servletapp.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        // Check if the user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            // User is not logged in, redirect to login page
            response.sendRedirect("login");
            return;
        }
        
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Process POST requests the same as GET requests
        processRequest(request, response);
    }
    
    private void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Get the logged-in user from session
        HttpSession session = request.getSession(false);
        String username = (String) session.getAttribute("loggedInUser");
        
        // Get welcome message from request attribute (if forwarded from LoginServlet)
        String welcomeMessage = (String) request.getAttribute("welcomeMessage");
        if (welcomeMessage == null) {
            welcomeMessage = "Welcome back, " + username + "!";
        }
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Welcome Page</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
        out.println(".welcome-box { background-color: #f0f8ff; border: 1px solid #b0c4de; padding: 20px; border-radius: 5px; }");
        out.println(".logout-btn { background-color: #f44336; color: white; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        
        out.println("<div class='welcome-box'>");
        out.println("<h1>" + welcomeMessage + "</h1>");
        out.println("<p>You are now in a protected area that requires authentication.</p>");
        out.println("<p>Session ID: " + session.getId() + "</p>");
        out.println("<p>Created: " + new java.util.Date(session.getCreationTime()) + "</p>");
        out.println("<p>Last Accessed: " + new java.util.Date(session.getLastAccessedTime()) + "</p>");
        out.println("</div>");
        
        out.println("<h2>Protected Actions:</h2>");
        out.println("<ul>");
        out.println("<li><a href='profile'>View Profile</a></li>");
        out.println("<li><a href='settings'>Account Settings</a></li>");
        out.println("</ul>");
        
        out.println("<form action='logout' method='post'>");
        out.println("<input type='submit' class='logout-btn' value='Logout'>");
        out.println("</form>");
        
        out.println("<p><a href='index.html'>Back to Home</a></p>");
        
        out.println("</body>");
        out.println("</html>");
    }
}