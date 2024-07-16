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
        H2Database.insert("insert into post (title, content, author_name) values (\'"+post.getTitle()+"\', \'"+post.getContent()+"\', \'"+post.getAuthorName()+"\')");
        logger.debug(post.toString());
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
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public static boolean isExist(int id) {
        return findById(id).isPresent();
    }

}
