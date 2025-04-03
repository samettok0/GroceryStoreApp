package com.example.ecom.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import manager.UserManager;
import util.Dialog;
import util.Validator;
import model.User;
import com.example.ecom.App;
import java.io.IOException;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private VBox registerFields;

    @FXML
    private Label welcomeText;

    @FXML
    private TextField fullnameField;

    @FXML
    private CheckBox registerCarrier;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize any other necessary components
    }

    @FXML
    public void onLoginButtonClick(ActionEvent actionEvent)
    {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Input validation
        if (username.isEmpty() || password.isEmpty()) {
            Dialog.showErrorAlert("Login failed", "Username and password cannot be empty.");
            return;
        }

        try
        {
            if (UserManager.login(username, password))
            {
                App.showAppropriateScreen(); // Assuming App class handles switching screens
            }
            else
                Dialog.showErrorAlert( "Login failed", "Username or password is incorrect.");

        } catch (SQLException | ClassNotFoundException | IOException e) { // Include IOException
            Dialog.showErrorAlert( "Login failed", e.getMessage());
        }
    }

    public void onRegisterButtonClick(ActionEvent actionEvent)
    {
        // If register fields are not visible, show them and return
        if (!registerFields.isVisible()) {
            registerFields.setVisible(true);
            registerFields.setManaged(true);
            // Optionally change the button text to indicate confirmation is needed
            // ((Button)actionEvent.getSource()).setText("Confirm Registration");
            return; // Exit the method, wait for user to fill fields and click again
        }

        // --- If fields ARE visible, proceed with registration attempt ---

        String username = usernameField.getText();
        String password = passwordField.getText();
        String fullname = fullnameField.getText();
        boolean isCarrier = registerCarrier.isSelected();
        
        try {
            if (!Validator.validatePassword(password)) {
                Dialog.showErrorAlert("Invalid password", "Your password is weak, it should be between 8-12 characters long, it must include at least on number, and instances of lower and upper case letters");
                return;
            }

            // Get email and phone from the new fields
            String email = emailField.getText(); 
            String phone = phoneField.getText(); 

            // Basic validation for new fields
            if (email.isEmpty() || phone.isEmpty()) {
                Dialog.showErrorAlert("Registration failed", "Email and phone number cannot be empty.");
                return;
            }
            // Add more specific validation for email/phone format here if needed

            UserManager.register(username, password, email, fullname, phone, 
                               isCarrier ? User.Role.CARRIER : User.Role.CUSTOMER);

            Dialog.showInformationAlert("Successfully Registered.", "Your account has been successfully created, you can login now.");

            // Hide the registration fields again after success
            registerFields.setVisible(false);
            registerFields.setManaged(false);
            // Optionally reset the button text if it was changed
            // ((Button)actionEvent.getSource()).setText("Register");

        } catch (SQLException | ClassNotFoundException e) {
            Dialog.showErrorAlert("Registration failed", e.getMessage());
        }
    }
} 