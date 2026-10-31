package db;

import java.sql.Connection;
import java.sql.DriverManager;

// Utility class for establishing database connection
public class DBConnection {

    // Returns a connection object to the MySQL database
    public static Connection getConnection() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Read credentials from the environment so they are never committed.
            String url = System.getenv().getOrDefault(
                "DB_URL", "jdbc:mysql://localhost:3306/encryptvault"
            );
            String user = System.getenv().getOrDefault("DB_USER", "root");
            String password = System.getenv("DB_PASSWORD");

            if (password == null || password.isBlank()) {
                throw new IllegalStateException(
                    "DB_PASSWORD is not set. Configure the database environment variables before starting EncryptVault."
                );
            }

            // Establish connection using environment-provided credentials.
            return DriverManager.getConnection(
                url,
                user,
                password
            );
        } catch (Exception e) {
            // Handle connection errors
            e.printStackTrace();
        }

        // Return null if connection fails
        return null;
    }
}
