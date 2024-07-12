package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import session.Session;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class HttpResponseTest {

    @DisplayName("ok: 정상적인 응답을 생성한다.")
    @ParameterizedTest(name = "path: {0}, httpVersion: {1}, body: {2}")
    @CsvSource(value = {
            "/css/styles.css, HTTP/1.1, body",
            "/login, HTTP/1.1, body",
            "/index, HTTP/1.1, body",
            "/images/logo.png, HTTP/1.1, image data",
            "/js/app.js, HTTP/1.1, console.log('test');",
            "/favicon.ico, HTTP/1.1, binary data",
            "/api/data, HTTP/1.1, {'key':'value'}",
            "/fonts/roboto.woff2, HTTP/1.1, binary data",
            "/submit, HTTP/1.1, name=value&anothername=anothervalue",
            "/, HTTP/1.1, <html><body>Welcome</body></html>"
    })
    void ok(String path, String httpVersion, String body) {
        // when
        HttpResponse response = HttpResponse.ok(path, httpVersion, body);

        // then
        assertThat(response.getType()).isEqualTo(ConstantUtil.DYNAMIC);
        assertThat(response.getPath()).isEqualTo(path);
        assertThat(response.getStatusCode()).isEqualTo(HttpCode.OK.getStatus());
        assertThat(response.getHttpVersion()).isEqualTo(httpVersion);
        assertThat(response.getBody()).isEqualTo(body.getBytes(StandardCharsets.UTF_8));
        assertThat(response.getTotalHeaders()).contains(ConstantUtil.CONTENT_LENGTH);
        assertThat(response.getTotalHeaders()).contains(ConstantUtil.CONTENT_TYPE);
    }

    @DisplayName("okStatic: 정적 자원 요청에 대한 응답을 생성한다.")
    @ParameterizedTest(name = "path: {0}, httpVersion: {1}")
    @CsvSource(value = {"/css/styles.css, HTTP/1.1", "/img/logo.png, HTTP/1.1"})
    void okStatic(String path, String httpVersion) {
        // when
        HttpResponse response = HttpResponse.okStatic(path, httpVersion);

        // then
        assertThat(response.getType()).isEqualTo(ConstantUtil.STATIC);
        assertThat(response.getPath()).isEqualTo(path);
        assertThat(response.getStatusCode()).isEqualTo(HttpCode.OK.getStatus());
        assertThat(response.getHttpVersion()).isEqualTo(httpVersion);
        assertThat(response.getBody()).isNull();
    }

    @DisplayName("error: 에러 응답을 생성한다.")
    @ParameterizedTest(name = "statusCode: {0}, httpVersion: {1}")
    @CsvSource(value = {"400, HTTP/1.1", "404, HTTP/1.1", "500, HTTP/1.1"})
    void error(String statusCode, String httpVersion) {
        // when
        HttpResponse response = HttpResponse.error(statusCode, httpVersion);

        // then
        assertThat(response.getType()).isEqualTo(ConstantUtil.FAULT);
        assertThat(response.getPath()).isNull();
        assertThat(response.getStatusCode()).isEqualTo(statusCode);
        assertThat(response.getHttpVersion()).isEqualTo(httpVersion);
        assertThat(response.getBody()).isNull();
    }

    @DisplayName("redirect: 리다이렉트 응답을 생성한다.")
    @ParameterizedTest(name = "path: {0}, httpVersion: {1}")
    @CsvSource(value = {"/login, HTTP/1.1", "/index, HTTP/1.1"})
    void redirect(String path, String httpVersion) {
        // when
        HttpResponse response = HttpResponse.redirect(path, httpVersion);

        // then
        assertThat(response.getType()).isEqualTo(ConstantUtil.DYNAMIC);
        assertThat(response.getPath()).isEqualTo(path);
        assertThat(response.getStatusCode()).isEqualTo(HttpCode.FOUND.getStatus());
        assertThat(response.getHttpVersion()).isEqualTo(httpVersion);
        assertThat(response.getBody()).isNull();
        assertThat(response.getTotalHeaders()).contains(ConstantUtil.LOCATION);
    }

    @DisplayName("setSessionId: 세션 생성을 위해 Set-Cookie 헤더에 sessionId를 설정한다.")
    @ParameterizedTest(name = "sessionId: {0}")
    @CsvSource(value = {"dajwkld", "dwkdlwl", "2dkljwdkld", "dwlkdwkldw"})
    void setSessionId(String sessionId) {
        // given
        Session session = new Session(sessionId, "userId", LocalDateTime.now(ZoneId.of("GMT")));
        HttpResponse response = new HttpResponse();

        // when
        response.setSessionId(session.toString());

        // then
        assertThat(response.getTotalHeaders()).contains(ConstantUtil.SET_COOKIE);
        assertThat(response.getTotalHeaders()).contains(session.toString());
    }

    @DisplayName("deleteSessionId: 세션 삭제를 위해 Max-Age 값을 0으로 설정한다.")
    @ParameterizedTest(name = "sessionId: {0}")
    @CsvSource(value = {"dajwkld", "dwkdlwl", "2dkljwdkld", "dwlkdwkldw"})
    void deleteSessionId(String sessionId) {
        // given
        HttpResponse response = new HttpResponse();

        // when
        response.deleteSessionId(sessionId);

        // then
        assertThat(response.getTotalHeaders()).contains(ConstantUtil.SET_COOKIE);
        assertThat(response.getTotalHeaders()).contains(sessionId);
        assertThat(response.getTotalHeaders()).contains("Max-Age=0");
    }
}
