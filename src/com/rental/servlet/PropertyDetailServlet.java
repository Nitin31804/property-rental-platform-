package com.rental.servlet;

import com.google.gson.Gson;
import com.rental.dao.PropertyDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/api/property")
public class PropertyDetailServlet extends HttpServlet {
    private PropertyDAO propertyDAO;
    private Gson gson;

    @Override
    public void init() {
        propertyDAO = new PropertyDAO();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            int propertyId = Integer.parseInt(request.getParameter("id"));
            Map<String, Object> property = propertyDAO.getPropertyDetails(propertyId);
            
            if (property.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(gson.toJson(com.rental.util.MapUtil.of("error", "Property not found")));
            } else {
                out.print(gson.toJson(property));
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(com.rental.util.MapUtil.of("error", "Invalid property ID")));
        } finally {
            out.flush();
        }
    }
}
