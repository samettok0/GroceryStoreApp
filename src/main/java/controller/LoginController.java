package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import manager.UserManager;
import model.User;
import util.Dialog;
import util.Validator;

import java.sql.SQLException;

public class LoginController
{

    public TextField fullnameField;
    public CheckBox registerCarrier;
    @FXML
    private Label welcomeText;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    public void onLoginButtonClick(ActionEvent actionEvent)
    {
        // TODO: Add input validations (check if empty etc.)

        String username = usernameField.getText();
        String password = passwordField.toString();

        try
        {
            if (UserManager.login(username, password))
            {
                Node source = (Node)  actionEvent.getSource();
                Stage stage  = (Stage) source.getScene().getWindow();
                stage.close();
            }
            else
                Dialog.showErrorAlert( "Login failed", "Username or password is incorrect."); //show error message

        } catch (SQLException | ClassNotFoundException e) {
            Dialog.showErrorAlert( "Login failed", e.getMessage());
        }
    }

    public void onRegisterButtonClick(ActionEvent actionEvent)
    {
        // TODO: Add input validations (check if empty etc.)

        String username = usernameField.getText();
        String password = passwordField.getText();
        String fullname = fullnameField.getText();
        boolean isCarrier = registerCarrier.isSelected();
        try {
            if (!Validator.validatePassword(password))
            {
                Dialog.showErrorAlert("Invalid password", "Your password is weak, it should be between 8-12 characters long, it must include at least on number, and instances of lower and upper case letters");
            }

            UserManager.register(username, password, fullname, isCarrier ? User.Role.CARRIER : User.Role.CUSTOMER);

            Dialog.showInformationAlert("Successfully Registered.", "Your account has been succesfully created, you can login now.");
        } catch (SQLException | ClassNotFoundException e) {
            Dialog.showErrorAlert( "Registration failed", e.getMessage());
        }
    }
}