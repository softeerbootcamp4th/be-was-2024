package model;

import java.util.UUID;

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
