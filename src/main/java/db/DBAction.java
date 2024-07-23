package db;

import java.sql.Connection;

/**
 * Connection을 간편하게 관리하기 위해 정하는 DB 액션
 */
public class DBAction {

    /**
     * 값을 반환하는 DB 작업 타입
     * @param <T> 반환할 값의 타입
     */
    @FunctionalInterface
    public interface Query<T> {
        T execute(Connection connection);
    }

    /**
     * 값을 반환하지 않는 DB 작업 타입
     */
    @FunctionalInterface
    public interface Statement {
        void execute(Connection connection);
    }
}
