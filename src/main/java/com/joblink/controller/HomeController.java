package com.joblink.controller;

import java.io.IOException;

import com.joblink.MainApp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomeController {
    
    @FXML
    private Button enterPortalButton;
    
    @FXML
    private void handleEnterPortal() throws IOException {
        MainApp.showAuthPage();
    }
}
