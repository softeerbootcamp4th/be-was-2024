package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestObjectTest {

    @DisplayName("단순한 RequestLine을 파싱 후 HttpRequestObject를 생성한다.")
    @Test
    void from() {
        // given
        String requestLine = "GET /index.html HTTP/1.1";

        // when
        HttpRequestObject httpRequestObject = HttpRequestObject.from(requestLine);

        // then
        assertThat(httpRequestObject.getRequestMethod()).isEqualTo("GET");
        assertThat(httpRequestObject.getRequestPath()).isEqualTo("/index.html");
        assertThat(httpRequestObject.getHttpVersion()).isEqualTo("HTTP/1.1");
    }

    @DisplayName("requestParam이 존재하는 RequestLine을 파싱 후 HttpRequestObject를 생성한다.")
    @Test
    void fromWithParams() {
        // given
        String requestLine = "GET /user/create?userId=userId&password=password&name=name&email=abc@naver.com HTTP/1.1";

        // when
        HttpRequestObject httpRequestObject = HttpRequestObject.from(requestLine);

        // then
        assertThat(httpRequestObject.getRequestMethod()).isEqualTo("GET");
        assertThat(httpRequestObject.getRequestPath()).isEqualTo("/user/create");
        assertThat(httpRequestObject.getHttpVersion()).isEqualTo("HTTP/1.1");
        assertThat(httpRequestObject.getRequestParams().get("userId")).isEqualTo("userId");
        assertThat(httpRequestObject.getRequestParams().get("password")).isEqualTo("password");
        assertThat(httpRequestObject.getRequestParams().get("name")).isEqualTo("name");
        assertThat(httpRequestObject.getRequestParams().get("email")).isEqualTo("abc@naver.com");
    }
}
