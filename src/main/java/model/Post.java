package model;

import java.util.List;

public class Post {
    private int id;
    private String title;
    private String content;
    private List<String> imageLinks;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
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

    public List<String> getImageLinks() {
        return imageLinks;
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

    public void setImageLinks(List<String> image_links) {
        this.imageLinks = image_links;
    }
}
