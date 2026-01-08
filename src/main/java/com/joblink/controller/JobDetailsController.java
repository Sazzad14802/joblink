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
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private Button uploadButton;
    
    @FXML
    private Label fileNameLabel;
    
    @FXML
    private Button submitButton;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private Button backButton;
    
    private Job job;
    private String previousPage = "applyJob"; // default to apply job page
    private File selectedCvFile = null;
    
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
    private void handleChooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Your CV (PDF)");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        
        File file = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        if (file != null) {
            selectedCvFile = file;
            fileNameLabel.setText(file.getName());
            fileNameLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 14px; -fx-font-weight: bold;");
        }
    }
    
    @FXML
    private void handleSubmitApplication() {
        if (selectedCvFile == null) {
            showAlert(Alert.AlertType.WARNING, "CV Required", "Please upload your CV (PDF format) to apply for this job.");
            return;
        }
        
        try {
            // Copy CV to secure location
            String cvFilePath = copyCvToSecureLocation(selectedCvFile);
            
            if (cvFilePath == null) {
                showAlert(Alert.AlertType.ERROR, "File Copy Failed", "Failed to save your CV. Please try again.");
                return;
            }
            
            Application application = ApplicationDAO.createApplication(job.getId(), MainApp.getCurrentUser().getId(), cvFilePath);
            if (application != null) {
                showAlert(Alert.AlertType.INFORMATION, "Application Submitted", "Your application with CV has been submitted successfully!");
                selectedCvFile = null;
                fileNameLabel.setText("No file selected");
                fileNameLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 14px;");
                updateUI();
            } else {
                showAlert(Alert.AlertType.ERROR, "Application Failed", "Failed to submit application. Please try again.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
        }
    }
    
    private String copyCvToSecureLocation(File sourceFile) {
        try {
            // Create cv_uploads directory structure: cv_uploads/userId/
            int userId = MainApp.getCurrentUser().getId();
            Path uploadDir = Paths.get("cv_uploads", String.valueOf(userId));
            Files.createDirectories(uploadDir);
            
            // Generate unique filename: jobId_timestamp.pdf
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = String.format("job%d_%s.pdf", job.getId(), timestamp);
            Path destinationPath = uploadDir.resolve(fileName);
            
            // Copy file to secure location
            Files.copy(sourceFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            
            // Return relative path for storage
            return destinationPath.toString();
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
