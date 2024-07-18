package model;

public class Article {

    private static Long articleIndex = 1L;

    private Long articleId;
    private String title;
    private String content;

    private Article(String title, String content, Long articleId) {
        this.title = title;
        this.content = content;
        this.articleId = articleId;
    }


    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }
    public Long getArticleId() {
        return articleId;
    }

    public static Long getArticleIndex() {
        return articleIndex;
    }

    public static Article of(String title, String content, Long articleId) {
        Article article = new Article(title, content, articleId);
        articleIndex++;
        return article;
    }

    @Override
    public String toString() {
        return "Article [articleId=" + articleId + ", title=" + title + ", content=" + content  + "]";
    }
}
