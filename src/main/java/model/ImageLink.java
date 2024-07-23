package model;

public class ImageLink {
    private int id;
    private String link;
    private int postId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getLink() {
        return link;
    }

    public ImageLink(String link, int postId) {
        this.link = link;
        this.postId = postId;
    }

    public ImageLink(int id, String link, int postId) {
        this(link, postId);
        this.id = id;
    }
}
