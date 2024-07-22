package model;

// Article POJO
public class Article {
    private String articleId;
    private String userId;
    private String imagePath; // 파일명 저장 후 가공하여 사용
    private String content;

    public Article(String articleId, String userId, String imagePath, String content) {
        this.articleId = articleId;
        this.userId = userId;
        this.imagePath = imagePath;
        this.content = content;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getUserId() {
        return userId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getContent() {
        return content;
    }
}
