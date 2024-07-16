package model;

public class Post {

    private String authorName;
    private String title;
    private String content;

    public Post(String content, String title, String authorName) {
        this.authorName = authorName;
        this.title = title;
        this.content = content;
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

    @Override
    public String toString() {
        return "authorName:"+authorName+", title:"+title+", content:"+content;
    }
}
