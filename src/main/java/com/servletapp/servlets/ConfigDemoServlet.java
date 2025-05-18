package com.servletapp.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

public class ConfigDemoServlet extends HttpServlet {
    
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        // Read initialization parameters from web.xml
        dbUrl = config.getInitParameter("dbUrl");
        dbUser = config.getInitParameter("dbUser");
        dbPassword = config.getInitParameter("dbPassword");
        
        // Store application-wide data in ServletContext
        ServletContext context = config.getServletContext();
        context.setAttribute("appName", "Servlet Assignment Application");
        context.setAttribute("visitorCount", 0);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Get the ServletContext
        ServletContext context = getServletContext();
        
        // Update visitor count
        Integer visitorCount = (Integer) context.getAttribute("visitorCount");
        visitorCount++;
        context.setAttribute("visitorCount", visitorCount);
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>ServletConfig & ServletContext Demo</title>");
        out.println("<style>");
        out.println("table { border-collapse: collapse; width: 100%; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
        out.println("th { background-color: #4CAF50; color: white; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>ServletConfig & ServletContext Demo</h1>");
        
        // Display ServletConfig parameters (with password masked)
        out.println("<h2>ServletConfig Parameters:</h2>");
        out.println("<table>");
        out.println("<tr><th>Parameter</th><th>Value</th></tr>");
        out.println("<tr><td>Database URL</td><td>" + dbUrl + "</td></tr>");
        out.println("<tr><td>Database User</td><td>" + dbUser + "</td></tr>");
        out.println("<tr><td>Database Password</td><td>********</td></tr>");
        
        // Display all init parameters
        out.println("<tr><td colspan='2'><strong>All Init Parameters:</strong></td></tr>");
        Enumeration<String> initParams = getInitParameterNames();
        while (initParams.hasMoreElements()) {
            String paramName = initParams.nextElement();
            String paramValue = getInitParameter(paramName);
            if (paramName.contains("password")) {
                paramValue = "********";
            }
            out.println("<tr><td>" + paramName + "</td><td>" + paramValue + "</td></tr>");
        }
        out.println("</table>");
        
        // Display ServletContext attributes
        out.println("<h2>ServletContext Attributes:</h2>");
        out.println("<table>");
        out.println("<tr><th>Attribute</th><th>Value</th></tr>");
        out.println("<tr><td>Application Name</td><td>" + context.getAttribute("appName") + "</td></tr>");
        out.println("<tr><td>Visitor Count</td><td>" + context.getAttribute("visitorCount") + "</td></tr>");
        
        // Display ServletContext parameters
        out.println("<tr><td colspan='2'><strong>Context Parameters:</strong></td></tr>");
        Enumeration<String> contextParams = context.getInitParameterNames();
        while (contextParams.hasMoreElements()) {
            String paramName = contextParams.nextElement();
            out.println("<tr><td>" + paramName + "</td><td>" + context.getInitParameter(paramName) + "</td></tr>");
        }
        out.println("</table>");
        
        out.println("<p><a href='index.html'>Back to Home</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
}