package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import manager.ProductManager;
import model.Product;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import model.User;
import util.Dialog;

public class OwnerProductController implements Initializable {
    public Product selectedProduct;
    public TextField productName;
    public RadioButton vegType;
    public RadioButton fruitType;
    public TextField productPrice;
    public TextField productThreshold;
    public TextField productStock;
    public TextField productImage;
    public Button pickImage;
    public Button updateProduct;
    public Button deleteProduct;
    public TextField newProductName;
    public RadioButton newTypeVeg;
    public RadioButton newTypeFruit;
    public TextField newProductPrice;
    public TextField newProductThreshold;
    public TextField newProductStock;
    public TextField newProductImage;
    public Button newPickImage;
    public Button createProduct;
    public TableView<Product> productsTable;
    public TableColumn<Product, String> nameCol;
    public TableColumn<Product, String> priceCol;
    public TableColumn<Product, String> thresholdCol;
    public TableColumn<Product, String> stockCol;
    public TableColumn<Product, String> typeCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Assuming you have a method in ProductManager to get all products

        try {
            ProductManager.fetchProducts();
        } catch (ClassNotFoundException | SQLException e) {
            Dialog.showErrorAlert("Error", e.getMessage());
        }

        List<Product> products = ProductManager.getProducts();

        // Assuming your TableView is named productsTable
        productsTable.getItems().addAll(products);

        // Set up cell value factories for each column
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("priceString"));
        thresholdCol.setCellValueFactory(new PropertyValueFactory<>("thresholdString"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stockString"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("typeString"));
    }

    public void pickImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Show open file dialog
        Stage stage = (Stage) pickImage.getScene().getWindow(); // Assuming pickImage is a button
        File selectedFile = fileChooser.showOpenDialog(stage);

        // Process the selected file (you can save the file path or perform other actions)
        if (selectedFile != null) {
            String imagePath = selectedFile.getAbsolutePath();
            productImage.setText(imagePath);
        }
    }

    public void updateProduct(ActionEvent actionEvent) {
        if (selectedProduct != null) {
            // Assuming you have corresponding text fields for updating the product
            selectedProduct.setName(productName.getText());
            selectedProduct.setPrice(Float.parseFloat(productPrice.getText()));
            selectedProduct.setThreshold(Float.parseFloat(productThreshold.getText()));
            selectedProduct.setAvailableStock(Float.parseFloat(productStock.getText()));
            selectedProduct.setImageLocation(productImage.getText());
            selectedProduct.setType(vegType.isSelected() ? Product.Type.VEGETABLE : Product.Type.FRUIT);

            // Assuming you have a method in ProductManager to update the product
            try {
                ProductManager.updateProduct(selectedProduct);

                productsTable.getItems().clear();
                List<Product> updatedProducts = ProductManager.getProducts();
                productsTable.getItems().addAll(updatedProducts);

                Dialog.showInformationAlert("Success", "Product updated successfully.");
            } catch (ClassNotFoundException | SQLException e) {
                Dialog.showErrorAlert("Error", "Failed to update product: " + e.getMessage());
            }
        } else {
            Dialog.showErrorAlert("Error", "Please select a product to update.");
        }
    }

    public void deleteProduct(ActionEvent actionEvent) {
        // Get the selected product from the table
        if (selectedProduct != null) {
            // Assuming you have a method in ProductManager to delete the product
            try {
                ProductManager.deleteProduct(selectedProduct);

                productsTable.getItems().clear();
                List<Product> updatedProducts = ProductManager.getProducts();
                productsTable.getItems().addAll(updatedProducts);

                Dialog.showInformationAlert("Success", "Product deleted successfully.");
            } catch (Exception e) {
                Dialog.showErrorAlert("Error", "Failed to delete product: " + e.getMessage());
            }
        } else {
            Dialog.showErrorAlert("Error", "Please select a product to delete.");
        }
    }

    public void newPickImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Show open file dialog
        Stage stage = (Stage) pickImage.getScene().getWindow(); // Assuming pickImage is a button
        File selectedFile = fileChooser.showOpenDialog(stage);

        // Process the selected file (you can save the file path or perform other actions)
        if (selectedFile != null) {
            String imagePath = selectedFile.getAbsolutePath();
            newProductImage.setText(imagePath);
        }
    }

    public void createProduct(ActionEvent actionEvent) {
        // Assuming you have corresponding text fields for creating a new product
        String newName = newProductName.getText();
        float newPrice = Float.parseFloat(newProductPrice.getText());
        float newThreshold = Float.parseFloat(newProductThreshold.getText());
        float newStock = Float.parseFloat(newProductStock.getText());
        String newImagePath = newProductImage.getText();
        Product.Type newType = vegType.isSelected() ? Product.Type.VEGETABLE : Product.Type.FRUIT;

        try {
            ProductManager.createProduct(newName, newStock, newThreshold, newPrice, newImagePath, newType);

            productsTable.getItems().clear();
            List<Product> updatedProducts = ProductManager.getProducts();
            productsTable.getItems().addAll(updatedProducts);

            Dialog.showInformationAlert("Success", "Product created successfully.");
        } catch (Exception e) {
            Dialog.showErrorAlert("Error", "Failed to create product: " + e.getMessage());
        }
    }

    public void onProductTableClicked(MouseEvent mouseEvent) {
        selectedProduct = productsTable.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            // Display details of the selected product in the UI
            productName.setText(selectedProduct.getName());
            productStock.setText(String.valueOf(selectedProduct.getAvailableStock()));
            productThreshold.setText(String.valueOf(selectedProduct.getThreshold()));
            productPrice.setText(String.valueOf(selectedProduct.getPrice()));

            // You may need to handle the image loading logic here based on selectedProduct.getImageLocation()

            if (selectedProduct.getType() == Product.Type.VEGETABLE) {
                vegType.setSelected(true);
            } else if (selectedProduct.getType() == Product.Type.FRUIT) {
                fruitType.setSelected(true);
            }
        }
    }
}
