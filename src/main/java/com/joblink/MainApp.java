package com.joblink;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class MainApp extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("JobLink - Job Portal");
        scene = new Scene(loadFXML("home"), 900, 600);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        try {
            scene.setRoot(loadFXML(fxml));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void showHomePage() throws IOException {
        setRoot("home");
    }
    
    public static void showAuthPage() throws IOException {
        setRoot("auth");
    }
    
    public static void showCreateAccountPage() throws IOException {
        setRoot("createAccount");
    }
    
    public static void showLoginPage() throws IOException {
        setRoot("login");
    }
    
    public static void showDashboard() throws IOException {
        setRoot("dashboard");
    }

}