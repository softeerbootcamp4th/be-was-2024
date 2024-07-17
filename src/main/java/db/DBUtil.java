package db;

import db.pool.ConnectionPool;

import java.sql.Connection;

public class DBUtil {
    private static final ConnectionPool connectionPool = new ConnectionPool();

    private static Connection getConnection() {
        while(true) {
            Connection connection = connectionPool.getConnection();
            if(connection != null) return connection;
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static<T> T query(DBAction.Query<T> query) {
        Connection connection = DBUtil.getConnection();
        try {
            return query.execute(connection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    public static void statement(DBAction.Statement statement) {
        Connection connection = DBUtil.getConnection();
        try {
            statement.execute(connection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }
}
