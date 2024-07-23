package utils;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {
  private static final String JDBC_URL = "jdbc:h2:tcp://localhost:9092/~/test";
  private static final String JDBC_TEST_URL = "jdbc:h2:mem:testdb";
  private static final String JDBC_USER = "sa";
  private static final String JDBC_PASSWORD = "";
  private static DataSource dataSource;

  public static DataSource getDataSource() {
    if (dataSource == null) {
      synchronized (DatabaseConfig.class) {
        if (dataSource == null) {
          JdbcDataSource ds = new JdbcDataSource();
          if ("test".equals(System.getenv("APP_ENV"))) {
            ds.setURL(JDBC_TEST_URL); // 메모리 기반 H2 데이터베이스
          } else {
            ds.setURL(JDBC_URL); // 파일 기반 H2 데이터베이스
          }
          ds.setUser(JDBC_USER);
          ds.setPassword(JDBC_PASSWORD);
          dataSource = ds;
        }
      }
    }
    return dataSource;
  }

  public static Connection getConnection() throws SQLException {
    try {
      synchronized (DatabaseConfig.class) {
        return getDataSource().getConnection();
      }
    } catch (SQLException e) {
      throw new SQLException("커넥션 획득 실패", e);
    }
  }
}
