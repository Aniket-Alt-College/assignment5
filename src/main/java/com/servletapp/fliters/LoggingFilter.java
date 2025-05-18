package com.servletapp.fliters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@WebFilter("/*")
public class LoggingFilter implements Filter {
    
    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());
    private FileHandler fileHandler;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            // Create a log file in the server's log directory
            fileHandler = new FileHandler(System.getProperty("catalina.base") + "/logs/servlet-app.log", true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            LOGGER.addHandler(fileHandler);
            LOGGER.info("LoggingFilter initialized");
        } catch (IOException e) {
            throw new ServletException("Failed to initialize logging filter", e);
        }
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        // Log request details
        String ipAddress = request.getRemoteAddr();
        String requestedURL = httpRequest.getRequestURL().toString();
        String method = httpRequest.getMethod();
        String userAgent = httpRequest.getHeader("User-Agent");
        String timestamp = new Date().toString();
        
        // Create log entry
        StringBuilder logEntry = new StringBuilder();
        logEntry.append("\n========== REQUEST LOG ==========\n");
        logEntry.append("Time: ").append(timestamp).append("\n");
        logEntry.append("IP Address: ").append(ipAddress).append("\n");
        logEntry.append("Method: ").append(method).append("\n");
        logEntry.append("URL: ").append(requestedURL).append("\n");
        logEntry.append("User-Agent: ").append(userAgent).append("\n");
        logEntry.append("=================================\n");
        
        // Log to file
        LOGGER.info(logEntry.toString());
        
        // Continue with the filter chain
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        if (fileHandler != null) {
            fileHandler.close();
        }
    }
}