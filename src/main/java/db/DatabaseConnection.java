package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 외부 h2 Database와의 연결을 담당하는 클래스
 */
public class DatabaseConnection {
    private static final String JDBC_URL = "jdbc:h2:~/test;AUTO_SERVER=TRUE";
    private static final String JDBC_USERNAME = "sa";
    private static final String JDBC_PASSWORD = "";

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load H2 driver", e);
        }
    }

    /**
     * h2 Database와 연결을 설정하는 메서드
     * @return 연결된 Connection
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    }
}
