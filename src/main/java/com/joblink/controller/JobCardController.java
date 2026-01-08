package com.joblink.controller;

import com.joblink.dao.UserDAO;
import com.joblink.model.Application;
import com.joblink.model.Job;
import com.joblink.model.User;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class JobCardController {
    @FXML private Label titleLabel;
    @FXML private Label companyLabel;
    @FXML private Label salaryLabel;
    @FXML private Label locationLabel;
    @FXML private Label applicantsLabel;
    @FXML private Label appliedDateLabel;
    @FXML private Label statusLabel;
    @FXML private Button viewApplicantsButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button viewDetailsButton;
    
    @FXML
    public void initialize() {
        // Hide all buttons by default
        statusLabel.setVisible(false);
        statusLabel.setManaged(false);
        viewApplicantsButton.setVisible(false);
        viewApplicantsButton.setManaged(false);
        editButton.setVisible(false);
        editButton.setManaged(false);
        deleteButton.setVisible(false);
        deleteButton.setManaged(false);
        viewDetailsButton.setVisible(false);
        viewDetailsButton.setManaged(false);
    }
    
    // For MyJobPosts - show all management buttons
    public void setJobPostData(Job job, int applicantCount, Runnable onViewApplicants, Runnable onEdit, Runnable onDelete) {
        setBasicJobData(job, applicantCount);
        viewApplicantsButton.setVisible(true);
        viewApplicantsButton.setManaged(true);
        editButton.setVisible(true);
        editButton.setManaged(true);
        deleteButton.setVisible(true);
        deleteButton.setManaged(true);
        
        viewApplicantsButton.setOnAction(e -> onViewApplicants.run());
        editButton.setOnAction(e -> onEdit.run());
        deleteButton.setOnAction(e -> onDelete.run());
    }
    
    // For MyApplications - show application status and view details
    public void setApplicationData(Application application, Job job, Runnable onViewDetails) {
        setBasicJobData(job, 0);
        applicantsLabel.setVisible(false);
        applicantsLabel.setManaged(false);
        
        appliedDateLabel.setText("📅 Applied: " + application.getAppliedDate());
        appliedDateLabel.setVisible(true);
        appliedDateLabel.setManaged(true);
        
        String status = application.getStatus();
        String emoji = status.equalsIgnoreCase("pending") ? "⏳" : status.equalsIgnoreCase("accepted") ? "✓" : "✗";
        String color = status.equalsIgnoreCase("pending") ? "#FF9800" : status.equalsIgnoreCase("accepted") ? "#4CAF50" : "#f44336";
        String bgColor = status.equalsIgnoreCase("pending") ? "#FFF3E0" : status.equalsIgnoreCase("accepted") ? "#E8F5E9" : "#FFEBEE";
        
        statusLabel.setText(emoji + " " + status);
        statusLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: " + color + "; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-color: " + bgColor + "; -fx-background-radius: 5;");
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
        
        viewDetailsButton.setVisible(true);
        viewDetailsButton.setManaged(true);
        viewDetailsButton.setOnAction(e -> onViewDetails.run());
    }
    
    // For ApplyJobs - show view details button
    public void setAvailableJobData(Job job, int applicantCount, Runnable onViewDetails) {
        setBasicJobData(job, applicantCount);
        viewDetailsButton.setVisible(true);
        viewDetailsButton.setManaged(true);
        viewDetailsButton.setOnAction(e -> onViewDetails.run());
    }
    
    private void setBasicJobData(Job job, int applicantCount) {
        titleLabel.setText(job.getTitle());
        User poster = UserDAO.getUserById(job.getPostedBy());
        companyLabel.setText(poster.getName());
        salaryLabel.setText("💰 $" + job.getSalary());
        locationLabel.setText("📍 " + job.getLocation());
        if (applicantCount >= 0) {
            applicantsLabel.setText("👥 " + applicantCount + " Applicants");
            applicantsLabel.setVisible(true);
            applicantsLabel.setManaged(true);
        }
    }
}
