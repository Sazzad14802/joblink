package com.joblink.controller;

import com.joblink.model.Application;
import com.joblink.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ApplicantCardController {
    @FXML private Label nameLabel;
    @FXML private Label statusBadge;
    @FXML private Label emailLabel;
    @FXML private Label appliedLabel;
    @FXML private Button viewFullExpButton;
    @FXML private Button rejectButton;
    @FXML private Button acceptButton;
    @FXML private HBox buttonContainer;
    
    private Application application;
    
    public void setData(Application application, User user, Runnable onAccept, Runnable onReject) {
        this.application = application;
        
        nameLabel.setText(user.getName());
        emailLabel.setText("📧 " + user.getEmail());
        appliedLabel.setText("📅 Applied: " + application.getAppliedDate());
        
        String exp = application.getExperience();
        if (exp == null || exp.trim().isEmpty()) {
            exp = "No experience provided";
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
        
        viewFullExpButton.setOnAction(e -> showFullExperience(user.getName()));
    }
    
    private void showFullExperience(String name) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Full Experience");
        alert.setHeaderText(name);
        alert.setContentText(application.getExperience());
        alert.showAndWait();
    }
}
