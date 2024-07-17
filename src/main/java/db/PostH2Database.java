package db;

import model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PostH2Database {
    public static final Logger logger = LoggerFactory.getLogger(PostH2Database.class);

    public static void addPost(Post post) throws SQLException {
        String sql = "insert into post (title, content, author_name) values (\'"+post.getTitle()+"\', \'"+post.getContent()+"\', \'"+post.getAuthorName()+"\')";
        H2Database.insert(sql);
        logger.debug("sql:{}, post:{}", sql, post);
    }

    public static Optional<Post> findById(int id) {
        try {
            ResultSet rs = H2Database.select("select * from post where id = " + id);
            if (rs.next()) {
                String title = rs.getString("title");
                String authorName = rs.getString("author_name");
                String content = rs.getString("content");
                return Optional.of(new Post(content, title, authorName));
            }
        }catch (SQLException e){
            logger.debug(e.getMessage());
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public static boolean isExist(int id) {
        return findById(id).isPresent();
    }

    public static Optional<Post> getLastPost(){
        try {
            ResultSet rs = H2Database.select("select * from post order by id desc limit 1");
            if (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String authorName = rs.getString("author_name");
                String content = rs.getString("content");
                return Optional.of(new Post(id, content, title, authorName));
            }
        }catch (SQLException e){
            logger.debug(e.getMessage());
            return Optional.empty();
        }
        return Optional.empty();
    }

}
