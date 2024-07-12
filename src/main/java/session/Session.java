package session;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Session {

    private String sessionId;
    private String userId;
    private LocalDateTime lastAccessedAt;
    private LocalDateTime expiredAt;

    public Session(String sessionId, String userId, LocalDateTime time) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.lastAccessedAt = time;
        this.expiredAt = time.plusMinutes(30); // 30분 뒤 만료
    }

    public String getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void updateSession() {
        this.lastAccessedAt = LocalDateTime.now(ZoneId.of("GMT"));
        this.expiredAt = lastAccessedAt.plusMinutes(30);
    }

    public boolean isExpired() {
        System.out.println(expiredAt);
        System.out.println(LocalDateTime.now(ZoneId.of("GMT")));
        return LocalDateTime.now(ZoneId.of("GMT")).isAfter(expiredAt);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
        String formattedExpiryTime = expiredAt.format(formatter);
        return "sid=" + sessionId + "; Expires=" + formattedExpiryTime + "; Path=/";
    }
}
