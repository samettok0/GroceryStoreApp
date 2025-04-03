package manager;

import java.sql.*;
import java.util.*;

public class DatabaseAdapter
{
    private static final String JDBC_URL = "jdbc:mysql://www.db4free.net/ecomdb321";
    private static final String USERNAME = "adminuser321";
    private static final String PASSWORD = "x1LmXjkx2sxVKSd";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    public static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection()) {
            // Create users table
            try (Statement stmt = conn.createStatement()) {
                String createUsersTable = """
                    CREATE TABLE IF NOT EXISTS users (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        username VARCHAR(50) UNIQUE NOT NULL,
                        password VARCHAR(50) NOT NULL,
                        email VARCHAR(100) NOT NULL,
                        fullname VARCHAR(100) NOT NULL,
                        phone VARCHAR(20) NOT NULL,
                        role VARCHAR(20) NOT NULL
                    )
                """;
                stmt.executeUpdate(createUsersTable);
            }

            // Create products table
            try (Statement stmt = conn.createStatement()) {
                String createProductsTable = """
                    CREATE TABLE IF NOT EXISTS products (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(100) NOT NULL,
                        type VARCHAR(20) NOT NULL,
                        image_location VARCHAR(255),
                        stock FLOAT NOT NULL DEFAULT 0,
                        threshold FLOAT NOT NULL DEFAULT 0,
                        price FLOAT NOT NULL DEFAULT 0
                    )
                """;
                stmt.executeUpdate(createProductsTable);
            }

            // Create orders table
            try (Statement stmt = conn.createStatement()) {
                String createOrdersTable = """
                    CREATE TABLE IF NOT EXISTS orders (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        user_id INT NOT NULL,
                        status VARCHAR(20) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id)
                    )
                """;
                stmt.executeUpdate(createOrdersTable);
            }

            // Create order_items table
            try (Statement stmt = conn.createStatement()) {
                String createOrderItemsTable = """
                    CREATE TABLE IF NOT EXISTS order_items (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        order_id INT NOT NULL,
                        product_id INT NOT NULL,
                        quantity FLOAT NOT NULL,
                        price FLOAT NOT NULL,
                        FOREIGN KEY (order_id) REFERENCES orders(id),
                        FOREIGN KEY (product_id) REFERENCES products(id)
                    )
                """;
                stmt.executeUpdate(createOrderItemsTable);
            }
            
            // Check if we need to add sample products
            insertSampleProductsIfNeeded(conn);
        }
    }
    
    private static void insertSampleProductsIfNeeded(Connection conn) throws SQLException {
        // Check if products table is empty
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM products")) {
            rs.next();
            int count = rs.getInt(1);
            
            if (count == 0) {
                System.out.println("No products found in database. Adding sample products...");
                
                // Sample product data - name, type, image, stock, threshold, price
                String[][] sampleProducts = {
                    {"Apple", "FRUIT", "apple.png", "100", "20", "1.99"},
                    {"Banana", "FRUIT", "banana.png", "150", "30", "0.99"},
                    {"Orange", "FRUIT", "orange.png", "80", "15", "1.49"},
                    {"Strawberry", "FRUIT", "strawberry.png", "50", "10", "3.99"},
                    {"Tomato", "VEGETABLE", "tomato.png", "120", "25", "1.29"},
                    {"Potato", "VEGETABLE", "potato.png", "200", "40", "0.79"},
                    {"Carrot", "VEGETABLE", "carrot.png", "150", "30", "0.89"},
                    {"Broccoli", "VEGETABLE", "broccoli.png", "80", "15", "2.49"}
                };
                
                String insertSQL = "INSERT INTO products (name, type, image_location, stock, threshold, price) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    for (String[] product : sampleProducts) {
                        pstmt.setString(1, product[0]); // name
                        pstmt.setString(2, product[1]); // type
                        pstmt.setString(3, product[2]); // image_location
                        pstmt.setFloat(4, Float.parseFloat(product[3])); // stock
                        pstmt.setFloat(5, Float.parseFloat(product[4])); // threshold
                        pstmt.setFloat(6, Float.parseFloat(product[5])); // price
                        pstmt.executeUpdate();
                    }
                }
                
                System.out.println("Added " + sampleProducts.length + " sample products to the database.");
            }
        }
    }
}
