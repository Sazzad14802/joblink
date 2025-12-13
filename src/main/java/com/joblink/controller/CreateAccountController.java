package com.joblink.controller;

import com.joblink.MainApp;
import com.joblink.dao.UserDAO;
import com.joblink.model.User;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CreateAccountController {
    
    @FXML
    private Label nameLabel;
    
    @FXML
    private TextField nameField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private ComboBox<String> accountTypeComboBox;
    
    @FXML
    private Button submitButton;
    
    @FXML
    private Button cancelButton;

    
    @FXML
    public void initialize() {

        accountTypeComboBox.getItems().addAll("Job Seeker", "Employer");
        accountTypeComboBox.setValue("Job Seeker");
        
        accountTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("Employer".equals(newVal)) {
                nameLabel.setText("Company Name:");
            } else {
                nameLabel.setText("Name:");
            }
        });
    }
    
    @FXML
    private void handleSubmitHover() {
        submitButton.setStyle("-fx-background-color: linear-gradient(to right, #5568d3, #6a42a0); -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 14; -fx-background-radius: 8; -fx-cursor: hand;");
    }
    
    @FXML
    private void handleSubmitExit() {
        submitButton.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2); -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 14; -fx-background-radius: 8; -fx-cursor: hand;");
    }

    @FXML
    private void handleSubmit() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String accountTypeDisplay = accountTypeComboBox.getValue();
        
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || accountTypeDisplay == null) {
            showAlert(Alert.AlertType.ERROR, "Missing Information", "Please fill in all required fields.");
            return;
        }
        String accountType = accountTypeDisplay.equals("Job Seeker") ? "SEEKER" : "EMPLOYER";
        
        // Check if email already exists
        if (UserDAO.emailExists(email)) {
            showAlert(Alert.AlertType.ERROR, "Email Already Registered", "This email address is already registered. Please use a different email or try logging in.");
            return;
        }
        
        try {
            User user = UserDAO.createUser(name, email, password, accountType);
            if (user != null) {
                showAlert(Alert.AlertType.INFORMATION, "Account Created", "Your account has been created successfully! You can now log in.");
                MainApp.showAuthPage();
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "Unable to create account. Please check the console for errors.");
                System.err.println("Failed to create user - returned null");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
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
