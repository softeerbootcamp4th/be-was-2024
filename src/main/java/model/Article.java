package model;

public class Article {

    private static Long articleIndex = 1L;

    private Long articleId;
    private String title;
    private String content;

    public Article(String title, String content, Long articleId) {
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

//    @Override
//    public String toString() {
////        return "A [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
//    }
}
