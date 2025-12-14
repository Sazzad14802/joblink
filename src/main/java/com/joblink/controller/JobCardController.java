package com.joblink.controller;

import com.joblink.model.Job;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class JobCardController {
    @FXML private Label titleLabel;
    @FXML private Label descLabel;
    @FXML private Label salaryLabel;
    @FXML private Label locationLabel;
    @FXML private Label applicantsLabel;
    @FXML private Button viewApplicantsButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    
    public void setData(Job job, int applicantCount, Runnable onViewApplicants, Runnable onEdit, Runnable onDelete) {
        titleLabel.setText(job.getTitle());
        descLabel.setText(job.getDescription());
        salaryLabel.setText("💰 $" + job.getSalary());
        locationLabel.setText("📍 " + job.getLocation());
        applicantsLabel.setText("👥 " + applicantCount + " Applicants");
        
        viewApplicantsButton.setOnAction(e -> onViewApplicants.run());
        editButton.setOnAction(e -> onEdit.run());
        deleteButton.setOnAction(e -> onDelete.run());
    }
}
