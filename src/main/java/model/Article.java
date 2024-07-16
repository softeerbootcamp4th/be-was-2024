package model;

public class Article {
    private static int nextId = 1; // 다음 articleId 초기값 설정
    private String articleId;
    private String userId;
    private String content;

    public Article(String userId, String content) {
        this.articleId = generateNextId();
        this.userId = userId;
        this.content = content;
    }

    private static synchronized String generateNextId() {
        return String.valueOf(nextId++);
    }

    public String getUserId() {
        return userId;
    }

    public String getArticleId() {
        return articleId;
    }

    @Override
    public String toString() {
        return "Article [articleId=" + articleId + ", userId=" + userId + ", content=" + content + "]";
    }
}