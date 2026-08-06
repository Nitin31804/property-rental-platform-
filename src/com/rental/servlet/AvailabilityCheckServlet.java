package com.rental.servlet;

import com.google.gson.Gson;
import com.rental.dao.BookingDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// We map this servlet to the /api/check-availability endpoint
@WebServlet("/api/check-availability")
public class AvailabilityCheckServlet extends HttpServlet {

    private BookingDAO bookingDAO;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        // Initialize DAO and Gson instance once for the servlet lifecycle
        bookingDAO = new BookingDAO();
        gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Ensure proper JSON response formatting
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Read the JSON payload from the request body
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        PrintWriter out = response.getWriter();

        try {
            // Parse JSON into a Map
            Map<String, String> requestData = gson.fromJson(jsonBuffer.toString(), Map.class);
            
            // Extract parameters
            // Default property ID to 1 if not provided, for demo purposes
            int propertyId = requestData.containsKey("propertyId") 
                             ? Integer.parseInt(requestData.get("propertyId")) 
                             : 1; 
            String checkInDate = requestData.get("checkIn");
            String checkOutDate = requestData.get("checkOut");

            // Validate inputs
            if (checkInDate == null || checkOutDate == null || checkInDate.isEmpty() || checkOutDate.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(com.rental.util.MapUtil.of("error", "checkIn and checkOut dates are required.")));
                return;
            }

            // Perform business logic via DAO
            boolean isAvailable = bookingDAO.isPropertyAvailable(propertyId, checkInDate, checkOutDate);
            
            // Prepare response object
            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("available", isAvailable);

            if (!isAvailable) {
                // If not available, fetch the exact conflicting dates to return to the UI
                List<String> conflictDates = bookingDAO.getConflictingDates(propertyId, checkInDate, checkOutDate);
                jsonResponse.put("conflictDates", conflictDates);
            }

            // Send JSON response
            out.print(gson.toJson(jsonResponse));

        } catch (Exception e) {
            // Handle exceptions and send JSON error
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(com.rental.util.MapUtil.of("error", "Server processing error: " + e.getMessage())));
            e.printStackTrace();
        } finally {
            out.flush();
        }
    }
}
