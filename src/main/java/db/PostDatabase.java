package db;

import model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PostDatabase {
    private static Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private static AtomicInteger postId = new AtomicInteger();

    public static void addPost(Post post) {
        posts.put(postId.getAndAdd(1), post);
    }

    public static Collection<Post> findAll() {
        return posts.values();
    }
}
