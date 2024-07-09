package util;

import constant.HttpMethod;
import constant.MimeType;
import dto.HttpRequest;
import exception.InvalidHttpRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("HttpRequestParser 테스트")
public class HttpRequestParserTest {
    private static final String CRLF = "\r\n";
    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String USER_ID = "javajigi";
    private static final String PASSWORD = "password";
    private static final String NAME = "박재성";
    private static final String ENCODED_NAME = "%EB%B0%95%EC%9E%AC%EC%84%B1";
    private static final String ENCODED_EMAIL = "javajigi%40slipp.net";
    private static final String EMAIL = "javajigi@slipp.net";



    @Test
    @DisplayName("post 요청 파싱 성공")
    public void ParsePostRequestSuccess() throws IOException {
        // given
        InputStream inputStream = new ByteArrayInputStream((
                HttpMethod.POST.name() + " /user/create "+ HTTP_VERSION + CRLF +
                "Content-Type: " + MimeType.APPLICATION_FORM_URLENCODED.getTypeName() + CRLF +
                "Content-Length: "+ 93 + CRLF + CRLF +
                "userId=" + USER_ID +
                "&password=" + PASSWORD +
                "&name=" + ENCODED_NAME +
                "&email=" + ENCODED_EMAIL + CRLF
                ).getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        // when
        HttpRequest httpRequest = HttpRequestParser.parseHttpRequest(br);
        Map<String, String> parsedRequestBody = HttpRequestParser.parseRequestBody(httpRequest.getBody().get()
        , MimeType.APPLICATION_FORM_URLENCODED);

        // then
        assertThat(httpRequest.getHttpMethod()).isEqualTo(HttpMethod.POST);

        assertThat(httpRequest.getPath().get()).isEqualTo("/user/create");

        assertThat(httpRequest.getHeader("Content-Type").get())
                .contains(MimeType.APPLICATION_FORM_URLENCODED.getTypeName());

        assertThat(parsedRequestBody).containsEntry("userId", USER_ID)
                .containsEntry("password", PASSWORD)
                .containsEntry("name", NAME)
                .containsEntry("email", EMAIL);
    }

    @Test
    @DisplayName("정적 파일 요청 파싱 성공")
    public void ParseGetRequestSuccess() throws IOException {
        // given
        InputStream inputStream = new ByteArrayInputStream((
                HttpMethod.GET.name() + " /register/index.html " + HTTP_VERSION + CRLF + CRLF).getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        // when
        HttpRequest httpRequest = HttpRequestParser.parseHttpRequest(br);

        // then
        assertThat(httpRequest.getHttpMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getPath().get()).isEqualTo("/register/index.html");
        assertThat(httpRequest.getExtensionType().get()).isEqualTo("html");
    }

    @Test
    @DisplayName("잘못된 HttpMethod 파싱 실패")
    public void ParseWrongHttpMethodFail() {
        // given
        // 잘못된 HttpMethod
        InputStream inputStream = new ByteArrayInputStream((
                "GOOD /register/index.html " + HTTP_VERSION + CRLF + CRLF).getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        // when & then
        assertThatThrownBy(() -> HttpRequestParser.parseHttpRequest(br))
                .isInstanceOf(InvalidHttpRequestException.class)
                .hasMessage("Incorrect HttpMethod");

    }
}
