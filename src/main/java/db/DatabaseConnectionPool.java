package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static util.Constants.*;

/**
 * 데이터베이스 커넥션 풀을 생성하는 클래스입니다.
 */

public class DatabaseConnectionPool {
    private List<Connection> availableConnections = new ArrayList<>();
    private List<Connection> usedConnections = new ArrayList<>();
    private static final int INITIAL_POOL_SIZE = 5;

    private static String JDBC_URL;

    /**
     * 데이터베이스 URL을 주입합니다.
     * @param jdbc_url H2 데이터베이스 주소입니다.
     */
    DatabaseConnectionPool(String jdbc_url){
        JDBC_URL = jdbc_url;
    }

    /**
     * 새로운 Connection을 생성합니다.
     * @return 새로운 Connection을 생성한 후 반환합니다.
     * @throws SQLException 잘못된 SQL문 입력 시 예외를 반환합니다.
     */
    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, H2_USERNAME, H2_PASSWORD);
    }


    /**
     * Connection Pool에 등록된 Connection을 반환하는 메소드입니다.
     * Connection Pool에 등록된 Connection이 없다면 새 Connection을 등록 후 반환합니다.
     * @return Connection객체를 반환합니다.
     * @throws SQLException 잘못된 SQL문 입력 시 예외를 반환합니다.
     */
    public synchronized Connection getConnection() throws SQLException {
        if (availableConnections.isEmpty()) {
            availableConnections.add(createConnection());
        }
        Connection connection = availableConnections.remove(availableConnections.size() - 1);
        usedConnections.add(connection);
        return connection;
    }
}
