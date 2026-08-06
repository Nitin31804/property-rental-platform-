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

public class UserDAO {

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

    public Map<String, Object> authenticateUser(String email, String password) {
        String sql = "SELECT user_id, name, role FROM Users WHERE email = ? AND password_hash = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            // In a real app, hash the incoming password first before comparing!
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("userId", rs.getInt("user_id"));
                    user.put("name", rs.getString("name"));
                    user.put("role", rs.getString("role"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean registerUser(String name, String email, String password, String role) {
        String sql = "INSERT INTO Users (name, email, password_hash, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, role);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Map<String, Object>> getAllUsers() {
        List<Map<String, Object>> users = new ArrayList<>();
        String sql = "SELECT user_id, name, email, role, created_at FROM Users ORDER BY created_at DESC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> u = new HashMap<>();
                u.put("userId", rs.getInt("user_id"));
                u.put("name", rs.getString("name"));
                u.put("email", rs.getString("email"));
                u.put("role", rs.getString("role"));
                u.put("createdAt", rs.getTimestamp("created_at").toString());
                users.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
