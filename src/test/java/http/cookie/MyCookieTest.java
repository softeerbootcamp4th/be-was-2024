package http.cookie;

import http.cookie.enums.CookieAttribute;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyCookieTest {
    @Test
    @DisplayName("대응되는 메서드를 실행하면 속성을 추가할 수 있다.")
    void canAddAttributes() {
        String key = "test", value = "test";
        MyCookie cookie = new MyCookie(key, value);
        // 나머지 속성은 지정 안됨
        cookie.maxAge(10).path("/").httpOnly();

        String cookieInfo = cookie.toString();

        Assertions.assertThat(cookieInfo)
                .contains(CookieAttribute.MaxAge.getAttrName())
                .contains(CookieAttribute.Path.getAttrName())
                .contains(CookieAttribute.HttpOnly.getAttrName())
                .doesNotContain(CookieAttribute.Expires.getAttrName())
                .doesNotContain(CookieAttribute.Secure.getAttrName())
                .doesNotContain(CookieAttribute.Domain.getAttrName());
    }
}