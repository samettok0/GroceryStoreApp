package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.Product;
import util.ClickListener;

public class ProductController {
    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLable;

    @FXML
    private ImageView img;

    @FXML
    private void click(MouseEvent mouseEvent) {
        clickListener.onClickListener(product);
    }

    private Product product;

    private ClickListener clickListener;

    public void setProduct(Product product, ClickListener clickListener) {
        this.product = product;
        this.clickListener = clickListener;
        nameLabel.setText(product.getName());
        priceLable.setText(product.getPriceString());
        img.setImage(new Image(getClass().getResourceAsStream(product.getImageLocation())));
    }
}
