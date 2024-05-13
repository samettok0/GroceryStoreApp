package com.example.ecom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import manager.UserManager;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loginLoader = new FXMLLoader(App.class.getResource("login.fxml"));
        Scene scene = new Scene(loginLoader.load(), 320, 240);
        stage.setTitle("Login Panel");
        stage.setScene(scene);
        stage.show();

        switch (UserManager.getCurrentUser().getRole()) {
            case CUSTOMER -> {
                FXMLLoader customerLoader = new FXMLLoader(App.class.getResource("login.fxml"));
                Scene customerScene = new Scene(customerLoader.load(), 320, 240);
                stage.setTitle("Customer Panel");
                stage.setScene(customerScene);
                stage.show();
            }
            case OWNER -> {
                FXMLLoader ownerLoader = new FXMLLoader(App.class.getResource("owner.fxml"));
                Scene ownerScene = new Scene(ownerLoader.load(), 320, 240);
                stage.setTitle("Owner Panel");
                stage.setScene(ownerScene);
                stage.show();
            }
            case CARRIER -> {
                FXMLLoader carrierLoader = new FXMLLoader(App.class.getResource("carrier.fxml"));
                Scene carrierScene = new Scene(carrierLoader.load(), 320, 240);
                stage.setTitle("Carrier Panel");
                stage.setScene(carrierScene);
                stage.show();
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}