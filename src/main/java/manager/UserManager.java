package manager;

import javafx.scene.chart.PieChart;
import model.User;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    public static void register(String username, String password, String email, String fullname, String phone, User.Role role) throws SQLException, ClassNotFoundException {
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
}
