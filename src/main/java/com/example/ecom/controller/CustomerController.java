package com.example.ecom.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.ecom.App;
import manager.Basket;
import manager.OrderManager;
import manager.ProductManager;
import manager.UserManager;
import model.Item;
import model.Order;
import model.Product;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomerController {
    @FXML
    private Label userNameLabel;
    @FXML
    private ComboBox<String> categoryFilter;
    @FXML
    private TextField searchField;
    @FXML
    private FlowPane productsPane;
    
    @FXML
    private TableView<CartRow> cartTable;
    @FXML
    private TableColumn<CartRow, String> productNameColumn;
    @FXML
    private TableColumn<CartRow, Integer> quantityColumn;
    @FXML
    private TableColumn<CartRow, Double> priceColumn;
    @FXML
    private TableColumn<CartRow, Double> totalColumn;
    
    @FXML
    private TableView<Object> ordersTable;
    @FXML
    private TableColumn<Object, String> orderIdColumn;
    @FXML
    private TableColumn<Object, String> orderDateColumn;
    @FXML
    private TableColumn<Object, String> orderStatusColumn;
    @FXML
    private TableColumn<Object, Double> orderTotalColumn;
    
    @FXML
    private Label totalPriceLabel;

    // Map to hold cart items (Product -> Quantity)
    private Map<Product, Integer> cartItems = new HashMap<>();
    
    // ObservableList for the cart table
    private ObservableList<CartRow> cartTableData = FXCollections.observableArrayList();

    // Inner class to represent a row in the cart table
    public static class CartRow {
        private final Product product;
        private final SimpleIntegerProperty quantity;
        private final SimpleDoubleProperty total;

        public CartRow(Product product, int quantity) {
            this.product = product;
            this.quantity = new SimpleIntegerProperty(quantity);
            this.total = new SimpleDoubleProperty(product.getPrice() * quantity);
        }

        public String getName() { return product.getName(); }
        public int getQuantity() { return quantity.get(); }
        public double getPrice() { return product.getPrice(); }
        public double getTotal() { return total.get(); }

        public SimpleIntegerProperty quantityProperty() { return quantity; }
        public SimpleDoubleProperty totalProperty() { return total; }
        // We'll use SimpleStringProperty and SimpleDoubleProperty for name/price in cell value factories
    }

    @FXML
    public void initialize() {
        // Initialize components
        initializeCartTable();
        initializeOrdersTable();
        loadProducts();
        refreshCartTable(); // Initial cart refresh
    }

    private void loadProducts() {
        System.out.println("Starting to load products...");
        try {
            List<Product> products = ProductManager.fetchProducts();
            System.out.println("Fetched " + products.size() + " products from database");
            
            if (products.isEmpty()) {
                System.out.println("WARNING: No products found in database!");
            }
            
            productsPane.getChildren().clear();
            System.out.println("Cleared products pane");

            for (Product product : products) {
                System.out.println("Creating card for product: " + product.getName() + ", image: " + product.getImageLocation());
                VBox productCard = createProductCard(product);
                if (productCard != null) {
                    productsPane.getChildren().add(productCard);
                } else {
                    System.out.println("WARNING: Failed to create product card for " + product.getName());
                }
            }
            
            System.out.println("Added " + productsPane.getChildren().size() + " product cards to the display");
            productsPane.setPadding(new Insets(10));
            productsPane.setHgap(15);
            productsPane.setVgap(15);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert("Error Loading Products", "Could not fetch products from the database: " + e.getMessage());
        }
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-color: white; -fx-background-radius: 5;");

        ImageView imageView = new ImageView();
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);
        try {
            String imagePath = "/img/" + product.getImageLocation(); 
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                System.err.println("Warning: Could not load image resource: " + imagePath);
            }
             if (imageStream != null) {
                Image image = new Image(imageStream);
                imageView.setImage(image);
            }
        } catch (Exception e) {
            System.err.println("Error loading image for product " + product.getName() + ": " + e.getMessage());
        }

        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-weight: bold;");
        Label priceLabel = new Label(String.format("$%.2f", product.getPrice()));
        Button addButton = new Button("Add to Cart");
        addButton.setOnAction(event -> addToCart(product));

        card.getChildren().addAll(imageView, nameLabel, priceLabel, addButton);
        return card;
    }

    private void addToCart(Product product) {
        cartItems.put(product, cartItems.getOrDefault(product, 0) + 1);
        refreshCartTable(); // Refresh table after adding
        showAlert("Cart", product.getName() + " added to cart.", Alert.AlertType.INFORMATION);
    }

    private void initializeCartTable() {
        // Set cell value factories to extract data from CartRow
        productNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty().asObject());
        
        // Set the table items
        cartTable.setItems(cartTableData);
    }

    private void refreshCartTable() {
        cartTableData.clear(); // Clear the observable list
        double grandTotal = 0;

        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            cartTableData.add(new CartRow(product, quantity)); // Add new CartRow objects
            grandTotal += product.getPrice() * quantity;
        }

        // Update total price label
        totalPriceLabel.setText(String.format("$%.2f", grandTotal));
    }

    private void initializeOrdersTable() {
        orderIdColumn.setCellValueFactory(data -> null);
        orderDateColumn.setCellValueFactory(data -> null);
        orderStatusColumn.setCellValueFactory(data -> null);
        orderTotalColumn.setCellValueFactory(data -> null);
    }

    @FXML
    public void onLogout(ActionEvent event) {
        try {
            App.showLoginScreen();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not logout. Please try again.");
        }
    }

    @FXML
    public void onSearch(ActionEvent event) {
        // TODO: Implement search functionality
    }

    @FXML
    public void onCheckout(ActionEvent event) {
        if (cartItems.isEmpty()) {
            showAlert("Checkout Error", "Your cart is empty. Please add items before checking out.", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            // First, clear any existing items in the basket
            Basket.emptyBasket(true);
            
            // Add our cart items to the Basket
            for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                
                // Create a new Item with the product and quantity (weight)
                Item item = new Item(product, quantity);
                Basket.addItem(item);
            }
            
            // Create dialog for delivery details
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Checkout");
            dialog.setHeaderText("Please enter delivery details:");
            
            // Create layout for dialog
            VBox content = new VBox(10);
            content.setPadding(new Insets(20, 10, 10, 10));
            
            // Delivery date picker
            DatePicker datePicker = new DatePicker(LocalDate.now().plusDays(1));
            datePicker.setPromptText("Select delivery date");
            Label dateLabel = new Label("Delivery Date:");
            
            // Delivery address field
            TextField addressField = new TextField();
            addressField.setPromptText("Enter delivery address");
            Label addressLabel = new Label("Delivery Address:");
            
            // Add components to dialog
            content.getChildren().addAll(
                dateLabel, datePicker,
                addressLabel, addressField
            );
            
            dialog.getDialogPane().setContent(content);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            
            // Show dialog and wait for response
            Optional<ButtonType> result = dialog.showAndWait();
            
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Validate input
                if (datePicker.getValue() == null) {
                    showAlert("Input Error", "Please select a delivery date.", Alert.AlertType.ERROR);
                    return;
                }
                
                String address = addressField.getText().trim();
                if (address.isEmpty()) {
                    showAlert("Input Error", "Please enter a delivery address.", Alert.AlertType.ERROR);
                    return;
                }
                
                // Convert LocalDate to java.sql.Date
                Date deliveryDate = Date.valueOf(datePicker.getValue());
                
                // Create the order
                OrderManager.createOrder(deliveryDate, address);
                
                // Clear the cart in our UI
                cartItems.clear();
                refreshCartTable();
                
                showAlert("Order Placed", "Your order has been successfully placed. Thank you for shopping with GreenGrocer!", Alert.AlertType.INFORMATION);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Checkout Error", "Failed to process your order: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onViewOrderDetails(ActionEvent event) {
        // TODO: Implement order details view
    }

    private void showAlert(String title, String content) {
        showAlert(title, content, Alert.AlertType.ERROR);
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 