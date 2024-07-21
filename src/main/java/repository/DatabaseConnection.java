package repository;

import exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * H2 DB의 Connection을 생성 및 해제하는 클래스
 */
public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static final String JDBC_URL = "jdbc:h2:tcp://localhost:9092/~/test";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "1234";


    // ThreadLocal을 사용하여 각 스레드에 고유한 Connection 객체를 할당
    private static final ThreadLocal<Connection> threadLocalConnection = ThreadLocal.withInitial(() -> {
        try{
            Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            // repository에 테이블이 없으면 생성
            UserRepository.initialize(connection);
            PostRepository.initialize(connection);

            return connection;
        }
        catch (SQLException e){
            throw new DatabaseException("getConnection error");
        }
    });

    /**
     * ThreadLocal에서 쓰레드마다 다른 Connection 객체를 반환한다.
     *
     * @return : H2 DB Connection 객체
     */
    public static Connection getConnection() {
        return threadLocalConnection.get();
    }

    // Connection 객체를 닫고 ThreadLocal을 초기화

    /**
     * H2 DB Connection 자원을 해제한다.
     */
    public static void closeConnection() {
        try {
            Connection connection = threadLocalConnection.get();
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            threadLocalConnection.remove();
        }
    }
}
