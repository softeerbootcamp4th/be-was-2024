package model;


/**
 * 게시판 객체
 */
public class Board {


    private String title;
    private String content;
    private byte[] image;

    /**
     * 게시판 객체 생성자
     * @param title 게시판 제목
     * @param content 게시판 내용
     * @param image 게시판 이미지
     */
    public Board(String title, String content,byte[] image) {
        this.title = title;
        this.content = content;
        this.image=image;
    }

    /**
     * 게시판 title getter
     */
    public String getTitle() {return title;}

    /**
     * 게시판 content getter
     */
    public String getContent() {return content;}

    /**
     * 게시판 image getter
     */
    public byte[] getImage() {return image;}

    /**
     * 게시판 title setter
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * 게시판 content setter
     */
    public void setContent(String content) {this.content = content;}

    /**
     * 게시판 image setter
     */
    public void setImage(byte[] image) {this.image=image;}
}