package model;

public class Post {

    private int id;
    private String authorName;
    private String title;
    private String content;

    public Post(String content, String title, String authorName) {
        this.authorName = authorName;
        this.title = title;
        this.content = content;
    }

    public Post(int id, String authorName, String title, String content) {
        this(authorName, title, content);
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getContent() {
        return content;
    }

    public String getTitle(){
        return title;
    }

    public int getId(){
        return id;
    }

    @Override
    public String toString() {
        return "authorName:"+authorName+", title:"+title+", content:"+content;
    }
}
