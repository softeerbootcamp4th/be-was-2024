package model;

import java.util.logging.Logger;

/**
 * 게시글 엔티티
 */
public class Post {
    private Long authorId;
    private String authorName;
    private String content;

    public Post(Long authorId, String authorName, String content) {
        this.authorId = authorId;
        this.authorName = authorName;
        this.content = content;
    }

    public Long getAuthorId() {
        return authorId;
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
                ", authorName='" + authorName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
