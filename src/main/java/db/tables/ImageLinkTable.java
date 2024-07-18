package db.tables;

import db.DBUtil;
import model.ImageLink;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ImageLinkTable {
    private final static String insertSQL = "insert into ImageLink(link, postId) values (?, ?)";
    private final static String insertManyBaseSQL = "insert into ImageLink(link, postId) values ";
    private final static String findByPostIdInBaseSQL = "select id, link, postId from ImageLink where postId in";

    public static void insert(ImageLink imageLink) {
        DBUtil.statement(conn -> {
            ResultSet keys = null;
            try(
                    PreparedStatement pstmt = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            ) {
                pstmt.setString(1, imageLink.getLink());
                pstmt.setInt(2, imageLink.getPostId());

                pstmt.executeUpdate();
                keys = pstmt.getGeneratedKeys();
                if(keys.next()) imageLink.setId(keys.getInt(1));
            } catch(Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if(keys != null) keys.close();
                } catch(Exception e) {

                }
            }
        });
    }

    public static void insertMany(List<ImageLink> imageLinks) {
        DBUtil.statement(conn -> {
            ResultSet keys = null;
            int insertLength = imageLinks.size();
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append(insertManyBaseSQL);
            for(int i = 0; i < insertLength; i++) {
                sqlBuilder.append("(?, ?)");
                if(i < insertLength - 1) sqlBuilder.append(",");
            }
            String sql = sqlBuilder.toString();

            try(
                    PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ) {
                for(int i = 0; i < insertLength; i++) {
                    pstmt.setString(i * 2 + 1, imageLinks.get(i).getLink());
                    pstmt.setInt(i * 2 + 2, imageLinks.get(i).getPostId());
                }

                pstmt.executeUpdate();
                keys = pstmt.getGeneratedKeys();

                int idx = 0;
                while(keys.next()) {
                    imageLinks.get(idx++).setId(keys.getInt(1));
                }

            } catch(Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if(keys != null) keys.close();
                } catch(Exception e) {

                }
            }
        });
    }

    public static List<ImageLink> findByPostIdInClause(List<Integer> postIds) {
        return DBUtil.query((conn) -> {
            StringBuilder queryBuilder = new StringBuilder();
            int idLength = postIds.size();
            queryBuilder.append(findByPostIdInBaseSQL).append(" (");
            for(int i = 0 ; i < idLength; i++) {
                queryBuilder.append("?");
                if(i < idLength - 1) queryBuilder.append(",");
            }
            queryBuilder.append(")");

            String sql = queryBuilder.toString();

            try(
                    PreparedStatement stmt = conn.prepareStatement(sql);
            ) {
                for(int i = 0 ; i < idLength; i++) {
                    stmt.setInt(i+1, postIds.get(i));
                }

                List<ImageLink> imageLinks = new ArrayList<>();
                try (ResultSet rs = stmt.executeQuery()) {
                    while(rs.next()) {
                        ImageLink link = new ImageLink(
                                rs.getInt(1),
                                rs.getString(2),
                                rs.getInt(3)
                        );
                        imageLinks.add(link);
                    }
                }

                return imageLinks;
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
