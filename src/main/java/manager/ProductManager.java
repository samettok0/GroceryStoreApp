package manager;

import model.Product;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    private static List<Product> products;

    public static List<Product> getProducts()
    {
        return products;
    }

    public static Product getProductById(int productId) {
        for (Product product : products) {
            if (product.getId() == productId)
                return product;
        }

        return null;
    }
    public static List<Product> fetchProducts() throws ClassNotFoundException, SQLException
    {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (Connection conn = DatabaseAdapter.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery())
        {
            while (resultSet.next())
            {
                Product product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getFloat("stock"),
                        resultSet.getFloat("threshold"),
                        resultSet.getFloat("price"),
                        resultSet.getString("image_location"),
                        Product.Type.valueOf(resultSet.getString("type"))
                );
                products.add(product);
            }
        }
        return products;
    }

    public static void updateProduct(Product product) throws SQLException, ClassNotFoundException {
        String updateQuery = "UPDATE products SET name = ?, type = ?, image_location = ?, stock = ?, threshold = ?, price = ? WHERE id = ?";
        try (Connection conn = DatabaseAdapter.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getType().name());
            preparedStatement.setString(3, product.getImageLocation());
            preparedStatement.setFloat(4, product.getAvailableStock());
            preparedStatement.setFloat(5, product.getThreshold());
            preparedStatement.setFloat(6, product.getPrice());
            preparedStatement.setInt(7, product.getId());

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Product updated successfully.");
            } else {
                System.out.println("Failed to update the product. No matching ID found.");
            }
        }
    }

    public static void deleteProduct(Product product) throws SQLException
    {
        String deleteQuery = "DELETE FROM products WHERE id = ?";

        try (Connection conn = DatabaseAdapter.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery))
        {
            preparedStatement.setInt(1, product.getId());
            preparedStatement.executeUpdate();
        }
    }

    public static void createProduct(String name, float stock, float threshold, float price, String imageLocation, Product.Type type) throws SQLException, ClassNotFoundException {
        String insertQuery = "INSERT INTO products (name, type, image_location, stock, threshold, price) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseAdapter.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, type.name());
            preparedStatement.setString(3, imageLocation);
            preparedStatement.setFloat(4, stock);
            preparedStatement.setFloat(5, threshold);
            preparedStatement.setFloat(6, price);

            preparedStatement.executeUpdate();
        }
    }
}
