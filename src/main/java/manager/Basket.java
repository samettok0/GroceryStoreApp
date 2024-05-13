package manager;

import model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Basket
{
    private static final List<Item> items = new ArrayList<>();

    public static float calculateTotalPrice()
    {
        float totalPrice = 0.0f;

        for (Item item : items) {
            totalPrice += item.calculatePrice();
        }

        return totalPrice;
    }

    public static List<Item> getItems()
    {
        return items;
    }

    public static void fetchBasket() throws SQLException
    {
        items.clear();
        String query = "SELECT * FROM items WHERE user_id = ? AND order_id IS NULL";
        Connection conn = DatabaseAdapter.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, UserManager.getCurrentUser().getId());

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                while (resultSet.next()) {
                    int productId = resultSet.getInt("product_id");
                    int weight = resultSet.getInt("weight");

                    Item item = new Item(ProductManager.getProductById(productId), weight); // assuming Item constructor supports a null order_id
                    items.add(item);
                }
            }
        }
    }

    public static void emptyBasket(boolean emptyDatabase) throws SQLException
    {
        items.clear();
        String deleteQuery = "DELETE FROM items WHERE user_id = ? AND order_id IS NULL";
        Connection conn = DatabaseAdapter.getConnection();
        try (PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery))
        {
            deleteStatement.setInt(1, UserManager.getCurrentUser().getId());

            // Delete items with NULL order_id for the specific user
            deleteStatement.executeUpdate();
        }
    }

    public static void addItemToDatabase(Item item) throws SQLException
    {
        String selectQuery = "SELECT * FROM items WHERE product_id = ? AND user_id = ? AND order_id IS NULL";
        String insertQuery = "INSERT INTO items (product_id, user_id, weight, order_id) VALUES (?, ?, ?, NULL)";
        String updateQuery = "UPDATE items SET weight = weight + ? WHERE product_id = ? AND user_id = ? AND order_id IS NULL";

        Connection conn = DatabaseAdapter.getConnection();
        try (PreparedStatement selectStatement = conn.prepareStatement(selectQuery);
             PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
             PreparedStatement updateStatement = conn.prepareStatement(updateQuery))
        {

            selectStatement.setInt(1, item.getProduct().getId());
            selectStatement.setInt(2, UserManager.getCurrentUser().getId());

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Item exists, update the weight
                    updateStatement.setFloat(1, item.getWeight());
                    updateStatement.setInt(2, item.getProduct().getId());
                    updateStatement.setInt(3, UserManager.getCurrentUser().getId());
                    updateStatement.executeUpdate();

                    System.out.println("Item updated in the database.");
                }
                else
                {
                    // Item does not exist, insert a new item
                    insertStatement.setInt(1, item.getProduct().getId());
                    insertStatement.setInt(2, UserManager.getCurrentUser().getId());
                    insertStatement.setFloat(3, item.getWeight());
                    insertStatement.executeUpdate();

                    System.out.println("Item added to the database.");
                }
            }
        }
    }

    public static void removeItemFromDatabase(int productId) throws SQLException {
        String query = "DELETE FROM items WHERE product_id = ? AND user_id = ?";
        Connection conn = DatabaseAdapter.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, productId);
            preparedStatement.setInt(2, UserManager.getCurrentUser().getId());

            preparedStatement.executeUpdate();
        }
    }
    public static void addItem(Item item) throws SQLException {
        // Check if an item with the same product ID already exists
        for (Item existingItem : items) {
            if (existingItem.getProduct().getId() == item.getProduct().getId()) {
                // Increase the weight of the existing item
                existingItem.setWeight(existingItem.getWeight() + item.getWeight());
                addItemToDatabase(item);
                return; // Exit the method after updating the weight
            }
        }

        // If the item does not exist, add a new item to the list
        items.add(item);
        addItemToDatabase(item);
    }

    public static void removeItem(int productId) throws SQLException {
        // Using an Iterator to safely remove items during iteration
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            Item currentItem = iterator.next();
            if (currentItem.getProduct().getId() == productId) {
                // Remove the item with the specified product ID
                iterator.remove();
                System.out.println("Item with Product ID " + productId + " removed from the cart.");
                removeItemFromDatabase(productId);
                return; // Exit the method after removing the item
            }
        }
        System.out.println("Item with Product ID " + productId + " not found in the cart.");
        removeItemFromDatabase(productId);
    }
}

