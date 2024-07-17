package model;

import java.time.LocalDateTime;

import static util.StringUtil.*;

public class Session {
    private final String sessionId;
    private final String userId;
    private final LocalDateTime sessionStart;
    private final LocalDateTime expiryTime;
    //private String ipAddress;
    //private LocalDateTime lastActivity;
    //private String userAgent;

    public Session(String sessionId, String userId, LocalDateTime sessionStart, LocalDateTime expiryTime) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.sessionStart = sessionStart;
        this.expiryTime = expiryTime;
    }

    public String getUserId(){
        return userId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append("sid=").append(sessionId).append(SEMICOLON)
                .append("expires=").append(expiryTime).append(SEMICOLON)
                .append("path=").append(ROOT_PATH).append(SEMICOLON);

        return sb.toString();
    }

}
