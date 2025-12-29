package com.joblink;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.joblink.controller.ApplicantListController;
import com.joblink.model.Job;
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
    
    public static void showJobFormPage() {
        setRoot("jobForm");
    }
    public static void showJobFormPage(Job job) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/joblink/jobForm.fxml"));
            Parent root = loader.load();
            
            com.joblink.controller.JobFormController controller = loader.getController();
            controller.setJob(job);
            
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showJobDetailsPage(Job job, int applicantCount) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/joblink/jobDetails.fxml"));
            Parent root = loader.load();
            
            com.joblink.controller.JobDetailsController controller = loader.getController();
            controller.setJob(job, applicantCount);
            
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void showJobDetailsPage(Job job, int applicantCount, String previousPage) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/joblink/jobDetails.fxml"));
            Parent root = loader.load();
            
            com.joblink.controller.JobDetailsController controller = loader.getController();
            controller.setJob(job, applicantCount, previousPage);
            
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showApplyJobPage() {
        setRoot("applyJob");
    }

    public static void showMyApplicationsPage() {
        setRoot("myApplications");
    }
    public static void showApplicantListPage(Job job) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/joblink/applicantList.fxml"));
            Parent root = loader.load();
            
            ApplicantListController controller = loader.getController();
            controller.setJob(job);
            
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void showMyJobPostsPage() {
        setRoot("myJobPosts");
    }
}