package db;

import model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PostDatabase {
    private static Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private static AtomicInteger postId = new AtomicInteger();
    public static final Logger logger = LoggerFactory.getLogger(PostDatabase.class);

    public static void addPost(Post post) {
        posts.put(postId.getAndAdd(1), post);
        logger.debug(post.toString());
    }

    public static Post findById(int id){
        return posts.get(id);
    }

    public static Collection<Post> findAll() {
        return posts.values();
    }

    public static boolean isExist(int id){
        return posts.containsKey(id);
    }

}
