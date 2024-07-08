package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestObjectTest {

    @DisplayName("from 메서드로 RequestLine을 파싱 후 HttpRequestObject를 생성하며, Optional White Space에 대해서도 문제 없이 처리한다.")
    @ParameterizedTest
    @ValueSource(strings = {"GET /index.html HTTP/1.1", "GET  /index.html HTTP/1.1", "GET     /index.html     HTTP/1.1"})
    void fromTest(String requestLine) {
        // when
        HttpRequestObject request = HttpRequestObject.from(requestLine);

        // then
        assertThat(request.getRequestMethod()).isEqualTo("GET");
        assertThat(request.getRequestPath()).isEqualTo("/index.html");
        assertThat(request.getHttpVersion()).isEqualTo("HTTP/1.1");
    }

    @DisplayName("from 메서드로 RequestParam이 존재하는 RequestLine을 파싱 후 HttpRequestObject를 생성하며, Optional White Space에 대해서도 문제 없이 처리한다.")
    @ParameterizedTest
    @ValueSource(strings = {"GET /user/create?userId=userId&password=password&name=name&email=abc@naver.com HTTP/1.1",
        "GET      /user/create?userId=userId&password=password&name=name&email=abc@naver.com   HTTP/1.1"})
    void fromWithParamsTest(String requestLine) {
        // when
        HttpRequestObject request = HttpRequestObject.from(requestLine);

        // then
        assertThat(request.getRequestMethod()).isEqualTo("GET");
        assertThat(request.getRequestPath()).isEqualTo("/user/create");
        assertThat(request.getHttpVersion()).isEqualTo("HTTP/1.1");
        assertThat(request.getRequestParams()).containsEntry("userId", "userId");
        assertThat(request.getRequestParams()).containsEntry("password", "password");
        assertThat(request.getRequestParams()).containsEntry("name", "name");
        assertThat(request.getRequestParams()).containsEntry("email", "abc@naver.com");
    }

    @DisplayName("putHeaders 메서드로 HeaderLine을 파싱하여 HttpRequestObject에 추가하며, Optional White Space에 대해서도 문제 없이 처리한다.")
    @ParameterizedTest
    @ValueSource(strings = {"Host:localhost:8080", "Host: localhost:8080", "Host:    localhost:8080", "Connection:keep-alive", "Accept:*/*"})
    void putHeadersSingleTest(String headerLine) {
        // given
        HttpRequestObject request = HttpRequestObject.from("GET /index.html HTTP/1.1");
        headerLine = headerLine.replaceAll(StringUtil.SPACES, StringUtil.SPACE); // remove multiple spaces
        int idx = headerLine.indexOf(StringUtil.COLON);
        String key = headerLine.substring(0, idx).trim();
        String value = headerLine.substring(idx + 1).trim();

        // when
        request.putHeaders(headerLine);

        // then
        assertThat(request.getRequestHeaders()).containsEntry(key, value);
    }

    @DisplayName("putHeaders 메서드로 여러 HeaderLine을 파싱하여 HttpRequestObject에 추가하며, Optional White Space에 대해서도 문제 없이 처리한다.")
    @ParameterizedTest
    @ValueSource(strings = {"Host:localhost:8080 \r\nConnection:keep-alive", "Host: localhost:8080 \r\nConnection:keep-alive", "Host:    localhost:8080 \r\nConnection:keep-alive"})
    void putHeadersMultiTest(String headerLines){
        // given
        HttpRequestObject request = HttpRequestObject.from("GET /index.html HTTP/1.1");
        String[] headers = headerLines.split(StringUtil.CRLF);

        // when
        for (String header : headers) {
            request.putHeaders(header);
        }

        // then
        assertThat(request.getRequestHeaders()).containsEntry("Host", "localhost:8080");
        assertThat(request.getRequestHeaders()).containsEntry("Connection", "keep-alive");
    }

    @DisplayName("putBody 메서드로 Body를 파싱하여 HttpRequestObject에 추가한다.")
    @ParameterizedTest
    @ValueSource(strings = {"userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net"})
    void putBodyTest(String bodyLine) {
        // given
        HttpRequestObject request = HttpRequestObject.from("POST /user/create HTTP/1.1");
        bodyLine = bodyLine.replaceAll(StringUtil.SPACES, StringUtil.SPACE); // remove multiple spaces

        // when
        request.putBody(bodyLine);

        // then
        assertThat(request.getRequestBody()).containsEntry("userId", "javajigi");
        assertThat(request.getRequestBody()).containsEntry("password", "password");
        assertThat(request.getRequestBody()).containsEntry("name", "%EB%B0%95%EC%9E%AC%EC%84%B1");
        assertThat(request.getRequestBody()).containsEntry("email", "javajigi%40slipp.net");
    }
}
