package com.example.ecom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import manager.DatabaseAdapter;
import manager.UserManager;
import model.User;
import util.Dialog;

import java.io.IOException;
import java.sql.SQLException;

public class App extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        
        // Initialize database and admin account
        try {
            // First initialize database schema
            DatabaseAdapter.initializeDatabase();
            
            // Then create default admin if needed
            UserManager.initializeDefaultAdmin();
            
            // Show login screen
            showLoginScreen();
            
            // Set title
            primaryStage.setTitle("GreenGrocer - Your Local Grocery Store");
            
        } catch (SQLException | ClassNotFoundException e) {
            Dialog.showErrorAlert("Database Error", 
                "Failed to initialize database. Please check your database connection.\nError: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void showLoginScreen() throws IOException {
        FXMLLoader loginLoader = new FXMLLoader(App.class.getResource("login.fxml"));
        Scene scene = new Scene(loginLoader.load(), 960, 540);
        scene.getStylesheets().add(App.class.getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showAppropriateScreen() throws IOException {
        User currentUser = UserManager.getCurrentUser();
        if (currentUser == null) {
            showLoginScreen();
            return;
        }

        FXMLLoader loader;
        String title;
        
        switch (currentUser.getRole()) {
            case CUSTOMER -> {
                loader = new FXMLLoader(App.class.getResource("customer.fxml"));
                title = "Customer Dashboard";
            }
            case OWNER -> {
                loader = new FXMLLoader(App.class.getResource("owner.fxml"));
                title = "Owner Dashboard";
            }
            case CARRIER -> {
                loader = new FXMLLoader(App.class.getResource("carrier.fxml"));
                title = "Carrier Dashboard";
            }
            default -> {
                showLoginScreen();
                return;
            }
        }

        Scene scene = new Scene(loader.load(), 960, 540);
        scene.getStylesheets().add(App.class.getResource("styles.css").toExternalForm());
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}