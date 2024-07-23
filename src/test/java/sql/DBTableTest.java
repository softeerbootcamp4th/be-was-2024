package sql;

import db.tables.PostTable;
import model.ImageLink;
import model.Post;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DBTableTest {
    @Test
    public void TableTest() {
        List<Post> posts = PostTable.findAll();
        PostTable.fetchImageLinks(posts);

        for (Post post : posts) {
            System.out.println(post.getId());
            System.out.println(post.getTitle());
            System.out.println(post.getContent());
            System.out.println(post.getUserId());
            if(post.getImageLinks() == null) continue;
            for (ImageLink imageLink : post.getImageLinks()) {
                System.out.println(imageLink.getId());
                System.out.println(imageLink.getLink());
                System.out.println(imageLink.getPostId());
            }
        }
    }
}
