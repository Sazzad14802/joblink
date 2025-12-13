package com.joblink.controller;

import com.joblink.MainApp;
import com.joblink.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DashboardController {
    
    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Button postJobButton;
    
    @FXML
    private Button applyJobButton;
    
    @FXML
    private Button myApplicationsButton;
    
    @FXML
    private Button myJobPostsButton;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    public void initialize() {
        User currentUser = MainApp.getCurrentUser();
        if (currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getName() + "!");
            
            // Show/hide buttons based on account type
            if (currentUser.isEmployer()) {
                // Employer can only post jobs and view their posts
                postJobButton.setVisible(true);
                postJobButton.setManaged(true);
                myJobPostsButton.setVisible(true);
                myJobPostsButton.setManaged(true);
                
                // Employer cannot apply for jobs or view applications
                applyJobButton.setVisible(false);
                applyJobButton.setManaged(false);
                myApplicationsButton.setVisible(false);
                myApplicationsButton.setManaged(false);
            } else {
                // Job Seeker can only apply for jobs and view applications
                applyJobButton.setVisible(true);
                applyJobButton.setManaged(true);
                myApplicationsButton.setVisible(true);
                myApplicationsButton.setManaged(true);
                
                // Job Seeker cannot post jobs
                postJobButton.setVisible(false);
                postJobButton.setManaged(false);
                myJobPostsButton.setVisible(false);
                myJobPostsButton.setManaged(false);
            }
        }
    }
    
    @FXML
    private void handlePostJob() {
        MainApp.showPostJobPage();
    }
    
    @FXML
    private void handleApplyJob() {
        MainApp.showApplyJobPage();
    }
    
    @FXML
    private void handleMyApplications() {
        MainApp.showMyApplicationsPage();
    }
    
    @FXML
    private void handleMyJobPosts() {
        MainApp.showMyJobPostsPage();
    }
    
    @FXML
    private void handleLogout() {
        MainApp.setCurrentUser(null);
        MainApp.showHomePage();
    }
}
