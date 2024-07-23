package model;

public class Post {
    private final int postId;
    private final String userId;
    private final String title;
    private final String content;

    private Post(Builder builder) {
        this.postId = builder.postId;
        this.userId = builder.userId;
        this.title = builder.title;
        this.content = builder.content;
    }

    public static class Builder {
        private int postId;
        private String userId;
        private String title;
        private String content;

        public Builder postId(int postId) {
            this.postId = postId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Post build() {
            return new Post(this);
        }
    }

    public int getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
