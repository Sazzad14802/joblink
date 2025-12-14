package com.joblink.controller;

import com.joblink.MainApp;
import com.joblink.dao.JobDAO;
import com.joblink.model.Job;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class JobFormController {
    
    @FXML
    private Label titleLabel;
    
    @FXML
    private TextField titleField;
    
    @FXML
    private TextArea descriptionArea;
    
    @FXML
    private TextField salaryField;
    
    @FXML
    private TextField locationField;
    
    @FXML
    private Button submitButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Button backButton;

    private Job existingJob; // null = create mode, not null = edit mode
    
    @FXML
    private void initialize() {
        // Default to create mode
        titleLabel.setText("Post a Job");
        submitButton.setText("Post Job");
    }
    
    public void setJob(Job job) {
        this.existingJob = job;
        
        if (job != null) {
            // Edit mode - pre-fill fields
            titleLabel.setText("Edit Job Posting");
            submitButton.setText("Update Job");
            
            titleField.setText(job.getTitle());
            descriptionArea.setText(job.getDescription());
            salaryField.setText(String.valueOf(job.getSalary()));
            locationField.setText(job.getLocation());
        }
    }
    
    @FXML
    private void handleSubmit() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String salaryText = salaryField.getText().trim();
        String location = locationField.getText().trim();
        
        if (title.isEmpty() || description.isEmpty() || salaryText.isEmpty() || location.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Information", "Please fill in all required fields.");
            return;
        }
        
        double salary;
        try {
            salary = Double.parseDouble(salaryText);
            if (salary < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Salary", "Please enter a valid positive number for salary.");
            return;
        }
        
        if (existingJob == null) {
            // Create mode
            Job job = JobDAO.createJob(title, description, salary, location, MainApp.getCurrentUser().getId());
            if (job != null) {
                showAlert(Alert.AlertType.INFORMATION, "Job Posted", "Your job posting has been created successfully!");
                MainApp.showDashboard();
            } else {
                showAlert(Alert.AlertType.ERROR, "Posting Failed", "Failed to create job posting. Please try again.");
            }
        } else {
            // Edit mode
            boolean updated = JobDAO.updateJob(existingJob.getId(), title, description, salary, location);
            if (updated) {
                showAlert(Alert.AlertType.INFORMATION, "Job Updated", "Your job posting has been updated successfully!");
                MainApp.showMyJobPostsPage();
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update job posting. Please try again.");
            }
        }
    }
    
    @FXML
    private void handleCancel() {
        if (existingJob == null) {
            MainApp.showDashboard();
        } else {
            MainApp.showMyJobPostsPage();
        }
    }
    
    @FXML
    private void handleBack() {
        if (existingJob == null) {
            MainApp.showDashboard();
        } else {
            MainApp.showMyJobPostsPage();
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
