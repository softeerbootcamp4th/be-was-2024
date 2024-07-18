package processor;

import db.PostDatabase;
import handler.SessionHandler;
import model.Post;
import webserver.Request;

import java.util.HashMap;

public class PostProcessor {
    public void addPost(Request request) {
        HashMap<String, String> postData = request.parseBody();
        String cookie = request.getSessionId();
        String userId = SessionHandler.getUser(cookie).getUserId();
        String title = postData.get("title");
        String content = postData.get("content");

        Post newPost = new Post.Builder()
                .userId(userId)
                .title(title)
                .content(content)
                .build();

        PostDatabase.addPost(newPost);
    }
}
