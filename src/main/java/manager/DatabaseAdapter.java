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
}
