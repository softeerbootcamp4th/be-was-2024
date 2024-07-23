package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCUtils { // h2 DB 연결

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/was", "was", "1234");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}