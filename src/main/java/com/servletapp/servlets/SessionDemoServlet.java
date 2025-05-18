package com.servletapp.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/session")
public class SessionDemoServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Get the HttpSession object
        HttpSession session = request.getSession();
        
        // Check if this is a new session
        boolean isNewSession = session.isNew();
        
        // Get session ID
        String sessionId = session.getId();
        
        // Get values from the session (if they exist)
        String username = (String) session.getAttribute("username");
        String theme = (String) session.getAttribute("theme");
        String language = (String) session.getAttribute("language");
        
        // Get values from cookies
        String cookieUsername = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    cookieUsername = cookie.getValue();
                    break;
                }
            }
        }
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Session Management Demo</title>");
        
        // Apply theme if available
        if (theme != null) {
            if (theme.equals("dark")) {
                out.println("<style>");
                out.println("body { background-color: #333; color: #fff; }");
                out.println("a { color: #0af; }");
                out.println("</style>");
            } else if (theme.equals("light")) {
                out.println("<style>");
                out.println("body { background-color: #fff; color: #333; }");
                out.println("a { color: #07f; }");
                out.println("</style>");
            }
        }
        
        out.println("</head>");
        out.println("<body>");
        
        // Apply language if available
        String greeting = "Welcome to the Session Demo";
        if (language != null) {
            if (language.equals("es")) {
                greeting = "Bienvenido a la Demostración de Sesión";
            } else if (language.equals("fr")) {
                greeting = "Bienvenue à la Démonstration de Session";
            }
        }
        
        out.println("<h1>" + greeting + "</h1>");
        
        // Display session information
        out.println("<h2>Session Information:</h2>");
        out.println("<p>Session ID: " + sessionId + "</p>");
        out.println("<p>New Session: " + isNewSession + "</p>");
        
        // Display session attributes
        out.println("<h2>Session Attributes:</h2>");
        out.println("<p>Username (HttpSession): " + (username != null ? username : "Not set") + "</p>");
        out.println("<p>Theme (HttpSession): " + (theme != null ? theme : "Not set") + "</p>");
        out.println("<p>Language (HttpSession): " + (language != null ? language : "Not set") + "</p>");
        
        // Display cookie values
        out.println("<h2>Cookie Values:</h2>");
        out.println("<p>Username (Cookie): " + (cookieUsername != null ? cookieUsername : "Not set") + "</p>");
        
        // Form to set session attributes
        out.println("<h2>Set Session Attributes:</h2>");
        out.println("<form method='post' action='session'>");
        out.println("  <label for='username'>Username:</label>");
        out.println("  <input type='text' id='username' name='username' value='" + (username != null ? username : "") + "'><br><br>");
        
        out.println("  <label for='theme'>Theme:</label>");
        out.println("  <select id='theme' name='theme'>");
        out.println("    <option value='light'" + ("light".equals(theme) ? " selected" : "") + ">Light</option>");
        out.println("    <option value='dark'" + ("dark".equals(theme) ? " selected" : "") + ">Dark</option>");
        out.println("  </select><br><br>");
        
        out.println("  <label for='language'>Language:</label>");
        out.println("  <select id='language' name='language'>");
        out.println("    <option value='en'" + ("en".equals(language) ? " selected" : "") + ">English</option>");
        out.println("    <option value='es'" + ("es".equals(language) ? " selected" : "") + ">Spanish</option>");
        out.println("    <option value='fr'" + ("fr".equals(language) ? " selected" : "") + ">French</option>");
        out.println("  </select><br><br>");
        
        out.println("  <label for='rememberMe'>Remember Username (Cookie):</label>");
        out.println("  <input type='checkbox' id='rememberMe' name='rememberMe'><br><br>");
        
        out.println("  <input type='submit' value='Save Preferences'>");
        out.println("</form>");
        
        // URL Rewriting example
        out.println("<h2>URL Rewriting Demo:</h2>");
        String encodedURL = response.encodeURL("session?action=urlrewrite");
        out.println("<p><a href='" + encodedURL + "'>Click here to test URL Rewriting</a></p>");
        
        out.println("<p><a href='index.html'>Back to Home</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get form parameters
        String username = request.getParameter("username");
        String theme = request.getParameter("theme");
        String language = request.getParameter("language");
        String rememberMe = request.getParameter("rememberMe");
        
        // Store username in HttpSession
        HttpSession session = request.getSession();
        session.setAttribute("username", username);
        session.setAttribute("theme", theme);
        session.setAttribute("language", language);
        
        // Store username in Cookie if rememberMe is checked
        if (rememberMe != null) {
            Cookie usernameCookie = new Cookie("username", username);
            usernameCookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
            response.addCookie(usernameCookie);
        }
        
        // Redirect back to the servlet
        response.sendRedirect("session");
    }
}