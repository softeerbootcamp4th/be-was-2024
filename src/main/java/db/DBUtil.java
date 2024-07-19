package db;

import db.pool.ConnectionPool;

import java.sql.Connection;

/**
 * DB 커넥션 풀을 편리하게 관리하기 위한 유틸 클래스
 */
public class DBUtil {
    private static final ConnectionPool connectionPool = new ConnectionPool();

    /**
     * 유효한 커넥션을 가져온다.
     * @return 유효한 커넥션
     */
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

    /**
     * 쿼리 작업을 처리하고, 값을 반환한다.
     * @param query 수행할 작업
     */
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

    /**
     * statement 작업을 처리한다.
     * @param statement 수행할 작업
     */
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
