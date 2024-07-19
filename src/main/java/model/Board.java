package model;

public class Board {


    private String title;
    private String content;
    private byte[] image;

    public Board(String title, String content,byte[] image) {
        this.title = title;
        this.content = content;
        this.image=image;
    }

    public String getTitle() {return title;}
    public String getContent() {return content;}
    public byte[] getImage() {return image;}
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) {this.content = content;}
    public void setImage(byte[] image) {this.image=image;}
}