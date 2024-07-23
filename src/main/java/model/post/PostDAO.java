package model.post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import db.JDBC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * DB의 POST table에 대한 접근 클래스
 */
public class PostDAO {
    private Connection conn = null;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;

    private static final Logger logger = LoggerFactory.getLogger(PostDAO.class);
    private final String LAST_INDEX = "SELECT ID FROM POST ORDER BY id DESC LIMIT 1";
    private final String POST_INSERT = "insert into POST(TEXT, IMGPATH, USERID) values(?, ?, ?)"; //userid, username, email, password
    private String POST_FIND = "select * from post where id= ?";
    private String PREV_POST = "SELECT MAX(id) AS prev_id FROM post WHERE id < ?";
    private String NEXT_POST = "SELECT MIN(id) AS next_id FROM post WHERE id > ?";


    /**
     * 마지막으로 작성된 글의 id를 반환한다.
     * @return 마지막 글의 id
     */
    public int getLastIndex() {
        int id = 0;
        try {
            conn = JDBC.getConnection();
            stmt = conn.prepareStatement(LAST_INDEX);
            rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            return -1;
        } finally {
            JDBC.close(stmt, conn);
        }
        return id;
    }

    /**
     * postid에 대한 다음 글
     * <p>
     *     해당 post와 가장 가까운 다음 글의 id를 반환한다.
     * </p>
     * @param postid 현재 글의 id
     * @return 다음 글의 id
     */
    public int getNextPostIndex(int postid){
        int id = 0;
        try {
            conn = JDBC.getConnection();
            stmt = conn.prepareStatement(NEXT_POST);
            stmt.setInt(1, postid);
            rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("next_id");
            }

        } catch (SQLException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            return -1;
        } finally {
            JDBC.close(stmt, conn);
        }
        return id;
    }

    /**
     * postid에 대한 이전 글
     * <p>
     *     해당 post와 가장 가까운 이전 글의 id를 반환한다.
     * </p>
     * @param postid 현재 글의 id
     * @return 이전 글의 id
     */
    public int getPrevPostIndex(int postid){
        int id = 0;
        try {
            conn = JDBC.getConnection();
            stmt = conn.prepareStatement(PREV_POST);
            stmt.setInt(1, postid);
            rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("prev_id");
            }

        } catch (SQLException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            return -1;
        } finally {
            JDBC.close(stmt, conn);
        }
        return id;
    }

    /**
     * 작성한 글을 저장한다
     * @param text 글의 text
     * @param imgpath 글의 image의 저장 경로
     * @param userid 작성자의 id
     */
    public void insertPost(String text, String imgpath, String userid) {
        try {
            conn = JDBC.getConnection();
            stmt = conn.prepareStatement(POST_INSERT);


            stmt.setString(1, text);
            stmt.setString(2, imgpath);
            stmt.setString(3, userid);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        } finally {
            JDBC.close(stmt, conn);
        }
    }

    /**
     * 해당 id에 대한 글을 찾는다
     * @param postid 찾을 글의 id
     * @return 찾은 글의 Post class. 만약 글이 없다면 null을 반환한다.
     */
    public Post getPost(int postid) {
        Post post = null;
        try {
            conn = JDBC.getConnection();
            stmt = conn.prepareStatement(POST_FIND);

            stmt.setInt(1, postid);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String text = rs.getString("text");
                String imgpath = rs.getString("imgpath");
                String userid = rs.getString("userid");
                post = new Post(text, imgpath, userid);
            }

        } catch (SQLException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            return null;
        } finally {
            JDBC.close(rs, stmt, conn);
        }
        return post;
    }

}