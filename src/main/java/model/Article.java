package model;

public class Article {
    private String userId;
    private String text;
    private String pic;

    public Article(String userId, String text, String pic) {
        this.userId = userId;
        this.text = text;
        this.pic = pic;
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
