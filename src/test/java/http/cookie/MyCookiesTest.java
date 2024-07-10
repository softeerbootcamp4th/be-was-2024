package http.cookie;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyCookiesTest {
    @Test
    @DisplayName("쿠키는 path가 지정되지 않으면 기본 값 / 을 가진다")
    void cookieHasDefaultPathSlash() {
        MyCookies myCookies = new MyCookies();
        MyCookie cookie = myCookies.put("test", "test");
        String cookieInfo = cookie.toString();

        Assertions.assertThat(cookieInfo)
                .contains("Path= /");
    }

    @Test

    void clearDeleteCookieObject() {

    }
}