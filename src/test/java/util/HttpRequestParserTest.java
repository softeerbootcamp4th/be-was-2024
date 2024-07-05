package util;

import dto.HttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HttpRequestParser 테스트")
public class HttpRequestParserTest {

    @Test
    @DisplayName("경로 및 쿼리 파라미터 파싱 성공")
    public void testParsePathAndQueryParams() throws IOException {
        // given
        InputStream inputStream = new ByteArrayInputStream(("GET /user/create?userId=javajigi" +
                "&password=password" +
                "&name=%EB%B0%95%EC%9E%AC%EC%84%B1" +
                "&email=javajigi%40slipp.net HTTP/1.1" +
                "\r\n\r\n").getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("userId", "javajigi");
        queryParams.put("password", "password");
        queryParams.put("name", "박재성");
        queryParams.put("email", "javajigi@slipp.net");

        // when
        HttpRequest httpRequest = HttpRequestParser.parseHeader(br);

        // then
        assertThat(httpRequest.getPath().get()).isEqualTo("/user/create");
        assertThat(httpRequest.getQueryParams().get()).isEqualTo(queryParams);
    }

    @Test
    @DisplayName("경로 및 파일 형식 파싱 성공")
    public void testParsePathAndExtensionType() throws IOException {
        // given
        InputStream inputStream = new ByteArrayInputStream(("GET /register/index.html" +
                "\r\n\r\n").getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        // when
        HttpRequest httpRequest = HttpRequestParser.parseHeader(br);

        // then
        assertThat(httpRequest.getPath().get()).isEqualTo("/register/index.html");
        assertThat(httpRequest.getExtensionType().get()).isEqualTo("html");
    }
}
