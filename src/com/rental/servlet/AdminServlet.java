package com.rental.servlet;

import com.google.gson.Gson;
import com.rental.dao.BookingDAO;
import com.rental.dao.FeedbackDAO;
import com.rental.dao.PropertyDAO;
import com.rental.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/admin/dashboard")
public class AdminServlet extends HttpServlet {
    private UserDAO userDAO;
    private PropertyDAO propertyDAO;
    private BookingDAO bookingDAO;
    private FeedbackDAO feedbackDAO;
    private Gson gson;

    @Override
    public void init() {
        userDAO = new UserDAO();
        propertyDAO = new PropertyDAO();
        bookingDAO = new BookingDAO();
        feedbackDAO = new FeedbackDAO();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // 1. Authenticate Admin Session
        HttpSession session = request.getSession(false);
        if (session == null || !"Admin".equals(session.getAttribute("role"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            out.print(gson.toJson(com.rental.util.MapUtil.of("error", "Access Denied. Admins only.")));
            out.flush();
            return;
        }

        // 2. Fetch all system data
        try {
            Map<String, Object> dashboardData = new HashMap<>();
            dashboardData.put("users", userDAO.getAllUsers());
            dashboardData.put("properties", propertyDAO.getAllProperties());
            dashboardData.put("bookings", bookingDAO.getAllBookings());
            dashboardData.put("feedback", feedbackDAO.getAllFeedback());
            
            out.print(gson.toJson(dashboardData));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(com.rental.util.MapUtil.of("error", e.getMessage())));
        } finally {
            out.flush();
        }
    }
}
