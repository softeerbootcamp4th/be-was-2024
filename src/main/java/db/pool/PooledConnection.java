package db.pool;

import java.sql.Connection;

/**
 * 커넥션 풀 내의 각 커넥션을 표현하는 클래스. ConnectionPool 내부에서만 사용되며, 유저 수준에는 노출되지 않아야 한다.
 */
public class PooledConnection {
    private final Connection connection;
    private boolean used;

    public PooledConnection(Connection connection) {
        this.connection = connection;
        this.used = false;
    }

    /**
     * 커넥션이 사용되고 있는지 정보를 반환된다.
     * @return 현재 사용 중인지 정보
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * 커넥션의 사용 중 상태를 마킹한다
     */
    public void setUsed(boolean used) {
        this.used = used;
    }

    /**
     * 실제 커넥션을 반환한다.
     * @return 가지고 있는 JDBC 커넥션
     */
    public Connection getConnection() {
        return connection;
    }
}
