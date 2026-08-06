package com.rental.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackDAO {

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

    public boolean saveFeedback(String name, String email, String subject, String message) {
        String sql = "INSERT INTO CustomerFeedback (name, email, subject, message) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, subject);
            pstmt.setString(4, message);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Map<String, Object>> getAllFeedback() {
        List<Map<String, Object>> feedbackList = new ArrayList<>();
        String sql = "SELECT feedback_id, name, email, subject, message, created_at FROM CustomerFeedback ORDER BY created_at DESC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> f = new HashMap<>();
                f.put("id", rs.getInt("feedback_id"));
                f.put("name", rs.getString("name"));
                f.put("email", rs.getString("email"));
                f.put("subject", rs.getString("subject"));
                f.put("message", rs.getString("message"));
                f.put("createdAt", rs.getTimestamp("created_at").toString());
                feedbackList.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbackList;
    }
}
