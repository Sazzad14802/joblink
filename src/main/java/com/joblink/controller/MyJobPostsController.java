package com.joblink.controller;

import com.joblink.MainApp;
import com.joblink.dao.ApplicationDAO;
import com.joblink.dao.JobDAO;
import com.joblink.model.Job;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class MyJobPostsController {
    
    @FXML
    private ListView<Job> jobsListView;
    
    @FXML
    private Button backButton;
    
    private ApplicationDAO applicationDAO = new ApplicationDAO();
    
    @FXML
    public void initialize() {
        setupListView();
        loadJobs();
    }
    
    private void setupListView() {
        jobsListView.setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-border-width: 0;");
        
        jobsListView.setCellFactory(lv -> new ListCell<Job>() {
            @Override
            protected void updateItem(Job job, boolean empty) {
                super.updateItem(job, empty);
                if (empty || job == null) {
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    try {
                        int applicantCount = applicationDAO.getApplicationCount(job.getId());
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/joblink/jobCard.fxml"));
                        VBox card = loader.load();
                        JobCardController controller = loader.getController();
                        controller.setData(job, applicantCount, 
                            () -> MainApp.showApplicantListPage(job),
                            () -> handleEdit(job),
                            () -> handleDelete(job));
                        setGraphic(card);
                        setStyle("-fx-background-color: transparent; -fx-padding: 5;");
                    } catch (Exception e) {
                        e.printStackTrace();
                        setGraphic(new Label("Error loading card"));
                    }
                }
            }
        });
    }
    
    private void loadJobs() {
        List<Job> jobs = JobDAO.getJobsByUser(MainApp.getCurrentUser().getId());
        ObservableList<Job> jobsList = FXCollections.observableArrayList(jobs);
        jobsListView.setItems(jobsList);
        
        if (jobs.isEmpty()) {
            jobsListView.setPlaceholder(new Label("No job posts yet. Start by posting a job!"));
        }
    }
    
    private void handleDelete(Job job) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Job Posting");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete this job posting?\n\nThis will also remove all applications for this position. This action cannot be undone.");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                applicationDAO.deleteApplicationsByJob(job.getId());
                boolean deleted = JobDAO.deleteJob(job.getId());
                if (deleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Job Deleted", "Job posting and all related applications have been deleted successfully.");
                    loadJobs();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Deletion Failed", "Failed to delete job posting. Please try again.");
                }
            }
        });
    }
    
    private void handleEdit(Job job) {
        MainApp.showJobFormPage(job);
    }
    
    @FXML
    private void handleBack() {
        MainApp.showDashboard();
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
