package com.rental.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingDAO {
    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/property_rental_db";
    private static final String USER = "root";
    private static final String PASS = "NITIN1875"; // Updated to user's password

    // Establish database connection
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    /**
     * Checks if a property is available for the given date range.
     * 
     * @param propertyId The ID of the property to check.
     * @param checkIn The requested check-in date.
     * @param checkOut The requested check-out date.
     * @return true if available, false if there are booking conflicts.
     */
    public boolean isPropertyAvailable(int propertyId, String checkIn, String checkOut) {
        /*
         * EXPLICIT OVERLAP LOGIC:
         * A booking conflict occurs if an existing booking for the given property has a date range
         * that overlaps with the requested date range.
         * 
         * An overlap happens when:
         * 1. Existing check-in date is strictly LESS THAN requested check-out date AND
         * 2. Existing check-out date is strictly GREATER THAN requested check-in date
         * 
         * This logic accounts for all 4 types of overlaps:
         * A. Requested dates completely contain existing dates.
         * B. Existing dates completely contain requested dates.
         * C. Requested dates overlap the start of existing dates.
         * D. Requested dates overlap the end of existing dates.
         * 
         * Note: If check-out date of an existing booking equals the check-in date of a new request,
         * it is typically allowed (same day turnover). Hence, we use strict inequalities (<, >).
         */
        String sql = "SELECT COUNT(*) FROM Bookings " +
                     "WHERE property_id = ? " +
                     "AND status = 'Confirmed' " +
                     "AND check_in_date < ? " +
                     "AND check_out_date > ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, propertyId);
            pstmt.setString(2, checkOut); // Notice order: existing check_in < requested check_out
            pstmt.setString(3, checkIn);  // existing check_out > requested check_in

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int conflictCount = rs.getInt(1);
                    return conflictCount == 0; // Available if 0 conflicts
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Default to not available if an error occurs
    }

    /**
     * Retrieves a list of conflicting dates for a property to highlight in the UI.
     * Since full date lists might be large, this method returns the start and end of conflicts.
     * For a robust UI, you might want to return every single booked day.
     */
    public List<String> getConflictingDates(int propertyId, String checkIn, String checkOut) {
        List<String> conflicts = new ArrayList<>();
        String sql = "SELECT check_in_date, check_out_date FROM Bookings " +
                     "WHERE property_id = ? " +
                     "AND status = 'Confirmed' " +
                     "AND check_in_date < ? " +
                     "AND check_out_date > ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, propertyId);
            pstmt.setString(2, checkOut);
            pstmt.setString(3, checkIn);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate start = rs.getDate("check_in_date").toLocalDate();
                    LocalDate end = rs.getDate("check_out_date").toLocalDate();
                    
                    // Populate the list with all individual dates in the conflict range
                    // This is very useful for the frontend calendar highlighting
                    LocalDate current = start;
                    while (current.isBefore(end)) { // check-out day is usually available for a new check-in
                        conflicts.add(current.toString());
                        current = current.plusDays(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conflicts;
    }

    /**
     * Creates a new booking in the database.
     */
    public boolean createBooking(int propertyId, int guestId, String checkIn, String checkOut, double totalPrice, String guestName, Integer guestAge, String specialRequests, Integer guestCount) {
        String checkSql = "SELECT COUNT(*) FROM Bookings WHERE property_id = ? AND status = 'Confirmed' AND (check_in_date < ? AND check_out_date > ?)";
        String insertSql = "INSERT INTO Bookings (property_id, guest_id, check_in_date, check_out_date, total_price, status, guest_name, guest_age, special_requests, guest_count) VALUES (?, ?, ?, ?, ?, 'Confirmed', ?, ?, ?, ?)";
        
        try (Connection conn = getConnection()) {
            
            // Check for overlapping dates
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, propertyId);
                checkStmt.setDate(2, java.sql.Date.valueOf(checkOut));
                checkStmt.setDate(3, java.sql.Date.valueOf(checkIn));
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false; // Reject booking due to overlap
                    }
                }
            }

            // Create booking
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setInt(1, propertyId);
                pstmt.setInt(2, guestId);
                pstmt.setDate(3, java.sql.Date.valueOf(checkIn));
                pstmt.setDate(4, java.sql.Date.valueOf(checkOut));
                pstmt.setDouble(5, totalPrice);
                
                if (guestName != null) pstmt.setString(6, guestName);
                else pstmt.setNull(6, java.sql.Types.VARCHAR);
                
                if (guestAge != null) pstmt.setInt(7, guestAge);
                else pstmt.setNull(7, java.sql.Types.INTEGER);
                
                if (specialRequests != null) pstmt.setString(8, specialRequests);
                else pstmt.setNull(8, java.sql.Types.VARCHAR);

                if (guestCount != null) pstmt.setInt(9, guestCount);
                else pstmt.setNull(9, java.sql.Types.INTEGER);

                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Map<String, Object>> getAllBookings() {
        List<Map<String, Object>> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, p.title as property_title, b.guest_name, b.guest_age, b.special_requests, b.guest_count, " +
                     "b.check_in_date, b.check_out_date, b.total_price, b.status " +
                     "FROM Bookings b " +
                     "JOIN Properties p ON b.property_id = p.property_id " +
                     "JOIN Users u ON b.guest_id = u.user_id " +
                     "ORDER BY b.created_at DESC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> b = new HashMap<>();
                b.put("bookingId", rs.getInt("booking_id"));
                b.put("propertyTitle", rs.getString("property_title"));
                b.put("guestName", rs.getString("guest_name"));
                b.put("guestAge", rs.getInt("guest_age"));
                b.put("specialRequests", rs.getString("special_requests"));
                b.put("guestCount", rs.getInt("guest_count"));
                b.put("checkIn", rs.getDate("check_in_date").toString());
                b.put("checkOut", rs.getDate("check_out_date").toString());
                b.put("totalPrice", rs.getDouble("total_price"));
                b.put("status", rs.getString("status"));
                bookings.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
}
