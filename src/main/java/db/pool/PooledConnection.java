package db.pool;

import java.sql.Connection;

public class PooledConnection {
    private final Connection connection;
    private boolean used;

    public PooledConnection(Connection connection) {
        this.connection = connection;
        this.used = false;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public Connection getConnection() {
        return connection;
    }
}
