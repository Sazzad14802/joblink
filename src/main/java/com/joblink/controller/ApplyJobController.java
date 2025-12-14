package com.joblink.controller;

import com.joblink.MainApp;
import com.joblink.dao.ApplicationDAO;
import com.joblink.dao.JobDAO;
import com.joblink.model.Job;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class ApplyJobController {
    
    @FXML
    private ListView<Job> jobsListView;
    
    @FXML
    private TextField minSalaryField;
    
    @FXML
    private Button applyFilterButton;
    
    @FXML
    private Button backButton;
    
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
                        int applicantCount = ApplicationDAO.getApplicationCount(job.getId());
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/joblink/jobCard.fxml"));
                        VBox card = loader.load();
                        JobCardController controller = loader.getController();
                        controller.setAvailableJobData(job, applicantCount, 
                            () -> MainApp.showJobDetailsPage(job, applicantCount));
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
        applyFilters();
    }
    
    @FXML
    private void handleApplyFilter() {
        applyFilters();
    }
    
    private void applyFilters() {
        double minSalary = 0;
        try {
            String text = minSalaryField.getText().trim();
            if (!text.isEmpty()) minSalary = Double.parseDouble(text);
        } catch (NumberFormatException e) {}
        
        List<Job> jobs = JobDAO.getAvailableJobsForUser(MainApp.getCurrentUser().getId(), minSalary);
        jobsListView.setItems(FXCollections.observableArrayList(jobs));
        if (jobs.isEmpty()) jobsListView.setPlaceholder(new Label("No jobs found matching your filters."));
    }
    
    @FXML
    private void handleBack() {
        MainApp.showDashboard();
    }
}
