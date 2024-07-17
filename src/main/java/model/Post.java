package model;

import java.util.logging.Logger;

/**
 * 게시글 엔티티
 */
public class Post {
    private Long authorId;
    private String content;

    public Post(Long authorId, String content) {
        this.authorId = authorId;
        this.content = content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }
}
