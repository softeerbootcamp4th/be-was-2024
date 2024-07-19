package model;

import java.util.logging.Logger;

/**
 * 게시글 엔티티
 */
public class Post {
    private Long authorId;
    private String image;
    private String authorName;
    private String content;

    public Post(Long authorId, String image, String authorName, String content) {
        this.authorId = authorId;
        this.image = image;
        this.authorName = authorName;
        this.content = content;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getImage() {
        return image;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Post{" +
                "authorId=" + authorId +
                ", image='" + image + '\'' +
                ", authorName='" + authorName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
