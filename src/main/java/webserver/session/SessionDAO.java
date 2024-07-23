package webserver.session;

import db.JDBC;
import model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

/**
 * DB 의 세션에 접근하기 위한 클래스
 */
public class SessionDAO {
    private Connection conn = null;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;

    private static final Logger logger = LoggerFactory.getLogger(SessionDAO.class);
    private final String SESSION_INSERT = "insert into session(USERID, SESSIONID) values(?, ?)";
    private final String SESSION_DELETE = "delete session where sessionid = ?";
    private final String USER_DELETE = "delete session where userid = ?";
    private final String SESSION_FIND = "select * from session where sessionid = ?";

    /**
     * sessionid에 대한 세션을 삭제한다
     * @param sessionid session의 id
     */
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

    /**
     * userid로 세션을 삭제한다.
     * @param userid 사용자의 id
     */
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

    /**
     * userid로 세션을 생성한다.
     * <p>
     *     세션을 여러 thread에서 동시 생성시 오류가 있을 수 있기에 synchronized로 지정
     * </p>
     * @param userid 사용자의 id
     * @return Sessionid 문자열을 반환한다.
     */
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

    /**
     * sessionid에 대해 세션을 검색한다
     * @param sessionId 검색할 세션 id
     * @return 해당 세션에 대한 userid
     */
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
