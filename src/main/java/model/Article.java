package model;

public class Article {
    private String articleId;
    private String imagePath; // 파일명 저장 후 가공하여 사용
    private String content;

    public Article(String articleId, String imagePath, String content) {
        this.articleId = articleId;
        this.imagePath = imagePath;
        this.content = content;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getContent() {
        return content;
    }
}
