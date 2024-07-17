package db;

import java.sql.*;

public class H2Database {

    private static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "sa");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet select(String sql) throws SQLException {
        return connection.createStatement().executeQuery(sql);
    }

    public static void insert(String sql) throws SQLException {
        connection.createStatement().execute(sql);
    }

    public static void deleteAll(String table) throws SQLException {
        connection.createStatement().execute("truncate table "+table);
    }

}
