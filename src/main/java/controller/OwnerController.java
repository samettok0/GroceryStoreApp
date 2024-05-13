package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.Dialog;

public class OwnerController {
    public void manageProducts(ActionEvent actionEvent)
    {
        try {
            // Load the FXML file for the OwnerCarrierController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("owner_product.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Manage Products");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            // Show the stage
            stage.showAndWait(); // Use showAndWait to make it a modal window
        } catch (Exception e) {
            Dialog.showErrorAlert("Error", e.getMessage()); // Handle the exception as needed
        }
    }

    public void manageCarriers(ActionEvent actionEvent)
    {
        try {
            // Load the FXML file for the OwnerCarrierController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("owner_carrier.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Manage Carriers");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            // Show the stage
            stage.showAndWait(); // Use showAndWait to make it a modal window
        } catch (Exception e) {
            Dialog.showErrorAlert("Error", e.getMessage()); // Handle the exception as needed
        }
    }

    public void manageOrders(ActionEvent actionEvent)
    {
        try {
            // Load the FXML file for the OwnerCarrierController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("owner_order.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Manage Orders");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            // Show the stage
            stage.showAndWait(); // Use showAndWait to make it a modal window
        } catch (Exception e) {
            Dialog.showErrorAlert("Error", e.getMessage()); // Handle the exception as needed
        }
    }
}
