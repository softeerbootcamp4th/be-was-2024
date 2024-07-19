package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    // 싱글톤 인스턴스
    private static ConnectionPool instance = new ConnectionPool();

    // JDBC 연결 정보
    private String url = "jdbc:h2:tcp://localhost:9092/~/test";
    private String username = "sa";
    private String password = "1234";

    // 최대 커넥션 수
    private int maxConnections = 10;

    // 커넥션 풀 리스트
    private List<Connection> connectionPool;

    private ConnectionPool() {
        connectionPool = new ArrayList<>(maxConnections);
        initializeConnectionPool();
    }

    public static ConnectionPool getInstance() {
        return instance;
    }

    private void initializeConnectionPool() {
        while (!checkIfConnectionPoolIsFull()) {
            // 커넥션 풀이 꽉 차지 않았으면 새로운 커넥션 추가
            connectionPool.add(createNewConnection());
        }
    }

    private synchronized boolean checkIfConnectionPoolIsFull() {
        return connectionPool.size() >= maxConnections;
    }

    // 새로운 JDBC 연결 생성
    private Connection createNewConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection " + connection + " created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating connection: " + e.getMessage());
        }
        return connection;
    }

    // 사용 가능한 JDBC 연결 가져오기
    public synchronized Connection getConnection() {
        Connection connection = null;
        if (!connectionPool.isEmpty()) {
            connection = connectionPool.remove(connectionPool.size() - 1);
        }
        return connection;
    }

    // 사용한 JDBC 연결 반환하기
    public synchronized void releaseConnection(Connection connection) {
        if (connection != null && connectionPool.size() < maxConnections) {
            connectionPool.add(connection);
        }
    }

    // 모든 JDBC 연결 닫기
    public synchronized void closeAllConnections() {
        for (Connection connection : connectionPool) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
        connectionPool.clear();
    }
}