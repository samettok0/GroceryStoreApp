package model;

public class Product {
    public enum Type {
        VEGETABLE,
        FRUIT
    }
    private String name;
    private int id;
    private float availableStock;
    private float threshold;
    private float price;
    private String imageLocation;
    private Type type;

    public Product(int id, String name, float availableStock, float threshold, float price, String imageLocation, Type type) {
        this.id = id;
        this.name = name;
        this.availableStock = availableStock;
        this.threshold = threshold;
        this.price = price;
        this.imageLocation = imageLocation;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(float availableStock) {
        this.availableStock = availableStock;
    }

    public float getThreshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public String getPriceString() {
        return String.format("$%.2f", price);
    }
}
