package model;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private int id;
    private String title;
    private String content;
    private String userId;
    private List<ImageLink> imageLinks;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
        this.imageLinks = new ArrayList<>();
    }

    public Post(String title, String content, String userId) {
        this(title, content);
        this.userId = userId;
    }

    public Post(int id, String title, String content, String userId) {
        this(title, content, userId);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ImageLink> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(List<ImageLink> imageLinks) {
        this.imageLinks = imageLinks;
    }
}
