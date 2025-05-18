package com.servletapp.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/welcome", "/profile", "/settings"})
public class AuthenticationFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Get the current session without creating a new one
        HttpSession session = httpRequest.getSession(false);
        
        // Check if user is logged in
        boolean isLoggedIn = (session != null && session.getAttribute("loggedInUser") != null);
        
        // Get the requested URL
        String requestURI = httpRequest.getRequestURI();
        
        // Allow access to login page even if not logged in
        boolean isLoginPage = requestURI.endsWith("login.html") || requestURI.endsWith("login");
        boolean isLoginAction = httpRequest.getMethod().equalsIgnoreCase("POST") && isLoginPage;
        
        if (isLoggedIn || isLoginPage || isLoginAction) {
            // User is authenticated or accessing login page, proceed
            chain.doFilter(request, response);
        } else {
            // User is not authenticated, redirect to login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        }
    }
    
    @Override
    public void destroy() {
        // Cleanup code if needed
    }
}