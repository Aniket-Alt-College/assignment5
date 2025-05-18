package com.servletapp.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

@WebServlet("/basic")
public class BasicServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Get query parameter if available
        String name = request.getParameter("name");
        if (name == null || name.isEmpty()) {
            name = "Guest";
        }
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Basic Servlet Demo</title>");
        out.println("<style>");
        out.println("table { border-collapse: collapse; width: 100%; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
        out.println("th { background-color: #4CAF50; color: white; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Welcome to Basic Servlet Demo, " + name + "!</h1>");
        
        // Display client request details
        out.println("<h2>Client Request Details:</h2>");
        out.println("<table>");
        out.println("<tr><th>Parameter</th><th>Value</th></tr>");
        
        // Display IP Address
        out.println("<tr><td>IP Address</td><td>" + request.getRemoteAddr() + "</td></tr>");
        
        // Display User-Agent
        out.println("<tr><td>User-Agent</td><td>" + request.getHeader("User-Agent") + "</td></tr>");
        
        // Display Method
        out.println("<tr><td>Request Method</td><td>" + request.getMethod() + "</td></tr>");
        
        // Display Protocol
        out.println("<tr><td>Protocol</td><td>" + request.getProtocol() + "</td></tr>");
        
        // Display all headers
        out.println("<tr><td colspan='2'><strong>All Request Headers:</strong></td></tr>");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            out.println("<tr><td>" + headerName + "</td><td>" + request.getHeader(headerName) + "</td></tr>");
        }
        
        out.println("</table>");
        
        // Form to test doPost method
        out.println("<h2>Test POST method:</h2>");
        out.println("<form method='post' action='basic'>");
        out.println("  <label for='message'>Enter Message:</label>");
        out.println("  <input type='text' id='message' name='message'><br><br>");
        out.println("  <input type='submit' value='Submit via POST'>");
        out.println("</form>");
        
        out.println("<p><a href='index.html'>Back to Home</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Get form parameter
        String message = request.getParameter("message");
        if (message == null || message.isEmpty()) {
            message = "No message provided";
        }
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>POST Method Response</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>You submitted the following message via POST:</h1>");
        out.println("<p><strong>" + message + "</strong></p>");
        out.println("<p><a href='basic'>Back to GET form</a></p>");
        out.println("<p><a href='index.html'>Back to Home</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
}