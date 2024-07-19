package model;

public class Session {
    private String sessionId;
    private String userId;
    private Long expires;

    public String getSessionId() {
        return sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public Long getExpires() {
        return expires;
    }

    // 왜 jwt에서 ms를 사용했는지
    public Session(String sessionId, String userId, Long expires) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.expires = expires;
    }

    @Override
    public String toString() {
        return "Sssion [sessionId=" + sessionId + ", userId=" + userId + ", expires=" + expires + "]";
    }
}
