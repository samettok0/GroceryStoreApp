package manager;

import model.Item;
import model.Order;
import model.Product;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderManager
{
    private static final List<Order> orders = new ArrayList<>();

    public static void fetchOrders() throws SQLException
    {
        orders.clear();
        String query = "SELECT * FROM orders";
        try (Connection conn = DatabaseAdapter.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery())
        {
            while (resultSet.next()) {
                Order order = new Order(
                        resultSet.getInt("id"),
                        null,
                        resultSet.getDate("order_time"),
                        resultSet.getDate("requested_delivery_time"),
                        resultSet.getDate("delivery_time"),
                        resultSet.getString("delivery_address"),
                        resultSet.getInt("customer_id"),
                        resultSet.getInt("carrier_id"),
                        Order.Status.valueOf(resultSet.getString("status"))
                );

                order.setItems(populateOrderItems(order.getId()));

                orders.add(order);
            }
        }
    }

    public static List<Item> populateOrderItems(int orderId) throws SQLException {
        List<Item> orderItems = new ArrayList<>();
        String query = "SELECT * FROM items WHERE order_id = ?";
        Connection conn = DatabaseAdapter.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, orderId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int productId = resultSet.getInt("product_id");
                    int weight = resultSet.getInt("weight");

                    Item item = new Item(ProductManager.getProductById(productId), weight);
                    orderItems.add(item);
                }
            }
        }

        return orderItems;
    }

    public static void createOrder(Date requestedDeliveryDate, String deliveryAddress) throws SQLException {
        Basket.emptyBasket(false);

        // Insert order information into the orders table
        String insertOrderQuery = "INSERT INTO orders (order_time, requested_delivery_time, delivery_time, delivery_address, customer_id, carrier_id, status) VALUES (NOW(), ?, NULL, ?, ?, NULL, 'PENDING')";
        Connection conn = DatabaseAdapter.getConnection();
        try (PreparedStatement insertOrderStatement = conn.prepareStatement(insertOrderQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertOrderStatement.setDate(1, requestedDeliveryDate);
            insertOrderStatement.setString(2, deliveryAddress);
            insertOrderStatement.setInt(3, UserManager.getCurrentUser().getId());

            int affectedRows = insertOrderStatement.executeUpdate();

            if (affectedRows > 0) {
                // Get the auto-generated order ID
                try (var generatedKeys = insertOrderStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int orderId = generatedKeys.getInt(1);

                        // Update order_id for each item in the items list
                        String updateItemsQuery = "UPDATE items SET order_id = ? WHERE AND user_id = ? AND order_id IS NULL";
                        try (PreparedStatement updateItemsStatement = conn.prepareStatement(updateItemsQuery)) {
                            updateItemsStatement.setInt(1, orderId);
                            updateItemsStatement.setInt(2, UserManager.getCurrentUser().getId());

                            // Execute batch update
                            updateItemsStatement.executeUpdate();
                        }

                    }
                }

                System.out.println("Order created successfully.");
            } else {
                System.out.println("Failed to create the order.");
            }
        }
    }

    public static void acceptOrder(Integer id) throws SQLException {
        String acceptOrderQuery = "UPDATE orders SET status = 'ACCEPTED', carrier_id = ? WHERE id = ?";
        Connection conn = DatabaseAdapter.getConnection();
        try (PreparedStatement acceptOrderStatement = conn.prepareStatement(acceptOrderQuery)) {
            acceptOrderStatement.setInt(1, UserManager.getCurrentUser().getId());
            acceptOrderStatement.setInt(2, id);

            acceptOrderStatement.executeUpdate();
        }
    }

    public static void completeOrder(Integer id, Date deliveryDate) throws SQLException {
        String completeOrderQuery = "UPDATE orders SET status = 'COMPLETED', delivery_time = ? WHERE id = ?";
        Connection conn = DatabaseAdapter.getConnection();
        try (PreparedStatement completeOrderStatement = conn.prepareStatement(completeOrderQuery)) {
            completeOrderStatement.setDate(1, deliveryDate);
            completeOrderStatement.setInt(2, id);

            completeOrderStatement.executeUpdate();
        }
    }

    public static void cancelOrder(Integer id) throws SQLException {
        String cancelOrderQuery = "UPDATE orders SET status = 'CANCELED' WHERE id = ?";
        Connection conn = DatabaseAdapter.getConnection();
        try (PreparedStatement cancelOrderStatement = conn.prepareStatement(cancelOrderQuery)) {
            cancelOrderStatement.setInt(1, id);

            cancelOrderStatement.executeUpdate();
        }
    }
}
