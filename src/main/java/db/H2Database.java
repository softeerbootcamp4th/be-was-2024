package db;

import java.sql.*;

/**
 * H2 와 JDBC 로 사용할 수 있는 명령문을 추상화한 클래스
 */
public class H2Database {

    private static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "sa");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * sql을 실행한 결과를 반환하는 메소드
     * @param sql
     * @return
     * @throws SQLException
     */
    public static ResultSet select(String sql) throws SQLException {
        return connection.createStatement().executeQuery(sql);
    }

    /**
     * sql을 입력하면 insert DB에 하는 메소드
     * @param sql
     * @throws SQLException
     */
    public static void insert(String sql) throws SQLException {
        connection.createStatement().execute(sql);
    }

    /**
     * table의 데이터를 비우는 메소드
     * @param table
     * @throws SQLException
     */
    public static void deleteAll(String table) throws SQLException {
        connection.createStatement().execute("truncate table "+table);
    }

}
