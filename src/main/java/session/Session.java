package session;

import util.ConstantUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Session 정보를 저장하는 클래스
 */
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

    public Session(String sessionId, String userId){
        this(sessionId, userId, LocalDateTime.now(ZoneId.of(ConstantUtil.GMT)));
    }

    public String getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    /**
     * 세션의 만료 시간을 갱신
     */
    public void updateSession() {
        this.lastAccessedAt = LocalDateTime.now(ZoneId.of(ConstantUtil.GMT));
        this.expiredAt = lastAccessedAt.plusMinutes(30);
    }

    /**
     * 세션이 만료되었는지 확인
     * @return boolean
     */
    public boolean isExpired() {
        return LocalDateTime.now(ZoneId.of(ConstantUtil.GMT)).isAfter(expiredAt);
    }

    /**
     * 세션 정보를 실제 쿠키 형식으로 반환하는 메서드
     * @return String
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
        String formattedExpiryTime = expiredAt.format(formatter);
        return "sid=" + sessionId + "; Expires=" + formattedExpiryTime + "; Path=/";
    }
}
