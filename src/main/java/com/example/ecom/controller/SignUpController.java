package com.example.ecom.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.User;
import manager.UserManager;
import com.example.ecom.App;
import util.Dialog;

public class SignUpController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField phoneField;

    @FXML
    public void onSignUp(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String phone = phoneField.getText().trim();

        // Validate input fields
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty() || phone.isEmpty()) {
            Dialog.showErrorAlert("Registration Error", "All fields are required.");
            return;
        }

        // Validate password confirmation
        if (!password.equals(confirmPassword)) {
            Dialog.showErrorAlert("Registration Error", "Passwords do not match.");
            return;
        }

        // Validate password strength
        if (password.length() < 8) {
            Dialog.showErrorAlert("Registration Error", "Password must be at least 8 characters long.");
            return;
        }

        // Validate email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            Dialog.showErrorAlert("Registration Error", "Please enter a valid email address.");
            return;
        }

        // Validate phone number format (simple check for now)
        if (!phone.matches("\\d{10}")) {
            Dialog.showErrorAlert("Registration Error", "Please enter a valid 10-digit phone number.");
            return;
        }

        try {
            // Register the user as a CUSTOMER
            UserManager.register(username, password, email, fullName, phone, User.Role.CUSTOMER);
            
            // Show success message
            Dialog.showInformationAlert("Registration Successful", 
                "Your account has been created successfully. Please log in.");
            
            // Return to login screen
            App.showLoginScreen();
            
        } catch (Exception e) {
            if (e.getMessage().contains("already taken")) {
                Dialog.showErrorAlert("Registration Error", 
                    "Username '" + username + "' is already taken. Please choose a different username.");
            } else {
                Dialog.showErrorAlert("Registration Error", 
                    "An error occurred during registration: " + e.getMessage());
            }
        }
    }

    @FXML
    public void onCancel(ActionEvent event) {
        try {
            App.showLoginScreen();
        } catch (Exception e) {
            Dialog.showErrorAlert("Navigation Error", 
                "Could not return to login screen: " + e.getMessage());
        }
    }
} 