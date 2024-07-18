package model;

public class Article {
    private final int articleId;
    private final String userName;
    private final String text;
    private final byte[] pic;

    public Article(int articleId, String userName, String text, byte[] pic) {
        this.articleId = articleId;
        this.userName = userName;
        this.text = text;
        this.pic = pic;
    }

    public int getArticleId() {
        return articleId;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }

    public byte[] getPic() {
        return pic;
    }
}
