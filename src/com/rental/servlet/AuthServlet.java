package com.rental.servlet;

import com.google.gson.Gson;
import com.rental.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {
    private UserDAO userDAO;
    private Gson gson;

    @Override
    public void init() {
        userDAO = new UserDAO();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String pathInfo = request.getPathInfo();

        if ("/me".equals(pathInfo)) {
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("userId") != null) {
                out.print(gson.toJson(com.rental.util.MapUtil.of(
                    "loggedIn", true,
                    "userId", session.getAttribute("userId"),
                    "name", session.getAttribute("name"),
                    "role", session.getAttribute("role")
                )));
            } else {
                out.print(gson.toJson(com.rental.util.MapUtil.of("loggedIn", false)));
            }
        } else if ("/logout".equals(pathInfo)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            out.print(gson.toJson(com.rental.util.MapUtil.of("success", true, "message", "Logged out")));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String pathInfo = request.getPathInfo();

        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        try {
            Map<String, String> data = gson.fromJson(jsonBuffer.toString(), Map.class);
            
            if ("/login".equals(pathInfo)) {
                String email = data.get("email");
                String password = data.get("password");
                
                Map<String, Object> user = userDAO.authenticateUser(email, password);
                if (user != null) {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("userId", user.get("userId"));
                    session.setAttribute("name", user.get("name"));
                    session.setAttribute("role", user.get("role"));
                    out.print(gson.toJson(com.rental.util.MapUtil.of("success", true, "user", user)));
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    out.print(gson.toJson(com.rental.util.MapUtil.of("error", "Invalid email or password")));
                }

            } else if ("/register".equals(pathInfo)) {
                String name = data.get("name");
                String email = data.get("email");
                String password = data.get("password");
                String role = data.get("role");

                if(userDAO.registerUser(name, email, password, role)) {
                    out.print(gson.toJson(com.rental.util.MapUtil.of("success", true, "message", "Registration successful! Please login.")));
                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    out.print(gson.toJson(com.rental.util.MapUtil.of("error", "Registration failed. Email might already exist.")));
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(com.rental.util.MapUtil.of("error", "Bad request format")));
        } finally {
            out.flush();
        }
    }
}
