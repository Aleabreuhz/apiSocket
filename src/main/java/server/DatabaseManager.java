package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DatabaseManager {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library_db", "root", "S@nFrancisco");
        }
        return connection;
    }
}