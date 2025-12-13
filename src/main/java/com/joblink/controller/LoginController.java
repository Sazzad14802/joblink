package com.joblink.controller;

import java.io.IOException;

import com.joblink.MainApp;
import com.joblink.dao.UserDAO;
import com.joblink.model.User;

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
    private void handleSubmit() throws IOException {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Information", "Please enter both email and password.");
            return;
        }
        User user = UserDAO.authenticate(email, password);
        if (user != null) {
            MainApp.setCurrentUser(user);
            // Redirect admin to admin dashboard
            if (user.isAdmin()) {
                MainApp.showAdminDashboard();
            } else {
                MainApp.showDashboard();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password. Please try again.");
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
