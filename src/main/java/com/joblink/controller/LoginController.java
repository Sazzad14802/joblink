package com.joblink.controller;

import java.io.IOException;

import com.joblink.MainApp;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {
    
    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button submitButton;
    
    @FXML
    private Button cancelButton;
    
    
    @FXML
    private void handleSubmit() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Information", "Please enter both email and password.");
            return;
        }
        
    }
    
    @FXML
    private void handleCancel() throws IOException {
        MainApp.showAuthPage();
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
