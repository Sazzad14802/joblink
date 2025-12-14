package com.joblink.controller;

import com.joblink.MainApp;
import com.joblink.dao.ApplicationDAO;
import com.joblink.dao.UserDAO;
import com.joblink.model.Application;
import com.joblink.model.Job;
import com.joblink.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class JobDetailsController {
    
    @FXML
    private Label titleLabel;
    
    @FXML
    private Label salaryLabel;
    
    @FXML
    private Label locationLabel;
    
    @FXML
    private Label applicantsLabel;
    
    @FXML
    private TextArea descriptionArea;
    
    @FXML
    private Label posterNameLabel;
    
    @FXML
    private Label posterEmailLabel;
    
    @FXML
    private VBox experienceBox;
    
    @FXML
    private TextArea experienceArea;
    
    @FXML
    private Button submitButton;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private Button backButton;
    
    private Job job;
    private String previousPage = "applyJob"; // default to apply job page
    
    public void setJob(Job job, int applicantCount) {
        this.job = job;
        
        // Set job details
        titleLabel.setText(job.getTitle());
        titleLabel.setStyle("-fx-text-fill: #2196F3;");
        
        salaryLabel.setText(String.format("$%.2f", job.getSalary()));
        salaryLabel.setStyle("-fx-text-fill: green;");
        
        locationLabel.setText(job.getLocation());
        applicantsLabel.setText(String.valueOf(applicantCount));
        descriptionArea.setText(job.getDescription());
        
        // Get and display poster information
        User poster = UserDAO.getUserById(job.getPostedBy());
        if (poster != null) {
            posterNameLabel.setText(poster.getName());
            posterEmailLabel.setText(poster.getEmail());
        }
        
        // Update UI based on application status
        updateUI();
    }
    
    public void setJob(Job job, int applicantCount, String previousPage) {
        this.previousPage = previousPage;
        setJob(job, applicantCount);
    }
    
    private void updateUI() {
        if (job.getPostedBy() == MainApp.getCurrentUser().getId()) {
            // User's own job - hide experience section
            experienceBox.setVisible(false);
            experienceBox.setManaged(false);
            statusLabel.setVisible(true);
            statusLabel.setText("This is your job posting");
            statusLabel.setStyle("-fx-text-fill: gray;");
        } else {
            // Check if user has applied
            Application app = ApplicationDAO.getApplicationByJobAndUser(
                job.getId(), 
                MainApp.getCurrentUser().getId()
            );
            
            if (app == null) {
                // Not applied yet - show experience section
                experienceBox.setVisible(true);
                experienceBox.setManaged(true);
                statusLabel.setVisible(false);
            } else {
                // Already applied - hide experience section and show status
                experienceBox.setVisible(false);
                experienceBox.setManaged(false);
                statusLabel.setVisible(true);
                
                switch (app.getStatus()) {
                    case "accepted":
                        statusLabel.setText("Application Accepted");
                        statusLabel.setStyle("-fx-text-fill: green;");
                        break;
                    case "rejected":
                        statusLabel.setText("Application Rejected");
                        statusLabel.setStyle("-fx-text-fill: red;");
                        break;
                    default: // pending
                        statusLabel.setText("Application Pending");
                        statusLabel.setStyle("-fx-text-fill: orange;");
                        break;
                }
            }
        }
    }
    
    @FXML
    private void handleSubmitApplication() {
        String experience = experienceArea.getText();
        
        if (experience == null || experience.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Experience Required", "Please describe your experience to apply for this job.");
            return;
        }
        
        Application application = ApplicationDAO.createApplication(job.getId(), MainApp.getCurrentUser().getId(), experience.trim());
        if (application != null) {
            showAlert(Alert.AlertType.INFORMATION, "Application Submitted", "Your application has been submitted successfully!");
            experienceArea.clear();
            updateUI();
        } else {
            showAlert(Alert.AlertType.ERROR, "Application Failed", "Failed to submit application. Please try again.");
        }
    }
    
    @FXML
    private void handleBack() {
        if ("myApplications".equals(previousPage)) {
            MainApp.showMyApplicationsPage();
        } else {
            MainApp.showApplyJobPage();
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
