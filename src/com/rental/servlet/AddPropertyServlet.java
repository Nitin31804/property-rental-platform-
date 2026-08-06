package com.rental.servlet;

import com.google.gson.Gson;
import com.rental.dao.PropertyDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/api/add-property")
public class AddPropertyServlet extends HttpServlet {
    private PropertyDAO propertyDAO;
    private Gson gson;

    @Override
    public void init() {
        propertyDAO = new PropertyDAO();
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
            // Check session and Host role
            javax.servlet.http.HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print(gson.toJson(com.rental.util.MapUtil.of("error", "You must be logged in to list a property.")));
                return;
            }
            if (!"Host".equals(session.getAttribute("role")) && !"Admin".equals(session.getAttribute("role"))) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.print(gson.toJson(com.rental.util.MapUtil.of("error", "Only Hosts can list properties.")));
                return;
            }
            int hostId = (Integer) session.getAttribute("userId");

            Map<String, String> data = gson.fromJson(jsonBuffer.toString(), Map.class);
            
            String title = data.get("title");
            String description = data.get("description");
            double price = Double.parseDouble(data.get("price"));
            String location = data.get("location");
            String coverUrl = data.get("coverUrl");

            boolean success = propertyDAO.addProperty(hostId, title, description, price, location, coverUrl);

            if (success) {
                out.print(gson.toJson(com.rental.util.MapUtil.of("success", true, "message", "Property added successfully")));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(gson.toJson(com.rental.util.MapUtil.of("error", "Failed to add property to database.")));
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(com.rental.util.MapUtil.of("error", "Invalid data format: " + e.getMessage())));
        } finally {
            out.flush();
        }
    }
}
