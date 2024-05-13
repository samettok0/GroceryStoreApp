package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import manager.UserManager;
import model.User;
import util.Dialog;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class OwnerCarrierController implements Initializable {
    public TableView<User> carrierTable;
    public TextField carrierFullName;
    public TextField carrierPhone;
    public TextField carrierEmail;
    public TextField carrierPassword;
    public TextField carrierUsername;
    public Label phoneLabel;
    public Label emailLabel;
    public Label nameLabel;
    public Label idLabel;
    public TableColumn<User, String> idColumn;
    public TableColumn<User, String> nameColumn;
    public TableColumn<User, String> emailColumn;
    public TableColumn<User, String> phoneColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            List<User> carriers = UserManager.getAllUsersByRole(User.Role.CARRIER);
            carrierTable.getItems().clear();
            carrierTable.getItems().addAll(carriers);
        } catch (SQLException e) {
            Dialog.showErrorAlert("Error", "Failed to fetch carriers: " + e.getMessage());
        }
        // Set up cell value factories for each column
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }

    public void registerCarrier(ActionEvent actionEvent) {
        String username = carrierUsername.getText();
        String password = carrierPassword.getText();
        String fullname = carrierFullName.getText();
        String email = carrierEmail.getText();
        String phone = carrierPhone.getText();

        if (username.isEmpty() || password.isEmpty() || fullname.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Dialog.showErrorAlert("Invalid input", "Please fill in all fields.");
            return;
        }

        // Assuming CarrierManager has a method for registering carriers
        try {
            UserManager.register(username, password, email, fullname, phone, User.Role.CARRIER);
        } catch (SQLException | ClassNotFoundException e) {
            Dialog.showErrorAlert("Error", e.getMessage());
        }

        carrierTable.refresh();
    }

    public void onTableClicked(MouseEvent mouseEvent) {
        User selectedCarrier = carrierTable.getSelectionModel().getSelectedItem();

        if (selectedCarrier != null) {
            // Display details of the selected carrier in the UI
            idLabel.setText(String.valueOf(selectedCarrier.getId()));
            nameLabel.setText(selectedCarrier.getFullname());
            emailLabel.setText(selectedCarrier.getEmail());
            phoneLabel.setText(selectedCarrier.getPhone());
        }
    }

    public void removeCarrier(ActionEvent actionEvent) {
        try {
            // Assuming you have a method to delete a user from the database
            UserManager.deleteUserById(idLabel.getText());

            // Assuming you have a method to get all users by role
            List<User> updatedUsers = UserManager.getAllUsersByRole(User.Role.CARRIER);

            // Assuming your TableView is named userTable
            carrierTable.getItems().setAll(updatedUsers);
            carrierTable.refresh(); // Refresh the TableView to reflect the changes
        } catch (SQLException e) {
            // Handle exceptions or show an error message
            Dialog.showErrorAlert("Error", e.getMessage());
        }
    }
}
