package src.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/first_aid_db";
    private static final String USER = System.getenv().getOrDefault("FIRST_AID_DB_USER", "root");

    public static Connection getConnection() {
        String password = System.getenv("FIRST_AID_DB_PASSWORD");
        if (password == null || password.isBlank()) {
            System.out.println("Database password is not configured.");
            System.out.println("Set the FIRST_AID_DB_PASSWORD environment variable, then restart the terminal.");
            return null;
        }
        try {
            Connection con = DriverManager.getConnection(URL, USER, password);
            System.out.println("Database connected successfully!");
            return con;
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }
}
