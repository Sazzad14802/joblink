package com.joblink.dao;

import com.joblink.model.Job;
import com.joblink.util.DatabaseHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDAO {
    
    public static Job createJob(String title, String description, double salary, String location, int postedBy) {
        String sql = "INSERT INTO jobs (title, description, salary, location, posted_by) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setDouble(3, salary);
            pstmt.setString(4, location);
            pstmt.setInt(5, postedBy);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // Use last_insert_rowid() for SQLite
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        return new Job(id, title, description, salary, location, postedBy);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                jobs.add(new Job(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getDouble("salary"),
                    rs.getString("location"),
                    rs.getInt("posted_by")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobs;
    }
    
    public static List<Job> getJobsByUser(int userId) {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs WHERE posted_by = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    jobs.add(new Job(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDouble("salary"),
                        rs.getString("location"),
                        rs.getInt("posted_by")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobs;
    }
    
    public static boolean deleteJob(int jobId) {
        String sql = "DELETE FROM jobs WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, jobId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateJob(int jobId, String title, String description, double salary, String location) {
        String sql = "UPDATE jobs SET title = ?, description = ?, salary = ?, location = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setDouble(3, salary);
            pstmt.setString(4, location);
            pstmt.setInt(5, jobId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static Job getJobById(int jobId) {
        String sql = "SELECT * FROM jobs WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, jobId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Job(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDouble("salary"),
                        rs.getString("location"),
                        rs.getInt("posted_by")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
