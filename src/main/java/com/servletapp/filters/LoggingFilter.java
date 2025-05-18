package com.servletapp.filters;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class LoggingFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("LoggingFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();

        LOGGER.info("Incoming request: " + uri);
        long startTime = System.currentTimeMillis();

        try {
            // Continue with the filter chain
            chain.doFilter(request, response);
        } finally {
            long endTime = System.currentTimeMillis();
            LOGGER.info("Request " + uri + " completed in " + (endTime - startTime) + " ms");
        }
    }

    @Override
    public void destroy() {
        LOGGER.info("LoggingFilter destroyed");
    }
}