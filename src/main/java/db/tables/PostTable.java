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
    private final static String findByIdSQL = "select id, title, content, userId from post where id = ? limit 1";
    private final static String findByUserIdSQL = "select id, title, content, userId  from post where userId = ? limit 1";
    private final static String findAllSQL = "select id, title, content, userId  from post";
    private final static String findLastCreatedSQL = "select id, title, content, userId  from post order by id desc limit 1";
    private final static String findBeforeSQL = "select id from post where id < ? order by id desc limit 1";
    private final static String findAfterSQL = "select id from post where id > ? order by id asc limit 1";

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

    /**
     * 게시글 목록에 대해 대응되는 이미지 링크를 채운다.
     * @param posts 이미지를 채울 게시글 목록
     */
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

    /**
     * 하나의 게시글에 대해 대응되는 이미지 링크를 채운다.
     * @param post 이미지를 채울 게시글
     */
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

    /**
     * 최근 생성된 게시글을 가져온다
     * @return 최근 생성된 게시글
     */
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

    /**
     * 앞 게시글의 id를 가져온다
     * @param id 현재 게시글의 id
     * @return 앞 게시글의 id ( 없으면 null )
     */
    public static Integer findBeforeId(int id) {
        return DBUtil.query(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(findBeforeSQL)) {
                stmt.setInt(1, id);
                Integer beforeId = null;

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                       beforeId = rs.getInt("id");
                    }
                }

                return beforeId;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 뒤 게시글의 id를 가져온다
     * @param id 현재 게시글의 id
     * @return 뒤 게시글의 id ( 없으면 null )
     */
    public static Integer findAfterId(int id) {
        return DBUtil.query(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(findAfterSQL)) {
                stmt.setInt(1, id);
                Integer afterId = null;

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        afterId = rs.getInt("id");
                    }
                }

                return afterId;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
