package com.joblink.controller;

import com.joblink.MainApp;
import com.joblink.dao.ApplicationDAO;
import com.joblink.dao.UserDAO;
import com.joblink.model.Application;
import com.joblink.model.Job;
import com.joblink.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class ApplicantListController {
    
    @FXML
    private Label jobTitleLabel;
    
    @FXML
    private VBox applicantsContainer;
    
    @FXML
    private Button backButton;
    
    private Job currentJob;
    
    public void setJob(Job job) {
        this.currentJob = job;
        jobTitleLabel.setText(job.getTitle());
        loadApplicants();
    }
    
    private void loadApplicants() {
        if (currentJob == null) return;
        
        applicantsContainer.getChildren().clear();
        
        List<Application> applications = ApplicationDAO.getApplicationsByJob(currentJob.getId());
        List<Application> pendingApps = applications.stream()
            .filter(app -> "pending".equals(app.getStatus()))
            .toList();
        
        if (pendingApps.isEmpty()) {
            Label emptyLabel = new Label("No pending applications.");
            emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
            VBox emptyBox = new VBox(emptyLabel);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(50));
            applicantsContainer.getChildren().add(emptyBox);
        } else {
            for (Application app : pendingApps) {
                User user = UserDAO.getUserById(app.getUserId());
                if (user != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/joblink/applicantCard.fxml"));
                        VBox card = loader.load();
                        ApplicantCardController controller = loader.getController();
                        controller.setData(app, user,
                            () -> handleAccept(app),
                            () -> handleReject(app));
                        applicantsContainer.getChildren().add(card);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    private void handleAccept(Application app) {
        boolean updated = ApplicationDAO.updateApplicationStatus(app.getId(), "accepted");
        if (updated) {
            showAlert(Alert.AlertType.INFORMATION, "Applicant Accepted", "The applicant has been called for an interview. They will see the updated status.");
            loadApplicants();
        } else {
            showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update applicant status. Please try again.");
        }
    }
    
    private void handleReject(Application app) {
        boolean updated = ApplicationDAO.updateApplicationStatus(app.getId(), "rejected");
        if (updated) {
            showAlert(Alert.AlertType.INFORMATION, "Applicant Rejected", "The applicant has been notified of the rejection.");
            loadApplicants();
        } else {
            showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update applicant status. Please try again.");
        }
    }
    
    @FXML
    private void handleBack() {
        MainApp.showMyJobPostsPage();
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
