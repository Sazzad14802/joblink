package com.joblink.controller;

import com.joblink.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AuthController {
    
    @FXML
    private Button createAccountButton;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Button backButton;
    
    @FXML
    private void handleCreateAccount() {
        MainApp.showCreateAccountPage();
    }
    
    @FXML
    private void handleLogin() {
        MainApp.showLoginPage();
    }
    
    @FXML
    private void handleBack() {
        MainApp.showHomePage();
    }
}
