package com.joblink.controller;

import com.joblink.MainApp;
import com.joblink.dao.UserDAO;
import com.joblink.model.User;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.regex.Pattern;

public class CreateAccountController {
    
    // Email regex pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    @FXML
    private Label nameLabel;
    
    @FXML
    private TextField nameField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Label lengthRequirementLabel;
    
    @FXML
    private Label uppercaseRequirementLabel;
        
    @FXML
    private Label numberRequirementLabel;
    
    @FXML
    private Label symbolRequirementLabel;
    
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
        
        // Add password validation listener
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> {
            validatePassword(newVal);
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
        
        // Validate email format
        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return;
        }
        
        // Validate password strength
        String passwordError = getPasswordStrengthError(password);
        if (passwordError != null) {
            showAlert(Alert.AlertType.ERROR, "Weak Password", passwordError);
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
    
    private void validatePassword(String password) {
        if (password.isEmpty()) {
            // Reset all to gray with X marks
            updateRequirementLabel(lengthRequirementLabel, false, "Min 6 chars");
            updateRequirementLabel(uppercaseRequirementLabel, false, "Uppercase");
            updateRequirementLabel(numberRequirementLabel, false, "Number");
            updateRequirementLabel(symbolRequirementLabel, false, "Symbol");
            passwordField.setStyle("-fx-background-color: #f7fafc; -fx-border-color: #e2e8f0; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12; -fx-font-size: 14px;");
            return;
        }
        
        boolean hasMinLength = password.length() >= 6;
        boolean hasUpperCase = password.matches(".*[A-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        boolean hasSymbol = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
        
        // Update each requirement label with appropriate color
        updateRequirementLabel(lengthRequirementLabel, hasMinLength, "Min 6 chars");
        updateRequirementLabel(uppercaseRequirementLabel, hasUpperCase, "Uppercase");
        updateRequirementLabel(numberRequirementLabel, hasNumber, "Number");
        updateRequirementLabel(symbolRequirementLabel, hasSymbol, "Symbol");
        
        // Update border based on overall strength
        boolean allMet = hasMinLength && hasUpperCase && hasNumber && hasSymbol;
        if (allMet) {
            passwordField.setStyle("-fx-background-color: #f7fafc; -fx-border-color: #38a169; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12; -fx-font-size: 14px;");
        } else {
            passwordField.setStyle("-fx-background-color: #f7fafc; -fx-border-color: #e53e3e; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12; -fx-font-size: 14px;");
        }
    }
    
    private void updateRequirementLabel(Label label, boolean isMet, String requirement) {
        if (isMet) {
            label.setText("✓ " + requirement);
            label.setStyle("-fx-text-fill: #38a169; -fx-font-size: 11px;");
        } else {
            label.setText("✗ " + requirement);
            label.setStyle("-fx-text-fill: #e53e3e; -fx-font-size: 11px;");
        }
    }
    
    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    private String getPasswordStrengthError(String password) {
        if (password.length() < 6) {
            return "Password must be at least 6 characters long.";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter.";
        }
        if (!password.matches(".*[0-9].*")) {
            return "Password must contain at least one number.";
        }
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return "Password must contain at least one symbol (!@#$%^&*(),.?\":{}|<>).";
        }
        return null;
    }
    
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
