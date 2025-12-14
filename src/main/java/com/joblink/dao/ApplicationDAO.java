package com.joblink.dao;

import com.joblink.model.Application;
import com.joblink.util.DatabaseHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {
    
    public Application createApplication(int jobId, int userId) {
        return createApplication(jobId, userId, null);
    }
    
    public Application createApplication(int jobId, int userId, String experience) {
        String sql = "INSERT INTO applications (job_id, user_id, status, applied_date, experience) VALUES (?, ?, 'pending', datetime('now'), ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, jobId);
            pstmt.setInt(2, userId);
            pstmt.setString(3, experience);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // Use last_insert_rowid() for SQLite
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        // Get the applied_date
                        String dateSql = "SELECT applied_date FROM applications WHERE id = ?";
                        try (PreparedStatement dateStmt = conn.prepareStatement(dateSql)) {
                            dateStmt.setInt(1, id);
                            try (ResultSet dateRs = dateStmt.executeQuery()) {
                                if (dateRs.next()) {
                                    return new Application(id, jobId, userId, "pending", dateRs.getString("applied_date"), experience);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean hasUserApplied(int jobId, int userId) {
        String sql = "SELECT COUNT(*) FROM applications WHERE job_id = ? AND user_id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, jobId);
            pstmt.setInt(2, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public int getApplicationCount(int jobId) {
        String sql = "SELECT COUNT(*) FROM applications WHERE job_id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, jobId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public List<Application> getApplicationsByJob(int jobId) {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT * FROM applications WHERE job_id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, jobId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    applications.add(new Application(
                        rs.getInt("id"),
                        rs.getInt("job_id"),
                        rs.getInt("user_id"),
                        rs.getString("status"),
                        rs.getString("applied_date"),
                        rs.getString("experience")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }
    
    public List<Application> getApplicationsByUser(int userId) {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT * FROM applications WHERE user_id = ? ORDER BY applied_date DESC";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    applications.add(new Application(
                        rs.getInt("id"),
                        rs.getInt("job_id"),
                        rs.getInt("user_id"),
                        rs.getString("status"),
                        rs.getString("applied_date"),
                        rs.getString("experience")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }
    
    public Application getApplicationByJobAndUser(int jobId, int userId) {
        String sql = "SELECT * FROM applications WHERE job_id = ? AND user_id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, jobId);
            pstmt.setInt(2, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Application(
                        rs.getInt("id"),
                        rs.getInt("job_id"),
                        rs.getInt("user_id"),
                        rs.getString("status"),
                        rs.getString("applied_date"),
                        rs.getString("experience")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updateApplicationStatus(int applicationId, String status) {
        String sql = "UPDATE applications SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, applicationId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteApplicationsByJob(int jobId) {
        String sql = "DELETE FROM applications WHERE job_id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, jobId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
