package com.joblink.controller;

import com.joblink.MainApp;
import com.joblink.dao.UserDAO;
import com.joblink.model.User;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class AdminDashboardController {
    
    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Label totalUsersLabel;
    
    @FXML
    private TableView<UserDisplay> usersTable;
    
    @FXML
    private TableColumn<UserDisplay, Integer> idColumn;
    
    @FXML
    private TableColumn<UserDisplay, String> nameColumn;
    
    @FXML
    private TableColumn<UserDisplay, String> emailColumn;
    
    @FXML
    private TableColumn<UserDisplay, String> accountTypeColumn;
    
    @FXML
    private TableColumn<UserDisplay, Integer> jobsColumn;
    
    @FXML
    private TableColumn<UserDisplay, Integer> applicationsColumn;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private Button refreshButton;
    
    @FXML
    private Button logoutButton;
        private ObservableList<UserDisplay> usersList = FXCollections.observableArrayList();
    
    @FXML
    private void initialize() {
        User currentUser = MainApp.getCurrentUser();
        if (currentUser != null) {
            welcomeLabel.setText("Admin Dashboard - Welcome, " + currentUser.getName());
        }
        
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        accountTypeColumn.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        jobsColumn.setCellValueFactory(new PropertyValueFactory<>("jobsCount"));
        applicationsColumn.setCellValueFactory(new PropertyValueFactory<>("applicationsCount"));
        
        // Load users data
        loadUsers();
        
        // Disable delete button if no selection
        deleteButton.setDisable(true);
        usersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            deleteButton.setDisable(newSelection == null);
        });
    }
    
    private void loadUsers() {
        usersList.clear();
        var users = UserDAO.getAllUsers();
        
        for (User user : users) {
            int jobsCount = UserDAO.getUserJobCount(user.getId());
            int applicationsCount = UserDAO.getUserApplicationCount(user.getId());
            usersList.add(new UserDisplay(user, jobsCount, applicationsCount));
        }
        
        usersTable.setItems(usersList);
        totalUsersLabel.setText(String.valueOf(users.size()));
    }
    
    @FXML
    private void handleDeleteUser() {
        UserDisplay selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to delete.");
            return;
        }
        
        // Confirm deletion
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete User Account");
        confirmAlert.setContentText("Are you sure you want to delete user '" + selectedUser.getName() + "'?\n\n" +
                "This will also delete:\n" +
                "- " + selectedUser.getJobsCount() + " job posting(s)\n" +
                "- " + "all the applications related to those jobs\n\n" +
                "This action cannot be undone.");
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean deleted = UserDAO.deleteUser(selectedUser.getId());
            if (deleted) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "User account deleted successfully.");
                loadUsers(); // Refresh the table
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete user account.");
            }
        }
    }
    
    @FXML
    private void handleRefresh() {
        loadUsers();
        showAlert(Alert.AlertType.INFORMATION, "Refreshed", "User list has been refreshed.");
    }
    
    @FXML
    private void handleLogout() {
        MainApp.setCurrentUser(null);
        MainApp.showHomePage();
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Inner class for table display
    public static class UserDisplay {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty email;
        private final SimpleStringProperty accountType;
        private final SimpleIntegerProperty jobsCount;
        private final SimpleIntegerProperty applicationsCount;
        
        public UserDisplay(User user, int jobsCount, int applicationsCount) {
            this.id = new SimpleIntegerProperty(user.getId());
            this.name = new SimpleStringProperty(user.getName());
            this.email = new SimpleStringProperty(user.getEmail());
            this.accountType = new SimpleStringProperty(user.getAccountType());
            this.jobsCount = new SimpleIntegerProperty(jobsCount);
            this.applicationsCount = new SimpleIntegerProperty(applicationsCount);
        }
        
        public int getId() {
            return id.get();
        }
        
        public String getName() {
            return name.get();
        }
        
        public String getEmail() {
            return email.get();
        }
        
        public String getAccountType() {
            return accountType.get();
        }
        
        public int getJobsCount() {
            return jobsCount.get();
        }
        
        public int getApplicationsCount() {
            return applicationsCount.get();
        }
    }
}
