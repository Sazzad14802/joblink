package com.joblink.controller;

import java.io.IOException;
import com.joblink.MainApp;
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
