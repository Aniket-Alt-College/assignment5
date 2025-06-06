package com.servletapp.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the current session
        HttpSession session = request.getSession(false);

        // Invalidate the session if it exists
        if (session != null) {
            session.invalidate();
        }

        // Redirect to login page
        response.sendRedirect(request.getContextPath() + "/login.html");
    }
}