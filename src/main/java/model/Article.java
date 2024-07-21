package model;

public class Article {
    private int id;
    private String userId;
    private String content;
    private String imgPath;

    public Article(int id, String userId, String content, String imgPath) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.imgPath = imgPath;
    }

    public String getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getImgPath() {
        return imgPath;
    }

    @Override
    public String toString() {
        return "Article [id=" + id + ", userId=" + userId + ", content=" + content + ", imgPath=" + imgPath + "]";
    }
}