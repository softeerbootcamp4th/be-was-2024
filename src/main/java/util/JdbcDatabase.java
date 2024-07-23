package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DB에 대한 유틸클래스
 */
public class JdbcDatabase {
    private static final String JDBC_URL = "jdbc:h2:tcp://localhost/~/bewas";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load JDBC driver", e);
        }
    }

    /**
     * 새로운 DB커넥션을 생성해서 반환한다.
     * @return 새로운 DB커넥션
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }
}
