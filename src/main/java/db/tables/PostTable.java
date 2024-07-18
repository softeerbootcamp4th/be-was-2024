package db.tables;

import db.DBUtil;
import model.ImageLink;
import model.Post;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostTable {
    private final static String insertSQL = "insert into post(title, content, userId) values(?, ?, ?)";
    private final static String findByIdSQL = "select id, title, content, userId from post where id = ?";
    private final static String findByUserIdSQL = "select id, title, content, userId  from post where userId = ?";
    private final static String findAllSQL = "select id, title, content, userId  from post";
    private final static String findLastCreatedSQL = "select id, title, content, userId  from post order by id desc";

    public static void insert(Post post) {
        DBUtil.statement(conn -> {
            ResultSet keys = null;
            try (
                    PreparedStatement pstmt = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            ) {
                pstmt.setString(1, post.getTitle());
                pstmt.setString(2, post.getContent());
                pstmt.setString(3, post.getUserId());

                pstmt.executeUpdate();
                keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    int key = keys.getInt(1);
                    post.setId(key);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (keys != null) keys.close();
                } catch (Exception e) {

                }
            }
        });
    }

    public static Post findById(int id) {
        return DBUtil.query(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(findByIdSQL)) {
                stmt.setInt(1, id);
                Post post = null;

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        post = new Post(
                                rs.getInt(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4)
                        );
                    }
                }

                return post;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static Post findByUserId(String userId) {
        return DBUtil.query(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(findByUserIdSQL)) {
                stmt.setString(1, userId);
                Post post = null;

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        post = new Post(
                                rs.getInt(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4)
                        );
                    }
                }

                return post;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void fetchImageLinks(List<Post> posts) {
        List<Integer> postIds = posts.stream().map(Post::getId).toList();

        Map<Integer, List<ImageLink>> imageLinkMapping = new HashMap<>();
        for (Post post : posts) {
            List<ImageLink> imageLinkList = new ArrayList<>();

            post.setImageLinks(imageLinkList);
            imageLinkMapping.put(post.getId(), imageLinkList);
        }

        List<ImageLink> imageLinks = ImageLinkTable.findByPostIdInClause(postIds);
        for (ImageLink imageLink : imageLinks) {
            imageLinkMapping.get(imageLink.getPostId()).add(imageLink);
        }
    }

    public static void fetchImageLinks(Post post) {
        List<ImageLink> imageLinks = ImageLinkTable.findByPostIdInClause(List.of(post.getId()));
        post.setImageLinks(imageLinks);
    }

    public static List<Post> findAll() {
        return DBUtil.query((conn) -> {
            try (
                    PreparedStatement stmt = conn.prepareStatement(findAllSQL);
                    ResultSet rs = stmt.executeQuery();
            ) {
                List<Post> posts = new ArrayList<>();

                while (rs.next()) {
                    Post post = new Post(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4)
                    );
                    posts.add(post);
                }
                return posts;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static Post findLastCreated() {
        return DBUtil.query((conn) -> {
            try (
                    PreparedStatement stmt = conn.prepareStatement(findLastCreatedSQL);
                    ResultSet rs = stmt.executeQuery();
            ) {
                Post post = null;
                if (rs.next()) {
                    post = new Post(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4)
                    );
                }
                return post;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
