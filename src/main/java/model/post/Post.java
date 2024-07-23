package model.post;

/**
 * 작성된 글에 대한 class
 */
public class Post {
    /**
     * 글 내용
     */
    String text;
    /**
     * 저장된 이미지 경로
     */
    String imgpath;
    /**
     * 작성한 user id
     */
    String userid;
    public Post(String text, String imgPath , String userid) {
        this.text = text;
        this.imgpath = imgPath;
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public String getImgpath() {
        return imgpath;
    }

    public String getUserid() {
        return userid;
    }
}
