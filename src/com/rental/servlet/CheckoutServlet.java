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
import java.util.Map;

@WebServlet("/api/checkout")
public class CheckoutServlet extends HttpServlet {
    private BookingDAO bookingDAO;
    private Gson gson;

    @Override
    public void init() {
        bookingDAO = new BookingDAO();
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
            // Check session
            javax.servlet.http.HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print(gson.toJson(com.rental.util.MapUtil.of("error", "You must be logged in to book a property.")));
                return;
            }
            int guestId = (Integer) session.getAttribute("userId");

            Map<String, String> data = gson.fromJson(jsonBuffer.toString(), Map.class);
            
            int propertyId = Integer.parseInt(data.get("propertyId"));
            String checkIn = data.get("checkIn");
            String checkOut = data.get("checkOut");
            double totalPrice = Double.parseDouble(data.get("totalPrice"));
            
            String guestName = data.get("guestName");
            Integer guestAge = data.containsKey("guestAge") && data.get("guestAge") != null && !data.get("guestAge").isEmpty() ? Integer.parseInt(data.get("guestAge")) : null;
            String specialRequests = data.get("specialRequests");
            Integer guestCount = data.containsKey("guestCount") && data.get("guestCount") != null && !data.get("guestCount").toString().isEmpty() ? Integer.parseInt(data.get("guestCount").toString()) : null;

            // Re-validate availability before charging (in a real app, this is critical)
            boolean available = bookingDAO.isPropertyAvailable(propertyId, checkIn, checkOut);
            
            if (!available) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                out.print(gson.toJson(com.rental.util.MapUtil.of("error", "These dates are no longer available. Someone else just booked them!")));
                return;
            }

            // SIMULATE PAYMENT PROCESSING HERE
            // ... contacting payment gateway ...
            // ... payment successful ...

            // Save booking to Database
            boolean success = bookingDAO.createBooking(propertyId, guestId, checkIn, checkOut, totalPrice, guestName, guestAge, specialRequests, guestCount);

            if (success) {
                out.print(gson.toJson(com.rental.util.MapUtil.of("success", true, "message", "Payment processed and booking confirmed!")));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(gson.toJson(com.rental.util.MapUtil.of("error", "Payment processed but failed to save booking to database.")));
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(com.rental.util.MapUtil.of("error", "Invalid checkout data: " + e.getMessage())));
            e.printStackTrace();
        } finally {
            out.flush();
        }
    }
}
