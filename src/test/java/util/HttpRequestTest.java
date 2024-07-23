package util;

import exception.RequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HttpRequestTest {

    @DisplayName("from 메서드로 RequestLine을 파싱 후 HttpRequestObject를 생성하며, Optional White Space에 대해서도 문제 없이 처리한다.")
    @ParameterizedTest
    @ValueSource(strings = {"GET /index.html HTTP/1.1", "GET  /index.html HTTP/1.1", "GET     /index.html     HTTP/1.1"})
    void from(String requestLine) {
        // when
        HttpRequest request = HttpRequest.from(requestLine);

        // then
        assertThat(request.getRequestMethod()).isEqualTo("GET");
        assertThat(request.getRequestPath()).isEqualTo("/index.html");
        assertThat(request.getHttpVersion()).isEqualTo("HTTP/1.1");
    }

    @DisplayName("from 메서드로 RequestParam이 존재하는 RequestLine을 파싱 후 HttpRequestObject를 생성하며, Optional White Space에 대해서도 문제 없이 처리한다.")
    @ParameterizedTest
    @ValueSource(strings = {"GET /user/create?userId=userId&password=password&name=name&email=abc@naver.com HTTP/1.1",
            "GET      /user/create?userId=userId&password=password&name=name&email=abc@naver.com   HTTP/1.1"})
    void fromWithParams(String requestLine) {
        // when
        HttpRequest request = HttpRequest.from(requestLine);

        // then
        assertThat(request.getRequestMethod()).isEqualTo("GET");
        assertThat(request.getRequestPath()).isEqualTo("/user/create");
        assertThat(request.getHttpVersion()).isEqualTo("HTTP/1.1");
        assertThat(request.getRequestParams())
                .containsEntry(ConstantUtil.USER_ID, "userId")
                .containsEntry(ConstantUtil.PASSWORD, "password")
                .containsEntry(ConstantUtil.NAME, "name")
                .containsEntry(ConstantUtil.EMAIL, "abc@naver.com");
    }

    @DisplayName("putHeaders 메서드로 HeaderLine을 파싱하여 HttpRequestObject에 추가하며, Optional White Space에 대해서도 문제 없이 처리한다.")
    @ParameterizedTest
    @ValueSource(strings = {"Host:localhost:8080", "Host: localhost:8080", "Host:    localhost:8080", "Connection:keep-alive", "Accept:*/*"})
    void putHeadersSingle(String headerLine) {
        // given
        HttpRequest request = HttpRequest.from("GET /index.html HTTP/1.1");
        headerLine = headerLine.replaceAll(ConstantUtil.SPACES, ConstantUtil.SPACE); // remove multiple spaces
        int idx = headerLine.indexOf(ConstantUtil.COLON);
        String key = headerLine.substring(0, idx).toLowerCase().trim();
        String value = headerLine.substring(idx + 1).toLowerCase().trim();

        // when
        request.putHeaders(headerLine);

        // then
        assertThat(request.getRequestHeaders()).containsEntry(key, value);
    }

    @DisplayName("putHeaders: 여러 HeaderLine을 파싱하여 HttpRequestObject에 추가하며, Optional White Space에 대해서도 문제 없이 처리한다.")
    @ParameterizedTest
    @ValueSource(strings = {"Host:localhost:8080 \r\nConnection:keep-alive", "Host: localhost:8080 \r\nConnection:keep-alive", "Host:    localhost:8080 \r\nConnection:keep-alive"})
    void putHeadersMulti(String headerLines) {
        // given
        HttpRequest request = HttpRequest.from("GET /index.html HTTP/1.1");
        String[] headers = headerLines.toLowerCase().split(ConstantUtil.CRLF);

        // when
        for (String header : headers) {
            request.putHeaders(header);
        }

        // then
        assertThat(request.getRequestHeaders())
                .containsEntry("host", "localhost:8080")
                .containsEntry("connection", "keep-alive");
    }

    @DisplayName("putHeaders: HeaderLine에 콜론(:)이 잘못된 경우 예외가 발생해야 한다.")
    @ParameterizedTest
    @ValueSource(strings = {"Host localhost:8080", "Host localhost:8080", "Host    localhost:8080", "Host  :  loca:lhost:8080"})
    void putHeadersException(String headerLine) {
        // given
        HttpRequest request = HttpRequest.from("GET /index.html HTTP/1.1");

        // when & then
        assertThatThrownBy(() -> request.putHeaders(headerLine))
                .isInstanceOf(RequestException.class)
                .hasMessageContaining(ConstantUtil.INVALID_HEADER);
    }


    @DisplayName("putBody: RequestBody를 파싱하여 HttpRequestObject에 추가한다.")
    @ParameterizedTest
    @ValueSource(strings = {"userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net"})
    void putBody(String bodyLine) {
        // given
        HttpRequest request = HttpRequest.from("POST /user/create HTTP/1.1");
        bodyLine = URLDecoder.decode(bodyLine, StandardCharsets.UTF_8);
        List<Byte> body = new ArrayList<>();
        for (byte b : bodyLine.getBytes(StandardCharsets.UTF_8)) {
            body.add(b);
        }

        // when
        request.putBody(body);

        // then
        Map<String, String> bodyMap = request.getBodyMap();
        assertThat(bodyMap)
                .containsEntry(ConstantUtil.USER_ID, "javajigi")
                .containsEntry(ConstantUtil.PASSWORD, "password")
                .containsEntry(ConstantUtil.NAME, "박재성")
                .containsEntry(ConstantUtil.EMAIL, "javajigi@slipp.net");
    }
}
