package com.rental.servlet;

import com.google.gson.Gson;
import com.rental.dao.FeedbackDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/api/feedback")
public class FeedbackServlet extends HttpServlet {
    private FeedbackDAO feedbackDAO;
    private Gson gson;

    @Override
    public void init() {
        feedbackDAO = new FeedbackDAO();
        gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        try {
            Map<String, String> data = gson.fromJson(jsonBuffer.toString(), Map.class);
            
            String name = data.get("name");
            String email = data.get("email");
            String subject = data.get("subject");
            String message = data.get("message");

            if(feedbackDAO.saveFeedback(name, email, subject, message)) {
                out.print(gson.toJson(com.rental.util.MapUtil.of("success", true, "message", "Thank you! Your feedback has been submitted.")));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(gson.toJson(com.rental.util.MapUtil.of("error", "Failed to save feedback.")));
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(com.rental.util.MapUtil.of("error", "Bad request format")));
        } finally {
            out.flush();
        }
    }
}
