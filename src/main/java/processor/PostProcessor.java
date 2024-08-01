package processor;

import db.PostDatabase;
import handler.SessionHandler;
import model.FileData;
import model.Post;
import webserver.Request;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class PostProcessor {
    public void addPost(Request request) {
        HashMap<String, String> postData = request.parseBody();
        String cookie = request.getSessionId();
        String userId = SessionHandler.getUser(cookie).getUserId();
        String title = postData.get("title");
        String content = postData.get("content");

        FileData fileData = request.getFileData();
        String path = "/Users/admin/Desktop/softeer/" + fileData.getFileName();

        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(fileData.getFileBinaryData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Post newPost = new Post.Builder()
                .userId(userId)
                .title(title)
                .content(content)
                .path(path)
                .build();

        PostDatabase.addPost(newPost);
    }
}
