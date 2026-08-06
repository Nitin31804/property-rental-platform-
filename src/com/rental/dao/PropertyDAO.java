package com.rental.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyDAO {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/property_rental_db";
    private static final String USER = "root";
    private static final String PASS = "NITIN1875";

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public List<Map<String, Object>> searchProperties(String locationQuery, String propertyType, String amenities, Double maxPrice) {
        List<Map<String, Object>> properties = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Properties WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (locationQuery != null && !locationQuery.trim().isEmpty()) {
            sql.append(" AND (location_city LIKE ? OR location_country LIKE ?)");
            params.add("%" + locationQuery + "%");
            params.add("%" + locationQuery + "%");
        }
        if (propertyType != null && !propertyType.trim().isEmpty()) {
            sql.append(" AND property_type = ?");
            params.add(propertyType);
        }
        if (maxPrice != null) {
            sql.append(" AND price_per_night <= ?");
            params.add(maxPrice);
        }
        if (amenities != null && !amenities.trim().isEmpty()) {
            // Very simple CSV LIKE check for demo purposes
            String[] reqAmens = amenities.split(",");
            for (String am : reqAmens) {
                sql.append(" AND amenities LIKE ?");
                params.add("%" + am.trim() + "%");
            }
        }
        
        sql.append(" ORDER BY property_id DESC");

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> p = new HashMap<>();
                    p.put("propertyId", rs.getInt("property_id"));
                    p.put("title", rs.getString("title"));
                    p.put("pricePerNight", rs.getDouble("price_per_night"));
                    p.put("locationCity", rs.getString("location_city"));
                    p.put("locationCountry", rs.getString("location_country"));
                    p.put("coverImageUrl", rs.getString("cover_image_url"));
                    p.put("propertyType", rs.getString("property_type"));
                    p.put("amenities", rs.getString("amenities"));
                    p.put("rating", rs.getDouble("rating"));
                    p.put("reviewCount", rs.getInt("review_count"));
                    p.put("latitude", rs.getDouble("latitude"));
                    p.put("longitude", rs.getDouble("longitude"));
                    properties.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return properties;
    }
    
    // Kept for backward compatibility if used elsewhere without args
    public List<Map<String, Object>> getAllProperties() {
        return searchProperties(null, null, null, null);
    }

    public Map<String, Object> getPropertyDetails(int propertyId) {
        Map<String, Object> property = new HashMap<>();
        String sql = "SELECT * FROM Properties WHERE property_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, propertyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    property.put("propertyId", rs.getInt("property_id"));
                    property.put("title", rs.getString("title"));
                    property.put("description", rs.getString("description"));
                    property.put("pricePerNight", rs.getDouble("price_per_night"));
                    property.put("locationCity", rs.getString("location_city"));
                    property.put("coverImageUrl", rs.getString("cover_image_url"));

                    // Fetch Image Gallery
                    property.put("gallery", getPropertyImages(propertyId, conn));
                    
                    // Fetch Active Views
                    com.rental.util.ViewTracker.getInstance().incrementView(propertyId);
                    property.put("activeViews", com.rental.util.ViewTracker.getInstance().getActiveViews(propertyId));
                    
                    // Fetch Recent Bookings
                    String bookingSql = "SELECT COUNT(*) FROM Bookings WHERE property_id = ? AND status = 'Confirmed' AND created_at >= NOW() - INTERVAL 1 DAY";
                    try (PreparedStatement pstmt2 = conn.prepareStatement(bookingSql)) {
                        pstmt2.setInt(1, propertyId);
                        try (ResultSet rs2 = pstmt2.executeQuery()) {
                            if (rs2.next()) {
                                int recent = rs2.getInt(1);
                                // For demo purposes, guarantee at least some bookings
                                if (recent == 0) recent = new java.util.Random().nextInt(4) + 1;
                                property.put("recentBookings", recent);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return property;
    }

    private List<String> getPropertyImages(int propertyId, Connection conn) throws SQLException {
        List<String> images = new ArrayList<>();
        String sql = "SELECT image_url FROM PropertyImages WHERE property_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, propertyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    images.add(rs.getString("image_url"));
                }
            }
        }
        return images;
    }

    public boolean addProperty(int hostId, String title, String desc, double price, String location, String coverUrl) {
        String sql = "INSERT INTO Properties (host_id, title, description, price_per_night, location_city, cover_image_url) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, hostId);
            pstmt.setString(2, title);
            pstmt.setString(3, desc);
            pstmt.setDouble(4, price);
            pstmt.setString(5, location);
            pstmt.setString(6, coverUrl);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
