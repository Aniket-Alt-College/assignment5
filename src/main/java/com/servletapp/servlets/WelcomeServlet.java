package com.servletapp.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Get the current session or return null if no session exists
        HttpSession session = request.getSession(false);

        String user = (session != null) ? (String) session.getAttribute("user") : null;

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Welcome Page</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }");
            out.println(".container { max-width: 800px; margin: 0 auto; background-color: white; padding: 20px; border-radius: 5px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
            out.println("h1 { color: #333; }");
            out.println("nav { margin-bottom: 20px; }");
            out.println("nav a { margin-right: 15px; text-decoration: none; color: #0066cc; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<h1>Welcome to Servlet Assignment</h1>");

            if (user != null) {
                out.println("<p>Hello, <strong>" + user + "</strong>! You are logged in.</p>");
                out.println("<nav>");
                out.println("<a href='" + request.getContextPath() + "/profile'>View Profile</a>");
                out.println("<a href='" + request.getContextPath() + "/settings'>Settings</a>");
                out.println("<a href='" + request.getContextPath() + "/logout'>Logout</a>");
                out.println("</nav>");
            } else {
                out.println("<p>You are not logged in. Please <a href='" + request.getContextPath() + "/login.html'>login</a> to continue.</p>");
            }

            out.println("<p>This page is protected by <code>AuthenticationFilter</code>.</p>");
            out.println("<p>Every request to this application is logged by <code>LoggingFilter</code>.</p>");

            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}