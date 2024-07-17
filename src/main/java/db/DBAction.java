package db;

import java.sql.Connection;

public class DBAction {

    @FunctionalInterface
    public interface Query<T> {
        T execute(Connection connection);
    }

    @FunctionalInterface
    public interface Statement {
        void execute(Connection connection);
    }
}
