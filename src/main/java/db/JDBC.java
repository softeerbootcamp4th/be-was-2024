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

public class JDBC {
    private static final Logger logger = LoggerFactory.getLogger(JDBC.class);


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
