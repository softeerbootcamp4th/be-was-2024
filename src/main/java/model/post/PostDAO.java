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


    // 마지막 게시글 번호
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

    // 다음 글 찾기
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

    // 이전 글 찾기
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

    // 글 삽입
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

    // 글 검색
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