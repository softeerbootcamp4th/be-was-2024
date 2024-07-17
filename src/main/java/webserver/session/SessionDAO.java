package webserver.session;

import db.JDBC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

public class SessionDAO {
    private Connection conn = null;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;

    private static final Logger logger = LoggerFactory.getLogger(SessionDAO.class);
    private final String SESSION_INSERT = "insert into session(USERID, SESSIONID) values(?, ?)";
    private final String SESSION_DELETE = "delete session where sessionid = ?";
    private final String USER_DELETE = "delete session where userid = ?";
    private final String SESSION_FIND = "select * from session where sessionid = ?";

    // 세션 삭제
    public void deleteSession(String sessionid) {
        try {
            conn = JDBC.getConnection();
            stmt = conn.prepareStatement(SESSION_DELETE);

            stmt.setString(1, sessionid);
            stmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        } finally {
            JDBC.close(stmt, conn);
        }
    }

    public void deleteSessionByUserid(String userid) {
        try {
            conn = JDBC.getConnection();
            stmt = conn.prepareStatement(USER_DELETE);

            stmt.setString(1, userid);
            stmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        } finally {
            JDBC.close(stmt, conn);
        }
    }

    // 새션 삽입
    public synchronized String insertSession(String userid) {
        String sessionId =
                String.valueOf(System.currentTimeMillis()).substring(8, 13)
                        + UUID.randomUUID().toString().substring(1,10);

        while(findSession(sessionId)!=null){
            sessionId =
                    String.valueOf(System.currentTimeMillis()).substring(8, 13)
                            + UUID.randomUUID().toString().substring(1,10);
        }

        try {
            conn = JDBC.getConnection();
            stmt = conn.prepareStatement(SESSION_INSERT);




            stmt.setString(1, userid);
            stmt.setString(2, sessionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            return null;
        } finally {
            JDBC.close(stmt, conn);
        }
        return sessionId;
    }

    // 세션 검색
    public String findSession (String sessionId) {
        String userid = null;
        try {
            conn = JDBC.getConnection();
            stmt = conn.prepareStatement(SESSION_FIND);

            stmt.setString(1, sessionId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                userid = rs.getString("userid");
            }

        } catch (SQLException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            return null;
        } finally {
            JDBC.close(rs, stmt, conn);
        }
        return userid;
    }


}
