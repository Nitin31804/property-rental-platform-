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
import java.util.List;
import java.util.Map;

@WebServlet("/api/properties")
public class PropertyListServlet extends HttpServlet {
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
        
        String location = request.getParameter("location");
        String propertyType = request.getParameter("propertyType");
        String amenities = request.getParameter("amenities");
        String maxPriceStr = request.getParameter("maxPrice");
        Double maxPrice = null;
        
        if (maxPriceStr != null && !maxPriceStr.trim().isEmpty()) {
            try { maxPrice = Double.parseDouble(maxPriceStr); } catch(Exception e){}
        }
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            List<Map<String, Object>> properties = propertyDAO.searchProperties(location, propertyType, amenities, maxPrice);
            out.print(gson.toJson(properties));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(com.rental.util.MapUtil.of("error", e.getMessage())));
        } finally {
            out.flush();
        }
    }
}
