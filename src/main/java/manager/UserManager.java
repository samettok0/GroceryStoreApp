package manager;

import javafx.scene.chart.PieChart;
import model.User;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserManager
{
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean login(String username, String password) throws SQLException, ClassNotFoundException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseAdapter.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // User found, set the currentUser
                    currentUser = new User(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("email"),
                            resultSet.getString("fullname"),
                            resultSet.getString("phone"),
                            User.Role.valueOf(resultSet.getString("role"))
                    );
                    return true;
                } else {
                    // User not found
                    return false;
                }
            }
        }
    }

    public static void logout() {
        currentUser = null;
    }

    public static boolean userExists(String username) throws SQLException {
        String query = "SELECT COUNT(*) as count FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseAdapter.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            
            preparedStatement.setString(1, username);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count") > 0;
                }
            }
        }
        return false;
    }

    public static void register(String username, String password, String email, String fullname, String phone, User.Role role) throws SQLException, ClassNotFoundException {
        // First check if user already exists
        if (userExists(username)) {
            throw new SQLException("Username '" + username + "' is already taken");
        }

        String insertQuery = "INSERT INTO users (username, password, email, fullname, phone, role) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseAdapter.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, fullname);
            preparedStatement.setString(5, phone);
            preparedStatement.setString(6, role.name());

            preparedStatement.executeUpdate();
        }
    }

    public static void deleteUser(String username) {

    }

    public static void initializeDefaultAdmin() throws SQLException, ClassNotFoundException {
        // Check if any OWNER exists
        String query = "SELECT COUNT(*) as count FROM users WHERE role = ?";
        
        try (Connection conn = DatabaseAdapter.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            
            preparedStatement.setString(1, User.Role.OWNER.name());
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt("count") == 0) {
                    // No admin exists, create default admin account
                    try {
                        register(
                            "admin",           // username
                            "Admin123!",       // password
                            "admin@store.com", // email
                            "Store Admin",     // fullname
                            "1234567890",      // phone
                            User.Role.OWNER    // role
                        );
                    } catch (SQLException e) {
                        // If admin user already exists, ignore the error
                        if (!e.getMessage().contains("already taken")) {
                            throw e;
                        }
                    }
                }
            }
        }
    }

    public static List<User> getAllUsersByRole(User.Role role) throws SQLException {
        String query = "SELECT * FROM users WHERE role = ?";
        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseAdapter.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, role.name());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getString("fullname"),
                        resultSet.getString("phone"),
                        User.Role.valueOf(resultSet.getString("role"))
                    ));
                }
            }
        }
        return users;
    }

    public static void deleteUserById(String userId) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DatabaseAdapter.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, Integer.parseInt(userId));
            preparedStatement.executeUpdate();
        }
    }
}
