package model;

public class Article {
    private String articleId;
    private byte[] image;
    private String content;

    public Article(String articleId, byte[] image, String content) {
        this.articleId = articleId;
        this.image = image;
        this.content = content;
    }

    public String getArticleId() {
        return articleId;
    }

    public byte[] getImage() {
        return image;
    }

    public String getContent() {
        return content;
    }
}
