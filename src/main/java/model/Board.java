package model;

/**
 * 게시판 형식을 담고 있는 클래스
 */
public class Board {

    private String userId;
    private String content;
    private String filePath; // 파일 경로 추가

    public Board(String userId, String content, String filePath) {
        this.userId = userId;
        this.content = content;
        this.filePath = filePath;
    }

    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public String getFilePath() {
        return filePath;
    }
}
