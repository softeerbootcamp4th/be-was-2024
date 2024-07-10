package http.cookie;
import java.util.*;

/**
 * 쿠키 목록을 저장하는 클래스. Path가 지정되지 않으면 기본 값 / 을 가진다.
 */
public class MyCookies {
    private final Map<String, MyCookie> cookies;

    public MyCookies() {
        this.cookies = new HashMap<>();
    }

    public MyCookie put(String key, String value) {
        // 쿠키의 path 기본값은 "/"로 취급
        MyCookie cookie =  new MyCookie(key, value).path("/");
        this.cookies.put(key, cookie);

        return cookie;
    }

    /**
     * 쿠키를 서버 쿠키 객체 상에서 지운다. 클라이언트 측 쿠키에는 영향을 주지 않는다.
     * @param key 제거할 쿠키 이름
     */
    public void clear(String key) {
        this.cookies.remove(key);
    }

    /**
     * 클라이언트 측 쿠키를 만료한다. Max-Age=0; Path=/; 을 이용.
     * @param key 클라이언트 측에서 만료할 쿠키 이름
     */
    public void expire(String key) {
        MyCookie cookie = this.cookies.get(key);
        if (cookie == null) {
            cookie = new MyCookie(key, "");
            this.cookies.put(key, cookie);
        }

        // Max-Age=0 -> 쿠키의 남은 수명을 제거
        // Path=/ -> 모든 경로에 적용
        cookie.maxAge(0).path("/");
    }

    public boolean has(String key) {
        return this.cookies.containsKey(key);
    }

    /**
     * 대응되는 쿠키가 있다면 쿠키 객체를 반환
     * @param key 쿠키의 키 값
     */
    public MyCookie get(String key) {
        return this.cookies.get(key);
    }

    /**
     * 대응되는 쿠키가 있다면 쿠키 객체의 값을 반환
     * @param key 쿠키의 값
     */
    public String getValue(String key) {
        MyCookie cookie = this.cookies.get(key);

        if(cookie == null) return null;
        return cookie.getValue();
    }

    public List<MyCookie> getCookies() {
        return new ArrayList<>(cookies.values());
    }
}
