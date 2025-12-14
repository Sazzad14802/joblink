package com.joblink.controller;

import com.joblink.MainApp;
import com.joblink.dao.ApplicationDAO;
import com.joblink.dao.JobDAO;
import com.joblink.model.Application;
import com.joblink.model.Job;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

public class MyApplicationsController {
    
    @FXML
    private ListView<Application> applicationsListView;
    
    @FXML
    private ComboBox<String> filterComboBox;
    
    @FXML
    private Button backButton;

    private List<Application> allApplications;
    
    @FXML
    public void initialize() {
        // Initialize filter combo box
        filterComboBox.setItems(FXCollections.observableArrayList("All", "Pending", "Accepted", "Rejected"));
        filterComboBox.setValue("All");
        
        // Add listener to filter combo box for immediate filtering
        filterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        
        setupListView();
        loadApplications();
    }
    
    private void setupListView() {
        applicationsListView.setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-border-width: 0;");
        
        applicationsListView.setCellFactory(lv -> new ListCell<Application>() {
            @Override
            protected void updateItem(Application app, boolean empty) {
                super.updateItem(app, empty);
                if (empty || app == null) {
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    try {
                        Job job = JobDAO.getJobById(app.getJobId());
                        if (job != null) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/joblink/jobCard.fxml"));
                            VBox card = loader.load();
                            JobCardController controller = loader.getController();
                            controller.setApplicationData(app, job, () -> {
                                int applicantCount = ApplicationDAO.getApplicationCount(job.getId());
                                MainApp.showJobDetailsPage(job, applicantCount, "myApplications");
                            });
                            setGraphic(card);
                            setStyle("-fx-background-color: transparent; -fx-padding: 5;");
                        } else {
                            setGraphic(new Label("Job not found"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        setGraphic(new Label("Error loading card"));
                    }
                }
            }
        });
    }
    
    private void loadApplications() {
        allApplications = ApplicationDAO.getApplicationsByUser(MainApp.getCurrentUser().getId());
        applyFilters();
    }
    
    private void applyFilters() {
        String filter = filterComboBox.getValue();
        
        List<Application> filteredApplications = allApplications.stream()
            .filter(app -> {
                if ("All".equals(filter)) {
                    return true;
                } else {
                    return app.getStatus().equalsIgnoreCase(filter);
                }
            })
            .collect(Collectors.toList());
        
        ObservableList<Application> applicationsList = FXCollections.observableArrayList(filteredApplications);
        applicationsListView.setItems(applicationsList);
        
        if (filteredApplications.isEmpty()) {
            applicationsListView.setPlaceholder(new Label("No applications found."));
        }
    }
    
    @FXML
    private void handleBack() {
        MainApp.showDashboard();
    }
}
