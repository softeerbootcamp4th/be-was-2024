package model;

/**
 * 게시판 형식을 담고있는 클래스
 */
public class Board {

    private String userId;
    private String content;

    public Board(String userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }
}
