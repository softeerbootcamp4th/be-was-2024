package db;

import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;


/**
 * H2데이터베이스 연결을 위한 클래스
 */
public class DatabaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);

    private static final String JDBC_URL = "jdbc:h2:~/test;MODE=MYSQL;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    static {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(createUserTable());
            statement.execute(createBoardTable());
        } catch (SQLException e) {
            logger.error("Database initialization failed: {}", e.getMessage());
            throw new RuntimeException("Database initialization failed!", e);
        }
    }

    /**
     * H2 데이터베이스를 연결하기위한 메소드
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

    }

    private static String createUserTable() {
        return "CREATE TABLE IF NOT EXISTS users (" +
                "user_id VARCHAR(50) PRIMARY KEY, " +
                "password VARCHAR(255) NOT NULL, " +
                "name VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) NOT NULL" +
                ")";
    }

    private static String createBoardTable() {
        return "CREATE TABLE IF NOT EXISTS boards (" +
                "title VARCHAR(255) NOT NULL, " +
                "content TEXT NOT NULL," +
                "image BLOB"+
                ")";
    }
}
