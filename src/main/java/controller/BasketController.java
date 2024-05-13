package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import manager.Basket;
import manager.OrderManager;
import manager.ProductManager;
import model.Item;
import model.Product;
import util.ClickListener;
import util.Dialog;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class BasketController implements Initializable {
    public GridPane grid;
    public TextField deliveryAddress;
    public DatePicker deliveryDate;
    public Label totalPriceLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Item> items = Basket.getItems();

        float total = Basket.calculateTotalPrice();
        totalPriceLabel.setText(String.format("₺%.02f", total));

        int column = 0;
        int row = 1;
        try {
            grid.getChildren().clear();
            for (Item item : items) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setItem(item);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row); //(child,column,row)
                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            Dialog.showErrorAlert("Error", e.getMessage());
        }
    }


    public void clearBasket(ActionEvent actionEvent) {
        try {
            Basket.emptyBasket(true);
            grid.getChildren().clear();
        } catch (SQLException e) {
            Dialog.showErrorAlert("Error", e.getMessage());
        }
    }

    public void placeOrder(ActionEvent actionEvent) {
        Date date = Date.valueOf(deliveryDate.getValue());
        String address = deliveryAddress.getText();

        try {
            OrderManager.createOrder(date, address);
            Basket.emptyBasket(false);
        } catch (SQLException e) {
            Dialog.showErrorAlert("Error", e.getMessage());
        }
    }
}
