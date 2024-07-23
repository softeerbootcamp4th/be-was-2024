package model;

public class Article {
    private final int articleId;
    private final String userId;
    private final String text;
    private final String pic;

    public Article(int articleId, String userId, String text, String pic) {
        this.articleId = articleId;
        this.userId = userId;
        this.text = text;
        this.pic = pic;
    }

    public int getArticleId(){
        return articleId;
    }
    public String getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public String getPic() {
        return pic;
    }
}
