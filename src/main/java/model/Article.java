package model;

public class Article {
    private int id;
    private String author;
    private String content;
    private byte[] image;

    public Article(int id, String author, String content, byte[] image) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.image = image;
    }

    // id를 제외한 생성자
    public Article(String author, String content, byte[] image) {
        this.author = author;
        this.content = content;
        this.image = image;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getAuthor() { return author; }
    public String getContent() { return content; }
    public byte[] getImage() { return image; }

    public void setId(int id) { this.id = id; }
    public void setAuthor(String author) { this.author = author; }
    public void setContent(String content) { this.content = content; }
    public void setImage(byte[] image) { this.image = image; }
}
