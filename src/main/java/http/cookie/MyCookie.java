package http.cookie;

import http.cookie.enums.CookieAttribute;

import java.util.EnumMap;
import java.util.Map;

public class MyCookie {
    private final String key;
    private String value;

    private final Map<CookieAttribute, String> attributes;

    public MyCookie(String key, String value) {
        this.key = key;
        this.value = value;
        this.attributes = new EnumMap<>(CookieAttribute.class);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    // 쿠키 값 변경
    public void setValue(String value) {
        this.value = value;
    }

    public void putAttribute(CookieAttribute attribute, String value) {
        attributes.put(attribute, value);
    }

    public void clearAttribute(CookieAttribute attribute) {
        this.attributes.remove(attribute);
    }

    public MyCookie domain(String domain) {
        putAttribute(CookieAttribute.Domain, domain);
        return this;
    }

    public MyCookie expiresAt(String dateString) {
        putAttribute(CookieAttribute.Expires, dateString);
        return this;
    }

    public MyCookie maxAge(long maxAge) {
        putAttribute(CookieAttribute.MaxAge, String.valueOf(maxAge));
        return this;
    }

    public MyCookie path(String path) {
        putAttribute(CookieAttribute.Path, path);
        return this;
    }

    public MyCookie secure() {
        putAttribute(CookieAttribute.Secure, "");
        return this;
    }

    public MyCookie httpOnly() {
        putAttribute(CookieAttribute.HttpOnly, "");
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(key).append("= ").append(value).append("; ");
        for (Map.Entry<CookieAttribute, String> entry : attributes.entrySet()) {
            CookieAttribute attribute = entry.getKey();
            String value = entry.getValue();

            // value가 없는 속성
            if(attribute.equals(CookieAttribute.Secure)
            || attribute.equals(CookieAttribute.HttpOnly)) {
                sb.append(attribute.getAttrName()).append("; ");
                continue;
            }
            sb.append(attribute.getAttrName()).append("= ").append(value).append("; ");
            // value가 있는 속성
        }

        return sb.toString();
    }
}

// 엄밀하게 하면
// __Secure- 접두사는 Secure,
// __Host- 접두사는 secure + https + path x 여야 하지만
// 너무 깊게 들어가기 때문에 이 부분은 구현하지 않는다.

// 일부 쿠키의 속성은 무시된다.