package com.joblink.model;

public class Application {
    private int id;
    private int jobId;
    private int userId;
    private String status; // pending, accepted, rejected
    private String appliedDate;
    private String cvFilePath;

    public Application() {
    }

    public Application(int id, int jobId, int userId, String status) {
        this.id = id;
        this.jobId = jobId;
        this.userId = userId;
        this.status = status;
    }
    
    public Application(int id, int jobId, int userId, String status, String appliedDate) {
        this.id = id;
        this.jobId = jobId;
        this.userId = userId;
        this.status = status;
        this.appliedDate = appliedDate;
    }
    
    public Application(int id, int jobId, int userId, String status, String appliedDate, String cvFilePath) {
        this.id = id;
        this.jobId = jobId;
        this.userId = userId;
        this.status = status;
        this.appliedDate = appliedDate;
        this.cvFilePath = cvFilePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getAppliedDate() {
        return appliedDate;
    }
    
    public void setAppliedDate(String appliedDate) {
        this.appliedDate = appliedDate;
    }
    
    public String getCvFilePath() {
        return cvFilePath;
    }
    
    public void setCvFilePath(String cvFilePath) {
        this.cvFilePath = cvFilePath;
    }
}
