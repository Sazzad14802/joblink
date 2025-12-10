package com.joblink.controller;

import java.io.IOException;

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
    private void handleCreateAccount() throws IOException {
        MainApp.showCreateAccountPage();
    }
    
    @FXML
    private void handleLogin() throws IOException {
        MainApp.showLoginPage();
    }
    
    @FXML
    private void handleBack() throws IOException {
        MainApp.showHomePage();
    }
}
