package http;

import http.enums.HttpMethodType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyHttpRequestTest {
    @Test
    @DisplayName("정상적인 Http 요청이 오면 객체 생성 성공")
    void createValidRequestObjIfInputValid() {
        String startLine = "GET /hello/world HTTP/1.1";
        List<String> headers = List.of("Accept: text/html");

        HttpMethodType expectedMethod = HttpMethodType.GET;
        String expectedPathname = "/hello/world";
        String expectedVersion = "HTTP/1.1";
        String expectedAcceptType = "text/html";

        MyHttpRequest request = new MyHttpRequest(startLine, headers, new byte[0]);

        Assertions.assertThat(request.getMethod()).isEqualTo(expectedMethod);
        Assertions.assertThat(request.getUrl().getPathname()).isEqualTo(expectedPathname);
        Assertions.assertThat(request.getVersion()).isEqualTo(expectedVersion);
        Assertions.assertThat(request.getHeaders().getHeader("Accept")).isEqualTo(expectedAcceptType);
    }

    @Test
    @DisplayName("Http 요청 라인이 잘못되었다면 예외 발생")
    void throwIfRequestLineInvalid() {
        String startLine1 = "GET /hello/world"; // 버전이 누락됨
        String startLine2 = "CHECK /hello/world HTTP/1.1"; // 지원되지 않는 메서드

        List<String> headers = List.of("Accept: text/html");

        Assertions.assertThatThrownBy(() -> new MyHttpRequest(startLine1, headers, new byte[0]));
        Assertions.assertThatThrownBy(() -> new MyHttpRequest(startLine2, headers, new byte[0]));
    }
}