package com.joblink.controller;

import com.joblink.model.Application;
import com.joblink.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.awt.Desktop;
import java.io.File;

public class ApplicantCardController {
    @FXML private Label nameLabel;
    @FXML private Label statusBadge;
    @FXML private Label emailLabel;
    @FXML private Label appliedLabel;
    @FXML private Button downloadCvButton;
    @FXML private Button rejectButton;
    @FXML private Button acceptButton;
    @FXML private HBox buttonContainer;
    
    private Application application;
    
    public void setData(Application application, User user, Runnable onAccept, Runnable onReject) {
        this.application = application;
        
        nameLabel.setText(user.getName());
        emailLabel.setText("📧 " + user.getEmail());
        appliedLabel.setText("📅 Applied: " + application.getAppliedDate());
        
        String cvPath = application.getCvFilePath();
        if (cvPath == null || cvPath.trim().isEmpty()) {
            downloadCvButton.setDisable(true);
            downloadCvButton.setText("📄 No CV Uploaded");
        } else {
            downloadCvButton.setDisable(false);
            downloadCvButton.setText("📄 Download CV");
            downloadCvButton.setOnAction(e -> downloadCV(cvPath, user.getName()));
        }
        
        String status = application.getStatus();
        String emoji = status.equalsIgnoreCase("pending") ? "⏳" : status.equalsIgnoreCase("accepted") ? "✓" : "✗";
        statusBadge.setText(emoji + " " + status);
        
        // Show buttons only for pending applications
        if (status.equalsIgnoreCase("pending")) {
            buttonContainer.setVisible(true);
            buttonContainer.setManaged(true);
            acceptButton.setOnAction(e -> onAccept.run());
            rejectButton.setOnAction(e -> onReject.run());
        } else {
            buttonContainer.setVisible(false);
            buttonContainer.setManaged(false);
            
            if (status.equalsIgnoreCase("accepted")) {
                statusBadge.setStyle("-fx-text-fill: #4CAF50;");
            } else if (status.equalsIgnoreCase("rejected")) {
                statusBadge.setStyle("-fx-text-fill: #f44336;");
            }
        }
    }
    
    private void downloadCV(String cvFilePath, String applicantName) {
        File cvFile = new File(cvFilePath);
        
        if (!cvFile.exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("CV Not Found");
            alert.setHeaderText("File Missing");
            alert.setContentText("The CV file could not be found. It may have been moved or deleted.");
            alert.showAndWait();
            return;
        }
        
        try {
            // Open the PDF file with the default system application
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(cvFile);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Cannot Open File");
                alert.setHeaderText("Desktop Not Supported");
                alert.setContentText("Unable to open the CV file on this system.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Opening CV");
            alert.setHeaderText("Failed to Open File");
            alert.setContentText("An error occurred while trying to open the CV: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
