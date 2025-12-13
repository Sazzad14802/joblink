package com.joblink;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.joblink.model.User;

public class MainApp extends Application {

    private static Scene scene;
    private static User currentUser;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("JobLink - Job Portal");
        scene = new Scene(loadFXML("home"), 900, 600);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) {
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

    public static void showHomePage() {
        setRoot("home");
    }

    public static void showAuthPage() {
        setRoot("auth");
    }

    public static void showCreateAccountPage() {
        setRoot("createAccount");
    }

    public static void showLoginPage() {
        setRoot("login");
    }

    public static void showDashboard() {
        setRoot("dashboard");
    }

    public static void showAdminDashboard() {
        setRoot("adminDashboard");
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void showPostJobPage() {

    }

    public static void showApplyJobPage() {

    }

    public static void showMyApplicationsPage() {

    }

    public static void showMyJobPostsPage() {
        
    }
}