package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.Item;
import model.Product;
import util.ClickListener;

public class ItemController {
    public Label itemNameLabel;
    public Label itemPriceLabel;
    public ImageView itemImageView;
    public Label countLabel;
    private Item item;

    public void setItem(Item item) {
        this.item = item;
        itemNameLabel.setText(item.getProduct().getName());
        itemPriceLabel.setText(item.getProduct().getPriceString());
        countLabel.setText(String.valueOf((int)item.getWeight()));
        itemImageView.setImage(new Image(getClass().getResourceAsStream(item.getProduct().getImageLocation())));
    }

    public void decreaseQty(ActionEvent actionEvent) {
        int currentQuantity = (int)item.getWeight();
        if (currentQuantity > 0) {
            countLabel.setText(String.valueOf(currentQuantity - 1));
            // Update the quantity in the associated item
            item.setWeight(currentQuantity - 1);
        }
    }

    @FXML
    public void increaseQty(ActionEvent actionEvent) {
        int currentQuantity = (int)item.getWeight();
        countLabel.setText(String.valueOf(currentQuantity + 1));
        // Update the quantity in the associated item
        item.setWeight(currentQuantity + 1);
    }
}
