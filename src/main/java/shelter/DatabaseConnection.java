package shelter;

import java.sql.*;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:postgresql:shelter";
    private Connection connection;

    public DatabaseConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URL, "postgres", "admin");
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
