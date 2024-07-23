package model.post;

public class Post {
    String text;
    String imgpath;
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
