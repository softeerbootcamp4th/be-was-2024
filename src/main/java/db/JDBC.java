package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.session.SessionDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * H2 database 연결을 위한 class
 */
public class JDBC {
    private static final Logger logger = LoggerFactory.getLogger(JDBC.class);

    /**
     * JDBC에 연결한다
     * @return 연결된 connection
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            DriverManager.registerDriver(new org.h2.Driver());
            conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "admin", "");

        } catch (SQLException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            return null;
        }
        return conn;
    }

    /**
     * statement와 connection을 종료한다.
     * @param stmt 종료할 statement
     * @param conn 종료할 connection
     */
    public static void close(PreparedStatement stmt, Connection conn) {
        try {
            stmt.close();
        } catch (SQLException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }

        try {
            conn.close();
        } catch (SQLException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * statement와 connection, resultset을 종료한다.
     * @param stmt 종료할 statement
     * @param conn 종료할 connection
     * @param rs 종료할 resultset
     */
    public static void close(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            rs.close();
        } catch (SQLException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }

        try {
            stmt.close();
        } catch (SQLException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }

        try {
            conn.close();
        } catch (SQLException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }
    }
}
