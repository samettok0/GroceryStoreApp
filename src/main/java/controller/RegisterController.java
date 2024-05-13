package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import manager.UserManager;
import model.User;
import util.Dialog;
import util.Validator;

import java.sql.SQLException;

public class RegisterController {
    public TextField usernameField;
    public TextField numberField;
    public TextField fullnameField;
    public TextField passwordField;
    public TextField emailField;

    public void registerButtonClicked(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String fullname = fullnameField.getText();
        String email = emailField.getText();
        String number = numberField.getText();

        if (username.isEmpty() || password.isEmpty() || fullname.isEmpty() || email.isEmpty() || number.isEmpty()) {
            Dialog.showErrorAlert("Invalid input", "Please fill in all fields.");
            return;
        }

        try {
            if (!Validator.validatePassword(password))
            {
                Dialog.showErrorAlert("Invalid password", "Your password is weak, it should be between 8-12 characters long, it must include at least on number, and instances of lower and upper case letters");
            }

            UserManager.register(username, password, email, fullname, number, User.Role.CUSTOMER);

            Dialog.showInformationAlert("Successfully Registered.", "Your account has been succesfully created, you can login now.");
        } catch (SQLException | ClassNotFoundException e) {
            Dialog.showErrorAlert( "Registration failed", e.getMessage());
        }
    }
}
