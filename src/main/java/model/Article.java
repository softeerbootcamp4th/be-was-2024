package model;

import java.util.ArrayList;

public class Article {
    private String userId;
    private String content;
    private String image;

    public Article(String userId, String content, String image) {
        this.userId = userId;
        this.content = content;
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }
}
