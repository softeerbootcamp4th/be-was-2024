package db.pool;

import db.DBConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionPool {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);

    private final int size;
    private final List<PooledConnection> connections;
    private final Map<Connection, PooledConnection> usedConnections;

    public ConnectionPool() {
        this(DBConfig.DEFAULT_POOL_SIZE);
    }

    public ConnectionPool(int size) {
        this.size = size;
        this.connections = new ArrayList<>();
        this.usedConnections = new HashMap<>();
        createConnections();
    }

    private void createConnections() {
        try {
            Class.forName(DBConfig.DRIVER_NAME);
            for (int i = 0; i < size; i++) {
                Connection connection = DriverManager.getConnection(
                        DBConfig.CONNECTION_URL,
                        DBConfig.USERNAME,
                        DBConfig.PASSWORD
                );
                connections.add(new PooledConnection(connection));
            }
            logger.debug("successfully connected to database, pool size = {}", connections.size());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public synchronized Connection getConnection() {
        for (var pooled : connections) {
            if (pooled.isUsed()) continue;

            // 사용 중 마킹 + Connection 자체만 반환
            usedConnections.put(pooled.getConnection(), pooled);
            pooled.setUsed(true);
            return pooled.getConnection();
        }
        return null;
    }

    public void releaseConnection(Connection connection) {
        var usedConnection = usedConnections.get(connection);
        if (usedConnection == null) return;

        // 사용 중 테이블에서 제거 + 사용 중 마킹 제거
        usedConnections.remove(connection);
        usedConnection.setUsed(false);
    }

    public void close() throws Exception {
        for (var pooled : connections) {
            Connection connection = pooled.getConnection();
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
        logger.debug("successfully close all database connections");
    }
}
